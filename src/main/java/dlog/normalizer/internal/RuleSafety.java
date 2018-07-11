package dlog.normalizer.internal;

import dlog.IRule;

public interface RuleSafety {
    public static final String UNLIMITED = "unlimited";

    public abstract IRule process(IRule rule);

}