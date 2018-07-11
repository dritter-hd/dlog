package dlog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

import dlog.algebra.DataIterator;
import dlog.algebra.ParameterValue;
import dlog.algebra.ProjectionIterator;
import dlog.utils.Utils;

public class ProjectionPerformanceTest {
    
    private DataIterator operation;
    private int measurementsNumber;
    private Formatter output;
    
    public ProjectionPerformanceTest(int edbRelationsSize, int measurementsNumber) throws IOException {
        
        /*
         * p(W, Z) :- r(W, X, Y, Z).
         *
         * r(1, 1, 1, 1).
         * r(2, 2, 2, 2).
         *        .
         *        .
         *        .
         * r(N, N, N, N).
         */
        
        int[][] relationR = new int[edbRelationsSize][4];
        
        for (int i = 0; i < edbRelationsSize; i++) {
            
            relationR[i][0] = i + 1;
            relationR[i][1] = i + 1;
            relationR[i][2] = i + 1;
            relationR[i][3] = i + 1;
        }
        
        DataIterator relationRIterator = Utils.createRelationIterator(relationR);
        
        this.operation = new ProjectionIterator(relationRIterator, new int[] { 0, 3 }, new ParameterValue[0]);
        
        this.measurementsNumber = measurementsNumber;

        this.output = new Formatter(System.out);
    }
    
    public static void main(String[] args) throws IOException {
        int edbRelationsSize = Integer.parseInt(args[0]);
        int measurementsNumber = Integer.parseInt(args[1]);
        new ProjectionPerformanceTest(edbRelationsSize, measurementsNumber).testDlogPerformance();
    }
    
    private void testDlogPerformance() {
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
