    import alice.tuprolog.*;
    import alice.tuprolog.lib.*;

    public class TestLibraryMain {
        public static void main(String[] args) throws Exception {
			Prolog engine = new Prolog();
			Library lib1 = engine.loadLibrary("TestLibrary");
			System.out.println("Lib1 " + (lib1==null ? "NOT " : " ") + "LOADED");
			Theory testTheory = new Theory(
				"test1 :- N is sum(5,6), println(N).\n" +
				"test2 :- invert('ABCD',S), println(S).\n" +
				"name(ab).\n name(bc).\n name(uk).\n" +
				"test3 :- name(X), println(X), invert(X,Z), println(Z), fail.\n");
			engine.setTheory(testTheory);
			SolveInfo res = engine.solve("test1.");
			// stampa a video quanto segue:
			//   Lib1 LOADED!
			//   N_e2 / 11.0
			//   S_e4 / 'ABCD'
			res = engine.solve("test2.");
			// stampa a video quanto segue:
			//   S_e4 / abcd
			res = engine.solve("test3.");
			// stampa a video quanto segue:
			//   
			
	  }
    }
