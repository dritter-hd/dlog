package dlog.evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dlog.Facts;
import dlog.IRule;
import dlog.NaiveRecursiveEvaluator;
import dlog.NonRecursiveEvaluator;
import dlog.Predicate;
import dlog.algebra.DataIterator;
import dlog.algebra.FillableTableIterator;
import dlog.algebra.ParameterValue;
import dlog.parser.DlogParser;

public class DlogEvaluator implements IEvaluator {
    // private static final Logger log =
    // LoggerFactory.getLogger(DlogEvaluator.class);

    private final DlogParser hlogParser = new DlogParser();
    private String rules;

    // private Configuration config;
    private Collection<IFacts> facts;
    private Collection<dlog.IFacts> factsNew;
    private NonRecursiveEvaluator nonRecursiveEvaluator;

    private DlogEvaluator() {
    }

    public DlogEvaluator(final String rules) {
        this.rules = rules;
        nonRecursiveEvaluator = new NonRecursiveEvaluator(this.getRules());
    }

    public static DlogEvaluator create() {
        return new DlogEvaluator();
    }
    
    public static DlogEvaluator create(final String rules) {
        return new DlogEvaluator(rules);
    }

    /*
     * (non-Javadoc)
     * 
     * @see dlog.evaluator.IEvaluator2#query(java.lang.String,
     * int)
     */
    @Override
    public IFacts query(String predicateName, int arity) {
        return query(predicateName, arity, new String[arity]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see dlog.evaluator.IEvaluator2#_query(java.lang.String,
     * int)
     */
    @Deprecated
    public Collection<dlog.IFacts> _query(String predicateName, int arity) {
        return _query(predicateName, arity, new String[arity]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see dlog.evaluator.IEvaluator2#_query(java.lang.String,
     * int, java.lang.String[])
     */
    @Deprecated
    public Collection<dlog.IFacts> _query(String predicateName, int arity, String[] params) {

        Collection<dlog.IFacts> factsForEvaluation = evaluateRules();
        if (null != this.factsNew && this.factsNew.size() > 0) {
            factsForEvaluation.addAll(this.factsNew);
        } else {
            factsForEvaluation.addAll(transformFacts(this.facts));
        }

        Collection<dlog.IFacts> result = new ArrayList<dlog.IFacts>();

        final Iterator<dlog.IFacts> fIt = factsForEvaluation.iterator();

        // Filter result of rule evaluation according to query
        while (fIt.hasNext()) {
            final dlog.IFacts f = fIt.next();
            if (f.getPredicate().getName().equals(this.convertPredicateName(predicateName)) && f.getPredicate().getArity() == arity) {
                result.add(f);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see dlog.evaluator.IEvaluator2#evaluateRules()
     */
    @Deprecated
    public Collection<dlog.IFacts> evaluateNonRecursiveRules() {
        if (null != this.factsNew && this.factsNew.size() > 0) {
            return nonRecursiveEvaluator.eval(this.factsNew);
        }
        return nonRecursiveEvaluator.eval(transformFacts(this.facts));
    }

    /*
     * (non-Javadoc)
     * 
     * @see dlog.evaluator.IEvaluator2#evaluateRules()
     */
    @Deprecated
    public Collection<dlog.IFacts> evaluateRules() {
        final NaiveRecursiveEvaluator evaluator = new NaiveRecursiveEvaluator(this.getRules());

        if (null != this.factsNew && this.factsNew.size() > 0) {
            return evaluator.eval(this.factsNew);
        }
        return evaluator.eval(transformFacts(this.facts));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see dlog.evaluator.IEvaluator2#query(java.lang.String,
     * int, java.lang.String[])
     */
    @Override
    public IFacts query(String predicateName, int arity, String[] params) {

        Collection<dlog.IFacts> result = _query(predicateName, arity, params);

        final Iterator<dlog.IFacts> fIt = result.iterator();

        final List<List<ParameterValue<?>>> values = new ArrayList<List<ParameterValue<?>>>();
        while (fIt.hasNext()) {
            final dlog.IFacts f = fIt.next();
            // if (!f.getPredicate().getName().equals(predicateName)
            // || f.getPredicate().getArity() == arity) {
            // continue;
            // }

            if (f.getPredicate().getName().equals(this.convertPredicateName(predicateName)) && f.getPredicate().getArity() == arity) {
                processValues(values, f, params);
            }
        }

        final List<List<ParameterValue<?>>> noDublicateValues = checkValues(values);
        return SimpleFacts.create(predicateName, arity, noDublicateValues);
    }

    private List<List<ParameterValue<?>>> checkValues(List<List<ParameterValue<?>>> values) {
        final List<List<ParameterValue<?>>> result = new ArrayList<List<ParameterValue<?>>>();
        final HashMap<String, List<ParameterValue<?>>> map = new HashMap<String, List<ParameterValue<?>>>();
        for (final List<ParameterValue<?>> list : values) {
            final String key = determineKey(list);
            if (!map.containsKey(key)) {
                map.put(key, list);
                result.add(list);
            }
        }
        return result;
    }

    private String determineKey(List<ParameterValue<?>> list) {
        final StringBuilder sb = new StringBuilder();
        for (final ParameterValue<?> s : list) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void processValues(final List<List<ParameterValue<?>>> values, final dlog.IFacts f, String[] params) {
        final DataIterator dit = f.getValues();
        dit.open();
        // String[] value = null;
        ParameterValue<?>[] value = null;
        while ((value = dit.next()) != null) {
            final List<ParameterValue<?>> list = new ArrayList<ParameterValue<?>>();
            boolean toBeConsidered = true;
            for (int i = 0; i < value.length; ++i) {
                list.add(value[i]/* .get().toString() */);

                if (params[i] != null && !value[i].get().toString().equals(params[i])) {
                    toBeConsidered = false;
                }
            }
            // values.add(Arrays.asList(value));
            if (toBeConsidered) {
                values.add(list);
            }
        }
        dit.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * dlog.evaluator.IEvaluator2#transformFacts(java.util.Collection
     * )
     */
    @Deprecated
    public List<dlog.IFacts> transformFacts(Collection<IFacts> facts) {
        final List<dlog.IFacts> fs = new ArrayList<dlog.IFacts>(facts.size());
        for (IFacts fact : facts) {
            final DataIterator ftIt = new FillableTableIterator();
            for (List<ParameterValue<?>> ls : fact.getValues()) {
                // ((FillableTableIterator) ftIt).add((String[]) ls.toArray());

                final ParameterValue<?>[] spv = new ParameterValue[ls.size()];
                for (int i = 0; i < ls.size(); i++) {
                    ParameterValue<?> value = ls.get(i);
                    spv[i] = /* ParameterValue.create( */value/* ) */;
                }
                ((FillableTableIterator) ftIt).add(spv);
            } // moved up to generate only one fact with multiple fact values.
              // if required move back down after fs.add();

            final String predicate = convertPredicateName(fact.getPredicate());
            final Facts newFact = Facts.create(Predicate.create(predicate, fact.getArity()), (ftIt));

            // if (!this.checkExists(fs, newFact)) {
            fs.add(newFact);
            // }
        }
        return fs;
    }

    private String convertPredicateName(final String predicate) {
        return predicate.replaceAll("_", "-");
    }

    List<IRule> getRules() {
        hlogParser.parse(rules);
        return hlogParser.getRules();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * dlog.evaluator.IEvaluator2#initalize(java.util.Collection)
     */
    @Override
    public void initalize(Collection<IFacts> facts) {
        this.facts = facts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * dlog.evaluator.IEvaluator2#initalize(java.util.Collection,
     * java.lang.String)
     */
    @Override
    public void initalize(final Collection<IFacts> facts, final String rules) {
        this.initalize(facts);
        this.rules = rules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * dlog.evaluator.IEvaluator2#_initalize(java.util.Collection,
     * java.lang.String)
     */
    @Deprecated
    public void _initalize(final Collection<dlog.IFacts> facts, final String rules) {
        this.factsNew = facts;
        this.rules = rules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * dlog.evaluator.IEvaluator2#_initalize(java.util.Collection)
     */
    @Deprecated
    public void _initalize(final Collection<dlog.IFacts> facts) {
        this.factsNew = facts;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see dlog.evaluator.IEvaluator2#initalize(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void initalize(final String facts, final String rules) {
        hlogParser.parse(facts);
        this.factsNew = hlogParser.getFacts();
        this.rules = rules;
    }
}