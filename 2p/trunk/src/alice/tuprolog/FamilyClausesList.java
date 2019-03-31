package alice.tuprolog;

import alice.util.ReadOnlyLinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * <code>FamilyClausesList</code> is a common <code>LinkedList</code>
 * which stores {@link ClauseInfo} objects. Internally it indexes stored data
 * in such a way that, knowing what type of clauses are required, only
 * goal compatible clauses are returned
 *
 * @author Paolo Contessi
 * @since 2.2
 * 
 * @see LinkedList
 */
class FamilyClausesList extends LinkedList<ClauseInfo> {
	private static final long serialVersionUID = 1L;
	private FamilyClausesIndex<TuNumber> numCompClausesIndex;
	private FamilyClausesIndex<String> constantCompClausesIndex;
	private FamilyClausesIndex<String> structCompClausesIndex;
	private LinkedList<ClauseInfo> listCompClausesList;

	//private LinkedList<ClauseInfo> clausesList;

	public FamilyClausesList(){
		super();

		numCompClausesIndex = new FamilyClausesIndex<TuNumber>();
		constantCompClausesIndex = new FamilyClausesIndex<String>();
		structCompClausesIndex = new FamilyClausesIndex<String>();

		listCompClausesList = new LinkedList<ClauseInfo>();
	}

	/**
	 * Adds the given clause as first of the family
	 *
	 * @param ci    The clause to be added (with related informations)
	 */
	@Override
	public void addFirst(ClauseInfo ci){
		super.addFirst(ci);

		// Add first in type related storage
		register(ci, true);
	}

	/**
	 * Adds the given clause as last of the family
	 *
	 * @param ci    The clause to be added (with related informations)
	 */
	@Override
	public void addLast(ClauseInfo ci){
		super.addLast(ci);

		// Add last in type related storage
		register(ci, false);
	}

	@Override
	public boolean add(ClauseInfo o) {
		addLast(o);

		return true;
	}

