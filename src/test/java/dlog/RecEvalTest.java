package dlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dlog.algebra.DataIterator;
import dlog.algebra.ParameterValue;
import dlog.algebra.TableIterator;

public class RecEvalTest {
    @Test
    public void testRecursiveEval() {
        // build rules
        final Predicate fi = Predicate.create("fi", 2);
        final Predicate mf = Predicate.create("mf", 2);

        final Parameter<?> paramX = Parameter.createVariable("X");
        final Parameter<?> paramY = Parameter.createVariable("Y");
        final Parameter<?> paramZ = Parameter.createVariable("Z");

        Literal head = Literal.create(fi, paramX, paramY);
        Literal body1 = Literal.create(mf, paramX, paramZ);
        final Literal body2 = Literal.create(fi, paramZ, paramY);

        Rule rule = Rule.create(head, body1, body2);

        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);

        final Parameter<?> paramA = Parameter.createVariable("A");
        final Parameter<?> paramB = Parameter.createVariable("B");

        head = Literal.create(fi, paramA, paramB);
        body1 = Literal.create(mf, paramA, paramB);

        rule = Rule.create(head, body1);
        rules.add(rule);

        // build facts
        final Facts fmf = Facts.create(mf, getTable());

        final Collection<IFacts> f = new ArrayList<IFacts>();
        f.add(fmf);

        // run evaluation
        final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(f);

        Assert.assertEquals(1, result.size());

        final HashMap<String, ParameterValue<?>[][]> expected = this.getExpected();

        for (IFacts fact : result) {
            final String predicateName = fact.getPredicate().getName();
            Assert.assertTrue(expected.containsKey(predicateName));

            final DataIterator op = fact.getValues();
            op.open();
            ParameterValue<?>[] tuple = null;
            int counter = 0;

            while ((tuple = op.next()) != null) {
                Assert.assertArrayEquals(expected.get(predicateName)[counter], tuple);
                ++counter;
            }
            op.close();
        }
    }

    private HashMap<String, ParameterValue<?>[][]> getExpected() {
        HashMap<String, ParameterValue<?>[][]> input = new HashMap<String, ParameterValue<?>[][]>(2);
        input.put("fi", new ParameterValue<?>[][] {
            { ParameterValue.create("a"), ParameterValue.create("c") },
            { ParameterValue.create("a"), ParameterValue.create("d") },
            { ParameterValue.create("b"), ParameterValue.create("d") },
            { ParameterValue.create("a"), ParameterValue.create("b") },
            { ParameterValue.create("b"), ParameterValue.create("c") },
            { ParameterValue.create("c"), ParameterValue.create("d") }
        });
//        input.put("fi", new ParameterValue<?>[][] {
//            { ParameterValue.create("a"), ParameterValue.create("b") },
//            { ParameterValue.create("a"), ParameterValue.create("c") },
//            { ParameterValue.create("a"), ParameterValue.create("d") },
//            { ParameterValue.create("b"), ParameterValue.create("c") },
//            { ParameterValue.create("b"), ParameterValue.create("d") },
//            { ParameterValue.create("c"), ParameterValue.create("d") }
//        });
//        input.put("mf", new ParameterValue<?>[][] {
//            { ParameterValue.create("a"), ParameterValue.create("b") },
//            { ParameterValue.create("b"), ParameterValue.create("c") },
//            { ParameterValue.create("c"), ParameterValue.create("d") }
//        });
        return input;
    }

    @Test
    public void testRecursiveStrictEval() {
        // build rules
        final Predicate fi = Predicate.create("fi", 2);
        final Predicate mf = Predicate.create("mf", 2);

        final Parameter<?> paramX = Parameter.createVariable("X");
        final Parameter<?> paramY = Parameter.createVariable("Y");
        final Parameter<?> paramZ = Parameter.createVariable("Z");

        final Literal head = Literal.create(fi, paramX, paramY);
        final Literal body1 = Literal.create(mf, paramX, paramZ);
        final Literal body2 = Literal.create(fi, paramZ, paramY);

        final Rule rule = Rule.create(head, body1, body2);

        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule);

        // build facts
        final Facts fmf = Facts.create(mf, getTable());

        final Collection<IFacts> f = new ArrayList<IFacts>();
        f.add(fmf);

        // run evaluation
        final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
        Collection<IFacts> result = eval.eval(f);

        Assert.assertEquals(1, result.size());

        final HashMap<String, ParameterValue<?>[][]> expected = this.getExpectedStrict();

        for (IFacts fact : result) {
            final String predicateName = fact.getPredicate().getName();
            Assert.assertTrue(expected.containsKey(predicateName));

            final DataIterator op = fact.getValues();
            op.open();
            ParameterValue<?>[] tuple = null;
            int counter = 0;

            while ((tuple = op.next()) != null) {
                Assert.assertArrayEquals(expected.get(predicateName)[counter], tuple);
                ++counter;
            }
            op.close();
        }
    }

    private HashMap<String, ParameterValue<?>[][]> getExpectedStrict() {
        final HashMap<String, ParameterValue<?>[][]> input = new HashMap<String, ParameterValue<?>[][]>(2);
        input.put("fi", new ParameterValue<?>[][] {
            { ParameterValue.create("a"), ParameterValue.create("c") },
            { ParameterValue.create("a"), ParameterValue.create("d") },
            { ParameterValue.create("b"), ParameterValue.create("d") }
        });
        return input;
    }

    private static TableIterator getTable() {
        final ParameterValue<?>[][] values = new ParameterValue<?>[][] {
            { ParameterValue.create("a"), ParameterValue.create("b") },
            { ParameterValue.create("b"), ParameterValue.create("c") },
            { ParameterValue.create("c"), ParameterValue.create("d") }
        };
        return new TableIterator(values);
    }
}
