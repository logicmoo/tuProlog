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

import alice.tuprolog.TermVisitor;
import alice.tuprolog.json.JSONSerializerManager;
import alice.util.OneWayList;

public abstract class Term implements Serializable {
	
	private static final long serialVersionUID = 1L;

    // true and false constants
    public static final Term TRUE  = new Struct("true");
    public static final Term FALSE = new Struct("false");   
    
    //boolean isCyclic = false; //Alberto -> da usare quando si supporteranno i termini ciclici
     
    // checking type and properties of the Term
    
    public abstract boolean isNumber();
    
    public abstract boolean isStruct();
    
    public abstract boolean isVar();
   
    public abstract boolean isEmptyList();
    
    public abstract boolean isAtomic();
    
    public abstract boolean isCompound();
    
    public abstract boolean isAtom();
    
    public abstract boolean isList();
    
    public abstract boolean isGround();
    
    public boolean equals(Object t) {
        if (!(t instanceof Term))
            return false;
        return isEqual((Term) t);
    }
    
    public abstract boolean isGreater(Term t);
    
    public boolean isEqual(Term t){ //Alberto
    	return this.toString().equals(t.toString());
    }
    
    public boolean isEqualObject(Term t){ //Alberto
    	if (!(t instanceof Term))
            return false;
    	else 
    		return this == t;
    }
    
    public abstract Term getTerm();
    
    public abstract void free();
    
    abstract long resolveTerm(long count);
    
    public void resolveTerm() {
        resolveTerm(System.currentTimeMillis());
    }
    
    public Term copyGoal(AbstractMap<Var,Var> vars, int idExecCtx) {
        return copy(vars,idExecCtx);
    }
    
    public Term copyResult(Collection<Var> goalVars, List<Var> resultVars) {
        IdentityHashMap<Var,Var> originals = new IdentityHashMap<Var,Var>();
        for (Var key: goalVars) 
        {
            Var clone = new Var();
            if (!key.isAnonymous())
            {
                clone =  new Var(key.getOriginalName()); 
            }
            originals.put(key,clone);
            resultVars.add(clone);
        }
        return copy(originals,new IdentityHashMap<Term,Var>());
    }
    
    abstract Term copy(AbstractMap<Var,Var> vMap, int idExecCtx);
    
    //Alberto
    public abstract Term copyAndRetainFreeVar(AbstractMap<Var,Var> vMap, int idExecCtx);
    
    abstract Term copy(AbstractMap<Var,Var> vMap, AbstractMap<Term,Var> substMap);
    
    public boolean unify(Prolog mediator, Term t1) {
        EngineManager engine = mediator.getEngineManager();
        resolveTerm();
        t1.resolveTerm();
        List<Var> v1 = new LinkedList<Var>(); /* Reviewed by: Paolo Contessi (was: ArrayList()) */
        List<Var> v2 = new LinkedList<Var>(); /* Reviewed by: Paolo Contessi (was: ArrayList()) */
        boolean ok = unify(v1,v2,t1);
        if (ok) {
            ExecutionContext ec = engine.getCurrentContext();
            if (ec != null) {
                int id = (engine.getEnv()==null)? Var.PROGRESSIVE : engine.getEnv().nDemoSteps;
                // Update trailingVars
                ec.trailingVars = new OneWayList<List<Var>>(v1,ec.trailingVars);
                // Renaming after unify because its utility regards not the engine but the user
                int count = 0;
                for(Var v:v1){
                    v.rename(id,count);
                    if(id>=0){
                        id++;
                    }else{
                        count++;
                    }
                }
                for(Var v:v2){
                    v.rename(id,count);
                    if(id>=0){
                        id++;
                    }else{
                        count++;
                    }
                }
            }
            return true;
        }
        Var.free(v1);
        Var.free(v2);
    	return false;
    }
    
    public boolean match(Term t) {
        resolveTerm();
        t.resolveTerm();
        List<Var> v1 = new LinkedList<Var>();
        List<Var> v2 = new LinkedList<Var>();
        boolean ok = unify(v1,v2,t);
        Var.free(v1);
        Var.free(v2);
        return ok;
    }
    
    abstract boolean unify(List<Var> varsUnifiedArg1, List<Var> varsUnifiedArg2, Term t);
    
    public static Term createTerm(String st) {
        return Parser.parseSingleTerm(st);
    }
    
    public static Term parse(String st) {
        return Term.createTerm(st);
    }
    
    public static Term createTerm(String st, OperatorManager op) {
        return Parser.parseSingleTerm(st, op);
    }
    
    public static Term parse(String st, OperatorManager op) {
        return Term.createTerm(st, op);
    }
    
    public static java.util.Iterator<Term> getIterator(String text) {
        return new Parser(text).iterator();
    }
    
    String toStringAsArgX(OperatorManager op,int prio) {
        return toStringAsArg(op,prio,true);
    }
    
    String toStringAsArgY(OperatorManager op,int prio) {
        return toStringAsArg(op,prio,false);
    }
    
    String toStringAsArg(OperatorManager op,int prio,boolean x) {
        return toString();
    }
    
    public Term iteratedGoalTerm() {
        return this;
    }
    
	public abstract void accept(TermVisitor tv);
	
	//Alberto
	public String toJSON(){
		return JSONSerializerManager.toJSON(this);
	}
		
	//Alberto
	public static Term fromJSON(String jsonString){
		if(jsonString.contains("Var")) {
			return JSONSerializerManager.fromJSON(jsonString, Var.class);	
		} else if(jsonString.contains("Struct")) {
			return JSONSerializerManager.fromJSON(jsonString, Struct.class);
		} else if(jsonString.contains("Double")) {
			return JSONSerializerManager.fromJSON(jsonString, Double.class);
		} else if(jsonString.contains("Int")) {
			return JSONSerializerManager.fromJSON(jsonString, Int.class);
		} else if(jsonString.contains("Long")) {
			return JSONSerializerManager.fromJSON(jsonString, Long.class);
		} else if(jsonString.contains("Float")) {
			return JSONSerializerManager.fromJSON(jsonString, Float.class);
		} else
			return null;
	}
}