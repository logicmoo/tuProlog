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
		assertTrue(g.isEqual(new TuStruct("atom_length", new TuVar("X"), new TuVar("Y"))));
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
		assertTrue(g.isEqual(new TuStruct("atom_length", new TuInt(1), new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
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
		assertTrue(g.isEqual(new TuStruct("atom_chars", new TuInt(1), new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
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
		assertTrue(g.isEqual(new TuStruct("atom_chars", new TuVar("X"), new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}
	
	// verifico che char_code(ab, X) lancia un errore di tipo
	public void test_char_code_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(char_code(ab, X), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("char_code", new TuStruct("ab"), new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("character")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("ab")));
	}
	
	// verifico che char_code(X, a) lancia un errore di tipo
	public void test_char_code_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(char_code(X, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("char_code", new TuVar("X"), new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("integer")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}
	
	// verifico che sub_atom(1, B, C, D, E) lancia un errore di tipo
	public void test_sub_atom_5_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(sub_atom(1, B, C, D, E), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("sub_atom_guard", new TuInt(1), new TuVar("B"),  new TuVar("C"),  new TuVar("D"),  new TuVar("E"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

}