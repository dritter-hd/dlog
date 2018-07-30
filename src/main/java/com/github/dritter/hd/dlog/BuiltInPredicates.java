package com.github.dritter.hd.dlog;

import com.github.dritter.hd.dlog.algebra.ParameterValue;
import com.github.dritter.hd.dlog.algebra.conditions.*;

import java.util.HashSet;
import java.util.Set;

public final class BuiltInPredicates {
    
    public static final Predicate LESS           = Predicate.create("<", 2);
    public static final Predicate EQUALS         = Predicate.create("=", 2);
    public static final Predicate GREATER        = Predicate.create(">", 2);
    public static final Predicate LESS_EQUALS    = Predicate.create("<=", 2);
    public static final Predicate NOT_EQUALS     = Predicate.create("!=", 2);
    public static final Predicate GREATER_EQUALS = Predicate.create(">=", 2);
	public static final Predicate CONTAINS = Predicate.create("=c", 2);
    
    private static Set<Predicate> builtInPredicates = new HashSet<Predicate>();
    
    private BuiltInPredicates() {
        
    }
    
    static {
        BuiltInPredicates.builtInPredicates.add(BuiltInPredicates.LESS);
        BuiltInPredicates.builtInPredicates.add(BuiltInPredicates.EQUALS);
        BuiltInPredicates.builtInPredicates.add(BuiltInPredicates.GREATER);
        BuiltInPredicates.builtInPredicates.add(BuiltInPredicates.LESS_EQUALS);
        BuiltInPredicates.builtInPredicates.add(BuiltInPredicates.NOT_EQUALS);
        BuiltInPredicates.builtInPredicates.add(BuiltInPredicates.GREATER_EQUALS);
        BuiltInPredicates.builtInPredicates.add(BuiltInPredicates.CONTAINS);
    }
    
    public static boolean isBuiltIn(final Predicate p) {
        return BuiltInPredicates.builtInPredicates.contains(p);
    }
    
    public static ComparisonFormula createComparisonFormula(Predicate operator, int left, int right) {
        if (BuiltInPredicates.LESS.equals(operator)) {
            return new LessThanFormula(left, right);
        } else if (BuiltInPredicates.EQUALS.equals(operator)) {
            return new EqualsFormula(left, right);
        } else if (BuiltInPredicates.GREATER.equals(operator)) {
            return new GreaterThanFormula(left, right);
        } else if (BuiltInPredicates.LESS_EQUALS.equals(operator)) {
            return new LessThanEqualsFormula(left, right);
        } else if (BuiltInPredicates.NOT_EQUALS.equals(operator)) {
            return new NotEqualsFormula(left, right);
        } else if (BuiltInPredicates.GREATER_EQUALS.equals(operator)) {
            return new GreaterThanEqualsFormula(left, right);
        } else if (BuiltInPredicates.CONTAINS.equals(operator)) {
            return new ContainsFormula(left, right);
        } else {
            throw new IllegalArgumentException("Only built-in predicates are supported in selection or join conditions");
        }
    }
    
    public static ComparisonFormula createComparisonFormula(Predicate operator, int left, ParameterValue<?> right) {
        if (BuiltInPredicates.LESS.equals(operator)) {
            return new LessThanFormula(left, right);
        } else if (BuiltInPredicates.EQUALS.equals(operator)) {
            return new EqualsFormula(left, right);
        } else if (BuiltInPredicates.GREATER.equals(operator)) {
            return new GreaterThanFormula(left, right);
        } else if (BuiltInPredicates.LESS_EQUALS.equals(operator)) {
            return new LessThanEqualsFormula(left, right);
        } else if (BuiltInPredicates.NOT_EQUALS.equals(operator)) {
            return new NotEqualsFormula(left, right);
        } else if (BuiltInPredicates.GREATER_EQUALS.equals(operator)) {
            return new GreaterThanEqualsFormula(left, right);
        } else if (BuiltInPredicates.CONTAINS.equals(operator)) {
            return new ContainsFormula(left, right);
        } else {
            throw new IllegalArgumentException("Only built-in predicates are supported in selection or join conditions");
        }
    }
    
    public static ComparisonFormula createComparisonFormula(Predicate operator, ParameterValue<?> left, int right) {
        if (BuiltInPredicates.LESS.equals(operator)) {
            return new LessThanFormula(left, right);
        } else if (BuiltInPredicates.EQUALS.equals(operator)) {
            return new EqualsFormula(left, right);
        } else if (BuiltInPredicates.GREATER.equals(operator)) {
            return new GreaterThanFormula(left, right);
        } else if (BuiltInPredicates.LESS_EQUALS.equals(operator)) {
            return new LessThanEqualsFormula(left, right);
        } else if (BuiltInPredicates.NOT_EQUALS.equals(operator)) {
            return new NotEqualsFormula(left, right);
        } else if (BuiltInPredicates.GREATER_EQUALS.equals(operator)) {
            return new GreaterThanEqualsFormula(left, right);
        } else if (BuiltInPredicates.CONTAINS.equals(operator)) {
            return new ContainsFormula(left, right);
        } else {
            throw new IllegalArgumentException("Only built-in predicates are supported in selection or join conditions");
        }
    }
}
