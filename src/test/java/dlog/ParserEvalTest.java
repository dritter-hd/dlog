package dlog;

import java.util.Collection;
import java.util.List;

import dlog.parser.DlogParser;
import org.junit.Before;
import org.junit.Test;

public final class ParserEvalTest {
    private DlogParser hp;

    static final String FACTS = "runs-on-disc(\"sys1\", \"host1\"). runs-on-disc(\"sys2\", \"host2\"). same-system(\"sys1\", \"sys2\").";
    static final String RULE = "same-host(pkey1, pkey2) :- runs-on-disc(lkey1, pkey1), runs-on-disc(lkey2, pkey2), same-system(lkey1, lkey2).";

    @Before
    public void setUp() {
        this.hp = new DlogParser();
    }

    /**
     * Test combination of parser and evaluation for same host fact.
     */
    @Test
    public void testSameHost() {
        this.hp.parse(ParserEvalTest.RULE);
        final List<IRule> rules = this.hp.getRules();

        this.hp.parse(ParserEvalTest.FACTS);
        final Collection<IFacts> facts = this.hp.getFacts();

        final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(facts);
        System.out.println(result);
    }

    @Test
    public void testNumericRule() {
        this.hp.parse("result(X) :- add(X, Y).");
        final List<IRule> rules = this.hp.getRules();

        this.hp.parse("add(1, 2).");
        final Collection<IFacts> facts = this.hp.getFacts();

        final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(facts);
        System.out.println(result);
    }
}
