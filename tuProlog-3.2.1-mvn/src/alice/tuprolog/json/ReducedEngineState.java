package alice.tuprolog.json;

import alice.tuprolog.Term;

//Alberto
public class ReducedEngineState extends AbstractEngineState {

	@SuppressWarnings("unused")
	private String type = "ReducedEngineState"; 

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
	
}
