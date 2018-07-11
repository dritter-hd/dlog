package dlog.algebra.internal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import dlog.BuiltInPredicates;
import dlog.Facts;
import dlog.IEvaluator;
import dlog.IFacts;
import dlog.IRule;
import dlog.Literal;
import dlog.NonRecursiveEvaluator;
import dlog.Parameter;
import dlog.Predicate;
import dlog.Rule;
import dlog.algebra.DataIterator;
import dlog.utils.Utils;

public class BuiltInPredicatesTest {
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInLessThanCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<5.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2).
         * p(3, 4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument5 = Parameter.createConstant(5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument5);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"1\", \"2\"). p(\"3\", \"4\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInLessThanCondition_OtherSeq() {
        
        /*
         * p(X, Y) :- r(X, Y) & 5>X.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2).
         * p(3, 4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument5 = Parameter.createConstant(5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.GREATER, argument5, argumentX);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"1\", \"2\"). p(\"3\", \"4\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithStringArgumentsInLessThanCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<e.
         * 
         * r(a, b).
         * r(c, d).
         * r(e, f).
         * r(g, h).
         * 
         * Expected IDB facts:
         * 
         * p(a, b).
         * p(c, d).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumente = Parameter.createConstant("e");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argumente);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "b"},
            {"c", "d"},
            {"e", "f"},
            {"g", "h"}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"a\", \"b\"). p(\"c\", \"d\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInNotEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X!=1.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(3, 4).
         * p(5, 6).
         * p(7, 8).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument1 = Parameter.createConstant(1);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.NOT_EQUALS, argumentX, argument1);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(3, size);
        }
        assertEquals("[p(\"3\", \"4\"). p(\"5\", \"6\"). p(\"7\", \"8\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInNotEqualsCondition_OtherSeq() {
        
        /*
         * p(X, Y) :- r(X, Y) & 1!=X.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(3, 4).
         * p(5, 6).
         * p(7, 8).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument1 = Parameter.createConstant(1);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.NOT_EQUALS, argument1, argumentX);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(3, size);
        }
        assertEquals("[p(\"3\", \"4\"). p(\"5\", \"6\"). p(\"7\", \"8\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithStringArgumentsInNotEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X!=a.
         * 
         * r(a, b).
         * r(c, d).
         * r(e, f).
         * r(g, h).
         * 
         * Expected IDB facts:
         * 
         * p(c, d).
         * p(e, f).
         * p(g, h).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumenta = Parameter.createConstant("a");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.NOT_EQUALS, argumentX, argumenta);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "b"},
            {"c", "d"},
            {"e", "f"},
            {"g", "h"}
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
            assertEquals(3, size);
        }
        assertEquals("[p(\"c\", \"d\"). p(\"e\", \"f\"). p(\"g\", \"h\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInLessThanConditionAndNotEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<7 & X!=3.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2).
         * p(5, 6).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument7 = Parameter.createConstant(7);
        Parameter<?> argument3 = Parameter.createConstant(3);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument7);
        Literal subgoal3 = Literal.create(BuiltInPredicates.NOT_EQUALS, argumentX, argument3);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"1\", \"2\"). p(\"5\", \"6\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithStringArgumentsInLessThanConditionAndNotEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<g & X!=c.
         * 
         * r(a, b).
         * r(c, d).
         * r(e, f).
         * r(g, h).
         * 
         * Expected IDB facts:
         * 
         * p(a, b).
         * p(e, f).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentg = Parameter.createConstant("g");
        Parameter<?> argumentc = Parameter.createConstant("c");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argumentg);
        Literal subgoal3 = Literal.create(BuiltInPredicates.NOT_EQUALS, argumentX, argumentc);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "b"},
            {"c", "d"},
            {"e", "f"},
            {"g", "h"}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"a\", \"b\"). p(\"e\", \"f\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInLessThanEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<=3.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2).
         * p(3, 4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument3 = Parameter.createConstant(3);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS_EQUALS, argumentX, argument3);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"1\", \"2\"). p(\"3\", \"4\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInLessThanEqualsCondition_OtherSeq() {
        
        /*
         * p(X, Y) :- r(X, Y) & 3=>X.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2).
         * p(3, 4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument3 = Parameter.createConstant(3);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.GREATER_EQUALS, argument3, argumentX);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"1\", \"2\"). p(\"3\", \"4\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithStringArgumentsInLessThanEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<=c.
         * 
         * r(a, b).
         * r(c, d).
         * r(e, f).
         * r(g, h).
         * 
         * Expected IDB facts:
         * 
         * p(a, b).
         * p(c, d).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentc = Parameter.createConstant("c");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS_EQUALS, argumentX, argumentc);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "b"},
            {"c", "d"},
            {"e", "f"},
            {"g", "h"}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"a\", \"b\"). p(\"c\", \"d\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInGreaterThanEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X>=5.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(5, 6).
         * p(7, 8).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument5 = Parameter.createConstant(5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.GREATER_EQUALS, argumentX, argument5);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"5\", \"6\"). p(\"7\", \"8\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInGreaterThanEqualsCondition_OtherSeq() {
        
        /*
         * p(X, Y) :- r(X, Y) & 5=<X.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(5, 6).
         * p(7, 8).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument5 = Parameter.createConstant(5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS_EQUALS, argument5, argumentX);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"5\", \"6\"). p(\"7\", \"8\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithStringArgumentsInGreaterThanEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X>=e.
         * 
         * r(a, b).
         * r(c, d).
         * r(e, f).
         * r(g, h).
         * 
         * Expected IDB facts:
         * 
         * p(e, f).
         * p(g, h).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumente = Parameter.createConstant("e");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.GREATER_EQUALS, argumentX, argumente);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "b"},
            {"c", "d"},
            {"e", "f"},
            {"g", "h"}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"e\", \"f\"). p(\"g\", \"h\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInLessThanEqualsConditionAndGreaterThanEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<=5 & X>=3.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(3, 4).
         * p(5, 6).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument5 = Parameter.createConstant(5);
        Parameter<?> argument3 = Parameter.createConstant(3);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS_EQUALS, argumentX, argument5);
        Literal subgoal3 = Literal.create(BuiltInPredicates.GREATER_EQUALS, argumentX, argument3);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"3\", \"4\"). p(\"5\", \"6\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithStringArgumentsInLessThanEqualsConditionAndGreaterThanEqualsCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<=e & X>=c.
         * 
         * r(a, b).
         * r(c, d).
         * r(e, f).
         * r(g, h).
         * 
         * Expected IDB facts:
         * 
         * p(c, d).
         * p(e, f).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumente = Parameter.createConstant("e");
        Parameter<?> argumentc = Parameter.createConstant("c");
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS_EQUALS, argumentX, argumente);
        Literal subgoal3 = Literal.create(BuiltInPredicates.GREATER_EQUALS, argumentX, argumentc);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        String[][] relationR = {
            {"a", "b"},
            {"c", "d"},
            {"e", "f"},
            {"g", "h"}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"c\", \"d\"). p(\"e\", \"f\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testEvaluatingSelectionWithIntegerArgumentsInGreaterThanCondition() {
        
        /*
         * p(X, Y) :- r(X, Y) & 5>X.
         * 
         * r(1, 2).
         * r(3, 4).
         * r(5, 6).
         * r(7, 8).
         * 
         * Expected IDB facts:
         * 
         * p(1, 2).
         * p(3, 4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument5 = Parameter.createConstant(5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.GREATER, argument5, argumentX);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        int[][] relationR = {
            {1, 2},
            {3, 4},
            {5, 6},
            {7, 8}
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
            assertEquals(2, size);
        }
        assertEquals("[p(\"1\", \"2\"). p(\"3\", \"4\"). ]", idbRelations.toString());
    }
}
