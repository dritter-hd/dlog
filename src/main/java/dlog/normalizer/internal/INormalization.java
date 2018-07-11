package dlog.normalizer.internal;

import java.util.Collection;

import dlog.IRule;

public interface INormalization {
    /**
     * 
     * @param rules
     * @return
     */
    Collection<IRule> rectify(Collection<IRule> rules);
}
