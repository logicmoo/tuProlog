/*
 * Generated on 11/15/14 6:05 PM
 */
package alice.tuprologx.ide;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;


/**
 * Generated with TokenMakerMaker
 */
%%

%public
%class PrologTokenMaker2
%extends AbstractJFlexTokenMaker
%unicode
/* Case sensitive */
%type org.fife.ui.rsyntaxtextarea.Token


%{


	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public PrologTokenMaker2() {
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos-1, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addHyperlinkToken(int, int, int)
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, false);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *        occurs.
	 * @param hyperlink Whether this token is a hyperlink.
	 */
	public void addToken(char[] array, int start, int end, int tokenType,
						int startOffset, boolean hyperlink) {
		super.addToken(array, start,end, tokenType, startOffset, hyperlink);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * {@inheritDoc}
	 */
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "%", null };
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;

		// Start off in the proper state.
		int state = Token.NULL;
		switch (initialTokenType) {
						case Token.COMMENT_MULTILINE:
				state = MLC;
				start = text.offset;
				break;

			/* No documentation comments */
			default:
				state = Token.NULL;
		}

		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}


	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}


	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream 
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream 
	 */
	public final void yyreset(Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtBOL  = true;
		zzAtEOF  = false;
	}


%}

Letter							= [A-Za-z]
LetterOrUnderscore				= ({Letter}|"_")
NonzeroDigit						= [1-9]
Digit							= ("0"|{NonzeroDigit})
HexDigit							= ({Digit}|[A-Fa-f])
OctalDigit						= ([0-7])
AnyCharacterButApostropheOrBackSlash	= ([^\\'])
AnyCharacterButDoubleQuoteOrBackSlash	= ([^\\\"\n])
EscapedSourceCharacter				= ("u"{HexDigit}{HexDigit}{HexDigit}{HexDigit})
Escape							= ("\\"(([btnfr\"'\\])|([0123]{OctalDigit}?{OctalDigit}?)|({OctalDigit}{OctalDigit}?)|{EscapedSourceCharacter}))
NonSeparator						= ([^\t\f\r\n\ \(\)\{\}\[\]\;\,\.\=\>\<\!\~\?\:\+\-\*\/\&\|\^\%\"\']|"#"|"\\")
IdentifierStart					= ([a-z]|"$")
IdentifierPart						= ((([A-Za-z]|"_")|"$")|{Digit}|("\\"{EscapedSourceCharacter}))
VariableStart					= (([A-Z]|"_")|"$")

LineTerminator				= (\n)
WhiteSpace				= ([ \t\f]+)

/* No char macros */
/* No string macros */

MLCBegin					= "/*"
MLCEnd					= "*/"

/* No documentation comments */
LineCommentBegin			= "%"

IntegerLiteral			= ({Digit}+)
HexLiteral			= (0x{HexDigit}+)
FloatLiteral			= (({Digit}+)("."{Digit}+)?(e[+-]?{Digit}+)? | ({Digit}+)?("."{Digit}+)(e[+-]?{Digit}+)?)
ErrorNumberFormat			= (({IntegerLiteral}|{HexLiteral}|{FloatLiteral}){NonSeparator}+)


Separator					= ([\(\)\{\}\[\]])
Separator2				= ([\;,.])

Identifier				= ({IdentifierStart}{IdentifierPart}*)
Variable				= ({VariableStart}{IdentifierPart}*)

URLGenDelim				= ([:\/\?#\[\]@])
URLSubDelim				= ([\!\$&'\(\)\*\+,;=])
URLUnreserved			= ({LetterOrUnderscore}|{Digit}|[\-\.\~])
URLCharacter			= ({URLGenDelim}|{URLSubDelim}|{URLUnreserved}|[%])
URLCharacters			= ({URLCharacter}*)
URLEndCharacter			= ([\/\$]|{Letter}|{Digit})
URL						= (((https?|f(tp|ile))"://"|"www.")({URLCharacters}{URLEndCharacter})?)


%state STRING
%state CHAR
%state MLC
/* No documentation comment state */
%state EOL_COMMENT

%%

<YYINITIAL> {

	/* Keywords */
	/* No keywords */

	/* Keywords 2 (just an optional set of keywords colored differently) */
	/* No keywords 2 */

	/* Data types */
	/* No data types */

	/* Functions */
	"!" |
"_" |
"abolish" |
"abs" |
"add_theory" |
"agent" |
"agent_file" |
"append" |
"arg" |
"as" |
"assert" |
"asserta" |
"assertz" |
"at_the_end_of_stream" |
"atan" |
"atom" |
"atom_chars" |
"atom_codes" |
"atom_concat" |
"atom_length" |
"atomic" |
"bagof" |
"bound" |
"call" |
"ceiling" |
"char_code" |
"clause" |
"compound" |
"constant" |
"consult" |
"copy_term" |
"cos" |
"current_op" |
"current_prolog_flag" |
"delete" |
"destroy_object" |
"div" |
"element" |
"exp" |
"fail" |
"findall" |
"float" |
"float_fractional_part" |
"float_integer_part" |
"floor" |
"functor" |
"get" |
"get0" |
"get_theory" |
"ground" |
"halt" |
"integer" |
"is" |
"java_array_get" |
"java_array_get_boolean" |
"java_array_get_byte" |
"java_array_get_char" |
"java_array_get_double" |
"java_array_get_float" |
"java_array_get_int" |
"java_array_get_long" |
"java_array_get_short" |
"java_array_length" |
"java_array_set" |
"java_array_set_boolean" |
"java_array_set_byte" |
"java_array_set_char" |
"java_array_set_double" |
"java_array_set_float" |
"java_array_set_int" |
"java_array_set_long" |
"java_array_set_short" |
"java_call" |
"java_class" |
"java_object" |
"java_object_bt" |
"java_object_nb" |
"java_object_string" |
"length" |
"list" |
"log" |
"member" |
"mod" |
"nl" |
"nonvar" |
"nospy" |
"not" |
"notrace" |
"num_atom" |
"number" |
"number_chars" |
"number_codes" |
"once" |
"put" |
"quicksort" |
"rand_float" |
"rand_int" |
"read" |
"rem" |
"repeat" |
"retract" |
"retract_bt" |
"retract_nb" |
"returns" |
"reverse" |
"round" |
"see" |
"seeing" |
"seen" |
"set_prolog_flag" |
"set_theory" |
"setof" |
"sign" |
"sin" |
"solve_file" |
"spy" |
"sqrt" |
"sub_atom" |
"tab" |
"tell" |
"telling" |
"text_concat" |
"text_from_file" |
"text_term" |
"told" |
"trace" |
"true" |
"truncate" |
"unify_with_occurs_check" |
"var" |
"write"		{ addToken(Token.FUNCTION); }

	

	{LineTerminator}				{ addNullToken(); return firstToken; }

	{Identifier}					{ addToken(Token.IDENTIFIER); }
	
	{Variable}						{ addToken(Token.VARIABLE); }

	{WhiteSpace}					{ addToken(Token.WHITESPACE); }

	/* String/Character literals. */
	'							{ start = zzMarkedPos-1; yybegin(CHAR); }
	\"							{ start = zzMarkedPos-1; yybegin(STRING); }

	/* Comment literals. */
	{MLCBegin}	{ start = zzMarkedPos-2; yybegin(MLC); }
	/* No documentation comments */
	{LineCommentBegin}			{ start = zzMarkedPos-1; yybegin(EOL_COMMENT); }

	/* Separators. */
	{Separator}					{ addToken(Token.SEPARATOR); }
	{Separator2}					{ addToken(Token.IDENTIFIER); }

	/* Operators. */
	"*" |
"**" |
"+" |
"," |
"-" |
"-->" |
"->" |
"." |
"/" |
"//" |
"/\\" |
":-" |
";" |
"<" |
"<-" |
"<<" |
"=" |
"=.." |
"=:=" |
"=<" |
"==" |
"=\\=" |
">" |
">=" |
">>" |
"?-" |
"@<" |
"@=<" |
"@>" |
"@>=" |
"\\+" |
"\\/" |
"\\=" |
"\\==" |
"\\\\" |
"as" |
"atan" |
"cos" |
"div" |
"exp" |
"is" |
"log" |
"mod" |
"not" |
"rem" |
"returns" |
"sin" |
"sqrt" |
"~"		{ addToken(Token.OPERATOR); }

	/* Numbers */
	{IntegerLiteral}				{ addToken(Token.LITERAL_NUMBER_DECIMAL_INT); }
	{HexLiteral}					{ addToken(Token.LITERAL_NUMBER_HEXADECIMAL); }
	{FloatLiteral}					{ addToken(Token.LITERAL_NUMBER_FLOAT); }
	{ErrorNumberFormat}			{ addToken(Token.ERROR_NUMBER_FORMAT); }

	/* Ended with a line not in a string or comment. */
	<<EOF>>						{ addNullToken(); return firstToken; }

	/* Catch any other (unhandled) characters. */
	.							{ addToken(Token.IDENTIFIER); }

}


<CHAR> {
	[^\']*						{}
	[\']						{ yybegin(YYINITIAL); addToken(start,zzStartRead, Token.LITERAL_CHAR); }
	<<EOF>>						{ addToken(start,zzStartRead-1, Token.LITERAL_CHAR); return firstToken; }
}


<STRING> {
	[^\"]*						{}
	[\"]						{ yybegin(YYINITIAL); addToken(start,zzStartRead, Token.LITERAL_STRING_DOUBLE_QUOTE); }
	<<EOF>>						{ addToken(start,zzStartRead-1, Token.LITERAL_STRING_DOUBLE_QUOTE); return firstToken; }
}


<MLC> {

	[^hwf\n*]+				{}
	{URL}					{ int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_MULTILINE); start = zzMarkedPos; }
	[hwf]					{}

	\n						{ addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); return firstToken; }
	{MLCEnd}					{ yybegin(YYINITIAL); addToken(start,zzStartRead+2-1, Token.COMMENT_MULTILINE); }
	"*"						{}
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.COMMENT_MULTILINE); return firstToken; }

}


/* No documentation comment state */

<EOL_COMMENT> {
	[^hwf\n]+				{}
	{URL}					{ int temp=zzStartRead; addToken(start,zzStartRead-1, Token.COMMENT_EOL); addHyperlinkToken(temp,zzMarkedPos-1, Token.COMMENT_EOL); start = zzMarkedPos; }
	[hwf]					{}
	\n						{ addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken; }
	<<EOF>>					{ addToken(start,zzStartRead-1, Token.COMMENT_EOL); addNullToken(); return firstToken; }
}

