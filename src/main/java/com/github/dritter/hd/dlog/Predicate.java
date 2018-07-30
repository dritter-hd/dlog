package com.github.dritter.hd.dlog;

public class Predicate {
    private String name;
    private int arity;

    private Predicate() {
    }

    public static Predicate create(final String name, final int arity) {
        final Predicate p = new Predicate();
        p.name = name;
        p.arity = arity;
        return p;
    }

    public final int hashCode() {
        return this.name.hashCode() + this.arity;
    }

    public final boolean equals(final Object o) {
        if (o instanceof Predicate) {
            final Predicate p = (Predicate) o;
            return p.arity == this.arity && p.name.equals(this.name);
        }
        return false;
    }

    public final String getName() {
        return this.name;
    }

    public final int getArity() {
        return this.arity;
    }

    public final String toString() {
        return this.name + ":" + this.arity;
    }
}
