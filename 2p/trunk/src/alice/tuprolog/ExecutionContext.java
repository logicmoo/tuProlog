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

import alice.tuprolog.TuStruct;
import alice.tuprolog.SubGoalId;
import alice.util.OneWayList;


/**
 * 
 * @author Alex Benini
 */
public class ExecutionContext {
    
    private int id;
    int depth;
    TuStruct currentGoal;
    ExecutionContext fatherCtx;
    SubGoalId fatherGoalId;
    TuStruct clause;
    TuStruct headClause;
    SubGoalStore goalsToEval;
    OneWayList<List<TuVar>> trailingVars;
    OneWayList<List<TuVar>> fatherVarsList;
    ChoicePointContext choicePointAfterCut;
    boolean haveAlternatives;
    
    ExecutionContext(int id) {
        this.id=id;
    }
    
    public int getId() { return id; }
    
    @Override
	public String toString(){
        return "        id: "+id+"\n"+
        "      currentGoal: "+currentGoal+"\n"+
        "           clause: "+clause+"\n"+
        "     subGoalStore: "+goalsToEval+"\n"+
        "     trailingVars: "+trailingVars+"\n";
    }
    
    public int getDepth() {
        return depth;
    }
    
    public TuStruct getCurrentGoal() {
        return currentGoal;
    }
    
    public SubGoalId getFatherGoalId() {
        return fatherGoalId;
    }
    
    public TuStruct getClause() {
        return clause;
    }
    
    public TuStruct getHeadClause() {
        return headClause;
    }
    
    public SubGoalStore getSubGoalStore() {
        return goalsToEval;
    }
    
    public List<List<TuVar>> getTrailingVars() {
        ArrayList<List<TuVar>> l = new ArrayList<List<TuVar>>();
        OneWayList<List<TuVar>> t = trailingVars;
        while (t != null) {
            l.add(t.getHead());
            t = t.getTail();
        }
        return l;        
    }
    
    /**
     * Save the state of the parent context to later bring the ExectutionContext
     * objects tree in a consistent state after a backtracking step.
     */
    void saveParentState() {
        if (fatherCtx != null) {
            fatherGoalId = fatherCtx.goalsToEval.getCurrentGoalId();
            fatherVarsList = fatherCtx.trailingVars;
        }
    }
   
    /**
     * If no open alternatives, no other term to execute and
     * current context doesn't contain as current goal a catch or java_catch predicate ->
     * current context no more needed ->
     * reused to execute g subgoal =>
     * got TAIL RECURSION OPTIMIZATION!   
     */
   
    //Alberto
    boolean tryToPerformTailRecursionOptimization(TuEngine e)
    {
    	if(!haveAlternatives && e.currentContext.goalsToEval.getCurSGId() == null && !e.currentContext.goalsToEval.haveSubGoals() && !(e.currentContext.currentGoal.fname().equalsIgnoreCase("catch") || e.currentContext.currentGoal.fname().equalsIgnoreCase("java_catch")))
    	{
    		fatherCtx = e.currentContext.fatherCtx;
    		depth = e.currentContext.depth;
    		return true;
    	}
    	else
    		return false;
    }

    //Alberto
	void updateContextAndDepth(TuEngine e)
	{
		fatherCtx = e.currentContext;
        depth = e.currentContext.depth +1; 
	}
}