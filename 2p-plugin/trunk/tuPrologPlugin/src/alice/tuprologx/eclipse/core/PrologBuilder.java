package alice.tuprologx.eclipse.core;

import java.util.Map;

import alice.tuprolog.Theory;
import alice.tuprologx.eclipse.views.ViewSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

public class PrologBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "tuPrologPlugin.prologBuilder";
	private ViewSet viewSet = new ViewSet();
	private PrologEngine engine = null;
	private static boolean alternativeBuild;
	private static Theory lastBuiltTheory;

	@SuppressWarnings({ "rawtypes" })
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (isBuildAllowed()) {
			if (kind == AUTO_BUILD) {
				return null;
			}
			if (kind == FULL_BUILD) {
				fullBuild(monitor);

				// Theory provaT=PrologParser.getTheory();
				// String provaTS=provaT.toString();
				// String provaS=engine.getTheory();

				// richiamo il refresh delle view
				// utilizzo questo workaround per evitare che Eclipse
				// interrompa il refresh per "recursive refresh"
				// Questo metodo verra' chiamato non appena il display sara'
				// disponibile.
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IProject project = getProject();
						String projectName = project.getName();
						String theory = "";
						for (int j = 0; j < PrologEngineFactory.getInstance()
								.getProjectEngines(projectName).size(); j++) {
							theory = theory
									+ "***"
									+ PrologEngineFactory.getInstance()
											.getEngine(project.getName(), j)
											.getName()
									+ "***\n"
									+ PrologEngineFactory.getInstance()
											.getEngine(project.getName(), j)
											.getTheory() + "\n";
							PrologEngineFactory
									.getInstance()
									.getEngine(project.getName(), j)
									.addTheory(
											(Theory) PrologParser.t
													.elementAt(j));
						}
						
						if (theory != null) {
							viewSet.refresh(theory);
							PrologEngineFactory.getInstance()
									.getEngine(project.getName(), 0)
									.refresh("refresh" + theory);
						}
						// riporto il motore a null per i build successivi
						engine = null;
					}
				});
			}
		} else {
			IProject project = getProject();
			PrologEngineFactory factory = PrologEngineFactory.getInstance();
			engine = factory.getEngine(project.getName(), 0);
			engine.refresh("Parser found syntax error(s), cannot make any query");
			engine.addTheory(null);
		}
		return null;

	}

	private boolean isBuildAllowed() {
		return true;
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		// reperisce il motore dal progetto
		IProject project = getProject();
		PrologEngineFactory factory = PrologEngineFactory.getInstance();
		for (int i = 0; i < factory.getProjectEngines(project.getName()).size(); i++) {
			engine = factory.getEngine(project.getName(), i);
			if (engine != null) { // e' stato trovato un motore --> faccio
									// partire il build
				// se l'ultimo build e' stato effettuato con uno scope
				// alternativo ripristino la teoria
				if (alternativeBuild) {
					engine.addTheory(lastBuiltTheory);
				}

				if (PrologParser.getTheory() != null) {
					// engine.addTheory(PrologParser.getTheory());
					lastBuiltTheory = PrologParser.getTheory();
				}
			}

		}

	}

	public static void setAlternativeBuild(boolean flag) {
		alternativeBuild = flag;
	}

	public static boolean isAlternativeBuild() {
		return alternativeBuild;
	}

}
