import alice.tuprologx.pj.annotations.*;
import alice.tuprologx.pj.engine.*;
import alice.tuprologx.pj.model.*;
import alice.tuprologx.pj.meta.*;

@PrologClass(
	clauses={"size(X,Y):-length(X,Y)."}
)
public abstract class PJLength {
	
	@PrologMethod abstract <$Ls extends List<?>, $Ln extends Int> Boolean size($Ls expr, $Ln rest);
	@PrologMethod abstract <$Ls extends List<?>, $Ln extends Int> $Ln size($Ls expr);
	@PrologMethod abstract <$Ls extends List<?>, $Ln extends Int> $Ls size($Ln expr);
	@PrologMethod abstract <$Ls extends List<?>, $Ln extends Int> Iterable<Compound2<$Ls,$Ln>> size();

	public static void main(String[] args) throws Exception {
		PJLength pjl = PJ.newInstance(PJLength.class);
		java.util.List<?> v = java.util.Arrays.asList(12,"twelve",false);
		List<?> list = new List<Term<?>>(v);
		Boolean b = pjl.size(list, 3);	        //true: ’list’ is of size 3
		Int i = pjl.size(list);					//length of ’list’ is 3
		List<?> l = pjl.size(3);				//[_,_,_] is a list whose size is 3
		int cont = 0;
		for (Term<?> t : pjl.size()) { 
			//{[],[_],[_,_], ...} all lists whose size is less than 5
			System.out.println(t);
			if (cont++ == 5) break;
		}
	}
	/* OUPUT:
	Compound:'size'(List[],Int(0))
	Compound:'size'(List[Var(_)],Int(1))
	Compound:'size'(List[Var(_), Var(_)],Int(2))
	Compound:'size'(List[Var(_), Var(_), Var(_)],Int(3))
	Compound:'size'(List[Var(_), Var(_), Var(_), Var(_)],Int(4))
	Compound:'size'(List[Var(_), Var(_), Var(_), Var(_), Var(_)],Int(5))
	*/
}