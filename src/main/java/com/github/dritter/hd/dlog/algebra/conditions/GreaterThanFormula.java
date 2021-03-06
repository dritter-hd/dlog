package com.github.dritter.hd.dlog.algebra.conditions;

import com.github.dritter.hd.dlog.algebra.ParameterValue;

public class GreaterThanFormula implements ComparisonFormula {
    
    private int leftComponentNumber;
    private int rightComponentNumber;
    private ParameterValue<?> leftValue;
    private ParameterValue<?> rightValue;
    
    public GreaterThanFormula(int left, int right) {
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
    
    public GreaterThanFormula(int left, ParameterValue<?> right) {
        if (left < 0) {
            throw new IllegalArgumentException("Left component number must not be negative");
        }
        if (right == null) {
            throw new IllegalArgumentException("Right value must not be null");
        }
        this.leftComponentNumber  = left;
        this.rightComponentNumber = NOT_SET;
        this.leftValue = null;
        this.rightValue = right;
    }
    
    public GreaterThanFormula(ParameterValue<?> left, int right) {
        if (left == null) {
            throw new IllegalArgumentException("Left value must not be null");
        }
        if (right < 0) {
            throw new IllegalArgumentException("Right component number must not be negative");
        }
        this.leftComponentNumber  = NOT_SET;
        this.rightComponentNumber = right;
        this.leftValue = left;
        this.rightValue = null;
    }
    
    @Override
    public boolean matches(ParameterValue<?>[] tuple) {
        if (leftComponentNumber != NOT_SET) {
            if (rightComponentNumber != NOT_SET) {
                return tuple[leftComponentNumber].compareTo(tuple[rightComponentNumber]) > EQUAL_TO;
            } else {
                return tuple[leftComponentNumber].compareTo(rightValue) > EQUAL_TO;
            }
        } else {
            return leftValue.compareTo(tuple[rightComponentNumber]) > EQUAL_TO;
        }
    }
    
    @Override
    public boolean matches(ParameterValue<?>[] left, ParameterValue<?>[] right) {
        if (leftComponentNumber != NOT_SET) {
            if (rightComponentNumber != NOT_SET) {
                return left[leftComponentNumber].compareTo(right[rightComponentNumber]) > EQUAL_TO;
            }
        }
        throw new IllegalStateException("Only comparison of two components is supported in join conditions");
    }
}
