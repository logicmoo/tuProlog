    import alice.tuprologx.pj.annotations.*;
	import alice.tuprologx.pj.engine.*;
	import alice.tuprologx.pj.model.*;
	import alice.tuprologx.pj.meta.*;

	abstract class Perm{ 
		@PrologMethod ( clauses = {
			 "permutation([],[])." ,
			 "permutation(U,[X|V]):-remove(U,X,Z),permutation(Z,V)." ,
			 "remove([X|T],X,T)." ,
			 "remove([X|U],E,[X|V]):-remove(U,E,V)."
			 }
		)
		public abstract < $X extends List<Int>, $Y extends List<Int> >
		 Iterable<$Y> permutation($X list);
	}

    public class PJexample {
        public static void main(String[] args) throws Exception {
			java.util.Collection<Integer> v = java.util.Arrays.asList(1,2,3);
			 Perm p=PJ.newInstance(Perm.class);
			 for (List<Int> list : p.permutation(new List<Int>(v))) {
				 System.out.println(list.toJava());
			 }
		}
		/*
		[1, 2, 3]
		[1, 3, 2]
		[2, 1, 3]
		[2, 3, 1]
		[3, 1, 2]
		[3, 2, 1]
		*/
	}