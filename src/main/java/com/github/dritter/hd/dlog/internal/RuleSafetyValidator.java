package com.github.dritter.hd.dlog.internal;

import com.github.dritter.hd.dlog.*;

import java.util.ArrayList;
import java.util.List;

public final class RuleSafetyValidator implements IRuleSafetyValidator {

    private List<Parameter<?>> ph;
    private List<Parameter<?>> limitedVariables;

    public RuleSafetyValidator(final IRule rule) {
        this.ph = rule.getHead().getParameters();
        this.limitedVariables = new ArrayList<Parameter<?>>();
        while (0 != this.addLimitedVariables(rule)) {}
    }

    private int addLimitedVariables(final IRule rule) {
        int oldSizeLimitedVariables = this.limitedVariables.size();

        for (Literal literal : rule.getBody()) {

            Predicate predicate = literal.getPredicate();
            final List<Parameter<?>> parameters = literal.getParameters();

            if (!BuiltInPredicates.isBuiltIn(predicate)) {
                this.addAllUnknownVariables(parameters);
            } else if (BuiltInPredicates.EQUALS.equals(predicate)) {
                this.addUnknownLimitedVariables(parameters);
            }

//            if (BuiltInPredicates.isBuiltIn(predicate)) {
//                if (predicate.getArity() != 2) {
//                    throw new IllegalStateException("Wrong arity of '" + predicate.toString() + "' built-in.");
//                }
//                addUnknownLimitedVariables(parameters);
//            } else {
//                this.addAllUnknownVariables(parameters);
//            }
        }
        return this.limitedVariables.size() - oldSizeLimitedVariables;
    }

    private void addUnknownLimitedVariables(final List<Parameter<?>> parameters) {

        Parameter<?> left  = parameters.get(0);
        Parameter<?> right = parameters.get(1);

        if (left.getKind().equals(Parameter.Kind.CONSTANT) && right.getKind().equals(Parameter.Kind.VARIABLE)) {
            this.addUnknownVariables(right);
        } else if (left.getKind().equals(Parameter.Kind.VARIABLE) && right.getKind().equals(Parameter.Kind.CONSTANT)) {
            this.addUnknownVariables(left);
        } else {
            if (this.limitedVariables.contains(left) || this.limitedVariables.contains(right)) {
                this.addAllUnknownVariables(parameters);
            }
        }
    }

    private void addUnknownVariables(Parameter<?> parameter) {
        if (!limitedVariables.contains(parameter)) {
            limitedVariables.add(parameter);
        }
    }

    private void addAllUnknownVariables(final List<Parameter<?>> parameters) {
        for (final Parameter<?> parameter : parameters) {
            this.addUnknownVariables(parameter);
        }
    }

    public List<Parameter<?>> getUnlimitedParameters() {

        List<Parameter<?>> unsafeParameters = new ArrayList<Parameter<?>>();

        for (Parameter<?> parameter : this.ph) {
            if (!this.limitedVariables.contains(parameter)) {
                if (parameter.getKind().equals(Parameter.Kind.VARIABLE)) {
                    unsafeParameters.add(parameter);
                }
            }
        }
        return unsafeParameters;
    }
}