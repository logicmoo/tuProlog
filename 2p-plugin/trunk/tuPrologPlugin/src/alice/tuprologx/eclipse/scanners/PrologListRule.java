package alice.tuprologx.eclipse.scanners;

import org.eclipse.core.runtime.Assert;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * Modified implementation of <code>PatternRule</code>. Now recognizes if the
 * same start and ending pattern appears more than one time, as in nested lists.
 * Also recognizes strings and comments inside the list.
 */
public class PrologListRule implements IPredicateRule {

	/** Internal setting for the uninitialized column constraint */
	protected static final int UNDEFINED = -1;

	/**
	 * Counter for the opening patterns, must match the relative ending pattern.
	 */
	protected int flag;

	/** The token to be returned on success */
	protected IToken fToken;

	/** The pattern's column constrain */
	protected int fColumn = UNDEFINED;

	/**
	 * Creates a rule for the given starting and ending sequence. When these
	 * sequences are detected the rule will return the specified token.
	 * 
	 * @param token
	 *            the token which will be returned on success
	 */
	public PrologListRule(IToken token) {
		Assert.isNotNull(token);
		fToken = token;

		flag = 0;
	}

	/**
	 * Evaluates this rules without considering any column constraints.
	 * 
	 * @param scanner
	 *            the character scanner to be used
	 * @return the token resulting from this evaluation
	 */
	protected IToken doEvaluate(ICharacterScanner scanner) {
		return doEvaluate(scanner, false);
	}

	/**
	 * Evaluates this rules without considering any column constraints. Resumes
	 * detection, i.e. look sonly for the end sequence required by this rule if
	 * the <code>resume</code> flag is set.
	 * 
	 * @param scanner
	 *            the character scanner to be used
	 * @param resume
	 *            <code>true</code> if detection should be resumed,
	 *            <code>false</code> otherwise
	 * @return the token resulting from this evaluation
	 */
	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {

		if (resume) {
			// do nothing, force the reevaluation of
			// the entire partition.
		} else {
			int c = scanner.read();
			if (c == '[') {
				flag = 1;
				if (endSequenceDetected(scanner))
					return fToken;
			}
		}
		scanner.unread();
		return Token.UNDEFINED;
	}

	/* @see IRule#evaluate(ICharacterScanner) */
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}

	/**
	 * Returns whether the end sequence was detected. As the pattern can be
	 * Considers anidates lists, strings and comments.
	 * 
	 * @param scanner
	 *            the character scanner to be used
	 * @return <code>true</code> if the end sequence has been detected
	 */
	protected boolean endSequenceDetected(ICharacterScanner scanner) {

		int c;
		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			// annidated list
			if (c == '[')
				flag = flag + 1;

			// check if there is a single line comment inside the list
			// ignore all the characters till the end of line
			else if (c == '%')
				while (c != '\n' && c != ICharacterScanner.EOF) {
					c = scanner.read();
				}

			// check if there is a string inside the list
			// ignore all the characters till closing
			// apostrophes or the end of line
			else if (c == '"')
				do {
					c = scanner.read();
					if (c == '\\') {
						scanner.read();
						c = scanner.read();
					}
				} while (c != '"' && c != ICharacterScanner.EOF && c != '\n');
			else if (c == '\'')
				do {
					c = scanner.read();
					if (c == '\\') {
						scanner.read();
						c = scanner.read();
					}
					if (c == '\'') {
						c = scanner.read();
						if (c == '\'')
							c = scanner.read();
						else {
							scanner.unread();
							c = '\'';
						}
						;
					}
				} while (c != '\'' && c != ICharacterScanner.EOF && c != '\n');

			// check if there is a multi line comment inside the list
			// ignore all the character between /* and */
			// or if the comment is not closed, until the EOF
			else if (c == '/') {
				c = scanner.read();
				if (c == '*') {
					while ((c = scanner.read()) != ICharacterScanner.EOF) {
						if (c == '*') {
							c = scanner.read();
							if (c == '/')
								break;
							else
								scanner.unread();
						}
					}
				} else
					scanner.unread();
			}

			// check if the specified end sequence has been found.
			else if (c == ']') {
				flag = flag - 1;
				if (flag == 0)
					return true;
			}
		}
		// the rule is positive if EOF is found
		return true;
	}

	/*
	 * @see IPredicateRule#evaluate(ICharacterScanner, boolean)
	 * 
	 * @since 2.0
	 */
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if (fColumn == UNDEFINED)
			return doEvaluate((RuleBasedScanner) scanner, resume);

		int c = scanner.read();
		scanner.unread();
		if (c == '[')
			return (fColumn == scanner.getColumn() ? doEvaluate(scanner, resume)
					: Token.UNDEFINED);
		return Token.UNDEFINED;
	}

	/*
	 * @see IPredicateRule#getSuccessToken()
	 * 
	 * @since 2.0
	 */
	public IToken getSuccessToken() {
		return fToken;
	}
}
