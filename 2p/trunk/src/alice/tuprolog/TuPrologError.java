package alice.tuprolog;

/**
 * @author Matteo Iuliani
 */
public class TuPrologError extends Throwable {
	private static final long serialVersionUID = 1L;
	// termine Prolog che rappresenta l'argomento di throw/1
	private Term error;
	/*Castagna 06/2011*/
	private String descriptionError;
	/**/

	public TuPrologError(Term error) {
		this.error = error;
	}

	/*Castagna 06/2011*/	
	/*	
	sintassi da prevedere:
	TYPE	in	argument	ARGUMENT	of			GOAL				(instantiation, type, domain, existence, representation, evaluation)
	TYPE	in				GOAL										(permission, resource)
	TYPE	at clause#CLAUSE, line#LINE, position#POS: DESCRIPTION		(syntax)
	TYPE																(system)
	*/
	/**/

	/*Castagna 06/2011*/
	@Override
	public String toString()
	{
		return descriptionError;
	}
	/**/

	/*Castagna 06/2011*/	
	public TuPrologError(Term error, String descriptionError) {
		this.error = error;	
		this.descriptionError = descriptionError;
	}
	/**/

	public Term getError() {
		return error;
	}

	public static TuPrologError instantiation_error(EngineManager engineManager, int argNo) {
		Term errorTerm = TuTerm.createAtomTerm("instantiation_error");
		Term tuPrologTerm = TuStruct.createTuStruct2("instantiation_error", engineManager.getEnv().currentContext.currentGoal, TuTerm.i32(argNo));
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));		
		String descriptionError =  "Instantiation error" +
		" in argument " + argNo + 
		" of " + engineManager.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/	
	}

	public static TuPrologError type_error(EngineManager e, int argNo, String validType, Term culprit) {
		Term errorTerm = TuStruct.createTuStruct2("type_error", TuTerm.createAtomTerm(validType), culprit);
		Term tuPrologTerm = TuStruct.createSTRUCT("type_error", e.getEnv().currentContext.currentGoal, TuTerm.i32(argNo), TuTerm.createAtomTerm(validType), culprit);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Type error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError domain_error(EngineManager e, int argNo, String validDomain, Term culprit) {
		Term errorTerm = TuStruct.createTuStruct2("domain_error", TuTerm.createAtomTerm(validDomain), culprit);
		Term tuPrologTerm = TuStruct.createSTRUCT("domain_error", e.getEnv().currentContext.currentGoal, TuTerm.i32(argNo), TuTerm.createAtomTerm(validDomain), culprit);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Domain error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError existence_error(EngineManager e, int argNo, String objectType, Term culprit, Term message) {
		Term errorTerm = TuStruct.createTuStruct2("existence_error", TuTerm.createAtomTerm(objectType), culprit);
		Term tuPrologTerm = TuStruct.createSTRUCT("existence_error", e.getEnv().currentContext.currentGoal, TuTerm.i32(argNo), TuTerm.createAtomTerm(objectType), culprit, message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Existence error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError permission_error(EngineManager e,	String operation, String objectType, Term culprit, Term message) {
		Term errorTerm = TuStruct.createSTRUCT("permission_error", TuTerm.createAtomTerm(operation), TuTerm.createAtomTerm(objectType), culprit);
		Term tuPrologTerm = TuStruct.createSTRUCT("permission_error", e.getEnv().currentContext.currentGoal, TuTerm.createAtomTerm(operation), TuTerm.createAtomTerm(objectType), culprit, message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Permission error" + 
		" in  " + e.getEnv().currentContext.currentGoal.toString();	
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError representation_error(EngineManager e, int argNo, String flag) {
		Term errorTerm = TuStruct.createTuStruct1("representation_error", TuTerm.createAtomTerm(flag));
		Term tuPrologTerm = TuStruct.createSTRUCT("representation_error", e.getEnv().currentContext.currentGoal, TuTerm.i32(argNo), TuTerm.createAtomTerm(flag));
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));
		String descriptionError =  "Representation error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError evaluation_error(EngineManager e, int argNo, String error) {
		Term errorTerm = TuStruct.createTuStruct1("evaluation_error", TuTerm.createAtomTerm(error));
		Term tuPrologTerm = TuStruct.createSTRUCT("evaluation_error", e.getEnv().currentContext.currentGoal, TuTerm.i32(argNo), TuTerm.createAtomTerm(error));
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Evaluation error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError resource_error(EngineManager e, Term resource) {
		Term errorTerm = TuStruct.createTuStruct1("resource_error", resource);
		Term tuPrologTerm = TuStruct.createTuStruct2("resource_error", e.getEnv().currentContext.currentGoal, resource);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));		
		String descriptionError =  "Resource error" + 
		" in " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError syntax_error(EngineManager e, 
			/*Castagna 06/2011*/			
			int clause, 
			/**/			
			int line, int position, Term message) {
		Term errorTerm = TuStruct.createTuStruct1("syntax_error", message);
		Term tuPrologTerm = TuStruct.createSTRUCT("syntax_error", e.getEnv().currentContext.currentGoal, TuTerm.i32(line), TuTerm.i32(position), message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));

		int[] errorInformation = {clause, line, position};
		String[] nameInformation = {"clause", "line", "position"};
		String syntaxErrorDescription = message.getTerm().toString();

		{
			//Sostituzione degli eventuali caratteri di nuova linea con uno spazio
			syntaxErrorDescription = syntaxErrorDescription.replace("\n", " ");
			//Eliminazione apice di apertura e chiusura stringa
			syntaxErrorDescription = syntaxErrorDescription.substring(1, syntaxErrorDescription.length()-1);
			String start = 	(""+syntaxErrorDescription.charAt(0)).toLowerCase();
			//Resa minuscola l'iniziale
			syntaxErrorDescription = start + syntaxErrorDescription.substring(1);
		}

		String descriptionError = "Syntax error";

		boolean firstSignificativeInformation = true;
		for(int i = 0; i < errorInformation.length; i++)
		{
			if(errorInformation[i] != -1)
				if(firstSignificativeInformation)
				{
					descriptionError += " at " + nameInformation[i] + "#" + errorInformation[i];
					firstSignificativeInformation = false;
				}
				else
					descriptionError += ", " + nameInformation[i] + "#" + errorInformation[i];	
		}
		descriptionError += ": " + syntaxErrorDescription;

		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError system_error(Term message) {
		Term errorTerm = TuTerm.createAtomTerm("system_error");
		Term tuPrologTerm = TuStruct.createTuStruct1("system_error", message);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));
		String descriptionError = "System error";
		return new TuPrologError(TuStruct.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

}
