package alice.tuprolog;

import junit.framework.TestCase;

public class StructTestCase extends TestCase {
	
	public void testStructWithNullArgument() {
		try {
			new TuStruct("p", (Term) null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			new TuStruct("p", new TuInt(1), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			new TuStruct("p", new TuInt(1), new TuInt(2), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			new TuStruct("p", new TuInt(1), new TuInt(2), new TuInt(3), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			new TuStruct("p", new TuInt(1), new TuInt(2), new TuInt(3), new TuInt(4), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			new TuStruct("p", new TuInt(1), new TuInt(2), new TuInt(3), new TuInt(4), new TuInt(5), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			new TuStruct("p", new TuInt(1), new TuInt(2), new TuInt(3), new TuInt(4), new TuInt(5), new TuInt(6), null);
			fail();
		} catch (InvalidTermException expected) {}
		try {
			Term[] args = new Term[] {new TuStruct("a"), null, new TuVar("P")};
			new TuStruct("p", args);
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	public void testStructWithNullName() {
		try {
			new TuStruct(null, new TuInt(1), new TuInt(2));
			fail();
		} catch (InvalidTermException expected) {}
	}
	
	/** Structs with an empty name can only be atoms. */
	public void testStructWithEmptyName() {
		try {
			new TuStruct("", new TuInt(1), new TuInt(2));
			fail();
		} catch (InvalidTermException expected) {}
		assertEquals(0, new TuStruct("").fname().length());
	}
	
	public void testEmptyList() {
		TuStruct list = new TuStruct();
		assertTrue(list.isConsList());
		assertTrue(list.isEmptyList());
		assertEquals(0, list.listSize());
		assertEquals("[]", list.fname());
		assertEquals(0, list.getArity());
	}

	/** Another correct method of building an empty list */
	public void testEmptyListAsSquaredStruct() {
		TuStruct emptyList = new TuStruct("[]");
		assertTrue(emptyList.isConsList());
		assertTrue(emptyList.isEmptyList());
		assertEquals("[]", emptyList.fname());
		assertEquals(0, emptyList.getArity());
		assertEquals(0, emptyList.listSize());
	}
	
	/** A wrong method of building an empty list */
	public void testEmptyListAsDottedStruct() {
		TuStruct notAnEmptyList = new TuStruct(".");
		assertFalse(notAnEmptyList.isConsList());
		assertFalse(notAnEmptyList.isEmptyList());
		assertEquals(".", notAnEmptyList.fname());
		assertEquals(0, notAnEmptyList.getArity());
	}
	
	/** Use dotted structs to build lists with content */
	public void testListAsDottedStruct() {
		TuStruct notAnEmptyList = new TuStruct(".", new TuStruct("a"), new TuStruct(".", new TuStruct("b"), new TuStruct()));
		assertTrue(notAnEmptyList.isConsList());
		assertFalse(notAnEmptyList.isEmptyList());
		assertEquals(".", notAnEmptyList.fname());
		assertEquals(2, notAnEmptyList.getArity());
	}
	
	public void testListFromArgumentArray() {
		assertEquals(new TuStruct(), new TuStruct(new Term[0]));
		
		Term[] args = new Term[2];
		args[0] = new TuStruct("a");
		args[1] = new TuStruct("b");
		TuStruct list = new TuStruct(args);
		assertEquals(new TuStruct(), list.listTail().listTail());
	}
	
	public void testListSize() {
		TuStruct list = new TuStruct(new TuStruct("a"),
				       new TuStruct(new TuStruct("b"),
				           new TuStruct(new TuStruct("c"), new TuStruct())));
		assertTrue(list.isConsList());
		assertFalse(list.isEmptyList());
		assertEquals(3, list.listSize());
	}
	
	public void testNonListHead() throws InvalidTermException {
		TuStruct s = new TuStruct("f", new TuVar("X"));
		try {
			assertNotNull(s.listHead()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testNonListTail() {
		TuStruct s = new TuStruct("h", new TuInt(1));
		try {
			assertNotNull(s.listTail()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testNonListSize() throws InvalidTermException {
		TuStruct s = new TuStruct("f", new TuVar("X"));
		try {
			assertEquals(0, s.listSize()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testNonListIterator() {
		TuStruct s = new TuStruct("f", new TuInt(2));
		try {
			assertNotNull(s.listIterator()); // just to make an assertion...
			fail();
		} catch (UnsupportedOperationException e) {
			assertEquals("The structure " + s + " is not a list.", e.getMessage());
		}
	}
	
	public void testToList() {
		TuStruct emptyList = new TuStruct();
		TuStruct emptyListToList = new TuStruct(new TuStruct("[]"), new TuStruct());
		assertEquals(emptyListToList, emptyList.toList());
	}
	
	public void testToString() throws InvalidTermException {
		TuStruct emptyList = new TuStruct();
		assertEquals("[]", emptyList.toString());
		TuStruct s = new TuStruct("f", new TuVar("X"));
		assertEquals("f(X)", s.toString());
		TuStruct list = new TuStruct(new TuStruct("a"),
		          new TuStruct(new TuStruct("b"),
		        	  new TuStruct(new TuStruct("c"), new TuStruct())));
		assertEquals("[a,b,c]", list.toString());
	}
	
	public void testAppend() {
		TuStruct emptyList = new TuStruct();
		TuStruct list = new TuStruct(new TuStruct("a"),
				          new TuStruct(new TuStruct("b"),
				        	  new TuStruct(new TuStruct("c"), new TuStruct())));
		emptyList.appendDestructive(new TuStruct("a"));
		emptyList.appendDestructive(new TuStruct("b"));
		emptyList.appendDestructive(new TuStruct("c"));
		assertEquals(list, emptyList);
		TuStruct tail = new TuStruct(new TuStruct("b"),
                          new TuStruct(new TuStruct("c"), new TuStruct()));
		assertEquals(tail, emptyList.listTail());
		
		emptyList = new TuStruct();
		emptyList.appendDestructive(new TuStruct());
		assertEquals(new TuStruct(new TuStruct(), new TuStruct()), emptyList);
		
		TuStruct anotherList = new TuStruct(new TuStruct("d"),
				                 new TuStruct(new TuStruct("e"), new TuStruct()));
		list.appendDestructive(anotherList);
		assertEquals(anotherList, list.listTail().listTail().listTail().listHead());
	}
	
	public void testIteratedGoalTerm() throws Exception {
		TuVar x = new TuVar("X");
		TuStruct foo = new TuStruct("foo", x);
		TuStruct term = new TuStruct("^", x, foo);
		assertEquals(foo, term.iteratedGoalTerm());
	}
	
	public void testIsList() {
		TuStruct notList = new TuStruct(".", new TuStruct("a"), new TuStruct("b"));
		assertFalse(notList.isConsList());
	}
	
	public void testIsAtomic() {
		TuStruct emptyList = new TuStruct();
		assertTrue(emptyList.isAtomic());
		TuStruct atom = new TuStruct("atom");
		assertTrue(atom.isAtomic());
		TuStruct list = new TuStruct(new Term[] {new TuInt(0), new TuInt(1)});
		assertFalse(list.isAtomic());
		TuStruct compound = new TuStruct("f", new TuStruct("a"), new TuStruct("b"));
		assertFalse(compound.isAtomic());
		TuStruct singleQuoted = new TuStruct("'atom'");
		assertTrue(singleQuoted.isAtomic());
		TuStruct doubleQuoted = new TuStruct("\"atom\"");
		assertTrue(doubleQuoted.isAtomic());
	}
	
	public void testIsAtom() {
		TuStruct emptyList = new TuStruct();
		assertTrue(emptyList.isAtomSymbol());
		TuStruct atom = new TuStruct("atom");
		assertTrue(atom.isAtomSymbol());
		TuStruct list = new TuStruct(new Term[] {new TuInt(0), new TuInt(1)});
		assertFalse(list.isAtomSymbol());
		TuStruct compound = new TuStruct("f", new TuStruct("a"), new TuStruct("b"));
		assertFalse(compound.isAtomSymbol());
		TuStruct singleQuoted = new TuStruct("'atom'");
		assertTrue(singleQuoted.isAtomSymbol());
		TuStruct doubleQuoted = new TuStruct("\"atom\"");
		assertTrue(doubleQuoted.isAtomSymbol());
	}
	
	public void testIsCompound() {
		TuStruct emptyList = new TuStruct();
		assertFalse(emptyList.isCompound());
		TuStruct atom = new TuStruct("atom");
		assertFalse(atom.isCompound());
		TuStruct list = new TuStruct(new Term[] {new TuInt(0), new TuInt(1)});
		assertTrue(list.isCompound());
		TuStruct compound = new TuStruct("f", new TuStruct("a"), new TuStruct("b"));
		assertTrue(compound.isCompound());
		TuStruct singleQuoted = new TuStruct("'atom'");
		assertFalse(singleQuoted.isCompound());
		TuStruct doubleQuoted = new TuStruct("\"atom\"");
		assertFalse(doubleQuoted.isCompound());
	}
	
	public void testEqualsToObject() {
		TuStruct s = new TuStruct("id");
		assertFalse(s.equals(new Object()));
	}

}
