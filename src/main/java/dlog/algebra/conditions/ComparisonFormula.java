package dlog.algebra.conditions;

import dlog.algebra.ParameterValue;

public interface ComparisonFormula {
    int NOT_SET      = -1;
    int LESS_THAN    = -1;
    int EQUAL_TO     = 0;
    int GREATER_THAN = 1;
    
    boolean matches(ParameterValue<?>[] tuple);
    
    boolean matches(ParameterValue<?>[] left, ParameterValue<?>[] right);
}
