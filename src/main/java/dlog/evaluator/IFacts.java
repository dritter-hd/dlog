package dlog.evaluator;

import java.util.Collection;
import java.util.List;

import dlog.algebra.ParameterValue;

public interface IFacts {
	String getPredicate();
	int getArity();
	Collection<List<ParameterValue<?>>> getValues();
}
