package alice.tuprolog;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.json.AbstractEngineState;

public class EngineManager implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Prolog vm;
	private Hashtable<Integer, EngineRunner> runners;	//key: id;  obj: runner
	private Hashtable<Integer, Integer> threads;	    //key: pid; obj: id
	private int rootID = 0;
	private EngineRunner er1;
	private int id = 0;
	
	private Hashtable<String, TermQueue> queues;
	private Hashtable<String, ReentrantLock> locks;

	public void initialize(Prolog vm) {
		this.vm=vm;
		runners=new Hashtable<Integer,EngineRunner>();
		threads = new Hashtable<Integer,Integer>();
		queues =new Hashtable<String, TermQueue>();
		locks = new Hashtable<String, ReentrantLock>();
		er1 = new EngineRunner(rootID);
		er1.initialize(vm);	
	}
	
	public synchronized boolean threadCreate(Term threadID, Term goal) {
		id = id+1;
		
		if (goal == null) return false;
		if (goal instanceof Var) 
			goal = goal.getTerm();
		
		EngineRunner er = new EngineRunner(id);
		er.initialize(vm);
		
		if (!vm.unify(threadID, new Int(id))) return false;
		
		er.setGoal(goal);
		addRunner(er, id);
		Thread t = new Thread(er);
		addThread(t.getId(), id);
	
		t.start();
		return true;
	}
	
	public SolveInfo join(int id) {
		EngineRunner er = findRunner(id);
		if (er==null || er.isDetached()) return null;
		SolveInfo solution = er.read();
		removeRunner(id);
		return solution;
	}
	
	public SolveInfo read (int id) {
		EngineRunner er = findRunner(id);
		if (er==null || er.isDetached()) return null;
		SolveInfo solution = er.read();
		return solution;
	}
	
	public boolean hasNext (int id){
		EngineRunner er = findRunner(id);
		if (er==null || er.isDetached()) return false;
		return er.hasOpenAlternatives();
	}
	
	public boolean nextSolution (int id){
		EngineRunner er = findRunner(id);
		if (er==null || er.isDetached()) return false;
		boolean bool = er.nextSolution();
		return bool;
	}
	
	public void detach (int id) {
		EngineRunner er= findRunner(id);
		if (er==null) return;
		er.detach();
	}
	
	public boolean sendMsg (int dest, Term msg){
		EngineRunner er = findRunner(dest);
		if (er==null) return false;
		Term msgcopy = msg.copy(new LinkedHashMap<Var,Var>(), 0);
		er.sendMsg(msgcopy);
		return true;
	}
	
	public boolean sendMsg(String name, Term msg) {
		TermQueue queue = queues.get(name);
		if (queue==null) return false;
		Term msgcopy = msg.copy(new LinkedHashMap<Var,Var>(), 0);
		queue.store(msgcopy);
		return true;
	}
	
	public boolean getMsg(int id, Term msg){
		EngineRunner er = findRunner(id);
		if (er==null) return false;
		return er.getMsg(msg);
	}
	
	public boolean getMsg(String name, Term msg){
		EngineRunner er=findRunner();
		if (er==null) return false;
		TermQueue queue = queues.get(name);
		if (queue==null) return false;
		return queue.get(msg, vm, er);
	}
	
	public boolean waitMsg(int id, Term msg){
		EngineRunner er=findRunner(id);
		if (er==null) return false;
		return er.waitMsg(msg);
	}	
	
	public boolean waitMsg(String name, Term msg){
		EngineRunner er=findRunner();
		if (er==null) return false;
		TermQueue queue=queues.get(name);
		if (queue==null) return false;
		return queue.wait(msg, vm, er);
	}
	
	public boolean peekMsg(int id, Term msg){
		EngineRunner er = findRunner(id);
		if (er==null) return false;
		return er.peekMsg(msg);
	}
	
	public boolean peekMsg(String name, Term msg){
		TermQueue queue = queues.get(name);
		if (queue==null) return false;
		return queue.peek(msg, vm);
	}

	public boolean removeMsg(int id, Term msg){
		EngineRunner er=findRunner(id);
		if (er==null) return false;
		return er.removeMsg(msg);
	}
	
	public boolean removeMsg(String name, Term msg){
		TermQueue queue=queues.get(name);
		if (queue==null) return false;
		return queue.remove(msg, vm);
	}
	
	private void removeRunner(int id){
		EngineRunner er=runners.get(id);
		if (er==null) return;
		synchronized (runners) {
			runners.remove(id);
		}
		int pid = er.getPid();
		synchronized (threads) {
			threads.remove(pid);
		}
	}
	
	private void addRunner(EngineRunner er, int id){
		synchronized (runners){
			runners.put(id, er);
		}
	}
	
	private void addThread(long pid, int id){
		synchronized (threads){
			threads.put((int) pid, id);
		}
	}
	
	void cut() {
		findRunner().cut();
	}
	
	ExecutionContext getCurrentContext() {
		EngineRunner runner=findRunner();
		return runner.getCurrentContext();
	}
	
	boolean hasOpenAlternatives() {
		EngineRunner runner = findRunner();
		return runner.hasOpenAlternatives();
	}

	boolean isHalted() {
		EngineRunner runner =findRunner();
		return runner.isHalted();
	}
	
	void pushSubGoal(SubGoalTree goals) {
		EngineRunner runner= findRunner();
		runner.pushSubGoal(goals);
		
	}
	
	public synchronized SolveInfo solve(Term query) {
		er1.setGoal(query);
		SolveInfo s = er1.solve();
		return s;
	}
	
	public void solveEnd() {
		er1.solveEnd();
		if(runners.size()!=0){
			java.util.Enumeration<EngineRunner> ers=runners.elements();
			while (ers.hasMoreElements()) {
				EngineRunner current=ers.nextElement();
				current.solveEnd();		
			}
			runners=new Hashtable<Integer,EngineRunner>();
			threads=new Hashtable<Integer,Integer>();
			queues =new Hashtable<String, TermQueue>();
			locks = new Hashtable<String, ReentrantLock>();
			id = 0;
		}
	}
	
	public void solveHalt() {
		er1.solveHalt();
		if(runners.size()!=0){
			java.util.Enumeration<EngineRunner> ers=runners.elements();
			while (ers.hasMoreElements()) {
				EngineRunner current=ers.nextElement();
				current.solveHalt();		
			}
		}
	}
	
	public synchronized SolveInfo solveNext() throws NoMoreSolutionException {
		return er1.solveNext();
	}
	
	void spy(String action, Engine env) {
		EngineRunner runner = findRunner();
		runner.spy(action, env);
	}
	
	/**
	 * 
	 * @return L'EngineRunner associato al thread di id specificato.
	 * 
	 */
	
	private EngineRunner findRunner (int id){
		if(!runners.containsKey(id)) return null;
		synchronized(runners){
			return runners.get(id);
		}
	}
	
	private EngineRunner findRunner(){
		int pid = (int) Thread.currentThread().getId();
		if(!threads.containsKey(pid))
			return er1;
		synchronized(threads){
			synchronized(runners){
				int id = threads.get(pid);
				return runners.get(id);
			}
		}	
	}
	
	//Ritorna l'identificativo del thread corrente
	public int runnerId (){
		EngineRunner er=findRunner();
		return er.getId();
	}
	
	public boolean createQueue (String name){
		synchronized (queues){
			if (queues.containsKey(name)) return true;
			TermQueue newQ = new TermQueue();
			queues.put(name, newQ);		
			}
		return true;
	}
	
	public void destroyQueue (String name) {
		synchronized (queues){
			queues.remove(name);
		}
	}
	
	public int queueSize(int id){
		EngineRunner er = findRunner(id);
		return er.msgQSize();
	}

	public int queueSize(String name){
		TermQueue q=queues.get(name);
		if (q==null) return -1;
		return q.size();
	}
	
	public boolean createLock(String name){
		synchronized (locks){
			if (locks.containsKey(name)) return true;
			ReentrantLock mutex=new ReentrantLock();
			locks.put(name, mutex);
		}
		return true;
	}
	
	public void destroyLock(String name){
		synchronized (locks){
			locks.remove(name);
		}
	}
	
	public boolean mutexLock(String name){
		ReentrantLock mutex = locks.get(name);
		if (mutex==null) {
			createLock(name);
			return mutexLock(name);
		}
		mutex.lock();
		return true;
	}

	
	public boolean mutexTryLock(String name){
		ReentrantLock mutex=locks.get(name);
		if (mutex==null) return false;
		return mutex.tryLock();
	}
	
	public boolean mutexUnlock (String name){
		ReentrantLock mutex=locks.get(name);
		if (mutex==null) return false;
		try{
			mutex.unlock();
			return true;
		}
		catch(IllegalMonitorStateException e){
			return false;
		}
	}
	
	public boolean isLocked(String name){
		ReentrantLock mutex=locks.get(name);
		if (mutex==null) return false;
		return mutex.isLocked();
	}
	
	public void unlockAll(){
		synchronized (locks){
			Set<String> mutexList=locks.keySet();
			Iterator<String> it=mutexList.iterator();
			
			while (it.hasNext()){
				ReentrantLock mutex=locks.get(it.next());
				boolean unlocked=false;
				while(!unlocked){
					try{
						mutex.unlock();
					}
					catch(IllegalMonitorStateException e){
						unlocked=true;
					}
				}
			}
		}
	}

	public Engine getEnv() {
		EngineRunner er=findRunner();
		return er.env;
	}
	
	public void identify(Term t) {
		EngineRunner er=findRunner();
		er.identify(t);
	}

	//Alberto
	public void serializeQueryState(AbstractEngineState brain) {
		brain.setQuery(findRunner().getQuery());
		if(findRunner().env == null) {
			brain.setNumberAskedResults(0);
			brain.setHasOpenAlternatives(false);
		} else {
			brain.setNumberAskedResults(findRunner().env.getNResultAsked());
			brain.setHasOpenAlternatives(findRunner().env.hasOpenAlternatives());
		}
	}	
}