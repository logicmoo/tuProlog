package alice.tuprologx.eclipse.core;

import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import alice.tuprologx.eclipse.properties.PropertyManager;

public class DeltaPrinter implements IResourceDeltaVisitor {
	private Stack<PrologEngine> stackEngines;
	private boolean isProjectRename;
	private boolean isOddOperation; // si deve evitare errore caso Delete -> Import
	
	public DeltaPrinter() {
		super();
		stackEngines = new Stack<>();
		setProjectRename(false);
		resetOddOperation();
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource res = delta.getResource();
        switch (delta.getKind()) {
           case IResourceDelta.ADDED:
              /*System.out.print("Resource ");
              System.out.print(res.getFullPath());
              System.out.println(" was added.");*/
              // Il nuovo progetto col nome cambiato viene aggiunto
              if (res instanceof IProject) {
            	  // Si verifica se il progetto che si sta per aggiungere
                  // è un progetto Prolog
                  boolean hasPrologNature = false;
          		  try {
          			  hasPrologNature = ((IProject) res).hasNature(PrologNature.NATURE_ID);
          		  } catch (CoreException e) {
          		  }
            	  if (hasPrologNature && isProjectRename()) {
            		  // Si aggiunge il progetto res a PrologEngineFactory
            		  String primoEngine = "Engine1";
            		  PrologEngineFactory.getInstance().insertEntry((IProject) res, primoEngine);
            		  // Si rimuove primoEngine dato che non era presente nel progetto iniziale
            		  PrologEngineFactory.getInstance().deleteEngine((IProject) res, primoEngine);
            		  // Si caricano i motori Prolog dallo stack dentro PrologEngineFactory
            		  while (!getStackEngines().isEmpty()) {
            			  PrologEngineFactory.getInstance().addEngine((IProject) res, getStackEngines().pop().getName());
            		  }
            		  setProjectRename(false); // si avvisa deltaPrinter che la rinomina è completa
            		  return false; // progetto rinominato, non si deve visitare i figli
            	  }
              }
              break;
           case IResourceDelta.CHANGED:
              /*System.out.print("Resource ");
              System.out.print(res.getFullPath());
              System.out.println(" has changed.");*/
              if (res instanceof IProject) {
            	  // Si verifica se il progetto che si sta per aggiungere
                  // è un progetto Prolog
                  boolean hasPrologNature = false;
          		  try {
          			  hasPrologNature = ((IProject) res).hasNature(PrologNature.NATURE_ID);
          		  } catch (CoreException e) {
          		  }
            	  if (hasPrologNature) {
            		  //System.out.println("Prolog Project was changed.");
            		  String primoEngine = "Engine1";
            		  PrologEngineFactory.getInstance().insertEntry((IProject) res, primoEngine);
            		  // Si salva il motore nelle proprietà persistenti
            		  PropertyManager.addEngineInProperty((IProject) res, primoEngine);            			  
            		  // inserimento delle librerie di default
            		  String[] libraries = {"alice.tuprolog.lib.BasicLibrary", "alice.tuprolog.lib.IOLibrary", 
            				  "alice.tuprolog.lib.ISOLibrary", "alice.tuprolog.lib.OOLibrary"};
            		  PropertyManager.setLibraryInProperties((IProject) res, primoEngine, libraries);
            		  // Si aggiungono tutte le teorie del progetto
            		  PropertyManager.setTheoriesInProperty((IProject) res, primoEngine, null, true);
            		  return false; // progetto importato, non visitare figli
            	  }
              }
              break;
        }
        return true; // visit the children
	}
	
	public Stack<PrologEngine> getStackEngines() {
		return stackEngines;
	}
	
	public void setProjectRename(boolean isProjectRename) {
		this.isProjectRename = isProjectRename;
	}
	
	public boolean isProjectRename() {
		return isProjectRename;
	}
	
	public boolean isOddOperation() {
		return this.isOddOperation;
	}
	
	public void toggleIsOddOperation() {
		if (isOddOperation()) {
			this.isOddOperation = false;
		} else {
			this.isOddOperation = true;
		}
	}
	
	public void resetOddOperation() {
		this.isOddOperation = false;
	}
}
