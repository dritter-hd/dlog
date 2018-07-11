package dlog.compiler.internal;

import java.util.List;

import dlog.Parameter;

public interface Operator {
    List<Operator> getChildren();

    List<Parameter<?>> getFreeVariables();

    int getArity();
}
