package alice.tuprolog;

import java.io.File;
import java.io.PrintWriter;

import junit.framework.TestCase;

/**
 * @author Matteo Iuliani
 * 
 *         Test del funzionamento delle eccezioni lanciate dai predicati della
 *         IOLibrary
 */
public class IOLibraryExceptionsTestCase extends TestCase {

	// verifico che see(X) lancia un errore di instanziazione
	public void test_see_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(see(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("see", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che see(1) lancia un errore di tipo
	public void test_see_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(see(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("see", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che see_1 lancia un errore di dominio se lo stream di input
	// StreamName non puo' essere acceduto
	public void test_see_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(see(a), error(domain_error(ValidDomain, Culprit), domain_error(Goal, ArgNo, ValidDomain, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("see", new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validDomain = (TuStruct) info.getTerm("ValidDomain");
		assertTrue(validDomain.isEqual(new TuStruct("stream")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che tell(X) lancia un errore di instanziazione
	public void test_tell_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(tell(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("tell", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che tell(1) lancia un errore di tipo
	public void test_tell_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(tell(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("tell", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che put(X) lancia un errore di instanziazione
	public void test_put_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(put(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("put", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che put(1) lancia un errore di tipo
	public void test_put_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(put(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("put", new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("character")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che put(aa) lancia un errore di tipo
	public void test_put_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(put(aa), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("put", new TuStruct("aa"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("character")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("aa")));
	}

	// verifico che tab(X) lancia un errore di instanziazione
	public void test_tab_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(tab(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("tab", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che tab(a) lancia un errore di tipo
	public void test_tab_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(tab(a), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("tab", new TuStruct("a"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("integer")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("a")));
	}

	// verifico che set_read_1 lancia un errore di sintassi se si verifica un
	// errore di sintassi durante la lettura dallo stream di input
	public void test_read_1_1() throws Exception {
		PrintWriter pw = new PrintWriter("read");
		pw.print("@term.");
		pw.close();
		TuProlog engine = new TuProlog();
		String goal = "see(read), catch(read(X), error(syntax_error(Message), syntax_error(Goal, Line, Position, Message)), true), seen.";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("read", new TuVar("X"))));
		TuInt line = (TuInt) info.getTerm("Line");
		assertTrue(line.intValue() == 1);
		TuInt position = (TuInt) info.getTerm("Line");
		assertTrue(position.intValue() == 1);
		TuStruct message = (TuStruct) info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("@term")));
		File f = new File("read");
		f.delete();
	}

	// verifico che write(X) lancia un errore di instanziazione
	public void test_write_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(write(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("write", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che print(X) lancia un errore di instanziazione
	public void test_print_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(print(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("print", new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che text_from_file(X, Y) lancia un errore di instanziazione
	public void test_text_from_file_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(text_from_file(X, Y), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("text_from_file", new TuVar("X"),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che text_from_file(1, Y) lancia un errore di tipo
	public void test_text_from_file_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(text_from_file(1, Y), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("text_from_file", new TuInt(1), new TuVar(
				"Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che text_from_file_2 lancia un errore di esistenza se il file
	// File non esiste
	public void test_text_from_file_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(text_from_file(text, Y), error(existence_error(ObjectType, Culprit), existence_error(Goal, ArgNo, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("text_from_file", new TuStruct("text"),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(new TuStruct("stream")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("text")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("File not found.")));
	}

	// verifico che agent_file(X) lancia un errore di instanziazione
	public void test_agent_file_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent_file(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuVar("X"), new TuVar(
				"Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che agent_file(1) lancia un errore di tipo
	public void test_agent_file_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent_file(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuInt(1),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che agent_file_1 lancia un errore di esistenza se il file
	// TheoryFileName non esiste
	public void test_agent_file_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(agent_file(text), error(existence_error(ObjectType, Culprit), existence_error(Goal, ArgNo, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuStruct("text"),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(new TuStruct("stream")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("text")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("File not found.")));
	}

	// verifico che solve_file(X, g) lancia un errore di instanziazione
	public void test_solve_file_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(solve_file(X, g), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuVar("X"), new TuVar(
				"Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che solve_file(1, g) lancia un errore di tipo
	public void test_solve_file_2_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(solve_file(1, g), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuInt(1),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che solve_file_2 lancia un errore di esistenza se il file
	// TheoryFileName non esiste
	public void test_solve_file_2_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(solve_file(text, g), error(existence_error(ObjectType, Culprit), existence_error(Goal, ArgNo, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuStruct("text"),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(new TuStruct("stream")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("text")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("File not found.")));
	}

	// verifico che solve_file(text, X) lancia un errore di instanziazione
	public void test_solve_file_2_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(solve_file(text, X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("solve_file_goal_guard", new TuStruct(
				"text"), new TuVar("X"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
	}

	// verifico che solve_file(text, 1) lancia un errore di tipo
	public void test_solve_file_2_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(solve_file(text, 1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(new TuStruct("solve_file_goal_guard", new TuStruct(
				"text"), new TuInt(1))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 2);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("callable")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che consult(X) lancia un errore di instanziazione
	public void test_consult_1_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(consult(X), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuVar("X"), new TuVar(
				"Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che consult(1) lancia un errore di tipo
	public void test_consult_1_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(consult(1), error(type_error(ValidType, Culprit), type_error(Goal, ArgNo, ValidType, Culprit)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuInt(1),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ValidType");
		assertTrue(validType.isEqual(new TuStruct("atom")));
		TuInt culprit = (TuInt) info.getTerm("Culprit");
		assertTrue(culprit.intValue() == 1);
	}

	// verifico che consult_1 lancia un errore di esistenza se il file
	// TheoryFileName non esiste
	public void test_consult_1_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "catch(consult(text), error(existence_error(ObjectType, Culprit), existence_error(Goal, ArgNo, ObjectType, Culprit, Message)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.match(new TuStruct("text_from_file", new TuStruct("text"),
				new TuVar("Y"))));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
		TuStruct validType = (TuStruct) info.getTerm("ObjectType");
		assertTrue(validType.isEqual(new TuStruct("stream")));
		TuStruct culprit = (TuStruct) info.getTerm("Culprit");
		assertTrue(culprit.isEqual(new TuStruct("text")));
		Term message = info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("File not found.")));
	}

}