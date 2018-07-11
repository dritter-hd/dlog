package dlog.internal;

import java.util.Collection;
import java.util.List;

import dlog.parser.DlogParser;
import org.junit.Test;

import dlog.IEvaluator;
import dlog.IFacts;
import dlog.IRule;
import dlog.NaiveRecursiveEvaluator;

public class ParserCommandTest {
    @Test
    public void testConsole() {
        // final String FACTS =
        // "h(6, \"a\"). i(6, 3). h(6, \"b\"). h(6, \"z\").";
        final String FACTS = generateFacts(1000);
        final String RULES = "q(A, B) :- h(B, A), i(B, Z), >(B, 3), =(Z, 3).";
        final DlogParser hp = new DlogParser();

        hp.parse(RULES);
        final List<IRule> rules = hp.getRules();

        hp.parse(FACTS);
        final Collection<IFacts> facts = hp.getFacts();

        final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
        final Collection<IFacts> result = eval.eval(facts);
        System.out.println(result);
    }

    private String generateFacts(final int numberFacts) {
        final StringBuilder sb = new StringBuilder();
        sb.append("i(6, 3). ");
        for (int i = 0; i < numberFacts; ++i) {
            sb.append("h(" + i + ", \"it" + i + "\"). ");
        }
        return sb.toString();
    }
}
