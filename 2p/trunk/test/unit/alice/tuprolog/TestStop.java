package alice.tuprolog;

class PrologThread extends Thread {
	TuProlog core;
	String goal;
	PrologThread(TuProlog core, String goal){
		this.core = core;
		this.goal = goal;
	}

	@Override
	public void run(){
		try {
			System.out.println("STARTING...");
			SolveInfo info = core.solve(goal);
			System.out.println(info);
			System.out.println("STOP.");
		} catch (Exception ex){
			ex.printStackTrace();			
		}
	}	
}

public class TestStop {

	public static void main(String[] args) throws Exception {
		
		TuProlog core = new TuProlog();
		
		TuTheory th = new TuTheory(
			"rec(X):- current_thread <- sleep(X), X1 is X + 100, rec(X1).\n"
		);
		core.setTheory(th);
		
		
		new PrologThread(core,"rec(100).").start();
		
		Thread.sleep(2000);
		
		System.out.println("STOPPING...");
		
		core.solveHalt();
		
		Thread.sleep(2000);
		
		System.out.println("OK.");
	}
}
