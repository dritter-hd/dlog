package com.github.dritter.hd.dlog.parser.internal;

import java.util.List;

import com.github.dritter.hd.dlog.parser.DlogParser;
import com.github.dritter.hd.dlog.parser.DlogParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dritter.hd.dlog.IRule;

public class NumericParserTest {
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
     * Test simple numeric rule with multiple body literals.
     */
    @Test
    public void testNumericGreaterRule() {
        final String input = "q(A, B) :- q(B, A), >(B, 3).";
        final String expected = "q(A, B) :- q(B, A), B > \"3\".";
        Assert.assertEquals(expected, parseSingleRule(input));
    }

    /**
     * Test simple numeric rule with multiple body literals.
     */
    @Test
    public void testNumericEqualsRule() {
        final String input = "q(A, B) :- q(B, A), =(B, 3).";
        final String expected = "q(A, B) :- q(B, A), B = \"3\".";
        Assert.assertEquals(expected, parseSingleRule(input));
    }

    /**
     * Test simple numeric rule with multiple body literals.
     */
    @Test
    public void testNumericRule() {
        String expected = "q(A, B) :- q(B, A), r(B, \"3\").";
        Assert.assertEquals(expected, parseSingleRule(expected));
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
}
