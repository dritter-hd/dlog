package dlog.evaluator;

import junit.framework.Assert;

import org.junit.Test;

public class TestHlogEvaluator {
    private DlogEvaluator eval = DlogEvaluator.create();

    @Test
    public void testContainsText() throws Exception {
        this.eval.initalize("p(\"4711\", \"dcd\"). p(\"4712\", \"ddd\").", "q(X):-p(X, Y), =c(Y, \"c\").");
        final IFacts queryResult = this.eval.query("q", 1);
        Assert.assertTrue(!queryResult.getValues().isEmpty());
        Assert.assertTrue(!queryResult.getValues().equals("[[4711]]"));
    }
}
