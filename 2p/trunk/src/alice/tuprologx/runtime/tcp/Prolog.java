package alice.tuprologx.runtime.tcp;

import alice.tuprolog.*;

public interface Prolog {

    public void clearTheory() throws Exception;
    public TuTheory getTheory() throws Exception;
    /**
	 * @param theory
	 * @throws Exception
	 */
    void setTheory(TuTheory theory) throws Exception;
    void addTheory(TuTheory theory) throws Exception;

    public SolveInfo   solve(String g) throws Exception;
    public SolveInfo   solve(Term th) throws Exception;
    public SolveInfo   solveNext() throws Exception;
    public boolean     hasOpenAlternatives() throws Exception;
    public void solveHalt() throws Exception;
    public void solveEnd() throws Exception;

    public void loadLibrary(String className) throws Exception;
    public void unloadLibrary(String className) throws Exception;
}
