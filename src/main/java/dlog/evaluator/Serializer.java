package dlog.evaluator;

import java.util.Iterator;
import java.util.List;

import dlog.algebra.ParameterValue;

public final class Serializer {
    private Serializer() {
    }

    public static String asString(IFacts ifacts) {
        if (ifacts == null) {
            throw new IllegalArgumentException("IFacts are null.");
        }

        final StringBuilder sb = new StringBuilder();
        for (final List<ParameterValue<?>> value : ifacts.getValues()) {
            if (ifacts.getPredicate() == null) {
                throw new IllegalArgumentException("IFacts Predicate is null.");
            }
            sb.append(ifacts.getPredicate()).append('(');

            if (value == null) {
                throw new IllegalArgumentException("IFacts values are null.");
            }
            for (final Iterator<ParameterValue<?>> its = value.iterator(); its.hasNext();) {
                final ParameterValue<?> parameterValue = its.next();
                if (parameterValue.get().getClass().getSimpleName().equals("String")) {
                    sb.append('\"').append(parameterValue).append('\"');
                } else {
                    sb.append(parameterValue);
                }
                if (its.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append(").\n");
        }
        return sb.toString();
    }
}