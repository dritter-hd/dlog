package com.github.dritter.hd.dlog.internal;

import com.github.dritter.hd.dlog.*;
import com.github.dritter.hd.dlog.algebra.ParameterValue;

import java.util.*;

public class Compiler {
    private Compiler() {
    }

    public static Operator compileRectifiedRules(final List<IRule> rules) {
        if (rules.size() == 1) {
            return compileSingleRule(rules.get(0));
        } else {
            final List<Operator> children = new ArrayList<Operator>(rules.size());
            for (IRule rule : rules) {
                children.add(compileSingleRule(rule));
            }
            return new UnionAll(children);
        }
    }

    private static Operator compileSingleRule(final IRule rule) {
        final CompilationContext cc = new CompilationContext();
        Operator ctPlan = buildPlanForBody(rule, cc);
        ctPlan = pushDownBuiltInPredicates(ctPlan, cc); // TODO: insert strategy
                                                        // for insert built-ins
                                                        // after variable
                                                        // appearance
        ctPlan = projectRequiredColumns(rule, ctPlan, cc);
        return ctPlan;
    }

    private static Operator buildPlanForBody(final IRule rule, final CompilationContext cc) {
        Operator ctPlan = null;

        HashMap<Parameter<?>, Operator> operatorMap = new HashMap<Parameter<?>, Operator>();
        List<Literal> builtIns = new ArrayList<Literal>();

        for (Literal l : rule.getBody()) {
            final Predicate pred = l.getPredicate();
            if (BuiltInPredicates.isBuiltIn(pred)) {
                builtIns.add(l);
                for (Parameter<?> param : l.getParameters()) {
                    cc.put(param, l);
                }
            } else {
                ctPlan = new Table(l);
                for (Parameter<?> param : l.getParameters()) {
                    operatorMap.put(param, ctPlan);
                }
            }
        }
        for (Literal l : builtIns) {
            if (l.getParameters().size() == 2) {
                final Parameter<?> left = l.getParameters().get(0);
                final Parameter<?> right = l.getParameters().get(1);
                if (left.getKind() == Parameter.Kind.VARIABLE && right.getKind() == Parameter.Kind.VARIABLE) {
                    final Operator opLeft = operatorMap.get(left);
                    final Operator opRight = operatorMap.get(right);
                    if (opLeft != null && opRight != null && opLeft != opRight) {
                        final XProduct xProduct = new XProduct(opLeft, opRight);
                        replaceOperator(operatorMap, opLeft, xProduct);
                        replaceOperator(operatorMap, opRight, xProduct);
                        ctPlan = xProduct;
                    }
                }
            }
        }
        Operator interimPlan = ctPlan;
        for (Operator operator : new HashSet<Operator>(operatorMap.values())) {
            if (operator != interimPlan) {
                XProduct xProduct = new XProduct(ctPlan, operator);
                ctPlan = xProduct;
            }
        }
        if (ctPlan == null) {
            throw new IllegalStateException();
        }
        return ctPlan;
    }

    private static void replaceOperator(HashMap<Parameter<?>, Operator> operatorMap, final Operator opLeft, final XProduct xProduct) {
        for (final Parameter<?> p : opLeft.getFreeVariables()) {
            operatorMap.put(p, xProduct);
        }
    }

    private static Operator pushDownBuiltInPredicates(final Operator root, final CompilationContext cc) {
        final List<Operator> children = root.getChildren();

        for (int i = 0; i < children.size(); ++i) {
            final Operator r = pushDownBuiltInPredicates(children.get(i), cc);
            children.set(i, r);
        }

        final List<Literal> literals = getApplicableBuiltInLiterals(root.getFreeVariables(), cc);

        if (literals.isEmpty()) {
            return root;
        }
        if (root instanceof XProduct) {
            return new Join(children, literals);
        }
        return new Selection(root, literals);
    }

    private static List<Literal> getApplicableBuiltInLiterals(final List<Parameter<?>> freeVaribles, final CompilationContext cc) {
        final List<Literal> result = new ArrayList<Literal>();

        for (Parameter<?> parameter : freeVaribles) {
            final List<Literal> builtInLiterals = cc.get(parameter);
            for (Literal literal : builtInLiterals) {
                if (freeVaribles.containsAll(extractVariableParameters(literal))) {
                    if (!result.contains(literal)) {
                        result.add(literal);
                    }
                }
            }
        }
        for (Literal lit : result) {
            for (Parameter<?> param : lit.getParameters()) {
                cc.remove(param, lit);
            }
        }
        return result;
    }

    private static Set<Parameter<?>> extractVariableParameters(final Literal literal) {
        final Set<Parameter<?>> variables = new HashSet<Parameter<?>>();
        for (Parameter<?> p : literal.getParameters()) {
            if (p.getKind() == Parameter.Kind.VARIABLE) {
                variables.add(p);
            }
        }
        return variables;
    }

    private static Operator projectRequiredColumns(final IRule rule, final Operator ctPlan, final CompilationContext cc) {
        final Literal head = rule.getHead();
        final int arity = head.getPredicate().getArity();
        final int[] columns = new int[arity];
        final ParameterValue<?>[] constants = new ParameterValue<?>[arity];
        cc.optimize(ctPlan.getFreeVariables());
        for (int i = 0; i < columns.length; ++i) {
            final Parameter<?> parameter = head.getParameters().get(i);
            final int col = getColumnOfParameterInPlan(ctPlan, parameter);

            columns[i] = col;
            if (col < 0) {
                final List<Literal> list = cc.get(parameter);

                if (list.size() != 1) {
                    throw new IllegalStateException();
                }

                if (!BuiltInPredicates.EQUALS.equals(list.get(0).getPredicate())) {
                    throw new IllegalStateException();
                }

                for (Parameter<?> p : list.get(0).getParameters()) {
                    if (Parameter.Kind.CONSTANT == p.getKind()) {
                        constants[i] = p.getParameterValue();
                        break;
                    } else if (!p.equals(parameter)) {
                        columns[i] = getColumnOfParameterInPlan(ctPlan, p);
                        break;
                    }
                }
            }
        }
        return new Projection(ctPlan, columns, constants);
    }

    private static int getColumnOfParameterInPlan(final Operator ctPlan, final Parameter<?> parameter) {
        return ctPlan.getFreeVariables().indexOf(parameter);
    }
}
