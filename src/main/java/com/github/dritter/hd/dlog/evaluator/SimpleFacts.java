package com.github.dritter.hd.dlog.evaluator;

import com.github.dritter.hd.dlog.algebra.ParameterValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class SimpleFacts implements IFacts {
	private final String predicateName;
	private final int arity;
	private final List<List<ParameterValue<?>>> values;

	public static IFacts create(String predicateName, int arity,
			ParameterValue<?>[][] values) {
		if (values == null) {
			throw new IllegalArgumentException("IFact values must not be null.");
		}

		List<List<ParameterValue<?>>> collection = new ArrayList<List<ParameterValue<?>>>(
				values.length);
		for (int i = 0; i < values.length; ++i) {
			collection.add(Arrays.asList(values[i]));
		}
		return new SimpleFacts(predicateName, arity, collection);
	}

	public static IFacts create(String predicateName, int arity,
			List<List<ParameterValue<?>>> values) {
		return new SimpleFacts(predicateName, arity, values);
	}

	private SimpleFacts(String predicateName, int arity,
			List<List<ParameterValue<?>>> values) {
		if (predicateName == null || predicateName.equals("")) {
			throw new IllegalArgumentException(
					"IFacts Predicate Name must be set.");
		}
		// if (values.size() != arity){
		// throw new
		// IllegalArgumentException("IFacts arity and value size must be equal.");
		// }
		this.predicateName = predicateName;
		this.arity = arity;
		this.values = values;
	}

	public int getArity() {
		return arity;
	}

	public String getPredicate() {
		return predicateName;
	}

	public Collection<List<ParameterValue<?>>> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return Serializer.asString(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SimpleFacts)) {
			return false;
		}
		SimpleFacts pred = (SimpleFacts) obj;
		return pred.predicateName.equals(this.predicateName)
				&& (pred.arity == this.arity)
				&& pred.values.equals(this.values) ? true : false;
	}

	@Override
	public int hashCode() {
		return this.predicateName.hashCode() * this.arity
				* this.values.hashCode();
	}
}

