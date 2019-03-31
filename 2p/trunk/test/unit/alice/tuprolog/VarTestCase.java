package alice.tuprolog;

import junit.framework.TestCase;

public class VarTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertFalse(new TuVar("X").isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(new TuVar("X").isAtomSymbol());
	}
	
	public void testIsCompound() {
		assertFalse(new TuVar("X").isCompound());
	}

}
