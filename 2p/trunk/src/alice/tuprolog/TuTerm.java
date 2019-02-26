/*
 * tuProlog - Copyright (C) 2001-2007  aliCE team at deis.unibo.it
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

import java.io.Serializable;
import java.util.AbstractMap;
//import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;

import alice.tuprolog.InvalidTermException;
import alice.tuprolog.TermVisitor;
import alice.tuprolog.json.JSONSerializerManager;
import alice.util.OneWayList;
import nu.xom.xslt.XSLException;

/**
 * Term class is the root abstract class for prolog data type
 * @see TuStruct
 * @see TuVar
 * @see  TuNumber
 */
public abstract class TuTerm implements Term {

    private static final long serialVersionUID = 1L;

    // true and false constants
    public static final Term TRUE = new TuStruct("true");
    public static final Term FALSE = new TuStruct("false");

    //boolean isCyclic = false; //Alberto -> da usare quando si supporteranno i termini ciclici

    // checking type and properties of the Term

    /**
     * is this term a prolog numeric term?
     * @deprecated Use <tt>instanceof Number</tt> instead.
     */
    @Deprecated
    public abstract boolean isNumber();

    /**
     * is this term a struct?
     * @deprecated Use <tt>instanceof Struct</tt> instead. 
     */
    @Deprecated
    public abstract boolean isStruct();

    /**
     * is this term a variable?
     * @deprecated Use <tt>instanceof Var</tt> instead. 
     */
    @Deprecated
    public abstract boolean isVar();

    /** is this term a null term?*/
    public abstract boolean isEmptyList();

    /** is this term a constant prolog term? */
    public abstract boolean isAtomic();

    /** is this term a prolog compound term? */
    public abstract boolean isCompound();

    /** is this term a prolog (alphanumeric) atom? */
    public abstract boolean isAtom();

    /** is this term a prolog list? */
    public abstract boolean isList();

    /** is this term a ground term? */
    public abstract boolean isGround();

    /**
     * Tests for the equality of two object terms
     *
     * The comparison follows the same semantic of
     * the isEqual method.
     *
     */
    @Override
    public boolean equals(Object t) {
        if (!(t instanceof Term))
            return false;
        return isEqual((Term) t);
    }

    /**
     * is term greater than term t?
     */
    public abstract boolean isGreater(Term t);

    /**
     * Tests if this term is (logically) equal to another
     */
    public boolean isEqual(Term t) { //Alberto
        return this.toString().equals(t.toString());
    }

    /**
     * Tests if this term (as java object) is equal to another
     */
    public boolean isEqualObject(Term t) { //Alberto
        if (!(t instanceof Term))
            return false;
        else
            return this == t;
    }

    /**
     * Gets the actual term referred by this Term. if the Term is a bound variable, the method gets the Term linked to the variable
     */
    public abstract Term getTerm();

    /**
     * Unlink variables inside the term
     */
    public abstract void free();

    /**
     * Resolves variables inside the term, starting from a specific time count.
     *
     * If the variables has been already resolved, no renaming is done.
     * @param count new starting time count for resolving process
     * @return the new time count, after resolving process
     */
    public  abstract long resolveTerm(long count);

    /**
     * Resolves variables inside the term
     * 
     * If the variables has been already resolved, no renaming is done.
     */
    public void resolveTerm() {
        resolveTerm(System.currentTimeMillis());
    }

    /**
     * gets a engine's copy of this term.
     * @param idExecCtx Execution Context identified
     */
    public Term copyGoal(AbstractMap<TuVar, TuVar> vars, int idExecCtx) {
        return copy(idExecCtx, vars);
    }

    /**
     * gets a copy of this term for the output
     */
    public Term copyResult(Collection<TuVar> goalVars, List<TuVar> resultVars) {
        IdentityHashMap<TuVar, TuVar> originals = new IdentityHashMap<TuVar, TuVar>();
        for (TuVar key : goalVars) {
            TuVar clone = new TuVar();
            if (!key.isAnonymous()) {
                clone = new TuVar(key.getOriginalName());
            }
            originals.put(key, clone);
            resultVars.add(clone);
        }
        return copy(originals, new IdentityHashMap<Term, TuVar>());
    }

    /**
     * gets a copy (with renamed variables) of the term.
     *
     * The list argument passed contains the list of variables to be renamed
     * (if empty list then no renaming)
     * @param idExecCtx Execution Context identifier
     */
    public abstract Term copy(int idExecCtx, AbstractMap<TuVar, TuVar> vMap);

    //Alberto
    public abstract Term copyAndRetainFreeVar(AbstractMap<TuVar, TuVar> vMap, int idExecCtx);

    /**
     * gets a copy for result.
     */
    public abstract Term copy(AbstractMap<TuVar, TuVar> vMap, AbstractMap<Term, TuVar> substMap);

    /**
     * Try to unify two terms
     * @param mediator have the reference of EngineManager
     * @param t1 the term to unify
     * @return true if the term is unifiable with this one
     */
    public boolean unify(TuProlog mediator, Term t1) {
        EngineManager engine = mediator.getEngineManager();
        resolveTerm();
        t1.resolveTerm();
        List<TuVar> v1 = new LinkedList<TuVar>(); /* Reviewed by: Paolo Contessi (was: ArrayList()) */
        List<TuVar> v2 = new LinkedList<TuVar>(); /* Reviewed by: Paolo Contessi (was: ArrayList()) */
        boolean ok = unify(v1, v2, t1, mediator.getFlagManager().isOccursCheckEnabled());
        if (ok) {
            ExecutionContext ec = engine.getCurrentContext();
            if (ec != null) {
                int id = (engine.getEnv() == null) ? TuVar.PROGRESSIVE : engine.getEnv().nDemoSteps;
                // Update trailingVars
                ec.trailingVars = new OneWayList<List<TuVar>>(v1, ec.trailingVars);
                // Renaming after unify because its utility regards not the engine but the user
                int count = 0;
                for (TuVar v : v1) {
                    v.rename(id, count);
                    if (id >= 0) {
                        id++;
                    } else {
                        count++;
                    }
                }
                for (TuVar v : v2) {
                    v.rename(id, count);
                    if (id >= 0) {
                        id++;
                    } else {
                        count++;
                    }
                }
            }
            return true;
        }
        TuVar.free(v1);
        TuVar.free(v2);
        return false;
    }

