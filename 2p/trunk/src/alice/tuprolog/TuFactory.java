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

}
