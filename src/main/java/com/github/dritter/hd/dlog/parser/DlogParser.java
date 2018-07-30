package com.github.dritter.hd.dlog.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import com.github.dritter.hd.dlog.Facts;
import com.github.dritter.hd.dlog.IFacts;
import com.github.dritter.hd.dlog.IQuery;
import com.github.dritter.hd.dlog.IRule;
import com.github.dritter.hd.dlog.Literal;
import com.github.dritter.hd.dlog.Parameter;
import com.github.dritter.hd.dlog.Predicate;
import com.github.dritter.hd.dlog.Query;
import com.github.dritter.hd.dlog.Rule;
import com.github.dritter.hd.dlog.algebra.FillableTableIterator;
import com.github.dritter.hd.dlog.algebra.ParameterValue;
import com.github.dritter.hd.dlog.parser.datalogParser.program_return;

@SuppressWarnings("unchecked")
public class DlogParser {

    private List<IRule> rulesResult = new ArrayList<IRule>();
    private Map<Predicate, FillableTableIterator> factsResult = new HashMap<Predicate, FillableTableIterator>();
    private List<IQuery> queriesResult = new ArrayList<IQuery>();

    public DlogParser() {
    }

    public final List<IRule> getRules() {
        return this.rulesResult;
    }

    public final List<IQuery> getQueries() {
        return this.queriesResult;
    }

    public final Collection<IFacts> getFacts() {
        final List<IFacts> lf = new ArrayList<IFacts>();
        for (final Predicate pred : this.factsResult.keySet()) {
            lf.add(Facts.create(pred, this.factsResult.get(pred)));
        }
        return lf;
    }

    public final void parse(final String program) {
        final CharStream cs = new ANTLRStringStream(program);
        final datalogLexer dl = new datalogLexer(cs);

        final CommonTokenStream cts = new CommonTokenStream(dl);
        final datalogParser dp = new datalogParser(cts);
        try {
            final datalogParser.program_return pr = dp.program();

            List<CommonTree> rules = ((CommonTree) pr.getTree()).getChildren();

            if (rules == null) {
                rules = new ArrayList<CommonTree>();
            }

            for (CommonTree rule : rules) {
                processRuleFactOrQuery(rule);
            }

        } catch (final RecognitionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void processRuleFactOrQuery(final CommonTree rule) {
        final CommonTree head = (CommonTree) rule.getChild(0);
        final CommonTree body = (CommonTree) rule.getChild(1);

        if (head != null) {
            if (body != null) {
                processRule(head, body);
            } else {
                if (head.getType() == datalogLexer.BODY) {
                    processQuery(head);
                } else {
                    processFact(head);
                }
            }
        } else {
            throw new IllegalStateException("Illegal rule " + rule.toString());
        }
    }

    private void processQuery(final CommonTree body) {
        final List<Literal> bodyLiterals = new ArrayList<Literal>(body.getChildCount());
        for (CommonTree literal : (List<CommonTree>) body.getChildren()) {
            bodyLiterals.add(translateLiteral(literal));
        }

        this.queriesResult.add(Query.create(bodyLiterals));
    }

    private void processFact(final CommonTree head) {
        // facts
        final Predicate pred = translatePredicate(head);
        final ParameterValue<?>[] values = new ParameterValue<?>[head.getChildCount()];

        for (int i = 0; i < head.getChildCount(); ++i) {
            final CommonTree param = (CommonTree) head.getChild(i);

            int type = param.getType();
            String text = param.getText();
            switch (type) {
                case datalogLexer.INTEGER:
                    Integer integerValue = Integer.valueOf(text);
                    values[i] = ParameterValue.create(integerValue);
                    break;
                case datalogLexer.DOUBLE:
                    Double doubleValue = Double.valueOf(text);
                    values[i] = ParameterValue.create(doubleValue);
                    break;
                case datalogLexer.CHARACTER:
                    Character characterValue = param.getText().charAt(1);
                    values[i] = ParameterValue.create(characterValue);
                    break;
                case datalogLexer.BOOLEAN:
                    Boolean booleanValue = Boolean.valueOf(text);
                    values[i] = ParameterValue.create(booleanValue);
                    break;
                case datalogLexer.STRING:
                    int endIndex = text.length() - 1;
                    String stringValue = text.substring(1, endIndex);
                    values[i] = ParameterValue.create(stringValue);
                    break;
                case datalogLexer.DATE:
                    String[] dateValue = text.split("\\.");
                    int year = Integer.valueOf(dateValue[2]);
                    int month = Integer.valueOf(dateValue[1]) - 1;
                    int dayOfMonth = Integer.valueOf(dateValue[0]);
                    values[i] = ParameterValue.create(new GregorianCalendar(year, month, dayOfMonth));
                    break;
                default:
                    throw new IllegalArgumentException("Token " + type + " is not supported");
            }
        }
        FillableTableIterator pf = this.factsResult.get(pred);

        if (pf == null) {
            pf = new FillableTableIterator();
            this.factsResult.put(pred, pf);
        }

        pf.add(values);
    }

    private void processRule(final CommonTree head, final CommonTree body) {
        // rule
        final Literal headLiteral = translateLiteral(head);

        final List<Literal> bodyLiterals = new ArrayList<Literal>(body.getChildCount());
        for (CommonTree literal : (List<CommonTree>) body.getChildren()) {
            bodyLiterals.add(translateLiteral(literal));
        }

        this.rulesResult.add(Rule.create(headLiteral, bodyLiterals));
    }

    private Literal translateLiteral(final CommonTree literal) {
        final Predicate pred = translatePredicate(literal);
        final List<Parameter<?>> params = new ArrayList<Parameter<?>>(literal.getChildCount());

        for (CommonTree parameter : (List<CommonTree>) literal.getChildren()) {
            params.add(translateParameter(parameter));
        }
        return Literal.create(pred, params);
    }

    private Predicate translatePredicate(final CommonTree literal) {
        return Predicate.create(literal.getText(), literal.getChildCount());
    }

    private Parameter<?> translateParameter(final CommonTree parameter) {
        int type = parameter.getType();
        String text = parameter.getText();
        switch (type) {
            case datalogLexer.IDENTIFIER:
                return Parameter.createVariable(text);
            case datalogLexer.INTEGER:
                Integer integerValue = Integer.valueOf(text);
                return Parameter.createConstant(integerValue);
            case datalogLexer.DOUBLE:
                Double doubleValue = Double.valueOf(text);
                return Parameter.createConstant(doubleValue);
            case datalogLexer.CHARACTER:
                Character characterValue = text.charAt(1);
                return Parameter.createConstant(characterValue);
            case datalogLexer.BOOLEAN:
                Boolean booleanValue = Boolean.valueOf(text);
                return Parameter.createConstant(booleanValue);
            case datalogLexer.STRING:
                int endIndex = text.length() - 1;
                String stringValue = text.substring(1, endIndex);
                return Parameter.createConstant(stringValue);
            case datalogLexer.DATE:
                String[] dateValue = text.split("\\.");
                int year = Integer.valueOf(dateValue[2]);
                int month = Integer.valueOf(dateValue[1]) - 1;
                int dayOfMonth = Integer.valueOf(dateValue[0]);
                return Parameter.createConstant(new GregorianCalendar(year, month, dayOfMonth));
            default:
                throw new IllegalArgumentException("Token " + type + " is not supported");
        }
    }
}