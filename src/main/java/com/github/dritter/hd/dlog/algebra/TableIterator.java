package com.github.dritter.hd.dlog.algebra;

public class TableIterator implements DataIterator {

    private ParameterValue<?>[][] values;
    private int pos;

    public TableIterator(final ParameterValue<?>[][] values) {
        this.values = values;
    }

    @Override
    public final void open() {
        this.pos = 0;
    }

    @Override
    public final ParameterValue<?>[] next() {
        if (this.pos < this.values.length) {
            return this.values[this.pos++];
        }
        return null;
    }

    @Override
    public void close() {
    }
}
