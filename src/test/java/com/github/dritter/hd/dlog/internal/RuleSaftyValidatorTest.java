package com.github.dritter.hd.dlog.internal;

import java.util.List;

import com.github.dritter.hd.dlog.*;
import junit.framework.Assert;

import org.junit.Test;

import com.github.dritter.hd.dlog.BuiltInPredicates;
import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.Parameter;
import com.github.dritter.hd.dlog.Predicate;
import com.github.dritter.hd.dlog.Rule;

public final class RuleSaftyValidatorTest {
    @Test
    public void testBasicSafetyVariables() {
        Predicate p = Predicate.create("p", 2);

        Predicate q = Predicate.create("q", 1);
        Predicate r = Predicate.create("r", 1);
        
        Parameter<?> paramX = Parameter.createVariable("X");
        Parameter<?> paramY = Parameter.createVariable("Y");
        
        // p(X, Y) :- q(X), r(Y).
        Literal head = Literal.create(p, paramX, paramY);
        Literal body1 = Literal.create(q, paramX);
        Literal body2 = Literal.create(r, paramY);

        IRuleSafetyValidator rsp = new RuleSafetyValidator(Rule.create(head, body1, body2));
        List<Parameter<?>> unlimitedParameters = rsp.getUnlimitedParameters();
        Assert.assertEquals(0, unlimitedParameters.size());

        // p(X, Y) :- q(X).
        head = Literal.create(p, paramX, paramY);
        body1 = Literal.create(q, paramX);

        rsp = new RuleSafetyValidator(Rule.create(head, body1));
        unlimitedParameters = rsp.getUnlimitedParameters();
        Assert.assertEquals(1, unlimitedParameters.size());
        Assert.assertEquals(paramY.getValue(), unlimitedParameters.get(0).getValue());
        printUnlimited(unlimitedParameters);
    }

    @Test
    public void testBasicSafetyVariablesAndConstant() {
        Predicate p = Predicate.create("p", 2);

        Predicate q = Predicate.create("q", 1);
        
        Parameter<?> parama = Parameter.createConstant("a");
        Parameter<?> paramX = Parameter.createVariable("X");
        Parameter<?> paramY = Parameter.createVariable("Y");
        Parameter<?> paramZ = Parameter.createVariable("Z");

        // p(X, Y) :- q(X), Y=a.
        Literal head = Literal.create(p, paramX, paramY);
        Literal body1 = Literal.create(q, paramX);
        Literal body2 = Literal.create(BuiltInPredicates.EQUALS, paramY, parama);

        IRuleSafetyValidator rsp = new RuleSafetyValidator(Rule.create(head, body1, body2));
        List<Parameter<?>> unlimitedParameters = rsp.getUnlimitedParameters();
        Assert.assertEquals(0, unlimitedParameters.size());

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body1 = Literal.create(q, paramX);
        body2 = Literal.create(BuiltInPredicates.EQUALS, paramZ, parama);

        rsp = new RuleSafetyValidator(Rule.create(head, body1, body2));
        unlimitedParameters = rsp.getUnlimitedParameters();
        Assert.assertEquals(1, unlimitedParameters.size());
        Assert.assertEquals(paramY.getValue(), unlimitedParameters.get(0).getValue());
        printUnlimited(unlimitedParameters);
    }

    @Test
    public void testBasicSafetyVariablesAndConstantTransitive() {
        Predicate p = Predicate.create("p", 2);

        Predicate q = Predicate.create("q", 1);
        
        Parameter<?> parama = Parameter.createConstant("a");
        Parameter<?> paramX = Parameter.createVariable("X");
        Parameter<?> paramY = Parameter.createVariable("Y");
        Parameter<?> paramZ = Parameter.createVariable("Z");

        // p(X, Y) :- q(X), Y=Z, Z=a.
        Literal head = Literal.create(p, paramX, paramY);
        Literal body1 = Literal.create(q, paramX);
        Literal body2 = Literal.create(BuiltInPredicates.EQUALS, paramZ, parama);
        Literal body3 = Literal.create(BuiltInPredicates.EQUALS, paramZ, paramY);

        IRuleSafetyValidator rsp = new RuleSafetyValidator(Rule.create(head, body1, body2, body3));
        List<Parameter<?>> unlimitedParameters = rsp.getUnlimitedParameters();
        Assert.assertEquals(0, unlimitedParameters.size());

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body3 = Literal.create(BuiltInPredicates.EQUALS, paramZ, paramY);

        rsp = new RuleSafetyValidator(Rule.create(head, body1, body3));
        unlimitedParameters = rsp.getUnlimitedParameters();
        Assert.assertEquals(1, unlimitedParameters.size());
        Assert.assertEquals(paramY.getValue(), unlimitedParameters.get(0).getValue());
        printUnlimited(unlimitedParameters);
    }

    private void printUnlimited(final List<Parameter<?>> unlimitedParameters) {
        System.out.print("Unlimited: ");
        for (Parameter<?> u : unlimitedParameters) {
            System.out.print(u.getValue());
            System.out.print(".");
        }
    }

}
