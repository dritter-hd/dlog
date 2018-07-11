package dlog;

import java.util.Arrays;
import java.util.Iterator;

import dlog.algebra.DataIterator;
import dlog.algebra.ParameterValue;

public class Facts implements IFacts {

    private static final String QUOTE = "\"";

    private Predicate predicate;
    private DataIterator values;

    public static Facts create(final Predicate p, final DataIterator r) {
        final Facts f = new Facts();
        f.predicate = p;
        f.values = r;
        return f;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IFacts#getPredicate()
     */
    public final Predicate getPredicate() {
        return this.predicate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IFacts#getValues()
     */
    public final DataIterator getValues() {
        return this.values;
    }

    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        this.values.open();
        ParameterValue<?>[] tuple;
        while ((tuple = this.values.next()) != null) {
            sb.append(this.predicate.getName());
            sb.append('(');
            final Iterator<ParameterValue<?>> it = Arrays.asList(tuple).iterator();
            if (it.hasNext()) {
                sb.append(quote(it.next()));
                while (it.hasNext()) {
                    sb.append(", ").append(quote(it.next()));
                }
            }
            sb.append(')').append('.').append(' ');
        }
        return sb.toString();
    }

    private String quote(final ParameterValue<?> content) {
        return QUOTE + content.get() + QUOTE;
    }
}
