package alice.tuprologx.eclipse.scanners;

import alice.tuprologx.eclipse.preferences.PreferenceConstants;
import alice.tuprologx.eclipse.util.TokenManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.*;

/**
 * A scanner for lists. Similar to DefaultScanner adds rules to recognize
 * comments.
 */
public class ListScanner extends RuleBasedScanner {
	TokenManager tokenManager;

	public ListScanner(TokenManager manager) {

		tokenManager = manager;

		Token literal2 = manager.getToken(PreferenceConstants.LITERAL2_COLOR,
				PreferenceConstants.LITERAL2_STYLE);
		Token literal3 = manager.getToken(PreferenceConstants.LITERAL3_COLOR,
				PreferenceConstants.LITERAL3_STYLE);
		setDefaultReturnToken(literal3);

		Token literal1 = tokenManager.getToken(
				PreferenceConstants.LITERAL1_COLOR,
				PreferenceConstants.LITERAL1_STYLE);
		Token comment1 = tokenManager.getToken(
				PreferenceConstants.MULTI_LINE_COMMENT_COLOR,
				PreferenceConstants.MULTI_LINE_COMMENT_STYLE);
		Token comment2 = tokenManager.getToken(
				PreferenceConstants.SINGLE_LINE_COMMENT_COLOR,
				PreferenceConstants.SINGLE_LINE_COMMENT_STYLE);

		List<IRule> rules = new ArrayList<IRule>();

		// Add rules for comments
		rules.add(new MultiLineRule("/*", "*/", comment1, (char) 0, true));
		rules.add(new EndOfLineRule("%", comment2));

		// Add rules for literal strings
		rules.add(new SingleLineRule("\"", "\"", literal1, '\\', true, true));
		rules.add(new SingleLineRule("'", "'", literal1, '\\', true, true));
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new JavaWhitespaceDetector()));

		// Add word rule for keywords.
		WordRule wordRule = new WordRule(new PrologWordDetector(), literal2);

		DefaultScanner.addPredicates(wordRule, literal3, literal3, literal3);

		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}
