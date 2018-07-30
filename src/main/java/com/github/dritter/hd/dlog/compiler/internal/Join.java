package com.github.dritter.hd.dlog.compiler.internal;

import com.github.dritter.hd.dlog.Literal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Join extends NaryOperator {

    private List<Literal> literals;

    public Join(final Operator left, final Operator right, final List<Literal> literals) {
        this(Arrays.asList(new Operator[] { left, right }), literals);
    }

    public Join(final Operator left, final Operator right, final Literal literal) {
        this(Arrays.asList(new Operator[] { left, right }), Collections.singletonList(literal));
    }

    public Join(final List<Operator> children, final List<Literal> literals) {
        super(children);
        this.literals = literals;
    }

    public final List<Literal> getLiterals() {
        return this.literals;
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
