package dlog.evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.storage.IRelation;
import org.junit.Ignore;
import org.junit.Test;

import com.jscalableunit.ScalableTestCase;
import com.jscalableunit.ScaleUp;
import com.jscalableunit.Solution;
import com.jscalableunit.result.TestCaseResult;
import com.jscalableunit.result.measures.RuntimeMeasure;
import com.jscalableunit.runner.ScalableTestCaseRunner;
import com.jscalableunit.ui.explorer.primitive.PrimitiveResultExplorer;

public class TestHlogJScalable {

    @Ignore
	@Test
	public void testHlogTransitiveClosureGraph() throws Exception {
		final ScalableTestCaseRunner runner = new ScalableTestCaseRunner();
		final TestCaseResult result = runner
				.run(TransitiveClosureHlogEvaluatorTest.class);

		Assert.assertNotNull(result);
		// TODO: disable graphical output: 
		new PrimitiveResultExplorer(result);
	}

	@ScalableTestCase(warmups = 1, iterations = 2, outliers = 1, measureTypes = { RuntimeMeasure.class })
	public static class TransitiveClosureHlogEvaluatorTest {
		// FIXME: instance data gets overwritten by scale up, solution
		// implementation
		private DlogEvaluator hlogEval = DlogEvaluator.create();
		//private IKnowledgeBase irisEval;
		private Parser irisParser = new Parser();
		private int counter = 0;

		private static final String TC_RULE = "tc(x,y) :- e(x,y). tc(x,y) :- e(x,z), tc(z,y).";
		private static final String IRIS_RULE = "tc(?X,?Y) :- e(?X,?Y). tc(?X,?Y) :- e(?X,?Z), tc(?Z,?Y).";

		private static final int MAX_TEST_DATA_SETS = 10;
		private static final int STEP_SIZE = 5;
		private static final double THRESHOLD = 0.5;

		private static final List<DataSet> FACT_DATA_SETS = new ArrayList<DataSet>();

		static {
			for (int i = 1; i <= MAX_TEST_DATA_SETS; ++i) {
				final TestHlogJScalable testHlogJScalable = new TestHlogJScalable();
				final DataSet dataSet = testHlogJScalable
						.convertAdjMatrixToFacts(testHlogJScalable
								.generateUniformAdjMatrix(STEP_SIZE * i,
										THRESHOLD));
				FACT_DATA_SETS.add(dataSet);
			}
		}

		public TransitiveClosureHlogEvaluatorTest() {
			this.scaleConcatenations();
		}

		@ScaleUp(times = MAX_TEST_DATA_SETS - 1, dimensionName = "Number of Facts (times 5)")
		public int scaleConcatenations() {
			// final String facts =
			// "e(\"e1\",\"e2\"). e(\"e2\",\"e3\"). e(\"e3\",\"e2\"). e(\"e3\",\"e4\").";
			final String facts = FACT_DATA_SETS.get(counter).getFacts();
			this.hlogEval.initalize(facts,
					TransitiveClosureHlogEvaluatorTest.TC_RULE);

			this.initializeIrisParser(facts);

			return FACT_DATA_SETS.get(counter++).getNumberEdges();
		}

		private void initializeIrisParser(final String facts) {
			try {
				this.irisParser.parse(IRIS_RULE + " "
						+ facts.replaceAll("\"", "'") + " " + "?-tc(?X, ?Y).");
			} catch (final ParserException e) {
				e.printStackTrace();
			}
		}

		@Solution(name = "irisEvalSolution")
		public void irisEvalSolution() {
			try {
				final List<IRule> irisRules = irisParser.getRules();
				final Map<IPredicate, IRelation> irisFacts = irisParser
						.getFacts();
				final Configuration irisConfiguration = new Configuration();
				final IKnowledgeBase irisEval = KnowledgeBaseFactory.createKnowledgeBase(
						irisFacts, irisRules, irisConfiguration);

				final List<IVariable> variableBindings = new ArrayList<IVariable>();
				final IQuery irisQuery = irisParser.getQueries().get(0);
				irisEval.execute(irisQuery, variableBindings);
			} catch (ProgramNotStratifiedException e) {
				e.printStackTrace();
			} catch (RuleUnsafeException e) {
				e.printStackTrace();
			} catch (EvaluationException e) {
				e.printStackTrace();
			}
		}
		
		@Solution(name = "hlogEvalSolution")
		public void hlogEvalSolution() {
			this.hlogEval.query("tc", 2);
		}
	}

	protected int[][] generateUniformAdjMatrix(final int dimension,
			final double threashold) {
		final int[][] adjMatrix = new int[dimension][dimension];
		final Random random = new Random(System.currentTimeMillis());

		for (int i = 0; i < dimension; ++i) {
			for (int j = 0; j < dimension; ++j) {
				final double nextDouble = random.nextDouble();
				if (threashold > nextDouble) {
					adjMatrix[i][j] = 1;
				} else {
					adjMatrix[i][j] = 0;
				}
			}
		}
		return adjMatrix;
	}

	protected DataSet convertAdjMatrixToFacts(final int[][] adjMatrix) {
		final int dimension = adjMatrix.length;
		final StringBuilder sb = new StringBuilder();

		int numberOfEdges = 0;

		for (int i = 0; i < dimension; ++i) {
			for (int j = 0; j < dimension; ++j) {
				if (1 == adjMatrix[i][j] && i != j) {
					sb.append("e(");
					sb.append("\"").append("e").append(i).append("\"")
							.append(",").append("\"").append("e").append(j)
							.append("\"");
					sb.append(").").append(" ");

					++numberOfEdges;
				}
			}
		}

		final DataSet dataSet = new DataSet(numberOfEdges, sb.toString());
		return dataSet;
	}

	public class DataSet {
		private final int numberEdges;
		private final String facts;

		public DataSet(final int numberOfEdges, final String facts) {
			this.numberEdges = numberOfEdges;
			this.facts = facts;
		}

		public int getNumberEdges() {
			return numberEdges;
		}

		public String getFacts() {
			return facts;
		}
	}
}
