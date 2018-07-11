package dlog.algebra;


public interface DataIterator {
    void open();

    ParameterValue<?>[] next();

    void close();
}
