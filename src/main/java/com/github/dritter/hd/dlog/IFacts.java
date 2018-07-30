package com.github.dritter.hd.dlog;

import com.github.dritter.hd.dlog.algebra.DataIterator;

public interface IFacts {

    /**
     * @return the predicate
     */
    Predicate getPredicate();

    /**
     * @return the values
     */
    DataIterator getValues();

}