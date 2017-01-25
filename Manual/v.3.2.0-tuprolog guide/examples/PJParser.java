import alice.tuprologx.pj.annotations.*;
import alice.tuprologx.pj.engine.*;
import alice.tuprologx.pj.model.*;
import alice.tuprologx.pj.meta.*;

@PrologClass
public abstract class PJParser {
	@PrologMethod (clauses={"expr(L,R):-term(L,R).",
							"expr(L,R):-term(L,['+'|R2]), expr(R2,R).",
							"expr(L,R):-term(L,['-'|R2]), expr(R2,R)."})
	public abstract <$E extends List<?>, $R extends List<?>> Boolean expr($E expr, $R rest);

	@PrologMethod (clauses={"term(L,R):-fact(L,R).",
							"term(L,R):-fact(L,['*'|R2]), term(R2,R).",
							"term(L,R):-fact(L,['/'|R2]), term(R2,R)."})
	public abstract <$T extends List<?>, $R extends List<?>> Boolean term($T term, $R rest);
	
	@PrologMethod (clauses={"fact(L,R):-num(L,R).",
							"fact(['(' | E],R):-expr(E,[')'|R])."})
	public abstract <$F extends List<?>, $R extends List<?>> Boolean fact($F fact, $R rest);
	
	@PrologMethod (clauses={"num([L|R],R):-num_atom(_,L)."})
	public abstract <$N extends List<?>, $R extends List<?>> Boolean num($N num, $R rest);
	
	public static void main(String[] args) throws Exception {
		PJParser ep = PJ.newInstance(PJParser.class);
		String tokenizer_regexp = "(?<!^)(\\b|(?=\\()|(?=\\))|(?=\\-)|(?=\\+)|(?=\\/)|(?=\\*))";
		List<Atom> exp1 = new Atom("12+(3-4)").split(tokenizer_regexp);
		List<Atom> exp2 = new Atom("(12+(3-4))").split(tokenizer_regexp);
		System.out.println(ep.expr(exp1, List.NIL)); //true, 12+(3*4) is an expression
		System.out.println(ep.fact(exp1, List.NIL)); //false, 12+(3*4) isn’t a factor
		System.out.println(ep.expr(exp2, List.NIL)); //true, (12+(3*4)) is an expression
		System.out.println(ep.fact(exp2, List.NIL)); //true, (12+(3*4)) is a factor
	}
}
