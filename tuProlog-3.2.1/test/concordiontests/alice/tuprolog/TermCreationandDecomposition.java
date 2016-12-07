package alice.tuprolog;

import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.extension.Extension;
import org.concordion.ext.EmbedExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

/* Run this class as a JUnit test. */

@RunWith(ConcordionRunner.class)
public class TermCreationandDecomposition {

	public boolean success(String goal, String theory) throws Exception {

		return ConcordionSingleton.getInstance().success(goal, theory);

	}

	public Term value(String evaluable) throws Exception {

		return ConcordionSingleton.getInstance().value(evaluable);

	}

	public boolean successWithException(String goal, String theory)
			throws PrologException {

		return ConcordionSingleton.getInstance().successWithException(goal,
				theory);

	}

	public String successWithExceptionAndText(String goal, String theory)
			throws PrologException {

		return ConcordionSingleton.getInstance().successWithExceptionAndText(
				goal, theory);

	}

	public String successAndResult(String goal, String theory, String variable)
			throws Exception {

		return ConcordionSingleton.getInstance().successAndResult(goal, theory,
				variable);
	}

	public String successAndResultWithoutReplace(String goal, String theory,
			String variable) throws Exception {

		return ConcordionSingleton.getInstance()
				.successAndResultWithoutReplace(goal, theory, variable);
	}

	public boolean successAndResultsWithLimit(String goal, String theory,
			String variable, String solution, int maxSolutions)
			throws Exception {

		return ConcordionSingleton.getInstance().successAndResultsWithLimit(
				goal, theory, variable, solution, maxSolutions);

	}

	public boolean successAndResultsWithLimitWithoutReplace(String goal,
			String theory, String variable, String solution, int maxSolutions)
			throws Exception {

		return ConcordionSingleton.getInstance()
				.successAndResultsWithLimitWithoutReplace(goal, theory,
						variable, solution, maxSolutions);

	}

	public boolean successAndResults(String goal, String theory,
			String variable, String solution) throws Exception {

		return ConcordionSingleton.getInstance().successAndResults(goal,
				theory, variable, solution);

	}

	public boolean successAndResultsWithoutReplace(String goal, String theory,
			String variable, String solution) throws Exception {

		return ConcordionSingleton.getInstance()
				.successAndResultsWithoutReplace(goal, theory, variable,
						solution);

	}

	@Extension
	public ConcordionExtension extension = new EmbedExtension().withNamespace(
			"myns", "http://com.myco/myns");
}
