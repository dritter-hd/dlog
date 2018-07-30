package com.github.dritter.hd.dlog.evaluator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public interface IEvaluator {
    /**
     * Derive facts from knowledge base
     * 
     * @param predicateName
     * @param arity
     * @return
     * @throws InvocationTargetException
     */
    IFacts query(String predicateName, int arity);

    /**
     * Derive facts from knowledge base with additional parameters
     * 
     * @param predicateName
     * @param arity
     * @param params
     * @return
     * @throws InvocationTargetException
     */
    IFacts query(String predicateName, int arity, String[] params);

    /**
     * @param facts
     * @throws InvocationTargetException
     */
    void initalize(Collection<IFacts> facts);
    
    void initalize(Collection<IFacts> facts, String rules);
    
    void initalize(String facts, String rules);
}