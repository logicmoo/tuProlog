package alice.tuprolog;

import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
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
		Term errorTerm = createTuAtom("instantiation_error");
		Term tuPrologTerm = createTuStruct2("instantiation_error", engineManager.getEnv().currentContext.currentGoal, createTuInt(argNo));
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));		
		String descriptionError =  "Instantiation error" +
		" in argument " + argNo + 
		" of " + engineManager.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/	
	}

	public static TuPrologError type_error(EngineManager e, int argNo, String validType, Term culprit) {
		Term errorTerm = createTuStruct2("type_error", createTuAtom(validType), culprit);
		Term tuPrologTerm = S("type_error", e.getEnv().currentContext.currentGoal, createTuInt(argNo), createTuAtom(validType), culprit);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Type error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError domain_error(EngineManager e, int argNo, String validDomain, Term culprit) {
		Term errorTerm = createTuStruct2("domain_error", createTuAtom(validDomain), culprit);
		Term tuPrologTerm = S("domain_error", e.getEnv().currentContext.currentGoal, createTuInt(argNo), createTuAtom(validDomain), culprit);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Domain error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError existence_error(EngineManager e, int argNo, String objectType, Term culprit, Term message) {
		Term errorTerm = createTuStruct2("existence_error", createTuAtom(objectType), culprit);
		Term tuPrologTerm = S("existence_error", e.getEnv().currentContext.currentGoal, createTuInt(argNo), createTuAtom(objectType), culprit, message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Existence error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError permission_error(EngineManager e,	String operation, String objectType, Term culprit, Term message) {
		Term errorTerm = S("permission_error", createTuAtom(operation), createTuAtom(objectType), culprit);
		Term tuPrologTerm = S("permission_error", e.getEnv().currentContext.currentGoal, createTuAtom(operation), createTuAtom(objectType), culprit, message);
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Permission error" + 
		" in  " + e.getEnv().currentContext.currentGoal.toString();	
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError representation_error(EngineManager e, int argNo, String flag) {
		Term errorTerm = S("representation_error", createTuAtom(flag));
		Term tuPrologTerm = S("representation_error", e.getEnv().currentContext.currentGoal, createTuInt(argNo), createTuAtom(flag));
		/*Castagna 06/2011*/
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));
		String descriptionError =  "Representation error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError evaluation_error(EngineManager e, int argNo, String error) {
		Term errorTerm = S("evaluation_error", createTuAtom(error));
		Term tuPrologTerm = S("evaluation_error", e.getEnv().currentContext.currentGoal, createTuInt(argNo), createTuAtom(error));
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));	
		String descriptionError =  "Evaluation error" + 
		" in argument " + argNo + 
		" of " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError resource_error(EngineManager e, Term resource) {
		Term errorTerm = S("resource_error", resource);
		Term tuPrologTerm = createTuStruct2("resource_error", e.getEnv().currentContext.currentGoal, resource);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));		
		String descriptionError =  "Resource error" + 
		" in " + e.getEnv().currentContext.currentGoal.toString();
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

	public static TuPrologError syntax_error(EngineManager e, 
			/*Castagna 06/2011*/			
			int clause, 
			/**/			
			int line, int position, Term message) {
		Term errorTerm = S("syntax_error", message);
		Term tuPrologTerm = S("syntax_error", e.getEnv().currentContext.currentGoal, createTuInt(line), createTuInt(position), message);
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

		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/
	}

	public static TuPrologError system_error(Term message) {
		Term errorTerm = createTuAtom("system_error");
		Term tuPrologTerm = S("system_error", message);
		/*Castagna 06/2011*/		
		//return new PrologError(new Struct("error", errorTerm, tuPrologTerm));
		String descriptionError = "System error";
		return new TuPrologError(TuFactory.createTuStruct2("error", errorTerm, tuPrologTerm), descriptionError);
		/**/		
	}

}
