package alice.tuprologx.eclipse.core;

import java.util.Vector;

public class PrologQuery{

	private String query;
	private Vector<PrologQueryScope> scopes;
	private Vector<PrologQueryResult> results;

	public PrologQuery()
	{
		this.query = "";
	}
	
	public PrologQuery(String Query)
	{
		this.query = Query;
	}
	
	public void setQuery(String query)
	{
		this.query = query;
	}
	
	public String getQuery()
	{
		return this.query;
	}
	
	public Vector<PrologQueryScope> getAllScopes() {
		return scopes;
	}
	
	public void resetResults()
	{
		results = new Vector<PrologQueryResult>();
	}
	
	public Vector<PrologEngine> getAllEngines()
	{
		Vector<PrologEngine> engines = new Vector<PrologEngine>();
		if(scopes != null)
		{
			for( int i = 0; i < scopes.size(); i++)
			{
				PrologEngine engine = scopes.get(i).getEngine();
				if (!engines.contains(engine))
				{
					engines.add(engine);
				}
			}
		}
		return engines;
	}
	
	public PrologQueryScope getEngineScope(PrologEngine engine)
	{
		if(scopes != null)
		{
			for( int i = 0; i < scopes.size(); i++)
			{
				if(scopes.get(i).getEngine() == engine)
				{
					return scopes.get(i);
				}
			}
		}
		
		return null;
	}
	
	public Vector<PrologQueryResult> getEngineSolutions(PrologEngine engine)
	{
		Vector<PrologQueryResult> res = new Vector<PrologQueryResult>();
		if( results != null)
		{
			for( int i = 0; i < results.size(); i++)
			{
				if(results.get(i).getEngine().equals(engine))
				{
					res.add(results.get(i));
				}
			}
		}
		return res;
	}

	public void addScope(PrologQueryScope scope) {
		if(this.scopes == null)
		{
			this.scopes = new Vector<PrologQueryScope>();
		}
		this.scopes.add(scope);
	}
	
	public Vector<PrologQueryResult> getResults()
	{
		return results;
	}
	
	public void addResult(PrologQueryResult result)
	{
		if(results == null)
		{
			this.results = new Vector<PrologQueryResult>();
		}
		if(!results.contains(result))
		{
			this.results.add(result);
		}
	}
	
	public void removeResult(PrologQueryResult result)
	{
		results.remove(result);
	}
}