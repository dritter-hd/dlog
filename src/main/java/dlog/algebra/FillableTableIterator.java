package dlog.algebra;

import java.util.ArrayList;

public class FillableTableIterator extends CollectionIterator {
    public FillableTableIterator() {
        // TODO: changed to synchronizedList for concurrency reasons
        // super(Collections.synchronizedList(new
        // CopyOnWriteArrayList<ParameterValue<?>[]>()));
        super(new ArrayList<ParameterValue<?>[]>());
    }

    public FillableTableIterator(final DataIterator di) {
        // TODO: changed to synchronizedList for concurrency reasons
        // super(Collections.synchronizedList(new
        // CopyOnWriteArrayList<ParameterValue<?>[]>()));
        super(new ArrayList<ParameterValue<?>[]>());
        this.addAll(di);
    }

    public final void add(final ParameterValue<?>[] tuple) {
        this.facts.add(tuple);
    }

    public final void addAll(final DataIterator di) {
        di.open();
        ParameterValue<?>[] data;
        while ((data = di.next()) != null) {
            this.add(data);
        }
        di.close();
    }
}
