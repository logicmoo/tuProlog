package alice.tuprologx.eclipse.scanners;

import alice.tuprologx.eclipse.preferences.PreferenceConstants;
import alice.tuprologx.eclipse.util.TokenManager;
import java.util.*;
import org.eclipse.jface.text.rules.*;

public class DefaultScanner extends RuleBasedScanner {

	private TokenManager tokenManager;

	public DefaultScanner(TokenManager tManager) {

		tokenManager = tManager;

		IToken defaultToken = tokenManager.getToken(
				PreferenceConstants.DEFAULT_COLOR,
				PreferenceConstants.DEFAULT_STYLE);
		setDefaultReturnToken(defaultToken);

		Token keyword1 = tokenManager.getToken(
				PreferenceConstants.KEYWORD1_COLOR,
				PreferenceConstants.KEYWORD1_STYLE);
		Token keyword2 = tokenManager.getToken(
				PreferenceConstants.KEYWORD2_COLOR,
				PreferenceConstants.KEYWORD2_STYLE);
		Token keyword3 = tokenManager.getToken(
				PreferenceConstants.KEYWORD3_COLOR,
				PreferenceConstants.KEYWORD3_STYLE);
		Token literal1 = tokenManager.getToken(
				PreferenceConstants.LITERAL1_COLOR,
				PreferenceConstants.LITERAL1_STYLE);
		Token other = tokenManager.getToken(PreferenceConstants.DEFAULT_COLOR,
				PreferenceConstants.DEFAULT_STYLE);

		List<IRule> rules = new ArrayList<IRule>();

		// Add rule for literal strings
		rules.add(new SingleLineRule("\"", "\"", literal1, '\\', true, true));
		rules.add(new SingleLineRule("'", "'", literal1, '\\', true, true));
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new JavaWhitespaceDetector()));

		// Add word rule for keywords.
		WordRule wordRule = new WordRule(new PrologWordDetector(), other);
		addPredicates(wordRule, keyword1, keyword2, keyword3);

		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

	/**
	 * Add to a WordRule the predicates from tuProlog standard libraries.
	 * <p>
	 * Similar to PrologTokenMarker in tuProlog.
	 * 
	 * @param wordRule
	 *            the WordRule the predicates are associated
	 * @param keyword1
	 *            token returned to be returned by most of the predicates
	 * @param keyword2
	 *            token to be returned by other 0-arity predicates not belonging
	 *            to any particular library
	 * @param keyword3
	 *            token to be returned by the singleton variable
	 */
	public static void addPredicates(WordRule wordRule, Token keyword1,
			Token keyword2, Token keyword3) {
		/* Predicates from BasicLibrary */
		wordRule.addWord("abolish", keyword2);
		wordRule.addWord("add_theory", keyword2);
		wordRule.addWord("agent", keyword2);
		wordRule.addWord("arg", keyword2);
		wordRule.addWord("append", keyword2);
		wordRule.addWord("assert", keyword2);
		wordRule.addWord("asserta", keyword2);
		wordRule.addWord("assertz", keyword2);
		wordRule.addWord("atom", keyword2);
		wordRule.addWord("atomic", keyword2);
		wordRule.addWord("bagof", keyword2);
		wordRule.addWord("call", keyword2);
		wordRule.addWord("clause", keyword2);
		wordRule.addWord("compound", keyword2);
		wordRule.addWord("constant", keyword2);
		wordRule.addWord("copy_term", keyword2);
		wordRule.addWord("current_op", keyword2);
		wordRule.addWord("current_prolog_flag", keyword2);
		wordRule.addWord("delete", keyword2);
		wordRule.addWord("element", keyword2);
		wordRule.addWord("findall", keyword2);
		wordRule.addWord("float", keyword2);
		wordRule.addWord("functor", keyword2);
		wordRule.addWord("get_theory", keyword2);
		wordRule.addWord("ground", keyword2);
		wordRule.addWord("integer", keyword2);
		wordRule.addWord("length", keyword2);
		wordRule.addWord("list", keyword2);
		wordRule.addWord("member", keyword2);
		wordRule.addWord("nonvar", keyword2);
		wordRule.addWord("nospy", keyword1);
		wordRule.addWord("not", keyword2);
		wordRule.addWord("num_atom", keyword2);
		wordRule.addWord("number", keyword2);
		wordRule.addWord("once", keyword2);
		wordRule.addWord("quicksort", keyword2);
		wordRule.addWord("repeat", keyword1);
		wordRule.addWord("retract", keyword2);
		wordRule.addWord("retract_bt", keyword2);
		wordRule.addWord("retract_nb", keyword2);
		wordRule.addWord("reverse", keyword2);
		wordRule.addWord("set_prolog_flag", keyword2);
		wordRule.addWord("set_theory", keyword2);
		wordRule.addWord("setof", keyword2);
		wordRule.addWord("spy", keyword2);
		wordRule.addWord("text_concat", keyword2);
		wordRule.addWord("text_term", keyword2);
		wordRule.addWord("unify_with_occurs_check", keyword2);
		wordRule.addWord("var", keyword2);

		/* Predicates from ISOLibrary */
		wordRule.addWord("atom_length", keyword2);
		wordRule.addWord("atom_chars", keyword2);
		wordRule.addWord("atom_codes", keyword2);
		wordRule.addWord("atom_concat", keyword2);
		wordRule.addWord("bound", keyword2);
		wordRule.addWord("char_code", keyword2);
		wordRule.addWord("number_chars", keyword2);
		wordRule.addWord("number_codes", keyword2);
		wordRule.addWord("sub_atom", keyword2);
		// mathematical functions
		wordRule.addWord("abs", keyword2);
		wordRule.addWord("atan", keyword2);
		wordRule.addWord("ceiling", keyword2);
		wordRule.addWord("cos", keyword2);
		wordRule.addWord("div", keyword2);
		wordRule.addWord("exp", keyword2);
		wordRule.addWord("float_fractional_part", keyword2);
		wordRule.addWord("float_integer_part", keyword2);
		wordRule.addWord("floor", keyword2);
		wordRule.addWord("log", keyword2);
		wordRule.addWord("mod", keyword2);
		wordRule.addWord("rem", keyword2);
		wordRule.addWord("round", keyword2);
		wordRule.addWord("sign", keyword2);
		wordRule.addWord("sin", keyword2);
		wordRule.addWord("sqrt", keyword2);
		wordRule.addWord("truncate", keyword2);

		/* Predicates from IOLibrary */
		wordRule.addWord("agent_file", keyword2);
		wordRule.addWord("consult", keyword2);
		wordRule.addWord("get", keyword2);
		wordRule.addWord("get0", keyword2);
		wordRule.addWord("nl", keyword1);
		wordRule.addWord("put", keyword2);
		wordRule.addWord("rand_float", keyword2);
		wordRule.addWord("rand_int", keyword2);
		wordRule.addWord("read", keyword2);
		wordRule.addWord("see", keyword2);
		wordRule.addWord("seeing", keyword2);
		wordRule.addWord("seen", keyword1);
		wordRule.addWord("solve_file", keyword2);
		wordRule.addWord("tab", keyword2);
		wordRule.addWord("tell", keyword2);
		wordRule.addWord("telling", keyword2);
		wordRule.addWord("text_from_file", keyword2);
		wordRule.addWord("told", keyword1);
		wordRule.addWord("write", keyword2);

		/* Predicates from JavaLibrary */
		wordRule.addWord("as", keyword2);
		wordRule.addWord("destroy_object", keyword2);
		wordRule.addWord("java_array_get", keyword2);
		wordRule.addWord("java_array_get_boolean", keyword2);
		wordRule.addWord("java_array_get_byte", keyword2);
		wordRule.addWord("java_array_get_char", keyword2);
		wordRule.addWord("java_array_get_double", keyword2);
		wordRule.addWord("java_array_get_float", keyword2);
		wordRule.addWord("java_array_get_int", keyword2);
		wordRule.addWord("java_array_get_long", keyword2);
		wordRule.addWord("java_array_get_short", keyword2);
		wordRule.addWord("java_array_length", keyword2);
		wordRule.addWord("java_array_set", keyword2);
		wordRule.addWord("java_array_set_boolean", keyword2);
		wordRule.addWord("java_array_set_byte", keyword2);
		wordRule.addWord("java_array_set_char", keyword2);
		wordRule.addWord("java_array_set_double", keyword2);
		wordRule.addWord("java_array_set_float", keyword2);
		wordRule.addWord("java_array_set_int", keyword2);
		wordRule.addWord("java_array_set_long", keyword2);
		wordRule.addWord("java_array_set_short", keyword2);
		wordRule.addWord("java_call", keyword2);
		wordRule.addWord("java_class", keyword2);
		wordRule.addWord("java_object", keyword2);
		wordRule.addWord("java_object_bt", keyword2);
		wordRule.addWord("java_object_nb", keyword2);
		wordRule.addWord("java_object_string", keyword2);
		wordRule.addWord("returns", keyword2);

		/* other 0-arity predicates not belonging to any particular library */
		wordRule.addWord("!", keyword1);
		wordRule.addWord("at_the_end_of_stream", keyword1);
		wordRule.addWord("fail", keyword1);
		wordRule.addWord("halt", keyword1);
		wordRule.addWord("is", keyword1);
		wordRule.addWord("true", keyword1);
		
		
		// singleton variable
		wordRule.addWord("_", keyword3);

	}

}
