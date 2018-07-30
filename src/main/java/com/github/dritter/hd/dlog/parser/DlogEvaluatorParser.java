package com.github.dritter.hd.dlog.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dritter.hd.dlog.Predicate;
import com.github.dritter.hd.dlog.algebra.ParameterValue;
import com.github.dritter.hd.dlog.evaluator.IFacts;
import com.github.dritter.hd.dlog.evaluator.SimpleFacts;
import com.github.dritter.hd.dlog.parser.datalogParser.program_return;

@SuppressWarnings("unchecked")
public class DlogEvaluatorParser {
    private static final Logger log = LoggerFactory.getLogger(DlogEvaluatorParser.class);

    private Map<Predicate, IFacts> factsResult = new HashMap<Predicate, IFacts>();

    private DlogEvaluatorParser() {
    }

    public static DlogEvaluatorParser create() {
        return new DlogEvaluatorParser();
    }

    public final Collection<IFacts> getFacts() {
        final List<IFacts> facts = new ArrayList<IFacts>();
        for (final Entry<Predicate, IFacts> entry : this.factsResult.entrySet()) {
            facts.add(entry.getValue());
        }
        return facts;
    }

    public final void parse(final String program) {
        if (program == null) {
            throw new IllegalArgumentException("Datalog program must not be null.");
        }

        final CharStream cs = new ANTLRStringStream(program);
        final datalogLexer dl = new datalogLexer(cs);

        final CommonTokenStream cts = new CommonTokenStream(dl);
        final datalogParser dp = new datalogParser(cts);
        try {
            final program_return pr = dp.program();
            final List<CommonTree> rules = ((CommonTree) pr.getTree()).getChildren();

            if (rules == null) {
                log.debug("Rule set was parsed, however, is empty.");
                return;
            } else {
                for (final CommonTree rule : rules) {
                    this.processRuleFactOrQuery(rule);
                }
            }
        } catch (final RecognitionException e) {
            throw new IllegalArgumentException("Could not parse program.", e);
        }
    }

    private void processRuleFactOrQuery(final CommonTree rule) {
        final CommonTree head = (CommonTree) rule.getChild(0);
        final CommonTree body = (CommonTree) rule.getChild(1);

        if (head != null) {
            if (body != null) {
                throw new UnsupportedOperationException("Parsing of Rules not supported yet.");
                // this.processRule(head, body);
            } else {
                if (head.getType() == datalogLexer.BODY) {
                    throw new UnsupportedOperationException("Parsing of Queries not supported yet.");
                    // processQuery(head);
                } else {
                    this.processFact(head);
                }
            }
        } else {
            throw new IllegalStateException("Illegal rule " + rule.toString());
        }
    }

    private void processFact(final CommonTree head) {
        final Predicate pred = translatePredicate(head);
        final List<List<ParameterValue<?>>> values = new ArrayList<List<ParameterValue<?>>>();

        final List<ParameterValue<?>> value = new ArrayList<ParameterValue<?>>(pred.getArity());
        values.add(value);

        for (int i = 0; i < head.getChildCount(); ++i) {
            final CommonTree param = (CommonTree) head.getChild(i);

            final int type = param.getType();
            final String text = param.getText();
            switch (type) {
                case datalogLexer.INTEGER:
                    final Integer integerValue = Integer.valueOf(text);
                    // values[i] = ParameterValue.create(integerValue);
                    // value.add(integerValue);
                    value.add(ParameterValue.<Integer>create(integerValue));
                    break;
                case datalogLexer.DOUBLE:
                    final Double doubleValue = Double.valueOf(text);
                    // values[i] = ParameterValue.create(doubleValue);
                    value.add(ParameterValue.<Double>create(doubleValue));
                    break;
                case datalogLexer.CHARACTER:
                    final Character characterValue = param.getText().charAt(1);
                    // values[i] = ParameterValue.create(characterValue);
                    value.add(ParameterValue.<Character>create(characterValue));
                    break;
                case datalogLexer.BOOLEAN:
                    final Boolean booleanValue = Boolean.valueOf(text);
                    // values[i] = ParameterValue.create(booleanValue);
                    value.add(ParameterValue.<Boolean>create(booleanValue));
                    break;
                case datalogLexer.STRING:
                    final int endIndex = text.length() - 1;
                    final String stringValue = text.substring(1, endIndex);
                    // values[i] = ParameterValue.create(stringValue);
                    value.add(ParameterValue.<String>create(stringValue));
                    break;
                case datalogLexer.DATE:
                    final String[] dateValue = text.split("\\.");
                    final int year = Integer.valueOf(dateValue[2]);
                    final int month = Integer.valueOf(dateValue[1]) - 1;
                    final int dayOfMonth = Integer.valueOf(dateValue[0]);
                    // values[i] = ParameterValue.create(new GregorianCalendar(year,
                    // month, dayOfMonth));
                    value.add(ParameterValue.<GregorianCalendar>create(new GregorianCalendar(year, month, dayOfMonth)));
                    break;
                default:
                    throw new IllegalArgumentException("Token " + type + " is not supported");
            }
        }
        IFacts iFacts = this.factsResult.get(pred);
        if (iFacts == null) {
            iFacts = SimpleFacts.create(pred.getName(), pred.getArity(), values);
            this.factsResult.put(pred, iFacts);
        } else {
            iFacts.getValues().addAll(values);
        }
    }

    private Predicate translatePredicate(final CommonTree literal) {
        return Predicate.create(literal.getText(), literal.getChildCount());
    }
}