	/**
	 * @deprecated 
	 */
	@Deprecated
	@Override
	public boolean addAll(int index, Collection<? extends ClauseInfo> c) {
		throw new UnsupportedOperationException("Not supported.");
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void add(int index, ClauseInfo element) {
		throw new UnsupportedOperationException("Not supported.");
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public ClauseInfo set(int index, ClauseInfo element) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public ClauseInfo removeFirst() {
		ClauseInfo ci = getFirst();
		if (remove(ci)){
			return ci;
		}

		return null;
	}

	@Override
	public ClauseInfo removeLast() {
		ClauseInfo ci = getLast();
		if (remove(ci)){
			return ci;
		}

		return null;
	}

	@Override
	public ClauseInfo remove(){
		return removeFirst();
	}

	@Override
	public ClauseInfo remove(int index){
		ClauseInfo ci = super.get(index);

		if(remove(ci)){
			return ci;
		}

		return null;
	}

	@Override
	public boolean remove(Object ci){
		if(super.remove(ci))
		{
			unregister((ClauseInfo) ci);

			return true;
		}
		return false;
	}

	@Override
	public void clear(){
		while(size() > 0){
			removeFirst();
		}
	}

	/**
	 * Retrieves a sublist of all the clauses of the same family as the goal
	 * and which, in all probability, could match with the given goal
	 *
	 * @param goal  The goal to be resolved
	 * @return      The list of goal-compatible predicates
	 */
	public List<ClauseInfo> get(Term goal){
		// Gets the correct list and encapsulates it in ReadOnlyLinkedList
		if(goal .isStruct()){
			TuStruct g = (TuStruct) goal.getTerm();

			/*
			 * If no arguments no optimization can be applied
			 * (and probably no optimization is needed)
			 */
			if(g.getArity() == 0){
				return new ReadOnlyLinkedList<ClauseInfo>(this);
			}

			/* Retrieves first argument and checks type */
			Term t = g.getArg(0).getTerm();
			if(t .isVar()){
				/*
				 * if first argument is an unbounded variable,
				 * no reasoning is possible, all family must be returned
				 */
				return new ReadOnlyLinkedList<ClauseInfo>(this);
			} else if(t.isAtomic()){
				if(t .isNumber()){
					/* retrieves clauses whose first argument is numeric (or Var)
					 * and same as goal's first argument, if no clauses
					 * are retrieved, all clauses with a variable
					 * as first argument
					 */
					return new ReadOnlyLinkedList<ClauseInfo>(numCompClausesIndex.get((TuNumber) t));
				} else if(t .isStruct()){
					/* retrieves clauses whose first argument is a constant (or Var)
					 * and same as goal's first argument, if no clauses
					 * are retrieved, all clauses with a variable
					 * as first argument
					 */
					return new ReadOnlyLinkedList<ClauseInfo>(constantCompClausesIndex.get(((TuStruct) t).getName()));
				}
			} else if(t .isStruct()){
				if(isAList((TuStruct) t)){
					/* retrieves clauses which has a list  (or Var) as first argument */
					return new ReadOnlyLinkedList<ClauseInfo>(listCompClausesList);
				} else {
					/* retrieves clauses whose first argument is a struct (or Var)
					 * and same as goal's first argument, if no clauses
					 * are retrieved, all clauses with a variable
					 * as first argument
					 */
					return new ReadOnlyLinkedList<ClauseInfo>(structCompClausesIndex.get(((TuStruct) t).getPredicateIndicator()));
				}
			}
		}

		/* Default behaviour: no optimization done */
		return new ReadOnlyLinkedList<ClauseInfo>(this);
	}

	@Override
	public Iterator<ClauseInfo> iterator(){
		return listIterator(0);
	}

	@Override
	public ListIterator<ClauseInfo> listIterator(){
		return new ListItr(this,0).getIt();
	}

	private ListIterator<ClauseInfo> superListIterator(int index){
		return super.listIterator(index);
	}

	@Override
	public ListIterator<ClauseInfo> listIterator(int index){
		return new ListItr(this,index).getIt();
	}

	private boolean isAList(TuStruct t) {
		/*
		 * Checks if a Struct is also a list.
		 * A list can be an empty list, or a Struct with name equals to "."
		 * and arity equals to 2.
		 */
		return t.isEmptyList() || (t.getName().equals(".") && t.getArity() == 2);

	}

	// Updates indexes, storing informations about the last added clause
	private void register(ClauseInfo ci, boolean first){
		// See FamilyClausesList.get(Term): same concept
		Term clause = ci.getHead();
		if(clause .isStruct()){
			TuStruct g = (TuStruct) clause.getTerm();

			if(g.getArity() == 0){
				return;
			}

			Term t = g.getArg(0).getTerm();
			if(t .isVar()){
				numCompClausesIndex.insertAsShared(ci, first);
				constantCompClausesIndex.insertAsShared(ci, first);
				structCompClausesIndex.insertAsShared(ci, first);

				if(first){
					listCompClausesList.addFirst(ci);
				} else {
					listCompClausesList.addLast(ci);
				}
			} else if(t.isAtomic()){
				if(t .isNumber()){
					numCompClausesIndex.insert((TuNumber) t,ci, first);
				} else if(t .isStruct()){
					constantCompClausesIndex.insert(((TuStruct) t).getName(), ci, first);
				}
			} else if(t .isStruct()){
				if(isAList((TuStruct) t)){
					if(first){
						listCompClausesList.addFirst(ci);
					} else {
						listCompClausesList.addLast(ci);
					}
				} else {
					structCompClausesIndex.insert(((TuStruct) t).getPredicateIndicator(), ci, first);
				}
			}
		}
	}

	// Updates indexes, deleting informations about the last removed clause
	public void unregister(ClauseInfo ci) {
		Term clause = ci.getHead();
		if(clause .isStruct()){
			TuStruct g = (TuStruct) clause.getTerm();

			if(g.getArity() == 0){
				return;
			}

			Term t = g.getArg(0).getTerm();
			if(t .isVar()){
				numCompClausesIndex.removeShared(ci);
				constantCompClausesIndex.removeShared(ci);
				structCompClausesIndex.removeShared(ci);

				listCompClausesList.remove(ci);
			} else if(t.isAtomic()){
				if(t .isNumber()){
					numCompClausesIndex.delete((TuNumber) t,ci);
				} else if(t .isStruct()){
					constantCompClausesIndex.delete(((TuStruct) t).getName(),ci);
				}
			} else if(t .isStruct()){
				if(t.isList()){
					listCompClausesList.remove(ci);
				} else {
					structCompClausesIndex.delete(((TuStruct) t).getPredicateIndicator(),ci);
				}
			}
		}
	}

	private class ListItr implements ListIterator<ClauseInfo> {

		private ListIterator<ClauseInfo> it;
		private LinkedList<ClauseInfo> l;
		private int currentIndex = 0;

		public ListItr(FamilyClausesList list, int index){
			l = list;
			it = list.superListIterator(index);
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public ClauseInfo next() {
			// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
			currentIndex = it.nextIndex();

			return it.next();
		}

		@Override
		public boolean hasPrevious() {
			return it.hasPrevious();
		}

		@Override
		public ClauseInfo previous() {
			// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
			currentIndex = it.previousIndex();

			return it.previous();
		}

		@Override
		public int nextIndex() {
			return it.nextIndex();
		}

		@Override
		public int previousIndex() {
			return it.previousIndex();
		}

		@Override
		public void remove() {
			// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
			ClauseInfo ci = l.get(currentIndex);

			it.remove();

			unregister(ci);
		}

		@Override
		public void set(ClauseInfo o) {
			it.set(o);
			//throw new UnsupportedOperationException("Not supported.");
		}

		@Override
		public void add(ClauseInfo o) {
			l.addLast(o);

		}

		public ListIterator<ClauseInfo> getIt(){
			return this;
		}    


	}

	// Short test about the new implementation of the ListItr
	// Alessandro Montanari - alessandro.montanar5@studio.unibo.it
	@SuppressWarnings("unused")
	private static class ListItrTest{

		private static FamilyClausesList clauseList = new FamilyClausesList();

		public static void main(String[] args) {
			ClauseInfo first = new ClauseInfo(new TuStruct(new TuStruct("First"),new TuStruct("First")),"First Element");
			ClauseInfo second = new ClauseInfo(new TuStruct(new TuStruct("Second"),new TuStruct("Second")),"Second Element");
			ClauseInfo third = new ClauseInfo(new TuStruct(new TuStruct("Third"),new TuStruct("Third")),"Third Element");
			ClauseInfo fourth = new ClauseInfo(new TuStruct(new TuStruct("Fourth"),new TuStruct("Fourth")),"Fourth Element");

			clauseList.add(first);
			clauseList.add(second);
			clauseList.add(third);
			clauseList.add(fourth);
			
			// clauseList = [First, Second, Third, Fourh]
			
			ListIterator<ClauseInfo> allClauses = clauseList.listIterator();
			// Get the first object and remove it
			allClauses.next();
			allClauses.remove();
			if(clauseList.contains(first))
			{
				System.out.println("Error!");
				System.exit(-1);
			}

			// First object removed
			// clauseList = [Second, Third, Fourh]

			// Get the second object
			allClauses.next();
			// Get the third object
			allClauses.next();
			// Get the third object
			allClauses.previous();
			// Get the second object and remove it
			allClauses.previous();
			allClauses.remove();
			if(clauseList.contains(second))
			{
				System.out.println("Error!");
				System.exit(-2);
			}
			
			// clauseList = [Third, Fourh]

			System.out.println("Ok!!!");
		}
	}

}


