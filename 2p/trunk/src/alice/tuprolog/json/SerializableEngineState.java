package alice.tuprolog.json;

import alice.tuprolog.Term;

//Alberto
public class SerializableEngineState {
	
	private String dynamicDBase;
	
	private Term query;
	private int nAskedResults;
	
	private String[] libraries;
	
	private boolean hasOpenAlternatives;
	
	public String getDynamicDBase() {
		return dynamicDBase;
	}
	
	public void setDynamicDBase(String dynamicDBase) {
		this.dynamicDBase = dynamicDBase;
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