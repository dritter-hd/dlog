package com.github.dritter.hd.dlog.normalizer.internal;

import com.github.dritter.hd.dlog.IRule;

import java.util.Collection;

public interface INormalization {
    /**
     * 
     * @param rules
     * @return
     */
    Collection<IRule> rectify(Collection<IRule> rules);
}
