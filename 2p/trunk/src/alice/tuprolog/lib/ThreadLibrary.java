/**
 * @author Eleonora Cau
 *
 */

package alice.tuprolog.lib;

import alice.tuprolog.EngineManager;
import alice.tuprolog.TuInt;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.TuLibrary;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.TuProlog;
import alice.tuprolog.TuPrologError;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;




public class ThreadLibrary extends TuLibrary {
	private static final long serialVersionUID = 1L;
	protected EngineManager engineManager;
	
	@Override
	public void setEngine(TuProlog en) {	
        engine = en;
        engineManager = en.getEngineManager();
	}
	
	//Tenta di unificare a t l'identificativo del thread corrente
	public boolean thread_id_1 (Term t) throws TuPrologError{
        int id = engineManager.runnerId();
        unify(t,new TuInt(id));
		return true;
	}
	
	//Crea un nuovo thread di identificatore id che comincia ad eseguire il goal dato
	public boolean thread_create_2 (Term id, Term goal){
		return engineManager.threadCreate(id, goal);
	}
	
	/*Aspetta la terminazione del thread di identificatore id e ne raccoglie il risultato, 
	unificando il goal risolto a result. Il thread viene eliminato dal sistema*/
	public boolean thread_join_2(Term id, Term result) throws TuPrologError{
		id = id.dref();
		if (!(id .isInt())) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "integer", id);
		SolveInfo res = engineManager.join(((TuInt)id).intValue());
		if (res == null) return false;
		Term status;
		try {
			status = res.getSolution();
		} catch (NoSolutionException e) {
			//status = new Struct("FALSE");		
			return false;
		}
		try{
			unify (result, status);
		} catch (InvalidTermException e) {
			throw TuPrologError.syntax_error(engine.getEngineManager(),-1, e.line, e.pos, result);
		}
		return true;
	}
		
	public boolean thread_read_2(Term id, Term result) throws TuPrologError{
		id=id.dref();
		if (!(id .isInt())) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "integer", id);
		SolveInfo res=engineManager.read( ((TuInt)id).intValue());
		if (res==null) return false;
		Term status;
		try {
			status = res.getSolution();
		} catch (NoSolutionException e) {
			//status = new Struct("FALSE");
			return false;
		}
		try{
			unify (result, status);
		} catch (InvalidTermException e) {
			throw TuPrologError.syntax_error(engine.getEngineManager(),-1, e.line, e.pos, result);
		}
		return true;
	}
	
	public boolean thread_has_next_1(Term id) throws TuPrologError{
		id=id.dref();
		if (!(id .isInt())) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "integer", id);
		return engineManager.hasNext(((TuInt)id).intValue());
	}
	
	
	public boolean thread_next_sol_1(Term id) throws TuPrologError{
		id=id.dref();
		if (!(id .isInt())) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "integer", id);
		return engineManager.nextSolution(((TuInt)id).intValue());
	}
	
	public boolean thread_detach_1 (Term id) throws TuPrologError{
		id=id.dref();
		if (!(id .isInt())) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "integer", id);
		engineManager.detach(((TuInt)id).intValue());
		return true;
	}
	
	public boolean thread_sleep_1(Term millisecs) throws TuPrologError{
		millisecs=millisecs.dref();
		if (!(millisecs .isInt())) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "integer", millisecs);
		long time=((TuInt)millisecs).intValue();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("ERRORE SLEEP");
			return false;
		}
		return true;
	}
	
	public boolean thread_send_msg_2(Term id, Term msg) throws TuPrologError{
		id=id.dref();
		if (id .isInt()) 
			return engineManager.sendMsg(((TuInt)id).intValue(), msg);	
		if (!id.isAtomic() || !id.isAtomSymbol()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom, atomic or integer", id);
		return engineManager.sendMsg(id.toString(), msg);
	}
	
	public  boolean  thread_get_msg_2(Term id, Term msg) throws TuPrologError{
		id=id.dref();
		if (id .isInt()) 
			return engineManager.getMsg(((TuInt)id).intValue(), msg);
		if (!id.isAtomSymbol() || !id.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom, atomic or integer", id);
		return engineManager.getMsg(id.toString(), msg);
	}	
	
	public  boolean  thread_peek_msg_2(Term id, Term msg) throws TuPrologError{
		id=id.dref();
		if (id .isInt()) 
			return engineManager.peekMsg(((TuInt)id).intValue(), msg);
		if (!id.isAtomSymbol() || !id.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom, atomic or integer", id);
		return engineManager.peekMsg(id.toString(), msg);
	}

	public  boolean  thread_wait_msg_2(Term id, Term msg) throws TuPrologError{
		id=id.dref();
		if (id .isInt()) 
			return engineManager.waitMsg(((TuInt)id).intValue(), msg);
		if (!id.isAtomSymbol() || !id.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom, atomic or integer", id);
		return engineManager.waitMsg(id.toString(), msg);
	}

	public  boolean  thread_remove_msg_2(Term id, Term msg) throws TuPrologError{
		id=id.dref();
		if (id .isInt()) 
			return engineManager.removeMsg(((TuInt)id).intValue(), msg);
		if (!id.isAtomSymbol() || !id.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom, atomic or integer", id);
		return engineManager.removeMsg(id.toString(), msg);
	}
	
	public boolean msg_queue_create_1(Term q) throws TuPrologError{
		q= q.dref();
		if (!q.isAtomic() || !q.isAtomSymbol()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", q);
		return engineManager.createQueue(q.toString());
	}
	
	public boolean msg_queue_destroy_1 (Term q) throws TuPrologError{
		q=q.dref();
		if (!q.isAtomic() || !q.isAtomSymbol()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", q);
		engineManager.destroyQueue(q.toString());
		return true;
	}
	
	public boolean msg_queue_size_2(Term id, Term n) throws TuPrologError{
		id=id.dref();
		int size;
		if (id .isInt()) 
			size=engineManager.queueSize(((TuInt)id).intValue());
		else{
			if (!id.isAtomSymbol() || !id.isAtomic())
				throw TuPrologError.type_error(engine.getEngineManager(), 1,
	                    "atom, atomic or integer", id);
			size=engineManager.queueSize(id.toString());
		}
		if (size<0) return false;
		return unify(n, new TuInt(size));
	}	
	
	public boolean mutex_create_1(Term mutex) throws TuPrologError{
		mutex=mutex.dref();
		if (!mutex.isAtomSymbol() || !mutex.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", mutex);
		return engineManager.createLock(mutex.toString());
	}
	
	public boolean mutex_destroy_1(Term mutex) throws TuPrologError{
		mutex=mutex.dref();
		if (!mutex.isAtomSymbol() || !mutex.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", mutex);
		engineManager.destroyLock(mutex.toString());
		return true;
	}
	
	public boolean mutex_lock_1(Term mutex) throws TuPrologError{
		mutex=mutex.dref();
		if (!mutex.isAtomSymbol() || !mutex.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", mutex);
		return engineManager.mutexLock(mutex.toString());
	}
	
	public boolean mutex_trylock_1(Term mutex) throws TuPrologError{
		mutex=mutex.dref();
		if (!mutex.isAtomSymbol() || !mutex.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", mutex);
		return engineManager.mutexTryLock(mutex.toString());
	}
	
	public boolean mutex_unlock_1(Term mutex) throws TuPrologError{
		mutex=mutex.dref();
		if (!mutex.isAtomSymbol() || !mutex.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", mutex);
		return engineManager.mutexUnlock(mutex.toString());
	}
	
	public boolean mutex_isLocked_1(Term mutex) throws TuPrologError{
		mutex=mutex.dref();
		if (!mutex.isAtomSymbol() || !mutex.isAtomic()) 
			throw TuPrologError.type_error(engine.getEngineManager(), 1,
                    "atom or atomic", mutex);
		return engineManager.isLocked(mutex.toString());
	}
	
	public boolean mutex_unlock_all_0(){
		engineManager.unlockAll();
		return true;
	}
	
	@Override
	public String getTheory(){
		return 
		"thread_execute(ID, GOAL):- thread_create(ID, GOAL), '$next'(ID). \n" +
		"'$next'(ID). \n"+
		"'$next'(ID) :- '$thread_execute2'(ID). \n"+
		"'$thread_execute2'(ID) :- not thread_has_next(ID),!,false. \n" +
		"'$thread_execute2'(ID) :- thread_next_sol(ID). \n" +
		"'$thread_execute2'(ID) :- '$thread_execute2'(ID). \n" +
	
		"with_mutex(MUTEX,GOAL):-mutex_lock(MUTEX), call(GOAL), !, mutex_unlock(MUTEX).\n" +
		"with_mutex(MUTEX,GOAL):-mutex_unlock(MUTEX), fail."		
		;
	
	}
}
