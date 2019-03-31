/*
 * tuProlog - Copyright (C) 2001-2007 aliCE team at deis.unibo.it
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

import java.util.AbstractMap;
//import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Struct class represents both compound prolog term
 * and atom term (considered as 0-arity compound).
 */
public class TuStruct extends TuTerm {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private String type = "Struct";

    /**
     * name of the structure
     */
    private String name;
    /**
     * args array
     */
    private Term[] arg;
    /**
     * arity
     */
    private int arity;
    /**
     * to speedup hash map operation
     */
    private String predicateIndicator;
    /**
     * primitive behaviour
     */
    private transient PrimitiveInfo primitive;
    /**
     * it indicates if the term is resolved
     */
    private boolean resolved = false;

    /**
     * Builds a Struct representing an atom
     */
    public TuStruct(String f) {
        this(f, 0);
    }

    /**
     * Builds a compound, with one argument
     */
    public TuStruct(String f, Term at0) {
        this(f, new Term[] { at0 });
    }

    /**
     * Builds a compound, with two arguments
     */
    public TuStruct(String f, Term at0, Term at1) {
        this(f, new Term[] { at0, at1 });
    }

    /**
     * Builds a compound, with three arguments
     */
    public TuStruct(String f, Term at0, Term at1, Term at2) {
        this(f, new Term[] { at0, at1, at2 });
    }

    /**
     * Builds a compound, with four arguments
     */
    public TuStruct(String f, Term at0, Term at1, Term at2, Term at3) {
        this(f, new Term[] { at0, at1, at2, at3 });
    }

    /**
     * Builds a compound, with five arguments
     */
    public TuStruct(String f, Term at0, Term at1, Term at2, Term at3, Term at4) {
        this(f, new Term[] { at0, at1, at2, at3, at4 });
    }

    /**
     * Builds a compound, with six arguments
     */
    public TuStruct(String f, Term at0, Term at1, Term at2, Term at3, Term at4, Term at5) {
        this(f, new Term[] { at0, at1, at2, at3, at4, at5 });
    }

    /**
     * Builds a compound, with seven arguments
     */
    public TuStruct(String f, Term at0, Term at1, Term at2, Term at3, Term at4, Term at5, Term at6) {
        this(f, new Term[] { at0, at1, at2, at3, at4, at5, at6 });
    }

    /**
     * Builds a compound, with an array of arguments
     */
    public TuStruct(String f, Term[] argList) {
        this(f, argList.length);
        for (int i = 0; i < argList.length; i++)
            if (argList[i] == null)
                throw new InvalidTermException("Arguments of a Struct cannot be null");
            else
                arg[i] = argList[i];
    }

    /**
     * Builds a structure representing an empty list
     */
    public TuStruct() {
        this("[]", 0);
        resolved = true;
    }

    /**
     * Builds a list providing head and tail
     */
    public TuStruct(Term h, Term t) {
        this(".", 2);
        arg[0] = h;
        arg[1] = t;
    }

    /**
     * Builds a list specifying the elements
     */
    public TuStruct(Term[] argList) {
        this(argList, 0);
    }

    private TuStruct(Term[] argList, int index) {
        this(".", 2);
        if (index < argList.length) {
            arg[0] = argList[index];
            arg[1] = new TuStruct(argList, index + 1);
        } else {
            // build an empty list
            name = "[]";
            arity = 0;
            arg = null;
        }
    }

    /**
     * Builds a compound, with a linked list of arguments
     */
    TuStruct(String f, LinkedList<Term> al) {
        name = f;
        arity = al.size();
        if (arity > 0) {
            arg = new Term[arity];
            for (int c = 0; c < arity; c++)
                arg[c] = al.removeFirst();
        }
        predicateIndicator = name + "/" + arity;
        resolved = false;
    }

    private TuStruct(int arity_) {
        arity = arity_;
        arg = new Term[arity];
    }

    private TuStruct(String name_, int arity_) {
        if (name_ == null)
            throw new InvalidTermException("The functor of a Struct cannot be null");
        if (name_.length() == 0 && arity_ > 0)
            throw new InvalidTermException("The functor of a non-atom Struct cannot be an empty string");
        name = name_;
        arity = arity_;
        if (arity > 0) {
            arg = new Term[arity];
        }
        predicateIndicator = name + "/" + arity;
        resolved = false;
    }

    /** @deprecated Use Struct#getPredicateIndicator instead. */
    String getHashKey() {
        return getPredicateIndicator();
    }

