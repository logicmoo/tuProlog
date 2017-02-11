import alice.tuprolog.lib.*;
import alice.tuprolog.*;

public class HybridLibrary extends TestLibrary {
	public String getTheory(){
     return "myprint(X) :- println(X). \n" +
            "myprint(X) :- invert(X,Y), myprint(Y). \n" +
			"mprint(X)  :- println(X). \n" +
            "mprint(X)  :- atom_codes(X,LX), reverse(LX,LY), atom_codes(Y,LY), mprint(Y). \n";
	}
}

/*
test1 :-
 mprint(abcd).
test2 :-
 myprint(abcd).
*/