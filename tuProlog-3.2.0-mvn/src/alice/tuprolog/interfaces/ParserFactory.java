package alice.tuprolog.interfaces;

import java.util.HashMap;

import alice.tuprolog.OperatorManager;
import alice.tuprolog.Parser;
import alice.tuprolog.Term;

public class ParserFactory {
	
	
	public static IParser createParser(String theory) {
		return new Parser(theory);
	}
	
	
    public static IParser createParser(String theory, HashMap<Term, Integer> mapping) {
    	return new Parser(theory, mapping);
    }    
	
	
    public static IParser createParser(IOperatorManager op, String theory) {
    	return new Parser((OperatorManager)op, theory);
    }
    
    
    public static IParser createParser(IOperatorManager op, String theory, HashMap<Term, Integer> mapping) {
    	return new Parser((OperatorManager)op, theory, mapping);
    }

}
