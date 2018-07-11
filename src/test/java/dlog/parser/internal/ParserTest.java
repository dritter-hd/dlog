package dlog.parser.internal;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import dlog.parser.DlogParser;
import org.antlr.runtime.RecognitionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dlog.IFacts;
import dlog.IQuery;
import dlog.IRule;

public final class ParserTest {

    /**
     * DlogParser.
     */
    private DlogParser hp = null;

    /**
     * Set up DlogParser for test.
     */
    @Before
    public void setUp() {
        hp = new DlogParser();
    }

    /**
     * Tear down test.
     */
    @After
    public void tearDown() {
    }

    /**
     * Test simple reflexive rule with multiple body literals.
     */
    @Test
    public void testRule() {
        String expected = "q(A, B) :- q(B, A), r(B, A).";
        Assert.assertEquals(expected, parseSingleRule(expected));
    }

    /**
     * Test rules used in Helix (recursive, ...).
     */
    @Test
    public void testHelixRules() {
        String expected = ParserTest.RULES;
        Assert.assertEquals(expected, parseMultipleRules(expected));
    }

    /**
     * Test Rule with variable and constant in head.
     */
    @Test
    public void testRuleWithConstantInHead() {
        String expected = "p(\"a\", Y) :- p(Y, X).";
        Assert.assertEquals(expected, parseSingleRule(expected));
    }

    /**
     * Test rule with variable and constant in body.
     */
    @Test
    public void testRuleWithConstantInBody() {
        String expected = "p(X, Y) :- p(Y, \"a\").";
        Assert.assertEquals(expected, parseSingleRule(expected));
    }

    /**
     * Test rule with negation.
     */
    @Test
    public void testNegatedRule() {
        // TODO: test if integrated in hlog
    }

