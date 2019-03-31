package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

/**
 * @author Matteo Iuliani
 * 
 *         Test del funzionamento delle eccezioni lanciate dai predicati della
 *         DCGLibrary
 */
public class DCGLibraryExceptionsTestCase extends TestCase {

	// verifico che phrase(X, []) lancia un errore di instanziazione
	public void test_phrase_2_1() throws Exception {
		TuProlog engine = new TuProlog();
		engine.loadLibrary("alice.tuprolog.lib.DCGLibrary");
		String goal = "catch(phrase(X, []), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.createTuStruct2("phrase_guard", createTuVarNamed("X"), createTuEmpty())));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

	// verifico che phrase(X, [], []) lancia un errore di instanziazione
	public void test_phrase_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		engine.loadLibrary("alice.tuprolog.lib.DCGLibrary");
		String goal = "catch(phrase(X, [], []), error(instantiation_error, instantiation_error(Goal, ArgNo)), true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuStruct g = (TuStruct) info.getTerm("Goal");
		assertTrue(g.isEqual(TuFactory.S("phrase_guard", createTuVarNamed("X"), createTuEmpty(), createTuEmpty())));
		TuInt argNo = (TuInt) info.getTerm("ArgNo");
		assertTrue(argNo.intValue() == 1);
	}

}