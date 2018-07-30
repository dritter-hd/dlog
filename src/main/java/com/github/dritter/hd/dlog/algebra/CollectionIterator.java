package com.github.dritter.hd.dlog.algebra;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CollectionIterator implements DataIterator {
    protected Collection<ParameterValue<?>[]> facts;
    private ThreadLocal<Iterator<ParameterValue<?>[]>> iter;

    public CollectionIterator(final Collection<ParameterValue<?>[]> facts) {
        this.facts = facts;
    }

    @Override
    public final void open() {
        this.iter = new ThreadLocal<Iterator<ParameterValue<?>[]>>();
        this.iter.set(this.facts.iterator());
        //TODO: this.iter = this.facts.iterator()
    }

    @Override
    public final ParameterValue<?>[] next() {
        return this.iter.get().hasNext() ? this.iter.get().next() : null;
    }

    @Override
    public final void close() {
        this.iter = null;
    }

    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        for (ParameterValue<?>[] tuple : this.facts) {
            sb.append('(');
            final Iterator<ParameterValue<?>> it = Arrays.asList(tuple).iterator();
            if (it.hasNext()) {
                sb.append('\'').append(it.next()).append('\'');
                while (it.hasNext()) {
                    sb.append(", ").append('\'');
                    sb.append(it.next()).append('\'');
                }
            }
            sb.append(")\n");
        }
        return sb.toString();
    }
}
