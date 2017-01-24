package alice.tuprolog.interfaces;

import alice.tuprolog.Library;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.event.OutputListener;
import alice.tuprolog.event.SpyListener;


public interface IProlog {
	
	
	IOperatorManager getOperatorManager();
		
	
	IPrimitiveManager getPrimitiveManager();
	
	
	Theory getTheory();
	
	
	void addTheory(Theory th) throws Exception;
	
	
	void clearTheory();
	
	
	String[] getCurrentLibraries();
	
	
	Library getLibrary(String name);
	
	Library loadLibrary(String className) throws Exception;
	
	
	void unloadLibrary(String name) throws Exception;
	
	
	SolveInfo solve(String st) throws Exception;
	
	
	SolveInfo solveNext() throws Exception;
	
	
	void solveHalt();
	
	void solveEnd();
	
	
	boolean hasOpenAlternatives();
	

	String toString(Term term);
	
	void addOutputListener(OutputListener l);
	
	
	void removeOutputListener(OutputListener l);
	
	
	void removeAllOutputListeners();
	
	
	void setSpy(boolean state);
	
	
	void addSpyListener(SpyListener l);
	
	
	void removeSpyListener(SpyListener l);
	
	
	void removeAllSpyListeners();

	
	
	void addExceptionListener(ExceptionListener l);

	
	void removeExceptionListener(ExceptionListener l);

	
	void removeAllExceptionListeners();

}
