package alice.tuprolog;

import junit.framework.TestCase;

/**
 * @author Matteo Iuliani
 * 
 *         Test del funzionamento dei predicati java_throw/1 e java_catch/3
 */
public class JavaThrowCatchTestCase extends TestCase {

	// verifico che il gestore venga eseguito con le sostituzioni effettuate
	// durante il processo di unificazione tra l'eccezione e il catcher, e che
	// successivamente venga eseguito il finally
	public void test_java_catch_3_1() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "atom_length(err, 3), java_catch(java_object('Counter', ['MyCounter'], c), [('java.lang.ClassNotFoundException'(Cause, Message, StackTrace), ((X is Cause+2, 5 is X+3)))], Y is 2+3), Z is X+5.";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuInt cause = (TuInt) info.getTerm("Cause");
		assertTrue(cause.intValue() == 0);
		TuStruct message = (TuStruct) info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("Counter")));
		TuStruct stackTrace = (TuStruct) info.getTerm("StackTrace");
		assertTrue(stackTrace.isList());
		TuInt x = (TuInt) info.getTerm("X");
		assertTrue(x.intValue() == 2);
		TuInt y = (TuInt) info.getTerm("Y");
		assertTrue(y.intValue() == 5);
		TuInt z = (TuInt) info.getTerm("Z");
		assertTrue(z.intValue() == 7);
	}

	// verifico che venga eseguito il piu' vicino antenato java_catch/3
	// nell'albero di risoluzione che abbia un catcher unificabile con
	// l'argomento di java_throw/1
	public void test_java_catch_3_2() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "java_catch(java_object('Counter', ['MyCounter'], c), [('java.lang.ClassNotFoundException'(Cause, Message, StackTrace), true)], true), java_catch(java_object('Counter', ['MyCounter2'], c2), [('java.lang.ClassNotFoundException'(C, M, ST), X is C+2)], true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		TuInt cause = (TuInt) info.getTerm("Cause");
		assertTrue(cause.intValue() == 0);
		TuStruct message = (TuStruct) info.getTerm("Message");
		assertTrue(message.isEqual(new TuStruct("Counter")));
		TuStruct stackTrace = (TuStruct) info.getTerm("StackTrace");
		assertTrue(stackTrace.isList());
		TuInt x = (TuInt) info.getTerm("X");
		assertTrue(x.intValue() == 2);
	}

	// verifico che l'esecuzione fallisce se si verifica un errore durante
	// l'esecuzione di un goal e non viene trovato nessun nodo java_catch/3
	// avente un catcher unificabile con l'argomento dell'eccezione lanciata
	public void test_java_catch_3_3() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "java_catch(java_object('Counter', ['MyCounter'], c), [('java.lang.Exception'(Cause, Message, StackTrace), true)], true).";
		SolveInfo info = engine.solve(goal);
		assertFalse(info.isSuccess());
		assertTrue(info.isHalted());
	}

	// verifico che catch/3 fallisce se il gestore e' falso
	public void test_java_catch_3_4() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "java_catch(java_object('Counter', ['MyCounter'], c), [('java.lang.ClassNotFoundException'(Cause, Message, StackTrace), false)], true).";
		SolveInfo info = engine.solve(goal);
		assertFalse(info.isSuccess());
	}

	// verifico che il finally venga eseguito in caso di successo di JavaGoal
	public void test_java_catch_3_5() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "java_catch(java_object('java.util.ArrayList', [], l), [(E, true)], (X is 2+3, Y is 3+5)).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		Term e = info.getTerm("E");
		assertTrue(e instanceof TuVar);
		TuInt x = (TuInt) info.getTerm("X");
		assertTrue(x.intValue() == 5);
		TuInt y = (TuInt) info.getTerm("Y");
		assertTrue(y.intValue() == 8);
	}

	// verifico che catch/3 fallisce se si verifica un'eccezione durante
	// l'esecuzione del gestore
	public void test_java_catch_3_6() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "java_catch(java_object('Counter', ['MyCounter'], c), [('java.lang.ClassNotFoundException'(Cause, Message, StackTrace), java_object('Counter', ['MyCounter2'], c2))], true).";
		SolveInfo info = engine.solve(goal);
		assertFalse(info.isSuccess());
		assertTrue(info.isHalted());
	}

	// verifico la correttezza della ricerca del catcher all'interno della lista
	public void test_java_catch_3_7() throws Exception {
		TuProlog engine = new TuProlog();
		String goal = "java_catch(java_object('Counter', ['MyCounter'], c), [('java.lang.Exception'(Cause, Message, StackTrace), X is 2+3), ('java.lang.ClassNotFoundException'(Cause, Message, StackTrace), Y is 3+5)], true).";
		SolveInfo info = engine.solve(goal);
		assertTrue(info.isSuccess());
		Term x = info.getTerm("X");
		assertTrue(x instanceof TuVar);
		Term y = info.getTerm("Y");
		assertTrue(y instanceof TuInt);
		assertTrue(((TuInt) y).intValue() == 8);
	}

}