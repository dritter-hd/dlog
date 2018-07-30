package com.github.dritter.hd.dlog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import com.github.dritter.hd.dlog.algebra.DataIterator;
import com.github.dritter.hd.dlog.algebra.DataIterator;
import com.github.dritter.hd.dlog.utils.Utils;

public class DlogPerformanceTest {
    
    private ArrayList<IFacts> edbRelations;
    private IEvaluator evaluator;
    private int measurementsNumber;
    private Formatter output;
    
    public DlogPerformanceTest(int edbRelationsSize, int measurementsNumber) throws IOException {
        
        /*
         * p(1, Y) :- r(X, Y).
         * p(X, Y) :- s(X, Z) & r(Z, Y).
         * q(X, X) :- p(X, 2).
         * q(X, Y) :- p(X, Z) & s(Z, Y).
         *
         * r(1, 1).
         * r(1, 2).
         *     .
         *     .
         *     .
         * r(1, N).
         *
         * s(1, 1).
         * s(2, 1).
         *     .
         *     .
         *     .
         * s(N, 1).
         */
        
        Predicate p = Predicate.create("p", 2);
        Predicate r = Predicate.create("r", 2);
        Predicate s = Predicate.create("s", 2);
        Predicate q = Predicate.create("q", 2);
        
        Parameter<?> argumentX = Parameter.createVariable("X");
        Parameter<?> argumentY = Parameter.createVariable("Y");
        Parameter<?> argumentZ = Parameter.createVariable("Z");
        Parameter<?> argument1 = Parameter.createConstant(1);
        Parameter<?> argument2 = Parameter.createConstant(2);
        
        Literal head1     = Literal.create(p, argument1, argumentY);
        Literal subgoal11 = Literal.create(r, argumentX, argumentY);
        
        Literal head2     = Literal.create(p, argumentX, argumentY);
        Literal subgoal21 = Literal.create(s, argumentX, argumentZ);
        Literal subgoal22 = Literal.create(r, argumentZ, argumentY);
        
        Literal head3     = Literal.create(q, argumentX, argumentX);
        Literal subgoal31 = Literal.create(p, argumentX, argument2);
        
        Literal head4     = Literal.create(q, argumentX, argumentY);
        Literal subgoal41 = Literal.create(p, argumentX, argumentZ);
        Literal subgoal42 = Literal.create(s, argumentZ, argumentY);
        
        IRule rule1 = Rule.create(head1, subgoal11);
        IRule rule2 = Rule.create(head2, subgoal21, subgoal22);
        IRule rule3 = Rule.create(head3, subgoal31);
        IRule rule4 = Rule.create(head4, subgoal41, subgoal42);
        
        List<IRule> rules = new ArrayList<IRule>();
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        rules.add(rule4);
        
        int[][] relationR = new int[edbRelationsSize][2];
        int[][] relationS = new int[edbRelationsSize][2];
        
        for (int i = 0; i < edbRelationsSize; i++) {
            
            relationR[i][0] = 1;
            relationR[i][1] = i + 1;
        }
        
        for (int i = 0; i < edbRelationsSize; i++) {
            
            relationS[i][0] = i + 1;
            relationS[i][1] = 1;
        }
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        DataIterator relationSIterator = Utils.createRelationIterator(relationS);
        
        IFacts relationRFacts = Facts.create(r, relationRIterator);
        IFacts relationSFacts = Facts.create(s, relationSIterator);
        
        this.edbRelations = new ArrayList<IFacts>();
        edbRelations.add(relationRFacts);
        edbRelations.add(relationSFacts);
        
        this.evaluator = new NaiveRecursiveEvaluator(rules);
        
        this.measurementsNumber = measurementsNumber;
        
        this.output = new Formatter(System.out);
    }
    
    public static void main(String[] args) throws IOException {
        int edbRelationsSize = Integer.parseInt(args[0]);
        int measurementsNumber = Integer.parseInt(args[1]);
        new DlogPerformanceTest(edbRelationsSize, measurementsNumber).testDLogPerformance();
    }
    
    private void testDLogPerformance() {
        for (int i = 0; i < this.measurementsNumber; i++) {
            this.executeMeasurement();
        }
        this.output.flush();
    }
    
    private void executeMeasurement() {
        long startTime = System.nanoTime();
        this.evaluator.eval(this.edbRelations);
        long estimatedTime = System.nanoTime() - startTime;
        this.output.format("%11d%n", estimatedTime);
    }
}
