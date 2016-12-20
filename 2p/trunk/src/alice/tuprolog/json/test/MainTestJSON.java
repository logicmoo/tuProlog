package alice.tuprolog.json.test;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;

//Alberto
public class MainTestJSON {
	
	public static void main(String[] args) {
		
		System.out.println("--- QUERY SUL PRIMO MOTORE PROLOG ---\n");
		Prolog p1 = new Prolog();
		String test = "setof(X, member(X, [y,K]), M).";
		
		String test2="member(A, [1, 2, 3, 4, 5]).";
		
		try {
			System.out.println("theory added -> pippo(a) :- true.\n");
			p1.addTheory(new Theory("pippo(a) :- true. "));
		} 
		
		catch (InvalidTheoryException e2) {
			e2.printStackTrace();
		}
		
		try {
			System.out.println("?- "+test);
			SolveInfo i  = SolveInfo.fromJSON(p1.solve(test).toJSON());
			System.out.println(i.toString());
			System.out.println(p1.solve(test).toString());
			System.out.println();
			System.out.println("?- "+test2);
			System.out.println(p1.solve(test2).toString());
			System.out.println();
			System.out.println(p1.solveNext().toString());
			/*System.out.println();
			System.out.println("?- "+"unload_library('alice.tuprolog.lib.ISOLibrary').");
			System.out.println(p1.solve("unload_library('alice.tuprolog.lib.ISOLibrary').").toString());
			System.out.println();
			System.out.println("?- pippo(a).");
			System.out.println(p1.solve("pippo(a).").toString());*/
		}
		
		catch (MalformedGoalException | NoMoreSolutionException e1) {
			e1.printStackTrace();
		}
		
		System.out.println();
		System.out.println("--- SERIALIZZAZIONE STATO DEL MOTORE ---");
	    String s = p1.toJSON(Prolog.INCLUDE_KB_IN_SERIALIZATION);
	    System.out.println("--- -------------------------------- ---");
	    System.out.println();
	    
	    try {
			System.out.println("theory added -> paperino(b) :- true.\n");
			p1.addTheory(new Theory("paperino(b) :- true. "));
		} 
	    
		catch (InvalidTheoryException e2) {
			e2.printStackTrace();
		}
	    
	    System.out.println("?- paperino(b).");
		
	    try {
			System.out.println(p1.solve("paperino(b).").toString());
		} 
	    
		catch (MalformedGoalException e1) {
			e1.printStackTrace();
		}
	    
	    System.out.println("\n--- QUERY SUL SECONDO MOTORE PROLOG ---\n");
	    
	    Prolog p2 = Prolog.fromJSON(s);
	    
	    try {
	    	System.out.println("?- "+test2+" next sol on new engine!");
	    	System.out.println(p2.solveNext().toString());
	    	System.out.println();
	    	System.out.println("?- "+test);
			System.out.println(p2.solve(test).toString());
			System.out.println();
			System.out.println("?- pippo(a).");
			System.out.println(p2.solve("pippo(a).").toString()+"\n");
			System.out.println("?- paperino(b).");
			System.out.println(p2.solve("paperino(b).").toString());
		}
	    
	    catch (MalformedGoalException | NoMoreSolutionException e) {
			e.printStackTrace();
		}
	}
}
