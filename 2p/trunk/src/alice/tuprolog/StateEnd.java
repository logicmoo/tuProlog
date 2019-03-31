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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Benini
 *
 * End state of demostration.
 */
public class StateEnd extends TuState {
    
    private int endState;    
    private TuStruct goal;
    private List<TuVar> vars;
    
    /**
     * Constructor
     * @param end Terminal state of computation
     */
    public StateEnd(EngineRunner c, int end) {
    	this.c=c;
        endState = end;
    }
    
    public int getResultDemo() {
        return endState;
    }
    
    public TuStruct getResultGoal() {
        return goal;
    }
    
    public List<TuVar> getResultVars() {
        return vars;
    }
    
    @Override
	public String toString() {
        switch(endState){
        	case EngineRunner.FALSE   : return "FALSE";
        	case EngineRunner.TRUE    : return "TRUE";
        	case EngineRunner.TRUE_CP : return "TRUE_CP";
        	default                   : return "HALT";
        }  
    }
    
    @Override
	void doJob(TuEngine e) {	
        vars = new ArrayList<TuVar>();
        goal = (TuStruct)e.startGoal.copyResult(e.goalVars,vars);  
    }
    
}