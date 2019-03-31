/**
 * 
 */
package alice.tuprolog;

/**
 * @author Administrator
 *
 */
public class TuFactory {
    /**
     * Static service to create a Term from a string.
     * @param st the string representation of the term
     * @return the term represented by the string
     * @throws InvalidTermException if the string does not represent a valid term
     */
    public static Term createTerm(String st) {
        return TuParser.parseSingleTerm(st);
    }

    /**
     * @deprecated Use {@link Term#createTerm(String)} instead.
     */
    @Deprecated
    public static Term parse(String st) {
        return createTerm(st);
    }

    /**
     * Static service to create a Term from a string, providing an
     * external operator manager.
     * @param st the string representation of the term
     * @param op the operator manager used to build the term
     * @return the term represented by the string
     * @throws InvalidTermException if the string does not represent a valid term
     */
    public static Term createTerm(String st, OperatorManager op) {
        return TuParser.parseSingleTerm(st, op);
    }

    /**
     * @deprecated Use {@link Term#createTerm(String, OperatorManager)} instead.
     */
    @Deprecated
    public static Term parse(String st, OperatorManager op) {
        return createTerm(st, op);
    }

    /**
     * Gets an iterator providing
     * a term stream from a source text
     */
    public static java.util.Iterator<Term> getIterator(String text) {
        return new TuParser(text).iterator();
    }

    public static TuTerm createTuEmpty() {
        return new TuStruct();
    }
    
    public static TuStruct createStructEmpty() {
        return new TuStruct();
    }

    public static TuTerm createTuAtom(String f) {
        return new TuStruct(f);
    }

    public static TuStruct S(String f, Term... args) {
        return new TuStruct(f, args.clone());
    }

    public static TuStruct createTuCons(Term h, Term t) {
        return new TuStruct(h, t);
    }

    public static TuDouble createTuDouble(double v) {
        return new TuDouble(v);
    }

    public static TuInt createTuInt(int v) {
        return new TuInt(v);
    }

    public static TuLong createTuLong(long v) {
        return new TuLong(v);
    }

    public static TuStruct createTuStructA(String f, Term[] argList) {
        return new TuStruct(f, argList);
    }

    public static TuStruct createTuStruct2(String f, Term at0, Term at1) {
        return S(f, at0, at1);
    }

    public static TuStruct createTuListStruct(Term[] argList) {
        return new TuStruct(argList);
    }

}
