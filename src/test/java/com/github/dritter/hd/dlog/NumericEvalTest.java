package com.github.dritter.hd.dlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.dritter.hd.dlog.algebra.DataIterator;
import com.github.dritter.hd.dlog.algebra.ParameterValue;
import com.github.dritter.hd.dlog.algebra.TableIterator;
import org.junit.Assert;
import org.junit.Test;

import com.github.dritter.hd.dlog.algebra.DataIterator;
import com.github.dritter.hd.dlog.algebra.ParameterValue;
import com.github.dritter.hd.dlog.algebra.TableIterator;

public class NumericEvalTest {
    @Test
    public void testNegativeExceptionNumericGreaterEval() {
        // build rules
        // p(X) :- q(X, Y), X>1. with X=1
        final Predicate sum = Predicate.create("sum", 1);
        final Predicate add = Predicate.create("add", 2);

        final Parameter<String> paramX = Parameter.createVariable("X");
        final Parameter<String> paramY = Parameter.createVariable("Y");
        // final Parameter paramN = NumericParameter.createConstant(1);
        final Parameter<Integer> paramN = Parameter.createConstant(-1);

        final Literal head = Literal.create(sum, paramX);
        final Literal body1 = Literal.create(add, paramX, paramY);
        final Literal body2 = Literal.create(BuiltInPredicates.GREATER, paramX, paramN);

        final Rule rule = Rule.create(head, body1, body2);

        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);

        // build facts
        final Facts fq = Facts.create(add, getNumericSelectionTable());

        final Collection<IFacts> f = new ArrayList<IFacts>();
        f.add(fq);

        // run evaluation
        final IEvaluator eval = new NonRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(f);

        Assert.assertEquals(1, result.size());
        
        final ParameterValue<?>[] expected = new ParameterValue[] { ParameterValue.create(1) };
        
