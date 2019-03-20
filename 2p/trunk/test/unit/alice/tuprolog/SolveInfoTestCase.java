package alice.tuprolog;

import junit.framework.TestCase;

public class SolveInfoTestCase extends TestCase {

	public void testGetSubsequentQuery() {
		TuProlog engine = new TuProlog();
		Term query = TuStruct.createTuStruct2("is", TuTerm.createTuVar("X"), TuStruct.createTuStruct2("+", TuTerm.i32(1), TuTerm.i32(2)));
		SolveInfo result = engine.solve(query);
		assertTrue(result.isSuccess());
		assertEquals(query, result.getQuery());
		query = TuStruct.createSTRUCT("functor", TuTerm.createAtomTerm("p"), TuTerm.createTuVar("Name"), TuTerm.createTuVar("Arity"));
		result = engine.solve(query);
		assertTrue(result.isSuccess());
		assertEquals(query, result.getQuery());
	}

}
