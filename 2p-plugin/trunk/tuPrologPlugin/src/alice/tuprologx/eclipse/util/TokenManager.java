package alice.tuprologx.eclipse.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import alice.tuprologx.eclipse.preferences.PreferenceConstants;

/**
 * 
 */

public class TokenManager {
	private Map<RGB, Color> colorTable = new HashMap<RGB, Color>(10);
	private Map<String, Token> tokenTable = new HashMap<String, Token>(10);
	private Map<String, String> knownProperties = new HashMap<String, String>(10);

	private final IPreferenceStore preferenceStore;

	public TokenManager(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}

	/**
	 * Returns the token for the chosen partition.
	 * 
	 * @param colorPreference
	 *            The preference for the text color
	 * @param stylePreference
	 *            The preference for the text style
	 */
	public Token getToken(String colorPreference, String stylePreference) {
		Token token = tokenTable.get(colorPreference);
		if (token == null) {
			// Create Token
			RGB rgb = PreferenceConverter.getColor(preferenceStore,
					colorPreference);
			int style = preferenceStore.getInt(stylePreference);
			token = new Token(new TextAttribute(getColor(rgb), null, style));
			tokenTable.put(colorPreference, token);
			knownProperties.put(stylePreference, colorPreference);
		}
		return token;
	}

	/**
     * 
     */
	public Color getColor(RGB rgb) {
		Color color = colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}
		return color;
	}

	/**
     * 
     */
	public void dispose() {
		Iterator<Color> e = colorTable.values().iterator();
		while (e.hasNext())
			e.next().dispose();
	}

	/**
     * 
     */
	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		String prefKey = (String) event.getProperty();
		if (knownProperties.containsKey(prefKey))
			prefKey = knownProperties.get(prefKey);
		Token token = tokenTable.get(prefKey);
		return (token != null);
	}

	/**
	 * Searches if the token associated to the changed property is known and
	 * updates it.
	 * 
	 * @param event
	 *            The property change event
	 */
	public void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		String prefKey = event.getProperty();
		if (event.getProperty().equals(PreferenceConstants.CONSOLE_FONT))
			return;
		if (event.getProperty().equals(PreferenceConstants.FONT))
			return;
		if (knownProperties.containsKey(prefKey)) {
			// Changed style
			prefKey = knownProperties.get(prefKey);
			Token token = tokenTable.get(prefKey);
			RGB rgb = PreferenceConverter.getColor(preferenceStore, prefKey);
			int style = preferenceStore.getInt(event.getProperty());
			token.setData(new TextAttribute(getColor(rgb), null, style));
		} else {
			// Changed color
			Token token = tokenTable.get(prefKey);
			TextAttribute tokenAttribute = (TextAttribute) token.getData();
			RGB rgb = PreferenceConverter.getColor(preferenceStore, prefKey);
			token.setData(new TextAttribute(getColor(rgb), null, tokenAttribute
					.getStyle()));
		}
	}

}