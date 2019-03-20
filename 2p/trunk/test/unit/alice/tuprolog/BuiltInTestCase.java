package alice.tuprolog;

import junit.framework.TestCase;

public class BuiltInTestCase extends TestCase {
	
	public void testConvertTermToGoal() throws InvalidTermException {
		Term t = TuTerm.createTuVar("T");
		final TuStruct createTuStruct1 = TuStruct.createTuStruct1("call", t);
        TuStruct result = createTuStruct1;
		assertEquals(result, BuiltIn.convertTermToGoal(t));
		assertEquals(result, BuiltIn.convertTermToGoal(createTuStruct1));
		
		t = TuTerm.i32(2);
		assertNull(BuiltIn.convertTermToGoal(t));
		
		t = TuStruct.createSTRUCT("p", TuTerm.createAtomTerm("a"), TuTerm.createTuVar("B"), TuTerm.createAtomTerm("c"));
		result = (TuStruct) t;
		assertEquals(result, BuiltIn.convertTermToGoal(t));
		
		TuVar linked = TuTerm.createTuVar("X");
		linked.setLink(TuTerm.createAtomTerm("!"));
		Term[] arguments = new Term[] { linked, TuTerm.createTuVar("Y") };
		Term[] results = new Term[] { TuTerm.createAtomTerm("!"), TuStruct.createTuStruct1("call", TuTerm.createTuVar("Y")) };
		assertEquals(TuStruct.createTuStructA(";", results), BuiltIn.convertTermToGoal(TuStruct.createTuStructA(";", arguments)));
		assertEquals(TuStruct.createTuStructA(",", results), BuiltIn.convertTermToGoal(TuStruct.createTuStructA(",", arguments)));
		assertEquals(TuStruct.createTuStructA("->", results), BuiltIn.convertTermToGoal(TuStruct.createTuStructA("->", arguments)));
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
