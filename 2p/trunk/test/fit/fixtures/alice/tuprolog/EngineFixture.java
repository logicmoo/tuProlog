package alice.tuprolog;

import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import fit.Fixture;

public class EngineFixture extends Fixture {
	private Prolog engine = new Prolog();
	private String query;
	private SolveInfo result;
	private String variable;
	private String output;

	public static final boolean EXCEPTIONS_IMPLEMENTED = false;

	/* Registers */

	public void theory(String program) throws InvalidTheoryException {
		engine.setTheory(new Theory(program));
	}

	public void query(String problem) {
		query = problem;
	}

	public void variable(String name) {
		variable = name;
	}

	/* Meters */

	public boolean hasSolution() throws MalformedGoalException {
		result = engine.solve(query);
		return result.isSuccess();
	}
	
	public boolean hasSolutionWithOutput() throws MalformedGoalException {
		output = "";
		engine.addOutputListener(new OutputListener() {
			public void onOutput(OutputEvent e) {
				output += e.getMsg();
			}
		});
		result = engine.solve(query);
		engine.removeAllOutputListeners();
		return result.isSuccess();
	}
    
    public boolean hasAnotherSolution() {
        try {
            result = engine.solveNext();
            return result.isSuccess();
        } catch (PrologException e) { // NoSolutionException, NoMoreSolutionException
            return false;
        }
    }
    
    public boolean hasAnotherSolutionWithOutput() {
    	output = "";
		engine.addOutputListener(new OutputListener() {
			public void onOutput(OutputEvent e) {
				output += e.getMsg();
			}
		});
		try {
            result = engine.solveNext();
            engine.removeAllOutputListeners();
            return result.isSuccess();
        } catch (PrologException e) { // NoSolutionException, NoMoreSolutionException
            return false;
        }
    }

	/*
	public boolean hasOpenAlternatives() {
		return engine.hasOpenAlternatives();
	}
	*/
	/*
	public String firstSolution() throws NoSolutionException {
		return result.getSolution().toString();
	}
	*/
	/*
	public String nextSolution() throws NoSolutionException, NoMoreSolutionException {
		result = engine.solveNext();
		return result.getSolution().toString();
	}
	*/
    
    public Term binding() throws NoSolutionException, UnknownVarException {
        Term t = result.getVarValue(variable);
		//t.resolveTerm();
		return t;
    }
    /*
    public String binding() throws NoSolutionException, UnknownVarException
    {
    	Term t = result.getVarValue(variable);
    	if (t.toString().startsWith("_"))
    	{
    		return "_";
    	}
    	return t.toString();
    }
    */
    public String output() {
    	return output;
    }
    
    /** Exceptions have not been implemented in tuProlog yet. */
	public Term exception() {
		return NullTerm.NULL_TERM;
	}
    
    public Theory getTheory() {
        return engine.getTheory();
    }

	/* Utilities */

	@SuppressWarnings("rawtypes")
	public Object parse(String s, Class type) throws Exception {
		if (type.equals(alice.tuprolog.Term.class))
			return Term.createTerm(s);
        if (type.equals(alice.tuprolog.Theory.class))
            return new Theory(s);
		return super.parse(s, type);
	}
}