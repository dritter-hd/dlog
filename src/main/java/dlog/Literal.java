package dlog;

import java.util.ArrayList;
import java.util.List;

// TODO: rename atomic formula
public class Literal {

    private Predicate predicate;
    private List<Parameter<?>> parameters;

    public Literal() {
        this.parameters = new ArrayList<Parameter<?>>();
    }

    public static Literal create(final Predicate pred, final Parameter<?>... params) {
        final Literal l = new Literal();
        l.predicate = pred;
        for (Parameter<?> param : params) {
            l.parameters.add(param);
        }
        return l;
    }

    public static Literal create(final Predicate pred, final List<Parameter<?>> params) {
        final Literal l = new Literal();
        l.predicate = pred;
        l.parameters = params;

        return l;
    }

    public final List<Parameter<?>> getParameters() {
        return this.parameters;
    }

    public final Predicate getPredicate() {
        return this.predicate;
    }

    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        if (BuiltInPredicates.isBuiltIn(this.predicate)) {
            sb.append(this.parameters.get(0));
            sb.append(" ");
            sb.append(this.predicate.getName());
            sb.append(" ");
            sb.append(this.parameters.get(1));
        } else {
            sb.append(this.predicate.getName());
            sb.append("(");
            final int size = this.parameters.size() - 1;
            for (int i = 0; i < size; ++i) {
                sb.append(this.parameters.get(i).toString());
                sb.append(", ");
            }
            sb.append(this.parameters.get(size));
            sb.append(")");
        }

        return sb.toString();
    }
}
