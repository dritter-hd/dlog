package com.github.dritter.hd.dlog.algebra;

import com.github.dritter.hd.dlog.algebra.conditions.ComparisonFormula;

import java.util.Arrays;
import java.util.List;

public class NLJoinIterator implements DataIterator {
    
    private List<DataIterator> children;
    private ComparisonFormula[] conditions;
    private ParameterValue<?>[] leftTuple;
    
    public NLJoinIterator(final DataIterator left, final DataIterator right, ComparisonFormula[] conditions) {
        this.children = Arrays.asList(new DataIterator[] { left, right });
        this.conditions = conditions;
    }
    
	@Override
	public final void open() {
		getLeft().open();
		getRight().open();
		this.leftTuple = null;
    }
    
    @Override
    public final ParameterValue<?>[] next() {
        if (this.leftTuple == null) {
            this.leftTuple = getLeft().next();
        }
        
        while (this.leftTuple != null) {
            
            ParameterValue<?>[] rightTuple = getRight().next();
            
            while (rightTuple != null) {
                
                if (matches(this.leftTuple, rightTuple)) {
                    return concat(this.leftTuple, rightTuple);
                }
                rightTuple = getRight().next();
            }
            
            getRight().close();
            this.leftTuple = getLeft().next();
            if (this.leftTuple != null) {
                getRight().open();
            }
        }
        
        return null;
    }
    
    private ParameterValue<?>[] concat(final ParameterValue<?>[] left, final ParameterValue<?>[] right) {
		final int llen = left.length;
		final int rlen = right.length;
		final int combinedLength = llen + rlen;
		// final ParameterValue<?>[] result = Arrays.copyOf(left,
		// combinedLength);
		ParameterValue<?>[] result = new ParameterValue[combinedLength];
		System.arraycopy(left, 0, result, 0, llen);
		for (int i = llen; i < combinedLength; ++i) {
			result[i] = right[i - llen];
		}
		return result;
    }
    
    private boolean matches(final ParameterValue<?>[] leftTuple, final ParameterValue<?>[] rightTuple) {
        for (ComparisonFormula condition : this.conditions) {
            if (!condition.matches(leftTuple, rightTuple)) {
                return false;
            }
        }
        return true;
    }
    
	@Override
	public final void close() {
		getLeft().close();
		getRight().close();
	}

	final DataIterator getLeft() {
		return this.children.get(0);
	}

	final DataIterator getRight() {
		return this.children.get(1);
	}

}
