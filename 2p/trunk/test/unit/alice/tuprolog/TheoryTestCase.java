package alice.tuprolog;

import junit.framework.TestCase;

public class TheoryTestCase extends TestCase {

	public void testToStringWithParenthesis() throws InvalidTheoryException {
		String before = "a :- b, (d ; e).";
		TuTheory theory = new TuTheory(before);
		String after = theory.toString();
		assertEquals(theory.toString(), new TuTheory(after).toString());
	}
	
	public void testAppendClauseLists() throws InvalidTheoryException, MalformedGoalException {
		Term[] clauseList = new Term[] {new TuStruct("p"), new TuStruct("q"), new TuStruct("r")};
		Term[] otherClauseList = new Term[] {new TuStruct("a"), new TuStruct("b"), new TuStruct("c")};
		TuTheory theory = new TuTheory(new TuStruct(clauseList));
		theory.append(new TuTheory(new TuStruct(otherClauseList)));
		TuProlog engine = new TuProlog();
		engine.setTheory(theory);
		assertTrue((engine.solve("p.")).isSuccess());
		assertTrue((engine.solve("b.")).isSuccess());
	}

}
