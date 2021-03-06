package com.github.dritter.hd.dlog.internal;

import com.github.dritter.hd.dlog.algebra.ParameterValue;
import junit.framework.Assert;

import org.junit.Test;

import com.github.dritter.hd.dlog.algebra.ParameterValue;

public class SimpleAlgebraFunctionalTest {
    @Test
	public void testNumericParameterValue_Equals() throws Exception {
    	int value = 1;
		Assert.assertTrue(ParameterValue.create(value).equals(ParameterValue.create(value)));
    }
    
    @Test
	public void testNumericParameterValue_NotEquals() throws Exception {
    	int refValue = 1;
		int value = 2;
		Assert.assertFalse(ParameterValue.create(refValue).equals(ParameterValue.create(value)));
    }
}
