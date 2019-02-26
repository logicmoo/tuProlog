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
            causeTerm = new TuStruct(cause.toString());
        else
            causeTerm = new TuInt(0);
        // Message
        Term messageTerm = null;
        String message = e.getMessage();
        if (message != null)
            messageTerm = new TuStruct(message);
        else
            messageTerm = new TuInt(0);
        // StackTrace
        TuStruct stackTraceTerm = new TuStruct();
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements)
            stackTraceTerm.append(new TuStruct(element.toString()));
        // return
        return new TuStruct(java_exception, causeTerm, messageTerm,
                stackTraceTerm);
    }

}
