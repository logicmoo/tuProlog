/*
 * Var.java
 *
 * Created on March 8, 2007, 5:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.model;

import alice.tuprolog.TuTerm;

/**
 *
 * @author maurizio
 */
public class TxVar<X extends TxTerm<?>> extends TxTerm<X> {
		X _theValue;
        String _theName;
        private static java.lang.reflect.Method _setLink = null;
        
        static {
            try {
                _setLink = alice.tuprolog.TuVar.class.getDeclaredMethod("setLink", alice.tuprolog.Term.class);
                _setLink.setAccessible(true);                
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

	public TxVar(String name) {_theName = name;}
        
    //private Var(String name, Term<?> val) {_theName = name; _theValue= (X) val;}
    private TxVar(String name, TxTerm<?> val) {
    	_theName = name; _theValue = uncheckedCast(val);
    }
    		
	@Override
	public <Z> Z toJava() {
		// return _theValue != null ? (Z)_theValue.toJava() : (Z)this;
		return uncheckedCast( _theValue != null ? _theValue.toJava() : this );
	}
        
    public X getValue() {return _theValue;}
    
    @Override
	public alice.tuprolog.TuVar marshal() {
        try {
            alice.tuprolog.TuVar v= TuTerm.createTuVar(_theName);                 
            if (_theValue != null) {
                setLink(v, _theValue.marshal());
            }
            return v;
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e);
            //return null;
        }
    }
    
    static TxTerm<?> unmarshal(alice.tuprolog.TuVar a) {
        if (!matches(a))
            throw new UnsupportedOperationException();
        //return new Var<Term<?>>(a.getName(),a.isBound() ? Term.unmarshal(a.getTerm()) : null);            
        return a.isBound() ? TxTerm.unmarshal(a.getTerm()) : new TxVar<TxTerm<?>>(a.getName(), null);
    }
    
    static boolean matches(alice.tuprolog.Term t) {
        return (t .isVar());
    }
    
    public String getName() {
        return _theName;
    }
    
    @Override
	public String toString() {
		return "Var("+_theName+(_theValue != null ? "/"+_theValue : "")+")";
	}
        
    private static void setLink(alice.tuprolog.TuVar v, Object o) {
        try {                
            _setLink.invoke(v,o);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}