/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tuprolog;

import java.util.*;

import alice.tuprolog.InvalidTermException;
import alice.tuprolog.TuTermVisitor;

/**
 * This class represents a variable term.
 * Variables are identified by a name (which must starts with
 * an upper case letter) or the anonymous ('_') name.
 *
 * @see Term
 *
 */
public class TuVar extends TuTerm {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private String type = "Var";

    /* Identify kind of renaming */
    final static int ORIGINAL = -1;
    final static int PROGRESSIVE = -2;

    private static long fingerprint = 0; //Alberto //static version as global counter

    final static String ANY = "_";

    // the name identifying the var
    private String name;
    private StringBuilder completeName; /* Reviewed by Paolo Contessi */
    private Term link; /* link is used for unification process */
    private long internalTimestamp; /* internalTimestamp is used for fix vars order (resolveTerm()) */
    private int ctxid; /* id of ExecCtx owners of this var util for renaming*/

    //Alberto
    private long fingerPrint; //fingerPrint is a unique id (per run) used for var comparison

    //Alberto
    static long getFingerprint() { //called by Var constructors
        fingerprint++;
        return fingerprint;
    }

    /**
     * Creates a variable identified by a name.
     *
     * The name must starts with an upper case letter or the underscore. If an underscore is
     * specified as a name, the variable is anonymous.
     *
     * @param n is the name
     * @throws InvalidTermException if n is not a valid Prolog variable name
     */
    public TuVar(String n) {
        link = null;
        ctxid = TuVar.ORIGINAL; //no execCtx owners
        internalTimestamp = 0;
        fingerPrint = getFingerprint();
        if (n.equals(ANY)) {
            name = null;
            completeName = new StringBuilder();
        } else if (Character.isUpperCase(n.charAt(0)) || (n.startsWith(ANY))) {
            name = n;
            completeName = new StringBuilder(n);
        } else {
            throw new InvalidTermException("Illegal variable name: " + n);
        }
    }

    /**
     * Creates an anonymous variable
     *
     *  This is equivalent to build a variable with name _
     */
    public TuVar() {
        name = null;
        completeName = new StringBuilder();
        link = null;
        ctxid = TuVar.ORIGINAL;
        internalTimestamp = 0;
        fingerPrint = getFingerprint();
    }

    /**
     * Creates a internal engine variable.
     *
     * @param n is the name
     * @param id is the id of ExecCtx
     * @param alias code to discriminate external vars
     * @param isCyclic 
     * @param time is timestamp
     */
    private TuVar(String n, int id, int alias, long count/*, boolean isCyclic*/) {
        name = n;
        completeName = new StringBuilder();
        internalTimestamp = count;
        //this.isCyclic = isCyclic;
        fingerPrint = getFingerprint();
        link = null;
        if (id < 0)
            id = TuVar.ORIGINAL;
        rename(id, alias);
    }

    /**
     * Rename variable (assign completeName) 
     */
    void rename(int idExecCtx, int count) { /* Reviewed by Paolo Contessi */
        ctxid = idExecCtx;

        if (ctxid > TuVar.ORIGINAL) {
            completeName = completeName.delete(0, completeName.length()).append(name).append("_e").append(ctxid);
        }

        else if (ctxid == ORIGINAL) {
            completeName = completeName.delete(0, completeName.length()).append(name);
        }

        else if (ctxid == PROGRESSIVE) {
            completeName = completeName.delete(0, completeName.length()).append("_").append(count);
        }
    }

    /**
     * Gets a copy of this variable.
     *
     * if the variable is not present in the list passed as argument,
     * a copy of this variable is returned and added to the list. If instead
     * a variable with the same time identifier is found in the list,
     * then the variable in the list is returned.
     */
    @Override
    public Term copy(int idExecCtx, AbstractMap<TuVar, TuVar> vMap) {
        Term tt = dref();
        if (tt == this) {
            TuVar v = (vMap.get(this));
            if (v == null) {
                //No occurence of v before
                v = new TuVar(name, idExecCtx, 0, internalTimestamp/*, this.isCyclic*/);
                vMap.put(this, v);
            }
            return v;
        } else {
            return tt.copy(idExecCtx, vMap);
        }
    }

    @Override //Alberto 
    public Term copyAndRetainFreeVar(AbstractMap<TuVar, TuVar> vMap, int idExecCtx) {
        Term tt = dref();
        if (tt == this) {
            TuVar v = (vMap.get(this));
            if (v == null) {
                //No occurence of v before
                v = this; //!!!
                vMap.put(this, v);
            }
            return v;
        } else {
            return tt.copy(idExecCtx, vMap);
        }
    }

