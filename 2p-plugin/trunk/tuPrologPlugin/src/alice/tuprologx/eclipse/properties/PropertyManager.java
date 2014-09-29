package alice.tuprologx.eclipse.properties;

import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;
import alice.tuprologx.eclipse.core.PrologNature;

public class PropertyManager {

	private static String qualifier = "alice.tuprologx.eclipse";

	public static void initializeWorkspace() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			configureProject(projects[i]);
		}
	}

	public static void configureProject(IProject project) {
		// configurazione = creazione del motore e set delle librerie
		// il metodo configura un progetto se è aperto e se ha la PrologNature
		boolean hasPrologNature = false;
		try {
			hasPrologNature = project.hasNature(PrologNature.NATURE_ID);
		} catch (CoreException e) {
		}
		if ((project.isOpen()) && (hasPrologNature == true)) {
			// TODO (orfeo) gestire l'apertura di un progetto
			String[] engines = getEngineNumber(project);
			for (int j = 0; j < engines.length; j++) {
				createEngine(project, engines[j]);
				Vector<String> libraries = getLibrariesFromProperties(project,
						engines[j]);
				setLibrariesOnEngine(libraries, PrologEngineFactory
						.getInstance().getEngine(project.getName(), j));
			}
		}
	}

	private static String[] getEngineNumber(IProject project) {
		String eng = "";
		try {
			eng = project.getPersistentProperty(new QualifiedName(qualifier,
					project.getName()));
		} catch (CoreException e) {
		}
		StringTokenizer st = new StringTokenizer(eng, ";");
		int count = st.countTokens();
		String[] engines = new String[count];
		for (int i = 0; i < count; i++)
			engines[i] = st.nextToken();
		return engines;
	}

	private static void createEngine(IProject project, String engineName) {
		PrologEngine engine = null;
		// crea il motore di un progetto (SENZA LIBRARIES!)
		if (PrologEngineFactory.getInstance().getProjectEngines(
				project.getName()) == null) {
			engine = PrologEngineFactory.getInstance().insertEntry(
					project.getName(), engineName);
		} else {
			engine = PrologEngineFactory.getInstance().addEngine(
					project.getName(), engineName);
		}// svuoto le librerie, perchè le lib di default potrebbero non servire
		engine.removeLibrary("alice.tuprolog.lib.BasicLibrary");
		engine.removeLibrary("alice.tuprolog.lib.IOLibrary");
		engine.removeLibrary("alice.tuprolog.lib.ISOLibrary");
		engine.removeLibrary("alice.tuprolog.lib.JavaLibrary");

	}

	public static void addEngineInProperty(IProject project, String engineName) {
		String engines = "";
		try {
			engines = project.getPersistentProperty(new QualifiedName(
					qualifier, project.getName()));
		} catch (CoreException e) {
		}
		if (engines == null)
			engines = "";
		engines = engines + engineName + ";";
		QualifiedName qn = new QualifiedName(qualifier, project.getName());
		try {
			project.setPersistentProperty(qn, engines);
		} catch (CoreException e1) {
		}
	}

	public static void deleteEngineInProperties(IProject project,
			String engineName, String[] engines) {
		String tmp = "";
		for (int i = 0; i < engines.length; i++) {
			tmp = tmp + engines[i] + ";";
		}

		QualifiedName qn = new QualifiedName(qualifier, project.getName());
		try {
			project.setPersistentProperty(qn, tmp);
		} catch (CoreException e1) {
		}

		try {
			project.setPersistentProperty(
					new QualifiedName(qualifier, project.getName() + "."
							+ engineName + ".theories"), null);
			project.setPersistentProperty(
					new QualifiedName(qualifier, project.getName() + "."
							+ engineName + ".libraries"), null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public static Vector<String> getLibrariesFromProperties(IProject project,
			String engineName) {
		String property = ""; // l'elenco di librerie
		Vector<String> libraries = new Vector<String>();
		try {
			property = project.getPersistentProperty(new QualifiedName(
					qualifier, project.getName() + "." + engineName
							+ ".libraries"));
		} catch (CoreException e) {
		}
		StringTokenizer st = new StringTokenizer(property, ";");
		while (st.hasMoreTokens())
			libraries.add(st.nextToken());
		return libraries;
	}

	public static void setLibraryInProperties(IProject project,
			String engineName, String[] libraries) {
		String librariesString = "";
		for (int i = 0; i < libraries.length; i++)
			librariesString = librariesString + libraries[i] + ";";
		try {
			project.setPersistentProperty(
					new QualifiedName(qualifier, project.getName() + "."
							+ engineName + ".libraries"), librariesString);
		} catch (CoreException e) {
		}

	}

	public static void setLibrariesOnEngine(Vector<String> libraries,
			PrologEngine engine) {
		String[] libs = engine.getLibrary();
		for (int i = 0; i < libs.length; i++)
			engine.removeLibrary(libs[i]);
		for (int i = 0; i < libraries.size(); i++)
			engine.addLibrary(libraries.elementAt(i));
	}

	public static boolean allTheories(IProject project, String engineName) {
		String property = ""; // l'elenco di teorie
		try {
			property = project.getPersistentProperty(new QualifiedName(
					qualifier, project.getName() + "." + engineName
							+ ".theories"));
		} catch (CoreException e) {
		}

		return (property.equalsIgnoreCase("*.pl"));// solo il ; significa TUTTE
													// LE TEORIE
	}

	public static Vector<String> getTheoriesFromProperties(IProject project,
			String engineName) {
		String property = "";
		Vector<String> theories = new Vector<String>();
		try {
			property = project.getPersistentProperty(new QualifiedName(
					qualifier, project.getName() + "." + engineName
							+ ".theories"));
		} catch (CoreException e) {
		}

		StringTokenizer st = new StringTokenizer(property, ";");
		while (st.hasMoreTokens())
			theories.add(st.nextToken());

		return theories;
	}

	public static void setTheoriesInProperty(IProject project,
			String engineName, String[] theories, boolean allTheories) {
		String theoriesString = "";
		if (theories == null) { // l'invocazione è partita dal wizard
			if (allTheories)
				theoriesString = "*.pl";

		} else { // l'invocazione è partita dalla pagina di properties
			for (int i = 0; i < theories.length; i++)
				theoriesString = theoriesString + theories[i] + ";";
		}

		try {
			QualifiedName qn3 = new QualifiedName(qualifier, project.getName()
					+ "." + engineName + ".theories");
			project.setPersistentProperty(qn3, theoriesString);
		} catch (CoreException e) {
		}
	}

}
