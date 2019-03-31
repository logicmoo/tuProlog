package alice.tuprolog;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

/**
 * 
 * @author <a href="mailto:giulio.piancastelli@unibo.it">Giulio Piancastelli</a>
 */
public class StructIteratorTestCase extends TestCase {

    public void testEmptyIterator() {
        TuTerm list = createTuEmpty();
        Iterator<? extends Term> i = list.listIteratorProlog();
        assertFalse(i.hasNext());
        try {
            i.next();
            fail();
        } catch (NoSuchElementException expected) {
        }
    }

    public void testIteratorCount() {
        Term list = createTuListStruct(new Term[] { createTuInt(1), createTuInt(2), createTuInt(3), createTuInt(5),
                createTuInt(7) });
        Iterator<? extends Term> i = list.listIteratorProlog();
        int count = 0;
        for (; i.hasNext(); count++)
            i.next();
        assertEquals(5, count);
        assertFalse(i.hasNext());
    }

    public void testMultipleHasNext() {
        Term list = createTuListStruct(new Term[] { createTuAtom("p"),
                createTuAtom("q"), createTuAtom("r") });
        Iterator<? extends Term> i = list.listIteratorProlog();
        assertTrue(i.hasNext());
        assertTrue(i.hasNext());
        assertTrue(i.hasNext());
        assertEquals(TuFactory.createTuAtom("p"), i.next());
    }

    public void testMultipleNext() {
        Term list = createTuListStruct(new Term[] { createTuInt(0), createTuInt(1), createTuInt(2), createTuInt(3),
                createTuInt(5), createTuInt(7) });
        Iterator<? extends Term> i = list.listIteratorProlog();
        assertTrue(i.hasNext());
        i.next(); // skip the first term
        assertEquals(TuFactory.createTuInt(1), i.next());
        assertEquals(TuFactory.createTuInt(2), i.next());
        assertEquals(TuFactory.createTuInt(3), i.next());
        assertEquals(TuFactory.createTuInt(5), i.next());
        assertEquals(TuFactory.createTuInt(7), i.next());
        // no more terms
        assertFalse(i.hasNext());
        try {
            i.next();
            fail();
        } catch (NoSuchElementException expected) {
        }
    }

    public void testRemoveOperationNotSupported() {
        TuTerm list = createTuCons(TuFactory.createTuInt(1), createTuEmpty());
        Iterator<? extends Term> i = list.listIteratorProlog();
        assertNotNull(i.next());
        try {
            i.remove();
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

}
