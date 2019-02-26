package alice.tuprolog;

import junit.framework.TestCase;

public class StateRuleSelectionTestCase extends TestCase {
	
	public void testUnknownPredicateInQuery() throws MalformedGoalException {
		TuProlog engine = new TuProlog();
		TestWarningListener warningListener = new TestWarningListener();
		engine.addWarningListener(warningListener);
		String query = "p(X).";
		engine.solve(query);
		assertTrue(warningListener.warning.indexOf("p/1") > 0);
		assertTrue(warningListener.warning.indexOf("is unknown") > 0);
	}
	
	public void testUnknownPredicateInTheory() throws InvalidTheoryException, MalformedGoalException {
		TuProlog engine = new TuProlog();
		TestWarningListener warningListener = new TestWarningListener();
		engine.addWarningListener(warningListener);
		String theory = "p(X) :- a, b. \nb.";
		engine.setTheory(new TuTheory(theory));
		String query = "p(X).";
		engine.solve(query);
		assertTrue(warningListener.warning.indexOf("a/0") > 0);
		assertTrue(warningListener.warning.indexOf("is unknown") > 0);
	}

}
