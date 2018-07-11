package dlog.internal;

import junit.framework.Assert;

import org.junit.Test;

import dlog.algebra.DataIterator;
import dlog.algebra.NLJoinIterator;
import dlog.algebra.ParameterValue;
import dlog.algebra.ProjectionIterator;
import dlog.algebra.SelectionIterator;
import dlog.algebra.TableIterator;
import dlog.algebra.conditions.ComparisonFormula;
import dlog.algebra.conditions.EqualsFormula;

public final class SimpleAlgebraTest {

    /**
     * Test selection.
     */
    @Test
    public void testSelect() {
        TableIterator t = getTable();
        
        DataIterator o = new SelectionIterator(t, new ComparisonFormula[] { new EqualsFormula(0, ParameterValue.create("abc")) });
        String expected = "abc|def\nabc|ghi\n";
        String actual = serialize(o);
        Assert.assertEquals(expected, actual);
    }
    
    /**
     * Test selection numeric parameters.
     */
    @Test
    public void testSelect_Numeric() {
        TableIterator t = getNumericTable();
        
        DataIterator o = new SelectionIterator(t, new ComparisonFormula[] { new EqualsFormula(0, ParameterValue.create(123)) });
        String expected = "123|456\n123|8910\n";
        String actual = serialize(o);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test projection.
     */
    @Test
    public void testProject() {
        TableIterator t = getTable();
        DataIterator o = new ProjectionIterator(t, new int[] { 0 }, null);
        String expected = "abc\nabc\ndef\n";
        String actual = serialize(o);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test nested loop join.
     */
    @Test
    public void testJoin() {
        TableIterator l = getTable();
        TableIterator r = getTable();
        DataIterator o = new NLJoinIterator(l, r, new ComparisonFormula[] { new EqualsFormula(1, 0) });
        String expected = "abc|def|def|ghi\n";
        String actual = serialize(o);
        Assert.assertEquals(expected, actual);
    }

    /**
     * @return TableIterator
     */
    private static TableIterator getTable() {
        ParameterValue<?>[][] values = new ParameterValue<?>[][] {
            { ParameterValue.create("abc"), ParameterValue.create("def") },
            { ParameterValue.create("abc"), ParameterValue.create("ghi") },
            { ParameterValue.create("def"), ParameterValue.create("ghi") }
        };
        TableIterator t = new TableIterator(values);
        return t;
    }
    
    private static TableIterator getNumericTable() {
        ParameterValue<?>[][] values = new ParameterValue<?>[][] {
            { ParameterValue.create(123), ParameterValue.create(456) },
            { ParameterValue.create(123), ParameterValue.create(8910) },
            { ParameterValue.create(456), ParameterValue.create(8910) }
        };
        TableIterator t = new TableIterator(values);
        return t;
    }

    /**
     * @param o
     * @return String
     */
    private static String serialize(final DataIterator o) {
        StringBuilder sb = new StringBuilder();
        serialize(o, sb);
        return sb.toString();
    }

    /**
     * @param o
     * @param sb
     */
    private static void serialize(final DataIterator o, final StringBuilder sb) {
        o.open();
        ParameterValue<?>[] tuple = o.next();
        while (tuple != null) {
            assert tuple.length > 0;
            serialize(tuple, sb);
            sb.append('\n');
            tuple = o.next();
        }
        o.close();
    }

    /**
     * @param tuple
     * @param sb
     */
    private static void serialize(final ParameterValue<?>[] tuple, final StringBuilder sb) {
        for (int i = 0; i < tuple.length - 1; ++i) {
            sb.append(tuple[i] + "|");
        }
        sb.append(tuple[tuple.length - 1]);
    }
}
