package alice.tuprologx.eclipse.perspective;

import org.eclipse.ui.*;
import org.eclipse.ui.navigator.resources.ProjectExplorer;

public class PrologPerspective implements IPerspectiveFactory {

	public static String ID_PERSPECTIVE = "tuPrologPlugin.perspective.PrologPerspective";

	public void createInitialLayout(IPageLayout layout) {

		String editorArea = layout.getEditorArea();

		IFolderLayout topLeft = layout.createFolder("topLeft",
				IPageLayout.LEFT, 0.15f, editorArea);
		topLeft.addView(ProjectExplorer.VIEW_ID);
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
		topLeft.addPlaceholder(IPageLayout.ID_BOOKMARKS);

		IFolderLayout tuP = layout.createFolder("topRight", IPageLayout.RIGHT,
				0.75f, editorArea);
		tuP.addView("alice.tuprologx.eclipse.views.ASTView");

		IFolderLayout tuP2 = layout.createFolder("topRight2", IPageLayout.BOTTOM,
				0.65f, "alice.tuprologx.eclipse.views.ASTView");
		tuP2.addView("alice.tuprologx.eclipse.views.QueryList");

		IFolderLayout util = layout.createFolder("Util", IPageLayout.BOTTOM,
				0.60f, editorArea);
		util.addView("alice.tuprologx.eclipse.views.ConsoleView");
		util.addView(IPageLayout.ID_PROBLEM_VIEW);

	


	}

}
