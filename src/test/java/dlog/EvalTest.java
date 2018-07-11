package dlog;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dlog.parser.DlogParser;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import dlog.algebra.DataIterator;
import dlog.algebra.ParameterValue;
import dlog.algebra.TableIterator;
import dlog.utils.Utils;

public final class EvalTest {
	@Test
	public void testTransitiveClosure() {
		final String program = "tc(X,Y) :- edge(X,Y)." + "tc(X,Y) :- edge(X,Z), tc(Z,Y)." + "edge(\"a\",\"b\")." + "edge(\"b\",\"c\")."
				+ "edge(\"c\",\"b\")." + "edge(\"c\",\"d\").";

		final DlogParser parser = new DlogParser();
		parser.parse(program);

		final List<IRule> rules = parser.getRules();

		final Collection<IFacts> edbRelations = parser.getFacts();

		final IEvaluator evaluator = new NaiveRecursiveEvaluator(rules);
		final Collection<IFacts> idbRelations = evaluator.eval(edbRelations);

		assertEquals(1, idbRelations.size());
		assertEquals(
				"[tc(\"a\", \"b\"). tc(\"b\", \"c\"). tc(\"c\", \"b\"). tc(\"c\", \"d\"). tc(\"a\", \"b\"). tc(\"a\", \"c\"). tc(\"a\", \"d\"). tc(\"b\", \"b\"). tc(\"b\", \"c\"). tc(\"b\", \"d\"). tc(\"c\", \"b\"). tc(\"c\", \"c\"). tc(\"c\", \"d\"). ]",
				idbRelations.toString());
	}

