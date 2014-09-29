package alice.tuprologx.eclipse.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * The <code>StyleSelector</code> is a wrapper for a series of check buttons
 * that displays the selected styles for a determined portion of text and allows
 * the user to change the selection.
 */
public class StyleSelector {
	public static final String PROP_COLORSTYLECHANGE = "colorStyleValue";
	// Indicates the number of check buttons in the selector
	public static final int CONTROL_NUMBER = 2;
	private Button boldButton;
	private Button italicButton;

	/**
	 * Create a new instance of the receiver and the buttons that it wrappers in
	 * the supplied parent <code>Composite</code>.
	 * 
	 * @param parent
	 *            The parent of the buttons.
	 */
	public StyleSelector(Composite parent) {
		;
		boldButton = new Button(parent, SWT.CHECK);
		boldButton.setText("Bold");
		italicButton = new Button(parent, SWT.CHECK);
		italicButton.setText("Italic");
	}

	/**
	 * Get the button controls being wrappered by the selector.
	 * 
	 * @return <code>Buttons</code>
	 */
	public Button[] getButtons() {
		Button[] buttonArray = new Button[CONTROL_NUMBER];
		buttonArray[0] = boldButton;
		buttonArray[1] = italicButton;
		return buttonArray;
	}

	public int getNumberOfControls() {
		return CONTROL_NUMBER;
	}

	/**
	 * Return the current style displayed by the buttons.
	 * <p>
	 * If the buttons aren't checked returns SWT.NORMAL.
	 */
	public int getStyleValue() {
		int style = SWT.NORMAL;
		if (boldButton.getSelection())
			style = style + SWT.BOLD;
		if (italicButton.getSelection())
			style = style + SWT.ITALIC;
		return style;
	}

	/**
	 * Set the status of the check buttons from the global style variable.
	 * 
	 * @param style
	 *            The new style.
	 */
	public void setStyleValue(int style) {
		switch (style) {
		case (SWT.BOLD):
			boldButton.setSelection(true);
			italicButton.setSelection(false);
			break;
		case (SWT.ITALIC):
			boldButton.setSelection(false);
			italicButton.setSelection(true);
			break;
		case (SWT.BOLD + SWT.ITALIC):
			boldButton.setSelection(true);
			italicButton.setSelection(true);
			break;
		default:
			boldButton.setSelection(false);
			italicButton.setSelection(false);
		}
		boldButton.update();
		italicButton.update();
	}

	/**
	 * Set whether or not the buttons are enabled.
	 * 
	 * @param state
	 *            the enabled state.
	 */
	public void setEnabled(boolean state) {
		Button[] buttonArray = getButtons();
		for (int i = 0; i < CONTROL_NUMBER; i++) {
			buttonArray[i].setEnabled(state);
		}
	}

}
