package alice.tuprolog;

import junit.framework.TestCase;

public class VarTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertFalse(TuTerm.createTuVar("X").isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(TuTerm.createTuVar("X").isAtomSymbol());
	}
	
	public void testIsCompound() {
		assertFalse(TuTerm.createTuVar("X").isCompound());
	}

}
