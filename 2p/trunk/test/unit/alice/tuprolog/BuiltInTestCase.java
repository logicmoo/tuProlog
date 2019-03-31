package alice.tuprolog;

import junit.framework.TestCase;

public class BuiltInTestCase extends TestCase {
	
	public void testConvertTermToGoal() throws InvalidTermException {
		Term t = new TuVar("T");
		TuStruct result = new TuStruct("call", t);
		assertEquals(result, BuiltIn.convertTermToGoal(t));
		assertEquals(result, BuiltIn.convertTermToGoal(new TuStruct("call", t)));
		
		t = new TuInt(2);
		assertNull(BuiltIn.convertTermToGoal(t));
		
		t = new TuStruct("p", new TuStruct("a"), new TuVar("B"), new TuStruct("c"));
		result = (TuStruct) t;
		assertEquals(result, BuiltIn.convertTermToGoal(t));
		
		TuVar linked = new TuVar("X");
		linked.setLink(new TuStruct("!"));
		Term[] arguments = new Term[] { linked, new TuVar("Y") };
		Term[] results = new Term[] { new TuStruct("!"), new TuStruct("call", new TuVar("Y")) };
		assertEquals(new TuStruct(";", results), BuiltIn.convertTermToGoal(new TuStruct(";", arguments)));
		assertEquals(new TuStruct(",", results), BuiltIn.convertTermToGoal(new TuStruct(",", arguments)));
		assertEquals(new TuStruct("->", results), BuiltIn.convertTermToGoal(new TuStruct("->", arguments)));
	}
	
	//Based on the bug #59 Grouping conjunctions in () changes result on sourceforge
	public void testGroupingConjunctions() throws InvalidTheoryException, MalformedGoalException {
		TuProlog engine = new TuProlog();
		engine.setTheory(new TuTheory("g1. g2."));
		SolveInfo info = engine.solve("(g1, g2), (g3, g4).");
		assertFalse(info.isSuccess());
		engine.setTheory(new TuTheory("g1. g2. g3. g4."));
		info = engine.solve("(g1, g2), (g3, g4).");
		assertTrue(info.isSuccess());
	}

}
