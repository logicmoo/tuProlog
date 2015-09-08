    import alice.tuprolog.*;
    import alice.tuprolog.lib.*;
    import alice.tuprolog.event.*;

    public class OnOutputExample {
	  static String finalResult = "";
      public static void main(String[] args) throws Exception {
		Prolog engine = new Prolog();

		engine.addOutputListener(new OutputListener() {
		  @Override
		  public void onOutput(OutputEvent e) {
			  finalResult += e.getMsg();
		  }
		});

		Term goal = Term.createTerm("write('Hello world!')");
		SolveInfo res = engine.solve(goal);
		res = engine.solve("write('Hello everybody!'), nl.");
		System.out.println("OUTPUT: " + finalResult);
	  }
    }
