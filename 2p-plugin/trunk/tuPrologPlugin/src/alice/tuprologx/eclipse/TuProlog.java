package alice.tuprologx.eclipse;

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import alice.tuprologx.eclipse.core.OpenProjectListener;
import alice.tuprologx.eclipse.perspective.PrologPerspective;
import alice.tuprologx.eclipse.util.TokenManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class TuProlog extends AbstractUIPlugin {
	// The shared instance.
	private static TuProlog plugin;
	// Resource bundle.
//	private ResourceBundle resourceBundle;

	public final static String PROLOG_PARTITIONING = "__prolog_partitioning"; //$NON-NLS-1$
	private static final String ICON_FOLDER = "icons/";

	private TokenManager tokenManager;

	public static String[] PROLOG_EXTENSIONS = { "pro", "pl" };


	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		/*
		 * qui recuperare le proprieta' persistenti, creare i motori richiamo un
		 * metodo statico di una classe apposita per es.
		 */
		alice.tuprologx.eclipse.properties.PropertyManager.initializeWorkspace();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new OpenProjectListener());

	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static TuProlog getDefault() {
		return plugin;
	}


	// *** Start code for visualization support ***

	/**
	 * Returns the Prolog token manager.
	 */
	public TokenManager getTokenManager() {
		if (tokenManager == null)
			tokenManager = new TokenManager(this.getPreferenceStore());
		return tokenManager;
	}

	public static IProject getActiveProject() {
		IWorkbenchWindow win = getDefault().getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		if (page != null) {
			IEditorPart editor = page.getActiveEditor();
			if (editor != null) {
				IEditorInput input = editor.getEditorInput();
				if (input instanceof IFileEditorInput) {
					return ((IFileEditorInput)input).getFile().getProject();
				}
			}
		}
		return null;
	}
	/*like the method above, return the current opened file Prolog*/
	public static IFile getActiveFile() {
		IWorkbenchWindow win = getDefault().getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		if (page != null) {
			IEditorPart editor = page.getActiveEditor();
			if (editor != null) {
				IEditorInput input = editor.getEditorInput();
				if (input instanceof IFileEditorInput) {
					return ((IFileEditorInput)input).getFile();
				}
			}
		}
		return null;
	}

	public static void showPerspective() {
		IWorkbench workbench = TuProlog.getDefault().getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IAdaptable input;
		if (page != null)
			input = page.getInput();
		else
			input = ResourcesPlugin.getWorkspace().getRoot();
		try {
			workbench.showPerspective(PrologPerspective.ID_PERSPECTIVE, window, input);
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
	}

	public static void log(final IStatus status) {
		getDefault().getLog().log(status);
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static Image getIconFromResources(String filename) {
		Image icon = null;
		Bundle bundle = TuProlog.getDefault().getBundle();
		String iconPath = ICON_FOLDER + filename;
		URL url = bundle.getEntry(iconPath);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		icon = imageDescriptor.createImage(true);
		return icon;
	}

}
