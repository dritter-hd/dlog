package com.github.dritter.hd.dlog.internal;

import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.Parameter;
import com.github.dritter.hd.dlog.Predicate;

import java.util.Collections;
import java.util.List;

public class Table extends AbstractOperator {
    private Literal edbLiteral;

    public Table(final Literal literal) {
        this.edbLiteral = literal;
    }

    @Override
    public final List<Operator> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public final List<Parameter<?>> getFreeVariables() {
        return this.edbLiteral.getParameters();
    }

    public final Predicate getPredicate() {
        return this.edbLiteral.getPredicate();
    }

    @Override
    public final int getArity() {
        return this.edbLiteral.getPredicate().getArity();
    }

    @Override
    void appendMembers(StringBuilder sb, int indent) {
        indent(sb, indent);
        sb.append(edbLiteral.toString()).append('\n');
    }
}
