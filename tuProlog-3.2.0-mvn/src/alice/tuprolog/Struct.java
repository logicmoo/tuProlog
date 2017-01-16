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

import alice.tuprolog.InvalidTermException;
import alice.tuprolog.TermVisitor;


public class Struct extends Term {
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private String type = "Struct";
    
    
    private String name;
    
    private Term[] arg;
   
    private int arity;
    
    private String predicateIndicator;
    
    private transient PrimitiveInfo primitive;
    
    private boolean resolved=false;
    
    public Struct(String f) {
        this(f,0);
    }
   
    public Struct(String f, Term at0) {
        this(f, new Term[] {at0});
    }
    
    public Struct(String f, Term at0, Term at1) {
        this(f, new Term[] {at0, at1});
    }
    
    public Struct(String f, Term at0, Term at1, Term at2) {
        this(f, new Term[] {at0, at1, at2});
    }
   
    public Struct(String f, Term at0, Term at1, Term at2, Term at3) {
        this(f, new Term[] {at0, at1, at2, at3});
    }
    
    public Struct(String f, Term at0, Term at1, Term at2, Term at3, Term at4) {
        this(f, new Term[] {at0, at1, at2, at3, at4});
    }
    
    public Struct(String f, Term at0, Term at1, Term at2, Term at3, Term at4, Term at5) {
        this(f, new Term[] {at0, at1, at2, at3, at4, at5});
    }
    
    public Struct(String f, Term at0, Term at1, Term at2, Term at3, Term at4, Term at5, Term at6) {
        this(f, new Term[] {at0, at1, at2, at3, at4, at5, at6});
    }
    
    public Struct(String f, Term[] argList) {
        this(f, argList.length);
        for (int i = 0; i < argList.length; i++)
            if (argList[i] == null)
                throw new InvalidTermException("Arguments of a Struct cannot be null");
            else
                arg[i] = argList[i];
    }
    
    public Struct() {
        this("[]", 0);
        resolved = true;
    }
    
    public Struct(Term h,Term t) {
        this(".",2);
        arg[0] = h;
        arg[1] = t;
    }
    
    public Struct(Term[] argList) {
        this(argList,0);
    }
    
    private Struct(Term[] argList, int index) {
        this(".",2);
        if (index<argList.length) {
            arg[0] = argList[index];
            arg[1] = new Struct(argList,index+1);
        } else {
            // build an empty list
            name = "[]";
            arity = 0;
            arg = null;
        }
    }
    
    Struct(String f, LinkedList<Term> al) {
        name = f;
        arity = al.size();
        if (arity > 0) {
            arg = new Term[arity];
            for(int c = 0;c < arity;c++)
                arg[c] = al.removeFirst();
        }
        predicateIndicator = name + "/" + arity;
        resolved = false;
    }
    
    private Struct(int arity_) {
        arity = arity_;
        arg = new Term[arity];
    }
    