	@Ignore
	@Test
	public void testHammingCodes() {
		final String program = "h(Y) :- h(X), Y=2*X. h(Y) :- h(X), Y=3*X. h(Y) :- h(X), Y=5*X. h(1).";

		final DlogParser parser = new DlogParser();
		parser.parse(program);

		final List<IRule> rules = parser.getRules();

		Collection<IFacts> edbRelations = parser.getFacts();

		final IEvaluator evaluator = new NaiveRecursiveEvaluator(rules);
		final Collection<IFacts> idbRelations = evaluator.eval(edbRelations);

	}

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
		final ParameterValue<?>[][] values = new ParameterValue<?>[][] { { ParameterValue.create(1), ParameterValue.create("def") },
				{ ParameterValue.create(1), ParameterValue.create("abc") } };
		return new TableIterator(values);
	}

	/**
	 * Test evaluation of already rectified rules.
	 */
	@Test
	public void testEvalRectified() {
		// build rules
		final Predicate p = Predicate.create("p", 2);
		final Predicate q = Predicate.create("q", 2);
		final Predicate r = Predicate.create("r", 2);

		final Parameter<?> paramW = Parameter.createVariable("W");
		final Parameter<?> paramX = Parameter.createVariable("X");
		final Parameter<?> paramY = Parameter.createVariable("Y");
		final Parameter<?> paramZ = Parameter.createVariable("Z");

		final Literal head = Literal.create(p, paramX, paramY);
		final Literal body1 = Literal.create(q, paramX, paramZ);
		final Literal body2 = Literal.create(r, paramW, paramY);
		final Literal body3 = Literal.create(BuiltInPredicates.EQUALS, paramZ, paramW);

		final Rule rule = Rule.create(head, body1, body2, body3);

		final List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		// build facts
		final Facts fq = Facts.create(q, getTable());
		final Facts fr = Facts.create(r, getTable());

		final Collection<IFacts> f = new ArrayList<IFacts>();
		f.add(fq);
		f.add(fr);

		// run evaluation
		final IEvaluator eval = new NonRecursiveEvaluator(rules);
		final Collection<IFacts> result = eval.eval(f);

		Assert.assertEquals(1, result.size());

		final ParameterValue<?>[] expected = new ParameterValue<?>[] { ParameterValue.create("abc"), ParameterValue.create("ghi") };
		for (IFacts fact : result) {
			Assert.assertEquals(fact.getPredicate().getName(), "p");

			final DataIterator op = fact.getValues();
			op.open();
			Assert.assertArrayEquals(expected, op.next());
			Assert.assertNull(op.next());
			op.close();
		}
	}

	/**
	 * Test evaluation with un-rectified rules.
	 */
	@Test
	public void testEvalUnRectified() {
		// build rules
		final Predicate p = Predicate.create("p", 2);
		final Predicate q = Predicate.create("q", 2);
		final Predicate r = Predicate.create("r", 2);

		final Parameter<?> paramX = Parameter.createVariable("X");
		final Parameter<?> paramY = Parameter.createVariable("Y");
		final Parameter<?> paramZ = Parameter.createVariable("Z");

		final Literal head = Literal.create(p, paramX, paramY);
		final Literal body1 = Literal.create(q, paramX, paramZ);
		final Literal body2 = Literal.create(r, paramZ, paramY);

		final Rule rule = Rule.create(head, body1, body2);

		final List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		// build facts
		final Facts fq = Facts.create(q, getTable());
		final Facts fr = Facts.create(r, getTable());

		final Collection<IFacts> f = new ArrayList<IFacts>();
		f.add(fq);
		f.add(fr);

		// run evaluation
		final IEvaluator eval = new NonRecursiveEvaluator(rules);
		final Collection<IFacts> result = eval.eval(f);

		Assert.assertEquals(1, result.size());

		final ParameterValue<?>[] expected = new ParameterValue<?>[] { ParameterValue.create("abc"), ParameterValue.create("ghi") };
		for (IFacts fact : result) {
			Assert.assertEquals(fact.getPredicate().getName(), "p");

			final DataIterator op = fact.getValues();
			op.open();
			Assert.assertArrayEquals(expected, op.next());
			Assert.assertNull(op.next());
			op.close();
		}
	}

	/**
	 * Test Evaluation with simple selection.
	 */
	@Test
	public void testEvalSelection() {
		// build rules
		final Predicate p = Predicate.create("p", 1);
		final Predicate q = Predicate.create("q", 2);

		final Parameter<?> paramX = Parameter.createVariable("X");
		final Parameter<?> paramAbc = Parameter.createConstant("abc");

		final Literal head = Literal.create(p, paramX);
		final Literal body1 = Literal.create(q, paramX, paramAbc);

		final Rule rule = Rule.create(head, body1);

		final List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		// build facts
		final Facts fq = Facts.create(q, getSelectionTable());

		final Collection<IFacts> f = new ArrayList<IFacts>();
		f.add(fq);

		// run evaluation
		final IEvaluator eval = new NonRecursiveEvaluator(rules);
		final Collection<IFacts> result = eval.eval(f);

		Assert.assertEquals(1, result.size());

		final ParameterValue<?>[] expected = new ParameterValue<?>[] { ParameterValue.create("abc") };
		for (IFacts fact : result) {
			Assert.assertEquals(fact.getPredicate().getName(), "p");

			final DataIterator op = fact.getValues();
			op.open();
			Assert.assertArrayEquals(expected, op.next());
			Assert.assertNull(op.next());
			op.close();
		}
	}

	/**
	 * Test evaluation with constants in head.
	 */
	@Test
	public void testEvalConstantInHead() {
		// build rules
		final Predicate p = Predicate.create("p", 2);
		final Predicate q = Predicate.create("q", 2);

		final Parameter<?> paramX = Parameter.createVariable("X");
		final Parameter<?> paramY = Parameter.createVariable("Y");
		final Parameter<?> paramAbc = Parameter.createConstant("abc");

		final Literal head = Literal.create(p, paramX, paramAbc);
		final Literal body1 = Literal.create(q, paramX, paramY);

		final Rule rule = Rule.create(head, body1);

		final List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		// build facts
		final Facts fq = Facts.create(q, getSelectionTable());

		final Collection<IFacts> f = new ArrayList<IFacts>();
		f.add(fq);

		// run evaluation
		final IEvaluator eval = new NonRecursiveEvaluator(rules);
		final Collection<IFacts> result = eval.eval(f);

		Assert.assertEquals(1, result.size());

		final ParameterValue<?>[] expected = new ParameterValue<?>[] { ParameterValue.create("abc"), ParameterValue.create("abc") };
		for (IFacts fact : result) {
			Assert.assertEquals(fact.getPredicate().getName(), "p");

			final DataIterator op = fact.getValues();
			op.open();
			Assert.assertArrayEquals(expected, op.next());
			Assert.assertArrayEquals(expected, op.next());
			Assert.assertNull(op.next());
			op.close();
		}
	}

	/**
	 * Test evaluation with duplicate variable in head.
	 */
	@Test
	public void testEvalDuplicateVariableInHead() {
		// build rules
		final Predicate p = Predicate.create("p", 2);
		final Predicate q = Predicate.create("q", 2);

		final Parameter<?> paramX = Parameter.createVariable("X");
		final Parameter<?> paramY = Parameter.createVariable("Y");

		final Literal head = Literal.create(p, paramX, paramX);
		final Literal body1 = Literal.create(q, paramX, paramY);

		final Rule rule = Rule.create(head, body1);

		final List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		// build facts
		final Facts fq = Facts.create(q, getSelectionTable());

		final Collection<IFacts> f = new ArrayList<IFacts>();
		f.add(fq);

		// run evaluation
		final IEvaluator eval = new NonRecursiveEvaluator(rules);
		final Collection<IFacts> result = eval.eval(f);

		Assert.assertEquals(1, result.size());

		for (IFacts fact : result) {
			Assert.assertEquals(fact.getPredicate().getName(), "p");

			final DataIterator op = fact.getValues();
			op.open();
			ParameterValue<?>[] tuple = op.next();
			Assert.assertArrayEquals(new ParameterValue<?>[] { ParameterValue.create("abc"), ParameterValue.create("abc") }, tuple);

			tuple = op.next();
			Assert.assertArrayEquals(new ParameterValue<?>[] { ParameterValue.create("abc"), ParameterValue.create("abc") }, tuple);

			tuple = op.next();
			Assert.assertNull(tuple);
			op.close();
		}
	}

	@Test
	public void testEvaluatingRuleWithTwoFactsObjectsForOneEdbPredicate() {

		/*
		 * p(X, Y) :- r(X, Y).
		 * 
		 * r(a, b). r(c, d).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(a, b). p(c, d).
		 */

		Predicate p = Predicate.create("p", 2);
		Predicate r = Predicate.create("r", 2);

		Parameter<?> argumentX = Parameter.createVariable("X");
		Parameter<?> argumentY = Parameter.createVariable("Y");

		Literal head = Literal.create(p, argumentX, argumentY);
		Literal subgoal1 = Literal.create(r, argumentX, argumentY);

		IRule rule = Rule.create(head, subgoal1);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		String[][] relationR1 = { { "a", "b" } };

		String[][] relationR2 = { { "c", "d" } };

		DataIterator relationR1Iterator = Utils.createRelationIterator(relationR1);
		DataIterator relationR2Iterator = Utils.createRelationIterator(relationR2);

		IFacts relationR1Facts = Facts.create(r, relationR1Iterator);
		IFacts relationR2Facts = Facts.create(r, relationR2Iterator);

		Collection<IFacts> edbRelations = new ArrayList<IFacts>();
		edbRelations.add(relationR1Facts);
		edbRelations.add(relationR2Facts);

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

	@Test
	public void testEvaluatingXProducts() {

		/*
		 * p(W, X, Y, Z) :- r(W, X) & s(Y, Z).
		 * 
		 * r(a, b). r(c, d).
		 * 
		 * s(e, f). s(g, h).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(a, b, e, f). p(a, b, g, h). p(c, d, e, f). p(c, d, g, h).
		 */

		Predicate p = Predicate.create("p", 4);
		Predicate r = Predicate.create("r", 2);
		Predicate s = Predicate.create("s", 2);

		Parameter<?> argumentW = Parameter.createVariable("W");
		Parameter<?> argumentX = Parameter.createVariable("X");
		Parameter<?> argumentY = Parameter.createVariable("Y");
		Parameter<?> argumentZ = Parameter.createVariable("Z");

		Literal head = Literal.create(p, argumentW, argumentX, argumentY, argumentZ);
		Literal subgoal1 = Literal.create(r, argumentW, argumentX);
		Literal subgoal2 = Literal.create(s, argumentY, argumentZ);

		IRule rule = Rule.create(head, subgoal1, subgoal2);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		String[][] relationR = { { "a", "b" }, { "c", "d" } };

		String[][] relationS = { { "e", "f" }, { "g", "h" } };

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
			assertEquals(4, size);
		}
	}

	@Test
	public void testEvaluatingProjectionWithTwoVariablesNotFoundAmongOrdinarySubgoals() {

		/*
		 * p(W, X, Y, Z) :- r(W, X) & Y=Z & Z=a.
		 * 
		 * r(a, b). r(c, d).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(a, b, a, a). p(c, d, a, a).
		 */

		Predicate p = Predicate.create("p", 4);
		Predicate r = Predicate.create("r", 2);

		Parameter<?> argumentW = Parameter.createVariable("W");
		Parameter<?> argumentX = Parameter.createVariable("X");
		Parameter<?> argumentY = Parameter.createVariable("Y");
		Parameter<?> argumentZ = Parameter.createVariable("Z");
		Parameter<?> argumenta = Parameter.createConstant("a");

		Literal head = Literal.create(p, argumentW, argumentX, argumentY, argumentZ);
		Literal subgoal1 = Literal.create(r, argumentW, argumentX);
		Literal subgoal2 = Literal.create(BuiltInPredicates.EQUALS, argumentY, argumentZ);
		Literal subgoal3 = Literal.create(BuiltInPredicates.EQUALS, argumentZ, argumenta);

		IRule rule = Rule.create(head, subgoal1, subgoal2, subgoal3);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(rule);

		String[][] relationR = { { "a", "b" }, { "c", "d" } };

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

	@Test
	public void testSimpleIntegers() {

		/*
		 * p(X, Y) :- r(X, Y) & X<5.
		 * 
		 * r(1, 2). r(3, 4). r(5, 6). r(7, 8).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(1, 2). p(3, 4).
		 */

		String program = "p(X, Y) :- r(X, Y), <(X, 5). r(1, 2). r(3, 4). r(5, 6). r(7, 8).";

		DlogParser parser = new DlogParser();
		parser.parse(program);

		List<IRule> rules = parser.getRules();

		Collection<IFacts> edbRelations = parser.getFacts();

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
	public void testSimpleDoubles() {

		/*
		 * p(X, Y) :- r(X, Y) & X<1.5.
		 * 
		 * r(1.1, 1.2). r(1.3, 1.4). r(1.5, 1.6). r(1.7, 1.8).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(1.1, 1.2). p(1.3, 1.4).
		 */

		String program = "p(X, Y) :- r(X, Y), <(X, 1.5). r(1.1, 1.2). r(1.3, 1.4). r(1.5, 1.6). r(1.7, 1.8).";

		DlogParser parser = new DlogParser();
		parser.parse(program);

		List<IRule> rules = parser.getRules();

		Collection<IFacts> edbRelations = parser.getFacts();

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
		 * r(a, b). r(c, d). r(e, f). r(g, h).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(a, b). p(c, d).
		 */

		String program = "p(X, Y) :- r(X, Y), <(X, 'e'). r('a', 'b'). r('c', 'd'). r('e', 'f'). r('g', 'h').";

		DlogParser parser = new DlogParser();
		parser.parse(program);

		List<IRule> rules = parser.getRules();

		Collection<IFacts> edbRelations = parser.getFacts();

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
		 * r(false, false). r(false, true). r(true, false). r(true, true).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(false, false). p(false, true).
		 */

		String program = "p(X, Y) :- r(X, Y), <(X, true). r(false, false). r(false, true). r(true, false). r(true, true).";

		DlogParser parser = new DlogParser();
		parser.parse(program);

		List<IRule> rules = parser.getRules();

		Collection<IFacts> edbRelations = parser.getFacts();

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
		 * r(a, b). r(c, d). r(e, f). r(g, h).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(a, b). p(c, d).
		 */

		String program = "p(X, Y) :- r(X, Y), <(X, \"e\"). r(\"a\", \"b\"). r(\"c\", \"d\"). r(\"e\", \"f\"). r(\"g\", \"h\").";

		DlogParser parser = new DlogParser();
		parser.parse(program);

		List<IRule> rules = parser.getRules();

		Collection<IFacts> edbRelations = parser.getFacts();

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
		 * r(01.01.1970, 02.01.1970). r(03.01.1970, 04.01.1970). r(05.01.1970,
		 * 06.01.1970). r(07.01.1970, 08.01.1970).
		 * 
		 * Expected IDB facts:
		 * 
		 * p(01.01.1970, 02.01.1970). p(03.01.1970, 04.01.1970).
		 */

		String program = "p(X, Y) :- r(X, Y), <(X, 05.01.1970). r(01.01.1970, 02.01.1970). r(03.01.1970, 04.01.1970). r(05.01.1970, 06.01.1970). r(07.01.1970, 08.01.1970).";

		DlogParser parser = new DlogParser();
		parser.parse(program);

		List<IRule> rules = parser.getRules();

		Collection<IFacts> edbRelations = parser.getFacts();

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

	/**
	 * @return DataIterator
	 */
	private static DataIterator getTable() {
		final ParameterValue<?>[][] values = new ParameterValue<?>[][] { { ParameterValue.create("abc"), ParameterValue.create("def") },
				{ ParameterValue.create("abc"), ParameterValue.create("ghi") },
				{ ParameterValue.create("def"), ParameterValue.create("ghi") } };
		return new TableIterator(values);
	}

	/**
	 * @return DataIterator
	 */
	private static DataIterator getSelectionTable() {
		final ParameterValue<?>[][] values = new ParameterValue<?>[][] { { ParameterValue.create("abc"), ParameterValue.create("def") },
				{ ParameterValue.create("abc"), ParameterValue.create("abc") } };
		return new TableIterator(values);
	}
}
