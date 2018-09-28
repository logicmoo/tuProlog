package alice.tuprologx.eclipse.scanners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

import alice.tuprologx.eclipse.preferences.PreferenceConstants;
import alice.tuprologx.eclipse.util.TokenManager;

/**
 * A simple scanner for multi-line comment. Distinguish between single-line and
 * multi-line rule.
 */
public class CommentScanner extends RuleBasedScanner {

	public CommentScanner(TokenManager tokenManager) {

		Token comment1 = tokenManager.getToken(
				PreferenceConstants.MULTI_LINE_COMMENT_COLOR,
				PreferenceConstants.MULTI_LINE_COMMENT_STYLE);
		Token comment2 = tokenManager.getToken(
				PreferenceConstants.SINGLE_LINE_COMMENT_COLOR,
				PreferenceConstants.SINGLE_LINE_COMMENT_STYLE);

		setDefaultReturnToken(comment1);

		List<IRule> rules = new ArrayList<IRule>();

		rules.add(new MultiLineRule("/*", "*/", comment1, (char) 0, true));
		rules.add(new EndOfLineRule("%", comment2));

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

}
