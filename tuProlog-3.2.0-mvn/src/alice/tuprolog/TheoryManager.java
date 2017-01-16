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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import alice.tuprolog.InvalidTermException;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.json.AbstractEngineState;
import alice.tuprolog.json.FullEngineState;
import alice.util.Tools;


public class TheoryManager implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ClauseDatabase dynamicDBase;
	private ClauseDatabase staticDBase;
	private ClauseDatabase retractDBase;
	private Prolog engine;
	private PrimitiveManager primitiveManager;
	private Stack<Term> startGoalStack;
	Theory lastConsultedTheory;

	public void initialize(Prolog vm) {
		dynamicDBase = new ClauseDatabase();
		staticDBase = new ClauseDatabase();
		retractDBase = new ClauseDatabase();
		lastConsultedTheory = new Theory();
		engine = vm;
		primitiveManager = engine.getPrimitiveManager();
	}

	
	public synchronized void assertA(Struct clause, boolean dyn, String libName, boolean backtrackable) {
		ClauseInfo d = new ClauseInfo(toClause(clause), libName);
		String key = d.getHead().getPredicateIndicator();
		if (dyn) {
			dynamicDBase.addFirst(key, d);
			if (staticDBase.containsKey(key)) {
				engine.warn("A static predicate with signature " + key + " has been overriden.");
			}
		} else
			staticDBase.addFirst(key, d);
		engine.spy("INSERTA: " + d.getClause() + "\n");
	}

	
	public synchronized void assertZ(Struct clause, boolean dyn, String libName, boolean backtrackable) {
		ClauseInfo d = new ClauseInfo(toClause(clause), libName);
		String key = d.getHead().getPredicateIndicator();
		if (dyn) {
			dynamicDBase.addLast(key, d);
			if (staticDBase.containsKey(key)) {
				engine.warn("A static predicate with signature " + key + " has been overriden.");
			}
		} else
			staticDBase.addLast(key, d);
		engine.spy("INSERTZ: " + d.getClause() + "\n");
	}

	
	public synchronized ClauseInfo retract(Struct cl) {
		Struct clause = toClause(cl);
		Struct struct = ((Struct) clause.getArg(0));
		FamilyClausesList family = dynamicDBase.get(struct.getPredicateIndicator());
		ExecutionContext ctx = engine.getEngineManager().getCurrentContext();
		
		/*creo un nuovo clause database x memorizzare la teoria all'atto della retract 
		 * questo lo faccio solo al primo giro della stessa retract 
		 * (e riconosco questo in base all'id del contesto)
		 * sara' la retract da questo db a restituire il risultato
		 */    
		FamilyClausesList familyQuery;
	    if(!retractDBase.containsKey("ctxId "+ctx.getId())){
	    	familyQuery=new FamilyClausesList();
	    	for (int i = 0; i < family.size(); i++) {
	 	       familyQuery.add(family.get(i));
	 	    }
	    	retractDBase.put("ctxId "+ctx.getId(), familyQuery);
	    }
	   else {
		   familyQuery=retractDBase.get("ctxId "+ctx.getId());
	   }
		
	    if (familyQuery == null)
			return null;
		//fa la retract dalla teoria base
		if (family != null){
			for (Iterator<ClauseInfo> it = family.iterator(); it.hasNext();) {
				ClauseInfo d = it.next();
				if (clause.match(d.getClause())) {
					it.remove();
					break;
				}
			}
		}
		//fa la retract dal retract db
		for (Iterator<ClauseInfo> i = familyQuery.iterator(); i.hasNext();) {
			ClauseInfo d = i.next();
			if (clause.match(d.getClause())) {
				i.remove();
				engine.spy("DELETE: " + d.getClause() + "\n");
				return new ClauseInfo(d.getClause(), null);
			}
		}
		return null;
	}

	
	public synchronized boolean abolish(Struct pi) {		
		if (!(pi instanceof Struct) || !pi.isGround() || !(pi.getArity() == 2))
			throw new IllegalArgumentException(pi + " is not a valid Struct");
		if(!pi.getName().equals("/"))
				throw new IllegalArgumentException(pi + " has not the valid predicate name. Espected '/' but was " + pi.getName());
		
		String arg0 = Tools.removeApices(pi.getArg(0).toString());
		String arg1 = Tools.removeApices(pi.getArg(1).toString());
		String key =  arg0 + "/" + arg1;
		List<ClauseInfo> abolished = dynamicDBase.abolish(key); /* Reviewed by Paolo Contessi: LinkedList -> List */
		if (abolished != null)
			engine.spy("ABOLISHED: " + key + " number of clauses=" + abolished.size() + "\n");
		return true;
	}

	
	public synchronized List<ClauseInfo> find(Term headt) {
		if (headt instanceof Struct) {
			List<ClauseInfo> list = dynamicDBase.getPredicates(headt);
			if (list.isEmpty())
				list = staticDBase.getPredicates(headt);
			return list;
		}

		if (headt instanceof Var){
			throw new RuntimeException();
		}
		return new LinkedList<ClauseInfo>();
	}

	public synchronized void consult(Theory theory, boolean dynamicTheory, String libName) throws InvalidTheoryException {
		startGoalStack = new Stack<Term>();
		int clause = 1;
		try {
			for (Iterator<? extends Term> it = theory.iterator(engine); it.hasNext();) {
				clause++;	
				Struct d = (Struct) it.next();
				if (!runDirective(d))
					assertZ(d, dynamicTheory, libName, true);
			}
		} catch (InvalidTermException e) {
			throw new InvalidTheoryException(e.getMessage(), clause, e.line, e.pos);
		}

		if (libName == null)
			lastConsultedTheory = theory;
	}

	public void rebindPrimitives() {
		for (ClauseInfo d:dynamicDBase){
			for(AbstractSubGoalTree sge:d.getBody()){
				Term t = ((SubGoalElement)sge).getValue();
				primitiveManager.identifyPredicate(t);
			}
		}
	}

	
	public synchronized void clear() {
		dynamicDBase = new ClauseDatabase();
	}

	
	public synchronized void removeLibraryTheory(String libName) {
		for (Iterator<ClauseInfo> allClauses = staticDBase.iterator(); allClauses.hasNext();) {
			ClauseInfo d = allClauses.next();
			if (d.libName != null && libName.equals(d.libName))
			{
				try 
				{
					// Rimuovendolo da allClauses si elimina solo il valore e non la chiave
					allClauses.remove();
				}
				catch (Exception e){}
			}
		}
	}


	private boolean runDirective(Struct c) {
		if ("':-'".equals(c.getName()) || ":-".equals(c.getName()) && c.getArity() == 1 && c.getTerm(0) instanceof Struct) {
			Struct dir = (Struct) c.getTerm(0);
			try {
				if (!primitiveManager.evalAsDirective(dir))
					engine.warn("The directive " + dir.getPredicateIndicator() + " is unknown.");
			} catch (Throwable t) {
				engine.warn("An exception has been thrown during the execution of the " +
						dir.getPredicateIndicator() + " directive.\n" + t.getMessage());
			}
			return true;
		}
		return false;
	}

	private Struct toClause(Struct t) {		//PRIMITIVE
		// TODO bad, slow way of cloning. requires approx twice the time necessary
		t = (Struct) Term.createTerm(t.toString(), this.engine.getOperatorManager());
		if (!t.isClause())
			t = new Struct(":-", t, new Struct("true"));
		primitiveManager.identifyPredicate(t);
		return t;
	}

	public synchronized void solveTheoryGoal() {
		Struct s = null;
		while (!startGoalStack.empty()) {
			s = (s == null) ?
					(Struct) startGoalStack.pop() :
						new Struct(",", (Struct) startGoalStack.pop(), s);
		}
		if (s != null) {
			try {
				engine.solve(s);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	
	public synchronized void addStartGoal(Struct g) {
		startGoalStack.push(g);
	}

	
	synchronized boolean save(OutputStream os, boolean onlyDynamic) {
		try {
			new DataOutputStream(os).writeBytes(getTheory(onlyDynamic));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	
	public synchronized String getTheory(boolean onlyDynamic) {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<ClauseInfo> dynamicClauses = dynamicDBase.iterator(); dynamicClauses.hasNext();) {
			ClauseInfo d = dynamicClauses.next();
			buffer.append(d.toString(engine.getOperatorManager())).append("\n");
		}
		if (!onlyDynamic)
			for (Iterator<ClauseInfo> staticClauses = staticDBase.iterator(); staticClauses.hasNext();) {
				ClauseInfo d = staticClauses.next();
				buffer.append(d.toString(engine.getOperatorManager())).append("\n");
			}
		return buffer.toString();
	}

	public synchronized Theory getLastConsultedTheory() {
		return lastConsultedTheory;
	}
	
	public void clearRetractDB() {
		this.retractDBase=new ClauseDatabase();
	}

	//Alberto
	public boolean checkExistance(String predicateIndicator){
		return (this.dynamicDBase.containsKey(predicateIndicator) || this.staticDBase.containsKey(predicateIndicator));
	}
	
	//Alberto
	public void serializeKnowledgeBase(AbstractEngineState brain){
		if(brain instanceof FullEngineState){
			((FullEngineState) brain).setDynamicDBase(getTheory(true));
			((FullEngineState) brain).setLibraries(engine.getCurrentLibraries());
		}
		serializeTimestamp(brain);
	}
		
	//Alberto
	private void serializeTimestamp(AbstractEngineState brain) {
		brain.setSerializationTimestamp(System.currentTimeMillis());
	}

	//Alberto
	public void reloadKnowledgeBase(FullEngineState brain) {
		try {
			engine.setTheory(new Theory(brain.getDynamicDBase()));
		} catch (InvalidTheoryException e) {}
	}
}