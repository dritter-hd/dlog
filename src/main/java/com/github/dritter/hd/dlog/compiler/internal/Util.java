package com.github.dritter.hd.dlog.compiler.internal;

import com.github.dritter.hd.dlog.IRule;
import com.github.dritter.hd.dlog.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Util {
    public static HashMap<Predicate, List<IRule>> clusterRules(final Collection<IRule> unrectRules) {
        final HashMap<Predicate, List<IRule>> cluster = new HashMap<Predicate, List<IRule>>();

        for (IRule rule : unrectRules) {
            final Predicate predicate = rule.getHead().getPredicate();
            if (cluster.containsKey(predicate)) {
                cluster.get(predicate).add(rule);
            } else {
                final List<IRule> r = new ArrayList<IRule>();
                r.add(rule);
                cluster.put(predicate, r);
            }
        }
        return cluster;
    }

    public static <T> List<T> singletonList(final T o) {
        final List<T> result = new ArrayList<T>(1);
        result.add(o);
        return result;
    }
}
