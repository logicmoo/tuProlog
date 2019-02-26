package alice.tuprolog;

import junit.framework.TestCase;

public class IntTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertTrue(new TuInt(0).isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(new TuInt(0).isAtom());
	}
	
	public void testIsCompound() {
		assertFalse(new TuInt(0).isCompound());
	}
	
	public void testEqualsToStruct() {
		TuStruct s = new TuStruct();
		TuInt zero = new TuInt(0);
		assertFalse(zero.equals(s));
	}
	
	public void testEqualsToVar() throws InvalidTermException {
		TuVar x = new TuVar("X");
		TuInt one = new TuInt(1);
		assertFalse(one.equals(x));
	}
	
	public void testEqualsToInt() {
		TuInt zero = new TuInt(0);
		TuInt one = new TuInt(1);
		assertFalse(zero.equals(one));
		TuInt anotherZero = new TuInt(1-1);
		assertTrue(anotherZero.equals(zero));
	}
	
	public void testEqualsToLong() {
		// TODO Test Int numbers for equality with Long numbers
	}
	
	public void testEqualsToDouble() {
		TuInt integerOne = new TuInt(1);
		alice.tuprolog.TuDouble doubleOne = new alice.tuprolog.TuDouble(1);
		assertFalse(integerOne.equals(doubleOne));
	}
	
	public void testEqualsToFloat() {
		// TODO Test Int numbers for equality with Float numbers
	}

}
