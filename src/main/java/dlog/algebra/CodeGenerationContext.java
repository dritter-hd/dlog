package dlog.algebra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dlog.Predicate;
import dlog.compiler.internal.Util;

public class CodeGenerationContext {
    private Map<Predicate, List<CodeGenerationIterator>> map;

    public CodeGenerationContext() {
        this.map = new HashMap<Predicate, List<CodeGenerationIterator>>();
    }

    public final void register(final Predicate p, final CodeGenerationIterator cgi) {
        List<CodeGenerationIterator> listIter = this.map.get(p);

        if (listIter == null) {
            listIter = Util.singletonList(cgi);
            this.map.put(p, listIter);
        } else {
            listIter.add(cgi);
        }
    }

    public final void bindFacts(final Map<Predicate, DataIterator> facts) {
        for (Predicate key : this.map.keySet()) {
            final List<CodeGenerationIterator> listCgi = this.map.get(key);
            if (listCgi == null || listCgi.size() == 0) {
                throw new IllegalStateException("Iterator list must not be null.");
            } else {
                DataIterator di = facts.get(key);
                if (di == null) {
                    di = new FillableTableIterator();
                }

                if (listCgi.size() == 1) {

                    listCgi.get(0).bind(di);
                } else {
                    for (CodeGenerationIterator iter : listCgi) {
                        final FillableTableIterator fti = new FillableTableIterator(di);
                        iter.bind(fti);
                    }
                }
            }
        }
    }
}