    /**
     * Gets a copy of this variable.
     */
    @Override
    public Term copy(AbstractMap<TuVar, TuVar> vMap, AbstractMap<Term, TuVar> substMap) {
        TuVar v;
        Object temp = vMap.get(this);
        if (temp == null) {
            v = new TuVar(null, TuVar.PROGRESSIVE, vMap.size(), internalTimestamp/*, this.isCyclic*/);
            vMap.put(this, v);
        } else {
            v = (TuVar) temp;
        }

        //if(v.isCyclic) //Alberto
        //	return v;

        Term t = dref();
        if (t .isVar()) {
            Object tt = substMap.get(t);
            if (tt == null) {
                substMap.put(t, v);
                v.link = null;
            } else {
                v.link = (tt != v) ? (TuVar) tt : null;
            }
        }
        if (t .isTuStruct()) {
            v.link = t.copy(vMap, substMap);
        }
        if (t .isNumber())
            v.link = t;
        return v;
    }

    /**
     * De-unify the variable
     */
    @Override
    public void free() {
        link = null;
    }

    /**
     * De-unify the variables of list
     */
    public static void free(List<TuVar> varsUnified) {
        for (TuVar v : varsUnified) {
            v.free();
        }
    }

    /**
     * Gets the name of the variable
     */
    public String getName() {
        if (name != null) {
            return completeName.toString();
        } else {
            return ANY;
        }
    }

    /**
     * Gets the name of the variable
     */
    public String getOriginalName() {
        if (name != null) {
            return name;
        } else {
            return ANY + "" + this.fingerPrint; //Alberto
        }
    }

    /**
     *  Gets the term which is referred by the variable.
     *
     *  For unbound variable it is the variable itself, while
     *  for bound variable it is the bound term.
     */
    @Override
    public Term dref() {
        Term tt = this;
        Term t = link;
        while (t != null) {
            tt = t;
            if (t .isVar()) {
                t = ((TuVar) t).link;
            } else {
                break;
            }
        }
        return tt;
    }

    /**
     * Gets the term which is direct referred by the variable.
     */
    public Term getLink() {
        return link;
    }

    /**
     * Set the term which is direct bound
     */
    void setLink(Term l) {
        link = l;
    }

