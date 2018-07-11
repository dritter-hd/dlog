package dlog.internal;

import dlog.*;

import java.util.*;

public class Rectifier implements INormalization {

    private int counter = 0;

    public Rectifier() {
    }

    @Override
    public final Collection<IRule> rectify(final Collection<IRule> unrectRules) {
        if (unrectRules == null) {
            throw new IllegalArgumentException("Rules must not be null");
        }

        final Collection<IRule> rectRules = new ArrayList<IRule>(unrectRules.size());

        for (List<IRule> rules : Util.clusterRules(unrectRules).values()) {
            final Literal newHead = extractNewHead(rules);

            for (IRule rule : rules) {
                final Rule r = rectifyRule(newHead, rule);
                rectRules.add(r);
            }
        }
        return rectRules;
    }

    /**
     * 
     * @param newHead
     * @param rule
     * @return
     */
    private Rule rectifyRule(final Literal newHead, final IRule rule) {
        HashMap<Parameter<?>, Parameter<?>> oldNewVariable = new HashMap<Parameter<?>, Parameter<?>>();
        final Set<Parameter<?>> usedInBody = new HashSet<Parameter<?>>();
        final List<Literal> builtInLiterals = new ArrayList<Literal>();

        this.processHeadMapping(oldNewVariable, builtInLiterals, rule.getHead().getParameters(), newHead.getParameters());

        final List<Literal> oldBody = rule.getBody();
        final List<Literal> newBody = new ArrayList<Literal>();

        for (Literal l : oldBody) {
            final List<Parameter<?>> oldLiteralParams = l.getParameters();
            final List<Parameter<?>> newLiteralParams = new ArrayList<Parameter<?>>();

            final Predicate predicate = l.getPredicate();

            if (BuiltInPredicates.isBuiltIn(predicate)) {
                this.processBuiltIn(oldNewVariable, builtInLiterals, oldLiteralParams, predicate);
            } else {
                this.processBodyMapping(oldNewVariable, usedInBody, builtInLiterals, oldLiteralParams, newLiteralParams);

                newBody.add(Literal.create(predicate, newLiteralParams));
            }
        }
        newBody.addAll(builtInLiterals);

        final Rule r = Rule.create(newHead, newBody);
        return r;
    }

    /**
     * 
     * @param rules
     * @return
     */
    private Literal extractNewHead(final List<IRule> rules) {
        final Predicate predicate = rules.get(0).getHead().getPredicate();
        final int arity = predicate.getArity();
        final List<Parameter<?>> params = new ArrayList<Parameter<?>>();

        for (int i = 0; i < arity; i++) {
            params.add(Parameter.createVariable("_" + predicate.getName() + i));
        }
        final Literal propHead = Literal.create(predicate, params);
        return propHead;
    }

    /**
     * 
     * @param map
     * @param builtInLiterals
     * @param oldBodyParams
     * @param predicate
     */
    private void processBuiltIn(final HashMap<Parameter<?>, Parameter<?>> map, final List<Literal> builtInLiterals,
            final List<Parameter<?>> oldBodyParams, final Predicate predicate) {
        if (oldBodyParams.size() != 2) {
            throw new IllegalArgumentException("Built-In arity violation. Arity must be exactly two.");
        }

        final List<Parameter<?>> newBodyParams = new ArrayList<Parameter<?>>();

        for (Parameter<?> p : oldBodyParams) {
            if (Parameter.Kind.CONSTANT.equals(p.getKind())) {
                newBodyParams.add(p);
            } else {
                if (map.containsKey(p)) {
                    newBodyParams.add(map.get(p));
                } else {
                    final Parameter<?> var = createNewVariableFor(p);
                    map.put(p, var);
                    newBodyParams.add(var);
                }
            }
        }
        builtInLiterals.add(Literal.create(predicate, newBodyParams.get(0), newBodyParams.get(1)));
    }

    /**
     * 
     * @param map
     * @param builtInLiterals
     * @param oldLiteralParams
     * @param newLiteralParams
     * @param predicateName
     */
    private void processBodyMapping(HashMap<Parameter<?>, Parameter<?>> map, final Set<Parameter<?>> usedInBody,
            final List<Literal> builtInLiterals, final List<Parameter<?>> oldLiteralParams, final List<Parameter<?>> newLiteralParams) {

        for (Parameter<?> p : oldLiteralParams) {
            if (Parameter.Kind.CONSTANT.equals(p.getKind())) {
                if (map.containsKey(p)) {
                    newLiteralParams.add(map.get(p));
                } else {
                    final Parameter<?> var = createNewVariableFor(p);
                    newLiteralParams.add(var);
                    map.put(p, var);
                    builtInLiterals.add(Literal.create(BuiltInPredicates.EQUALS, var, p));
                }
            } else {
                if (map.containsKey(p)) {
                    if (usedInBody.contains(p)) {
                        final Parameter<?> var = createNewVariableFor(p);
                        newLiteralParams.add(var);
                        builtInLiterals.add(Literal.create(BuiltInPredicates.EQUALS, map.get(p), var));
                    } else {
                        newLiteralParams.add(map.get(p));
                        usedInBody.add(p);
                    }
                } else {
                    final Parameter<?> var = createNewVariableFor(p);
                    newLiteralParams.add(var);
                    map.put(p, var);
                    usedInBody.add(p);
                }
            }
        }
    }

    private Parameter<?> createNewVariableFor(Parameter<?> p) {
        return Parameter.createVariable(p.getValue() + "_" + ++counter);
    }

    /**
     * 
     * @param map
     * @param builtInLiterals
     * @param oldHeadParams
     * @param newHeadParams
     */
    private void processHeadMapping(final HashMap<Parameter<?>, Parameter<?>> map, final List<Literal> builtInLiterals,
            final List<Parameter<?>> oldHeadParams, final List<Parameter<?>> newHeadParams) {

        for (int i = 0; i < oldHeadParams.size(); ++i) {
            Parameter<?> p = oldHeadParams.get(i);

            if (Parameter.Kind.CONSTANT.equals(p.getKind())) {
                builtInLiterals.add(Literal.create(BuiltInPredicates.EQUALS, p, newHeadParams.get(i)));
            } else {
                if (map.containsKey(p)) {
                    builtInLiterals.add(Literal.create(BuiltInPredicates.EQUALS, map.get(p), newHeadParams.get(i)));
                } else {
                    map.put(p, newHeadParams.get(i));
                }
            }
        }
    }
}