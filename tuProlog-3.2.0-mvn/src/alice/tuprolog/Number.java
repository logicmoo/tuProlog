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


public abstract class Number extends Term implements Comparable<Number> {
	private static final long serialVersionUID = 1L;
    
    
    public abstract int intValue();
    
    
    public abstract float floatValue();
    
    public abstract long longValue();
    
    public abstract double doubleValue();
    
    public abstract boolean isInteger();
   
    public abstract boolean isReal();
 
    public abstract boolean isTypeInt();

    public abstract boolean isInt();
    
    
    public abstract boolean isTypeFloat();

  
    public abstract boolean isFloat();
    
    
    public abstract boolean isTypeDouble();

    public abstract boolean isDouble();
    
    
    public abstract boolean isTypeLong();

    public abstract boolean isLong();
    
    public static Number createNumber(String s) {
        Term t = Term.createTerm(s);
        if (t instanceof Number)
            return (Number) t;
        throw new InvalidTermException("Term " + t + " is not a number.");
    }
    
   
    public Term getTerm() {
        return this;
    }
   
    final public boolean isNumber() {
        return true;
    }
    
   
    final public boolean isStruct() {
        return false;
    }
    
   
    final public boolean isVar() {
        return false;
    }
    
    final public boolean isEmptyList() {
        return false;
    }

    final public boolean isAtomic() {
        return true;
    }
    
    
    final public boolean isCompound() {
        return false;
    }
    
   
    final public boolean isAtom() {
        return false;
    }
    
    
    final public boolean isList() {
        return false;
    }
   
    final public boolean isGround() {
        return true;
    }
    
    public Term copy(int idExecCtx) {
        return this;
    }
   
    Term copy(AbstractMap<Var,Var> vMap, int idExecCtx) {
        return this;
    }
    
    Term copy(AbstractMap<Var,Var> vMap, AbstractMap<Term,Var> substMap) {
        return this;
    }
    
    @Override
	public Term copyAndRetainFreeVar(AbstractMap<Var, Var> vMap, int idExecCtx) {
		// TODO Auto-generated method stub
		return this;
	}
    
    
    long resolveTerm(long count) {
        return count;
    }
    
    
    public void free() {}
    
    void restoreVariables() {}
    

    @Override    
    public void accept(TermVisitor tv) {		 
    	tv.visit(this);		 
    }

}