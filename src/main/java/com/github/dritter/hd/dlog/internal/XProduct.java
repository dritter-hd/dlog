package com.github.dritter.hd.dlog.internal;

import java.util.Arrays;

public class XProduct extends NaryOperator {

    public XProduct(final Operator left, final Operator right) {
        super(Arrays.asList(new Operator[] { left, right }));
    }
}
