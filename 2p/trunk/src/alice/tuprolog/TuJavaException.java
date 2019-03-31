package alice.tuprolog;

import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
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
            causeTerm = createTuAtom(cause.toString());
        else
            causeTerm = createTuInt(0);
        // Message
        Term messageTerm = null;
        String message = e.getMessage();
        if (message != null)
            messageTerm = createTuAtom(message);
        else
            messageTerm = createTuInt(0);
        // StackTrace
        TuStruct stackTraceTerm = createStructEmpty();
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements)
            stackTraceTerm.appendDestructive(TuFactory.createTuAtom(element.toString()));
        // return
        return S(java_exception, causeTerm, messageTerm, stackTraceTerm);
    }

}
