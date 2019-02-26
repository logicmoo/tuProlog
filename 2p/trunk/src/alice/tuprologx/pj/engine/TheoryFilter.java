package alice.tuprologx.pj.engine;

import alice.tuprologx.pj.model.*;

import java.util.Vector;

/**
 *
 * @author maurizio
 */
public class TheoryFilter {
    
	protected TxTheory _theory;
    protected TxTheory _filter;
    protected PJProlog _engine;
    
    private static String base_filter_string = "filter(L,R):-filter(L,[],R).\n"+
                                 "filter([],X,X).\n"+
                                 "filter([H|T],F,R):-call(H),append(F,[H],Z),filter(T,Z,R).\n"+
                                 "filter([H|T],F,R):-not call(H),filter(T,F,R).\n";
    
    private static TxTheory base_filter = new TxTheory(base_filter_string);
    
    
    /** Creates a new instance of TheoryFilter */
    public TheoryFilter(TxTheory theory, TxTheory filter) {
        _theory = theory;
        _filter = filter;
    }
    
    public TheoryFilter(TxTheory theory, String filter) {
        this(theory,new TxTheory(filter));
    }
    
    @SuppressWarnings("unchecked")
	public TxTheory apply() {                
        TxVar<TxList<TxClause<?,?>>> filtered_list = new TxVar<TxList<TxClause<?,?>>>("X");
        TxCompound2<TxList<TxClause<?,?>>,TxVar<TxList<TxClause<?,?>>>> goal = new TxCompound2<TxList<TxClause<?,?>>,TxVar<TxList<TxClause<?,?>>>>("filter",_theory,filtered_list);
        try {
            PJProlog p = new PJProlog();        
            p.setTheory(_filter);
            p.addTheory(base_filter);
            //System.out.println(p.getTheory());
            //p.setTheory(p.getTheory());
            //System.out.println(goal.marshal());
            PrologSolution<?,?> sol = p.solve(goal);
            TxList<TxTerm<?>> res = sol.getTerm("X");            
            //System.out.println("PIPPO="+res);            
            Vector<TxClause<?,?>> filtered_clauses = new Vector<TxClause<?,?>>();
            for (TxTerm<?> t : res) {
                if (t instanceof TxCompound2 && ((TxCompound2<TxTerm<?>,TxTerm<?>>)t).getName().equals(":-")) {
                    filtered_clauses.add(new TxClause<TxTerm<?>,TxTerm<?>>(((TxCompound2<TxTerm<?>,TxTerm<?>>)t).get0(),((TxCompound2<TxTerm<?>,TxTerm<?>>)t).get1()));
                }
                else {
                    filtered_clauses.add(new TxClause<TxTerm<?>,TxTerm<?>>(t,null));
                }
            }
            return new TxTheory(filtered_clauses);
        }
        catch (Exception e) {
            e.printStackTrace();
            //Var<Var<Int>> vvi = null;
            //Term<Var<Int>> ti3 = null;
            //Term<? extends Term<Int>> ti = null;
            //Int i = null;
            //ti = i;
            //ti = vvi;
            return _theory;
        }
    }    
}
