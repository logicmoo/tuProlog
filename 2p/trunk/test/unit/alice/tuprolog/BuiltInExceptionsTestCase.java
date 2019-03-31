package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

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
		assertTrue(g.isEqual(TuFactory.S("asserta", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("asserta", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("clause")));
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
		assertTrue(g.isEqual(TuFactory.S("assertz", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("assertz", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("clause")));
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
		assertTrue(g.isEqual(TuFactory.S("$retract", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("$retract", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("clause")));
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
		assertTrue(g.isEqual(TuFactory.S("abolish", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("abolish", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("predicate_indicator")));
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
		assertTrue(g.isEqual(TuFactory.S("abolish", S("p", createTuVarNamed("X")))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("predicate_indicator")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.S("p", createTuVarNamed("X"))));
	}

	// verifico che halt(X) lancia un errore di instanziazione
	public void test_halt_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(halt(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("halt", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("halt", createTuDouble(1.5))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("integer")));
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
		assertTrue(g.isEqual(TuFactory.S("load_library", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("load_library", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
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
		assertTrue(g.isEqual(TuFactory.S("load_library", createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("class")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(TuFactory.createTuAtom("InvalidLibraryException: a at -1:-1")));
	}

	// verifico che unload_library(X) lancia un errore di instanziazione
	public void test_unload_library_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(unload_library(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("unload_library", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("unload_library", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
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
		assertTrue(g.isEqual(TuFactory.S("unload_library", createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("class")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(TuFactory.createTuAtom("InvalidLibraryException: null at 0:0")));
	}

	// verifico che '$call'(X) lancia un errore di instanziazione
	public void test_$call_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$call'(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("$call", createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.S("$call", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("callable")));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("is", createTuVarNamed("X"), createTuVarNamed("Y"))));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("is", createTuVarNamed("X"), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}
	
	// verifico che is(X, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_is_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("is", createTuVarNamed("X"), createTuStruct2("/", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct error = (TuStruct) info.getTerm("Error");
		assertTrue(error.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}
	
	// verifico che is(X, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_is_2_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("is", createTuVarNamed("X"), createTuStruct2("//", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct error = (TuStruct) info.getTerm("Error");
		assertTrue(error.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}
	
	// verifico che is(X, 1 div 0) lancia l'errore di valutazione "zero_divisor"
	public void test_is_2_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(is(X, 1 div 0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("is", createTuVarNamed("X"), createTuStruct2("div", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct error = (TuStruct) info.getTerm("Error");
		assertTrue(error.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}
	
	// verifico che '$tolist'(X, List) lancia un errore di instanziazione
	public void test_$tolist_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$tolist'(X, List), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("$tolist", createTuVarNamed("X"), createTuVarNamed("List"))));
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
				.isEqual(TuFactory.createTuStruct2("$tolist", createTuInt(1), createTuVarNamed("List"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("struct")));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("$fromlist", createTuVarNamed("Struct"), createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("$fromlist", createTuVarNamed("Struct"), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '$append'(a, X) lancia un errore di instanziazione
	public void test_$append_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$append'(a, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("$append", createTuAtom("a"), createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("$append", createTuAtom("a"), createTuAtom("b"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("b")));
	}

	// verifico che '$find'(X, []) lancia un errore di instanziazione
	public void test_$find_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$find'(X, []), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("$find", createTuVarNamed("X"), createTuEmpty())));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("$find", S("p", createTuVarNamed("X")), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che set_prolog_flag(X, 1) lancia un errore di instanziazione
	public void test_set_prolog_flag_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("set_prolog_flag", createTuVarNamed("X"), createTuInt(1))));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("set_prolog_flag", createTuAtom("a"), createTuVarNamed("X"))));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("set_prolog_flag", createTuInt(1), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("struct")));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("set_prolog_flag", createTuAtom("a"), S("p", createTuVarNamed("X")))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("ground")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.S("p", createTuVarNamed("X"))));
	}

	// verifico che set_prolog_flag(Flag, Value) lancia un errore di dominio se
	// il Flag non e' definito nel motore
	public void test_set_prolog_flag_2_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(a, 1), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("set_prolog_flag", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(TuFactory.createTuAtom("prolog_flag")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che set_prolog_flag(bounded, a) lancia un errore di dominio
	public void test_set_prolog_flag_2_6() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(bounded, a), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("set_prolog_flag", createTuAtom("bounded"), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(TuFactory.createTuAtom("flag_value")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che set_prolog_flag(bounded, false) lancia un errore di permesso
	public void test_set_prolog_flag_2_7() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_prolog_flag(bounded, false), error(permission_error(Operation, ObjectType, Culprit), permission_error(Goal, Operation, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("set_prolog_flag", createTuAtom("bounded"), createTuAtom("false"))));
		TuStruct operation = (TuStruct) info.getTerm("Operation");
		assertTrue(operation.isEqual(TuFactory.createTuAtom("modify")));
		TuStruct objectType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(objectType.isEqual(TuFactory.createTuAtom("flag")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("bounded")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(TuFactory.createTuInt(0)));
	}

	// verifico che get_prolog_flag(X, Value) lancia un errore di instanziazione
	public void test_get_prolog_flag_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(get_prolog_flag(X, Value), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("get_prolog_flag", createTuVarNamed("X"), createTuVarNamed("Value"))));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("get_prolog_flag", createTuInt(1), createTuVarNamed("Value"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("struct")));
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
		assertTrue(g.isEqual(TuFactory.createTuStruct2("get_prolog_flag", createTuAtom("a"), createTuVarNamed("Value"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(TuFactory.createTuAtom("prolog_flag")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '$op'(Priority, yfx, '+') lancia un errore di instanziazione
	public void test_$op_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(Priority, yfx, '+'), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("$op", createTuVarNamed("Priority"), createTuAtom("yfx"), createTuAtom("+"))));
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
		assertTrue(g.isEqual(TuFactory.S("$op", createTuInt(600), createTuVarNamed("Specifier"), createTuAtom("+"))));
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
		assertTrue(g.isEqual(TuFactory.S("$op", createTuInt(600), createTuAtom("yfx"), createTuVarNamed("Operator"))));
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
		assertTrue(g.isEqual(TuFactory.S("$op", createTuAtom("a"), createTuAtom("yfx"), createTuAtom("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("integer")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '$op'(600, 1, '+') lancia un errore di tipo
	public void test_$op_3_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('$op'(600, 1, '+'), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("$op", createTuInt(600), createTuInt(1), createTuAtom("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
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
		assertTrue(g.isEqual(TuFactory.S("$op", createTuInt(600), createTuAtom("yfx"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 3);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom_or_atom_list")));
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
		assertTrue(g.isEqual(TuFactory.S("$op", createTuInt(1300), createTuAtom("yfx"), createTuAtom("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(TuFactory.createTuAtom("operator_priority")));
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
		assertTrue(g.isEqual(TuFactory.S("$op", createTuInt(600), createTuAtom("a"), createTuAtom("+"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(TuFactory.createTuAtom("operator_specifier")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

}