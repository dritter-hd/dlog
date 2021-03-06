package com.github.dritter.hd.dlog.normalizer.internal;

import com.github.dritter.hd.dlog.IRule;

public interface RuleSafety {
    public static final String UNLIMITED = "unlimited";

    public abstract IRule process(IRule rule);

}