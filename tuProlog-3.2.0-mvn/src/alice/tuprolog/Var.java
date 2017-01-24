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
import alice.tuprolog.TermVisitor;


public class Var extends Term {

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
	private StringBuilder completeName;    /* Reviewed by Paolo Contessi */
	private Term   link;                   /* link is used for unification process */
	private long   internalTimestamp;      /* internalTimestamp is used for fix vars order (resolveTerm()) */
	private int    ctxid;                  /* id of ExecCtx owners of this var util for renaming*/

	//Alberto
	private long fingerPrint; //fingerPrint is a unique id (per run) used for var comparison

	//Alberto
	static long getFingerprint(){ //called by Var constructors
		fingerprint++;
		return fingerprint;
	}
	
	
	public Var(String n) {
		link = null;
		ctxid = Var.ORIGINAL; //no execCtx owners
		internalTimestamp = 0;
		fingerPrint = getFingerprint();
		if (n.equals(ANY)) {
			name = null;
			completeName = new StringBuilder();
		} else if (Character.isUpperCase(n.charAt(0))||
				(n.startsWith(ANY))) {
			name = n;
			completeName = new StringBuilder(n);
		} else {
			throw new InvalidTermException("Illegal variable name: " + n);
		}
	}

	
	public Var() {
		name = null;
		completeName = new StringBuilder();
		link = null;
		ctxid = Var.ORIGINAL;
		internalTimestamp = 0;
		fingerPrint = getFingerprint();
	}

	
	private Var(String n, int id, int alias, long count/*, boolean isCyclic*/) {
		name = n;
		completeName = new StringBuilder();
		internalTimestamp = count;
		//this.isCyclic = isCyclic;
		fingerPrint = getFingerprint();
		link  = null;
		if(id < 0) id = Var.ORIGINAL;
		rename(id,alias);
	}

	
	void rename(int idExecCtx, int count) { /* Reviewed by Paolo Contessi */
		ctxid = idExecCtx;

		if (ctxid > Var.ORIGINAL) {
			completeName = completeName
					.delete(0, completeName.length())
					.append(name).append("_e").append(ctxid);
		}

		else if (ctxid == ORIGINAL) {
			completeName = completeName
					.delete(0, completeName.length())
					.append(name);
		}

		else if (ctxid == PROGRESSIVE) {
			completeName = completeName
					.delete(0, completeName.length())
					.append("_").append(count);
		}
	}

	
	@Override
	Term copy(AbstractMap<Var,Var> vMap, int idExecCtx) {
		Term tt = getTerm();
		if (tt == this) {
			Var v = (Var)(vMap.get(this));
			if (v == null) {
				//No occurence of v before
				v = new Var(name,idExecCtx,0,internalTimestamp/*, this.isCyclic*/);
				vMap.put(this,v);
			}
			return v;
		} else {
			return tt.copy(vMap, idExecCtx);
		}
	}
	
	@Override //Alberto 
	public Term copyAndRetainFreeVar(AbstractMap<Var,Var> vMap, int idExecCtx) {
		Term tt = getTerm();
		if (tt == this) {
			Var v = (Var)(vMap.get(this));
			if (v == null) {
				//No occurence of v before
				v = this; //!!!
				vMap.put(this,v);
			}
			return v;
		} else {
			return tt.copy(vMap, idExecCtx);
		}
	}

	
	@Override
	Term copy(AbstractMap<Var,Var> vMap, AbstractMap<Term,Var> substMap) {
		Var v;
		Object temp = vMap.get(this);
		if (temp == null) {
			v = new Var(null,Var.PROGRESSIVE,vMap.size(),internalTimestamp/*, this.isCyclic*/);
			vMap.put(this,v);
		} else {
			v = (Var) temp;
		}
		
		//if(v.isCyclic) //Alberto
		//	return v;
		
		Term t = getTerm();
		if (t instanceof Var) {
			Object tt = substMap.get(t);
			if (tt == null) {
				substMap.put(t,v);
				v.link = null;
			} else {
				v.link = (tt != v) ? (Var)tt : null;
			}
		}
		if (t instanceof Struct) {
			v.link = t.copy(vMap,substMap);
		}
		if (t instanceof Number) v.link = t;
		return v;
	}

	
	public void free() {
		link = null;
	}

	
	public static void free(List<Var> varsUnified) {
		for(Var v:varsUnified){
			v.free();                
		}
	}

	public String getName() {
		if (name!=null) {
			return completeName.toString();
		} else {
			return ANY;
		}
	}

	
	public String getOriginalName() {
		if (name!=null) {
			return name;
		} else {
			return ANY + ""+this.fingerPrint; //Alberto
		}
	}

	
	public Term getTerm() {
		Term tt = this;
		Term t  = link;
		while (t != null ) {
			tt = t;
			if (t instanceof Var) {
				t  = ((Var)t).link;
			} else {
				break;
			}
		}
		return tt;
	}

	
	public Term getLink() {
		return link;
	}

	
	void setLink(Term l) {
		link = l;
	}

	
	void setInternalTimestamp(long t) {
		internalTimestamp = t;
	}

