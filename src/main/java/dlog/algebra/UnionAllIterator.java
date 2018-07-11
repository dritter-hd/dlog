package dlog.algebra;

import java.util.Iterator;
import java.util.List;

public class UnionAllIterator implements DataIterator {
    private List<DataIterator> children;
    private Iterator<DataIterator> curIter;
    private DataIterator current;

    public UnionAllIterator(final List<DataIterator> children) {
        this.children = children;
    }

    @Override
    public final void open() {
        this.curIter = this.children.iterator();
        if (this.curIter.hasNext()) {
            this.current = this.curIter.next();
        }

        for (DataIterator c : this.children) {
            c.open();
        }
    }

    @Override
    public final ParameterValue<?>[] next() {
        if (this.current == null) {
            return null;
        }
        ParameterValue<?>[] result = this.current.next();

        while (result == null) {
            if (!this.curIter.hasNext()) {
                return null;
            }
            this.current = this.curIter.next();
            result = this.current.next();
        }
        return result;
    }

    @Override
    public final void close() {
        for (DataIterator c : this.children) {
            c.close();
        }
    }
}
