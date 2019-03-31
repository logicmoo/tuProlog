package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

public class ParserTestCase extends TestCase {
	
	public void testReadingTerms() throws InvalidTermException {
		TuParser p = new TuParser("hello.");
		TuTerm result = createTuAtom("hello");
		assertEquals(result, p.nextTerm(true));
	}
	
	public void testReadingEOF() throws InvalidTermException {
		TuParser p = new TuParser("");
		assertNull(p.nextTerm(false));
	}
	
	public void testUnaryPlusOperator() {
		TuParser p = new TuParser("n(+100).\n");
        // SICStus Prolog interprets "n(+100)" as "n(100)"
		// GNU Prolog interprets "n(+100)" as "n(+(100))"
		// The ISO Standard says + is not a unary operator
		try {
			assertNotNull(p.nextTerm(true));
			fail();
		} catch (InvalidTermException e) {}
	}
	
	public void testUnaryMinusOperator() throws InvalidTermException {
		TuParser p = new TuParser("n(-100).\n");
		// TODO Check the interpretation by other engines
		// SICStus Prolog interprets "n(+100)" as "n(100)"
		// GNU Prolog interprets "n(+100)" as "n(+(100))"
		// What does the ISO Standard say about that?
		TuStruct result = S("n", createTuInt(-100));
		result.resolveTerm();
		assertEquals(result, p.nextTerm(true));
	}
	
	public void testBinaryMinusOperator() throws InvalidTermException {
		String s = "abs(3-11)";
		TuParser p = new TuParser(s);
		TuStruct result = S("abs", createTuStruct2("-", createTuInt(3), createTuInt(11)));
		assertEquals(result, p.nextTerm(false));
	}
	
	public void testListWithTail() throws InvalidTermException {
		TuParser p = new TuParser("[p|Y]");
		TuTerm result = createTuCons(TuFactory.createTuAtom("p"), new TuVar("Y"));
		result.resolveTerm();
		assertEquals(result, p.nextTerm(false));
	}
	
	public void testBraces() throws InvalidTermException {
		String s = "{a,b,[3,{4,c},5],{a,b}}";
		TuParser parser = new TuParser(s);
		assertEquals(s, parser.nextTerm(false).toString());
	}
	
	public void testUnivOperator() throws InvalidTermException {
		TuParser p = new TuParser("p =.. q.");
		TuStruct result = createTuStruct2("=..", createTuAtom("p"), createTuAtom("q"));
		assertEquals(result, p.nextTerm(true));
	}
	
	public void testDotOperator() throws InvalidTermException {
		String s = "class('java.lang.Integer').'MAX_VALUE'";
		DefaultOperatorManager om = new DefaultOperatorManager();
		om.opNew(".", "xfx", 600);
		TuParser p = new TuParser(om, s);
		TuStruct result = createTuCons( S("class", createTuAtom("java.lang.Integer")), createTuAtom("MAX_VALUE"));
		assertEquals(result, p.nextTerm(false));
	}
	
	public void testBracketedOperatorAsTerm() throws InvalidTermException {
		String s = "u (b1) b2 (b3)";
		DefaultOperatorManager om = new DefaultOperatorManager();
		om.opNew("u", "fx", 200);
		om.opNew("b1", "yfx", 400);
		om.opNew("b2", "yfx", 500);
		om.opNew("b3", "yfx", 300);
		TuParser p = new TuParser(om, s);
		TuStruct result = createTuStruct2("b2", S("u", createTuAtom("b1")), createTuAtom("b3"));
		assertEquals(result, p.nextTerm(false));
	}
	
	public void testBracketedOperatorAsTerm2() throws InvalidTermException {
		String s = "(u) b1 (b2) b3 a";
		DefaultOperatorManager om = new DefaultOperatorManager();
		om.opNew("u", "fx", 200);
		om.opNew("b1", "yfx", 400);
		om.opNew("b2", "yfx", 500);
		om.opNew("b3", "yfx", 300);
		TuParser p = new TuParser(om, s);
		TuStruct result = createTuStruct2("b1", createTuAtom("u"), createTuStruct2("b3", createTuAtom("b2"), createTuAtom("a")));
		assertEquals(result, p.nextTerm(false));
	}
	
