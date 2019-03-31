package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

/**
 * @author Matteo Iuliani
 * 
 *         Test del funzionamento delle eccezioni lanciate dai predicati della
 *         BasicLibrary
 */
public class BasicLibraryExceptionsTestCase extends TestCase {

	// verifico che set_theory(X) lancia un errore di instanziazione
	public void test_set_theory_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_theory(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("set_theory", createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che set_theory(1) lancia un errore di tipo
	public void test_set_theory_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_theory(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("set_theory", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che set_theory(a) lancia un errore di sintassi
	public void test_set_theory_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(set_theory(a), error(syntax_error(Message), syntax_error(Goal, Line, Position, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("set_theory", createTuAtom("a"))));
		TuInt line = (TuInt) info.getTerm("Line");
		assertTrue(line.intValue() == 1);
		TuInt position = (TuInt) info.getTerm("Line");
		assertTrue(position.intValue() == 1);
		TuStruct message = (TuStruct) info.getTerm("Message");
		assertTrue(message.isEqual(TuFactory.createTuAtom("The term 'a' is not ended with a period.")));
	}

	// verifico che add_theory(X) lancia un errore di instanziazione
	public void test_add_theory_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(add_theory(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("add_theory", createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che add_theory(1) lancia un errore di tipo
	public void test_add_theory_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(add_theory(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("add_theory", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che add_theory(a) lancia un errore di sintassi
	public void test_add_theory_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(add_theory(a), error(syntax_error(Message), syntax_error(Goal, Line, Position, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("add_theory", createTuAtom("a"))));
		TuInt line = (TuInt) info.getTerm("Line");
		assertTrue(line.intValue() == 1);
		TuInt position = (TuInt) info.getTerm("Line");
		assertTrue(position.intValue() == 1);
		TuStruct message = (TuStruct) info.getTerm("Message");
		assertTrue(message.isEqual(TuFactory.createTuAtom("The term 'a' is not ended with a period.")));
	}

	// verifico che agent(X) lancia un errore di instanziazione
	public void test_agent_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("agent", createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che agent(1) lancia un errore di tipo
	public void test_agent_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("agent", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che agent(X, a) lancia un errore di instanziazione
	public void test_agent_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent(X, a), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g
				.isEqual(TuFactory.createTuStruct2("agent", createTuVarNamed("X"), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che agent(a, X) lancia un errore di instanziazione
	public void test_agent_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent(a, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g
				.isEqual(TuFactory.createTuStruct2("agent", createTuAtom("a"), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che agent(1, a) lancia un errore di tipo
	public void test_agent_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent(1, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("agent", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che agent(a, 1) lancia un errore di tipo
	public void test_agent_2_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("agent", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("struct")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che '=:='(X, 1) lancia un errore di instanziazione
	public void test_expression_comparison_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=:='(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuVarNamed("X"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '=:='(1, X) lancia un errore di instanziazione
	public void test_expression_comparison_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=:='(1, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '=:='(a, 1) lancia un errore di tipo
	public void test_expression_comparison_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=:='(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '=:='(1, a) lancia un errore di tipo
	public void test_expression_comparison_2_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=:='(1, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '=\='(X, 1) lancia un errore di instanziazione
	public void test_expression_comparison_2_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=\\='(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuVarNamed("X"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '=\='(1, X) lancia un errore di instanziazione
	public void test_expression_comparison_2_6() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=\\='(1, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '=\='(a, 1) lancia un errore di tipo
	public void test_expression_comparison_2_7() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=\\='(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '=\='(1, a) lancia un errore di tipo
	public void test_expression_comparison_2_8() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=\\='(1, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '>'(X, 1) lancia un errore di instanziazione
	public void test_expression_comparison_2_9() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>'(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_than", createTuVarNamed("X"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '>'(1, X) lancia un errore di instanziazione
	public void test_expression_comparison_2_10() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>'(1, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_than", createTuInt(1), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '>'(a, 1) lancia un errore di tipo
	public void test_expression_comparison_2_11() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>'(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_than", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '>'(1, a) lancia un errore di tipo
	public void test_expression_comparison_2_12() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>'(1, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_than", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '<'(X, 1) lancia un errore di instanziazione
	public void test_expression_comparison_2_13() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('<'(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_than", createTuVarNamed("X"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '<'(1, X) lancia un errore di instanziazione
	public void test_expression_comparison_2_14() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('<'(1, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_than", createTuInt(1), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '<'(a, 1) lancia un errore di tipo
	public void test_expression_comparison_2_15() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('<'(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_than", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '<'(1, a) lancia un errore di tipo
	public void test_expression_comparison_2_16() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('<'(1, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_than", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '>='(X, 1) lancia un errore di instanziazione
	public void test_expression_comparison_2_17() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>='(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_or_equal_than", createTuVarNamed("X"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '>='(1, X) lancia un errore di instanziazione
	public void test_expression_comparison_2_18() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>='(1, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_or_equal_than", createTuInt(1), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '>='(a, 1) lancia un errore di tipo
	public void test_expression_comparison_2_19() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>='(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_or_equal_than", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '>='(1, a) lancia un errore di tipo
	public void test_expression_comparison_2_20() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>='(1, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_or_equal_than", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '=<'(X, 1) lancia un errore di instanziazione
	public void test_expression_comparison_2_21() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=<'(X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_or_equal_than", createTuVarNamed("X"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che '=<'(1, X) lancia un errore di instanziazione
	public void test_expression_comparison_2_22() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=<'(1, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_or_equal_than", createTuInt(1), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che '=<'(a, 1) lancia un errore di tipo
	public void test_expression_comparison_2_23() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=<'(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_or_equal_than", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '=<'(1, a) lancia un errore di tipo
	public void test_expression_comparison_2_24() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=<'(1, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_or_equal_than", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("evaluable")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che '=:='(1, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_25() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=:='(1, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuStruct2("/", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=\='(1, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_26() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=\\='(1, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuStruct2("/", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '>'(1, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_27() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>'(1, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_than", createTuInt(1), createTuStruct2("/", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '<'(1, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_28() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('<'(1, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_than", createTuInt(1), createTuStruct2("/", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '>='(1, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_29() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>='(1, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_or_equal_than", createTuInt(1), createTuStruct2("/", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=<'(1, 1/0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_30() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=<'(1, 1/0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_or_equal_than", createTuInt(1), createTuStruct2("/", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=:='(1, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_31() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=:='(1, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuStruct2("//", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=\='(1, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_32() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=\\='(1, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuInt(1), createTuStruct2("//", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '>'(1, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_33() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>'(1, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_than", createTuInt(1), createTuStruct2("//", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '<'(1, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_34() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('<'(1, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_than", createTuInt(1), createTuStruct2("//", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '>='(1, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_35() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>='(1, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_or_equal_than", createTuInt(1), createTuStruct2("//", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=<'(1, 1//0) lancia l'errore di valutazione "zero_divisor"
	public void test_expression_comparison_2_36() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=<'(1, 1//0), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_or_equal_than", createTuInt(1), createTuStruct2("//", createTuInt(1), createTuInt(0)))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=:='(1 div 0, 1) lancia l'errore di valutazione
	// "zero_divisor"
	public void test_expression_comparison_2_37() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=:='(1 div 0, 1), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuStruct2("div", createTuInt(1), createTuInt(0)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=\='(1 div 0, 1) lancia l'errore di valutazione
	// "zero_divisor"
	public void test_expression_comparison_2_38() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=\\='(1 div 0, 1), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_equality", createTuStruct2("div", createTuInt(1), createTuInt(0)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '>'(1 div 0, 1) lancia l'errore di valutazione
	// "zero_divisor"
	public void test_expression_comparison_2_39() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>'(1 div 0, 1), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_than", createTuStruct2("div", createTuInt(1), createTuInt(0)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '<'(1 div 0, 1) lancia l'errore di valutazione
	// "zero_divisor"
	public void test_expression_comparison_2_40() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('<'(1 div 0, 1), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_than", createTuStruct2("div", createTuInt(1), createTuInt(0)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '>='(1 div 0, 1) lancia l'errore di valutazione
	// "zero_divisor"
	public void test_expression_comparison_2_41() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('>='(1 div 0, 1), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_greater_or_equal_than", createTuStruct2("div", createTuInt(1), createTuInt(0)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che '=<'(1 div 0, 1) lancia l'errore di valutazione
	// "zero_divisor"
	public void test_expression_comparison_2_42() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch('=<'(1 div 0, 1), error(evaluation_error(Error), evaluation_error(Goal, ArgNo, Error)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("expression_less_or_equal_than", createTuStruct2("div", createTuInt(1), createTuInt(0)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("Error");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("zero_divisor")));
	}

	// verifico che text_concat(X, a, b) lancia un errore di instanziazione
	public void test_text_concat_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(text_concat(X, a, b), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("text_concat", createTuVarNamed("X"), createTuAtom("a"), createTuAtom("b"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che text_concat(a, X, b) lancia un errore di instanziazione
	public void test_text_concat_3_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(text_concat(a, X, b), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("text_concat", createTuAtom("a"), createTuVarNamed("X"), createTuAtom("b"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che text_concat(1, a, b) lancia un errore di tipo
	public void test_text_concat_3_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(text_concat(1, a, b), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("text_concat", createTuInt(1), createTuAtom("a"), createTuAtom("b"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che text_concat(a, 1, b) lancia un errore di tipo
	public void test_text_concat_3_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(text_concat(a, 1, b), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("text_concat", createTuAtom("a"), createTuInt(1), createTuAtom("b"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che num_atom(a, X) lancia un errore di tipo
	public void test_num_atom_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(num_atom(a, X), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("num_atom", createTuAtom("a"), createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("number")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che num_atom(1, 1) lancia un errore di tipo
	public void test_num_atom_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(num_atom(1, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("num_atom", createTuInt(1), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che num_atom(1, a) lancia un errore di dominio
	public void test_num_atom_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(num_atom(1, a), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g
				.isEqual(TuFactory.createTuStruct2("num_atom", createTuInt(1), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(TuFactory.createTuAtom("num_atom")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che arg(X, p(1), 1) lancia un errore di instanziazione
	public void test_arg_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(arg(X, p(1), 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("arg_guard", createTuVarNamed("X"), S("p", createTuInt(1)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che arg(1, X, 1) lancia un errore di instanziazione
	public void test_arg_3_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(arg(1, X, 1), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("arg_guard", createTuInt(1), createTuVarNamed("X"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che arg(a, p(1), 1) lancia un errore di tipo
	public void test_arg_3_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(arg(a, p(1), 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("arg_guard", createTuAtom("a"), S("p", createTuInt(1)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("integer")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che arg(1, p, 1) lancia un errore di tipo
	public void test_arg_3_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(arg(1, p, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("arg_guard", createTuInt(1), createTuAtom("p"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("compound")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("p")));
	}

	// verifico che arg(0, p(0), 1) lancia un errore di dominio
	public void test_arg_3_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(arg(0, p(0), 1), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("arg_guard", createTuInt(0), S("p", createTuInt(0)), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("greater_than_zero")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 0);
	}

	// verifico che clause(X, true) lancia un errore di instanziazione
	public void test_clause_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(clause(X, true), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("clause_guard", createTuVarNamed("X"), createTuAtom("true"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che call(X) lancia un errore di instanziazione
	public void test_call_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(call(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("call_guard", createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che call(1) lancia un errore di tipo
	public void test_call_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(call(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("call_guard", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("callable")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che findall(a, X, L) lancia un errore di instanziazione
	public void test_findall_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(findall(a, X, L), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("all_solutions_predicates_guard", createTuAtom("a"), createTuVarNamed("X"), createTuVarNamed("L"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che findall(a, 1, L) lancia un errore di tipo
	public void test_findall_3_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(findall(a, 1, L), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("all_solutions_predicates_guard", createTuAtom("a"), createTuInt(1), createTuVarNamed("L"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("callable")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che setof(a, X, L) lancia un errore di instanziazione
	public void test_setof_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(setof(a, X, L), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("all_solutions_predicates_guard", createTuAtom("a"), createTuVarNamed("X"), createTuVarNamed("L"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che setof(a, 1, L) lancia un errore di tipo
	public void test_setof_3_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(setof(a, 1, L), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("all_solutions_predicates_guard", createTuAtom("a"), createTuInt(1), createTuVarNamed("L"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("callable")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che bagof(a, X, L) lancia un errore di instanziazione
	public void test_bagof_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(bagof(a, X, L), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("all_solutions_predicates_guard", createTuAtom("a"), createTuVarNamed("X"), createTuVarNamed("L"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che bagof(a, 1, L) lancia un errore di tipo
	public void test_bagof_3_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(bagof(a, 1, L), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("all_solutions_predicates_guard", createTuAtom("a"), createTuInt(1), createTuVarNamed("L"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("callable")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che assert(X) lancia un errore di instanziazione
	public void test_assert_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(assert(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("assertz", createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che assert(1) lancia un errore di tipo
	public void test_assert_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(assert(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
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

	// verifico che retract(X) lancia un errore di instanziazione
	public void test_retract_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(retract(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("retract_guard", createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che retract(1) lancia un errore di tipo
	public void test_retract_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(retract(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("retract_guard", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("clause")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che retractall(X) lancia un errore di instanziazione
	public void test_retractall_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(retractall(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("retract_guard", createTuVarNamed("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che retractall(1) lancia un errore di tipo
	public void test_retractall_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(retractall(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("retract_guard", createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("clause")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che member(a, 1) lancia un errore di tipo
	public void test_member_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(member(a, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("member_guard", createTuAtom("a"), createTuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("list")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che reverse(a, []) lancia un errore di tipo
	public void test_reverse_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(reverse(a, []), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("reverse_guard", createTuAtom("a"), createTuEmpty())));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che delete(a, a, []) lancia un errore di tipo
	public void test_delete_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(delete(a, a, []), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("delete_guard", createTuAtom("a"), createTuAtom("a"), createTuEmpty())));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

	// verifico che element(1, a, a) lancia un errore di tipo
	public void test_element_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(element(1, a, a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("element_guard", createTuInt(1), createTuAtom("a"), createTuAtom("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(TuFactory.createTuAtom("list")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(TuFactory.createTuAtom("a")));
	}

}