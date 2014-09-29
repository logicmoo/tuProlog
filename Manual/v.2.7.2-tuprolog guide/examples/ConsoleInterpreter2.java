    import alice.tuprolog.*;
    import java.io.*;
    
    public class ConsoleInterpreter2  {
     public static void main (String args[]) throws Exception {
      Prolog engine=new Prolog();
      if (args.length>0)
          engine.setTheory(new Theory(":-consult('" + args[0] + "')." ));
      BufferedReader stdin =
          new BufferedReader(new InputStreamReader(System.in));
      while (true) {
       String goal;
       do { 
		   System.out.print("?- "); goal=stdin.readLine();
       } while (goal.equals(""));
       try {
        SolveInfo info = engine.solve(goal);
        if (engine.isHalted()) break;
        else if (!info.isSuccess()) System.out.println("no.");
        else if (!engine.hasOpenAlternatives()) {
			System.out.println(info);
		} else {
		  // main case
          System.out.println(info + " ?");
          String answer = stdin.readLine();
          while (answer.equals(";") && engine.hasOpenAlternatives()) {
            info = engine.solveNext();
            if (!info.isSuccess()) { System.out.println("no."); break; }
            else { 
				System.out.println(info + " ?");
                answer = stdin.readLine();
            }
          }  // endwhile
          if (answer.equals(";") && !engine.hasOpenAlternatives())
              System.out.println("no.");
        }  // end main case
       } catch (MalformedGoalException ex) {
              System.err.println("syntax error.");
       }  // end try
      }  // end main loop
      if (args.length>1) {
       Theory curTh = engine.getTheory();  // save current theory to file
       new FileOutputStream(args[1]).write(curTh.toString().getBytes());
      }
     }
    }