    /**
     * @return
     */
    String getPredicateIndicator() {
        return predicateIndicator;
    }

    /**
     * Gets the number of elements of this structure
     */
    public int getArity() {
        return arity;
    }

    /**
     * Gets the functor name  of this structure
     */
    public String fname() {
        return name;
    }

    /**
     * Gets the i-th element of this structure
     *
     * No bound check is done
     */
    public Term getPlainArg(int index) {
        return arg[index];
    }

    /**
     * Sets the i-th element of this structure
     *
     * (Only for internal service)
     */
    public void setArg(int index, Term argument) {
        arg[index] = argument;
    }

    /**
     * Gets the i-th element of this structure
     *
     * No bound check is done. It is equivalent to
     * <code>getArg(index).getTerm()</code>
     */
    public Term getTerm(int index) {
        final Term term = arg[index];
        if (!(term.isVar()))
            return term;
        return term.dref();
    }

    // checking type and properties of the Term

    /** is this term a prolog numeric term? */
    @Override
    public boolean isNumber() {
        return false;
    }

    /** is this term a struct  */
    @Override
    public boolean isTuStruct() {
        return true;
    }


    /** is this term a variable  */
    @Override
    public boolean isVar() {
        return false;
    }

    // check type services

    @Override
    public boolean isAtomic() {
        return arity == 0;
    }

    @Override
    public boolean isCompound() {
        return arity > 0;
    }

    @Override
    public boolean isAtomSymbol() {
        return (arity == 0 || isEmptyList());
    }

    @Override
    public boolean isConsList() {
        return (name.equals(".") && arity == 2 && arg[1].isConsList()) || isEmptyList();
    }

