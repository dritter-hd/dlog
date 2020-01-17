package com.github.dritter.hd.dlog;

import com.github.dritter.hd.dlog.evaluator.DlogEvaluator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS) @BenchmarkMode(Mode.AverageTime)
public class JmhLatencyTest {
    private static final String TC_RULE = "tc(x,y) :- e(x,y). tc(x,y) :- e(x,z), tc(z,y).";
    private static final String IRIS_RULE = "tc(?X,?Y) :- e(?X,?Y). tc(?X,?Y) :- e(?X,?Z), tc(?Z,?Y).";

    private static final double THRESHOLD = 0.5;

    public static void main(String[] args) {
        final Options opts = new OptionsBuilder()
                .include(".*")
                .addProfiler(StackProfiler.class)
                .warmupIterations(5)
                .measurementIterations(5)
                .jvmArgs("-server", "-XX:+UseConcMarkSweepGC", "-Xmx1500m", "-Xms1000m")
                .result("target" + "/" + "results.csv")
                .forks(3) // 15
                .resultFormat(ResultFormatType.CSV)
                .build();

        try {
            final Collection<RunResult> result = new Runner(opts).run();
            for (final RunResult rr : result) {
                final BenchmarkResult ar = rr.getAggregatedResult();
                System.out.println("Benchmark score: "
                        + ar.getPrimaryResult().getScore() + " "
                        + ar.getScoreUnit() + " over "
                        + ar.getPrimaryResult().getStatistics().getN() + " iterations");
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


//    @Benchmark
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

    @Benchmark
    public void test_dlogEval() {
        this.hlogEval.query("tc", 2);
    }
}
