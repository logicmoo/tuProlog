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
		Term errorTerm = new TuStruct("instantiation_error");
		Term tuPrologTerm = new TuStruct("instantiation_error", engineManager.getEnv().currentContext.currentGoal, new TuInt(argNo));
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));		
		String descriptionError =  "Instantiation error" +
		" in argument " + argNo + 
		" of " + engineManager.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/	
	}

	public static TuPrologError type_error(EngineManager e, int argNo, String validType, Term culprit) {
		Term errorTerm = new TuStruct("type_error", new TuStruct(validType), culprit);
		Term tuPrologTerm = new TuStruct("type_error", e.getEnv().currentContext.currentGoal, new TuInt(argNo), new TuStruct(validType), culprit);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Type error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError domain_error(EngineManager e, int argNo, String validDomain, Term culprit) {
		Term errorTerm = new TuStruct("domain_error", new TuStruct(validDomain), culprit);
		Term tuPrologTerm = new TuStruct("domain_error", e.getEnv().currentContext.currentGoal, new TuInt(argNo), new TuStruct(validDomain), culprit);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Domain error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError existence_error(EngineManager e, int argNo, String objectType, Term culprit, Term message) {
		Term errorTerm = new TuStruct("existence_error", new TuStruct(objectType), culprit);
		Term tuPrologTerm = new TuStruct("existence_error", e.getEnv().currentContext.currentGoal, new TuInt(argNo), new TuStruct(objectType), culprit, message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Existence error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError permission_error(EngineManager e,	String operation, String objectType, Term culprit, Term message) {
		Term errorTerm = new TuStruct("permission_error", new TuStruct(operation), new TuStruct(objectType), culprit);
		Term tuPrologTerm = new TuStruct("permission_error", e.getEnv().currentContext.currentGoal, new TuStruct(operation), new TuStruct(objectType), culprit, message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Permission error" + 
		" in  " + e.getEnv().currentContext.currentGoal.toString();	
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError representation_error(EngineManager e, int argNo, String flag) {
		Term errorTerm = new TuStruct("representation_error", new TuStruct(flag));
		Term tuPrologTerm = new TuStruct("representation_error", e.getEnv().currentContext.currentGoal, new TuInt(argNo), new TuStruct(flag));
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));
		String descriptionError =  "Representation error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError evaluation_error(EngineManager e, int argNo, String error) {
		Term errorTerm = new TuStruct("evaluation_error", new TuStruct(error));
		Term tuPrologTerm = new TuStruct("evaluation_error", e.getEnv().currentContext.currentGoal, new TuInt(argNo), new TuStruct(error));
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Evaluation error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError resource_error(EngineManager e, Term resource) {
		Term errorTerm = new TuStruct("resource_error", resource);
		Term tuPrologTerm = new TuStruct("resource_error", e.getEnv().currentContext.currentGoal, resource);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));		
		String descriptionError =  "Resource error" + 
		" in " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError syntax_error(EngineManager e, 
			/*Castagna 06/2011*/			
			int clause, 
			/**/			
			int line, int position, Term message) {
		Term errorTerm = new TuStruct("syntax_error", message);
		Term tuPrologTerm = new TuStruct("syntax_error", e.getEnv().currentContext.currentGoal, new TuInt(line), new TuInt(position), message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));

		int[] errorInformation = {clause, line, position};
		String[] nameInformation = {"clause", "line", "position"};
		String syntaxErrorDescription = message.dref().toString();

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

		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError system_error(Term message) {
		Term errorTerm = new TuStruct("system_error");
		Term tuPrologTerm = new TuStruct("system_error", message);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));
		String descriptionError = "System error";
		return new TuPrologError(new TuStruct("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

}
