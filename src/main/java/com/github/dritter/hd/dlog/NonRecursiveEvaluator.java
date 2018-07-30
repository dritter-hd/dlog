package com.github.dritter.hd.dlog;

import com.github.dritter.hd.dlog.algebra.*;
import com.github.dritter.hd.dlog.compiler.internal.Compiler;
import com.github.dritter.hd.dlog.compiler.internal.Operator;
import com.github.dritter.hd.dlog.compiler.internal.Util;
import com.github.dritter.hd.dlog.normalizer.internal.Rectifier;
import com.github.dritter.hd.dlog.normalizer.internal.RuleSafetyProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NonRecursiveEvaluator implements IEvaluator {
	private static final Logger log = LoggerFactory.getLogger(NonRecursiveEvaluator.class);
	
    private Collection<IRule> rules;
    private Map<Predicate, List<IRule>> clusteredRules;
    private Map<Predicate, CodeGenerationContext> contexts;
    private Map<Predicate, DataIterator> iteratorTrees;
    
    public NonRecursiveEvaluator(final List<IRule> pRules) {
        this.rules = preprocessRules(pRules);
        this.constructPlansAndIteratorTrees();
    }
    
    // TODO: public???
    private Collection<IRule> preprocessRules(final List<IRule> pRules) {
        // TODO: magic sets optimization
        // TODO: stratification

        final Collection<IRule> limitedRules = new ArrayList<IRule>();
        for (final IRule r : pRules) {
            // try {
            final IRule safeRule = new RuleSafetyProcessor().process(r);
            limitedRules.add(safeRule);
            // } catch (IllegalArgumentException ia) {
            // logger.severe(ia.getMessage());
            // }
        }
        // this.rules = new Rectifier().rectify(pRules);
        final Collection<IRule> rectifiedRules = new Rectifier().rectify(limitedRules);
        return rectifiedRules;
    }
    
    private void constructPlansAndIteratorTrees() {
        
        this.clusteredRules = new HashMap<Predicate, List<IRule>>();
        this.contexts = new HashMap<Predicate, CodeGenerationContext>();
        this.iteratorTrees = new HashMap<Predicate, DataIterator>();
        clusteredRules = Util.clusterRules(this.rules);
        
        for (List<IRule> ruleList : clusteredRules.values()) {
            
            Operator plan = Compiler.compileRectifiedRules(ruleList);
            CodeGenerationContext context = new CodeGenerationContext();
            DataIterator iteratorTree = CodeGenerator.codeGen(plan, context);
            Predicate predicate = ruleList.get(0).getHead().getPredicate();
            contexts.put(predicate, context);
            iteratorTrees.put(predicate, iteratorTree);
            
            log.debug(ruleList.toString());
            log.debug(plan.toString());
        }
    }
    
    // FIXME: changed to synchronized due to concurrency
    // synchronized 
    public static Map<Predicate, DataIterator> translateFacts(final Collection<IFacts> facts) {
        final HashMap<Predicate, DataIterator> factsMap = new HashMap<Predicate, DataIterator>();
        for (IFacts f : facts) {
            // TODO: fake
            FillableTableIterator di = (FillableTableIterator) factsMap.get(f.getPredicate());
            if (di == null) {
                di = new FillableTableIterator();
                factsMap.put(f.getPredicate(), di);
            }
            ParameterValue<?>[] s = null;
            f.getValues().open();
            while ((s = f.getValues().next()) != null) {
                di.add(s);
            }
            f.getValues().close();
            // TODO: fake end

            // factsMap.put(f.getPredicate(), f.getValues());
        }
        return factsMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IEvaluator#eval()
     */
    public final Collection<IFacts> eval(final Collection<IFacts> facts) {
        final List<IFacts> result = new ArrayList<IFacts>();
        final Map<Predicate, DataIterator> factsMap = translateFacts(facts);
        for (List<IRule> r : this.clusteredRules.values()) {
            result.add(this.evalRectRule(r, factsMap));
        }
        return result;
    }
    
    private Facts evalRectRule(final List<IRule> rules, final Map<Predicate, DataIterator> facts) {
        Predicate predicate = rules.get(0).getHead().getPredicate();
        final CodeGenerationContext ctx = contexts.get(predicate);
        final DataIterator result = iteratorTrees.get(predicate);
        ctx.bindFacts(facts);
        return Facts.create(predicate, result);
    }
}
