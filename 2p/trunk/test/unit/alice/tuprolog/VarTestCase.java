package alice.tuprolog;

import junit.framework.TestCase;

public class VarTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertFalse(TuFactory.createTuVarNamed("X").isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(TuFactory.createTuVarNamed("X").isAtomSymbol());
	}
	
	public void testIsCompound() {
		assertFalse(TuFactory.createTuVarNamed("X").isCompound());
	}

}
