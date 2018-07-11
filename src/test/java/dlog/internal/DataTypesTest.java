package dlog.internal;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
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

public class DataTypesTest {
    
    @Test
    public void testSimpleBytes() {
        
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
        Parameter<?> argument5 = Parameter.createConstant((byte) 5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument5);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        Byte[][] relationR = {
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
    public void testSimpleShorts() {
        
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
        Parameter<?> argument5 = Parameter.createConstant((short) 5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument5);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        Short[][] relationR = {
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
    public void testSimpleIntegers() {
        
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
        
        Integer[][] relationR = {
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
    public void testSimpleLongs() {
        
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
        Parameter<?> argument5 = Parameter.createConstant(5L);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument5);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        Long[][] relationR = {
            {1L, 2L},
            {3L, 4L},
            {5L, 6L},
            {7L, 8L}
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
    public void testSimpleFloats() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<1.5.
         * 
         * r(1.1, 1.2).
         * r(1.3, 1.4).
         * r(1.5, 1.6).
         * r(1.7, 1.8).
         * 
         * Expected IDB facts:
         * 
         * p(1.1, 1.2).
         * p(1.3, 1.4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument15 = Parameter.createConstant(1.5F);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument15);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        Float[][] relationR = {
            {1.1F, 1.2F},
            {1.3F, 1.4F},
            {1.5F, 1.6F},
            {1.7F, 1.8F}
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
        assertEquals("[p(\"1.1\", \"1.2\"). p(\"1.3\", \"1.4\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testSimpleDoubles() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<1.5.
         * 
         * r(1.1, 1.2).
         * r(1.3, 1.4).
         * r(1.5, 1.6).
         * r(1.7, 1.8).
         * 
         * Expected IDB facts:
         * 
         * p(1.1, 1.2).
         * p(1.3, 1.4).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument15 = Parameter.createConstant(1.5);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument15);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        Double[][] relationR = {
            {1.1, 1.2},
            {1.3, 1.4},
            {1.5, 1.6},
            {1.7, 1.8}
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
        assertEquals("[p(\"1.1\", \"1.2\"). p(\"1.3\", \"1.4\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testSimpleCharacters() {
        
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
        Parameter<?> argumente = Parameter.createConstant('e');
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argumente);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        Character[][] relationR = {
            {'a', 'b'},
            {'c', 'd'},
            {'e', 'f'},
            {'g', 'h'}
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
    public void testSimpleBooleans() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<true.
         * 
         * r(false, false).
         * r(false, true).
         * r(true, false).
         * r(true, true).
         * 
         * Expected IDB facts:
         * 
         * p(false, false).
         * p(false, true).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumenttrue = Parameter.createConstant(true);
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argumenttrue);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        Boolean[][] relationR = {
            {false, false},
            {false, true},
            {true, false},
            {true, true}
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
        assertEquals("[p(\"false\", \"false\"). p(\"false\", \"true\"). ]", idbRelations.toString());
    }
    
    @Test
    public void testSimpleStrings() {
        
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
    public void testSimpleDates() {
        
        /*
         * p(X, Y) :- r(X, Y) & X<05.01.1970.
         * 
         * r(01.01.1970, 02.01.1970).
         * r(03.01.1970, 04.01.1970).
         * r(05.01.1970, 06.01.1970).
         * r(07.01.1970, 08.01.1970).
         * 
         * Expected IDB facts:
         * 
         * p(01.01.1970, 02.01.1970).
         * p(03.01.1970, 04.01.1970).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argument5 = Parameter.createConstant(new GregorianCalendar(1970, 0, 5));
        
        Literal head     = Literal.create(p, argumentX, argumentY);
        Literal subgoal1 = Literal.create(r, argumentX, argumentY);
        Literal subgoal2 = Literal.create(BuiltInPredicates.LESS, argumentX, argument5);
        
        IRule rule = Rule.create(head, subgoal1, subgoal2);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);
        
        GregorianCalendar[][] relationR = {
            {new GregorianCalendar(1970, 0, 1), new GregorianCalendar(1970, 0, 2)},
            {new GregorianCalendar(1970, 0, 3), new GregorianCalendar(1970, 0, 4)},
            {new GregorianCalendar(1970, 0, 5), new GregorianCalendar(1970, 0, 6)},
            {new GregorianCalendar(1970, 0, 7), new GregorianCalendar(1970, 0, 8)}
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
    }
}