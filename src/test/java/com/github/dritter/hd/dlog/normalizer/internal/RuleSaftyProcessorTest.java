package com.github.dritter.hd.dlog.normalizer.internal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import com.github.dritter.hd.dlog.BuiltInPredicates;
import com.github.dritter.hd.dlog.IRule;
import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.Parameter;
import com.github.dritter.hd.dlog.Predicate;
import com.github.dritter.hd.dlog.Rule;

public class RuleSaftyProcessorTest {
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

        new RuleSafetyProcessor().process(Rule.create(head, body1, body2));

        // p(X, Y) :- q(X).
        head = Literal.create(p, paramX, paramY);
        body1 = Literal.create(q, paramX);

        try {
            new RuleSafetyProcessor().process(Rule.create(head, body1));
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X). contains unlimited variable(s): Y");
        }
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

        new RuleSafetyProcessor().process(Rule.create(head, body1, body2));

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body1 = Literal.create(q, paramX);
        body2 = Literal.create(BuiltInPredicates.EQUALS, paramZ, parama);

        try {
            new RuleSafetyProcessor().process(Rule.create(head, body1, body2));
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X), Z = \"a\". contains unlimited variable(s): Y");
        }
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

        new RuleSafetyProcessor().process(Rule.create(head, body1, body2, body3));

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body3 = Literal.create(BuiltInPredicates.EQUALS, paramZ, paramY);

        try {
            new RuleSafetyProcessor().process(Rule.create(head, body1, body3));
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X), Z = Y. contains unlimited variable(s): Y");
        }
    }

    @Test
    public void testSafety() {
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

        final Collection<IRule> rules = new ArrayList<IRule>();
        rules.add(Rule.create(head, body1, body2, body3));

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body3 = Literal.create(BuiltInPredicates.EQUALS, paramZ, parama);

        rules.add(Rule.create(head, body1, body3));

        final Collection<IRule> limitedRules = new ArrayList<IRule>();
        try {
            for (IRule iRule : rules) {
                limitedRules.add((new RuleSafetyProcessor()).process(iRule));
            }
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X), Z = \"a\". contains unlimited variable(s): Y");
        }
    }

    @Test
    public void testBasicSafetyVariablesEnsuring() {
        Predicate p = Predicate.create("p", 2);

        Predicate q = Predicate.create("q", 1);
        Predicate r = Predicate.create("r", 1);
        
        Parameter<?> paramX = Parameter.createVariable("X");
        Parameter<?> paramY = Parameter.createVariable("Y");

        // p(X, Y) :- q(X), r(Y).
        Literal head = Literal.create(p, paramX, paramY);
        Literal body1 = Literal.create(q, paramX);
        Literal body2 = Literal.create(r, paramY);

        new RuleSafetyEnsuringProcessor().process(Rule.create(head, body1, body2));

        // p(X, Y) :- q(X).
        head = Literal.create(p, paramX, paramY);
        body1 = Literal.create(q, paramX);

        try {
            new RuleSafetyEnsuringProcessor().process(Rule.create(head, body1));
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X). contains unlimited variable(s): Y");
        }
    }

    @Test
    public void testBasicSafetyVariablesAndConstantEnsuring() {
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

        new RuleSafetyEnsuringProcessor().process(Rule.create(head, body1, body2));

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body1 = Literal.create(q, paramX);
        body2 = Literal.create(BuiltInPredicates.EQUALS, paramZ, parama);

        try {
            new RuleSafetyEnsuringProcessor().process(Rule.create(head, body1, body2));
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X), Z = \"a\". contains unlimited variable(s): Y");
        }
    }

    @Test
    public void testBasicSafetyVariablesAndConstantTransitiveEnsuring() {
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

        new RuleSafetyEnsuringProcessor().process(Rule.create(head, body1, body2, body3));

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body3 = Literal.create(BuiltInPredicates.EQUALS, paramZ, paramY);

        try {
            new RuleSafetyEnsuringProcessor().process(Rule.create(head, body1, body3));
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X), Z = Y. contains unlimited variable(s): Y");
        }
    }

    @Test
    public void testSafetyEnsuring() {
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

        final Collection<IRule> rules = new ArrayList<IRule>();
        rules.add(Rule.create(head, body1, body2, body3));

        // p(X, Y) :- q(X), Z=a.
        head = Literal.create(p, paramX, paramY);
        body3 = Literal.create(BuiltInPredicates.EQUALS, paramZ, parama);

        rules.add(Rule.create(head, body1, body3));

        final Collection<IRule> limitedRules = new ArrayList<IRule>();
        try {
            for (IRule iRule : rules) {
                limitedRules.add((new RuleSafetyEnsuringProcessor()).process(iRule));
            }
        } catch (IllegalArgumentException ia) {
            Assert.assertEquals(ia.getMessage(), "p(X, Y) :- q(X), Z = \"a\". contains unlimited variable(s): Y");
        }
    }
    
    @Test
    public void testProcessingRuleWithTwoVariablesNotFoundAmongOrdinarySubgoals() {
        
        /*
         * p(W, X, Y, Z) :- r(W, X) & Y=Z & Z=a.
         * 
         * r(a, b).
         * r(c, d).
         * 
         * Expected IDB facts:
         * 
         * p(a, b, a, a).
         * p(c, d, a, a).
         */
        
        Predicate p = Predicate.create("p", 4);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentW = Parameter.createVariable("W");
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        Parameter<?> argumenta = Parameter.createConstant("a");
        
        Literal head     = Literal.create(p, argumentW, argumentX, argumentY, argumentZ);
        Literal subgoal1 = Literal.create(r, argumentW, argumentX);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentY, argumentZ);
        Literal subgoal3 = Literal.create(BuiltInPredicates.EQUALS, argumentZ, argumenta);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        RuleSafety processor = new RuleSafetyProcessor();
        IRule actualRule = processor.process(rule);
        
        assertEquals(rule, actualRule);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testProcessingRuleWithGreaterThanPredicate() {
        
        /*
         * p(X, Y, Z) :- r(X, Y) & Z>1.
         * 
         * r(1, 2).
         * r(3, 4).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2, 2).
         * p(3, 4, 2).
         * p(1, 2, 3).
         * p(3, 4, 3).
         *      .
         *      .
         *      .
         * p(1, 2, infinite).
         * p(3, 4, infinite).
         */
        
        Predicate p = Predicate.create("p", 3);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        Parameter<?> argument1 = Parameter.createConstant(1);
        
        Literal head     = Literal.create(p, argumentX, argumentY, argumentZ);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.GREATER, argumentZ, argument1);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        RuleSafety processor = new RuleSafetyProcessor();
        processor.process(rule);
    }
    
    @Test
    public void testProcessingRuleWithTwoVariablesInEqualsPredicate() {
        
        /*
         * p(X, Y, Z) :- r(X, Y) & X=Z.
         * 
         * r(a, b).
         * r(c, d).
         * 
         * Expected IDB facts:
         * 
         * p(a, b, a).
         * p(c, d, c).
         */
        
        Predicate p = Predicate.create("p", 3);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        
        Literal head     = Literal.create(p, argumentX, argumentY, argumentZ);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentX, argumentZ);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        RuleSafety processor = new RuleSafetyProcessor();
        processor.process(rule);
    }
    
    @Test
    public void testProcessingRuleWithTwoVariablesNotFoundAmongOrdinarySubgoalsInGoodOrder() {
        
        /*
         * p(W, X, Y, Z) :- r(W, X) & Z=a & Y=Z.
         * 
         * r(a, b).
         * r(c, d).
         * 
         * Expected IDB facts:
         * 
         * p(a, b, a, a).
         * p(c, d, a, a).
         */
        
        Predicate p = Predicate.create("p", 4);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentW = Parameter.createVariable("W");
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        Parameter<?> argumenta = Parameter.createConstant("a");
        
        Literal head     = Literal.create(p, argumentW, argumentX, argumentY, argumentZ);
        Literal subgoal1 = Literal.create(r, argumentW, argumentX);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentZ, argumenta);
        Literal subgoal3 = Literal.create(BuiltInPredicates.EQUALS, argumentY, argumentZ);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        RuleSafety processor = new RuleSafetyProcessor();
        IRule actualRule = processor.process(rule);
        
        assertEquals(rule, actualRule);
    }
    
    @Test
    public void testProcessingRuleWithThreeVariablesNotFoundAmongOrdinarySubgoals() {
        
        /*
         * p(V, W, X, Y, Z) :- r(V, W) & Z=a & Y=Z & X=Y.
         * 
         * r(a, b).
         * r(c, d).
         * 
         * Expected IDB facts:
         * 
         * p(a, b, a, a, a).
         * p(c, d, a, a, a).
         */
        
        Predicate p = Predicate.create("p", 5);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentV = Parameter.createVariable("V");
        Parameter<?> argumentW = Parameter.createVariable("W");
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        Parameter<?> argumenta = Parameter.createConstant("a");
        
        Literal head     = Literal.create(p, argumentV, argumentW, argumentX, argumentY, argumentZ);
        Literal subgoal1 = Literal.create(r, argumentV, argumentW);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentZ, argumenta);
        Literal subgoal3 = Literal.create(BuiltInPredicates.EQUALS, argumentY, argumentZ);
        Literal subgoal4 = Literal.create(BuiltInPredicates.EQUALS, argumentX, argumentY);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3, subgoal4);
        
        RuleSafety processor = new RuleSafetyProcessor();
        IRule actualRule = processor.process(rule);
        
        assertEquals(rule, actualRule);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testProcessingRuleWithTwoVariablesInGreaterThanPredicate() {
        
        /*
         * p(X, Y, Z) :- r(X, Y) & X>Z.
         * 
         * r(1, 2).
         * r(3, 4).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2, 0).
         * p(3, 4, 2).
         * p(1, 2, -1).
         * p(3, 4, 1).
         *      .
         *      .
         *      .
         * p(1, 2, -infinite).
         * p(3, 4, -infinite).
         */
        
        Predicate p = Predicate.create("p", 3);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        
        Literal head     = Literal.create(p, argumentX, argumentY, argumentZ);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.GREATER, argumentX, argumentZ);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        RuleSafety processor = new RuleSafetyProcessor();
        processor.process(rule);
    }
}
