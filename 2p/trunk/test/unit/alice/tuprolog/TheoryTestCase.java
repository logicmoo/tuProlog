package alice.tuprolog;

import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
import junit.framework.TestCase;

public class TheoryTestCase extends TestCase {

	public void testToStringWithParenthesis() throws InvalidTheoryException {
		String before = "a :- b, (d ; e).";
		TuTheory theory = new TuTheory(before);
		String after = theory.toString();
		assertEquals(theory.toString(), new TuTheory(after).toString());
	}
	
	public void testAppendClauseLists() throws InvalidTheoryException, MalformedGoalException {
		Term[] clauseList = new Term[] {TuFactory.createTuAtom("p"), createTuAtom("q"), createTuAtom("r")};
		Term[] otherClauseList = new Term[] {TuFactory.createTuAtom("a"), createTuAtom("b"), createTuAtom("c")};
		TuTheory theory = new TuTheory(TuFactory.createTuListStruct(clauseList));
		theory.append(new TuTheory(TuFactory.createTuListStruct(otherClauseList)));
		TuProlog engine = new TuProlog();
		engine.setTheory(theory);
		assertTrue((engine.solve("p.")).isSuccess());
		assertTrue((engine.solve("b.")).isSuccess());
	}

}
