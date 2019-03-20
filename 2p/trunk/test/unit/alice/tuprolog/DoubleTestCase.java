package alice.tuprolog;

import junit.framework.TestCase;

public class DoubleTestCase extends TestCase {

    public void testIsAtomic() {
        assertTrue(TuTerm.f64(0).isAtomic());
    }

    public void testIsAtom() {
        assertFalse(TuTerm.f64(0).isAtomSymbol());
    }

    public void testIsCompound() {
        assertFalse(TuTerm.f64(0).isCompound());
    }

    public void testEqualsToStruct() {
        alice.tuprolog.TuDouble zero = TuTerm.f64(0);
        Term s = TuTerm.createNilStruct();
        assertFalse(zero.equals(s));
    }

    public void testEqualsToVar() throws InvalidTermException {
        alice.tuprolog.TuDouble one = TuTerm.f64(1);
        TuVar x = TuTerm.createTuVar("X");
        assertFalse(one.equals(x));
    }

    public void testEqualsToDouble() {
        alice.tuprolog.TuDouble zero = TuTerm.f64(0);
        alice.tuprolog.TuDouble one = TuTerm.f64(1);
        assertFalse(zero.equals(one));
        alice.tuprolog.TuDouble anotherZero = TuTerm.f64(0.0);
        assertTrue(anotherZero.equals(zero));
    }

    public void testEqualsToFloat() {
        // TODO Test Double numbers for equality with Float numbers
    }

    public void testEqualsToInt() {
        alice.tuprolog.TuDouble doubleOne = TuTerm.f64(1.0);
        TuInt integerOne = TuTerm.i32(1);
        assertFalse(doubleOne.equals(integerOne));
    }

    public void testEqualsToLong() {
        // TODO Test Double numbers for equality with Long numbers
    }

}
