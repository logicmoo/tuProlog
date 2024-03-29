package alice.tuprolog;

import java.util.Iterator;
import java.util.List;
import static alice.tuprolog.TuFactory.*;

/**
 * @author Matteo Iuliani
 */
public class TuStateException extends TuState {

    final Term catchTerm = createTerm("catch(Goal, Catcher, Handler)");
    final Term javaCatchTerm = createTerm("java_catch(Goal, List, Finally)");

    public TuStateException(EngineRunner c) {
        this.c = c;
        stateName = "Exception";
    }

    @Override
	void doJob(TuEngine e) {
        String errorType = e.currentContext.currentGoal.fname();
        if (errorType.equals("throw"))
            prologError(e);
        else
            javaException(e);
    }

    private void prologError(TuEngine e) {
        Term errorTerm = e.currentContext.currentGoal.getPlainArg(0);
        e.currentContext = e.currentContext.fatherCtx;
        if (e.currentContext == null) {
            // passo nello stato HALT se l?errore non pu? essere gestito (sono
            // arrivato alla radice dell'albero di risoluzione)
            e.nextState = c.END_HALT;
            return;
        }
        while (true) {
            // visito all'indietro l'albero di risoluzione alla ricerca di un
            // subgoal catch/3 il cui secondo argomento unifica con l?argomento
            // dell?eccezione lanciata
            if (e.currentContext.currentGoal.match(catchTerm)
                    && e.currentContext.currentGoal.getPlainArg(1).match(errorTerm)) {
                // ho identificato l?ExecutionContext con il corretto subgoal
                // catch/3

                // taglio tutti i punti di scelta generati da Goal
                c.cut();

                // unifico l'argomento di throw/1 con il secondo argomento di
                // catch/3
                List<TuVar> unifiedVars = e.currentContext.trailingVars
                        .getHead();
                e.currentContext.currentGoal.getPlainArg(1).unify(unifiedVars,
                        unifiedVars, errorTerm, c.getMediator().getFlagManager().isOccursCheckEnabled());

                // inserisco il gestore dell?errore in testa alla lista dei
                // subgoal da eseguire, come definito dal terzo argomento di
                // catch/3. Il gestore deve inoltre essere preparato per
                // l?esecuzione, mantenendo le sostituzioni effettuate durante
                // il processo di unificazione tra l?argomento di throw/1 e il
                // secondo argomento di catch/3
                Term handlerTerm = e.currentContext.currentGoal.getPlainArg(2);
                Term curHandlerTerm = handlerTerm.dref();
                if (!(curHandlerTerm .isTuStruct())) {
                    e.nextState = c.END_FALSE;
                    return;
                }
                // Code inserted to allow evaluation of meta-clause
                // such as p(X) :- X. When evaluating directly terms,
                // they are converted to execution of a call/1 predicate.
                // This enables the dynamic linking of built-ins for
                // terms coming from outside the demonstration context.
                if (handlerTerm != curHandlerTerm)
                    handlerTerm = S("call", curHandlerTerm);
                TuStruct handler = (TuStruct) handlerTerm;
                c.identify(handler);
                SubGoalTree sgt = new SubGoalTree();
                sgt.addChild(handler);
                c.pushSubGoal(sgt);
                e.currentContext.currentGoal = handler;

                // passo allo stato GOAL_SELECTION
                e.nextState = c.GOAL_SELECTION;
                return;
            } else {
                // passo all'ExecutionContext successivo
                e.currentContext = e.currentContext.fatherCtx;
                if (e.currentContext == null) {
                    // passo nello stato HALT se l?errore non pu? essere gestito
                    // (sono arrivato alla radice dell'albero di risoluzione)
                    e.nextState = c.END_HALT;
                    return;
                }
            }
        }
    }

