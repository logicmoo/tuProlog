package alice.tuprolog;

import junit.framework.TestCase;

public class SolveInfoTestCase extends TestCase {

	public void testGetSubsequentQuery() {
		TuProlog engine = new TuProlog();
		Term query = new TuStruct("is", new TuVar("X"), new TuStruct("+", new TuInt(1), new TuInt(2)));
		SolveInfo result = engine.solve(query);
		assertTrue(result.isSuccess());
		assertEquals(query, result.getQuery());
		query = new TuStruct("functor", new TuStruct("p"), new TuVar("Name"), new TuVar("Arity"));
		result = engine.solve(query);
		assertTrue(result.isSuccess());
		assertEquals(query, result.getQuery());
	}

}
