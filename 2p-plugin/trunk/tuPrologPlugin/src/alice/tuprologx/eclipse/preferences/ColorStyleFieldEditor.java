package alice.tuprologx.eclipse.preferences;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A field editor for color and style styles preferences.
 */
public class ColorStyleFieldEditor extends FieldEditor {
	/** The style selector, or <code>null</code> if none. */
	private StyleSelector styleSelector;
	/** The color selector, or <code>null</code> if none. */
	private ColorSelector colorSelector;
	private String colorPreferenceName;

	/** Creates a new color and style field editor */
	protected ColorStyleFieldEditor() {
		// No default behavior
	}

	/**
	 * Creates a color-style field editor.
	 * 
	 * @param colorPropertyname
	 *            the name of the color preference this field editor works on
	 * @param stylePropertyName
	 *            the name of the style preference this field editor works on
	 * @param labelText
	 *            the label of this field
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public ColorStyleFieldEditor(String colorPropertyName,
			String stylePropertyName, String labelText, Composite parent) {
		super(stylePropertyName, labelText, parent);
		colorPreferenceName = colorPropertyName;
	}

	/**
	 * Creates a color-style field editor. (with tooltip help)
	 * 
	 * @param colorPropertyname
	 *            the name of the color preference this field editor works on
	 * @param stylePropertyName
	 *            the name of the style preference this field editor works on
	 * @param labelText
	 *            the label of this field
	 * @param tooltipText
	 *            the text shown as a tooltip while hovering on the label
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public ColorStyleFieldEditor(String colorPropertyName,
			String stylePropertyName, String labelText, String tooltipText,
			Composite parent) {
		super(stylePropertyName, labelText, parent);
		colorPreferenceName = colorPropertyName;
		getLabelControl(parent).setToolTipText(tooltipText);
	}

	/* (non-Javadoc) Method declared on FieldEditor. */
	protected void adjustForNumColumns(int numColumns) {
		((GridData) colorSelector.getButton().getLayoutData()).horizontalSpan = numColumns - 3;
		Button[] buttonArray = styleSelector.getButtons();
		for (int i = 0; i < styleSelector.getNumberOfControls(); i++) {
			((GridData) buttonArray[1].getLayoutData()).horizontalSpan = 1;
		}
	}

	/* (non-Javadoc) Method declared on FieldEditor. */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns - 3;
		control.setLayoutData(gd);

		Button colorButton = getColorChangeControl(parent);
		gd = new GridData();
		int widthHint = convertHorizontalDLUsToPixels(colorButton,
				IDialogConstants.BUTTON_WIDTH);
		gd.widthHint = Math.max(widthHint,
				colorButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		colorButton.setLayoutData(gd);

		Button[] styleButton = getStyleControl(parent);
		gd = new GridData();
		for (int i = 0; i < styleSelector.getNumberOfControls(); i++) {
			styleButton[i].setLayoutData(gd);
		}
	}

	/* (non-Javadoc) Method declared on FieldEditor. */
	protected void doLoad() {
		if (styleSelector == null || colorSelector == null)
			return;
		int value = getPreferenceStore().getInt(getStylePreferenceName());
		styleSelector.setStyleValue(value);
		RGB color = PreferenceConverter.getColor(getPreferenceStore(),
				getColorPreferenceName());
		colorSelector.setColorValue(color);
	}

	/* (non-Javadoc) Method declared on FieldEditor. */
	protected void doLoadDefault() {
		if (styleSelector == null || colorSelector == null)
			return;
		styleSelector.setStyleValue(getPreferenceStore().getDefaultInt(
				getStylePreferenceName()));
		colorSelector.setColorValue(PreferenceConverter.getDefaultColor(
				getPreferenceStore(), getColorPreferenceName()));
	}

	/* (non-Javadoc) Method declared on FieldEditor. */
	protected void doStore() {
		int value = styleSelector.getStyleValue();
		getPreferenceStore().setValue(getPreferenceName(), value);
		PreferenceConverter.setValue(getPreferenceStore(),
				getColorPreferenceName(), colorSelector.getColorValue());
	}

	/**
	 * Get the style selector used by the receiver.
	 * 
	 * @return StyleSelector/
	 * @uml.property name="styleSelector"
	 */
	public StyleSelector getStyleSelector() {
		return styleSelector;
	}

	/**
	 * Returns the buttons for the style part of this field editor.
	 * 
	 * @param parent
	 *            The control to create the buttons in if required.
	 * @return an array containing the check buttons for the styles
	 */
	protected Button[] getStyleControl(Composite parent) {
		if (styleSelector == null)
			styleSelector = new StyleSelector(parent);
		else {
			Button[] buttonArray = styleSelector.getButtons();
			for (int i = 0; i < styleSelector.getNumberOfControls(); i++) {
				checkParent(buttonArray[i], parent);
			}
		}
		return styleSelector.getButtons();
	}

	/* (non-Javadoc) Method declared on FieldEditor. */
	public int getNumberOfControls() {
		int controls = 2 + StyleSelector.CONTROL_NUMBER;
		return controls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#setEnabled(boolean,
	 * org.eclipse.swt.widgets.Composite)
	 */
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		getColorChangeControl(parent).setEnabled(enabled);
		Button[] buttonArray = getStyleControl(parent);
		for (int i = 0; i < styleSelector.getNumberOfControls(); i++) {
			buttonArray[i].setEnabled(enabled);
		}
	}

	/**
	 * Returns the relative preference names
	 */
	protected String getColorPreferenceName() {
		return colorPreferenceName;
	}

	protected String getStylePreferenceName() {
		return getPreferenceName();
	}

	/**
	 * Stores this field editor's values back into the preference store.
	 * Overrides the FieldEditor method to add support for two preference.
	 */
	public void store() {
		if (super.getPreferenceStore() == null)
			return;

		if (super.presentsDefaultValue()) {
			super.getPreferenceStore().setToDefault(super.getPreferenceName());
			super.getPreferenceStore().setToDefault(colorPreferenceName);

		} else {
			doStore();
		}
	}

	// Utility methods from ColorFieldEditor
	/**
	 * Computes the size of the color image displayed on the button.
	 * <p>
	 * This is an internal method and should not be called by clients.
	 * </p>
	 * 
	 * @param window
	 *            the window to create a GC on for calculation.
	 * @return Point The image size
	 */
	protected Point computeImageSize(Control window) {
		// Make the image height as high as a corresponding character. This
		// makes sure that the button has the same size as a "normal" text
		// button.
		GC gc = new GC(window);
		Font f = JFaceResources.getFontRegistry().get(
				JFaceResources.DEFAULT_FONT);
		gc.setFont(f);
		int height = gc.getFontMetrics().getHeight();
		gc.dispose();
		Point p = new Point(height * 3 - 6, height);
		return p;
	}

	/**
	 * Returns the change button for the color part of this field editor.
	 * 
	 * @param parent
	 *            The control to create the button in if required.
	 * @return the change button
	 */
	protected Button getColorChangeControl(Composite parent) {
		if (colorSelector == null) {
			colorSelector = new ColorSelector(parent);
			colorSelector.addListener(new IPropertyChangeListener() {
				// forward the property change of the color selector
				public void propertyChange(PropertyChangeEvent event) {
					ColorStyleFieldEditor.this.fireValueChanged(
							event.getProperty(), event.getOldValue(),
							event.getNewValue());
					setPresentsDefaultValue(false);
				}
			});

		} else {
			checkParent(colorSelector.getButton(), parent);
		}
		return colorSelector.getButton();
	}

}