package alice.tuprolog;

/**
 * @author Matteo Iuliani
 */
public class TuJavaException extends Throwable {
	private static final long serialVersionUID = 1L;
    // eccezione Java che rappresenta l'argomento di java_throw/1
    private Throwable e;

    public TuJavaException(Throwable e) {
        this.e = e;
    }

    public TuStruct getException() {
        // java_exception
        String java_exception = e.getClass().getName();
        // Cause
        Term causeTerm = null;
        Throwable cause = e.getCause();
        if (cause != null)
            causeTerm = TuTerm.createAtomTerm(cause.toString());
        else
            causeTerm = TuTerm.i32(0);
        // Message
        Term messageTerm = null;
        String message = e.getMessage();
        if (message != null)
            messageTerm = TuTerm.createAtomTerm(message);
        else
            messageTerm = TuTerm.i32(0);
        // StackTrace
        TuStruct stackTraceTerm = TuTerm.createAppendableStruct();
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements)
            stackTraceTerm.append(TuTerm.createAtomTerm(element.toString()));
        // return
        return TuStruct.createSTRUCT(java_exception, causeTerm, messageTerm, stackTraceTerm);
    }

}