    /**
     * Set the timestamp
     */
    void setInternalTimestamp(long t) {
        internalTimestamp = t;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isTuStruct() {
        return false;
    }

    @Override
    public boolean isVar() {
        return true;
    }

    @Override
    public boolean isEmptyList() {
        Term t = dref();
        if (t == this) {
            return false;
        } else {
            return t.isEmptyList();
        }
    }

    @Override
    public boolean isAtomic() {
        Term t = dref();
        if (t == this) {
            return false;
        } else {
            return t.isAtomic();
        }
    }

    @Override
    public boolean isCompound() {
        Term t = dref();
        if (t == this) {
            return false;
        } else {
            return t.isCompound();
        }
    }

    @Override
    public boolean isAtomSymbol() {
        Term t = dref();
        if (t == this) {
            return false;
        } else {
            return t.isAtomSymbol();
        }
    }

    @Override
    public boolean isConsList() {
        Term t = dref();
        if (t == this)
            return false;
        else
            return t.isConsList();
    }

    @Override
    public boolean isGround() {
        Term t = dref();
        if (t == this) {
            return false;
        } else {
            return t.isGround();
        }
    }

    /**
     * Tests if this variable is ANY
     */
    public boolean isAnonymous() {
        return name == null;
    }

    /**
     * Tests if this variable is bound
     *
     */
    public boolean isBound() {
        return link != null;
    }

    /**
     * finds var occurence in a Struct, doing occur-check.
     * (era una findIn)
     * @param vl TODO
     * @param choice 
     */
    private boolean occurCheck(List<TuVar> vl, TuStruct t) {
        int arity = t.getArity();
        for (int c = 0; c < arity; c++) {
            Term at = t.getTerm(c);
            if (at .isTuStruct()) {
                if (occurCheck(vl, (TuStruct) at)) {
                    return true;
                }
            } else if (at .isVar()) {
                TuVar v = (TuVar) at;
                if (v.link == null) {
                    vl.add(v);
                }
                if (this == v) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Resolve the occurence of variables in a Term
     */
    @Override
    public long resolveTerm(long count) {
        Term tt = dref();
        if (tt != this) {
            return tt.resolveTerm(count);
        } else {
            internalTimestamp = count;
            return count++;
        }
    }

    /**
     * var unification.
     * <p>
     * First, verify the Term eventually already unified with the same Var
     * if the Term exist, unify var with that term, in order to handle situation
     * as (A = p(X) , A = p(1)) which must produce X/1.
     * <p>
     * If instead the var is not already unified, then:
     * <p>
     * if the Term is a var bound to X, then try unification with X
     * so for example if A=1, B=A then B is unified to 1 and not to A
     * (note that it's coherent with chronological backtracking:
     * the eventually backtracked A unification is always after
     * backtracking of B unification.
     * <p>
     * if are the same Var, unification must succeed, but without any new
     * bindings (to avoid cycles for extends in A = B, B = A)
     * <p>
     * if the term is a number, then it's a success and new link is created
     * (retractable by means of a code)
     * <p>
     * if the term is a compound, then occur check test is executed:
     * the var must not appear in the compound ( avoid X=p(X),
     * or p(X,X)=p(Y,f(Y)) ); if occur check is ok
     * then it's success and a new link is created (retractable by a code)
     * (test done if occursCheck is enabled)
     */
    @Override
    public boolean unify(List<TuVar> vl1, List<TuVar> vl2, Term t, boolean isOccursCheckEnabled) {
        Term tt = dref();
        if (tt == this) {
            t = t.dref();
            if (t .isVar()) {
                ((TuVar) t).fingerPrint = this.fingerPrint; //Alberto
                if (this == t) {
                    try {
                        vl1.add(this);
                    } catch (NullPointerException e) {
                    }
                    return true;
                }
            } else if (t .isTuStruct()) {
                if (isOccursCheckEnabled) {
                    if (occurCheck(vl2, (TuStruct) t)) {
                        //this.isCyclic = true;  //Alberto -> da usare quando si supporteranno i termini ciclici
                        return false; // da togliere 
                    }
                } else {
                    checkVar(vl2, t); //Alberto
                }
            } else if (!(t .isNumber())) {
                return false;
            }
            link = t;
            try {
                vl1.add(this);
            } catch (NullPointerException e) {
            }
            return true;
        } else {
            return (tt.unify(vl1, vl2, t, isOccursCheckEnabled));
        }
    }

    //Alberto
    private void checkVar(List<TuVar> vl, Term t) {
        TuStruct st = (TuStruct) t;
        int arity = st.getArity();
        for (int c = 0; c < arity; c++) {
            Term at = st.getTerm(c);
            if (at .isVar()) {
                TuVar v = (TuVar) at;
                if (v.link == null) {
                    vl.add(v);
                }
            } else if (at .isTuStruct()) {
                checkVar(vl, at);
            }
        }
    }

    @Override
    public boolean isGreater(Term t) {
        Term tt = dref();
        if (tt == this) {
            t = t.dref();
            if (!(t .isVar()))
                return false;
            return fingerPrint > ((TuVar) t).fingerPrint; //Alberto
        } else {
            return tt.isGreater(t);
        }
    }

    public void setName(String s) {
        this.name = s;
    }

    /**
     * Gets the string representation of this variable.
     *
     * For bounded variables, the string is <Var Name>/<bound Term>.
     */
    @Override
    public String toString() {
        Term tt = dref();
        if (name != null) {
            if (tt == this/* || this.isCyclic*/) {
                //if(this.isCyclic) //Alberto
                // return name;
                return completeName.toString();
            } else {
                return (completeName.toString() + " / " + tt.toString());
            }
        } else {
            if (tt == this /*|| this.isCyclic*/) {
                return ANY + "" + this.fingerPrint; //Alberto
            } else {
                return tt.toString();
            }
        }
    }

    /**
     * Gets the string representation of this variable, providing
     * the string representation of the linked term in the case of
     * bound variable
     *
     */
    public String toStringFlattened() {
        Term tt = dref();
        if (name != null) {
            if (tt == this /*|| this.isCyclic*/) {
                //if(this.isCyclic)
                // return name;
                return completeName.toString();
            } else {
                return tt.toString();
            }
        } else {
            if (tt == this /*|| this.isCyclic*/) {
                return ANY + "" + this.fingerPrint; //Alberto
            } else {
                return tt.toString();
            }
        }
    }

    /*Castagna 06/2011*/
    @Override
    public void accept(TuTermVisitor tv) {
        tv.visit(this);
    }

    @Override
    public boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t) {
        return unify(varsUnifiedArg1, varsUnifiedArg2, t, true);
    }
}