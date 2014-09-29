package alice.tuprologx.eclipse.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
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

public class PreferencePageSyntaxColoring extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public PreferencePageSyntaxColoring() {
		super(GRID);
		setPreferenceStore(TuProlog.getDefault().getPreferenceStore());
		setDescription("Customize color for Syntax Highlighting.\n ");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		Composite p = getFieldEditorParent();

		// TODO: inserire opzione underline, striketrough
		addField(new ColorStyleFieldEditor(
				PreferenceConstants.MULTI_LINE_COMMENT_COLOR,
				PreferenceConstants.MULTI_LINE_COMMENT_STYLE,
				"Multi-line comment:", p));
		addField(new ColorStyleFieldEditor(
				PreferenceConstants.SINGLE_LINE_COMMENT_COLOR,
				PreferenceConstants.SINGLE_LINE_COMMENT_STYLE,
				"Single-line comment:", p));
		addField(new ColorStyleFieldEditor(
				PreferenceConstants.KEYWORD1_COLOR,
				PreferenceConstants.KEYWORD1_STYLE,
				"Keyword1:",
				"Other 0-arity predicates\nnot belonging to any particular library",
				p));
		addField(new ColorStyleFieldEditor(PreferenceConstants.KEYWORD2_COLOR,
				PreferenceConstants.KEYWORD2_STYLE, "Keyword2:",
				"Most of the predicates defined in the standard libraries", p));
		addField(new ColorStyleFieldEditor(PreferenceConstants.KEYWORD3_COLOR,
				PreferenceConstants.KEYWORD3_STYLE, "Keyword3:",
				"The singleton variable _", p));
		addField(new ColorStyleFieldEditor(PreferenceConstants.LITERAL1_COLOR,
				PreferenceConstants.LITERAL1_STYLE, "Literal String:", p));
		addField(new ColorStyleFieldEditor(PreferenceConstants.LITERAL2_COLOR,
				PreferenceConstants.LITERAL2_STYLE, "Literal List:", p));
		addField(new ColorStyleFieldEditor(PreferenceConstants.LITERAL3_COLOR,
				PreferenceConstants.LITERAL3_STYLE, "List Elements:", p));
		addField(new ColorStyleFieldEditor(PreferenceConstants.DEFAULT_COLOR,
				PreferenceConstants.DEFAULT_STYLE, "Text:", p));
		addField(new ColorStyleFieldEditor(PreferenceConstants.KEYWORD4_COLOR,
				PreferenceConstants.KEYWORD4_STYLE, "DynOp:", p));
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