    private void javaException(TuEngine e) {
        Term exceptionTerm = e.currentContext.currentGoal.getPlainArg(0);
        e.currentContext = e.currentContext.fatherCtx;
        if (e.currentContext == null) {
            // passo nello stato HALT se l?errore non pu? essere gestito (sono
            // arrivato alla radice dell'albero di risoluzione)
            e.nextState = c.END_HALT;
            return;
        }
        while (true) {
            // visito all'indietro l'albero di risoluzione alla ricerca di un
            // subgoal java_catch/3 che abbia un catcher unificabile con
            // l'argomento dell'eccezione lanciata
        	if (e.currentContext.currentGoal.match(javaCatchTerm)
                    && javaMatch(e.currentContext.currentGoal.getPlainArg(1),
                            exceptionTerm)) {
                // ho identificato l?ExecutionContext con il corretto subgoal
                // java_catch/3

                // taglio tutti i punti di scelta generati da JavaGoal
                c.cut();

                // unifico l'argomento di java_throw/1 con il catcher
                // appropriato e recupero l'handler corrispondente
                List<TuVar> unifiedVars = e.currentContext.trailingVars
                        .getHead();
                Term handlerTerm = javaUnify(e.currentContext.currentGoal
                        .getPlainArg(1), exceptionTerm, unifiedVars);
                if (handlerTerm == null) {
                    e.nextState = c.END_FALSE;
                    return;
                }

                // inserisco il gestore e il finally (se presente) in testa alla
                // lista dei subgoal da eseguire. I due predicati devono inoltre
                // essere preparati per l?esecuzione, mantenendo le sostituzioni
                // effettuate durante il processo di unificazione tra
                // l'eccezione e il catcher
                Term curHandlerTerm = handlerTerm.dref();
                if (!(curHandlerTerm .isTuStruct())) {
                    e.nextState = c.END_FALSE;
                    return;
                }
                Term finallyTerm = e.currentContext.currentGoal.getPlainArg(2);
                Term curFinallyTerm = finallyTerm.dref();
                // verifico se c'? il blocco finally
                boolean isFinally = true;
                if (curFinallyTerm .isInt()) {
                    Term finallyInt = (TuInt) curFinallyTerm;
                    if (finallyInt.intValue() == 0)
                        isFinally = false;
                    else {
                        // errore di sintassi, esco
                        e.nextState = c.END_FALSE;
                        return;
                    }
                } else if (!(curFinallyTerm .isTuStruct())) {
                    e.nextState = c.END_FALSE;
                    return;
                }
                // Code inserted to allow evaluation of meta-clause
                // such as p(X) :- X. When evaluating directly terms,
                // they are converted to execution of a call/1 predicate.
                // This enables the dynamic linking of built-ins for
                // terms coming from outside the demonstration context.
                if (handlerTerm != curHandlerTerm)
                    handlerTerm = S("call", curHandlerTerm);
                if (finallyTerm != curFinallyTerm)
                    finallyTerm = S("call", curFinallyTerm);

                TuStruct handler = (TuStruct) handlerTerm;
                c.identify(handler);
                SubGoalTree sgt = new SubGoalTree();
                sgt.addChild(handler);
                if (isFinally) {
                    TuStruct finallyStruct = (TuStruct) finallyTerm;
                    c.identify(finallyStruct);
                    sgt.addChild(finallyStruct);
                }
                c.pushSubGoal(sgt);
                e.currentContext.currentGoal = handler;

                // passo allo stato GOAL_SELECTION
                e.nextState = c.GOAL_SELECTION;
                return;

            } else {
                // passo all'ExecutionContext successivo
                e.currentContext = e.currentContext.fatherCtx;
                if (e.currentContext == null) {
                    // passo nello stato HALT se l?errore non pu? essere gestito
                    // (sono arrivato alla radice dell'albero di risoluzione)
                    e.nextState = c.END_HALT;
                    return;
                }
            }
        }
    }

    // verifica se c'? un catcher unificabile con l'argomento dell'eccezione
    // lanciata
    private boolean javaMatch(Term arg1, Term exceptionTerm) {
        if (!arg1.isPlList())
            return false;
        TuStruct list = (TuStruct) arg1;
        if (list.isEmptyList())
            return false;
        Iterator<? extends Term> it = list.listIteratorProlog();
        while (it.hasNext()) {
            Term nextTerm = it.next();
            if (!nextTerm.isCompound())
                continue;
            TuStruct element = (TuStruct) nextTerm;
            if (!element.fname().equals(","))
                continue;
            if (element.getPlArity() != 2)
                continue;
            if (element.getPlainArg(0).match(exceptionTerm)) {
                return true;
            }
        }
        return false;
    }

    // unifica l'argomento di java_throw/1 con il giusto catcher e restituisce
    // l'handler corrispondente
    private Term javaUnify(Term arg1, Term exceptionTerm, List<TuVar> unifiedVars) {
        TuStruct list = (TuStruct) arg1;
        Iterator<? extends Term> it = list.listIteratorProlog();
        while (it.hasNext()) {
            Term nextTerm = it.next();
            if (!nextTerm.isCompound())
                continue;
            TuStruct element = (TuStruct) nextTerm;
            if (!element.fname().equals(","))
                continue;
            if (element.getPlArity() != 2)
                continue;
            if (element.getPlainArg(0).match(exceptionTerm)) {
                element.getPlainArg(0)
                        .unify(unifiedVars, unifiedVars, exceptionTerm, c.getMediator().getFlagManager().isOccursCheckEnabled());
                return element.getPlainArg(1);
            }
        }
        return null;
    }
}