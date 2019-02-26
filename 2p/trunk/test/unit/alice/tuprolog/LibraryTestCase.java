package alice.tuprolog;

import junit.framework.TestCase;

public class LibraryTestCase extends TestCase {
	
	public void testLibraryFunctor() throws PrologException {
		TuProlog engine = new TuProlog();
		engine.loadLibrary(new TestLibrary());
		SolveInfo goal = engine.solve("N is sum(1, 3).");
		assertTrue(goal.isSuccess());
		assertEquals(new TuInt(4), goal.getVarValue("N"));
	}
	
	public void testLibraryPredicate() throws PrologException {
		TuProlog engine = new TuProlog();
		engine.loadLibrary(new TestLibrary());
		TestOutputListener l = new TestOutputListener();
		engine.addOutputListener(l);
		engine.solve("println(sum(5)).");
		assertEquals("sum(5)", l.output);
	}

}
