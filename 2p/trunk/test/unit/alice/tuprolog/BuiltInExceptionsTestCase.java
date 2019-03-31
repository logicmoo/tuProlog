package alice.tuprolog;

import junit.framework.TestCase;

/**
 * @author Matteo Iuliani
 * 
 *         Test del funzionamento delle eccezioni lanciate dai predicati della
 *         classe BuiltIn
 */
public class BuiltInExceptionsTestCase extends TestCase {

	// verifico che asserta(X) lancia un errore di instanziazione
	public void test_asserta_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(asserta(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("asserta", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che asserta(1) lancia un errore di tipo
	public void test_asserta_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(asserta(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("asserta", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("clause")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che assertz(X) lancia un errore di instanziazione
	public void test_assertz_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(assertz(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("assertz", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che assertz(1) lancia un errore di tipo
	public void test_assertz_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(assertz(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("assertz", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("clause")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che '$retract'(X) lancia un errore di instanziazione
	public void test_$retract_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$retract'(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$retract", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '$retract'(1) lancia un errore di tipo
	public void test_$retract_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$retract'(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$retract", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("clause")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che abolish(X) lancia un errore di instanziazione
	public void test_abolish_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(abolish(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("abolish", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che abolish(1) lancia un errore di tipo
	public void test_abolish_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(abolish(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("abolish", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("predicate_indicator")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che abolish(p(X)) lancia un errore di tipo
	public void test_abolish_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(abolish(p(X)), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("abolish",
				new TuStruct("p", new TuVar("X")))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("predicate_indicator")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("p", new TuVar("X"))));
	}

	// verifico che halt(X) lancia un errore di instanziazione
	public void test_halt_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(halt(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("halt", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che halt(1.5) lancia un errore di tipo
	public void test_halt_1_2() throws Exception {
	    if(true) return;
		TuProlog engine = new TuProlog();
		String goal = "catch(halt(1.5), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("halt", new TuDouble(1.5))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("integer")));
		TuDouble culprit = (TuDouble) info.getTerm("Culprit");
		assertTrue(culprit.doubleValue() == 1.5);
	}

	// verifico che load_library(X) lancia un errore di instanziazione
	public void test_load_library_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(load_library(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("load_library", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che load_library(1) lancia un errore di tipo
	public void test_load_library_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(load_library(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("load_library", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che load_library_1 lancia un errore di esistenza se la libreria
	// LibraryName non esiste
	public void test_load_library_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(load_library('a'), error(existence_error(ObjectType, Culprit), existence_error(Goal, ArgNo, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("load_library", new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(new TuStruct("class")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("InvalidLibraryException: a at -1:-1")));
	}

	// verifico che unload_library(X) lancia un errore di instanziazione
	public void test_unload_library_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(unload_library(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("unload_library", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che unload_library(1) lancia un errore di tipo
	public void test_unload_library_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(unload_library(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("unload_library", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che unload_library(LibraryName) lancia un errore di esistenza se
	// la libreria LibraryName non esiste
	public void test_unload_library_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(unload_library('a'), error(existence_error(ObjectType, Culprit), existence_error(Goal, ArgNo, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("unload_library", new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(new TuStruct("class")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("InvalidLibraryException: null at 0:0")));
	}

	// verifico che '$call'(X) lancia un errore di instanziazione
	public void test_$call_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$call'(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$call", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '$call'(1) lancia un errore di tipo
	public void test_$call_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$call'(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$call", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("callable")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che is(X, Y) lancia un errore di instanziazione
	public void test_is_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, Y), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("is", new TuVar("X"), new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che is(X, a) lancia un errore di tipo
	public void test_is_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("is", new TuVar("X"), new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}
	
	// verifico che is(X, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_is_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("is", new TuVar("X"), new TuStruct("/", new TuInt(1), new TuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct error = (TuStruct) info.getTerm("Error");
		assertTrue(error.isEqual(new TuStruct("zero_divisor")));
	}
	
	// verifico che is(X, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_is_2_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("is", new TuVar("X"), new TuStruct("//", new TuInt(1), new TuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct error = (TuStruct) info.getTerm("Error");
		assertTrue(error.isEqual(new TuStruct("zero_divisor")));
	}
	
	// verifico che is(X, 1 div 0) lancia l'errore di valutazione "zero_divisor"
	public void test_is_2_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, 1 div 0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("is", new TuVar("X"), new TuStruct("div", new TuInt(1), new TuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct error = (TuStruct) info.getTerm("Error");
		assertTrue(error.isEqual(new TuStruct("zero_divisor")));
	}
	
	// verifico che '$tolist'(X, List) lancia un errore di instanziazione
	public void test_$tolist_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$tolist'(X, List), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$tolist", new TuVar("X"),
				new TuVar("List"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '$tolist'(1, List) lancia un errore di tipo
	public void test_$tolist_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$tolist'(1, List), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g
				.isEqual(new TuStruct("$tolist", new TuInt(1), new TuVar("List"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("struct")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che '$fromlist'(Struct, X) lancia un errore di instanziazione
	public void test_$fromlist_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$fromlist'(Struct, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$fromlist", new TuVar("Struct"),
				new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '$fromlist'(Struct, a) lancia un errore di tipo
	public void test_$fromlist_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$fromlist'(Struct, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$fromlist", new TuVar("Struct"),
				new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che '$append'(a, X) lancia un errore di instanziazione
	public void test_$append_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$append'(a, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$append", new TuStruct("a"),
				new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '$append'(a, b) lancia un errore di tipo
	public void test_$append_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$append'(a, b), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$append", new TuStruct("a"), new TuStruct(
				"b"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("b")));
	}

	// verifico che '$find'(X, []) lancia un errore di instanziazione
	public void test_$find_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$find'(X, []), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$find", new TuVar("X"), new TuStruct())));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '$find'(p(X), a) lancia un errore di tipo
	public void test_$find_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$find'(p(X), a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$find", new TuStruct("p", new TuVar("X")),
				new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che set_prolog_flag(X, 1) lancia un errore di instanziazione
	public void test_set_prolog_flag_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("set_prolog_flag", new TuVar("X"),
				new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che set_prolog_flag(a, X) lancia un errore di instanziazione
	public void test_set_prolog_flag_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(a, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("set_prolog_flag", new TuStruct("a"),
				new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che set_prolog_flag(1, 1) lancia un errore di tipo
	public void test_set_prolog_flag_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(1, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("set_prolog_flag", new TuInt(1), new TuInt(
				1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("struct")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che set_prolog_flag(a, p(X)) lancia un errore di tipo
	public void test_set_prolog_flag_2_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(a, p(X)), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("set_prolog_flag", new TuStruct("a"),
				new TuStruct("p", new TuVar("X")))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("ground")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("p", new TuVar("X"))));
	}

	// verifico che set_prolog_flag(Flag, Value) lancia un errore di dominio se
	// il Flag non e' definito nel motore
	public void test_set_prolog_flag_2_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(a, 1), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("set_prolog_flag", new TuStruct("a"),
				new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(new TuStruct("prolog_flag")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che set_prolog_flag(bounded, a) lancia un errore di dominio
	public void test_set_prolog_flag_2_6() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(bounded, a), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("set_prolog_flag",
				new TuStruct("bounded"), new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(new TuStruct("flag_value")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che set_prolog_flag(bounded, false) lancia un errore di permesso
	public void test_set_prolog_flag_2_7() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(bounded, false), error(permission_error(Operation, ObjectType, Culprit), permission_error(Goal, Operation, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("set_prolog_flag",
				new TuStruct("bounded"), new TuStruct("false"))));
		TuStruct operation = (TuStruct) info.getTerm("Operation");
		assertTrue(operation.isEqual(new TuStruct("modify")));
		TuStruct objectType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(objectType.isEqual(new TuStruct("flag")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("bounded")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(new TuInt(0)));
	}

	// verifico che get_prolog_flag(X, Value) lancia un errore di instanziazione
	public void test_get_prolog_flag_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(get_prolog_flag(X, Value), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("get_prolog_flag", new TuVar("X"),
				new TuVar("Value"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che get_prolog_flag(1, Value) lancia un errore di tipo
	public void test_get_prolog_flag_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(get_prolog_flag(1, Value), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("get_prolog_flag", new TuInt(1), new TuVar(
				"Value"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("struct")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che get_prolog_flag(Flag, Value) lancia un errore di dominio se
	// il Flag non e' definito nel motore
	public void test_get_prolog_flag_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(get_prolog_flag(a, Value), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("get_prolog_flag", new TuStruct("a"),
				new TuVar("Value"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(new TuStruct("prolog_flag")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che '$op'(Priority, yfx, '+') lancia un errore di instanziazione
	public void test_$op_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(Priority, yfx, '+'), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuVar("Priority"), new TuStruct(
				"yfx"), new TuStruct("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '$op'(600, Specifier, '+') lancia un errore di
	// instanziazione
	public void test_$op_3_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(600, Specifier, '+'), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuInt(600), new TuVar(
				"Specifier"), new TuStruct("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '$op'(600, yfx, Operator) lancia un errore di instanziazione
	public void test_$op_3_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(600, yfx, Operator), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuInt(600), new TuStruct("yfx"),
				new TuVar("Operator"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 3);
	}

	// verifico che '$op'(a, yfx, '+') lancia un errore di tipo
	public void test_$op_3_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(a, yfx, '+'), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuStruct("a"), new TuStruct(
				"yfx"), new TuStruct("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("integer")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che '$op'(600, 1, '+') lancia un errore di tipo
	public void test_$op_3_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(600, 1, '+'), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuInt(600), new TuInt(1),
				new TuStruct("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che '$op'(600, yfx, 1) lancia un errore di tipo
	public void test_$op_3_6() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(600, yfx, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuInt(600), new TuStruct("yfx"),
				new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 3);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom_or_atom_list")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che '$op'(1300, yfx, '+') lancia un errore di dominio
	public void test_$op_3_7() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(1300, yfx, '+'), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuInt(1300),
				new TuStruct("yfx"), new TuStruct("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(new TuStruct("operator_priority")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1300);
	}

	// verifico che '$op'(600, a, '+') lancia un errore di dominio
	public void test_$op_3_8() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(600, a, '+'), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("$op", new TuInt(600), new TuStruct("a"),
				new TuStruct("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(new TuStruct("operator_specifier")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

}