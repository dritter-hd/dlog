package dlog;

import dlog.evaluator.DlogEvaluator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.logic.results.Result;
import org.openjdk.jmh.logic.results.RunResult;
import org.openjdk.jmh.output.OutputFormatType;
import org.openjdk.jmh.runner.BenchmarkRecord;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State
@OutputTimeUnit(TimeUnit.MILLISECONDS) @BenchmarkMode(Mode.AverageTime)
public class JmhLatencyTest {
    private static final String TC_RULE = "tc(x,y) :- e(x,y). tc(x,y) :- e(x,z), tc(z,y).";
    private static final String IRIS_RULE = "tc(?X,?Y) :- e(?X,?Y). tc(?X,?Y) :- e(?X,?Z), tc(?Z,?Y).";

    private static final double THRESHOLD = 0.5;

    public static void main(String[] args) {
        final Options opts = new OptionsBuilder()
                .include(".*")
                .warmupIterations(5)
                .measurementIterations(10)
                .jvmArgs("-server -XX:+UseConcMarkSweepGC -Xmx1500m -Xms1000m")
                //.param("offHeapMessages", "true""
                .forks(3) // 15
                .outputFormat(OutputFormatType.TextReport)
                .build();

        try {
            final Map<BenchmarkRecord, RunResult> records = new Runner(opts).run();
            for (final Map.Entry<BenchmarkRecord, RunResult> results : records.entrySet()) {
                final Result result = results.getValue().getPrimaryResult();
                System.out.println("Benchmark score: "
                        + result.getScore() + " "
                        + result.getScoreUnit() + " over "
                        + result.getStatistics().getN() + " iterations");
            }
        } catch (final RunnerException e) {
            e.printStackTrace();
        }
    }

    private DlogEvaluator hlogEval = DlogEvaluator.create();
    // private Parser irisParser = new Parser();

    @Setup
    public void setup() {
        final String facts = "e(\"e1\",\"e2\"). e(\"e2\",\"e3\"). e(\"e3\",\"e2\"). e(\"e3\",\"e4\").";
        // final String facts = FACT_DATA_SETS.get(counter).getFacts();
        this.hlogEval.initalize(facts, TC_RULE);

//        this.initializeIrisParser(facts);
    }

//    private void initializeIrisParser(final String facts) {
//        try {
//            this.irisParser.parse(IRIS_RULE + " "
//                    + facts.replaceAll("\"", "'") + " " + "?-tc(?X, ?Y).");
//        } catch (final ParserException e) {
//            e.printStackTrace();
//        }
//    }


//    @GenerateMicroBenchmark
//    public void testRule_iris() {
//        try {
//            // Iris starts with the evaluation, when facts are added
//            final List<IRule> irisRules = irisParser.getRules();
//            final Map<IPredicate, IRelation> irisFacts = irisParser
//                    .getFacts();
//            final Configuration irisConfiguration = new Configuration();
//            final IKnowledgeBase irisEval = KnowledgeBaseFactory.createKnowledgeBase(
//                    irisFacts, irisRules, irisConfiguration);
//
//            final List<IVariable> variableBindings = new ArrayList<IVariable>();
//            final IQuery irisQuery = irisParser.getQueries().get(0);
//            irisEval.execute(irisQuery, variableBindings);
//        } catch (ProgramNotStratifiedException e) {
//            e.printStackTrace();
//        } catch (RuleUnsafeException e) {
//            e.printStackTrace();
//        } catch (EvaluationException e) {
//            e.printStackTrace();
//        }
//    }

    @GenerateMicroBenchmark
    public void test_dlogEval() {
        this.hlogEval.query("tc", 2);
    }
}
