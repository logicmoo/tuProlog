package alice.tuprologx.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import alice.tuprologx.eclipse.TuProlog;

/** Class used to initialize default preference values. */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	@SuppressWarnings("deprecation")
	public void initializeDefaultPreferences() {
		IPreferenceStore store = TuProlog.getDefault().getPreferenceStore();
		// Initialize default values for Color
		PreferenceConverter.setDefault(store,
				PreferenceConstants.MULTI_LINE_COMMENT_COLOR, new RGB(192, 192,
						192));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.SINGLE_LINE_COMMENT_COLOR, new RGB(192,
						192, 192));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.KEYWORD1_COLOR, new RGB(255, 0, 0));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.KEYWORD2_COLOR, new RGB(0, 0, 128));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.KEYWORD3_COLOR, new RGB(255, 128, 0));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.KEYWORD4_COLOR, new RGB(0,100,0));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.LITERAL1_COLOR, new RGB(255, 0, 0));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.LITERAL2_COLOR, new RGB(255, 128, 0));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.LITERAL3_COLOR, new RGB(213, 106, 0));
		PreferenceConverter.setDefault(store,
				PreferenceConstants.DEFAULT_COLOR, new RGB(0, 0, 0));
		// Initialize default values for Style
		store.setDefault(PreferenceConstants.MULTI_LINE_COMMENT_STYLE,
				SWT.ITALIC + SWT.BOLD);
		store.setDefault(PreferenceConstants.SINGLE_LINE_COMMENT_STYLE,
				SWT.ITALIC);
		store.setDefault(PreferenceConstants.KEYWORD1_STYLE, SWT.NORMAL);
		store.setDefault(PreferenceConstants.KEYWORD2_STYLE, SWT.BOLD
				+ SWT.ITALIC);
		store.setDefault(PreferenceConstants.KEYWORD3_STYLE, SWT.NORMAL);
		store.setDefault(PreferenceConstants.KEYWORD4_STYLE, SWT.NORMAL);

		store.setDefault(PreferenceConstants.LITERAL1_STYLE, SWT.NORMAL);
		store.setDefault(PreferenceConstants.LITERAL2_STYLE, SWT.ITALIC);
		store.setDefault(PreferenceConstants.LITERAL3_STYLE, SWT.BOLD
				+ SWT.ITALIC);
		store.setDefault(PreferenceConstants.DEFAULT_STYLE, SWT.NORMAL);
		// Initialize default font
		// The default font is Eclipse default editor font @see
		// JFaceResources.TEXT_FONT;
		// store.setDefault(PreferenceConstants.FONT, JFaceResources.TEXT_FONT);
		PreferenceConverter.setDefault(
				store,
				PreferenceConstants.FONT,
				JFaceResources.getFontRegistry().getFontData(
						JFaceResources.TEXT_FONT));
		PreferenceConverter.setDefault(
				store,
				PreferenceConstants.CONSOLE_FONT,
				JFaceResources.getFontRegistry().getFontData(
						JFaceResources.VIEWER_FONT));
		// Set preferences for matching brackets, not user modificable
		store.setDefault(PreferenceConstants.ENABLE_MATCHING_BRACKETS, true);
		PreferenceConverter.setDefault(store,
				PreferenceConstants.MATCHING_BRACKETS_COLOR, new RGB(255, 128,
						0));
	}

}
