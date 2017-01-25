package alice.tuprolog.json;

import java.util.ArrayList;

import alice.tuprolog.Term;

//Alberto
public class FullEngineState extends AbstractEngineState {
	
	@SuppressWarnings("unused")
	private String type = "FullEngineState";
	
	private String dynamicDBase;
	private String[] libraries;
	private ArrayList<String> flags;
	
	public String getDynamicDBase() {
		return dynamicDBase;
	}
	
	public void setDynamicDBase(String dynamicDBase) {
		this.dynamicDBase = dynamicDBase;
	}

	@Override
	public void setQuery(Term query) {
		this.query = query;
	}
	
	@Override
	public Term getQuery(){
		return this.query;
	}

	@Override
	public void setNumberAskedResults(int nResultAsked) {
		this.nAskedResults = nResultAsked;
	}
	
	@Override
	public int getNumberAskedResults(){
		return this.nAskedResults;
	}
	
	public void setLibraries(String[] libraries){
		this.libraries = libraries;
	}

	public String[] getLibraries() {
		return this.libraries;
	}

	@Override
	public void setHasOpenAlternatives(boolean hasOpenAlternatives) {
		this.hasOpenAlternatives = hasOpenAlternatives;
	}
	
	@Override
	public boolean  hasOpenAlternatives(){
		return this.hasOpenAlternatives;
	}

	@Override
	public long getSerializationTimestamp() {
		return serializationTimestamp;
	}

	@Override
	public void setSerializationTimestamp(long serializationTimestamp) {
		this.serializationTimestamp = serializationTimestamp;
	}


	public ArrayList<String> getFlags() {
		return flags;
	}

	public void setFlags(ArrayList<String> flags) {
		this.flags = flags;
	}
	
}