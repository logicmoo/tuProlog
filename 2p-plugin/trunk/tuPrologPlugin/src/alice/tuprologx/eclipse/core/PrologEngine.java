package alice.tuprologx.eclipse.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import alice.tuprolog.Library;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.event.OutputListener;
import alice.tuprolog.event.SpyListener;
import alice.tuprolog.interfaces.IOperatorManager;
import alice.tuprolog.interfaces.IPrimitiveManager;
import alice.tuprolog.interfaces.IProlog;
import alice.tuprolog.interfaces.PrologFactory;
import alice.tuprolog.lib.IOLibrary;
import alice.tuprologx.eclipse.views.ConsoleView;

public class PrologEngine {
	private String name;
	private IProject project;
	private IProlog prolog;
	private SolveInfo si = null;
	private ArrayList<String> Info;
	private ArrayList<Term> termList;
	public static Display display;
	@SuppressWarnings("unused")
	private boolean hasNext;

	private void setNext(boolean next) {
		this.hasNext = next;
	}

	public boolean hasOpenAlternatives() {
		return prolog.hasOpenAlternatives();
	}

	public PrologEngine(String projectName, String name) {
		this.name = name;
		prolog = PrologFactory.createProlog();
		prolog.setSpy(true);
		project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);
		display = Display.getDefault();
	}

	public void rename(String name) {
		this.name = name;
	}

	public void refresh(String theoryToShow) {
	}

	public String getTheory() {
		Theory theory = prolog.getTheory();
		return theory.toString();
	}
	/**
	 * Adds (append) theory
	 * @param theory
	 */
	public void addTheory(Theory theory) {
		try {
			if (theory != null)
				prolog.addTheory(theory);
		} catch (Exception e) {
		}
	}
	/**
	 * Set new Theory
	 * @param theory
	 */
	public void setTheory(Theory theory) {
		try {
			prolog.clearTheory();
			if (theory != null)
				prolog.addTheory(theory);
		} catch (Exception e) {
		}
	}

	public void stop() {
		prolog.solveHalt();
		restoreScope();
	}

	public void accept() {
		prolog.solveEnd();
		restoreScope();
	}

	private void restoreScope() {
		if (PrologBuilder.isAlternativeBuild()) {
			try {
				project.build(IncrementalProjectBuilder.FULL_BUILD,
						PrologBuilder.BUILDER_ID, null, null);
				PrologBuilder.setAlternativeBuild(false);
			} catch (CoreException e) {
			}
		}
	}

	public String next() {
		if (si == null) {
			setNext(false);
			return "";
		}
		String result = "";
		try {
			if (si.isSuccess()) {
				String binds = si.toString();
				if (!prolog.hasOpenAlternatives()) {
					if (binds.equals("")) {
						result = "yes.\nSolution: "
								+ prolog.toString(si.getSolution());
						setNext(false);
					} else {
						result = "yes.\nSolution: " + binds + "\n"
								+ prolog.toString(si.getSolution());
						setNext(false);
					}
					si = null;

				} else {
					result = "yes.\nSolution: " + binds + "\n"
							+ prolog.toString(si.getSolution()) + "?";
					
					/**Andrea Mordenti 28/04/2011
					 */
					try {
						si = prolog.solveNext();
						List<Var> bindings = si.getBindingVars();
						Iterator<?> it = bindings.iterator();
						while(it.hasNext()) {
							Var v = (Var) it.next();
							if (v != null && !v.isAnonymous() && v.isBound() &&
									(!(v.getTerm() instanceof Var) || (!((Var) (v.getTerm())).getName().startsWith("_")))) {
										if (!exist(v.getName())) Info.add(v.getName());
										termList.add(v.getTerm());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					/*end Andrea Mordenti*/
					//si = prolog.solveNext();
					setNext(true);
				}
			} else {
				/*Castagna 06/2011*/
				if(si.isHalted())
					result = "halt.";
				else
				/**/
					result = "no";
				setNext(false);
				si = null;
				restoreScope();

			}
			return result;
		} catch (Exception nse) {
			nse.printStackTrace();
		}
		return result;
	}
	
	/*Andrea Mordenti 28/04/2011
	 *  Retrive useful information about the query solution
	 * like variables bindings*/
	public void query(final String q) {
		
		final IOLibrary IO = (IOLibrary)prolog.getLibrary("alice.tuprolog.lib.IOLibrary");
		if (IO != null) { // IOLibrary could not be loaded
			IO.setExecutionType(IOLibrary.graphicExecution);
			ConsoleView console = null;
			IWorkbenchWindow dwindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow(); 
			final IWorkbenchPage wp = dwindow.getActivePage(); 
			//final IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IViewReference[] viewList = wp.getViewReferences();
				for(IViewReference ref : viewList){
					if(ref.getId().equalsIgnoreCase("alice.tuprologx.eclipse.views.ConsoleView")){
						console = (ConsoleView) ref.getView(false);
					}
				}
				if(console != null){
					IO.getUserContextInputStream().setReadListener(console);
					console.inputViewer.setUserContextInputStream(IO.getUserContextInputStream());
				}
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		try {
			Info = new ArrayList<String>();
			termList = new ArrayList<Term>();
			si = prolog.solve(q);
			List<?> bindings = si.getBindingVars();
			Iterator<?> it = bindings.iterator();
			while(it.hasNext()) {
				Var v = (Var) it.next();
				if (v != null && !v.isAnonymous() && v.isBound() &&
						(!(v.getTerm() instanceof Var) || (!((Var) (v.getTerm())).getName().startsWith("_")))) {
					if (!exist(v.getName())) Info.add(v.getName());
					termList.add(v.getTerm());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String[] getLibrary() {
		if (prolog == null)
			return new String[] {};
		return prolog.getCurrentLibraries();
	}
	
	/**
	 * Added to get a Library by its name
	 */	
	public Library getLibrary(String name) {
		return prolog.getLibrary(name);
	}

	public void removeLibrary(String lib) {
		try {
			prolog.unloadLibrary(lib);
		} catch (Exception ile) {
		}
	}

	public void addLibrary(String lib) {
		try {
			if (prolog.getLibrary(lib) == null)
				prolog.loadLibrary(lib);
		} catch (Exception ile) {
		}
	}

	public String getName() {
		return name;
	}

	public void addSpyListener(SpyListener spyListener) {
		prolog.addSpyListener(spyListener);
	}

	public void addOutputListener(OutputListener outputListener) {
		prolog.addOutputListener(outputListener);
	}
	
	/*Castagna 06/2011*/
	public void addExceptionListener(ExceptionListener exceptionListener) {
		prolog.addExceptionListener(exceptionListener);
	}
	
	public void removeExceptionListeners() {
		prolog.removeAllExceptionListeners();
	}
	/**/

	public void removeSpyListeners() {
		prolog.removeAllSpyListeners();
	}

	public void removeOutputListeners() {
		prolog.removeAllOutputListeners();

	}
	public ArrayList<String> getSolveInfo(){
		return Info;
	}
	public ArrayList<Term> getListTerm(){
		return termList;
	}
	
	public IOperatorManager getOperatorManager() {
		return prolog.getOperatorManager();
	}
	
	public IPrimitiveManager getPrimitiveManager() {
		return prolog.getPrimitiveManager();
	}
	
	/*Andrea Mordenti 19/04/2011
	 * check if the binding variable is already inserted in the variable list*/
	public boolean exist(String varName){
		Iterator<String> i = Info.iterator();
		while(i.hasNext()){
			if(i.next().equals(varName)) return true;
		}
		return false;
	}
}