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

import java.util.List;

/**
 *
 * Double class represents the double prolog data type
 *
 */
public class TuDouble extends TuNumber {
    private static final long serialVersionUID = 1L;
    private double value;

    @SuppressWarnings("unused")
    private String type = "Double";

    TuDouble(double v) {
        value = v;
    }

    /**
     *  Returns the value of the Double as int
     */
    @Override
    final public int intValue() {
        return (int) value;
    }

    /**
     *  Returns the value of the Double as float
     *
     */
    @Override
    final public float floatValue() {
        return (float) value;
    }

    /**
     *  Returns the value of the Double as double
     *
     */
    @Override
    final public double doubleValue() {
        return value;
    }

    /**
     *  Returns the value of the Double as long
     */
    @Override
    final public long longValue() {
        return (long) value;
    }

    /** is this term a prolog integer term? */
    @Override
    final public boolean isInteger() {
        return false;
    }

    /** is this term a prolog real term? */
    @Override
    final public boolean isReal() {
        return true;
    }

    /** is an int Integer number? 
     * Was <tt>instanceof Int</tt> instead. */
    @Override
    final public boolean isTypeInt() {
        return false;
    }

    /** is an int Integer number?
     * Was <tt>instanceof Int</tt> instead. */
    @Override
    final public boolean isInt() {
        return false;
    }

    /** is a float Real number? 
     * Was <tt>instanceof alice.tuprolog.Float</tt> instead. */
    @Override
    final public boolean isTypeFloat() {
        return false;
    }

    /** is a float Real number?
     * Was <tt>instanceof alice.tuprolog.Float</tt> instead. */
    @Override
    final public boolean isFloat() {
        return false;
    }

    /** is a double Real number? 
     * Was <tt>instanceof alice.tuprolog.Double</tt> instead. */
    @Override
    final public boolean isTypeDouble() {
        return true;
    }

    /** is a double Real number?
     * Was <tt>instanceof alice.tuprolog.Double</tt> instead. */
    @Override
    final public boolean isDouble() {
        return true;
    }

    /** is a long Integer number? 
     * Was <tt>instanceof alice.tuprolog.Long</tt> instead. */
    @Override
    final public boolean isTypeLong() {
        return false;
    }

    /** is a long Integer number?
     * Was <tt>instanceof alice.tuprolog.Long</tt> instead. */
    @Override
    final public boolean isLong() {
        return false;
    }

    /**
     * Returns true if this Double term is grater that the term provided.
     * For number term argument, the int value is considered.
     */
    @Override
    public boolean isGreater(Term t) {
        t = t.dref();
        if (t .isNumber()) {
            return value > ((TuNumber) t).doubleValue();
        } else if (t .isTuStruct()) {
            return false;
        } else if (t .isVar()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tries to unify a term with the provided term argument.
     * This service is to be used in demonstration context.
     */
    @Override
    public boolean unify(List<TuVar> vl1, List<TuVar> vl2, Term t, boolean isOccursCheckEnabled) {
        t = t.dref();
        if (t .isVar()) {
            return t.unify(vl2, vl1, this, isOccursCheckEnabled);
        } else if (t .isNumber() && ((TuNumber) t).isReal()) {
            return value == ((TuNumber) t).doubleValue();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return java.lang.Double.toString(value);
    }

    public int resolveVariables(int count) {
        return count;
    }

    /**
     * @author Paolo Contessi
     */
    @Override
    public int compareTo(TuNumber o) {
        return (new java.lang.Double(value)).compareTo(o.doubleValue());
    }

    @Override
    public boolean unify(List<TuVar> varsUnifiedArg1, List<TuVar> varsUnifiedArg2, Term t) {
        return unify(varsUnifiedArg1, varsUnifiedArg2, t, true);
    }

}