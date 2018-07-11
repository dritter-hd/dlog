package dlog.internal;

import dlog.*;

import java.util.ArrayList;
import java.util.List;

public class RuleSafetyEnsuringProcessor implements RuleSafety {

    public IRule process(IRule rule) {
        RuleSafetyValidator v = new RuleSafetyValidator(rule);

        List<Parameter<?>> unsafe = v.getUnlimitedParameters();

        if (unsafe.size() > 0) {
            List<Literal> body = new ArrayList<Literal>();

            for (Literal literal : rule.getBody())
                body.add(literal);

            for (Parameter<?> param : unsafe) {
                Literal newLiteral = Literal.create(Predicate.create(RuleSafety.UNLIMITED, 1), param);
                body.add(newLiteral);
            }
            return Rule.create(rule.getHead(), body);
        } else
            return rule;
    }
}
