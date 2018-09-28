package alice.tuprologx.eclipse.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

public class MyRenameProjectChangeReporter implements IResourceChangeListener {
	private DeltaPrinter deltaPrinter;
	
	public MyRenameProjectChangeReporter() {
		super();
		deltaPrinter = new DeltaPrinter();
	}
	
	/* Si hanno i casi seguenti:
	 * (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 * Rename
	 * 		PRE_DELETE
	 * 		POST_CHANGE -> ADDED
	 * 
	 * Import
	 * 		POST_CHANGE -> ADDED
	 * 		POST_CHANGE -> CHANGED
	 * 
	 * Delete
	 * 		PRE_DELETE
	 */
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResource res = event.getResource();
        switch (event.getType()) {  
           case IResourceChangeEvent.PRE_DELETE:
              /*System.out.print("Project ");
              System.out.print(res.getFullPath());
              System.out.println(" is about to be deleted.");*/
              // Si cancella il progetto res
              if (res instanceof IProject) {
            	  // Si verifica se il progetto che si sta per cancellare
                  // è un progetto Prolog
                  boolean hasPrologNature = false;
          		  try {
          			  hasPrologNature = ((IProject) res).hasNature(PrologNature.NATURE_ID);
          		  } catch (CoreException e) {
          		  }
          		  if (hasPrologNature) {
          			  // Si rimuove il progetto res da PrologEngineFactory
           			  // Si inizializza (svuota) lo Stack
           			  while (!deltaPrinter.getStackEngines().isEmpty()) {
           				  deltaPrinter.getStackEngines().pop();
           			  }
           			  // Si avvisa deltaPrinter della rinomina
           			  deltaPrinter.setProjectRename(true);
           			  // Si ha una probabile delete
           			  /*deltaPrinter.resetOddOperation();
           			  deltaPrinter.toggleIsOddOperation();*/
          			  // Si salvano i rispettivi motori in stack dentro deltaPrinter
          			  for (int i=0; i<PrologEngineFactory.getInstance().getProjectEngines((IProject) res).size(); i++) {
          				  deltaPrinter.getStackEngines().push(PrologEngineFactory.getInstance().getEngine((IProject) res, i));
          			  }
          			  
          			  // Si rimuovono i motori aggiunti nello stack da PrologEngineFactory
          			  for (int i=0; i<deltaPrinter.getStackEngines().size(); i++) {
          				  PrologEngineFactory.getInstance().deleteEngine((IProject) res, PrologEngineFactory.getInstance().getEngine((IProject) res, 0).getName());
          			  }
          		  }
              }
              break;
           case IResourceChangeEvent.POST_CHANGE:
              //System.out.println("Resources have changed.");
              try {
				event.getDelta().accept(deltaPrinter);
              } catch (CoreException e) {
            	  // TODO Auto-generated catch block
            	  e.printStackTrace();
              }
              break;
        }
	}
}
