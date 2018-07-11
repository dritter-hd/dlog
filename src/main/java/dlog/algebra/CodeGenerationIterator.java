package dlog.algebra;

public class CodeGenerationIterator implements DataIterator {
    private DataIterator di;

    @Override
    public final void open() {
        this.di.open();
    }

    @Override
    public final ParameterValue<?>[] next() {
        return this.di.next();
    }

    @Override
    public final void close() {
        this.di.close();
    }

    public final void bind(final DataIterator di) {
        this.di = di;
    }
}
