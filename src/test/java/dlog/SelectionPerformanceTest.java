package dlog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

import dlog.algebra.DataIterator;
import dlog.algebra.ParameterValue;
import dlog.algebra.SelectionIterator;
import dlog.algebra.conditions.ComparisonFormula;
import dlog.algebra.conditions.EqualsFormula;
import dlog.utils.Utils;

public class SelectionPerformanceTest {
    
    private DataIterator operation;
    private int measurementsNumber;
    private Formatter output;
    
    public SelectionPerformanceTest(int edbRelationsSize, int measurementsNumber) throws IOException {
        
        /*
         * p(X, Y) :- r(X, Y) & Y=1.
         *
         * r(1, 1).
         * r(2, 1).
         *     .
         *     .
         *     .
         * r(N, 1).
         */
        
        int[][] relationR = new int[edbRelationsSize][2];
        
        for (int i = 0; i < edbRelationsSize; i++) {
            
            relationR[i][0] = i + 1;
            relationR[i][1] = 1;
        }
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        
        ComparisonFormula[] conditions = new ComparisonFormula[1];
        conditions[0] = new EqualsFormula(1, ParameterValue.create(1));
        
//        List<ComparisonFormula> conditions = new ArrayList<ComparisonFormula>(1);
//        conditions.add(new EqualsFormula(new Variable(1), new Constant(ParameterValue.create(1))));
//        
        this.operation = new SelectionIterator(relationRIterator, conditions);
        
//        this.operation = new SelectionIterator(relationRIterator, new int[] { 1 }, new ParameterValue[] { ParameterValue.create(1) });
        
        this.measurementsNumber = measurementsNumber;

        this.output = new Formatter(System.out);
    }
    
    public static void main(String[] args) throws IOException {
        int edbRelationsSize = Integer.parseInt(args[0]);
        int measurementsNumber = Integer.parseInt(args[1]);
        new SelectionPerformanceTest(edbRelationsSize, measurementsNumber).testdLogPerformance();
    }
    
    private void testdLogPerformance() {
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