    @Override
    public boolean isGround() {
        for (int i = 0; i < arity; i++) {
            if (!arg[i].isGround()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check is this struct is clause or directive
     */
    public boolean isClause() {
        return (name.equals(":-") && arity > 1 && arg[0].dref().isTuStruct());
    }

    @Override
    public Term dref() {
        return this;
    }

    /**
     * Gets an argument inside this structure, given its name
     * 
     * @param name name of the structure 
     * @return the argument or null if not found
     */
    public TuStruct getArg(String name) {
        if (arity == 0) {
            return null;
        }
        for (int i = 0; i < arg.length; i++) {
            if (arg[i].isTuStruct()) {
                TuStruct s = (TuStruct) arg[i];
                if (s.fname().equals(name)) {
                    return s;
                }
            }
        }
        for (int i = 0; i < arg.length; i++) {
            if (arg[i].isTuStruct()) {
                TuStruct s = (TuStruct) arg[i];
                TuStruct sol = s.getArg(name);
                if (sol != null) {
                    return sol;
                }
            }
        }
        return null;
    }

    /**
     * Test if a term is greater than other
     */
    @Override
    public boolean isGreater(Term t) {
        t = t.dref();
        if (!(t.isTuStruct())) {
            return true;
        } else {
            TuStruct ts = (TuStruct) t;
            int tarity = ts.arity;
            if (arity > tarity) {
                return true;
            } else if (arity == tarity) {
                if (name.compareTo(ts.name) > 0) {
                    return true;
                } else if (name.compareTo(ts.name) == 0) {
                    for (int c = 0; c < arity; c++) {
                        if (arg[c].isGreater(ts.arg[c])) {
                            return true;
                        } else if (!arg[c].isEqual(ts.arg[c])) {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets a copy of this structure
     * @param vMap is needed for register occurence of same variables
     */
    @Override
    public Term copy(int idExecCtx, AbstractMap<TuVar, TuVar> vMap) {
        TuStruct t = new TuStruct(arity);
        t.resolved = resolved;
        t.name = name;
        t.predicateIndicator = predicateIndicator;
        t.primitive = primitive;
        for (int c = 0; c < arity; c++) {
            //if(!this.arg[c].isCyclic)
            t.arg[c] = arg[c].copy(idExecCtx, vMap);
            //else
            //	t.arg[c] = this.arg[c];
        }
        return t;
    }

    @Override //Alberto
    public Term copyAndRetainFreeVar(AbstractMap<TuVar, TuVar> vMap, int idExecCtx) {
        TuStruct t = new TuStruct(arity);
        t.resolved = resolved;
        t.name = name;
        t.predicateIndicator = predicateIndicator;
        t.primitive = primitive;
        for (int c = 0; c < arity; c++) {
            //if(!this.arg[c].isCyclic)
            t.arg[c] = arg[c].dref().copyAndRetainFreeVar(vMap, idExecCtx);
            //qui una .getTerm() necessaria solo in $wt_list!
            //else
            //	t.arg[c] = this.arg[c];
        }
        return t;
    }

    /**
     * Gets a copy of this structure
     * @param vMap is needed for register occurence of same variables
     */
    @Override
    public Term copy(AbstractMap<TuVar, TuVar> vMap, AbstractMap<Term, TuVar> substMap) {
        TuStruct t = new TuStruct(arity);
        t.resolved = false;
        t.name = name;
        t.predicateIndicator = predicateIndicator;
        t.primitive = null;
        for (int c = 0; c < arity; c++) {
            //if(!this.arg[c].isCyclic)
            t.arg[c] = arg[c].copy(vMap, substMap);
            //else
            //	t.arg[c] = this.arg[c];
        }
        return t;
    }

    /**
     * resolve term
     */
    @Override
    public long resolveTerm(long count) {
        if (resolved) {
            return count;
        } else {
            LinkedList<TuVar> vars = new LinkedList<TuVar>();
            return resolveTerm(vars, count);
        }
    }

    /**
     * Resolve name of terms
     * @param vl list of variables resolved
     * @param count start timestamp for variables of this term
     * @return next timestamp for other terms
     */
    long resolveTerm(LinkedList<TuVar> vl, long count) {
        long newcount = count;
        for (int c = 0; c < arity; c++) {
            Term term = arg[c];
            if (term != null) {
                //--------------------------------
                // we want to resolve only not linked variables:
                // so linked variables must get the linked term
                term = term.dref();
                //--------------------------------
                if (term.isVar()) {
                    TuVar t = (TuVar) term;
                    t.setInternalTimestamp(newcount++);
                    if (!t.isAnonymous()) {
                        // searching a variable with the same name in the list
                        String name = t.getName();
                        Iterator<TuVar> it = vl.iterator();
                        TuVar found = null;
                        while (it.hasNext()) {
                            TuVar vn = it.next();
                            if (name.equals(vn.getName())) {
                                found = vn;
                                break;
                            }
                        }
                        if (found != null) {
                            arg[c] = found;
                        } else {
                            vl.add(t);
                        }
                    }
                } else if (term.isTuStruct()) {
                    newcount = ((TuStruct) term).resolveTerm(vl, newcount);
                }
            }
        }
        resolved = true;
        return newcount;
    }

    // services for list structures

    /**
     * Is this structure an empty list?
     */
    @Override
    public boolean isEmptyList() {
        return fname().equals("[]") && getArity() == 0;
    }

    /**
     * Gets the head of this structure, which is supposed to be a list.
     * 
     * <p>
     * Gets the head of this structure, which is supposed to be a list.
     * If the callee structure is not a list, throws an <code>UnsupportedOperationException</code>
     * </p>
     */
    public Term listHead() {
        if (!isConsList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        return arg[0].dref();
    }

    /**
     * Gets the tail of this structure, which is supposed to be a list.
     * 
     * <p>
     * Gets the tail of this structure, which is supposed to be a list.
     * If the callee structure is not a list, throws an <code>UnsupportedOperationException</code>
     * </p>
     */
    public TuStruct listTail() {
        if (!isConsList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        return (TuStruct) arg[1].dref();
    }

    /**
     * Gets the number of elements of this structure, which is supposed to be a list.
     * 
     * <p>
     * Gets the number of elements of this structure, which is supposed to be a list.
     * If the callee structure is not a list, throws an <code>UnsupportedOperationException</code>
     * </p>
     */
    public int listSize() {
        if (!isConsList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        TuStruct t = this;
        int count = 0;
        while (!t.isEmptyList()) {
            count++;
            t = (TuStruct) t.arg[1].dref();
        }
        return count;
    }

    /**
     * Gets an iterator on the elements of this structure, which is supposed to be a list.
     * 
     * <p>
     * Gets an iterator on the elements of this structure, which is supposed to be a list.
     * If the callee structure is not a list, throws an <code>UnsupportedOperationException</code>
     * </p>
     */
    public Iterator<? extends Term> listIterator() {
        if (!isConsList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        return new StructIterator(this);
    }

    // hidden services

    /**
     * Gets a list Struct representation, with the functor as first element.
     */
    TuStruct toList() {
        TuStruct t = new TuStruct();
        for (int c = arity - 1; c >= 0; c--) {
            t = new TuStruct(arg[c].dref(), t);
        }
        return new TuStruct(new TuStruct(name), t);
    }

    /**
     * Gets a flat Struct from this structure considered as a List
     *
     * If this structure is not a list, null object is returned
     */
    TuStruct fromList() {
        Term ft = arg[0].dref();
        if (!ft.isAtomSymbol()) {
            return null;
        }
        TuStruct at = (TuStruct) arg[1].dref();
        LinkedList<Term> al = new LinkedList<Term>();
        while (!at.isEmptyList()) {
            if (!at.isConsList()) {
                return null;
            }
            al.addLast(at.getTerm(0));
            at = (TuStruct) at.getTerm(1);
        }
        return new TuStruct(((TuStruct) ft).name, al);
    }

    /**
     * Appends an element to this structure supposed to be a list
     */
    public void appendDestructive(Term t) {
        if (isEmptyList()) {
            name = ".";
            arity = 2;
            predicateIndicator = name + "/" + arity; /* Added by Paolo Contessi */
            arg = new Term[arity];
            arg[0] = t;
            arg[1] = new TuStruct();
        } else if (arg[1].isConsList()) {
            ((TuStruct) arg[1]).appendDestructive(t);
        } else {
            arg[1] = t;
        }
    }

    /**
     * Inserts (at the head) an element to this structure supposed to be a list
     */
    void insertDestructive(Term t) {
        TuStruct co = new TuStruct();
        co.arg[0] = arg[0];
        co.arg[1] = arg[1];
        arg[0] = t;
        arg[1] = co;
    }

    /**
     * Try to unify two terms
     * @param t the term to unify
     * @param vl1 list of variables unified
     * @param vl2 list of variables unified
     * @return true if the term is unifiable with this one
     */
    @Override
    public boolean unify(List<TuVar> vl1, List<TuVar> vl2, Term t, boolean isOccursCheckEnabled) {
        // In fase di unificazione bisogna annotare tutte le variabili della struct completa.
        t = t.dref();
        if (t.isTuStruct()) {
            TuStruct ts = (TuStruct) t;
            if (arity == ts.arity && name.equals(ts.name)) {
                for (int c = 0; c < arity; c++) {
                    if (!arg[c].unify(vl1, vl2, ts.arg[c], isOccursCheckEnabled)) {
                        return false;
                    }
                }
                return true;
            }
        } else if (t.isVar()) {
            return t.unify(vl2, vl1, this, isOccursCheckEnabled);
        }
        return false;
    }

    /** dummy method */
    @Override
    public void free() {
    }

    /**
     * Set primitive behaviour associated at structure
     */
    void setPrimitive(PrimitiveInfo b) {
        primitive = b;
    }

    /**
     * Get primitive behaviour associated at structure
     */
    public PrimitiveInfo getPrimitive() {
        return primitive;
    }

    /**
     * Check if this term is a primitive struct
     */
    public boolean isPrimitive() {
        return primitive != null;
    }

    /**
     * Gets the string representation of this structure
     *
     * Specific representations are provided for lists and atoms.
     * Names starting with upper case letter are enclosed in apices.
     */
    @Override
    public String toString() {
        // empty list case
        if (isEmptyList())
            return "[]";
        // list case
        if (name.equals(".") && arity == 2) {
            return ("[" + toString0() + "]");
        } else if (name.equals("{}")) {
            return ("{" + toString0_bracket() + "}");
        } else {
            String s = (TuParser.isAtom(name) ? name : "'" + name + "'");
            if (arity > 0) {
                s = s + "(";
                for (int c = 1; c < arity; c++) {
                    if (!(arg[c - 1].isVar())) {
                        s = s + arg[c - 1].toString() + ",";
                    } else {
                        s = s + ((TuVar) arg[c - 1]).toStringFlattened() + ",";
                    }
                }
                if (!(arg[arity - 1].isVar())) {
                    s = s + arg[arity - 1].toString() + ")";
                } else {
                    s = s + ((TuVar) arg[arity - 1]).toStringFlattened() + ")";
                }
            }
            return s;
        }
    }

    private String toString0() {
        Term h = arg[0].dref();
        Term t = arg[1].dref();
        if (t.isConsList()) {
            TuStruct tl = (TuStruct) t;
            if (tl.isEmptyList()) {
                return h.toString();
            }
            if (h.isVar()) {
                return (((TuVar) h).toStringFlattened() + "," + tl.toString0());
            } else {
                return (h.toString() + "," + tl.toString0());
            }
        } else {
            String h0;
            String t0;
            if (h.isVar()) {
                h0 = ((TuVar) h).toStringFlattened();
            } else {
                h0 = h.toString();
            }
            if (t.isVar()) {
                t0 = ((TuVar) t).toStringFlattened();
            } else {
                t0 = t.toString();
            }
            return (h0 + "|" + t0);
        }
    }

    private String toString0_bracket() {
        if (arity == 0) {
            return "";
        } else if (arity == 1 && !((arg[0].isTuStruct()) && ((TuStruct) arg[0]).fname().equals(","))) {
            return arg[0].dref().toString();
        } else {
            // comma case 
            Term head = ((TuStruct) arg[0]).getTerm(0);
            Term tail = ((TuStruct) arg[0]).getTerm(1);
            StringBuffer buf = new StringBuffer(head.toString());
            while (tail.isTuStruct() && ((TuStruct) tail).fname().equals(",")) {
                head = ((TuStruct) tail).getTerm(0);
                buf.append("," + head.toString());
                tail = ((TuStruct) tail).getTerm(1);
            }
            buf.append("," + tail.toString());
            return buf.toString();
        }
    }

    private String toStringAsList(OperatorManager op) {
        Term h = arg[0];
        Term t = arg[1].dref();
        if (t.isConsList()) {
            TuStruct tl = (TuStruct) t;
            if (tl.isEmptyList()) {
                return h.toStringAsArgY(op, 0);
            }
            return (h.toStringAsArgY(op, 0) + "," + tl.toStringAsList(op));
        } else {
            return (h.toStringAsArgY(op, 0) + "|" + t.toStringAsArgY(op, 0));
        }
    }

    @Override
    public String toStringAsArg(OperatorManager op, int prio, boolean x) {
        int p = 0;
        String v = "";

        if (name.equals(".") && arity == 2) {
            if (arg[0].isEmptyList()) {
                return ("[]");
            } else {
                return ("[" + toStringAsList(op) + "]");
            }
        } else if (name.equals("{}")) {
            return ("{" + toString0_bracket() + "}");
        }

        if (arity == 2) {
            if ((p = op.opPrio(name, "xfx")) >= OperatorManager.OP_LOW) {
                return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + arg[0].toStringAsArgX(op, p) + " " + name
                        + " " + arg[1].toStringAsArgX(op, p) + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name, "yfx")) >= OperatorManager.OP_LOW) {
                return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + arg[0].toStringAsArgY(op, p) + " " + name
                        + " " + arg[1].toStringAsArgX(op, p) + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name, "xfy")) >= OperatorManager.OP_LOW) {
                if (!name.equals(",")) {
                    return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + arg[0].toStringAsArgX(op, p) + " "
                            + name + " " + arg[1].toStringAsArgY(op, p)
                            + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
                } else {
                    return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + arg[0].toStringAsArgX(op, p) +
                    //",\n\t"+
                            "," + arg[1].toStringAsArgY(op, p) + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
                }
            }
        } else if (arity == 1) {
            if ((p = op.opPrio(name, "fx")) >= OperatorManager.OP_LOW) {
                return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + name + " " + arg[0].toStringAsArgX(op, p)
                        + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name, "fy")) >= OperatorManager.OP_LOW) {
                return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + name + " " + arg[0].toStringAsArgY(op, p)
                        + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name, "xf")) >= OperatorManager.OP_LOW) {
                return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + arg[0].toStringAsArgX(op, p) + " " + name
                        + " " + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name, "yf")) >= OperatorManager.OP_LOW) {
                return ((((x && p >= prio) || (!x && p > prio)) ? "(" : "") + arg[0].toStringAsArgY(op, p) + " " + name
                        + " " + (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
        }
        v = (TuParser.isAtom(name) ? name : "'" + name + "'");
        if (arity == 0) {
            return v;
        }
        v = v + "(";
        for (p = 1; p < arity; p++) {
            v = v + arg[p - 1].toStringAsArgY(op, 0) + ",";
        }
        v = v + arg[arity - 1].toStringAsArgY(op, 0);
        v = v + ")";
        return v;
    }

    @Override
    public Term iteratedGoalTerm() {
        if (name.equals("^") && arity == 2) {
            Term goal = getTerm(1);
            return goal.iteratedGoalTerm();
        } else
            return super.iteratedGoalTerm();
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