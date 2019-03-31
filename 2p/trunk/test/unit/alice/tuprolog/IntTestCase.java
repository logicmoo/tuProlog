package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

public class IntTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertTrue(TuFactory.createTuInt(0).isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(TuFactory.createTuInt(0).isAtomSymbol());
	}
	
	public void testIsCompound() {
		assertFalse(TuFactory.createTuInt(0).isCompound());
	}
	
	public void testEqualsToStruct() {
	    TuTerm s = createTuEmpty();
		TuInt zero = createTuInt(0);
		assertFalse(zero.equals(s));
	}
	
	public void testEqualsToVar() throws InvalidTermException {
		TuVar x = createTuVarNamed("X");
		TuInt one = createTuInt(1);
		assertFalse(one.equals(x));
	}
	
	public void testEqualsToInt() {
		TuInt zero = createTuInt(0);
		TuInt one = createTuInt(1);
		assertFalse(zero.equals(one));
		TuInt anotherZero = createTuInt(1-1);
		assertTrue(anotherZero.equals(zero));
	}
	
	public void testEqualsToLong() {
		// TODO Test Int numbers for equality with Long numbers
	}
	
	public void testEqualsToDouble() {
		TuInt integerOne = createTuInt(1);
		alice.tuprolog.TuDouble doubleOne = createTuDouble(1);
		assertFalse(integerOne.equals(doubleOne));
	}
	
	public void testEqualsToFloat() {
		// TODO Test Int numbers for equality with Float numbers
	}

}
