package com.github.dritter.hd.dlog;

import java.util.ArrayList;
import java.util.List;

public final class Rule implements IRule {
    private Literal head;
    private List<Literal> body;

    private Rule() {
    }

    /**
     * 
     * @param head
     * @param body
     * @return
     */
    public static Rule create(final Literal head, final Literal... body) {
        final Rule r = new Rule();
        r.head = head;
        r.body = new ArrayList<Literal>();
        for (Literal b : body) {
            r.getBody().add(b);
        }
        return r;
    }

    public static Rule create(final Literal head, final List<Literal> body) {
        final Rule r = new Rule();
        r.head = head;
        r.body = body;
        return r;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IRule#getHead()
     */
    public Literal getHead() {
        return this.head;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IRule#getBody()
     */
    public List<Literal> getBody() {
        return this.body;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IRule#toString()
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.head);

        sb.append(" :- ");

        final int size = this.body.size() - 1;
        for (int i = 0; i < size; ++i) {
            sb.append(this.body.get(i).toString());
            sb.append(", ");
        }
        sb.append(this.body.get(size));
        sb.append(".");

        return sb.toString();
    }
}
