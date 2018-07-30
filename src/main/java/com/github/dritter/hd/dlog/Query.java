package com.github.dritter.hd.dlog;

import java.util.ArrayList;
import java.util.List;

public final class Query implements IQuery {
    private List<Literal> body;

    private Query() {
    }

    /**
     * 
     * @param body
     * @return
     */
    public static IQuery create(final Literal... body) {
        final Query r = new Query();
        r.body = new ArrayList<Literal>();
        for (Literal b : body) {
            r.getBody().add(b);
        }
        return r;
    }

    public static IQuery create(final List<Literal> body) {
        final Query r = new Query();
        r.body = body;
        return r;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IRule#getBody()
     */
    /*
     * (non-Javadoc)
     * 
     * @see IQuery#getBody()
     */
    public List<Literal> getBody() {
        return this.body;
    }

    /*
     * (non-Javadoc)
     * 
     * @see IRule#toString()
     */
    /*
     * (non-Javadoc)
     * 
     * @see IQuery#toString()
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("?- ");

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
