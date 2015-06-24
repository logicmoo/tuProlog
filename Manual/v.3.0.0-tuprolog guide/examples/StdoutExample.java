    import alice.tuprolog.*;
    import alice.tuprolog.lib.*;

    public class StdoutExample {
        public static void main(String[] args) throws Exception {
			Prolog engine = new Prolog();
//			Library lib = engine.loadLibrary("alice.tuprolog.lib.JavaLibrary");
			Library lib = engine.getLibrary("alice.tuprolog.lib.JavaLibrary");
//			Library lib = engine.getLibrary("JavaLibrary");
			((JavaLibrary)lib).register(new Struct("stdout"), System.out);
			engine.setTheory(new Theory(":-solve(go). \n go:- stdout <- println('hello!')."));
      }
    }
