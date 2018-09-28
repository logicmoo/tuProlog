package alice.tuprologx.eclipse.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import alice.tuprologx.eclipse.TuProlog;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class PreferencePageRoot extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public static FontFieldEditor fontFieldOutput;

	public PreferencePageRoot() {
		super(GRID);
		// noDefaultAndApplyButton();
		setPreferenceStore(TuProlog.getDefault().getPreferenceStore());
		setDescription("General tuProlog plugin editor settings\n\n"
				+ "Change the font used in this editor. Default is Eclipse Text Font.\n ");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		Composite p = getFieldEditorParent();
		addField(new FontFieldEditor(PreferenceConstants.FONT, "Font: ", p));
		addField(new FontFieldEditor(PreferenceConstants.CONSOLE_FONT,
				"Font tuProlog console: ", p));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}