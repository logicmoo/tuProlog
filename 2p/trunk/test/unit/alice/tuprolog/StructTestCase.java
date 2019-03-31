package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

public class StructTestCase extends TestCase {

    public void testStructWithNullArgument() {
        try {
            S("p", (Term) null);
            fail();
        } catch (InvalidTermException expected) {
        }
        try {
            createTuStruct2("p", createTuInt(1), null);
            fail();
        } catch (InvalidTermException expected) {
        }
        try {
            S("p", createTuInt(1), createTuInt(2), null);
            fail();
        } catch (InvalidTermException expected) {
        }
        try {
            S("p", createTuInt(1), createTuInt(2), createTuInt(3), null);
            fail();
        } catch (InvalidTermException expected) {
        }
        try {
            S("p", createTuInt(1), createTuInt(2), createTuInt(3), createTuInt(4), null);
            fail();
        } catch (InvalidTermException expected) {
        }
        try {
            S("p", createTuInt(1), createTuInt(2), createTuInt(3), createTuInt(4), createTuInt(5), null);
            fail();
        } catch (InvalidTermException expected) {
        }
        try {
            S("p", createTuInt(1), createTuInt(2), createTuInt(3), createTuInt(4), createTuInt(5), createTuInt(6), null);
            fail();
        } catch (InvalidTermException expected) {
        }
        try {
            Term[] args = new Term[] { createTuAtom("a"), null, createTuVarNamed("P") };
            createTuStructA("p", args);
            fail();
        } catch (InvalidTermException expected) {
        }
    }

    public void testStructWithNullName() {
        try {
            createTuStruct2(null, createTuInt(1), createTuInt(2));
            fail();
        } catch (InvalidTermException expected) {
        }
    }

    /** Structs with an empty name can only be atoms. */
    public void testStructWithEmptyName() {
        try {
            createTuStruct2("", createTuInt(1), createTuInt(2));
            fail();
        } catch (InvalidTermException expected) {
        }
        assertEquals(0, createTuAtom("").fname().length());
    }

    public void testEmptyList() {
        TuTerm list = createTuEmpty();
        assertTrue(list.isPlList());
        assertTrue(list.isEmptyList());
        assertEquals(0, list.listSize());
        assertEquals("[]", list.fname());
        assertEquals(0, list.getPlArity());
    }

    /** Another correct method of building an empty list */
    public void testEmptyListAsSquaredStruct() {
        TuTerm emptyList = createTuAtom("[]");
        assertTrue(emptyList.isPlList());
        assertTrue(emptyList.isEmptyList());
        assertEquals("[]", emptyList.fname());
        assertEquals(0, emptyList.getPlArity());
        assertEquals(0, emptyList.listSize());
    }

    /** A wrong method of building an empty list */
    public void testEmptyListAsDottedStruct() {
        TuTerm notAnEmptyList = createTuAtom(".");
        assertFalse(notAnEmptyList.isPlList());
        assertFalse(notAnEmptyList.isEmptyList());
        assertEquals(".", notAnEmptyList.fname());
        assertEquals(0, notAnEmptyList.getPlArity());
    }

    /** Use dotted structs to build lists with content */
    public void testListAsDottedStruct() {
        TuStruct notAnEmptyList = createTuCons( createTuAtom("a"), createTuCons( createTuAtom("b"), createTuEmpty()));
        assertTrue(notAnEmptyList.isPlList());
        assertFalse(notAnEmptyList.isEmptyList());
        assertEquals(".", notAnEmptyList.fname());
        assertEquals(2, notAnEmptyList.getPlArity());
    }

    public void testListFromArgumentArray() {
        assertEquals(TuFactory.createTuEmpty(), createTuListStruct(new Term[0]));

        Term[] args = new Term[2];
        args[0] = createTuAtom("a");
        args[1] = createTuAtom("b");
        TuTerm list = createTuListStruct(args);
        assertEquals(TuFactory.createTuEmpty(), list.getPlTail().getPlTail());
    }

    public void testListSize() {
        TuTerm list = createTuCons(TuFactory.createTuAtom("a"), createTuCons(TuFactory.createTuAtom("b"), createTuCons(TuFactory.createTuAtom("c"), createTuEmpty())));
        assertTrue(list.isPlList());
        assertFalse(list.isEmptyList());
        assertEquals(3, list.listSize());
    }

    public void testNonListHead() throws InvalidTermException {
        TuStruct s = S("f", createTuVarNamed("X"));
        try {
            assertNotNull(s.getPlHead()); // just to make an assertion...
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("The structure " + s + " is not a list.", e.getMessage());
        }
    }

    public void testNonListTail() {
        TuStruct s = S("h", createTuInt(1));
        try {
            assertNotNull(s.getPlTail()); // just to make an assertion...
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("The structure " + s + " is not a list.", e.getMessage());
        }
    }

    public void testNonListSize() throws InvalidTermException {
        TuStruct s = S("f", createTuVarNamed("X"));
        try {
            assertEquals(0, s.listSize()); // just to make an assertion...
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("The structure " + s + " is not a list.", e.getMessage());
        }
    }

