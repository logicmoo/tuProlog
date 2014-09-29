package alice.tuprologx.eclipse.core;

import java.util.Observable;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.editors.PrologEditor;
import alice.tuprologx.eclipse.util.EventListener;



public class PrologQueryFactory extends Observable {
	/* Questa factory si occupa di gestire e creare le query.
	 * in tal modo è possibile accedere a tutte le informazioni
	 * relative ad una query come, ad esempio, tutte le query associate
	 * ad un motore.
	 */
		
	private static PrologQueryFactory instance;
	Vector<PrologQuery> queries;
	private int queryId;
	private static boolean init;
	private static Theory lastTheory;
	public static PrologQueryFactory getInstance(){
		if (instance == null){
			instance = new PrologQueryFactory();
		}
		return instance;
	}
	
	public PrologQueryFactory()
	{
		queryId = 0;
		queries = new Vector<PrologQuery>();
		try {
			lastTheory = new Theory("");
		} catch (InvalidTheoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init = true;
	}
	
	public void addQuery(PrologQuery query)
	{
		if(!queries.contains(query))
		{
			queries.add(query);
			setChanged();
			notifyObservers();
		}
	}
	
	public Vector<PrologQuery> getQueries(){
		return queries;
	}
	
	public void removeQuery(PrologQuery query){
		queries.remove(query);
		setChanged();
		notifyObservers();
	}
	/**Andrea Mordenti 28/04/2011
	/*Method to execute the query within the current scope*/
	public boolean executeQueryWS(PrologQuery query)
	{		
		IProject currentProject = TuProlog.getActiveProject();

		try {
			currentProject.build(IncrementalProjectBuilder.FULL_BUILD,
					PrologParser.BUILDER_ID, null, null);
		} catch (CoreException e1) {
		}
		
		PrologEngine engine = PrologEngineFactory.getInstance().getEngine(currentProject.getName(),0);
		PrologQueryScope scope = new PrologQueryScope(engine);
		scope.setProject(currentProject);
		/*Add the file open in the PrologEditor to the query scope*/
		scope.addFile(TuProlog.getActiveFile().toString());
		query.addScope(scope);
		query.resetResults();
		Theory scopeTheory = null;
		try {
			scopeTheory = getActiveTheory();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**Andrea Mordenti 18/05/2011
		 *  The new theory, written into the file .pl is added to the
		 *  engine only if ins't the same that the last theory parsed.
		 */
		if(!scopeTheory.toString().equals(lastTheory.toString())){
			engine.setTheory(scopeTheory);
			lastTheory = scopeTheory;
		}
		EventListener listener = new EventListener();
		engine.addOutputListener(listener);
		/*Castagna 06/2011*/
		engine.addExceptionListener(listener);
		/**/
		engine.addSpyListener(listener);
		engine.query(query.getQuery());
		while(engine.hasOpenAlternatives()){
				PrologQueryResult result = new PrologQueryResult(engine);
				result.setSpy(listener.getSpy());
				result.setLost(listener.getSpy());
				result.setOutput(listener.getOutput());
				result.setResult(engine.next());
				query.addResult(result);
			}
			
			PrologQueryResult lastResult = new PrologQueryResult(engine);
			lastResult.setSpy(listener.getSpy());
			lastResult.setLost(listener.getSpy());
			lastResult.setOutput(listener.getOutput());
			/*Castagna 06/2011*/
			lastResult.setException(listener.getException());
			/**/
			lastResult.setResult(engine.next());
			query.addResult(lastResult);
			addQuery(query);
			
			engine.removeSpyListeners();
			engine.removeOutputListeners();
			return true;
	}
	
	/* Francesco Fabbri
	 * 19/05/2011
	 * Execute queries without save
	 */
	protected Theory getActiveTheory() throws Exception {
		
		boolean existsAnEditor = false;
		boolean isStartup = false;
		IEditorReference[] editorReferences = null;
		IEditorPart editor = null;
		IFile file = TuProlog.getActiveFile();

		try {
			editorReferences = TuProlog.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();
		} catch (Exception e) {
			isStartup = true;
		}

		if (!(isStartup)) {
			for (int i = 0; i < editorReferences.length; i++) {
				editor = editorReferences[i].getEditor(false);
				if (editor instanceof PrologEditor) {
					PrologEditor pEditor = (PrologEditor) editor;
					if ((pEditor.getProject().getName().equals(file
							.getProject().getName()))
							&& (pEditor.getEditorInput().getName()
									.equals(file.getName()))) {
						existsAnEditor = true;
						break;
					}

				}
			}

		}		
		if (existsAnEditor) 
			return new Theory(((PrologEditor) (editor)).getText());
		else 
			return new Theory(file.getContents());
	}
}
