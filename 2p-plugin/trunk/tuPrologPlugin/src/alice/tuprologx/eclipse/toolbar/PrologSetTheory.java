package alice.tuprologx.eclipse.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;

public class PrologSetTheory extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		IProject currentProject = TuProlog.getActiveProject();
		String theory = TuProlog.getActiveFile().toString();
		PrologEngine engine = PrologEngineFactory.getInstance().getEngine(currentProject,0);
		try {
			engine.setTheory(new Theory(theory));
		} catch (InvalidTheoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
