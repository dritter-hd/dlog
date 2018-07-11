package dlog.internal;

import dlog.Parameter;

import java.util.ArrayList;
import java.util.List;

public class NaryOperator extends AbstractOperator {

    protected List<Operator> children;

    protected NaryOperator(final List<Operator> children) {
        this.children = children;
    }

    @Override
    public final List<Operator> getChildren() {
        return this.children;
    }

    @Override
    public List<Parameter<?>> getFreeVariables() {
        final List<Parameter<?>> v = new ArrayList<Parameter<?>>();
        for (Operator op : this.children) {
            v.addAll(op.getFreeVariables());
        }
        return v;
    }

    @Override
    public int getArity() {
        int arity = 0;
        for (Operator child : this.children) {
            arity += child.getArity();
        }
        return arity;
    }

    @Override
    void appendMembers(StringBuilder sb, int indent) {
        appendAdditionalMembers(sb, indent);
        appendChildren(sb, indent);
    }

    void appendAdditionalMembers(StringBuilder sb, int indent) {
    }

    private void appendChildren(StringBuilder sb, final int indent) {
        for (Operator child : children) {
            if (child instanceof AbstractOperator) {
                ((AbstractOperator) child).append(sb, indent);
            } else {
                indent(sb, indent);
                sb.append(child.getClass().getSimpleName()).append(" (external)\n");
            }
        }
    }
}
