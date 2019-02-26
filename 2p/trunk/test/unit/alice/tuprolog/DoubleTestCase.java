package alice.tuprolog;

import junit.framework.TestCase;

public class DoubleTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertTrue(new alice.tuprolog.TuDouble(0).isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(new alice.tuprolog.TuDouble(0).isAtom());
	}
	
	public void testIsCompound() {
		assertFalse(new alice.tuprolog.TuDouble(0).isCompound());
	}
	
	public void testEqualsToStruct() {
		alice.tuprolog.TuDouble zero = new alice.tuprolog.TuDouble(0);
		TuStruct s = new TuStruct();
		assertFalse(zero.equals(s));
	}
	
	public void testEqualsToVar() throws InvalidTermException {
		alice.tuprolog.TuDouble one = new alice.tuprolog.TuDouble(1);
		TuVar x = new TuVar("X");
		assertFalse(one.equals(x));
	}
	
	public void testEqualsToDouble() {
		alice.tuprolog.TuDouble zero = new alice.tuprolog.TuDouble(0);
		alice.tuprolog.TuDouble one = new alice.tuprolog.TuDouble(1);
		assertFalse(zero.equals(one));
		alice.tuprolog.TuDouble anotherZero = new alice.tuprolog.TuDouble(0.0);
		assertTrue(anotherZero.equals(zero));
	}
	
	public void testEqualsToFloat() {
		// TODO Test Double numbers for equality with Float numbers
	}
	
	public void testEqualsToInt() {
		alice.tuprolog.TuDouble doubleOne = new alice.tuprolog.TuDouble(1.0);
		TuInt integerOne = new TuInt(1);
		assertFalse(doubleOne.equals(integerOne));
	}
	
	public void testEqualsToLong() {
		// TODO Test Double numbers for equality with Long numbers
	}

}
