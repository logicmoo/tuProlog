package alice.tuprolog.json.test;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;

//Muccioli - Sita
public class MultiTest {

	public static void main(String[] args) throws InvalidTheoryException, MalformedGoalException, NoSolutionException, UnknownVarException, NoMoreSolutionException {
		prova1();
		prova2();
		prova3();
		provaSita();
	}
	
	public static void prova1() {
		Prolog engine = new Prolog();
		String json = engine.toJSON();
		System.out.println(json);
		System.out.println("");
		Prolog engine2 = Prolog.fromJSON(json);
		System.out.println(engine2.toString());
	}
	
	public static void prova2() throws InvalidTheoryException, MalformedGoalException, NoSolutionException, UnknownVarException {
		Prolog engine = new Prolog();
		Theory t = new Theory(getTheory());
		engine.setTheory(t);
		String json = engine.toJSON();
		System.out.println(json);
		System.out.println("");
		Prolog engine2 = Prolog.fromJSON(json);
		SolveInfo info = engine2.solve("dExpr(x^5,Der).");
		System.out.println(info.getTerm("Der"));
	}
	
	public static void prova3() throws InvalidTheoryException, MalformedGoalException, NoSolutionException, UnknownVarException, NoMoreSolutionException {
		Prolog engine = new Prolog();
		Theory t = new Theory(getTheory());
		engine.setTheory(t);
		SolveInfo info = engine.solve("dExpr(x^5,Der).");
		System.out.println(info.toString());
		String json = engine.toJSON();
		System.out.println("Serializzato");
		System.out.println(json);
		System.out.println("");
		Prolog engine2 = Prolog.fromJSON(json);
		SolveInfo info2 = engine2.solve("dExpr(x^7,Der).");
		SolveInfo info23 = engine2.solve("is(5,5).");
		System.out.println(info2.toString());
		System.out.println(info23.toString());
	}
	
	public static void provaSita() throws MalformedGoalException, NoMoreSolutionException {
		Prolog prolog = new Prolog();
		String query = "member(A, [1,2,3,4,5,6,7,8,9]).";
		System.out.println("CLIENT: ?- "+query);
		System.out.println(prolog.solve(query).toString());
		System.out.println(prolog.solveNext().toString());
		String json = prolog.toJSON();
		System.out.println("Serializzato");
		Prolog prolog2 = Prolog.fromJSON(json);
		System.out.println(prolog2.solveNext().toString());
		System.out.println(prolog2.solveNext().toString());
	}
	
	private static String getTheory() {
		StringBuilder sb = new StringBuilder();
		sb.append("dExpr(T,DT) :- dTerm(T,DT).\n");
		sb.append("dExpr(E+T, [DE+DT]) :- dExpr(E,DE), dTerm(T,DT).\n");
		sb.append("dExpr(E-T, [DE-DT]) :- dExpr(E,DE), dTerm(T,DT).\n");
		sb.append("dTerm(F, DF) :- dFactor(F,DF).\n");
		sb.append("dTerm(T*F, [[DT*F]+[T*DF]]) :- dTerm(T, DT), dFactor(F, DF).\n");
		sb.append("dTerm(T/F, [[F*DT]-[T*DF]]) :- dTerm(T, DT), dFactor(F, DF).\n");
		sb.append("dFactor(x, 1).\n");
		sb.append("dFactor(N, 0) :- number(N).\n");
		sb.append("dFactor([E], DE) :- dExpr(E, DE).\n");
		sb.append("dFactor(-E, -DE) :- dExpr(E, DE).\n");
		sb.append("dFactor(sin(x), cos(x)) :- !.\n");
		sb.append("dFactor(cos(x), -sin(x)) :- !.\n");
		sb.append("dFactor(sin(E), [cos(E)*DE]) :- dExpr(E, DE).\n");
		sb.append("dFactor(cos(E), [-sin(E)*DE]) :- dExpr(E, DE).\n");
		sb.append("dFactor(x^N, [N*x^(N1)]) :- N1 is N-1, !.\n");
		sb.append("dFactor(E^N, [N*E^(Nm1)*(DE)]) :- dExpr(E, DE), Nm1 is N-1.\n");
		return sb.toString();
	}
}
