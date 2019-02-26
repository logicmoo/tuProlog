package alice.tuprologx.pj.model;

/**
 * @author  Maurizio
 */
public abstract class TxCompound<X extends TxCompound<?>> extends TxTerm<X> {
//public abstract class Compound<X extends Term<?>> extends Term<Compound<X>> {
    public abstract int arity();
    
    public abstract String getName();
}
