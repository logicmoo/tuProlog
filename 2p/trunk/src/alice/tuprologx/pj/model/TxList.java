/*
 * List.java
 *
 * Created on March 8, 2007, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.model;
import java.util.*;
/**
 *
 * @author maurizio
 */
public class TxList<X extends TxTerm<?>> extends TxTerm<TxList<X>> implements Iterable<X> {
//public class List<X extends Term<?>> extends Compound<List<X>> {
	protected java.util.Vector<X> _theList;
        
        public final static TxList<?> NIL = new TxList<TxTerm<?>>(new Vector<TxTerm<?>>());
        
        TxList(Vector<X> lt) {
		_theList = lt;		
	}
        
	public <Z> TxList(Collection<Z> cz) {
		_theList = new Vector<X>(cz.size());
		for (Z z : cz) {
			_theList.add(TxTerm.<X>fromJava(z));
		}
	}
	
	@Override
	public <Z> Z/*Collection<Z>*/ toJava() {
		Vector<Z> _javaList = new Vector<Z>(_theList.size());
		for (TxTerm<?> t : _theList) {
			// _javaList.add( (Z)t.toJava() );
			Z auxList = uncheckedCast(t.toJava());
			_javaList.add( auxList );
		}
		//return (Z)_javaList;
		return uncheckedCast(_javaList);
	}

	@Override
	public String toString() {
		return "List"+_theList;
	}
        
        public X getHead() {
            return _theList.get(0);
        }
        
        public TxList<X> getTail() {
            //Vector<X> tail = (Vector<X>)_theList.clone();
            Vector<X> tail = uncheckedCast(_theList.clone());
            tail.remove(0);
            return new TxList<X>(tail);
        }
        
        @Override
		public alice.tuprolog.TuStruct marshal() {
            alice.tuprolog.Term[] termArray = new alice.tuprolog.Term[_theList.size()];
            int i=0;
            for (TxTerm<?> t : _theList) {
                termArray[i++]=t.marshal();
            }
            return new alice.tuprolog.TuStruct(termArray);
        }
        
        static <Z extends TxTerm<?>> TxList<Z> unmarshal(alice.tuprolog.TuStruct s) {
            if (!matches(s))
                throw new UnsupportedOperationException();
            Iterator<? extends alice.tuprolog.Term> listIt = s.listIterator();
            Vector<TxTerm<?>> termList = new Vector<TxTerm<?>>();
            while (listIt.hasNext())
                termList.add(TxTerm.unmarshal(listIt.next()));
            return new TxList<Z>(termList);
        }
        
        static boolean matches(alice.tuprolog.Term t) {
            return (!(t instanceof alice.tuprolog.TuVar) && t.isConsList() && t instanceof alice.tuprolog.TuStruct);
        }

        @Override
		public Iterator<X> iterator() {
            return _theList.iterator();
        }
        
        public static TxList<TxAtom> tokenize(java.util.StringTokenizer stok) {            
            java.util.Vector<String> tokens = new java.util.Vector<String>();      
            while (stok.hasMoreTokens()) {
                tokens.add(stok.nextToken());
            }
            return new TxList<TxAtom>(tokens);
        }
}