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
		TuStruct list = new TuStruct();
		Iterator<? extends Term> i = list.listIterator();
		assertFalse(i.hasNext());
		try {
			i.next();
			fail();
		} catch (NoSuchElementException expected) {}
	}
	
	public void testIteratorCount() {
		TuStruct list = new TuStruct(new Term[] {new TuInt(1), new TuInt(2), new TuInt(3), new TuInt(5), new TuInt(7)});
		Iterator<? extends Term> i = list.listIterator();
		int count = 0;
		for (; i.hasNext(); count++)
			i.next();
		assertEquals(5, count);
		assertFalse(i.hasNext());
	}
	
	public void testMultipleHasNext() {
		TuStruct list = new TuStruct(new Term[] {new TuStruct("p"), new TuStruct("q"), new TuStruct("r")});
		Iterator<? extends Term> i = list.listIterator();
		assertTrue(i.hasNext());
		assertTrue(i.hasNext());
		assertTrue(i.hasNext());
		assertEquals(new TuStruct("p"), i.next());
	}
	
	public void testMultipleNext() {
		TuStruct list = new TuStruct(new Term[] {new TuInt(0), new TuInt(1), new TuInt(2), new TuInt(3), new TuInt(5), new TuInt(7)});
		Iterator<? extends Term> i = list.listIterator();
		assertTrue(i.hasNext());
		i.next(); // skip the first term
		assertEquals(new TuInt(1), i.next());
		assertEquals(new TuInt(2), i.next());
		assertEquals(new TuInt(3), i.next());
		assertEquals(new TuInt(5), i.next());
		assertEquals(new TuInt(7), i.next());
		// no more terms
		assertFalse(i.hasNext());
		try {
			i.next();
			fail();
		} catch (NoSuchElementException expected) {}
	}
	
	public void testRemoveOperationNotSupported() {
		TuStruct list = new TuStruct(new TuInt(1), new TuStruct());
		Iterator<? extends Term> i = list.listIterator();
		assertNotNull(i.next());
		try {
			i.remove();
			fail();
		} catch (UnsupportedOperationException expected) {}
	}

}
