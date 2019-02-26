/*
 * Clause.java
 *
 * Created on April 4, 2007, 9:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.model;

import alice.tuprolog.TuStruct;

/**
 *
 * @author maurizio
 */
public class TxClause<H extends TxTerm<?>, B extends TxTerm<?>> extends TxCompound2<H,B> {
    
    private boolean isFact;
    
    /** Creates a new instance of Clause */
    @SuppressWarnings("unchecked")
	public TxClause(H head, B body) {
        super(":-", head, body == null ? (B)new TxBool(true) : body);        
        isFact = (body == null || body instanceof TxBool);
    }
    
    @SuppressWarnings("unchecked")
	public TxClause(TuStruct s) { 
        this(s.getName().equals(":-") ? (H)TxTerm.unmarshal(s.getArg(0)) : (H)TxTerm.unmarshal(s), s.getName().equals(":-") ? (B)TxTerm.unmarshal(s.getArg(1)) : null);        
    }
    /*
    public Clause(String s) {
        this((H)parseClause(s).get0(), (B)parseClause(s).get1());
    }
    
    private static Compound2<?,?> parseClause(String s) {
        Parser p = new Parser(s);               
        if (p.readTerm(false) != Parser.EOF) {
            if (p.getCurrentTerm()!=null && p.getCurrentTermType()== Parser.TERM && p.getCurrentTerm().isStruct()) {
                Cons c = Term.unmarshal(p.getCurrentTerm());
                if (!c.getName().equals(":-")) {                                      
                    c = new Compound2(null,c,null);
                }
                return (Compound2<?,?>)c;
            }
        }
        return null;
    }
    */
    public B getBody() {
        return get1();
    }
    
    public boolean isFact() {
        return isFact;
    }
    
    @Override
	public String toString() {
        return "Clause{"+getHead()+(isFact() ? "" : " :- "+getBody())+"}";
    }

    @Override
	public TuStruct marshal() {
        if (!isFact()) {
            return super.marshal();
        }
        else {
            return (TuStruct)getHead().marshal();
        }
    }
    
    public boolean match(String name, int arity) {
        if (getHead() instanceof TxCompound<?>) {
            return ((TxCompound<?>)getHead()).getName().equals(name) && arity == ((TxCompound<?>)getHead()).arity();
        }
        return false;
    }
}
