package com.github.dritter.hd.dlog.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.dritter.hd.dlog.*;
import org.junit.Assert;
import org.junit.Test;

import com.github.dritter.hd.dlog.IRule;
import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.Parameter;
import com.github.dritter.hd.dlog.Predicate;
import com.github.dritter.hd.dlog.Rule;
import com.github.dritter.hd.dlog.internal.Rectifier;

public final class RectTest {
    /**
     * Test rectification of rules.
     */
    @Test
    public void testRect() {
        Predicate p = Predicate.create("p", 3);
        Predicate p2 = Predicate.create("p", 3);
        Predicate p3 = Predicate.create("p", 2);

        Predicate q = Predicate.create("q", 2);
        Predicate r = Predicate.create("r", 2);

        Parameter<?> parama = Parameter.createConstant("a");
        Parameter<?> paramX = Parameter.createVariable("X");
        Parameter<?> paramY = Parameter.createVariable("Y");
        Parameter<?> paramZ = Parameter.createVariable("Z");

        // p(a, X, Y) :- r(X, Y).
        Literal head = Literal.create(p, parama, paramX, paramY);
        Literal body = Literal.create(r, paramX, paramY);

        List<IRule> rules = new ArrayList<IRule>();
        rules.add(Rule.create(head, body));

        // p(X, Y, X) :- r(Y, X).
        head = Literal.create(p2, paramX, paramY, paramX);
        body = Literal.create(r, paramY, paramX);

        rules.add(Rule.create(head, body));

        // p(X, Y) :- q(X, Z), r(Z, Y).
        head = Literal.create(p3, paramX, paramY);
        body = Literal.create(q, paramX, paramZ);
        Literal body2 = Literal.create(r, paramZ, paramY);

        rules.add(Rule.create(head, body, body2));

        Collection<IRule> result = new Rectifier().rectify(rules);

        Assert.assertEquals(3, result.size());

        // TODO: further checks
    }
}
