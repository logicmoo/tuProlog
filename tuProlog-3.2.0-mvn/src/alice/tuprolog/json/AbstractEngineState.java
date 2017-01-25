package alice.tuprolog.json;

import alice.tuprolog.Term;

//Alberto
public abstract class AbstractEngineState {

	Term query;
	int nAskedResults;
	boolean hasOpenAlternatives;
	
	long serializationTimestamp;
	
	public abstract void setQuery(Term query);
	public abstract Term getQuery();
	
	public abstract void setNumberAskedResults(int nResultAsked);
	public abstract int getNumberAskedResults();
	
	public abstract void setHasOpenAlternatives(boolean hasOpenAlternatives);
	public abstract boolean hasOpenAlternatives();
	
	public abstract long getSerializationTimestamp();
	public abstract void setSerializationTimestamp(long serializationTimestamp);
	
}
