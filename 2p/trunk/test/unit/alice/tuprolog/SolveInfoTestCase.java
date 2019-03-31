package alice.tuprolog;

import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

import junit.framework.TestCase;

public class SolveInfoTestCase extends TestCase {

	public void testGetSubsequentQuery() {
		TuProlog engine = new TuProlog();
		Term query = createTuStruct2("is", new TuVar("X"), createTuStruct2("+", createTuInt(1), createTuInt(2)));
		SolveInfo result = engine.solve(query);
		assertTrue(result.isSuccess());
		assertEquals(query, result.getQuery());
		query = S("functor", createTuAtom("p"), new TuVar("Name"), new TuVar("Arity"));
		result = engine.solve(query);
		assertTrue(result.isSuccess());
		assertEquals(query, result.getQuery());
	}

}
