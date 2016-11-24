package alice.tuprolog.json;

import alice.tuprolog.ClauseDatabase;
import alice.tuprolog.Term;

//Alberto
public class SerializableEngineState {
	
	private ClauseDatabase dynamicDBase;
	private ClauseDatabase retractDBase;
	
	private Term query;
	private int nAskedResults;
	
	private String[] libraries;
	
	private boolean hasOpenAlternatives;
	
	public ClauseDatabase getDynamicDBase() {
		return dynamicDBase;
	}
	
	public void setDynamicDBase(ClauseDatabase dynamicDBase) {
		this.dynamicDBase = dynamicDBase;
	}

	public ClauseDatabase getRetractDBase() {
		return retractDBase;
	}

	public void setRetractDBase(ClauseDatabase retractDBase) {
		this.retractDBase = retractDBase;
	}

	public void setQuery(Term query) {
		this.query = query;
	}
	
	public Term getQuery(){
		return this.query;
	}

	public void setNumberAskedResults(int nResultAsked) {
		this.nAskedResults = nResultAsked;
	}
	
	public int getNumberAskedResults(){
		return this.nAskedResults;
	}
	
	public void setLibraries(String[] libraries){
		this.libraries = libraries;
	}

	public String[] getLibraries() {
		return this.libraries;
	}

	public void setHasOpenAlternatives(boolean hasOpenAlternatives) {
		this.hasOpenAlternatives = hasOpenAlternatives;
	}
	
	public boolean  hasOpenAlternatives(){
		return this.hasOpenAlternatives;
	}
	
}