        for (IFacts fact : result) {
            Assert.assertEquals(fact.getPredicate().getName(), "sum");

            final DataIterator op = fact.getValues();
            op.open();
            Assert.assertArrayEquals(expected, op.next());
            Assert.assertArrayEquals(expected, op.next());
            Assert.assertNull(op.next());
            op.close();
        }
    }

    @Test
    public void testExceptionNumericGreaterEval() {
        // build rules
        // p(X) :- q(X, Y), X>1. with X=1
        final Predicate sum = Predicate.create("sum", 1);
        final Predicate add = Predicate.create("add", 2);

        final Parameter<String> paramX = Parameter.createVariable("X");
        final Parameter<String> paramY = Parameter.createVariable("Y");
        // final Parameter paramN = NumericParameter.createConstant(1);
        final Parameter<Integer> paramN = Parameter.createConstant(1);

        final Literal head = Literal.create(sum, paramX);
        final Literal body1 = Literal.create(add, paramX, paramY);
        final Literal body2 = Literal.create(BuiltInPredicates.GREATER, paramX, paramN);

        final Rule rule = Rule.create(head, body1, body2);

        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);

        // build facts
        final Facts fq = Facts.create(add, getNumericSelectionTable());

        final Collection<IFacts> f = new ArrayList<IFacts>();
        f.add(fq);

        // run evaluation
        final IEvaluator eval = new NonRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(f);

        Assert.assertEquals(1, result.size());
        
        for (IFacts fact : result) {
            Assert.assertEquals(fact.getPredicate().getName(), "sum");

            final DataIterator op = fact.getValues();
            op.open();
            Assert.assertNull(op.next());
            op.close();
        }
    }

    @Test
    public void testNumericGreaterEval() {
        // build rules
        // p(X) :- q(X, Y), X=1.
        final Predicate sum = Predicate.create("sum", 1);
        final Predicate add = Predicate.create("add", 2);

        final Parameter<String> paramX = Parameter.createVariable("X");
        final Parameter<String> paramY = Parameter.createVariable("Y");
        // final Parameter paramN = NumericParameter.createConstant(1);
        final Parameter<Integer> paramN = Parameter.createConstant(0);

        final Literal head = Literal.create(sum, paramX);
        final Literal body1 = Literal.create(add, paramX, paramY);
        final Literal body2 = Literal.create(BuiltInPredicates.GREATER, paramX, paramN);

        final Rule rule = Rule.create(head, body1, body2);

        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);

        // build facts
        final Facts fq = Facts.create(add, getNumericSelectionTable());

        final Collection<IFacts> f = new ArrayList<IFacts>();
        f.add(fq);

        // run evaluation
        final IEvaluator eval = new NonRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(f);

        Assert.assertEquals(1, result.size());
        
        final ParameterValue<?>[] expected = new ParameterValue[] { ParameterValue.create(1) };
        
        for (IFacts fact : result) {
            Assert.assertEquals(fact.getPredicate().getName(), "sum");

            final DataIterator op = fact.getValues();
            op.open();
            Assert.assertArrayEquals(expected, op.next());
            Assert.assertArrayEquals(expected, op.next());
            Assert.assertNull(op.next());
            op.close();
        }
    }

    @Test
    public void testExceptionNumericEqualityEval() {
        // build rules
        // p(X) :- q(X, Y), X=1.
        final Predicate sum = Predicate.create("sum", 1);
        final Predicate add = Predicate.create("add", 2);

        final Parameter<String> paramX = Parameter.createVariable("X");
        final Parameter<String> paramY = Parameter.createVariable("Y");
        // final Parameter paramN = NumericParameter.createConstant(1);
        final Parameter<Integer> paramN = Parameter.createConstant(3);

        final Literal head = Literal.create(sum, paramX);
        final Literal body1 = Literal.create(add, paramX, paramY);
        final Literal body2 = Literal.create(BuiltInPredicates.EQUALS, paramX, paramN);

        final Rule rule = Rule.create(head, body1, body2);

        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);

        // build facts
        final Facts fq = Facts.create(add, getNumericSelectionTable());

        final Collection<IFacts> f = new ArrayList<IFacts>();
        f.add(fq);

        // run evaluation
        final IEvaluator eval = new NonRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(f);

        Assert.assertEquals(1, result.size());
        
        for (IFacts fact : result) {
            Assert.assertEquals(fact.getPredicate().getName(), "sum");

            final DataIterator op = fact.getValues();
            op.open();
            Assert.assertNull(op.next());
            op.close();
        }
    }

    @Test
    public void testNumericEqualityEval() {
        // build rules
        // p(X) :- q(X, Y), X=1.
        final Predicate sum = Predicate.create("sum", 1);
        final Predicate add = Predicate.create("add", 2);

        final Parameter<String> paramX = Parameter.createVariable("X");
        final Parameter<String> paramY = Parameter.createVariable("Y");
        // final Parameter paramN = NumericParameter.createConstant(1);
        final Parameter<Integer> paramN = Parameter.createConstant(1);

        final Literal head = Literal.create(sum, paramX);
        final Literal body1 = Literal.create(add, paramX, paramY);
        final Literal body2 = Literal.create(BuiltInPredicates.EQUALS, paramX, paramN);

        final Rule rule = Rule.create(head, body1, body2);

        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);

        // build facts
        final Facts fq = Facts.create(add, getNumericSelectionTable());

        final Collection<IFacts> f = new ArrayList<IFacts>();
        f.add(fq);

        // run evaluation
        final IEvaluator eval = new NonRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(f);

        Assert.assertEquals(1, result.size());
        
        final ParameterValue<?>[] expected = new ParameterValue[] { ParameterValue.create(1) };
        
        for (IFacts fact : result) {
            Assert.assertEquals(fact.getPredicate().getName(), "sum");

            final DataIterator op = fact.getValues();
            op.open();
            Assert.assertArrayEquals(expected, op.next());
            Assert.assertArrayEquals(expected, op.next());
            Assert.assertNull(op.next());
            op.close();
        }
    }

    /**
     * @return DataIterator
     */
    private static DataIterator getNumericSelectionTable() {
        final ParameterValue<?>[][] values = new ParameterValue<?>[][] {
            { ParameterValue.create(1), ParameterValue.create("def") },
            { ParameterValue.create(1), ParameterValue.create("abc") }
        };
        return new TableIterator(values);
    }
}
