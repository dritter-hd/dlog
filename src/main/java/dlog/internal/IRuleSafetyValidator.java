package dlog.internal;

import dlog.Parameter;

import java.util.List;

public interface IRuleSafetyValidator {

    /**
     * @return List<Parameter>
     */
    public abstract List<Parameter<?>> getUnlimitedParameters();

}