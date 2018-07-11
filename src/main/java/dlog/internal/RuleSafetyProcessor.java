package dlog.internal;

import dlog.IRule;
import dlog.Parameter;

import java.util.List;

public final class RuleSafetyProcessor implements RuleSafety {
    /**
     * process rule safety.
     * 
     * @param IRule
     * @return IRule
     */
    public IRule process(final IRule rule) {
        final IRuleSafetyValidator v = new RuleSafetyValidator(rule);

        final List<Parameter<?>> unsafe = v.getUnlimitedParameters();

        if (unsafe.size() > 0) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(rule).append(" contains unlimited variable(s): ");

            boolean first = true;
            for (Parameter<?> p : unsafe) {
                if (first) {
                    first = false;
                } else {
                    buffer.append(", ");
                }
                buffer.append(p);
            }
            throw new IllegalArgumentException(buffer.toString());
        }
        return rule;
    }
}
