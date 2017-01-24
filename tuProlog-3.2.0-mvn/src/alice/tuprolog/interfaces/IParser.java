package alice.tuprolog.interfaces;

import alice.tuprolog.Term;

public interface IParser {
	
	
    Term nextTerm(boolean endNeeded) throws Exception;
    
    
    int getCurrentLine();
    
    
    int getCurrentOffset();
    
    
    int[] offsetToRowColumn(int offset);

}
