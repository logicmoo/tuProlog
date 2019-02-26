package alice.tuprologx.pj.model;

import java.util.*;

/**
 *
 * @author maurizio
 */
public class TxCompound3<X1 extends TxTerm<?>,X2 extends TxTerm<?>,X3 extends TxTerm<?>> extends TxCons<X1,TxCons<X2,TxCons<X3,TxNil>>> {    
    public TxCompound3(String name, X1 x1, X2 x2, X3 x3) {
        super(name,new Vector<TxTerm<?>>(Arrays.asList(new TxTerm<?>[]{x1,x2,x3})));
    }
    
    public X1 get0() {return getHead();}
    
    public X2 get1() {return getRest().getHead();}
    
    public X3 get2() {return getRest().getRest().getHead();}
}
