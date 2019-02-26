/*
 * SolveInfo.java
 *
 * Created on 13 marzo 2007, 12.00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.engine;

import alice.tuprologx.pj.model.*;
import alice.tuprolog.UnknownVarException;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Maurizio
 */
public class PrologSolution<Q extends TxTerm<?>, S extends TxTerm<?>> /*implements ISolution<Q,S,Term<?>>*/ {
    
    private alice.tuprolog.SolveInfo _solveInfo;
    
    /** Creates a new instance of SolveInfo */
    public PrologSolution(alice.tuprolog.SolveInfo si) {
        _solveInfo = si;
    }

    public <Z extends TxTerm<?>> Z getVarValue(String varName) throws alice.tuprolog.NoSolutionException {
        alice.tuprolog.Term retValue;        
        retValue = _solveInfo.getVarValue(varName);
        return TxTerm.<Z>unmarshal(retValue);
    }

    public <Z extends TxTerm<?>> Z getTerm(String varName) throws alice.tuprolog.NoSolutionException, UnknownVarException {
        alice.tuprolog.Term retValue;                
        retValue = _solveInfo.getTerm(varName);
        return TxTerm.<Z>unmarshal(retValue);
    }

    public boolean isSuccess() {        
        return _solveInfo.isSuccess();
    }

    public boolean isHalted() {        
        return _solveInfo.isHalted();
    }

    public boolean hasOpenAlternatives() {        
        return _solveInfo.hasOpenAlternatives();
    }

    public S getSolution() throws alice.tuprolog.NoSolutionException {
        alice.tuprolog.Term retValue;        
        retValue = _solveInfo.getSolution();
        return TxTerm.<S>unmarshal(retValue);
    }

    public Q getQuery() {
        alice.tuprolog.Term retValue;        
        retValue = _solveInfo.getQuery();
        return TxTerm.<Q>unmarshal(retValue);
    }

    public List<TxTerm<?>> getBindingVars() throws alice.tuprolog.NoSolutionException {
        List<alice.tuprolog.TuVar> retValue;        
        retValue = _solveInfo.getBindingVars();
        Vector<TxTerm<?>> bindings = new Vector<TxTerm<?>>();
        for (alice.tuprolog.Term t : retValue) {
            bindings.add(TxTerm.unmarshal(t));
        }
        return bindings;
    }
}
