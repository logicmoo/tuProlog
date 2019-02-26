package alice.tuprologx.pj.model;

import java.util.*;

/**
 *
 * @author maurizio
 */
public class TxCompound1<X1 extends TxTerm<?>> extends TxCons<X1,TxNil> {    
    public TxCompound1(String name, X1 x1) {
        super(name,new Vector<TxTerm<?>>(Arrays.asList(new TxTerm<?>[]{x1})));
    }
    
    public X1 get0() {return getHead();}
}


