    import alice.tuprolog.*;
    import alice.tuprolog.lib.*;
	import alice.tuprolog.event.*;

    public class LoadLibraryMain {
        public static void main(String[] args) throws Exception {
			Prolog engine = new Prolog();
			Library lib2 = engine.loadLibrary("HybridLibrary");
			System.out.println("Lib2 " + (lib2==null ? "NOT " : " ") + "LOADED");
			// res = engine.solve("mprint(henry).");
			SolveInfo res = engine.solve("myprint(henry).");
			// stampa a video quanto segue:
			//   X_e115 / henry
			//   X_e123 / yrneh
			int count=0;
			while (engine.hasOpenAlternatives() && count < 5){
				count++;
				res = engine.solveNext();
			// stampa a video quanto segue:
			//   X_e115 / henry
			//   X_e123 / yrneh
			//   X_e154 / henry
			//   X_e176 / yrneh
			}
	  }
    }
