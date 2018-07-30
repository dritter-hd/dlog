package com.github.dritter.hd.dlog.internal;

import java.util.List;

public class UnionAll extends NaryOperator {

    public UnionAll(final List<Operator> children) {
        super(children);
    }

    @Override
    public final int getArity() {
        return children.get(0).getArity();
    }
}
