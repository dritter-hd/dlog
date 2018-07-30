package com.github.dritter.hd.dlog.internal;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import com.github.dritter.hd.dlog.IEvaluator;
import com.github.dritter.hd.dlog.IFacts;
import com.github.dritter.hd.dlog.IRule;
import com.github.dritter.hd.dlog.NaiveRecursiveEvaluator;
import com.github.dritter.hd.dlog.parser.DlogParser;
import com.github.dritter.hd.dlog.parser.DlogParser;
import org.junit.Before;
import org.junit.Test;

import com.github.dritter.hd.dlog.IEvaluator;
import com.github.dritter.hd.dlog.IFacts;
import com.github.dritter.hd.dlog.IRule;
import com.github.dritter.hd.dlog.NaiveRecursiveEvaluator;

public class SystematicEvalTest {
	 private DlogParser hp;

	    static final String FACTS = "runs-on-discx(\"sys1\", \"host1\"). runs-on-discx(\"sys2\", \"host2\"). same-systemx(\"sys1\", \"sys2\").";
	    static final String RULE = "same-hostx(pkey1, pkey2) :- runs-on-discx(lkey1, pkey1), runs-on-discx(lkey2, pkey2), same-systemx(lkey1, lkey2).";

	    @Before
	    public void setUp() {
	        this.hp = new DlogParser();
	    }

	    @Test
		public void testArrayCopy_compatibility() throws Exception {
	    	final String rule = "isMarried(Y, Z) :- hasSon(Y, X), hasSon(Z, X).";
			this.hp.parse(rule);
			final List<IRule> rules = this.hp.getRules();
			
			final String fact = "hasSon(\"marge\", \"bart\"). hasSon(\"homer\", \"bart\").";
			this.hp.parse(fact);
			final Collection<IFacts> facts = this.hp.getFacts();
			
			final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
	        final Collection<IFacts> result = eval.eval(facts);
	        
	        assertEquals(1, result.size());
	        final String expected = "[isMarried(\"marge\", \"marge\"). isMarried(\"marge\", \"homer\"). isMarried(\"homer\", \"marge\"). isMarried(\"homer\", \"homer\"). ]";
			assertEquals(expected, result.toString());
	        System.out.println(result);
		}
	    
	    
	    /**
	     * Test combination of parser and evaluation for same host fact.
	     */
	    @Test
	    public void testSameHost_twoJoin() {
	        this.hp.parse(SystematicEvalTest.RULE);
	        final List<IRule> rules = this.hp.getRules();

	        this.hp.parse(SystematicEvalTest.FACTS);
	        final Collection<IFacts> facts = this.hp.getFacts();

	        final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
	        final Collection<IFacts> result = eval.eval(facts);
	        assertEquals(1, result.size());
	        assertEquals("[same-hostx(\"host1\", \"host2\"). ]", result.toString());
	        System.out.println(result);
	    }
	    
	    @Test
		public void testSameHost_simple() throws Exception {
			final String rule = "same-hostx(pkey1, pkey2) :- same-host-discx(pkey1, pkey2, origin).";
			this.hp.parse(rule);
			final List<IRule> rules = this.hp.getRules();
			
			final String fact = "same-host-discx(\"host1\", \"host2\", \"origin\"). same-host-discx(\"host3\", \"host4\", \"origin2\").";
			this.hp.parse(fact);
			final Collection<IFacts> facts = this.hp.getFacts();
			
			final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
	        final Collection<IFacts> result = eval.eval(facts);
	        
	        assertEquals(1, result.size());
	        final String expected = "[same-hostx(\"host1\", \"host2\"). same-hostx(\"host3\", \"host4\"). ]";
			assertEquals(expected, result.toString());
	        System.out.println(result);
		}
	    
	    @Test
		public void testSameHost_simpleName() throws Exception {
	    	final String rule = "same-hostx(pkey, pkey) :- host-discx(pkey, desc, agent, uri).";
			this.hp.parse(rule);
			final List<IRule> rules = this.hp.getRules();
			
			final String fact = "host-discx(\"host1\", \"desc1\", \"origin1\", \"uri1\"). host-discx(\"host1\", \"desc2\", \"origin2\", \"uri2\").";
			this.hp.parse(fact);
			final Collection<IFacts> facts = this.hp.getFacts();
			
			
	        final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
			final Collection<IFacts> result = eval.eval(facts);
	        
	        assertEquals(1, result.size());
	        final String expected = "[same-hostx(\"host1\", \"host1\"). same-hostx(\"host1\", \"host1\"). ]";
			assertEquals(expected, result.toString());
	        System.out.println(result);
		}
	    
	    @Test
		public void testSameHost_transitive() throws Exception {
	    	String rule = "same-hostx(pkey1, pkey2) :- same-hostx(pkey1, pkey3), same-hostx(pkey3, pkey2).";
	    	rule += "same-hostx(pkey, pkey) :- host-discx(pkey, desc, agent, uri).";
	    	rule += "same-hostx(pkey1, pkey2) :- same-host-discx(pkey1, pkey2, origin).";
	    	rule += "same-hostx(pkey1, pkey2) :- runs-on-discx(lkey1, pkey1), runs-on-discx(lkey2, pkey2), same-systemx(lkey1, lkey2).";
	    	
			this.hp.parse(rule);
			final List<IRule> rules = this.hp.getRules();

			final String fact = "same-hostx(\"host1\", \"host2\"). same-hostx(\"host2\", \"host4\").";
			this.hp.parse(fact);
			final Collection<IFacts> facts = this.hp.getFacts();
			
			final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
	        final Collection<IFacts> result = eval.eval(facts);
	        
	        assertEquals(1, result.size());
	        final String expected = "[same-hostx(\"host1\", \"host4\"). ]";
			assertEquals(expected, result.toString());
	        System.out.println(result);
		}
	    
	    @Test
		public void testSameHost_reflexive() throws Exception {
	    	String rule = "same-hostx(pkey1, pkey2) :- same-hostx(pkey2, pkey1).";
	    	rule += "same-hostx(pkey1, pkey2) :- same-hostx(pkey1, pkey3), same-hostx(pkey3, pkey2).";
	    	rule += "same-hostx(pkey, pkey) :- host-discx(pkey, desc, agent, uri).";
	    	rule += "same-hostx(pkey1, pkey2) :- same-host-discx(pkey1, pkey2, origin).";
	    	rule += "same-hostx(pkey1, pkey2) :- runs-on-discx(lkey1, pkey1), runs-on-discx(lkey2, pkey2), same-systemx(lkey1, lkey2).";
	    	
			this.hp.parse(rule);
			final List<IRule> rules = this.hp.getRules();

			final String fact = "same-hostx(\"host1\", \"host2\"). same-hostx(\"host2\", \"host1\").";
			this.hp.parse(fact);
			final Collection<IFacts> facts = this.hp.getFacts();
			
			final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
	        final Collection<IFacts> result = eval.eval(facts);
	        
	        assertEquals(1, result.size());
            final String expected = "[same-hostx(\"host1\", \"host1\"). same-hostx(\"host2\", \"host1\"). same-hostx(\"host1\", \"host2\"). same-hostx(\"host2\", \"host2\"). same-hostx(\"host1\", \"host1\"). same-hostx(\"host1\", \"host2\"). same-hostx(\"host1\", \"host1\"). same-hostx(\"host1\", \"host2\"). same-hostx(\"host2\", \"host1\"). same-hostx(\"host2\", \"host2\"). same-hostx(\"host2\", \"host1\"). same-hostx(\"host2\", \"host2\"). ]";
            assertEquals(expected, result.toString());
            System.out.println(result);
		} 
}
