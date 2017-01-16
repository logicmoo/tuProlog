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
import java.io.*;
import java.util.*;

import alice.tuprolog.json.JSONSerializerManager;


public class SolveInfo implements Serializable/*, ISolution<Term,Term,Term>*/  {
	private static final long serialVersionUID = 1L;
    /*
     * possible values returned by step functions
     * and used as eval state flags
     */
    static final int HALT    = EngineRunner.HALT;
    static final int FALSE   = EngineRunner.FALSE;
    static final int TRUE    = EngineRunner.TRUE;
    static final int TRUE_CP = EngineRunner.TRUE_CP;
    
    private int     endState;
    private boolean isSuccess;
    
    private Term query;
    private Struct goal;
    private List<Var> bindings;
    
    
    SolveInfo(Term initGoal){
        query = initGoal;
        isSuccess = false;
    }
    
    SolveInfo(Term initGoal, Struct resultGoal, int resultDemo, List<Var> resultVars) {
        query = initGoal;
        goal = resultGoal;
        bindings = resultVars;
        endState = resultDemo;
        isSuccess = (endState > FALSE);
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
    
    public boolean isHalted() {
        return (endState == HALT);
    }
    
    public boolean hasOpenAlternatives() {
        return (endState == TRUE_CP);
    }
    
    public Term getQuery() {
        return query;
    }
    
    public Term  getSolution() throws NoSolutionException {
        if (isSuccess){
            return goal;
        } else {
            throw new NoSolutionException();
        }
    }
    
    public List<Var> getBindingVars() throws NoSolutionException {
        if (isSuccess){
            return bindings;
        }else {
            throw new NoSolutionException();
        }
    }
    
    public Term getTerm(String varName) throws NoSolutionException, UnknownVarException {
        Term t = getVarValue(varName);
        if (t == null)
            throw new UnknownVarException();
        return t;
    }
   
    public Term getVarValue(String varName) throws NoSolutionException {
        if (isSuccess) {
            Iterator<Var> it = bindings.iterator();
            while (it.hasNext()) {
                Var v = (Var)it.next();
                if (v!=null && v.getName().equals(varName)) {
                    return v.getTerm();
                }
            }
            return null;
        } else
            throw new NoSolutionException();
    }
      
    public String toString() {
        if (isSuccess) {
            StringBuffer st = new StringBuffer("yes");
            if (bindings.size() > 0) {
                st.append(".\n");
            } else {
                st.append(". ");
            }
            Iterator<Var> it = bindings.iterator();
            while(it.hasNext()) {
                Var v = (Var) it.next();
                if (v != null && !v.isAnonymous() && v.isBound() && 
                        (!(v.getTerm() instanceof Var) || (!((Var) (v.getTerm())).getName().startsWith("_")))) {
                    st.append(v);
                    st.append("  ");
                }
            }
            return st.toString().trim();
        } else {
        	/*Castagna 06/2011*/
        	if(endState == EngineRunner.HALT)
        		return ("halt.");
        	else
            return "no.";
        }
    }
    
    //Alberto
  	public String toJSON(){
  		return JSONSerializerManager.toJSON(this);
  	}
  	
  	//Alberto
  	public static SolveInfo fromJSON(String jsonString){
  		return JSONSerializerManager.fromJSON(jsonString, SolveInfo.class);	
  	}
    
}