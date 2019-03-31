package alice.tuprolog;

import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
import java.util.List;


import junit.framework.TestCase;

public class TheoryManagerTestCase extends TestCase {

	public void testUnknownDirective() throws InvalidTheoryException {
		String theory = ":- unidentified_directive(unknown_argument).";
		TuProlog engine = new TuProlog();
		TestWarningListener warningListener = new TestWarningListener();
		engine.addWarningListener(warningListener);
		engine.setTheory(new TuTheory(theory));
		assertTrue(warningListener.warning.indexOf("unidentified_directive/1") > 0);
		assertTrue(warningListener.warning.indexOf("is unknown") > 0);
	}

	public void testFailedDirective() throws InvalidTheoryException {
		String theory = ":- load_library('UnknownLibrary').";
		TuProlog engine = new TuProlog();
		TestWarningListener warningListener = new TestWarningListener();
		engine.addWarningListener(warningListener);
		engine.setTheory(new TuTheory(theory));
		assertTrue(warningListener.warning.indexOf("load_library/1") > 0);
		assertTrue(warningListener.warning.indexOf("InvalidLibraryException") > 0);
	}

	public void testAssertNotBacktrackable() throws TuPrologException {
		TuProlog engine = new TuProlog();
		SolveInfo firstSolution = engine.solve("assertz(a(z)).");
		assertTrue(firstSolution.isSuccess());
		assertFalse(firstSolution.hasOpenAlternatives());
	}

	public void testAbolish() throws TuPrologException {
		TuProlog engine = new TuProlog();
		String theory = "test(A, B) :- A is 1+2, B is 2+3.";
		engine.setTheory(new TuTheory(theory));
		TuTheoryManager manager = engine.getTheoryManager();
		TuStruct testTerm = createTuStruct2("test", createTuAtom("a"), createTuAtom("b"));
		List<ClauseInfo> testClauses = manager.find(testTerm);
		assertEquals(1, testClauses.size());
		manager.abolish(TuFactory.createTuStruct2("/", createTuAtom("test"), createTuInt(2)));
		testClauses = manager.find(testTerm);
		// The predicate should also disappear completely from the clause
		// database, i.e. ClauseDatabase#get(f/a) should return null
		assertEquals(0, testClauses.size());
	}

	public void testAbolish2() throws InvalidTheoryException, MalformedGoalException{
		TuProlog engine = new TuProlog();
		engine.setTheory(new TuTheory("fact(new).\n" +
									"fact(other).\n"));

		SolveInfo info = engine.solve("abolish(fact/1).");
		assertTrue(info.isSuccess());
		info = engine.solve("fact(V).");
		assertFalse(info.isSuccess());
	}
	
	// Based on the bugs 65 and 66 on sourceforge
	public void testRetractall() throws MalformedGoalException, NoSolutionException, NoMoreSolutionException {
		TuProlog engine = new TuProlog();
		SolveInfo info = engine.solve("assert(takes(s1,c2)), assert(takes(s1,c3)).");
		assertTrue(info.isSuccess());
		info = engine.solve("takes(s1, N).");
		assertTrue(info.isSuccess());
		assertTrue(info.hasOpenAlternatives());
		assertEquals("c2", info.getVarValue("N").toString());
		info = engine.solveNext();
		assertTrue(info.isSuccess());
		assertEquals("c3", info.getVarValue("N").toString());
		
		info = engine.solve("retractall(takes(s1,c2)).");
		assertTrue(info.isSuccess());
		info = engine.solve("takes(s1, N).");
		assertTrue(info.isSuccess());
		assertFalse(info.hasOpenAlternatives());
		assertEquals("c3", info.getVarValue("N").toString());
	}

	// TODO test retractall: ClauseDatabase#get(f/a) should return an
	// empty list
	
	public void testRetract() throws InvalidTheoryException, MalformedGoalException {
		TuProlog engine = new TuProlog();
		TestOutputListener listener = new TestOutputListener();
		engine.addOutputListener(listener);
		engine.setTheory(new TuTheory("insect(ant). insect(bee)."));
		SolveInfo info = engine.solve("retract(insect(I)), write(I), retract(insect(bee)), fail.");
		assertFalse(info.isSuccess());
		assertEquals("antbee", listener.output);
		
	}

}
