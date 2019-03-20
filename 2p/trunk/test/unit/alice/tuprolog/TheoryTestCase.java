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
		Term[] clauseList = new Term[] {TuTerm.createAtomTerm("p"), TuTerm.createAtomTerm("q"), TuTerm.createAtomTerm("r")};
		Term[] otherClauseList = new Term[] {TuTerm.createAtomTerm("a"), TuTerm.createAtomTerm("b"), TuTerm.createAtomTerm("c")};
		TuTheory theory = new TuTheory(TuStruct.createTuList(clauseList));
		theory.append(new TuTheory(TuStruct.createTuList(otherClauseList)));
		TuProlog engine = new TuProlog();
		engine.setTheory(theory);
		assertTrue((engine.solve("p.")).isSuccess());
		assertTrue((engine.solve("b.")).isSuccess());
	}

}
