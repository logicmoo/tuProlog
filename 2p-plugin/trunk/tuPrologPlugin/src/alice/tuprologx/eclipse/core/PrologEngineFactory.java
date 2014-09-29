package alice.tuprologx.eclipse.core;

import java.util.*;

public class PrologEngineFactory {
	
	private Hashtable<String, Vector<PrologEngine>> registry = null;
	private static PrologEngineFactory instance;

	protected PrologEngineFactory() {
		registry = new Hashtable<String, Vector<PrologEngine>>();
	}

	public static PrologEngineFactory getInstance() {
		if (instance == null)
			instance = new PrologEngineFactory();
		return instance;
	}

	public Vector<PrologEngine> getProjectEngines(String projectName) {
		if (projectName != null)
			return registry.get(projectName);
		else
			return null;
	}

	public Vector<Vector<PrologEngine>> getEngines() {
		return (Vector<Vector<PrologEngine>>) registry.values();
	}

	public PrologEngine getEngine(String projectName, int index) {
		Vector<PrologEngine> engines = getProjectEngines(projectName);
		if (engines != null)
			return engines.elementAt(index);
		else
			return null;
	}

	public PrologEngine insertEntry(String projectName, String name) {
		if (projectName != null) {
			PrologEngine engine = new PrologEngine(projectName, name);
			Vector<PrologEngine> engines = new Vector<PrologEngine>();
			engines.add(engine);
			registry.put(projectName, engines);
			return engine;
		}
		return null;
	}

	public PrologEngine addEngine(String projectName, String name) {
		if (projectName != null) {
			PrologEngine engine = new PrologEngine(projectName, name);
			Vector<PrologEngine> engines = getProjectEngines(projectName);
			if (engines != null) {
				engines.add(engine);
				registry.put(projectName, engines);
			}
			return engine;
		}
		return null;
	}

	public void deleteEngine(String projectName, String name) {
		if (projectName != null) {
			Vector<PrologEngine> engines = getProjectEngines(projectName);
			for (int i = 0; i < engines.size(); i++) {
				if (name.equals(PrologEngineFactory.getInstance()
						.getEngine(projectName, i).getName())) {
					engines.removeElementAt(i);
				}
			}
			registry.remove(projectName);
			registry.put(projectName, engines);
		}
	}

}