    public void testNonListIterator() {
        TuStruct s = S("f", createTuInt(2));
        try {
            assertNotNull(s.listIteratorProlog()); // just to make an assertion...
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("The structure " + s + " is not a list.", e.getMessage());
        }
    }

    public void testToList() {
        TuTerm emptyList = createTuEmpty();
        TuTerm emptyListToList = createTuCons(TuFactory.createTuAtom("[]"), createTuEmpty());
        assertEquals(emptyListToList, emptyList.toList());
    }

    public void testToString() throws InvalidTermException {
        TuTerm emptyList = createTuEmpty();
        assertEquals("[]", emptyList.toString());
        TuStruct s = S("f", createTuVarNamed("X"));
        assertEquals("f(X)", s.toString());
        TuTerm list = createTuCons(TuFactory.createTuAtom("a"), createTuCons(TuFactory.createTuAtom("b"), createTuCons(TuFactory.createTuAtom("c"), createTuEmpty())));
        assertEquals("[a,b,c]", list.toString());
    }

    public void testAppend() {
        TuStruct emptyList = createStructEmpty();
        TuStruct         list = createTuCons(TuFactory.createTuAtom("a"), createTuCons(TuFactory.createTuAtom("b"), createTuCons(TuFactory.createTuAtom("c"), createTuEmpty())));
        emptyList.appendDestructive(TuFactory.createTuAtom("a"));
        emptyList.appendDestructive(TuFactory.createTuAtom("b"));
        emptyList.appendDestructive(TuFactory.createTuAtom("c"));
        assertEquals(list, emptyList);
        TuTerm tail = createTuCons(TuFactory.createTuAtom("b"), createTuCons(TuFactory.createTuAtom("c"), createTuEmpty()));
        assertEquals(tail, emptyList.getPlTail());

        emptyList = createStructEmpty();
        emptyList.appendDestructive(TuFactory.createTuEmpty());
        assertEquals(TuFactory.createTuCons(TuFactory.createTuEmpty(), createTuEmpty()), emptyList);

        TuStruct anotherList = createTuCons(TuFactory.createTuAtom("d"), createTuCons(TuFactory.createTuAtom("e"), createTuEmpty()));
        list.appendDestructive(anotherList);
        assertEquals(anotherList, list.getPlTail().getPlTail().getPlTail().getPlHead());
    }

    public void testIteratedGoalTerm() throws Exception {
        TuVar x = createTuVarNamed("X");
        TuStruct foo = S("foo", x);
        Term term = createTuStruct2("^", x, foo);
        assertEquals(foo, term.iteratedGoalTerm());
    }

    public void testIsList() {
        TuStruct notList = createTuCons( createTuAtom("a"), createTuAtom("b"));
        assertFalse(notList.isPlList());
    }

    public void testIsAtomic() {
        Term emptyList = createTuEmpty();
        assertTrue(emptyList.isAtomic());
        TuTerm atom = createTuAtom("atom");
        assertTrue(atom.isAtomic());
        TuTerm list = createTuListStruct(new Term[] { createTuInt(0), createTuInt(1) });
        assertFalse(list.isAtomic());
        Term compound = createTuStruct2("f", createTuAtom("a"), createTuAtom("b"));
        assertFalse(compound.isAtomic());
        TuTerm singleQuoted = createTuAtom("'atom'");
        assertTrue(singleQuoted.isAtomic());
        TuTerm doubleQuoted = createTuAtom("\"atom\"");
        assertTrue(doubleQuoted.isAtomic());
    }

    public void testIsAtom() {
        Term emptyList = createTuEmpty();
        assertTrue(emptyList.isAtomSymbol());
        TuTerm atom = createTuAtom("atom");
        assertTrue(atom.isAtomSymbol());
        Term list = createTuListStruct(new Term[] { createTuInt(0), createTuInt(1) });
        assertFalse(list.isAtomSymbol());
        Term compound = createTuStruct2("f", createTuAtom("a"), createTuAtom("b"));
        assertFalse(compound.isAtomSymbol());
        TuTerm singleQuoted = createTuAtom("'atom'");
        assertTrue(singleQuoted.isAtomSymbol());
        TuTerm doubleQuoted = createTuAtom("\"atom\"");
        assertTrue(doubleQuoted.isAtomSymbol());
    }

    public void testIsCompound() {
        Term emptyList = createTuEmpty();
        assertFalse(emptyList.isCompound());
        Term atom = createTuAtom("atom");
        assertFalse(atom.isCompound());
        TuTerm list = createTuListStruct(new Term[] { createTuInt(0), createTuInt(1) });
        assertTrue(list.isCompound());
        Term compound = createTuStruct2("f", createTuAtom("a"), createTuAtom("b"));
        assertTrue(compound.isCompound());
        Term singleQuoted = createTuAtom("'atom'");
        assertFalse(singleQuoted.isCompound());
        Term doubleQuoted = createTuAtom("\"atom\"");
        assertFalse(doubleQuoted.isCompound());
    }

    public void testEqualsToObject() {
        Term s = createTuAtom("id");
        assertFalse(s.equals(new Object()));
    }

}