    /**
     * Test simple Fact.
     */
    @Test
    public void testFact() {
        String expected = "p(\"b\", \"a\"). ";
        hp.parse(expected);
        final Collection<IFacts> facts = hp.getFacts();
        String actual = facts.iterator().next().toString();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test query.
     */
    @Test
    public void testQuery() {
        final String expected = "?- p(X, Y).";
        this.hp.parse(expected);
        final List<IQuery> queries = hp.getQueries();
        final String actual = queries.get(0).toString();
        Assert.assertEquals(expected, actual);
    }
    
    @Ignore
    @Test(expected=RecognitionException.class)
    public void testParsingRuleWithUnusualVariableNames() {
        
        /*
         * p(X, _Y) :- r(X, _Y).
         * 
         * r(a, b).
         * r(c, d).
         * 
         * Expected IDB facts:
         * 
         * p(a, b).
         * p(c, d).
         */
        
        String rule = "p(X, _Y) :- r(X, _Y).";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
    }
    
    @Test
    public void testParsingRuleWithLessThanPredicate() {
        
        String rule = "p(X, Y) :- r(X, Y), <(X, 5).";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
        
        List<IRule> rules = parser.getRules();
        assertEquals(1, rules.size());
        assertEquals("p(X, Y) :- r(X, Y), X < \"5\".", rules.get(0).toString());
    }
    
    @Test
    public void testParsingRuleWithNotEqualsPredicate() {
        
        String rule = "p(X, Y) :- r(X, Y), !=(X, 1).";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
        
        List<IRule> rules = parser.getRules();
        assertEquals(1, rules.size());
        assertEquals("p(X, Y) :- r(X, Y), X != \"1\".", rules.get(0).toString());
    }
    
    @Test
    public void testParsingRuleWithLessThanPredicateAndNotEqualsPredicate() {
        
        String rule = "p(X, Y) :- r(X, Y), <(X, 7), !=(X, 3).";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
        
        List<IRule> rules = parser.getRules();
        assertEquals(1, rules.size());
        assertEquals("p(X, Y) :- r(X, Y), X < \"7\", X != \"3\".", rules.get(0).toString());
    }
    
    @Test
    public void testParsingRuleWithLessThanEqualsPredicate() {
        
        String rule = "p(X, Y) :- r(X, Y), <=(X, 3).";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
        
        List<IRule> rules = parser.getRules();
        assertEquals(1, rules.size());
        assertEquals("p(X, Y) :- r(X, Y), X <= \"3\".", rules.get(0).toString());
    }
    
    @Test
    public void testParsingRuleWithGreaterThanEqualsPredicate() {
        
        String rule = "p(X, Y) :- r(X, Y), >=(X, 5).";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
        
        List<IRule> rules = parser.getRules();
        assertEquals(1, rules.size());
        assertEquals("p(X, Y) :- r(X, Y), X >= \"5\".", rules.get(0).toString());
    }
    
    @Test
    public void testParsingRuleWithLessThanEqualsPredicateAndGreaterThanEqualsPredicate() {
        
        String rule = "p(X, Y) :- r(X, Y), <=(X, 5), >=(X, 3).";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
        
        List<IRule> rules = parser.getRules();
        assertEquals(1, rules.size());
        assertEquals("p(X, Y) :- r(X, Y), X <= \"5\", X >= \"3\".", rules.get(0).toString());
    }
    
    @Test
    public void testParsingRuleWithCharacterContains() {
        
        String rule = "p(X, Y) :- r(X, Y), =c(X, \"3\").";
        
        DlogParser parser = new DlogParser();
        parser.parse(rule);
        
        List<IRule> rules = parser.getRules();
        assertEquals(1, rules.size());
        assertEquals("p(X, Y) :- r(X, Y), X =c \"3\".", rules.get(0).toString());
    }
    
    
    /**
     * @param expected
     * @return String
     */
    private String parseSingleRule(final String expected) {
        hp.parse(expected);
        final List<IRule> rules = this.hp.getRules();
        return rules.get(0).toString();
    }

    /**
     * @param expected
     * @return String
     */
    private String parseMultipleRules(final String expected) {
        hp.parse(expected);
        final List<IRule> rules = hp.getRules();
        final String value = rules.toString();
        final String substringValue = value.substring(1, value.length() - 1);
        return substringValue.replace("., ", ".");
    }

    /**
     * Rules used in Helix. TODO: get from Helix HlogEvaluator!!!
     */
    static final String RULES = "same-system(lkey1, lkey2) :- same-system-disc(lkey1, lkey2)."
            + "same-system(lkey1, lkey2) :- same-system(lkey2, lkey1)."
            + "same-system(lkey1, lkey2) :- same-system(lkey1, lkey3), same-system(lkey3, lkey2)."
            + "same-system(lkey, lkey) :- system-disc(lkey, x)."

            + "same-host(pkey1, pkey2) :- same-host-disc(pkey1, pkey2)."
            + "same-host(pkey1, pkey2) :- same-host(pkey2, pkey1)."
            + "same-host(pkey1, pkey2) :- same-host(pkey1, pkey3), same-host(pkey3, pkey2)."
            + "same-host(pkey, pkey) :- host-disc(pkey, x)."
            + "same-host(pkey1, pkey2) :- runs-on-disc(lkey1, pkey1), runs-on-disc(lkey2, pkey2), "
            + "same-system(lkey1, lkey2)."

            + "message-flow(lkeysender, lkeyreceiver) :- outgoing-disc(lkeysender, RCONF), "
            + "receiver-disc(RCONF, lkeyreceiver)."
            + "message-flow(lkeysender, lkeyreceiver) :- incoming-disc(lkeyreceiver, SCONF), "
            + "sender-disc(SCONF, lkeysender)."

            + "message-flow-host(pkeysender, pkeyreceiver) :- runs-on-disc(lkeysender, pkeysender), "
            + "outgoing-disc(lkeysender, RCONF), receiver-host-disc(RCONF, pkeyreceiver)."
            + "message-flow-host(pkeysender, pkeyreceiver) :- message-flow(lkeysender, lkeyreceiver), "
            + "runs-on-disc(lkeysender, pkey1sender), same-host(pkey1sender, pkeysender), "
            + "runs-on-disc(lkeyreceiver, pkey1receiver), same-host(pkey1receiver, pkeyreceiver)."

            // TODO: negation not yet supported
            // +
            // "empty-host(pkey1) :- host-disc(pkey1, uri), same-host(pkey1, pkey2), not runs-on-disc(lkey, pkey2)."

            // new disc rules
            + "integration-flow(systemIdSender, systemIdReceiver, systemIdMw, URI) :- message-flow-disc(systemIdSender, systemIdMw, URI), message-flow-disc(systemIdMw, systemIdReceiver, URI)."

            + "message-flow(systemIdSender, systemIdReceiver) :- message-flow-disc(systemIdSender, systemIdReceiver, configUri)."

            + "sender(SCONF, systemIdSender) :- message-flow(systemIdSender, systemIdReceiver, opsURI), incoming(systemIdReceiver, SCONF), match(opsURI, SCONF)."

            + "sender(SCONF, systemIdSender) :- sender-disc(SCONF, systemIdSender)."

            + "receiver(RCONF, systemIdReceiver) :- message-flow(systemIdSender, systemIdReceiver, opsURI), outgoing(systemIdSender, RCONF), match(opsURI, RCONF)."

            + "receiver(RCONF, systemIdReceiver) :- receiver-disc(RCONF, systemIdReceiver)."

            + "integration-process(SCONF, RCONF, opsURI) :- message-flow(systemIdSender, systemIdMw, opsURI), "
            + "message-flow(systemIdMw, systemIdReceiver, opsURI), incoming-disc(systemIdMw, SCONF), "
            + "sender(SCONF, systemIdSender), outgoing-disc(systemIdMw, RCONF), receiver(RCONF, systemIdReceiver)."

            // new user rules
            + "same-system(lkey1, lkey2) :- same-system-user(lkey1, lkey2)." + "same-host(pkey1, pkey2) :- same-host-user(pkey1, pkey2)."
            + "runs-on-disc(lkey1, pkey1) :- runs-on-user(lkey1, pkey1).";
}
