package com.github.dritter.hd.dlog.internal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.dritter.hd.dlog.*;
import com.github.dritter.hd.dlog.algebra.DataIterator;
import org.junit.Test;

import com.github.dritter.hd.dlog.BuiltInPredicates;
import com.github.dritter.hd.dlog.Facts;
import com.github.dritter.hd.dlog.IEvaluator;
import com.github.dritter.hd.dlog.IFacts;
import com.github.dritter.hd.dlog.IRule;
import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.NonRecursiveEvaluator;
import com.github.dritter.hd.dlog.Parameter;
import com.github.dritter.hd.dlog.Predicate;
import com.github.dritter.hd.dlog.Rule;
import com.github.dritter.hd.dlog.algebra.DataIterator;
import com.github.dritter.hd.dlog.utils.Utils;

public class ComplexAlgebraTest {
    
    @Test
    public void testEvaluatingSelectionWithTwoVariablesInCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X=Y.
         * 
         * r(a, a).
         * r(a, b).
         * 
         * Expected IDB facts:
         * 
         * p(a, a).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentX, argumentY);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "a"},
            {"a", "b"}
        };
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        
        IFacts relationRFacts = Facts.create(r, relationRIterator);
        
        Collection<IFacts> edbRelations = new ArrayList<IFacts>();
        edbRelations.add(relationRFacts);
        
        IEvaluator evaluator = new NonRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(1, idbRelations.size());
        for (IFacts relation : idbRelations) {
            DataIterator iterator = relation.getValues();
            iterator.open();
            int size = 0;
            while (iterator.next() != null) {
                size = size + 1;
            }
            assertEquals(1, size);
        }
    }
    
    @Test
    public void testEvaluatingSelectionWithTwoConditions() {
        
        /*
         * p(X, Y) :- r(X, Y) & X=a & Y=b.
         * 
         * r(a, a).
         * r(a, b).
         * r(b, a).
         * r(b, b).
         * 
         * Expected IDB facts:
         * 
         * p(a, b).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumenta = Parameter.createConstant("a");
        Parameter<?> argumentb = Parameter.createConstant("b");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentX, argumenta);
        Literal subgoal3 = Literal.create(BuiltInPredicates.EQUALS, argumentY, argumentb);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "a"},
            {"a", "b"},
            {"b", "a"},
            {"b", "b"}
        };
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        
        IFacts relationRFacts = Facts.create(r, relationRIterator);
        
        Collection<IFacts> edbRelations = new ArrayList<IFacts>();
        edbRelations.add(relationRFacts);
        
        IEvaluator evaluator = new NonRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(1, idbRelations.size());
        for (IFacts relation : idbRelations) {
            DataIterator iterator = relation.getValues();
            iterator.open();
            int size = 0;
            while (iterator.next() != null) {
                size = size + 1;
            }
            assertEquals(1, size);
        }
    }
    
    @Test
    public void testEvaluatingJoinWithGreaterThanCondition() {
        
        /*
         * p(W, X, Y, Z) :- r(W, X) & s(Y, Z) & W>Y.
         * 
         * r(1, 2).
         * r(5, 6).
         * 
         * s(3, 4).
         * s(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(5, 6, 3, 4).
         */
        
        Predicate p = Predicate.create("p", 4);
        Predicate r = Predicate.create("r", 2);
        Predicate s = Predicate.create("s", 2);
        
        Parameter<?> argumentW = Parameter.createVariable("W");
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        
        Literal head     = Literal.create(p, argumentW, argumentX, argumentY, argumentZ);
        Literal subgoal1 = Literal.create(r, argumentW, argumentX);
        Literal subgoal2 = Literal.create(s, argumentY, argumentZ);
        Literal subgoal3 = Literal.create(BuiltInPredicates.GREATER, argumentW, argumentY);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {5, 6}
        };
        
        int[][] relationS = {
            {3, 4},
            {7, 8}
        };
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        DataIterator relationSIterator = Utils.createRelationIterator(relationS);
        
        IFacts relationRFacts = Facts.create(r, relationRIterator);
        IFacts relationSFacts = Facts.create(s, relationSIterator);
        
        Collection<IFacts> edbRelations = new ArrayList<IFacts>();
        edbRelations.add(relationRFacts);
        edbRelations.add(relationSFacts);
        
        IEvaluator evaluator = new NonRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(1, idbRelations.size());
        for (IFacts relation : idbRelations) {
            DataIterator iterator = relation.getValues();
            iterator.open();
            int size = 0;
            while (iterator.next() != null) {
                size = size + 1;
            }
            assertEquals(1, size);
        }
    }
    
    @Test
    public void testEvaluatingSelectionWithEqualsConditionAndGreaterThanCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X=1 & Y>2.
         * 
         * r(1, 2).
         * r(1, 4).
         * r(3, 2).
         * r(3, 4).
         * 
         * Expected IDB facts:
         * 
         * p(1, 4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument1 = Parameter.createConstant(1);
        Parameter<?> argument2 = Parameter.createConstant(2);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentX, argument1);
        Literal subgoal3 = Literal.create(BuiltInPredicates.GREATER, argumentY, argument2);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {1, 4},
            {3, 2},
            {3, 4}
        };
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        
        IFacts relationRFacts = Facts.create(r, relationRIterator);
        
        Collection<IFacts> edbRelations = new ArrayList<IFacts>();
        edbRelations.add(relationRFacts);
        
        IEvaluator evaluator = new NonRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(1, idbRelations.size());
        for (IFacts relation : idbRelations) {
            DataIterator iterator = relation.getValues();
            iterator.open();
            int size = 0;
            while (iterator.next() != null) {
                size = size + 1;
            }
            assertEquals(1, size);
        }
    }
    
    @Test
    public void testEvaluatingSimpleSelection() {
        
        /*
         * p(X, Y) :- r(X, Y) & X=a.
         * 
         * r(a, b).
         * r(c, d).
         * 
         * Expected IDB facts:
         * 
         * p(a, b).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumenta = Parameter.createConstant("a");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentX, argumenta);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "b"},
            {"c", "d"}
        };
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        
        IFacts relationRFacts = Facts.create(r, relationRIterator);
        
        Collection<IFacts> edbRelations = new ArrayList<IFacts>();
        edbRelations.add(relationRFacts);
        
        IEvaluator evaluator = new NonRecursiveEvaluator(rules);
        Collection<IFacts> idbRelations = evaluator.eval(edbRelations);
        
        assertEquals(1, idbRelations.size());
        for (IFacts relation : idbRelations) {
            DataIterator iterator = relation.getValues();
            iterator.open();
            int size = 0;
            while (iterator.next() != null) {
                size = size + 1;
            }
            assertEquals(1, size);
        }
    }
}
