package alice.tuprolog;

import junit.framework.TestCase;

public class IntTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertTrue(TuTerm.i32(0).isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(TuTerm.i32(0).isAtomSymbol());
	}
	
	public void testIsCompound() {
		assertFalse(TuTerm.i32(0).isCompound());
	}
	
	public void testEqualsToStruct() {
		Term s = TuTerm.createNilStruct();
		TuInt zero = TuTerm.i32(0);
		assertFalse(zero.equals(s));
	}
	
	public void testEqualsToVar() throws InvalidTermException {
		TuVar x = TuTerm.createTuVar("X");
		TuInt one = TuTerm.i32(1);
		assertFalse(one.equals(x));
	}
	
	public void testEqualsToInt() {
		TuInt zero = TuTerm.i32(0);
		TuInt one = TuTerm.i32(1);
		assertFalse(zero.equals(one));
		TuInt anotherZero = TuTerm.i32(1-1);
		assertTrue(anotherZero.equals(zero));
	}
	
	public void testEqualsToLong() {
		// TODO Test Int numbers for equality with Long numbers
	}
	
	public void testEqualsToDouble() {
		TuInt integerOne = TuTerm.i32(1);
		alice.tuprolog.TuDouble doubleOne = TuTerm.f64(1);
		assertFalse(integerOne.equals(doubleOne));
	}
	
	public void testEqualsToFloat() {
		// TODO Test Int numbers for equality with Float numbers
	}

}
