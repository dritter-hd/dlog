package com.github.dritter.hd.dlog.algebra;

import java.util.Collections;
import java.util.List;

public final class ProjectionIterator implements DataIterator {
    private List<DataIterator> children;
    private int[] columns;
    private ParameterValue<?>[] constants;

    public ProjectionIterator(final DataIterator input, final int[] columns, final ParameterValue<?>[] constants) {
        this.children = Collections.singletonList(input);
        this.columns = columns;
        this.constants = constants;
    }

    @Override
    public void open() {
        this.getInput().open();
    }

    @Override
    public ParameterValue<?>[] next() {
        final ParameterValue<?>[] tuple = this.getInput().next();
        if (tuple != null) {
            final int length = this.columns.length;
            final ParameterValue<?>[] result = new ParameterValue<?>[length];
            for (int i = 0; i < length; ++i) {
                if (this.columns[i] == -1) {
                    result[i] = this.constants[i];
                } else {
                    result[i] = tuple[this.columns[i]];
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public void close() {
        this.getInput().close();
    }

    private DataIterator getInput() {
        return this.children.get(0);
    }
}
