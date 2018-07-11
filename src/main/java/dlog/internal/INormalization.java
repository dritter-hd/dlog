package dlog.internal;

import dlog.IRule;

import java.util.Collection;

public interface INormalization {
    /**
     * 
     * @param rules
     * @return
     */
    Collection<IRule> rectify(Collection<IRule> rules);
}
