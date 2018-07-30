package com.github.dritter.hd.dlog;

import com.github.dritter.hd.dlog.algebra.ParameterValue;

public class Parameter<T extends Comparable<? super T>> {
    
    public enum Kind {
        VARIABLE, CONSTANT;
    }
    
    protected Kind kind;
    protected T value;
    
    public Parameter(final Kind kind, final T value) {
        this.kind = kind;
        this.value = value;
    }
    
    public static <T extends Comparable<? super T>> Parameter<T> createVariable(T value) {
        return new Parameter<T>(Parameter.Kind.VARIABLE, value);
    }
    
    public static <T extends Comparable<? super T>> Parameter<T> createConstant(T value) {
        return new Parameter<T>(Parameter.Kind.CONSTANT, value);
    }
    
    public Kind getKind() {
        return this.kind;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public ParameterValue<?> getParameterValue() {
        return ParameterValue.create(this.value);
    }
    
    @Override
    public String toString() {
        return this.kind == Parameter.Kind.CONSTANT ? "\"" + value.toString() + "\"" : value.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kind == null) ? 0 : kind.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Parameter<?> other = (Parameter<?>) obj;
        if (kind != other.kind) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
