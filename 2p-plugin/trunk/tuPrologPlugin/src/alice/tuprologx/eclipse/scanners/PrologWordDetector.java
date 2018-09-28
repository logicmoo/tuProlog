package alice.tuprologx.eclipse.scanners;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * A Prolog aware word detector.
 */
public class PrologWordDetector implements IWordDetector {

	public final static char[] symbols = { '[', ']', '(', ')', '{', '}' };

	/*
	 * (non-Javadoc) Method declared on IWordDetector.
	 */
	public boolean isWordPart(char character) {
		if (Character.isLetterOrDigit(character))
			return true;
		if (character == '_')
			return true;
		return false;
	}

	/*
	 * (non-Javadoc) Method declared on IWordDetector.
	 */
	public boolean isWordStart(char character) {
		if (Character.isLetterOrDigit(character))
			return true;
		if (character == '!')
			return true;
		if (character == '_')
			return true;
		return false;
	}

}
