package alice.tuprologx.eclipse.core;

import java.util.*;

import org.eclipse.core.resources.IProject;

public class PrologEngineFactory {
	
	private Hashtable<IProject, Vector<PrologEngine>> registry = null;
	private static PrologEngineFactory instance;

	protected PrologEngineFactory() {
		registry = new Hashtable<IProject, Vector<PrologEngine>>();
	}

	public static PrologEngineFactory getInstance() {
		if (instance == null)
			instance = new PrologEngineFactory();
		return instance;
	}

	public Vector<PrologEngine> getProjectEngines(IProject project) {
		if (project != null)
			return registry.get(project);
		else
			return null;
	}

	public Vector<Vector<PrologEngine>> getEngines() {
		return (Vector<Vector<PrologEngine>>) registry.values();
	}

	public PrologEngine getEngine(IProject project, int index) {
		Vector<PrologEngine> engines = getProjectEngines(project);
		if (engines != null)
			return engines.elementAt(index);
		else
			return null;
	}

	public PrologEngine insertEntry(IProject project, String name) {
		if (project != null) {
			PrologEngine engine = new PrologEngine(project.getName(), name);
			Vector<PrologEngine> engines = new Vector<PrologEngine>();
			engines.add(engine);
			registry.put(project, engines);
			return engine;
		}
		return null;
	}

	public PrologEngine addEngine(IProject project, String name) {
		if (project != null) {
			PrologEngine engine = new PrologEngine(project.getName(), name);
			Vector<PrologEngine> engines = getProjectEngines(project);
			if (engines != null) {
				engines.add(engine);
				registry.put(project, engines);
			}
			return engine;
		}
		return null;
	}

	public void deleteEngine(IProject project, String name) {
		if (project != null) {
			Vector<PrologEngine> engines = getProjectEngines(project);
			for (int i = 0; i < engines.size(); i++) {
				if (name.equals(PrologEngineFactory.getInstance()
						.getEngine(project, i).getName())) {
					engines.removeElementAt(i);
				}
			}
			registry.remove(project);
			registry.put(project, engines);
		}
	}

}