    //Alberto
    /**
     * Tests if this term is unifiable with an other term.
     * No unification is done.
     *
     * The test is done outside any demonstration context 
     * @param t the term to checked
     * @param isOccursCheckEnabled
     * @return true if the term is unifiable with this one
     */
    public boolean match(boolean isOccursCheckEnabled, Term t) {
        resolveTerm();
        t.resolveTerm();
        List<TuVar> v1 = new LinkedList<TuVar>();
        List<TuVar> v2 = new LinkedList<TuVar>();
        boolean ok = unify(v1, v2, t, isOccursCheckEnabled);
        TuVar.free(v1);
        TuVar.free(v2);
        return ok;
    }

    /**
     * Tests if this term is unifiable with an other term.
     * No unification is done.
     *
     * The test is done outside any demonstration context 
     * @param t the term to checked
     * @return true if the term is unifiable with this one
     */
    public boolean match(Term t) {
        return match(true, t); //Alberto
    }

    //Alberto
    /**
     * Tries to unify two terms, given a demonstration context
     * identified by the mark integer.
     *
     * Try the unification among the term and the term specified
     * @param varsUnifiedArg1 Vars unified in myself
     * @param varsUnifiedArg2 Vars unified in term t
     * @param isOccursCheckEnabled
     */
    public abstract boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t, boolean isOccursCheckEnabled);

    /**
     * Tries to unify two terms, given a demonstration context
     * identified by the mark integer.
     *
     * Try the unification among the term and the term specified
     * @param varsUnifiedArg1 Vars unified in myself
     * @param varsUnifiedArg2 Vars unified in term t
     */
    public  abstract boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t);

    /**
     * Static service to create a Term from a string.
     * @param st the string representation of the term
     * @return the term represented by the string
     * @throws InvalidTermException if the string does not represent a valid term
     */
    public static Term createTerm(String st) {
        return TuParser.parseSingleTerm(st);
    }

    /**
     * @deprecated Use {@link Term#createTerm(String)} instead.
     */
    @Deprecated
    public static Term parse(String st) {
        return Term.createTerm(st);
    }

    /**
     * Static service to create a Term from a string, providing an
     * external operator manager.
     * @param st the string representation of the term
     * @param op the operator manager used to build the term
     * @return the term represented by the string
     * @throws InvalidTermException if the string does not represent a valid term
     */
    public static Term createTerm(String st, OperatorManager op) {
        return TuParser.parseSingleTerm(st, op);
    }

    /**
     * @deprecated Use {@link Term#createTerm(String, OperatorManager)} instead.
     */
    @Deprecated
    public static Term parse(String st, OperatorManager op) {
        return Term.createTerm(st, op);
    }

    /**
     * Gets an iterator providing
     * a term stream from a source text
     */
    public static java.util.Iterator<Term> getIterator(String text) {
        return new TuParser(text).iterator();
    }

    // term representation

    /**
     * Gets the string representation of this term
     * as an X argument of an operator, considering the associative property.
     */
    public String toStringAsArgX(OperatorManager op, int prio) {
        return toStringAsArg(op, prio, true);
    }

    /**
     * Gets the string representation of this term
     * as an Y argument of an operator, considering the associative property.
     */
    public String toStringAsArgY(OperatorManager op, int prio) {
        return toStringAsArg(op, prio, false);
    }

    /**
     * Gets the string representation of this term
     * as an argument of an operator, considering the associative property.
     *
     *  If the boolean argument is true, then the term must be considered
     *  as X arg, otherwise as Y arg (referring to prolog associative rules)
     */
    public String toStringAsArg(OperatorManager op, int prio, boolean x) {
        return toString();
    }

    /**
     * The iterated-goal term G of a term T is a term defined
     * recursively as follows:
     * <ul>
     * <li>if T unifies with ^(_, Goal) then G is the iterated-goal
     * term of Goal</li>
     * <li>else G is T</li>
     * </ul>
     */
    public Term iteratedGoalTerm() {
        return this;
    }

    /*Castagna 06/2011*/
    /**
     * Visitor pattern
     * @param tv - Visitor
     */
    public abstract void accept(TermVisitor tv);

    //Alberto
    public String toJSON() {
        return JSONSerializerManager.toJSON(this);
    }

    //Alberto
    public static Term fromJSON(String jsonString) {
        if (jsonString.contains("Var")) {
            return JSONSerializerManager.fromJSON(jsonString, TuVar.class);
        } else if (jsonString.contains("Struct")) {
            return JSONSerializerManager.fromJSON(jsonString, TuStruct.class);
        } else if (jsonString.contains("Double")) {
            return JSONSerializerManager.fromJSON(jsonString, TuDouble.class);
        } else if (jsonString.contains("Int")) {
            return JSONSerializerManager.fromJSON(jsonString, TuInt.class);
        } else if (jsonString.contains("Long")) {
            return JSONSerializerManager.fromJSON(jsonString, TuLong.class);
        } else if (jsonString.contains("Float")) {
            return JSONSerializerManager.fromJSON(jsonString, TuFloat.class);
        } else
            return null;
    }
}