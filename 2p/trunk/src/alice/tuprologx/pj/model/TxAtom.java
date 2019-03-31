/*
 * Atom.java
 *
 * Created on March 8, 2007, 5:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.model;

import alice.tuprolog.TuFactory;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

/**
 *
 * @author maurizio
 */
public class TxAtom extends TxTerm<TxAtom> {
    String _theAtom;

    public TxAtom(String s) {
        _theAtom = s;
    }

    @Override
    public <Z> Z toJava() {
        //return (Z)_theAtom;
        return uncheckedCast(_theAtom);
    }

    @Override
    public String toString() {
        return "Atom(" + _theAtom + ")";
    }

    @Override
    public alice.tuprolog.Term marshal() {
        return createTuAtom(_theAtom);
    }

    static TxAtom unmarshal(alice.tuprolog.TuStruct a) {
        if (!matches(a))
            throw new UnsupportedOperationException();
        return new TxAtom(a.fname());
    }

    static boolean matches(alice.tuprolog.Term t) {
        return (!(t instanceof alice.tuprolog.TuVar) && t.isAtomSymbol() && !t.isPlList() && !TxBool.matches(t));
    }

    public TxList<TxAtom> toCharList() {
        char[] carr = _theAtom.toCharArray();
        java.util.Vector<String> vs = new java.util.Vector<String>();
        for (char c : carr) {
            vs.add(c + "");
        }
        return new TxList<TxAtom>(vs);
    }

    public TxList<TxAtom> split(String regexp) {
        java.util.Vector<String> vs = new java.util.Vector<String>();
        for (String s : _theAtom.split(regexp)) {
            vs.add(s);
        }
        return new TxList<TxAtom>(vs);
    }
}