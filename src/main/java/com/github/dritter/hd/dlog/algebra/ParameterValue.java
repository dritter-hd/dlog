package com.github.dritter.hd.dlog.algebra;


public class ParameterValue<T extends Comparable<? super T>> implements Comparable<ParameterValue<?>> {
    
    private T _value;
    
    public ParameterValue(T value) {
        this._value = value;
    }
    
    public static <T extends Comparable<? super T>> ParameterValue<T> create(T value) {
        return new ParameterValue<T>(value);
    }
    
    public T get() {
        return this._value;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(ParameterValue<?> o) {
        T value;
        try {
            value = (T) o.get();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.get().getClass().getSimpleName() + " cannot be compare to " + o.get().getClass().getSimpleName());
        }
        return this.get().compareTo(value);
    }
    
    @Override
    public String toString() {
        return this._value.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_value == null) ? 0 : _value.hashCode());
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
        ParameterValue<?> other = (ParameterValue<?>) obj;
        if (_value == null) {
            if (other._value != null) {
                return false;
            }
        } else if (!_value.equals(other._value)) {
            return false;
        }
        return true;
    }
}
