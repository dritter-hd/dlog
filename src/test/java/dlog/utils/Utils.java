package dlog.utils;

import dlog.algebra.DataIterator;
import dlog.algebra.ParameterValue;
import dlog.algebra.TableIterator;

public class Utils {

    public static <T extends Comparable<? super T>> DataIterator createRelationIterator(T[][] relation) {
        
        ParameterValue<?>[][] edbRelation = new ParameterValue<?>[relation.length][relation[0].length];
        for (int i = 0; i < relation.length; i++) {
            for (int j = 0; j < relation[i].length; j++) {
                edbRelation[i][j] = ParameterValue.create(relation[i][j]);
            }
        }
        
        DataIterator relationIterator = new TableIterator(edbRelation);
        
        return relationIterator;
    }
    
    public static DataIterator createRelationIterator(int[][] relation) {
        
        ParameterValue<?>[][] edbRelation = new ParameterValue<?>[relation.length][relation[0].length];
        for (int i = 0; i < relation.length; i++) {
            for (int j = 0; j < relation[i].length; j++) {
                edbRelation[i][j] = ParameterValue.create(relation[i][j]);
            }
        }
        
        DataIterator relationIterator = new TableIterator(edbRelation);
        
        return relationIterator;
    }
}