    private Struct(String name_,int arity_) {
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
    
    String getHashKey() {
        return getPredicateIndicator();
    }
    
    String getPredicateIndicator() {
        return predicateIndicator;
    }
   
    public int getArity() {
        return arity;
    }
    
    public String getName() {
        return name;
    }
    
    public Term getArg(int index) {
        return arg[index];
    }
    
    public void setArg(int index, Term argument) {
        arg[index] = argument;
    }
    
    public Term getTerm(int index) {
            if (!(arg[index] instanceof Var))
                return arg[index];
            return arg[index].getTerm();
    }
    
    public boolean isNumber() {
        return false;
    }
    
    
    public boolean isStruct() {
        return true;
    }
    
    
    public boolean isVar() {
        return false;
    }
    
    // check type services
    
    public boolean isAtomic() {
        return  arity == 0;
    }
    
    public boolean isCompound() {
        return arity > 0;
    }
    
    public boolean isAtom() {
        return (arity == 0 || isEmptyList());
    }
    
    public boolean isList() {
        return (name.equals(".") && arity == 2 && arg[1].isList()) || isEmptyList();
    }
    
    public boolean isGround() {
        for (int i=0; i<arity; i++) {
            if (!arg[i].isGround()) {
                return false;
            }
        }
        return true;
    }
    
   
    public boolean isClause() {
        return(name.equals(":-") && arity > 1 && arg[0].getTerm() instanceof Struct);
    }    
    
    public Term getTerm() {
        return this;
    }
    
    
    public Struct getArg(String name) {
        if (arity == 0) {
            return null;
        }
        for (int i=0; i<arg.length; i++) {
            if (arg[i] instanceof Struct) {
                Struct s = (Struct) arg[i];
                if (s.getName().equals(name)) {
                    return s;
                }
            }
        }
        for (int i=0; i<arg.length; i++) {
            if (arg[i] instanceof Struct) {
                Struct s = (Struct)arg[i];
                Struct sol = s.getArg(name);
                if (sol!=null) {
                    return sol;
                }
            }
        }
        return null;
    }
    
   
    public boolean isGreater(Term t) {
        t = t.getTerm();
        if (!(t instanceof Struct)) {
            return true;
        } else {
            Struct ts = (Struct) t;
            int tarity = ts.arity;
            if (arity > tarity) {
                return true;
            } else if (arity == tarity) {
            	if (name.compareTo(ts.name) > 0) {
                    return true;
                } else if (name.compareTo(ts.name) == 0) {
                    for (int c = 0;c < arity;c++) {
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
    
    Term copy(AbstractMap<Var,Var> vMap, int idExecCtx) {
        Struct t = new Struct(arity);
        t.resolved  = resolved;
        t.name      = name;
        t.predicateIndicator   = predicateIndicator;
        t.primitive = primitive;
        for (int c = 0;c < arity;c++) {
        	//if(!this.arg[c].isCyclic)
        		t.arg[c] = arg[c].copy(vMap, idExecCtx);
        	//else
        	//	t.arg[c] = this.arg[c];
        }
        return t;
    }
    
    @Override //Alberto
	public Term copyAndRetainFreeVar(AbstractMap<Var,Var> vMap, int idExecCtx) {
    	Struct t = new Struct(arity);
        t.resolved  = resolved;
        t.name      = name;
        t.predicateIndicator   = predicateIndicator;
        t.primitive = primitive;
        for (int c = 0;c < arity;c++) {
        	//if(!this.arg[c].isCyclic)
        		t.arg[c] = arg[c].getTerm().copyAndRetainFreeVar(vMap, idExecCtx); 
            	//qui una .getTerm() necessaria solo in $wt_list!
        	//else
        	//	t.arg[c] = this.arg[c];
        }
        return t;
	}
    
    Term copy(AbstractMap<Var,Var> vMap, AbstractMap<Term,Var> substMap) {
        Struct t = new Struct(arity);
        t.resolved  = false;
        t.name      = name;
        t.predicateIndicator   = predicateIndicator;
        t.primitive = null;
        for (int c = 0;c < arity;c++) {
        	//if(!this.arg[c].isCyclic)
        		t.arg[c] = arg[c].copy(vMap, substMap);
        	//else
        	//	t.arg[c] = this.arg[c];
        }
        return t;
    }
    
   
    long resolveTerm(long count) {
        if (resolved) {
            return count;
        } else {
            LinkedList<Var> vars = new LinkedList<Var>();
            return resolveTerm(vars,count);
        }
    }
    
    long resolveTerm(LinkedList<Var> vl,long count) {
        long newcount=count;
        for (int c = 0;c < arity;c++) {
            Term term=arg[c];
            if (term!=null) {
                //--------------------------------
                // we want to resolve only not linked variables:
                // so linked variables must get the linked term
                term=term.getTerm();
                //--------------------------------
                if (term instanceof Var) {
                    Var t = (Var) term;
                    t.setInternalTimestamp(newcount++);
                    if (!t.isAnonymous()) {
                        // searching a variable with the same name in the list
                        String name= t.getName();
                        Iterator<Var> it = vl.iterator();
                        Var found = null;
                        while (it.hasNext()) {
                            Var vn = it.next();
                            if (name.equals(vn.getName())) {
                                found=vn;
                                break;
                            }
                        }
                        if (found != null) {
                            arg[c] = found;
                        } else {
                            vl.add(t);
                        }
                    }
                } else if (term instanceof Struct) {
                    newcount = ( (Struct) term ).resolveTerm(vl,newcount);
                }
            }
        }
        resolved = true;
        return newcount;
    }
    
    
    public boolean isEmptyList() {
        return name.equals("[]") && arity == 0;
    }
    
   
    public Term listHead() {
        if (!isList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        return arg[0].getTerm();
    }
  
    public Struct listTail() {
        if (!isList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        return (Struct) arg[1].getTerm();
    }
   
    public int listSize() {
        if (!isList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        Struct t = this;
        int count = 0;
        while (!t.isEmptyList()) {
            count++;
            t = (Struct) t.arg[1].getTerm();
        }
        return count;
    }
    
    
    public Iterator<? extends Term> listIterator() {
        if (!isList())
            throw new UnsupportedOperationException("The structure " + this + " is not a list.");
        return new StructIterator(this);
    }
    
   
    Struct toList() {
        Struct t = new Struct();
        for(int c = arity - 1;c >= 0;c--) {
            t = new Struct(arg[c].getTerm(),t);
        }
        return new Struct(new Struct(name),t);
    }
    
    Struct fromList() {
        Term ft = arg[0].getTerm();
        if (!ft.isAtom()) {
            return null;
        }
        Struct at = (Struct) arg[1].getTerm();
        LinkedList<Term> al = new LinkedList<Term>();
        while (!at.isEmptyList()) {
            if (!at.isList()) {
                return null;
            }
            al.addLast(at.getTerm(0));
            at = (Struct) at.getTerm(1);
        }
        return new Struct(((Struct) ft).name, al);
    }
    
   
    public void append(Term t) {
        if (isEmptyList()) {
            name = ".";
            arity = 2;
            predicateIndicator = name + "/" + arity; /* Added by Paolo Contessi */
            arg = new Term[arity];
            arg[0] = t; arg[1] = new Struct();
        } else if (arg[1].isList()) {
            ((Struct) arg[1]).append(t);
        } else {
            arg[1] = t;
        }
    }
    
  
    void insert(Term t) {
        Struct co=new Struct();
        co.arg[0]=arg[0];
        co.arg[1]=arg[1];
        arg[0] = t;
        arg[1] = co;
    }
    
    boolean unify(List<Var> vl1,List<Var> vl2,Term t) {
        // In fase di unificazione bisogna annotare tutte le variabili della struct completa.
        t = t.getTerm();
        if (t instanceof Struct) {
            Struct ts = (Struct) t;
            if ( arity == ts.arity && name.equals(ts.name)) {
                for (int c = 0;c < arity;c++) {
                    if (!arg[c].unify(vl1,vl2,ts.arg[c])) {
                        return false;
                    }
                }
                return true;
            }
        } else if (t instanceof Var) {
            return t.unify(vl2, vl1, this);
        }
        return false;
    }
    
    
    public void free() {}
   
    void setPrimitive(PrimitiveInfo b) {
        primitive = b;
    }
    
        public PrimitiveInfo getPrimitive() {
        return primitive;
    }
    
   
    public boolean isPrimitive() {
        return primitive != null;
    }
    
    public String toString() {
        // empty list case
        if (isEmptyList()) return "[]";
        // list case
        if (name.equals(".") && arity == 2) {
            return ("[" + toString0() + "]");
        } else if (name.equals("{}")) {
            return ("{" + toString0_bracket() + "}");
        } else {
            String s = (Parser.isAtom(name) ? name : "'" + name + "'");
            if (arity > 0) {
                s = s + "(";
                for (int c = 1;c < arity;c++) {
                    if (!(arg[c - 1] instanceof Var)) {
                        s = s + arg[c - 1].toString() + ",";
                    } else {
                        s = s + ((Var)arg[c - 1]).toStringFlattened() + ",";
                    }
                }
                if (!(arg[arity - 1] instanceof Var)) {
                    s = s + arg[arity - 1].toString() + ")";
                } else {
                    s = s + ((Var)arg[arity - 1]).toStringFlattened() + ")";
                }
            }
            return s;
        }
    }
    
    private String toString0() {
        Term h = arg[0].getTerm();
        Term t = arg[1].getTerm();
        if (t.isList()) {
            Struct tl = (Struct) t;
            if (tl.isEmptyList()) {
                return h.toString();
            }
            if (h instanceof Var) {
                return (((Var)h).toStringFlattened() + "," + tl.toString0());
            } else {
                return (h.toString() + "," + tl.toString0());
            }
        } else {
            String h0;
            String t0;
            if (h instanceof Var) {
                h0 = ((Var)h).toStringFlattened();
            } else {
                h0 = h.toString();
            }
            if (t instanceof Var) {
                t0 = ((Var)t).toStringFlattened();
            } else {
                t0 = t.toString();
            }
            return (h0 + "|" + t0);
        }
    }
    
    private String toString0_bracket() {
        if (arity == 0) {
            return "";
        } else if (arity==1 && !((arg[0] instanceof Struct) && ((Struct)arg[0]).getName().equals(","))){
            return arg[0].getTerm().toString();
        } else {
            // comma case 
            Term head = ((Struct)arg[0]).getTerm(0);
            Term tail = ((Struct)arg[0]).getTerm(1);
            StringBuffer buf = new StringBuffer(head.toString());
            while (tail instanceof Struct && ((Struct)tail).getName().equals(",")){
                head = ((Struct)tail).getTerm(0);
                buf.append(","+head.toString());
                tail = ((Struct)tail).getTerm(1);
            }
            buf.append(","+tail.toString());
            return buf.toString();
        }
    }
    
    private String toStringAsList(OperatorManager op) {
        Term h = arg[0];
        Term t = arg[1].getTerm();
        if (t.isList()) {
            Struct tl = (Struct)t;
            if (tl.isEmptyList()){
                return h.toStringAsArgY(op,0);
            }
            return (h.toStringAsArgY(op,0) + "," + tl.toStringAsList(op));
        } else {
            return (h.toStringAsArgY(op,0) + "|" + t.toStringAsArgY(op,0));
        }
    }
    
    String toStringAsArg(OperatorManager op, int prio, boolean x) {
        int p = 0;
        String v = "";
        
        if (name.equals(".") && arity == 2) {
            if (arg[0].isEmptyList()) {
                return("[]");
            } else {
                return("[" + toStringAsList(op) + "]");
            }
        } else if (name.equals("{}")) {
            return("{" + toString0_bracket() + "}");
        }
        
        if (arity == 2) {
            if ((p = op.opPrio(name,"xfx")) >= OperatorManager.OP_LOW) {
                return(
                        (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                        arg[0].toStringAsArgX(op,p) +
                        " " + name + " "      +
                        arg[1].toStringAsArgX(op,p) +
                        (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name,"yfx")) >= OperatorManager.OP_LOW) {
                return(
                        (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                        arg[0].toStringAsArgY(op,p) +
                        " " + name + " "      +
                        arg[1].toStringAsArgX(op,p) +
                        (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name,"xfy")) >= OperatorManager.OP_LOW) {
                if (!name.equals(",")) {
                    return(
                            (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                            arg[0].toStringAsArgX(op,p) +
                            " " + name + " "      +
                            arg[1].toStringAsArgY(op,p) +
                            (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
                } else {
                    return(
                            (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                            arg[0].toStringAsArgX(op,p) +
                            //",\n\t"+
                            ","+
                            arg[1].toStringAsArgY(op,p) +
                            (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
                }
            }
        }
        else if (arity == 1) {
            if ((p = op.opPrio(name,"fx")) >= OperatorManager.OP_LOW) {
                return(
                        (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                        name + " "            +
                        arg[0].toStringAsArgX(op,p) +
                        (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name,"fy")) >= OperatorManager.OP_LOW) {
                return(
                        (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                        name + " "            +
                        arg[0].toStringAsArgY(op,p) +
                        (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name,"xf")) >= OperatorManager.OP_LOW) {
                return(
                        (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                        arg[0].toStringAsArgX(op,p) +
                        " " + name + " "      +
                        (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
            if ((p = op.opPrio(name,"yf")) >= OperatorManager.OP_LOW) {
                return(
                        (((x && p >= prio) || (!x && p > prio)) ? "(" : "") +
                        arg[0].toStringAsArgY(op,p) +
                        " " + name + " "      +
                        (((x && p >= prio) || (!x && p > prio)) ? ")" : ""));
            }
        }
        v = (Parser.isAtom(name) ? name : "'" + name + "'");
        if (arity == 0) {
            return v;
        }
        v = v + "(";
        for (p = 1;p < arity;p++) {
            v = v + arg[p - 1].toStringAsArgY(op,0) + ",";
        }
        v = v + arg[arity - 1].toStringAsArgY(op,0);
        v = v + ")";
        return v;
    }
    
    public Term iteratedGoalTerm() {
        if (name.equals("^") && arity == 2) {
            Term goal = getTerm(1);
            return goal.iteratedGoalTerm();
        } else
            return super.iteratedGoalTerm();
    }
    
    /*Castagna 06/2011*/
    @Override
	public void accept(TermVisitor tv) {
		tv.visit(this);
	}
}