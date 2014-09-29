package alice.tuprologx.eclipse.toolbar;


import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;

public class PrologSetTheoryAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
	};

	public void init(IWorkbenchWindow window) {
	};

	public void run(IAction action) {
		IProject currentProject = TuProlog.getActiveProject();
		String theory = TuProlog.getActiveFile().toString();
		PrologEngine engine = PrologEngineFactory.getInstance().getEngine(currentProject.getName(),0);
		try {
			engine.setTheory(new Theory(theory));
		} catch (InvalidTheoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}

}
