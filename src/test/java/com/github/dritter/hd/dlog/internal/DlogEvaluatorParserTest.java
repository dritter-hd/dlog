package com.github.dritter.hd.dlog.internal;

import java.util.Collection;
import java.util.Iterator;

import com.github.dritter.hd.dlog.evaluator.DlogEvaluator;
import com.github.dritter.hd.dlog.evaluator.IFacts;
import com.github.dritter.hd.dlog.parser.DlogEvaluatorParser;
import com.github.dritter.hd.dlog.parser.DlogParser;
import com.github.dritter.hd.dlog.evaluator.DlogEvaluator;
import com.github.dritter.hd.dlog.parser.DlogParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.dritter.hd.dlog.evaluator.IFacts;
import com.github.dritter.hd.dlog.parser.DlogEvaluatorParser;

public class DlogEvaluatorParserTest {
    private DlogEvaluatorParser evalParser;
    private DlogParser parser;

    @Before
    public void setup() throws Exception {
        this.evalParser = DlogEvaluatorParser.create();
        this.parser = new DlogParser();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_null() throws Exception {
        this.evalParser.parse(null);
    }

    @Test
    public void testParse_empty() throws Exception {
        this.evalParser.parse("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParse_rule() throws Exception {
        this.evalParser.parse("q(A, B) :- q(B, A), r(B, A).");
    }

    @Test
    public void testParse_facts() throws Exception {
        this.evalParser.parse("q(\"hasi\", 17).r(23, \"dritter\").");
        final Collection<IFacts> facts = this.evalParser.getFacts();
        Assert.assertNotNull(facts);
        Assert.assertEquals(2, facts.size());

        final Iterator<IFacts> iterator = facts.iterator();
        Assert.assertEquals(1, iterator.next().getValues().size());
        Assert.assertEquals(1, iterator.next().getValues().size());
    }

    @Test
    public void testParse_factsMulti() throws Exception {
        this.evalParser.parse("q(\"hasi\", 17).q(\"dritter\", 23).r(23, \"dritter\").r(17, \"hasi\").");
        final Collection<IFacts> facts = this.evalParser.getFacts();
        Assert.assertNotNull(facts);
        Assert.assertEquals(2, facts.size());

        final Iterator<IFacts> iterator = facts.iterator();
        Assert.assertEquals(2, iterator.next().getValues().size());
        Assert.assertEquals(2, iterator.next().getValues().size());
    }

    @Test
    public void testParse_factsEval() throws Exception {
        this.evalParser.parse("q(\"hasi\", 23).r(23, \"dritter\").");
        final Collection<IFacts> facts = this.evalParser.getFacts();
        Assert.assertNotNull(facts);
        Assert.assertEquals(2, facts.size());

        final DlogEvaluator eval = DlogEvaluator.create();
        eval.initalize(facts, "qr(A,Z) :- q(A,N), r(N, Z).");
        final IFacts query = eval.query("qr", 2);
        Assert.assertEquals(1, query.getValues().size());
    }

    @Test
    public void testParseResult_equals() throws Exception {
        this.evalParser.parse("q(\"dritter\", 17).r(23, \"dritter\").");
        this.parser.parse("q(\"dritter\", 17).r(23, \"dritter\").");

        final Collection<IFacts> evalFacts = this.evalParser.getFacts();
        final Collection<com.github.dritter.hd.dlog.IFacts> internalFacts = this.parser.getFacts();

        // Assert.assertEquals(internalFacts.toString(), evalFacts.toString());

        final DlogEvaluator eval = DlogEvaluator.create();
        eval.initalize(evalFacts, "qr(A,Z) :- q(A,N), r(Z, A).");
        final IFacts query = eval.query("qr", 2);
        Assert.assertEquals(1, query.getValues().size());

        final DlogEvaluator eval2 = DlogEvaluator.create();
        eval2._initalize(internalFacts, "qr(A,Z) :- q(A,N), r(Z, A).");
        final IFacts query2 = eval2.query("qr", 2);
        Assert.assertEquals(1, query2.getValues().size());
    }

    @Test
    public void testParseResult_selection() throws Exception {
        this.evalParser.parse("q(\"hasi\", 17).q(\"dritter\", 23).");
        this.parser.parse("q(\"hasi\", 17).q(\"dritter\", 23).");

        final Collection<IFacts> evalFacts = this.evalParser.getFacts();
        final Collection<com.github.dritter.hd.dlog.IFacts> internalFacts = this.parser.getFacts();

        // Assert.assertEquals(internalFacts.toString(), evalFacts.toString());

        final DlogEvaluator eval = DlogEvaluator.create();
        eval.initalize(evalFacts, "qr(A,N) :- q(A,N), =c(A, \"has\").");
        final IFacts query = eval.query("qr", 2);
        Assert.assertEquals(1, query.getValues().size());

        final DlogEvaluator eval2 = DlogEvaluator.create();
        eval2._initalize(internalFacts, "qr(A,N) :- q(A,N), =c(A, \"has\").");
        final IFacts query2 = eval2.query("qr", 2);
        Assert.assertEquals(1, query2.getValues().size());
    }

    @Test
    public void testParseResult_numericalExpression() throws Exception {
        this.evalParser.parse("q(\"hasi\", 17).q(\"dritter\", 23).");
        this.parser.parse("q(\"hasi\", 17).q(\"dritter\", 23).");

        final Collection<IFacts> evalFacts = this.evalParser.getFacts();
        final Collection<com.github.dritter.hd.dlog.IFacts> internalFacts = this.parser.getFacts();

        // Assert.assertEquals(internalFacts.toString(), evalFacts.toString());

        final DlogEvaluator eval = DlogEvaluator.create();
        eval.initalize(evalFacts, "qr(A,N) :- q(A,N), >(N, 17).");
        final IFacts query = eval.query("qr", 2);
        Assert.assertEquals(1, query.getValues().size());

        final DlogEvaluator eval2 = DlogEvaluator.create();
        eval2._initalize(internalFacts, "qr(A,N) :- q(A,N), >(N, 17).");
        final IFacts query2 = eval2.query("qr", 2);
        Assert.assertEquals(1, query2.getValues().size());
    }

    @Test
    public void testParseResult_noDuplicateRuleResults() throws Exception {
        this.evalParser.parse("q(\"hasi\", 17).q(\"hasi\", 27).q(\"dritter\", 23).");
        this.parser.parse("q(\"hasi\", 17).q(\"hasi\", 27).q(\"dritter\", 23).");

        final Collection<IFacts> evalFacts = this.evalParser.getFacts();
        final Collection<com.github.dritter.hd.dlog.IFacts> internalFacts = this.parser.getFacts();

        // Assert.assertEquals(internalFacts.toString(), evalFacts.toString());

        final DlogEvaluator eval = DlogEvaluator.create();
        eval.initalize(evalFacts, "qr(A) :- q(A,N).");
        final Collection<com.github.dritter.hd.dlog.IFacts> query = eval.evaluateRules();
        Assert.assertEquals("('hasi')\n('hasi')\n('dritter')\n", query.iterator().next().getValues().toString());

        final DlogEvaluator eval2 = DlogEvaluator.create();
        eval2._initalize(internalFacts, "qr(A) :- q(A,N).");
        final Collection<com.github.dritter.hd.dlog.IFacts> query2 = eval.evaluateRules();
        // FIXME: Assert.assertEquals("('hasi')\n('hasi')\n('dritter')\n",
        // query2.iterator().next().getValues().toString());
    }

    @Test
    public void testParseResult_queryNoRules() throws Exception {
        this.evalParser.parse("q(\"hasi\", 17).q(\"dritter\", 23).");
        this.parser.parse("q(\"hasi\", 17).q(\"dritter\", 23).");

        final Collection<IFacts> evalFacts = this.evalParser.getFacts();
        final Collection<com.github.dritter.hd.dlog.IFacts> internalFacts = this.parser.getFacts();

        // Assert.assertEquals(internalFacts.toString(), evalFacts.toString());

        final DlogEvaluator eval = DlogEvaluator.create();
        eval.initalize(evalFacts, "");
        final IFacts query = eval.query("q", 2);
        Assert.assertEquals(2, query.getValues().size());

        final DlogEvaluator eval2 = DlogEvaluator.create();
        eval2._initalize(internalFacts, "");
        final IFacts query2 = eval2.query("q", 2);
        Assert.assertEquals(2, query2.getValues().size());
    }

    @Test
    public void testTypeSerialization() throws Exception {
        this.evalParser.parse("q(17,\"hasi\").q(23,\"hasi\").");
        final IFacts original = this.evalParser.getFacts().iterator().next();

        final DlogEvaluatorParser localParser = DlogEvaluatorParser.create();
        localParser.parse(original.toString());
        localParser.getFacts().iterator().next();
        Assert.assertEquals(original.toString(), localParser.getFacts().iterator().next().toString());
    }

    @Ignore
    @Test
    public void testEval() throws Exception {
        String facts = "position(0, 0, 0)." + "position(0, 0, 1)." + "position(0, -1, 2)." + "position(0, -1, 3)." + "position(0, -2, 4)."
                + "position(0, -2, 5)." + "position(0, -3, 6)." + "position(0, -3, 7)." + "position(0, -4, 8)." + "position(-1, -5, 9)."
                + "position(0, -1, 10)." + "position(0, 3, 11)." + "position(0, 7, 12)." + "position(0, 11, 13)." + "position(0, 15, 14)."
                + "position(0, 14, 15)." + "position(0, 14, 16)." + "position(0, 14, 17)." + "position(0, 14, 18)."
                + "position(7, 13, 19)." + "position(15, 13, 20)." + "position(23, 13, 21)." + "position(30, 13, 22)."
                + "position(38, 13, 23)." + "position(46, 13, 24)." + "position(54, 13, 25)." + "position(61, 12, 26)."
                + "position(69, 12, 27)." + "position(77, 12, 28)." + "position(84, 12, 29)." + "position(92, 12, 30).";

        this.evalParser.parse(facts);

        String ruleString = "position-qad-0-0(Player,X,Y):-position(Player,X,Y), >(X,-525), <(X,515), >(Y,-340), <(Y,330).";

        DlogEvaluator evaluator = DlogEvaluator.create();
        Collection<IFacts> facts2 = this.evalParser.getFacts();
        evaluator.initalize(facts2, ruleString);
        Collection<com.github.dritter.hd.dlog.IFacts> correspondingFacts = evaluator.evaluateRules();

        Assert.assertTrue(correspondingFacts.size() > 1);
    }

    @Test
    public void testName() throws Exception {
        final String facts = "TRAJECTORIES(-3.0286579501667053E17, 3.4169866605512161E18). Pos-subObj(1.26606916742304845E18, -2.468041330049512E17, 0, 0, 0). Pos-subObj(3.7110172570606812E18, -2.468041330049512E17, 0, 0, 1). Pos-subObj(6.5300030972400282E18, -2.468041330049512E17, -1, 0, 2). Pos-subObj(-2.0373265970706967E18, -2.468041330049512E17, -1, 0, 3). Pos-subObj(-7.4769713543983729E18, -2.468041330049512E17, -2, 0, 4). Pos(-2.468041330049512E17, 5.4261754488193198E18). root(3.4169866605512161E18). PLAYER(5.4261754488193198E18, 1, -3.0286579501667053E17, 0, 23208).";
        this.evalParser.getFacts();
        
        final String ruleString = "position(Player,X,Y,Time):-Pos-subObj(Uid,Pos2Id,Y,X,Time), Pos(Pos2Id,PlayerId), PLAYER(PlayerId,Player,B,C,D).position(Player,X,Y,Time):-Pos-subObj(Uid,Pos2Id,Y,X,Time), Pos2(Pos2Id,PlayerId), PLAYER(PlayerId,Player,A,B,C,D).";
    }
}
