package alice.tuprolog;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * 
 * @author <a href="mailto:giulio.piancastelli@unibo.it">Giulio Piancastelli</a>
 */
public class StructIteratorTestCase extends TestCase {
	
	public void testEmptyIterator() {
		TuStruct list = TuTerm.createAppendableStruct();
		Iterator<? extends Term> i = list.listIterator();
		assertFalse(i.hasNext());
		try {
			i.next();
			fail();
		} catch (NoSuchElementException expected) {}
	}
	
	public void testIteratorCount() {
		TuStruct list = TuStruct.createTuList(new Term[] {TuTerm.i32(1), TuTerm.i32(2), TuTerm.i32(3), TuTerm.i32(5), TuTerm.i32(7)});
		Iterator<? extends Term> i = list.listIterator();
		int count = 0;
		for (; i.hasNext(); count++)
			i.next();
		assertEquals(5, count);
		assertFalse(i.hasNext());
	}
	
	public void testMultipleHasNext() {
		TuStruct list = TuStruct.createTuList(new Term[] {TuTerm.createAtomTerm("p"), TuTerm.createAtomTerm("q"), TuTerm.createAtomTerm("r")});
		Iterator<? extends Term> i = list.listIterator();
		assertTrue(i.hasNext());
		assertTrue(i.hasNext());
		assertTrue(i.hasNext());
		assertEquals(TuTerm.createAtomTerm("p"), i.next());
	}
	
	public void testMultipleNext() {
		TuStruct list = TuStruct.createTuList(new Term[] {TuTerm.i32(0), TuTerm.i32(1), TuTerm.i32(2), TuTerm.i32(3), TuTerm.i32(5), TuTerm.i32(7)});
		Iterator<? extends Term> i = list.listIterator();
		assertTrue(i.hasNext());
		i.next(); // skip the first term
		assertEquals(TuTerm.i32(1), i.next());
		assertEquals(TuTerm.i32(2), i.next());
		assertEquals(TuTerm.i32(3), i.next());
		assertEquals(TuTerm.i32(5), i.next());
		assertEquals(TuTerm.i32(7), i.next());
		// no more terms
		assertFalse(i.hasNext());
		try {
			i.next();
			fail();
		} catch (NoSuchElementException expected) {}
	}
	
	public void testRemoveOperationNotSupported() {
		TuStruct list = TuTerm.createTuCons(TuTerm.i32(1), TuTerm.createNilStruct());
		Iterator<? extends Term> i = list.listIterator();
		assertNotNull(i.next());
		try {
			i.remove();
			fail();
		} catch (UnsupportedOperationException expected) {}
	}

}
