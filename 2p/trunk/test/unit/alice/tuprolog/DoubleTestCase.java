package alice.tuprolog;

import junit.framework.TestCase;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

public class DoubleTestCase extends TestCase {
	
	public void testIsAtomic() {
		assertTrue(TuFactory.createTuDouble(0).isAtomic());
	}
	
	public void testIsAtom() {
		assertFalse(TuFactory.createTuDouble(0).isAtomSymbol());
	}
	
	public void testIsCompound() {
		assertFalse(TuFactory.createTuDouble(0).isCompound());
	}
	
	public void testEqualsToStruct() {
		alice.tuprolog.TuDouble zero = createTuDouble(0);
		Term s = createTuEmpty();
		assertFalse(zero.equals(s));
	}
	
	public void testEqualsToVar() throws InvalidTermException {
		alice.tuprolog.TuDouble one = createTuDouble(1);
		TuVar x = new TuVar("X");
		assertFalse(one.equals(x));
	}
	
	public void testEqualsToDouble() {
		alice.tuprolog.TuDouble zero = createTuDouble(0);
		alice.tuprolog.TuDouble one = createTuDouble(1);
		assertFalse(zero.equals(one));
		alice.tuprolog.TuDouble anotherZero = createTuDouble(0.0);
		assertTrue(anotherZero.equals(zero));
	}
	
	public void testEqualsToFloat() {
		// TODO Test Double numbers for equality with Float numbers
	}
	
	public void testEqualsToInt() {
		alice.tuprolog.TuDouble doubleOne = createTuDouble(1.0);
		TuInt integerOne = createTuInt(1);
		assertFalse(doubleOne.equals(integerOne));
	}
	
	public void testEqualsToLong() {
		// TODO Test Double numbers for equality with Long numbers
	}

}
