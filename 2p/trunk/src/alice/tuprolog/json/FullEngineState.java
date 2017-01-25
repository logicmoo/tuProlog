package alice.tuprolog.json;

import java.util.ArrayList;
import java.util.LinkedList;

import alice.tuprolog.Operator;
import alice.tuprolog.Term;

//Alberto
public class FullEngineState extends AbstractEngineState {
	
	@SuppressWarnings("unused")
	private String type = "FullEngineState";
	
	private String[] libraries;
	private ArrayList<String> flags;
	
	private String dynTheory;
	
	private LinkedList<Operator> op;
	
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

	public void setDynTheory(String theory) {
		this.dynTheory = theory;
	}
	
	public String getDynTheory(){
		return this.dynTheory;
	}

	public LinkedList<Operator> getOp() {
		return op;
	}

	public void setOp(LinkedList<Operator> list) {
		this.op = list;
	}

}