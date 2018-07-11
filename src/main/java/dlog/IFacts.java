package dlog;

import dlog.algebra.DataIterator;

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