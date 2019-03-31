/*
 * Theory.java
 *
 * Created on April 4, 2007, 10:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.model;

import java.util.Collection;
import java.util.Vector;

/**
 *
 * @author maurizio
 */
public class TxTheory extends TxList<TxClause<?, ?>> {

    private static alice.tuprolog.TuProlog engine;

    static {
        engine = new alice.tuprolog.TuProlog();
        try {
            engine.unloadLibrary("alice.tuprolog.lib.OOLibrary");
            engine.loadLibrary("alice.tuprologx.pj.lib.PJLibrary");
            engine.loadLibrary("alice.tuprolog.lib.DCGLibrary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*  
        static {
            try {
                alice.tuprolog.Prolog p = new alice.tuprolog.Prolog();
                java.lang.reflect.Method m = p.getClass().getDeclaredMethod("getTheoryManager");
                m.setAccessible(true);             
                _tm = (alice.tuprolog.TheoryManager)m.invoke(p);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    */
    /** Creates a new instance of Theory */
    public TxTheory(Collection<TxClause<?, ?>> clauses) {
        super(clauses);
    }

    public TxTheory(String s) {
        this(parseTheory(s));
        //System.out.println(this);     
    }

    public TxTheory(String[] s) {
        this(parseTheoryArray(s));
    }

    public static TxTheory unmarshal(alice.tuprolog.TuTheory t) {
        Vector<TxClause<?, ?>> clauses = new Vector<TxClause<?, ?>>();
        for (java.util.Iterator<? extends alice.tuprolog.Term> it = t.iterator(engine); it.hasNext();) {
            alice.tuprolog.TuStruct st = (alice.tuprolog.TuStruct) it.next();
            //Clause<?,?> clause = new Clause(Term.unmarshal(st.getArg(0)),Term.unmarshal(st.getArg(1)));
            TxClause<?, ?> clause = new TxClause<TxTerm<?>, TxTerm<?>>(st);
            clauses.add(clause);
        }
        return new TxTheory(clauses);
    }

    //    public static Theory unmarshal2(alice.tuprolog.Theory t) {
    //        alice.tuprolog.Prolog p = new alice.tuprolog.Prolog(); 
    //        try {
    //            p.setTheory(t);        
    //        }
    //        catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return new Theory(p.getTheory().toString());
    //        //return new Theory(t.toString());
    //    }
    //    
    //    public static Theory unmarshal3(alice.tuprolog.Theory t) {
    //        Vector<Clause<?,?>> clauses = new Vector<Clause<?,?>>();
    //        try {
    //            _tm.clear(true);
    //            _tm.consult(t,false,true,null);        
    //            //System.out.println(tm.getDynamicClauseList());
    //        }
    //        catch (Exception e) {
    //            throw new UnsupportedOperationException(e);
    //        }
    //        for (Object o : _tm.getDynamicClauseList()) {
    //            alice.tuprolog.Struct st = (alice.tuprolog.Struct)o;
    //            //Clause<?,?> clause = !st.getArg(1).equals(alice.tuprolog.Struct.TRUE) ? new Clause(Term.unmarshal(st.getArg(0)),Term.unmarshal(st.getArg(1))) : new Clause(Term.unmarshal(st.getArg(0)),null);
    //            Clause<?,?> clause = new Clause(Term.unmarshal(st.getArg(0)),Term.unmarshal(st.getArg(1)));
    //            clauses.add(clause);            
    //        }        
    //        return new Theory(clauses);
    //    }

    /* This method should be removed or deprecated when (hepefully) one day tuProlog
     * will expose the clause list view over a Prolog theory. Currently this method has to deal with all
     * the strange formattings that are carried out by the tuProlog's TheoryManager!!
     */
    //    private static Collection<Clause<?,?>> parseTheory2(String s) {         
    //        Vector<Clause<?,?>> clauses = new Vector<Clause<?,?>>();
    //        int endIdx = s.indexOf(".\n"); 
    //        String rest = s;
    //        while (endIdx!=-1) {            
    //            String substring = rest.substring(0,endIdx/*+1*/);
    //            if (!rest.equals(substring)) {
    //                rest = rest.substring(substring.length()+1,rest.length());                
    //            }
    //            else {
    //                rest ="";            
    //            }
    //            if (!substring.equals("")) {
    //                clauses.add(new Clause(substring));
    //            }            
    //            endIdx = rest.indexOf(".\n");
    //        }        
    //        return clauses;
    //    }
    //    
    //    private static Collection<Clause<?,?>> parseTheory3(String s) {         
    //        Vector<Clause<?,?>> clauses = new Vector<Clause<?,?>>();
    //        try {
    //            _tm.clear(true);
    //            _tm.consult(new alice.tuprolog.Theory(s),false,true,null);        
    //            //System.out.println(tm.getDynamicClauseList());
    //        }
    //        catch (Exception e) {
    //            throw new UnsupportedOperationException(e);
    //        }
    //        for (Object o : _tm.getDynamicClauseList()) {
    //            alice.tuprolog.Struct st = (alice.tuprolog.Struct)o;            
    //            //Clause<?,?> clause = !st.getArg(1).equals(alice.tuprolog.Struct.TRUE) ? new Clause(Term.unmarshal(st.getArg(0)),Term.unmarshal(st.getArg(1))) : new Clause(Term.unmarshal(st.getArg(0)),null);
    //            Clause<?,?> clause = new Clause(Term.unmarshal(st.getArg(0)),Term.unmarshal(st.getArg(1)));
    //            clauses.add(clause);            
    //        }        
    //        return clauses;
    //    }

    private static Collection<TxClause<?, ?>> parseTheory(String s) {
        Vector<TxClause<?, ?>> clauses = new Vector<TxClause<?, ?>>();
        alice.tuprolog.TuTheory t = null;
        try {
            t = new alice.tuprolog.TuTheory(s);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        for (java.util.Iterator<? extends alice.tuprolog.Term> it = t.iterator(engine); it.hasNext();) {
            alice.tuprolog.TuStruct st = (alice.tuprolog.TuStruct) it.next();
            //Clause<?,?> clause = new Clause(Term.unmarshal(st.getArg(0)),Term.unmarshal(st.getArg(1)));
            TxClause<?, ?> clause = new TxClause<TxTerm<?>, TxTerm<?>>(st);
            clauses.add(clause);
        }
        return clauses;
    }

    private static Collection<TxClause<?, ?>> parseTheoryArray(String[] arr) {
        String temp = "";
        for (String s : arr) {
            temp += s + "\n";
        }
        return parseTheory(temp);
    }

    public TxClause<?, ?>[] find(String name, int arity) {
        Vector<TxClause<?, ?>> temp = new Vector<TxClause<?, ?>>();
        for (TxClause<?, ?> c : this) {
            if (c.match(name, arity))
                temp.add(c);
        }
        return temp.toArray(new TxClause<?, ?>[temp.size()]);
    }

    @Override
    public alice.tuprolog.Term marshal() {
        alice.tuprolog.Term s = super.marshal();
        java.util.Iterator<? extends alice.tuprolog.Term> listIterator = s.listIteratorProlog();
        while (listIterator.hasNext()) {
            ((alice.tuprolog.Term) listIterator.next()).resolveTerm();
        }
        return s;
    }

    public void appendTheory(TxTheory that) {
        for (TxClause<?, ?> c : that) {
            _theList.add(c);
        }
    }
}
