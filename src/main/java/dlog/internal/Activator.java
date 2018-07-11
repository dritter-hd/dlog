package dlog.internal;

import java.util.Collection;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import dlog.IEvaluator;
import dlog.IFacts;
import dlog.IRule;
import dlog.NaiveRecursiveEvaluator;
import dlog.parser.DlogParser;

@Component
@Service
public class Activator implements CommandProvider {

	private BundleContext _context;
	private static Activator instance;

	public Activator() {
	}

	public void start(BundleContext context) throws Exception {
		instance = this;
		_context = context;

		Bundle bundle = _context.getBundle();
	}

	public void stop(BundleContext context) throws Exception {
		instance = null;
		_context = null;
	}

	@Override
	public String getHelp() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("--- Discovery Controller ---\n");
		buffer
				.append("\tdiscover - request facts from registered source model providers.\n");
		return buffer.toString();
	}

	public void _exec(final CommandInterpreter ci) {
		final String FACTS = "";
		final String RULES = "";
		final DlogParser hp = new DlogParser();

		hp.parse(RULES);
		final List<IRule> rules = hp.getRules();

		hp.parse(FACTS);
		final Collection<IFacts> facts = hp.getFacts();

		final IEvaluator eval = new NaiveRecursiveEvaluator(rules);
		final Collection<IFacts> result = eval.eval(facts);
		ci.println(result);
	}
}
