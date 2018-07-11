package dlog.compiler.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dlog.BuiltInPredicates;
import dlog.Literal;
import dlog.Parameter;

class CompilationContext {

    private Map<Parameter<?>, List<Literal>> builtIn;

    CompilationContext() {
        this.builtIn = new HashMap<Parameter<?>, List<Literal>>();
    }

    void put(final Parameter<?> p, final Literal l) {
        if (this.builtIn.containsKey(p)) {
            final List<Literal> list = get(p);
            if (!list.contains(l)) {
                list.add(l);
            }
        } else {
            final List<Literal> literals = new ArrayList<Literal>();
            literals.add(l);
            this.builtIn.put(p, literals);
        }
    }

    void remove(final Parameter<?> p, final Literal l) {
        if (this.builtIn.containsKey(p)) {
            final List<Literal> list = get(p);
            list.remove(l);

            if (list.size() == 0) {
                this.builtIn.remove(p);
            }
        } else {
            throw new IllegalStateException();
        }
    }

    List<Literal> get(final Parameter<?> parameter) {
        final List<Literal> result = this.builtIn.get(parameter);

        if (result != null) {
            return result;
        }
        return Collections.emptyList();
    }
    
    public void optimize(List<Parameter<?>> varsInOrdinaryPredicates) {
        Map<Parameter<?>, List<Literal>> varsInBuiltInPredicates = new HashMap<Parameter<?>, List<Literal>>();
        int size;
        do {
            size = varsInBuiltInPredicates.size();
            for (Map.Entry<Parameter<?>, List<Literal>> entry : this.builtIn.entrySet()) {
                Parameter<?> parameter = entry.getKey();
                if (!varsInOrdinaryPredicates.contains(parameter)) {
                    for (Literal literal : entry.getValue()) {
                        List<Parameter<?>> parameters = literal.getParameters();
                        Parameter<?> leftParameter = parameters.get(0);
                        Parameter<?> rightParameter = parameters.get(1);
                        this.processLiteral(leftParameter, rightParameter, varsInOrdinaryPredicates, varsInBuiltInPredicates);
                        this.processLiteral(rightParameter, leftParameter, varsInOrdinaryPredicates, varsInBuiltInPredicates);
                    }
                }
            }
        } while (varsInBuiltInPredicates.size() != size);
        this.builtIn = varsInBuiltInPredicates;
    }
    
    private void processLiteral(Parameter<?> leftParameter, Parameter<?> rightParameter, List<Parameter<?>> varsInOrdinaryPredicates, Map<Parameter<?>, List<Literal>> varsInBuiltInPredicates) {
        if (!varsInOrdinaryPredicates.contains(leftParameter)) {
            if (varsInOrdinaryPredicates.contains(rightParameter)) {
                Literal literal = Literal.create(BuiltInPredicates.EQUALS, leftParameter, rightParameter);
                varsInBuiltInPredicates.put(leftParameter, Collections.singletonList(literal));
            } else if (rightParameter.getKind() == Parameter.Kind.CONSTANT) {
                Literal literal = Literal.create(BuiltInPredicates.EQUALS, leftParameter, rightParameter);
                varsInBuiltInPredicates.put(leftParameter, Collections.singletonList(literal));
            } else if (varsInBuiltInPredicates.containsKey(rightParameter)) {
                Literal literal = Literal.create(BuiltInPredicates.EQUALS, leftParameter, varsInBuiltInPredicates.get(rightParameter).get(0).getParameters().get(1));
                varsInBuiltInPredicates.put(leftParameter, Collections.singletonList(literal));
            }
        }
    }
}
