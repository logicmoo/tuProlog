package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

public class BuiltInTestCase extends TestCase {
	
	public void testConvertTermToGoal() throws InvalidTermException {
		Term t = new TuVar("T");
		TuStruct result = S("call", t);
		assertEquals(result, BuiltIn.convertTermToGoal(t));
		assertEquals(result, BuiltIn.convertTermToGoal(TuFactory.S("call", t)));
		
		t = createTuInt(2);
		assertNull(BuiltIn.convertTermToGoal(t));
		
		t = S("p", createTuAtom("a"), new TuVar("B"), createTuAtom("c"));
		result = (TuStruct) t;
		assertEquals(result, BuiltIn.convertTermToGoal(t));
		
		TuVar linked = new TuVar("X");
		linked.setLink(TuFactory.createTuAtom("!"));
		Term[] arguments = new Term[] { linked, new TuVar("Y") };
		Term[] results = new Term[] { createTuAtom("!"), S("call", new TuVar("Y")) };
		assertEquals(TuFactory.createTuStructA(";", results), BuiltIn.convertTermToGoal(TuFactory.createTuStructA(";", arguments)));
		assertEquals(TuFactory.createTuStructA(",", results), BuiltIn.convertTermToGoal(TuFactory.createTuStructA(",", arguments)));
		assertEquals(TuFactory.createTuStructA("->", results), BuiltIn.convertTermToGoal(TuFactory.createTuStructA("->", arguments)));
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
