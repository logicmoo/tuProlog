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

import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import alice.tuprolog.json.JSONSerializerManager;

/**
 * Term class is the root abstract class for prolog data type
 * @see TuStruct
 * @see TuVar
 * @see  TuNumber
 */
public abstract interface Term extends Serializable {

    // true and false constants
    public static final Term TRUE = createTuAtom("true");
    public static final Term FALSE = createTuAtom("false");

    //boolean isCyclic = false; //Alberto -> da usare quando si supporteranno i termini ciclici

    // checking type and properties of the Term

    /**
     * is this term a prolog numeric term?
     * Was <tt>instanceof Number</tt> instead.
     */
    // @Deprecated
    public abstract boolean isNumber();

    /**
     * is this term a struct?
     * Was <tt>instanceof Struct</tt> instead. */
    public abstract boolean isTuStruct();

    /**
     * is this term a variable?
     * Was <tt>instanceof Var</tt> instead. */
    public abstract boolean isVar();

    /** is this term a null term?*/
    public abstract boolean isEmptyList();

    /** is this term a constant prolog term? */
    public abstract boolean isAtomic();

    /** is this term a prolog compound term? */
    public abstract boolean isCompound();

    /** is this term a prolog (alphanumeric) atom? */
    public abstract boolean isAtomSymbol();

    /** is this term a prolog list? */
    public abstract boolean isPlList();

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
    public boolean equals(Object t);

    /**
     * is term greater than term t?
     */
    public abstract boolean isGreater(Term t);

    /**
     * Tests if this term is (logically) equal to another
     */
    public boolean isEqual(Term t);

    /**
     * Tests if this term (as java object) is equal to another
     */
    public boolean isEqualObject(Term t);

    /**
     * Gets the actual term referred by this Term. if the Term is a bound variable, the method gets the Term linked to the variable
     */
    public abstract Term dref();

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
    abstract long resolveTerm(long count);

    /**
     * Resolves variables inside the term
     * 
     * If the variables has been already resolved, no renaming is done.
     */
    public void resolveTerm();

    /**
     * gets a engine's copy of this term.
     * @param idExecCtx Execution Context identified
     */
    public Term copyGoal(AbstractMap<TuVar, TuVar> vars, int idExecCtx);

    /**
     * gets a copy of this term for the output
     */
    public Term copyResult(Collection<TuVar> goalVars, List<TuVar> resultVars);

    /**
     * gets a copy (with renamed variables) of the term.
     *
     * The list argument passed contains the list of variables to be renamed
     * (if empty list then no renaming)
     * @param idExecCtx Execution Context identifier
     */
    abstract Term copy(int idExecCtx, AbstractMap<TuVar, TuVar> vMap);

    //Alberto
    public abstract Term copyAndRetainFreeVar(AbstractMap<TuVar, TuVar> vMap, int idExecCtx);

    /**
     * gets a copy for result.
     */
    abstract Term copy(AbstractMap<TuVar, TuVar> vMap, AbstractMap<Term, TuVar> substMap);

    /**
     * Try to unify two terms
     * @param mediator have the reference of EngineManager
     * @param t1 the term to unify
     * @return true if the term is unifiable with this one
     */
    public boolean unify(TuProlog mediator, Term t1);

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
    public boolean match(boolean isOccursCheckEnabled, Term t);

    /**
     * Tests if this term is unifiable with an other term.
     * No unification is done.
     *
     * The test is done outside any demonstration context 
     * @param t the term to checked
     * @return true if the term is unifiable with this one
     */
    public boolean match(Term t);

    //Alberto
    /**
     * Tries to unify two terms, given a demonstration context
     * identified by the mark integer.
     *
     * Try the unification between the term and the term specified
     * @param varsUnifiedArg1 Vars unified in myself
     * @param varsUnifiedArg2 Vars unified in term t
     * @param isOccursCheckEnabled
     */
    abstract boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t,
            boolean isOccursCheckEnabled);

    /**
     * Tries to unify two terms, given a demonstration context
     * identified by the mark integer.
     *
     * Try the unification among the term and the term specified
     * @param varsUnifiedArg1 Vars unified in myself
     * @param varsUnifiedArg2 Vars unified in term t
     */
    abstract boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t);

    // term representation

    /**
     * Gets the string representation of this term
     * as an X argument of an operator, considering the associative property.
     */
    String toStringAsArgX(OperatorManager op, int prio);

    /**
     * Gets the string representation of this term
     * as an Y argument of an operator, considering the associative property.
     */
    String toStringAsArgY(OperatorManager op, int prio);

    /**
     * Gets the string representation of this term
     * as an argument of an operator, considering the associative property.
     *
     *  If the boolean argument is true, then the term must be considered
     *  as X arg, otherwise as Y arg (referring to prolog associative rules)
     */
    String toStringAsArg(OperatorManager op, int prio, boolean x);

    /**
     * The iterated-goal term G of a term T is a term defined
     * recursively as follows:
     * <ul>
     * <li>if T unifies with ^(_, Goal) then G is the iterated-goal
     * term of Goal</li>
     * <li>else G is T</li>
     * </ul>
     */
    public Term iteratedGoalTerm();

    /*Castagna 06/2011*/
    /**
     * Visitor pattern
     * @param tv - Visitor
     */
    public abstract void accept(TuTermVisitor tv);

    //Alberto
    public String toJSON();

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

    /**
     * @return
     */
    public abstract boolean isInt();

    /**
     * @return
     */
    public abstract int intValue();

    /**
     * @return
     */
    String fname();

    /**
     * @return
     */
    public abstract Iterator<? extends Term> listIteratorProlog();

    /**
     * @return
     */
    public abstract int getPlArity();

    /**
     * @param z
     * @return
     */
    public abstract Term getPlainArg(int z);

    /**
     * @param i
     * @param otherClauseList
     */
    public abstract void setArg(int i, Term otherClauseList);

    /**
     * @return
     */
    public abstract boolean isInteger();

    /**
     * @return
     */
    public abstract long longValue();

}