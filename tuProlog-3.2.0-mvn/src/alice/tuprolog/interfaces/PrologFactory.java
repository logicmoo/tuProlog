package alice.tuprolog.interfaces;

import alice.tuprolog.Prolog;

public class PrologFactory {
	
	
	public static IProlog createProlog() {
		return new Prolog();
	}

}
