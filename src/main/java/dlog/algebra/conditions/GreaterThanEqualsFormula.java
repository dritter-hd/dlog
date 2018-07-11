package dlog.algebra.conditions;

import dlog.algebra.ParameterValue;

public class GreaterThanEqualsFormula implements ComparisonFormula {
    
    private int leftComponentNumber;
    private int rightComponentNumber;
    private ParameterValue<?> leftValue;
    private ParameterValue<?> rightValue;
    
    public GreaterThanEqualsFormula(int left, int right) {
        if (left < 0) {
            throw new IllegalArgumentException("Left component number must not be negative");
        }
        if (right < 0) {
            throw new IllegalArgumentException("Right component number must not be negative");
        }
        this.leftComponentNumber  = left;
        this.rightComponentNumber = right;
        this.leftValue = null;
        this.rightValue = null;
    }
    
    public GreaterThanEqualsFormula(int left, ParameterValue<?> right) {
        if (left < 0) {
            throw new IllegalArgumentException("Left component number must not be negative");
        }
        if (right == null) {
            throw new IllegalArgumentException("Right value must not be null");
        }
        this.leftComponentNumber  = left;
        this.rightComponentNumber = ComparisonFormula.NOT_SET;
        this.leftValue = null;
        this.rightValue = right;
    }
    
    public GreaterThanEqualsFormula(ParameterValue<?> left, int right) {
        if (left == null) {
            throw new IllegalArgumentException("Left value must not be null");
        }
        if (right < 0) {
            throw new IllegalArgumentException("Right component number must not be negative");
        }
        this.leftComponentNumber  = ComparisonFormula.NOT_SET;
        this.rightComponentNumber = right;
        this.leftValue = left;
        this.rightValue = null;
    }
    
    @Override
    public boolean matches(ParameterValue<?>[] tuple) {
        if (leftComponentNumber != ComparisonFormula.NOT_SET) {
            if (rightComponentNumber != ComparisonFormula.NOT_SET) {
                return tuple[leftComponentNumber].compareTo(tuple[rightComponentNumber]) > ComparisonFormula.LESS_THAN;
            } else {
                return tuple[leftComponentNumber].compareTo(rightValue) > ComparisonFormula.LESS_THAN;
            }
        } else {
            return leftValue.compareTo(tuple[rightComponentNumber]) > ComparisonFormula.LESS_THAN;
        }
    }
    
    @Override
    public boolean matches(ParameterValue<?>[] left, ParameterValue<?>[] right) {
        if (leftComponentNumber != ComparisonFormula.NOT_SET) {
            if (rightComponentNumber != ComparisonFormula.NOT_SET) {
                return left[leftComponentNumber].compareTo(right[rightComponentNumber]) > ComparisonFormula.LESS_THAN;
            }
        }
        throw new IllegalStateException("Only comparison of two components is supported in join conditions");
    }
}
