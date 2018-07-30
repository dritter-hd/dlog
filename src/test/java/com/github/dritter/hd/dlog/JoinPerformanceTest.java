package com.github.dritter.hd.dlog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

import com.github.dritter.hd.dlog.algebra.DataIterator;
import com.github.dritter.hd.dlog.algebra.NLJoinIterator;
import com.github.dritter.hd.dlog.algebra.conditions.ComparisonFormula;
import com.github.dritter.hd.dlog.algebra.conditions.EqualsFormula;
import com.github.dritter.hd.dlog.algebra.DataIterator;
import com.github.dritter.hd.dlog.algebra.NLJoinIterator;
import com.github.dritter.hd.dlog.algebra.conditions.ComparisonFormula;
import com.github.dritter.hd.dlog.algebra.conditions.EqualsFormula;
import com.github.dritter.hd.dlog.utils.Utils;

public class JoinPerformanceTest {
    
    private DataIterator operation;
    private int measurementsNumber;
    private Formatter output;
    
    public JoinPerformanceTest(int edbRelationsSize, int measurementsNumber) throws IOException {
        
        /*
         * p(X, Y) :- r(X, Z) & s(Z, Y).
         *
         * r(1, 1).
         * r(2, 1).
         *     .
         *     .
         *     .
         * r(N, 1).
         *
         * s(1, 1).
         * s(1, 2).
         *     .
         *     .
         *     .
         * s(1, N).
         */
        
        int[][] relationR = new int[edbRelationsSize][2];
        int[][] relationS = new int[edbRelationsSize][2];
        
        for (int i = 0; i < edbRelationsSize; i++) {
            
            relationR[i][0] = i + 1;
            relationR[i][1] = 1;
        }
        
        for (int i = 0; i < edbRelationsSize; i++) {
            
            relationS[i][0] = 1;
            relationS[i][1] = i + 1;
        }
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        DataIterator relationSIterator = Utils.createRelationIterator(relationS);
        
        ComparisonFormula[] conditions = new ComparisonFormula[1];
        conditions[0] = new EqualsFormula(1, 0);
        
//        List<ComparisonFormula> conditions = new ArrayList<ComparisonFormula>(1);
//        conditions.add(new EqualsFormula(new Variable(1), new Variable(0)));
        
        this.operation = new NLJoinIterator(relationRIterator, relationSIterator, conditions);
        
//        this.operation = new NLJoinIterator(relationRIterator, new int[] { 1 }, relationSIterator, new int[] { 0 });
        
        this.measurementsNumber = measurementsNumber;

        this.output = new Formatter(System.out);
    }
    
    public static void main(String[] args) throws IOException {
        int edbRelationsSize = Integer.parseInt(args[0]);
        int measurementsNumber = Integer.parseInt(args[1]);
        new JoinPerformanceTest(edbRelationsSize, measurementsNumber).testDLogPerformance();
    }
    
    private void testDLogPerformance() {
        for (int i = 0; i < this.measurementsNumber; i++) {
            this.executeMeasurement();
        }
        this.output.flush();
    }
    
    private void executeMeasurement() {
        long startTime = System.nanoTime();
        while (this.operation.next() != null) {}
        long estimatedTime = System.nanoTime() - startTime;
        this.output.format("%11d%n", estimatedTime);
    }
}