	public void testIntegerBinaryRepresentation() throws InvalidTermException {
		String n = "0b101101";
		TuParser p = new TuParser(n);
		alice.tuprolog.TuNumber result = createTuInt(45);
		assertEquals(result, p.nextTerm(false));
		String invalid = "0b101201";
		try {
			new TuParser(invalid).nextTerm(false);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testIntegerOctalRepresentation() throws InvalidTermException {
		String n = "0o77351";
		TuParser p = new TuParser(n);
		alice.tuprolog.TuNumber result = createTuInt(32489);
		assertEquals(result, p.nextTerm(false));
		String invalid = "0o78351";
		try {
			new TuParser(invalid).nextTerm(false);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testIntegerHexadecimalRepresentation() throws InvalidTermException {
		String n = "0xDECAF";
		TuParser p = new TuParser(n);
		alice.tuprolog.TuNumber result = createTuInt(912559);
		assertEquals(result, p.nextTerm(false));
		String invalid = "0xG";
		try {
			new TuParser(invalid).nextTerm(false);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testEmptyDCGAction() throws InvalidTermException {
		String s = "{}";
		TuParser p = new TuParser(s);
		TuTerm result = createTuAtom("{}");
		assertEquals(result, p.nextTerm(false));
	}
	
	public void testSingleDCGAction() throws InvalidTermException {
		String s = "{hello}";
		TuParser p = new TuParser(s);
		TuStruct result = S("{}", createTuAtom("hello"));
		assertEquals(result, p.nextTerm(false));
	}
	
	public void testMultipleDCGAction() throws InvalidTermException {
		String s = "{a, b, c}";
		TuParser p = new TuParser(s);
		TuStruct result = S("{}", createTuStruct2(",", createTuAtom("a"), TuFactory
                .createTuStruct2(",", createTuAtom("b"), createTuAtom("c"))));
		assertEquals(result, p.nextTerm(false));
	}
	
	// This is an error both in 2.0.1 and in 2.1... don't know why, though.
//	public void testDCGActionWithOperators() throws Exception {
//        String input = "{A =.. B, hotel, 2}";
//        Struct result = new Struct("{}",
//                            new Struct(",", new Struct("=..", new Var("A"), new Var("B")),
//                                new Struct(",", new Struct("hotel"), new Int(2))));
//        result.resolveTerm();
//        Parser p = new Parser(input);
//        assertEquals(result, p.nextTerm(false));
//	}
	
	public void testMissingDCGActionElement() {
		String s = "{1, 2, , 4}";
		TuParser p = new TuParser(s);
		try {
			p.nextTerm(false);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testDCGActionCommaAsAnotherSymbol() {
		String s = "{1 @ 2 @ 4}";
		TuParser p = new TuParser(s);
		try {
			p.nextTerm(false);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testUncompleteDCGAction() {
		String s = "{1, 2,}";
		TuParser p = new TuParser(s);
		try {
			p.nextTerm(false);
			fail();
		} catch (InvalidTermException expected) {}
		
		s = "{1, 2";
		p = new TuParser(s);
		try {
			p.nextTerm(false);
			fail();
		} catch (InvalidTermException expected) {}
	}

	public void testMultilineComments() throws InvalidTermException {
		String theory = "t1." + "\n" +
		                "/*" + "\n" +
		                "t2" + "\n" +
		                "*/" + "\n" +
		                "t3." + "\n";
		TuParser p = new TuParser(theory);
		assertEquals(TuFactory.createTuAtom("t1"), p.nextTerm(true));
		assertEquals(TuFactory.createTuAtom("t3"), p.nextTerm(true));
	}
	
	public void testSingleQuotedTermWithInvalidLineBreaks() {
		String s = "out('"+
		           "can_do(X).\n"+
		           "can_do(Y).\n"+
	               "').";
		TuParser p = new TuParser(s);
		try {
			p.nextTerm(true);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	// TODO More tests on Parser
	
	// Character code for Integer representation
	
	// :-op(500, yfx, v). 3v2 NOT CORRECT, 3 v 2 CORRECT
	// 3+2 CORRECT, 3 + 2 CORRECT
	
	// +(2, 3) is now acceptable
	// what about f(+)

}
