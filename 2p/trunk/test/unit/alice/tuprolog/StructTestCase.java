package alice.tuprolog;

import junit.framework.TestCase;

public class StructTestCase extends TestCase {
	
	public void testStructWithNullArgument() {
		try {
			TuStruct.createTuStruct1("p", (Term) null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			TuStruct.createTuStruct2("p", TuTerm.i32(1), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			TuStruct.createSTRUCT("p", TuTerm.i32(1), TuTerm.i32(2), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			TuStruct.createSTRUCT("p", TuTerm.i32(1), TuTerm.i32(2), TuTerm.i32(3), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			TuStruct.createSTRUCT("p", TuTerm.i32(1), TuTerm.i32(2), TuTerm.i32(3), TuTerm.i32(4), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			TuStruct.createSTRUCT("p", TuTerm.i32(1), TuTerm.i32(2), TuTerm.i32(3), TuTerm.i32(4), TuTerm.i32(5), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			TuStruct.createSTRUCT("p", TuTerm.i32(1), TuTerm.i32(2), TuTerm.i32(3), TuTerm.i32(4), TuTerm.i32(5), TuTerm.i32(6), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			Term[] args = new Term[] {TuTerm.createAtomTerm("a"), null, TuTerm.createTuVar("P")};
			TuStruct.createTuStructA("p", args);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testStructWithNullName() {
		try {
			TuStruct.createTuStruct2(null, TuTerm.i32(1), TuTerm.i32(2));
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	/** Structs with an empty name can only be atoms. */
	public void testStructWithEmptyName() {
		try {
			TuStruct.createTuStruct2("", TuTerm.i32(1), TuTerm.i32(2));
			fail();
		} catch (InvalidTermException expected) {}
		assertEquals(0, TuTerm.createAtomTerm("").getName().length());
	}
	
	public void testEmptyList() {
		TuStruct list = TuTerm.createAppendableStruct();
		assertTrue(list.isList());
		assertTrue(list.isEmptyList());
		assertEquals(0, list.listSize());
		assertEquals("[]", list.getName());
		assertEquals(0, list.getArity());
	}

	/** Another correct method of building an empty list */
	public void testEmptyListAsSquaredStruct() {
		TuStruct emptyList = TuTerm.createAtomTerm("[]");
		assertTrue(emptyList.isList());
		assertTrue(emptyList.isEmptyList());
		assertEquals("[]", emptyList.getName());
		assertEquals(0, emptyList.getArity());
		assertEquals(0, emptyList.listSize());
	}
	
	/** A wrong method of building an empty list */
	public void testEmptyListAsDottedStruct() {
		TuStruct notAnEmptyList = TuTerm.createAtomTerm(".");
		assertFalse(notAnEmptyList.isList());
		assertFalse(notAnEmptyList.isEmptyList());
		assertEquals(".", notAnEmptyList.getName());
		assertEquals(0, notAnEmptyList.getArity());
	}
	
	/** Use dotted structs to build lists with content */
	public void testListAsDottedStruct() {
		TuStruct notAnEmptyList = TuStruct.createTuStruct2(".", TuTerm.createAtomTerm("a"), TuStruct.createTuStruct2(".", TuTerm.createAtomTerm("b"), TuTerm.createAppendableStruct()));
		assertTrue(notAnEmptyList.isList());
		assertFalse(notAnEmptyList.isEmptyList());
		assertEquals(".", notAnEmptyList.getName());
		assertEquals(2, notAnEmptyList.getArity());
	}
	
	public void testListFromArgumentArray() {
		assertEquals(TuTerm.createAppendableStruct(), TuStruct.createTuList(new Term[0]));
		
		Term[] args = new Term[2];
		args[0] = TuTerm.createAtomTerm("a");
		args[1] = TuTerm.createAtomTerm("b");
		TuStruct list = TuStruct.createTuList(args);
		assertEquals(TuTerm.createAppendableStruct(), list.listTail().listTail());
	}
	
	public void testListSize() {
		TuStruct list = TuTerm.createTuCons(TuTerm.createAtomTerm("a"), TuTerm.createTuCons(TuTerm.createAtomTerm("b"), TuTerm.createTuCons(TuTerm.createAtomTerm("c"), TuTerm.createAppendableStruct())));
		assertTrue(list.isList());
		assertFalse(list.isEmptyList());
		assertEquals(3, list.listSize());
	}
	
	public void testNonListHead() throws InvalidTermException {
		TuStruct s = TuStruct.createTuStruct1("f", TuTerm.createTuVar("X"));
		try {
			assertNotNull(s.listHead()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testNonListTail() {
		TuStruct s = TuStruct.createTuStruct1("h", TuTerm.i32(1));
		try {
			assertNotNull(s.listTail()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testNonListSize() throws InvalidTermException {
		TuStruct s = TuStruct.createTuStruct1("f", TuTerm.createTuVar("X"));
		try {
			assertEquals(0, s.listSize()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testNonListIterator() {
		TuStruct s = TuStruct.createTuStruct1("f", TuTerm.i32(2));
		try {
			assertNotNull(s.listIterator()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testToList() {
		TuStruct emptyList = TuTerm.createAppendableStruct();
		TuStruct emptyListToList = TuTerm.createTuCons(TuTerm.createAtomTerm("[]"), TuTerm.createAppendableStruct());
		assertEquals(emptyListToList, emptyList.toList());
	}
	
	public void testToString() throws InvalidTermException {
		TuStruct emptyList = TuTerm.createAppendableStruct();
		assertEquals("[]", emptyList.toString());
		TuStruct s = TuStruct.createTuStruct1("f", TuTerm.createTuVar("X"));
		assertEquals("f(X)", s.toString());
		TuStruct list = TuTerm.createTuCons(TuTerm.createAtomTerm("a"), TuTerm.createTuCons(TuTerm.createAtomTerm("b"), TuTerm.createTuCons(TuTerm.createAtomTerm("c"), TuTerm.createAppendableStruct())));
		assertEquals("[a,b,c]", list.toString());
	}
	
	public void testAppend() {
		TuStruct emptyList = TuTerm.createAppendableStruct();
		TuStruct list = TuTerm.createTuCons(TuTerm.createAtomTerm("a"), TuTerm.createTuCons(TuTerm.createAtomTerm("b"), TuTerm.createTuCons(TuTerm.createAtomTerm("c"), TuTerm.createAppendableStruct())));
		emptyList.append(TuTerm.createAtomTerm("a"));
		emptyList.append(TuTerm.createAtomTerm("b"));
		emptyList.append(TuTerm.createAtomTerm("c"));
		assertEquals(list, emptyList);
		TuStruct tail = TuTerm.createTuCons(TuTerm.createAtomTerm("b"), TuTerm.createTuCons(TuTerm.createAtomTerm("c"), TuTerm.createAppendableStruct()));
		assertEquals(tail, emptyList.listTail());
		
		emptyList = TuTerm.createAppendableStruct();
		emptyList.append(TuTerm.createAppendableStruct());
		assertEquals(TuTerm.createTuCons(TuTerm.createAppendableStruct(), TuTerm.createAppendableStruct()), emptyList);
		
		TuStruct anotherList = TuTerm.createTuCons(TuTerm.createAtomTerm("d"), TuTerm.createTuCons(TuTerm.createAtomTerm("e"), TuTerm.createAppendableStruct()));
		list.append(anotherList);
		assertEquals(anotherList, list.listTail().listTail().listTail().listHead());
	}
	
	public void testIteratedGoalTerm() throws Exception {
		TuVar x = TuTerm.createTuVar("X");
		TuStruct foo = TuStruct.createTuStruct1("foo", x);
		TuStruct term = TuStruct.createTuStruct2("^", x, foo);
		assertEquals(foo, term.iteratedGoalTerm());
	}
	
	public void testIsList() {
		TuStruct notList = TuStruct.createTuStruct2(".", TuTerm.createAtomTerm("a"), TuTerm.createAtomTerm("b"));
		assertFalse(notList.isList());
	}
	
	public void testIsAtomic() {
		TuStruct emptyList = TuTerm.createAppendableStruct();
		assertTrue(emptyList.isAtomic());
		TuStruct atom = TuTerm.createAtomTerm("atom");
		assertTrue(atom.isAtomic());
		TuStruct list = TuStruct.createTuList(new Term[] {TuTerm.i32(0), TuTerm.i32(1)});
		assertFalse(list.isAtomic());
		TuStruct compound = TuStruct.createTuStruct2("f", TuTerm.createAtomTerm("a"), TuTerm.createAtomTerm("b"));
		assertFalse(compound.isAtomic());
		TuStruct singleQuoted = TuTerm.createAtomTerm("'atom'");
		assertTrue(singleQuoted.isAtomic());
		TuStruct doubleQuoted = TuTerm.createAtomTerm("\"atom\"");
		assertTrue(doubleQuoted.isAtomic());
	}
	
	public void testIsAtom() {
		TuStruct emptyList = TuTerm.createAppendableStruct();
		assertTrue(emptyList.isAtomSymbol());
		TuStruct atom = TuTerm.createAtomTerm("atom");
		assertTrue(atom.isAtomSymbol());
		TuStruct list = TuStruct.createTuList(new Term[] {TuTerm.i32(0), TuTerm.i32(1)});
		assertFalse(list.isAtomSymbol());
		TuStruct compound = TuStruct.createTuStruct2("f", TuTerm.createAtomTerm("a"), TuTerm.createAtomTerm("b"));
		assertFalse(compound.isAtomSymbol());
		TuStruct singleQuoted = TuTerm.createAtomTerm("'atom'");
		assertTrue(singleQuoted.isAtomSymbol());
		TuStruct doubleQuoted = TuTerm.createAtomTerm("\"atom\"");
		assertTrue(doubleQuoted.isAtomSymbol());
	}
	
	public void testIsCompound() {
		TuStruct emptyList = TuTerm.createAppendableStruct();
		assertFalse(emptyList.isCompound());
		TuStruct atom = TuTerm.createAtomTerm("atom");
		assertFalse(atom.isCompound());
		TuStruct list = TuStruct.createTuList(new Term[] {TuTerm.i32(0), TuTerm.i32(1)});
		assertTrue(list.isCompound());
		TuStruct compound = TuStruct.createTuStruct2("f", TuTerm.createAtomTerm("a"), TuTerm.createAtomTerm("b"));
		assertTrue(compound.isCompound());
		TuStruct singleQuoted = TuTerm.createAtomTerm("'atom'");
		assertFalse(singleQuoted.isCompound());
		TuStruct doubleQuoted = TuTerm.createAtomTerm("\"atom\"");
		assertFalse(doubleQuoted.isCompound());
	}
	
	public void testEqualsToObject() {
		TuStruct s = TuTerm.createAtomTerm("id");
		assertFalse(s.equals(new Object()));
	}

}
