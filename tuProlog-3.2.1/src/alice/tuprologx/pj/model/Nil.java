package alice.tuprologx.pj.model;

public class Nil extends Compound<Nil> {
    public int arity() {return 0;}
       
        
    public <Z> Z/*Object*/ toJava() {
        throw new UnsupportedOperationException();
    }

    public alice.tuprolog.Term marshal() {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return null;
    }
}

