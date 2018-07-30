package com.github.dritter.hd.dlog.compiler.internal;

public abstract class AbstractOperator implements Operator {

    AbstractOperator() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        append(sb, 0);
        return sb.toString();
    }

    void append(StringBuilder sb, int indent) {
        indent(sb, indent);
        sb.append(getClass().getSimpleName()).append(" {").append('\n');
        appendMembers(sb, indent + 2);
        indent(sb, indent);
        sb.append('}').append('\n');
    }

    abstract void appendMembers(StringBuilder sb, int indent);

    void indent(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; ++i) {
            sb.append(' ');
        }
    }
}
