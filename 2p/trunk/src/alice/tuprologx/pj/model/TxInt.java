/*
 * Int.java
 *
 * Created on March 8, 2007, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.model;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
import alice.tuprolog.TuFactory;

/**
 *
 * @author maurizio
 */
public class TxInt extends TxTerm<TxInt> {
	Integer _theInt;

	@Override
	public <Z> Z/*Integer*/ toJava() {
		//return (Z)_theInt;
		return uncheckedCast(_theInt);
	}
	
	public TxInt (Integer i) {_theInt = i;}
        
        @Override
		public alice.tuprolog.TuInt marshal() {
            return createTuInt(_theInt);
        }
        
        static TxInt unmarshal(alice.tuprolog.TuInt i) {
            if (!matches(i))
                throw new UnsupportedOperationException();
            return new TxInt(i.intValue());
        }
        
        static boolean matches(alice.tuprolog.Term t) {
            return (t instanceof alice.tuprolog.TuInt);
        }
        
	@Override
	public String toString() {
		return "Int("+_theInt+")";
	}

}