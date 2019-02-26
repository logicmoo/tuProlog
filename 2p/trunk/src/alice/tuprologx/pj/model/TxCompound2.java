package alice.tuprologx.pj.model;

import java.util.*;

/**
 *
 * @author maurizio
 */
public class TxCompound2<X1 extends TxTerm<?>,X2 extends TxTerm<?>> extends TxCons<X1,TxCons<X2,TxNil>> {    
    public TxCompound2(String name, X1 x1, X2 x2) {
        super(name,new Vector<TxTerm<?>>(Arrays.asList(new TxTerm<?>[]{x1,x2})));
    }
    
    public X1 get0() {return getHead();}
    
    public X2 get1() {return getRest().getHead();}
}
