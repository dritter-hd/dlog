package com.github.dritter.hd.dlog.compiler.internal;

import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.Parameter;

import java.util.Collections;
import java.util.List;

public final class Selection extends NaryOperator {
    private List<Literal> literals;

    public Selection(final Operator input, final Literal literal) {
        this(input, Collections.singletonList(literal));
    }

    public Selection(final Operator input, final List<Literal> literals) {
        super(Util.singletonList(input));
        this.literals = literals;
    }

    public List<Literal> getLiterals() {
        return this.literals;
    }

    @Override
    public List<Parameter<?>> getFreeVariables() {
        return children.get(0).getFreeVariables();
    }

    @Override
    public int getArity() {
        return children.get(0).getArity();
    }

    @Override
    void appendAdditionalMembers(StringBuilder sb, int indent) {
        indent(sb, indent);
        for (Literal l : literals) {
            sb.append(l.toString()).append(' ');
        }
        sb.append('\n');
    }
}
