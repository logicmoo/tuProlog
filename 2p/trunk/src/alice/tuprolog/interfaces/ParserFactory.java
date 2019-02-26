package alice.tuprolog.interfaces;

import java.util.HashMap;

import alice.tuprolog.OperatorManager;
import alice.tuprolog.TuParser;
import alice.tuprolog.Term;

public class ParserFactory {
	
	/**
     * Creating a parser with default operator interpretation
     */
	public static IParser createParser(String theory) {
		return new TuParser(theory);
	}
	
	/**
     * creating a parser with default operator interpretation
     */
    public static IParser createParser(String theory, HashMap<Term, Integer> mapping) {
    	return new TuParser(theory, mapping);
    }    
	
	/**
     * creating a Parser specifing how to handle operators
     * and what text to parse
     */
    public static IParser createParser(IOperatorManager op, String theory) {
    	return new TuParser((OperatorManager)op, theory);
    }
    
    /**
     * creating a Parser specifing how to handle operators
     * and what text to parse
     */
    public static IParser createParser(IOperatorManager op, String theory, HashMap<Term, Integer> mapping) {
    	return new TuParser((OperatorManager)op, theory, mapping);
    }

}
