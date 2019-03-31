package alice.tuprolog;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import static alice.tuprolog.TuFactory.*;
import static alice.tuprolog.TuFactory.*;

/**
 * 
 * @author <a href="mailto:giulio.piancastelli@unibo.it">Giulio Piancastelli</a>
 */
public class TermIteratorTestCase extends TestCase {
	
	public void testEmptyIterator() {
		String theory = "";
		Iterator<Term> i = getIterator(theory);
		assertFalse(i.hasNext());
		try {
			i.next();
			fail();
		} catch (NoSuchElementException expected) {}
	}
	
	public void testIterationCount() {
		String theory = "q(1)." + "\n" +
		                "q(2)." + "\n" +
		                "q(3)." + "\n" +
		                "q(5)." + "\n" +
		                "q(7).";
		Iterator<Term> i = getIterator(theory);
		int count = 0;
		for (; i.hasNext(); count++)
			i.next();
		assertEquals(5, count);
		assertFalse(i.hasNext());
	}
	
	public void testMultipleHasNext() {
		String theory = "p. q. r.";
		Iterator<Term> i = getIterator(theory);
		assertTrue(i.hasNext());
		assertTrue(i.hasNext());
		assertTrue(i.hasNext());
		assertEquals(TuFactory.createTuAtom("p"), i.next());
	}
	
	public void testMultipleNext() {
		String theory = "p(X):-q(X),X>1." + "\n" +
		                "q(1)." + "\n" +
						"q(2)." + "\n" +
						"q(3)." + "\n" +
						"q(5)." + "\n" +
						"q(7).";
		Iterator<Term> i = getIterator(theory);
		assertTrue(i.hasNext());
		i.next(); // skip the first term
		assertEquals(TuFactory.S("q", createTuInt(1)), i.next());
		assertEquals(TuFactory.S("q", createTuInt(2)), i.next());
		assertEquals(TuFactory.S("q", createTuInt(3)), i.next());
		assertEquals(TuFactory.S("q", createTuInt(5)), i.next());
		assertEquals(TuFactory.S("q", createTuInt(7)), i.next());
		// no more terms
		assertFalse(i.hasNext());
		try {
			i.next();
			fail();
		} catch (NoSuchElementException expected) {}
	}
	
	public void testIteratorOnInvalidTerm() {
		String t = "q(1)"; // missing the End-Of-Clause!
		try {
			getIterator(t);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testIterationOnInvalidTheory() {
		String theory = "q(1)." + "\n" +
		                "q(2)." + "\n" +
						"q(3) " + "\n" + // missing the End-Of-Clause!
						"q(5)." + "\n" +
						"q(7).";
		TuStruct firstTerm = S("q", createTuInt(1));
		TuStruct secondTerm = S("q", createTuInt(2));
		Iterator<Term> i1 = getIterator(theory);
		assertTrue(i1.hasNext());
		assertEquals(firstTerm, i1.next());
		assertTrue(i1.hasNext());
		assertEquals(secondTerm, i1.next());
		try {
			i1.hasNext();
			fail();
		} catch (InvalidTermException expected) {}
		Iterator<Term> i2 = getIterator(theory);
		assertEquals(firstTerm, i2.next());
		assertEquals(secondTerm, i2.next());
		try {
			i2.next();
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testRemoveOperationNotSupported() {
		String theory = "p(1).";
		Iterator<Term> i = getIterator(theory);
		assertNotNull(i.next());
		try {
			i.remove();
			fail();
		} catch (UnsupportedOperationException expected) {}
	}

}
