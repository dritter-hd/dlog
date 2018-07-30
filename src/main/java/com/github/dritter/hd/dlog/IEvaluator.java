package com.github.dritter.hd.dlog;

import java.util.Collection;

public interface IEvaluator {
    /**
     * @param facts
     * @return
     */
    Collection<IFacts> eval(Collection<IFacts> facts);
}