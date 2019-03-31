/*
 * Double.java
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
public class TxDouble extends TxTerm<TxDouble> {
    java.lang.Double _theDouble;

    @Override
    public <Z> Z/*java.lang.Double*/ toJava() {
        //return (Z)_theDouble;
        return uncheckedCast(_theDouble);
    }

    public TxDouble(java.lang.Double d) {
        _theDouble = d;
    }

    @Override
    public alice.tuprolog.TuDouble marshal() {
        return createTuDouble(_theDouble);
    }

    static TxDouble unmarshal(alice.tuprolog.TuDouble d) {
        if (!matches(d))
            throw new UnsupportedOperationException();
        return new TxDouble(d.doubleValue());
    }

    static boolean matches(alice.tuprolog.Term t) {
        return (t instanceof alice.tuprolog.TuDouble);
    }

    @Override
    public String toString() {
        return "Double(" + _theDouble + ")";
    }

}