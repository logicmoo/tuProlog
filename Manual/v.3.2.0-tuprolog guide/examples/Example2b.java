    import alice.tuprolog.*;

    public class Example2b {
        public static void main(String[] args) throws Exception {
			Var varX = new Var("X"), varY = new Var("Y");
			Struct atomP = new Struct("p");
			Struct list = new Struct(atomP, varY);	// should be [p|Y]
			System.out.println(list); // prints the list [p|Y]
			Struct fact = new Struct("p", new Struct("a"), new Int(5));
			Struct goal = new Struct("p", varX, new Var("Z"));
			Prolog engine = new Prolog();
			boolean res = goal.unify(engine, fact); 	// should be X/a, Y/5
			System.out.println(goal);  // prints the unified term p(a,5)
			System.out.println(varX);  // prints the variable binding X / a
			Var varW = new Var("W");
			res = varW.unify(engine, varY);	// should be Z=Y
			System.out.println(varY);		// prints just Y, since it is unbound
			System.out.println(varW);		// prints the variable binding W / Y
      }
    }
