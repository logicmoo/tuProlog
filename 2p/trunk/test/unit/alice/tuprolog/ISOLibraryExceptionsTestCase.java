package alice.tuprolog;

import junit.framework.TestCase;

/**
 * @author Matteo Iuliani
 * 
 *         Test del funzionamento delle eccezioni lanciate dai predicati della ISOLibrary
 */
public class ISOLibraryExceptionsTestCase extends TestCase {

	// verifico che atom_length(X, Y) lancia un errore di instanziazione
	public void test_atom_length_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(atom_length(X, Y), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuStruct.createTuStruct2("atom_length", TuTerm.createTuVar("X"), TuTerm.createTuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che atom_length(1, Y) lancia un errore di tipo
	public void test_atom_length_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(atom_length(1, Y), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuStruct.createTuStruct2("atom_length", TuTerm.i32(1), TuTerm.createTuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuTerm.createAtomTerm("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}
	
	// verifico che atom_chars(1, X) lancia un errore di tipo
	public void test_atom_chars_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(atom_chars(1, X), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuStruct.createTuStruct2("atom_chars", TuTerm.i32(1), TuTerm.createTuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuTerm.createAtomTerm("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}
	
	// verifico che atom_chars(X, a) lancia un errore di tipo
	public void test_atom_chars_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(atom_chars(X, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuStruct.createTuStruct2("atom_chars", TuTerm.createTuVar("X"), TuTerm.createAtomTerm("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuTerm.createAtomTerm("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuTerm.createAtomTerm("a")));
	}
	
	// verifico che char_code(ab, X) lancia un errore di tipo
	public void test_char_code_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(char_code(ab, X), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuStruct.createTuStruct2("char_code", TuTerm.createAtomTerm("ab"), TuTerm.createTuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuTerm.createAtomTerm("character")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuTerm.createAtomTerm("ab")));
	}
	
	// verifico che char_code(X, a) lancia un errore di tipo
	public void test_char_code_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(char_code(X, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuStruct.createTuStruct2("char_code", TuTerm.createTuVar("X"), TuTerm.createAtomTerm("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuTerm.createAtomTerm("integer")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuTerm.createAtomTerm("a")));
	}
	
	// verifico che sub_atom(1, B, C, D, E) lancia un errore di tipo
	public void test_sub_atom_5_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(sub_atom(1, B, C, D, E), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuStruct.createSTRUCT("sub_atom_guard", TuTerm.i32(1), TuTerm.createTuVar("B"), TuTerm.createTuVar("C"), TuTerm.createTuVar("D"), TuTerm.createTuVar("E"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuTerm.createAtomTerm("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

}