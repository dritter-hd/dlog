package dlog.normalizer.internal;

import java.util.List;

import dlog.Parameter;

public interface IRuleSafetyValidator {

    /**
     * @return List<Parameter>
     */
    public abstract List<Parameter<?>> getUnlimitedParameters();

}