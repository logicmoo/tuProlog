package alice.tuprologx.eclipse.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.*;

import alice.tuprologx.eclipse.scanners.PrologListRule;

public class PrologPartitionScanner extends RuleBasedPartitionScanner {

	public final static String PROLOG_COMMENT = "__prolog_comment";
	public final static String PROLOG_LIST = "__prolog_list";
	public final static String PROLOG_DYNOP = "__prolog_dynOp";


	/**
	 * Creates partitioning for a Prolog document.
	 * <p>
	 * Partitions are: comment, list, default
	 */
	public PrologPartitionScanner() {
		super();

		IToken prologComment = new Token(PROLOG_COMMENT);
		IToken prologList = new Token(PROLOG_LIST);

		PrologListRule list = new PrologListRule(prologList);
		MultiLineRule comment = new MultiLineRule("/*", "*/", prologComment,
				(char) 0, true);
		EndOfLineRule comment2 = new EndOfLineRule("%", prologComment);

		IPredicateRule[] rules = new IPredicateRule[] { list, comment, comment2 };
		setPredicateRules(rules);
	}

	public static String[] getLegalContentTypes() {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				PrologPartitionScanner.PROLOG_COMMENT,
				PrologPartitionScanner.PROLOG_LIST };
	}
}