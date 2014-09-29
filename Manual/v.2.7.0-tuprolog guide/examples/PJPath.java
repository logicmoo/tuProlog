import alice.tuprologx.pj.annotations.*;
import alice.tuprologx.pj.engine.*;
import alice.tuprologx.pj.model.*;
import alice.tuprologx.pj.meta.*;

@PrologClass (
	clauses={"arc(a,b).","arc(a,d).","arc(b,e).","arc(d,g).","arc(g,h).","arc(e,f).","arc(f,i).","arc(e,h)."}
)
public abstract class PJPath {
	@PrologMethod (
		clauses = {	"path(X,X,[X]).",
					"path(X,Y,[X|Q]):-arc(X,Z),path(Z,Y,Q)."}
	)
	public abstract <$X,$Y,$P> Iterable<$P> path($X from, $Y to);

	public static void main(String[] s) throws Exception {
		PJPath pjpath = PJ.newInstance(PJPath.class);
		for (Object solution : pjpath.path(new Atom("a"), new Var<Atom>("X"))) {
			System.out.println(solution);
		}
	}
}
/*
List[Atom(a)]
List[Atom(a), Atom(b)]
List[Atom(a), Atom(b), Atom(e)]
List[Atom(a), Atom(b), Atom(e), Atom(f)]
List[Atom(a), Atom(b), Atom(e), Atom(f), Atom(i)]
List[Atom(a), Atom(b), Atom(e), Atom(h)]
List[Atom(a), Atom(d)]
List[Atom(a), Atom(d), Atom(g)]
List[Atom(a), Atom(d), Atom(g), Atom(h)]
*/