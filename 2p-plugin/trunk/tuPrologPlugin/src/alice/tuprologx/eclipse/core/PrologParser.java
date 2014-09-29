package alice.tuprologx.eclipse.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;

import alice.tuprolog.Int;
import alice.tuprolog.Library;
import alice.tuprolog.Number;
import alice.tuprolog.Operator;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.TermVisitor;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import alice.tuprolog.interfaces.IOperatorManager;
import alice.tuprolog.interfaces.IParser;
import alice.tuprolog.interfaces.ParserFactory;
import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.editors.PrologEditor;
import alice.tuprologx.eclipse.properties.PropertyManager;
import alice.tuprologx.eclipse.util.OperatorEvent;
import alice.tuprologx.eclipse.util.OperatorListener;
import alice.tuprologx.eclipse.views.ViewSet;

public class PrologParser extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "tuPrologPlugin.prologParser";
	public static Vector<Theory> t;
	private IParser parser;
	private boolean correct = true;
	private static Vector<String> scope;
	private static boolean allTheories = false;
	@SuppressWarnings({ "rawtypes" })
	private static Vector[] alternativeScope;
	private static String projectName;
	private Vector<Term> terms;
	private IMarker mark;
	private static Theory theory; // la teoria da passare al builder in caso di
									// parse corretto

	public static boolean go = false;
	/**List of Dynamic Operators listener**/
	private ArrayList<OperatorListener> opListeners = new ArrayList<OperatorListener>();
	
	private Vector<Operator> dynOps=null;
	private ViewSet viewSet = new ViewSet();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (go == true) {
			go = false;
			t = new Vector<Theory>();
			alternativeScope = new Vector[PrologEngineFactory.getInstance()
					.getProjectEngines(projectName).size()];
			for (int i = 0; i < alternativeScope.length; i++) {
				alternativeScope[i] = new Vector<String>();
				String tmp = "";
				try {
					tmp = args.get(new Integer(i)).toString();
				} catch (Exception e) {
				}
				StringTokenizer st = new StringTokenizer(tmp, ";");
				while (st.hasMoreTokens()) {
					String t = st.nextToken();
					alternativeScope[i].add(t);
				}
			}
		} else
			alternativeScope = null;

		if (kind == FULL_BUILD) {
			fullBuild(monitor,args);

		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void fullBuild(final IProgressMonitor monitor,Map args) {
		t = new Vector<Theory>();
		IProject project = getProject();
		projectName = project.getName();
		Vector<PrologEngine> engines =PrologEngineFactory.getInstance().getProjectEngines(project.getName());
		for (int j = 0; j < (engines!=null?engines.size():0); j++) {
			PrologEngine engine = PrologEngineFactory.getInstance().getEngine(
					project.getName(), j);
			setListeners(args);
			if (alternativeScope == null) {
				try {
				if (PropertyManager.allTheories(project, engine.getName())) {
					Vector<String> theories = new Vector<String>();
					
						IResource[] resources = project.members();
						for (int i = 0; i < resources.length; i++)
							if ((resources[i] instanceof IFile)
									&& (resources[i].getName().endsWith(".pl")))
								theories.add(resources[i].getName());
					scope = theories;
				} else
					scope = PropertyManager.getTheoriesFromProperties(project,
							engine.getName());
				} catch (Exception e) 
				{
					e.printStackTrace();
				}

			}
			else {
				try {
					scope = alternativeScope[j];
				} catch (Exception e) { }
			}
			
			try {
				terms = new Vector<Term>();
				dynOps = new Vector<Operator>();
				project.accept(new TheoryParser(engine));
				if (correct) {

					Term[] termsArray = new Term[terms.size()];
					for (int i = 0; i < terms.size(); i++)
						termsArray[i] = (terms.elementAt(i));

					Struct struct = new Struct(termsArray);
					try {
						theory = new Theory(struct);
					} catch (Exception e) {
					} // non dovrebbe mai essere lanciata



				} else {
					theory = null;
					correct = true;
				}
				//notify operator List
				notifyOpChanged(new OperatorEvent(this, dynOps));
				t.add(getTheory());
				viewSet.refresh(theory.toString());

			} catch (CoreException e) {
			}

		}
	}
	/*
	 * Marco Prati 18/04/11
	 */

	/**
	 * Adds a listener to operator events
	 *
	 * @param l the listener
	 */
	public synchronized void addOperatorListener(OperatorListener l) {
		opListeners.add(l);
	}
	 /** remove a listener to operator events
	 *
	 * @param l the listener
	 */
	public synchronized void removeOperatorListener(OperatorListener l) {
		opListeners.remove(l);
	}
	 /** Remove All listeners to operator events
	 *
	 */
	public synchronized void removeAllOperatorListener() {
		opListeners.clear();
	}
	
	/**
	 * Gets a copy of current listener list to operator events
	 *
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<OperatorListener> getOperatorListenerList() {
		return (List<OperatorListener>) opListeners.clone();
	}
	/**
	 * Notifies a operatorList Change
	 *
	 * @param e the event
	 */
	protected void notifyOpChanged(OperatorEvent e) {
		Iterator<OperatorListener> it = opListeners.listIterator();
		while (it.hasNext()) {
			it.next().opChanged(e);
		}
	}
	/**
	 * Set Listener for newOp events
	 */
	@SuppressWarnings({"rawtypes" })
	private void setListeners(Map args){
		OperatorListener listener=null;
		if(args!=null){
			listener=(PrologEditor)args.get("Editor");
		}
		if(listener!=null){
			addOperatorListener(listener);
		}
	}
	/////Marco Prati END/////
	
	// metodo che verifica se la risorsa e' una teoria
	private boolean checkTheory(IResource resource) {
		if (resource instanceof IFile && resource.getName().endsWith(".pl")) {
			IFile file = (IFile) resource;
			deleteMarkers(file);
			return true;
		} else
			return false;
	}

	// metodo che verifica se la risorsa fa parte dello scope
	private boolean isTheoryInScope(IResource resource) {
		if (allTheories)
			return true;

		if (scope.contains(resource.getName()))
			return true;
		else
			return false;
	}

	private void deleteMarkers(IResource resource) {
		try {
			resource.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	private void showError(IFile file, String message, int line) {
		try {
			mark = file.createMarker(IMarker.PROBLEM);
			mark.setAttribute(IMarker.LINE_NUMBER, line);
			mark.setAttribute(IMarker.MESSAGE, message);
			mark.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
			mark.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		} catch (CoreException e) {
		}
	}
	
	private void showWarning(IFile file, String message, int line, int start, int length) {
		try {
			mark = file.createMarker(IMarker.PROBLEM);
			mark.setAttribute(IMarker.LINE_NUMBER, line);		
			mark.setAttribute(IMarker.MESSAGE, message);
			mark.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
			mark.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		} catch (CoreException e) {
		}
	}
	
	private String openFile(IFile file) {
		return openFile(file.getLocation().toOSString());
	}

	private String openFile(String path) {
		String ilFile = new String();

		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String linea = new String();
			while ((linea = in.readLine()) != null) {
				ilFile += linea;
				ilFile += System.getProperty("line.separator");
			}
			in.close();
		} catch (IOException e) {
			ilFile = null;
		}

		return ilFile;
	}

	public static Theory getTheory() {
		return theory;
	}

	public static String getProjectName() {
		return projectName;
	}
	/**
	 * Update the theory in its String representation removing
	 * the line that causes error
	 * 
	 * @param oldTh old theory
	 * @param m message within the error is included
	 * @return vector[0] = new Theory
	 * 			vector[1] = line parsed
	 */
	
	private Vector<String> updateTheory(String oldTh){
		int i =0;
		int lineParsed=0;
		String newTh="";
		for(;i<oldTh.length();i++){
			char c = oldTh.charAt(i);
			if(c == '\n') lineParsed++;
			if(c =='.'){
				newTh = oldTh.substring(i+1);
				break;
			}
		}
		Vector<String> out = new Vector<String>();
		out.add(newTh);
		out.add(String.valueOf(lineParsed));
		return out;

	}
	
	class TheoryParser implements IResourceVisitor {
		
		protected PrologEngine engine;
		protected IOperatorManager opManager;
		protected String currentPath;
		
		public TheoryParser(PrologEngine engine) {
			this.engine = engine;
		}
		
		public boolean visit(IResource resource) {
			String theoryText = "";
			int alreadyParsed=0;

			if (checkTheory(resource)) {

				/*
				 * verifico se esiste un editor aperto sulla mia risorsa
				 */
				boolean existsAnEditor = false;
				boolean isStartup = false;
				IEditorReference[] editorReferences = null;
				IEditorPart editor = null;

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
							// String pro=pEditor.getProject().getName();
							// String pro2=resource.getProject().getName();
							// String in=pEditor.getEditorInput().getName();
							// String in2=resource.getName();
							if ((pEditor.getProject().getName().equals(resource
									.getProject().getName()))
									&& (pEditor.getEditorInput().getName()
											.equals(resource.getName()))) {
								existsAnEditor = true;
								break;
							}

						}
					}

				}
				if (existsAnEditor) {
					theoryText = ((PrologEditor) (editor)).getText();
				} else {
					theoryText = openFile((IFile) resource);
				}
				
				currentPath = ((IFile)resource).getLocation().toFile().getParentFile().getPath() + File.separator;

				boolean noErrors = true;
				HashMap<Term, Integer> mapping = new HashMap<Term, Integer>();
				opManager = engine.getOperatorManager().clone();
				parser = ParserFactory.createParser(opManager, theoryText, mapping);
				
				Vector<Term> theoryTerms = new Vector<Term>();
				while (true) {
					Term result = null;
					
					try {
						result = parser.nextTerm(true);
						if (result == null)
							break;
						lookForDirectives(result);

						//if (isTheoryInScope(resource))
						theoryTerms.add(result);
					}
					catch (Exception e) {
						/*
						 * Marco Prati 19/04/11
						 */
						noErrors = false;
						int currLine = parser.getCurrentLine()+alreadyParsed;
						//shows the error
						showError((IFile) resource, e.getMessage(), currLine);
						//update local theory (without line that cause error
						String oldTh=new String(theoryText);
						Vector<String> res = updateTheory(theoryText);
						alreadyParsed+=Integer.parseInt(res.get(1));
						theoryText = res.get(0);
						if(!oldTh.equals(theoryText)){
							parser = ParserFactory.createParser(opManager, theoryText, mapping);
							continue;
						}else{
							if (isTheoryInScope(resource))
								correct = false;
							break;
						}
						//Marco Prati//						
					}
				}				
				
				if (isTheoryInScope(resource)){
					terms.addAll(theoryTerms);
				}
				
				if (noErrors)
					new TheoryChecker((IFile)resource, theoryText, theoryTerms, mapping, engine).check();

			}
			// return true to continue visiting children.
			return true;
		}
		
		/* Francesco Fabbri
		 * 16/05/2011
		 * Search (and execute) directives in last parsed term
		 */
		protected void lookForDirectives(Term t) throws Exception {
			if (t instanceof Struct) {
	    		Struct sd = (Struct)t;
	    		if (sd.getName().equals(":-") && sd.getArity() == 1 && sd.getArg(0) instanceof Struct) {
	    			Struct sop = (Struct)sd.getArg(0);
	    			if (sop.getName().equals("op") && sop.getArity() == 3) {
	    				try {
	    					String name = ((Struct)sop.getArg(2)).getName();
	    					String assc = ((Struct)sop.getArg(1)).getName();
	    					int prio = ((Int)sop.getArg(0)).intValue();
	    					opManager.opNew(name, assc, prio);
	    					Operator op = new Operator(name,assc,prio);
	    					dynOps.add(op);
	    				}
	    				catch (Exception e) {}
	    			}
	    			if (sop.getName().equals("load_library") && sop.getArity() == 1) {
	    				String libName = null;
	    				try {
	    					libName = ((Struct)sop.getArg(0)).getName();
	    					Library lib = (Library) Class.forName(libName).newInstance();	    					
	    					IParser tp = ParserFactory.createParser(opManager, lib.getTheory());
	    					while (true) {
	    						Term tt = tp.nextTerm(true);
	    						if (tt == null)
	    							break;
	    						
	    						terms.add(tt);
	    						lookForDirectives(tt);
	    					}
	    				}
	    				catch (Exception e) { throw new Exception("Cannot load library: " + libName); }
	    			}
	    			if (sop.getName().equals("consult") && sop.getArity() == 1) {
	    				String thName = null;
	    				IParser tp = null;
	    				try {
	    					thName = ((Struct)sop.getArg(0)).getName();
	    					if (!(new File(thName).isAbsolute()))
	    						thName = currentPath + thName;
	    					String th = openFile(thName);	    					
	    					tp = ParserFactory.createParser(opManager, th);
	    					while (true) {
	    						Term tt = tp.nextTerm(true);
	    						if (tt == null)
	    							break;
	    						
	    						terms.add(tt);
	    						lookForDirectives(tt);
	    					}
	    				}
	    				catch (Exception e) { throw new Exception("Cannot load theory: " + thName + (tp == null ? "" : "\n\t" + e.getMessage() + " @ line " + tp.getCurrentLine())); }
	    			}
	    		}
	    	}
		}
	}
	
	class TheoryChecker implements TermVisitor {
		
		protected String theory;
		protected Vector<Term> terms;
		protected HashMap<Term, Integer> mapping;
		protected PrologEngine engine;
		protected IFile file;
		
		protected HashMap<String, Struct> definitions;
		protected HashMap<String, Struct> unresolved;
		protected HashMap<String, Struct> target;
		
		protected Vector<Term> heads;
		protected boolean recoursive;
		
		public TheoryChecker(IFile file, String theory, Vector<Term> terms, HashMap<Term, Integer> mapping, PrologEngine engine) {
			this.file = file;
			this.theory = theory;
			this.terms = terms;
			this.mapping = mapping;
			this.engine = engine;
		}
		
		public void check() {
			scanTheory();
			
			Vector<Struct> unresolvedStructs = new Vector<Struct>();			
			Iterator<String> it = unresolved.keySet().iterator();
			while (it.hasNext()) {
				String sID = it.next();
				if (!definitions.containsKey(sID)) {
					if (!engine.getPrimitiveManager().containsTerm(unresolved.get(sID).getName(), unresolved.get(sID).getArity()))
						unresolvedStructs.add(unresolved.get(sID));
				}
			}
			
			if (unresolvedStructs.size() > 0) {
				IParser tp = ParserFactory.createParser(theory);
				for (int i = 0; i < unresolvedStructs.size(); i++) {
					String name = unresolvedStructs.get(i).getName() + "/" + unresolvedStructs.get(i).getArity();					
					Integer to = mapping.get(unresolvedStructs.get(i));
					if (to == null)
						System.err.println("No mapping for unresolved predicate " + unresolvedStructs.get(i).getName() + "/"
								+ unresolvedStructs.get(i).getArity());
					else if (theory.charAt(to) != '\'' && theory.charAt(to) != '"') {
						int[] region = tp.offsetToRowColumn(to);
						showWarning(file, "Computation may fail due to unresolved term: " + name, region[0], region[1], name.length());
					}
				}
			}
		}
		
		protected void scanTheory() {			
			
			heads = new Vector<Term>();			
			unresolved = new HashMap<String, Struct>();
			target = unresolved;
			recoursive = true;
			for(int i=0; i<terms.size(); i++) { 
				Term t = terms.get(i);
				visit0(t);
			}
			
			definitions = new HashMap<String, Struct>();
			target = definitions;
			recoursive = false;
			for(int i=0; i<heads.size(); i++) 
				heads.get(i).accept(this);			
		}
		
		protected void visit0(Term t) {
			if (t != null && t instanceof Struct) {
				Struct s = (Struct)t;
				if (s.getName().equals(":-")) {
					if (s.getArity() == 1)
						s.getArg(0).accept(this);
					else {
						heads.add(s.getArg(0));
						for (int j=1; j<s.getArity(); j++)
							s.getArg(j).accept(this);
					}
				}
				else
					heads.add(s);
			}			
		}

		public void visit(Struct s) {
			String sID = s.getName() + "/" + s.getArity();		
			
			// Ignore asserts and retracts contents
			if ((s.getName().indexOf("assert") == 0 || s.getName().indexOf("retract") == 0) && s.getArity() == 1) {
				visit0(s.getArg(0));
				return;
			}
			
			// Ignore directives
			if ((s.getName().equals("op") && s.getArity() == 3) || (s.getName().equals("flag") && s.getArity() == 4) || (s.getName().equals("initialization") && s.getArity() == 1) || (s.getName().equals("solve") && s.getArity() == 1) || (s.getName().equals("load_library") && s.getArity() == 1) || (s.getName().equals("consult") && s.getArity() == 1)) {
				return;
			}
			
			if (!target.containsKey(sID))
				target.put(sID, s);	
			
			for (int i=0; recoursive && i<s.getArity(); i++)
				s.getArg(i).accept(this);
		}

		public void visit(Var v) {
		}

		public void visit(Number n) {
		}
	}
}
