package alice.tuprolog;

import fit.Fixture;

public class EvaluationFixture extends Fixture {
    
    public String evaluable;
    
    public static final boolean EXCEPTIONS_IMPLEMENTED = false;
    
    /* Registers */
    
    public void evaluable(String evaluable) {
        this.evaluable = evaluable;
    }
    
    /* Meters */
    
    public Term value() throws Exception {
        Prolog engine = new Prolog();
        SolveInfo result = engine.solve("X is " + evaluable);
        return result.getVarValue("X");
    }
    
    public void exception() {
    }
    
    /* Utilities */

	@SuppressWarnings("rawtypes")
	public Object parse(String s, Class type) throws Exception {
		if (type.equals(alice.tuprolog.Term.class))
			return Term.createTerm(s);
		return super.parse(s, type);
	}
    
}