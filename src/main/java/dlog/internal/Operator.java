package dlog.internal;

import dlog.Parameter;

import java.util.List;

public interface Operator {
    List<Operator> getChildren();

    List<Parameter<?>> getFreeVariables();

    int getArity();
}
