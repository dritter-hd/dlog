package dlog;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import dlog.algebra.CollectionIterator;
import dlog.algebra.DataIterator;
import dlog.algebra.ParameterValue;

public class NaiveRecursiveEvaluator implements IEvaluator {
    private IEvaluator evalutor;

    public NaiveRecursiveEvaluator(final List<IRule> rules) {
        this.evalutor = new NonRecursiveEvaluator(rules);
    }

    public final Collection<IFacts> eval(final Collection<IFacts> facts) {
        Collection<IFacts> deltaFacts = this.evalutor.eval(facts);

        final Map<Predicate, DataIterator> total = NonRecursiveEvaluator.translateFacts(facts);
        Map<Predicate, DataIterator> delta = NonRecursiveEvaluator.translateFacts(deltaFacts);
        Map<Predicate, DataIterator> resultFacts = delta;

        while (mergeFacts(total, delta)) {
            final Collection<IFacts> cf = translateSetToFacts(total);
            deltaFacts = this.evalutor.eval(cf);
            delta = NonRecursiveEvaluator.translateFacts(deltaFacts);
            resultFacts.putAll(delta);
        }
        return translateSetToFacts(resultFacts); // translateSetToFacts(total);
    }

    private Collection<IFacts> translateSetToFacts(final Map<Predicate, DataIterator> total) {
        final Collection<IFacts> cf = new ArrayList<IFacts>();

        for (Entry<Predicate, DataIterator> entry : total.entrySet()) {
        	// TOOD: filter duplicate facts
        	cf.add(Facts.create(entry.getKey(), entry.getValue()));        	
        }
        return cf;
    }

    private boolean mergeFacts(final Map<Predicate, DataIterator> total, final Map<Predicate, DataIterator> delta) {
        boolean changed = false;

        for (Entry<Predicate, DataIterator> pred : delta.entrySet()) {
            final DataIterator totalIter = total.get(pred.getKey());

            final DataIterator deltaIter = pred.getValue();
            if (totalIter == null) {
                total.put(pred.getKey(), deltaIter);
                changed = true;
            } else {
                // TODO: changed to Collections.synchronizedSortedSet for concurrency reasons
                // final SortedSet<ParameterValue<?>[]> totalTree = Collections.synchronizedSortedSet(new TreeSet<ParameterValue<?>[]>(new TupleComparator()));
                final TreeSet<ParameterValue<?>[]> totalTree = new TreeSet<ParameterValue<?>[]>(new TupleComparator());
                addIterToSet(totalIter, totalTree);
                changed = addIterToSet(deltaIter, totalTree) || changed;

                final DataIterator dataIter = new CollectionIterator(totalTree);
                total.put(pred.getKey(), dataIter);
            }
        }
        return changed;
    }

    private boolean addIterToSet(final DataIterator iter, final Set<ParameterValue<?>[]> set) {
        boolean changed = false;
        ParameterValue<?>[] tuple = null;

        iter.open();
        while ((tuple = iter.next()) != null) {
            changed = set.add(tuple) || changed;
        }
        iter.close();

        return changed;
    }
    
    @SuppressWarnings("unused")
    private static void print(final Map<Predicate, DataIterator> facts, PrintStream out) {
        for (Entry<Predicate, DataIterator> fact : facts.entrySet()) {
            out.println(fact.getKey());
            final DataIterator di = fact.getValue();
            di.open();
            ParameterValue<?>[] tuple;
            while ((tuple = di.next()) != null) {
                out.print("  ");
                for (ParameterValue<?> v : tuple) {
                    out.print(v.get());
                    out.print(" ");
                }
                out.println("");
            }
            di.close();
        }
    }
}

final class TupleComparator implements Comparator<ParameterValue<?>[]> {
    public TupleComparator() {
    }

    @Override
    public int compare(final ParameterValue<?>[] left, final ParameterValue<?>[] right) {
        final int difference = left.length - right.length;
        return difference != 0 ? difference : comp(left, right);
    }

    /**
     * @param left
     * @param right
     * @return int
     */
    private int comp(final ParameterValue<?>[] left, final ParameterValue<?>[] right) {
        final int length = left.length;
        for (int i = 0; i < length; ++i) {
            ParameterValue<?> leftValue = left[i];
            ParameterValue<?> rightValue = right[i];
            if (leftValue == null || rightValue == null) {
                throw new NullPointerException("Left and Right values must not be null.");
            }
            
            int compare = leftValue.compareTo(rightValue);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }
}