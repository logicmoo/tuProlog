package alice.tuprologx.pj.model;

public class TxNil extends TxCompound<TxNil> {
    @Override
	public int arity() {return 0;}
       
        
    @Override
	public <Z> Z/*Object*/ toJava() {
        throw new UnsupportedOperationException();
    }

    @Override
	public alice.tuprolog.Term marshal() {
        throw new UnsupportedOperationException();
    }

    @Override
	public String getName() {
        return null;
    }
}

