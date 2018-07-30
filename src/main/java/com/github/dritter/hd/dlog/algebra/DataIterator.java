package com.github.dritter.hd.dlog.algebra;


public interface DataIterator {
    void open();

    ParameterValue<?>[] next();

    void close();
}