	public boolean isNumber() {
		return false;
	}

	public boolean isStruct() {
		return false;
	}

	public boolean isVar() {
		return true;
	}

	public boolean isEmptyList() {
		Term t=getTerm();
		if (t==this) {
			return false;
		} else {
			return t.isEmptyList();
		}
	}

	public boolean isAtomic() {
		Term t=getTerm();
		if (t==this) {
			return false;
		} else {
			return t.isAtomic();
		}
	}

	public boolean isCompound() {
		Term t=getTerm();
		if (t==this) {
			return false;
		} else {
			return t.isCompound();
		}
	}

	public boolean isAtom() {
		Term t=getTerm();
		if (t==this) {
			return false;
		} else {
			return t.isAtom();
		}
	}

	public boolean isList() {
		Term t = getTerm();
		if (t == this)
			return false;
		else
			return t.isList();
	}

	public boolean isGround(){
		Term t=getTerm();
		if (t==this) {
			return false;
		} else {
			return t.isGround();
		}
	}

	
	 public boolean isAnonymous() {
		 return name == null;
	 }

	
	 public boolean isBound() {
		 return link != null;
	 }

	
	 private boolean occurCheck(List<Var> vl, Struct t) {
		 int arity=t.getArity();
		 for (int c = 0;c < arity;c++) {
			 Term at = t.getTerm(c);
			 if (at instanceof Struct) {
				 if (occurCheck(vl, (Struct)at)) {
					 return true;
				 }
			 } else if (at instanceof Var) {
				 Var v = (Var)at;
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

	 long resolveTerm(long count) {
		 Term tt=getTerm();
		 if (tt != this) {
			 return tt.resolveTerm(count);
		 } else {
			 internalTimestamp = count;
			 return count++;
		 }
	 }

	 boolean unify(List<Var> vl1, List<Var> vl2, Term t) {
		 Term tt = getTerm();
		 if(tt == this) {
			 t = t.getTerm();
			 if (t instanceof Var) { 
				 ((Var)t).fingerPrint = this.fingerPrint; //Alberto
				 if (this == t) {
					 try{
						 vl1.add(this);
					 } catch(NullPointerException e) {}
					 return true;
				 }
			 } else if (t instanceof Struct) {
				 boolean choice = FlagManager.isOccurCheckEnabled(); //Alberto
				 if(choice){
					 if(occurCheck(vl2, (Struct)t)){
						 //this.isCyclic = true;  //Alberto -> da usare quando si supporteranno i termini ciclici
						 return false; // da togliere 
					 }
				 } else {
					 checkVar(vl2, t); //Alberto
				 }
			 } else if (!(t instanceof Number)) {
				 return false;
			 }
			 link = t;
			 try {
				 vl1.add(this);                
			 } catch(NullPointerException e) {}
			 return true;
		 } else {
			 return (tt.unify(vl1, vl2, t));
		 }
	 }

	 //Alberto
	 private void checkVar(List<Var> vl, Term t) {
		 Struct st = (Struct)t;
		 int arity=st.getArity();
		 for (int c = 0;c < arity;c++) {
			 Term at = st.getTerm(c);
			 if (at instanceof Var) {
				 Var v = (Var)at;
				 if (v.link == null) {
					 vl.add(v);
				 }
			 } else if(at instanceof Struct) {
				 checkVar(vl, at);
			 } 
		 }
	}

	public boolean isGreater(Term t) {
		 Term tt = getTerm();
		 if (tt == this) {
			 t = t.getTerm();
			 if (!(t instanceof Var)) return false;
			 return fingerPrint > ((Var)t).fingerPrint; //Alberto
		 }
		 else {
			 return tt.isGreater(t);
		 }
	 }
	 
	 public void setName(String s){
		 this.name=s;
	 }

	
	 @Override
	 public String toString() {
		 Term tt = getTerm();
		 if (name != null) {
			 if (tt == this/* || this.isCyclic*/){
				 //if(this.isCyclic) //Alberto
					// return name;
				 return completeName.toString();
			 } else {
				 return (completeName.toString() + " / " + tt.toString());
			 }
		 } else {
			 if (tt == this /*|| this.isCyclic*/) {
				 return ANY + ""+this.fingerPrint; //Alberto
			 } else {
				 return tt.toString();
			 }
		 }
	 }


	 public String toStringFlattened() {
		 Term tt = getTerm();
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
				 return ANY + ""+this.fingerPrint; //Alberto
			 } else {
				 return tt.toString();
			 }
		 }
	 }

	 
	 @Override
	 public void accept(TermVisitor tv) {
		 tv.visit(this);
	 }
}