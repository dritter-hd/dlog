package dlog.algebra;

import java.util.Collections;
import java.util.List;

import dlog.algebra.conditions.ComparisonFormula;

public class SelectionIterator implements DataIterator {
    private List<DataIterator> children;
    private ComparisonFormula[] conditions;
    
    public SelectionIterator(final DataIterator input, ComparisonFormula[] conditions) {
        this.children = Collections.singletonList(input);
        this.conditions = conditions;
    }
    
    @Override
    public final void open() {
        this.getInput().open();
    }
    
    @Override
    public final ParameterValue<?>[] next() {
        ParameterValue<?>[] tuple = this.getInput().next();
        
        while (tuple != null) {
            if (matches(tuple)) {
                return tuple;
            }
            tuple = this.getInput().next();
        }
        return null;
    }
    
    private boolean matches(final ParameterValue<?>[] tuple) {
        for (ComparisonFormula condition : this.conditions) {
            if (!condition.matches(tuple)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final void close() {
        this.getInput().close();
    }

    private DataIterator getInput() {
        return this.children.get(0);
    }
}
