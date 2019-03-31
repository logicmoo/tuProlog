package alice.tuprologx.pj.model;

import java.util.*;

import alice.tuprolog.TuFactory;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;
/**
 *
 * @author Maurizio
 */
public class TxCons<H extends TxTerm<?>, R extends TxCompound<?>> extends TxCompound<TxCons<H,R>> implements Iterable<TxTerm<?>> {
        
    
    String _theName;
    H _theHead;	
    R _theRest;

    public TxCons(String name, H head) {
        _theHead = head;
        _theName = name;
        //_theRest = (R)new Nil();
        _theRest = uncheckedCast(new TxNil());
    }

    protected TxCons(String name, java.util.List<TxTerm<?>> termList) {
        initFromList(termList);
        _theName = name;        
    }
    
    public static <Z extends TxCons<?,?>> Z make(String f, TxTerm<?>[] termList) {        
        if (termList.length == 1)
            //return (Z)new Compound1<Term<?>>(f,termList[0]);
        	return uncheckedCast(new TxCompound1<TxTerm<?>>(f,termList[0]));
        else if (termList.length == 2)
            //return (Z)new Compound2<Term<?>, Term<?>>(f,termList[0],termList[1]);
        	return uncheckedCast(new TxCompound2<TxTerm<?>, TxTerm<?>>(f,termList[0],termList[1]));
        else if (termList.length == 3)
            //return (Z)new Compound3<Term<?>, Term<?>, Term<?>>(f,termList[0],termList[1],termList[2]);
        	return uncheckedCast(new TxCompound3<TxTerm<?>, TxTerm<?>, TxTerm<?>>(f,termList[0],termList[1],termList[2]));
        else if (termList.length > 3)
            //return (Z)new Cons<Term<?>, Compound<?>>(f,termList);
        	return uncheckedCast(new TxCons<TxTerm<?>, TxCompound<?>>(f,termList));
        else
            throw new UnsupportedOperationException();
    }
    /*
    Cons(Object po) {            
        try {
            java.util.Vector<Term<?>> termArr = new java.util.Vector<Term<?>>();
            java.beans.BeanInfo binfo = java.beans.Introspector.getBeanInfo(po.getClass());
            for (java.beans.PropertyDescriptor pdesc : binfo.getPropertyDescriptors()) {
                //only read-write properties are translated into a compound
                if (pdesc.getReadMethod()!=null && pdesc.getWriteMethod()!=null) { 
                    Object o = pdesc.getReadMethod().invoke(po);
                    Atom propertyName = new Atom(pdesc.getName());
                    Term<?> propertyValue = Term.fromJava(o);
                    termArr.add(new Cons<Atom,Cons<Term<?>,Nil>>("_property",new Term<?>[] {propertyName, propertyValue}));
                }
            }
            _theName = binfo.getBeanDescriptor().getBeanClass().getName();            
            initFromList(termArr);
        }              
        catch (UnsupportedOperationException e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    @Override
	public Iterator<TxTerm<?>> iterator() {
        return new Iterator<TxTerm<?>>() {
            TxCons<?, ?> theTuple = TxCons.this;	
            @Override
			public TxTerm<?> next() {
                if (theTuple == null) {
                    throw new java.util.NoSuchElementException();
                }
                TxTerm<?> head = theTuple.getHead();
                theTuple=(theTuple.getRest() instanceof TxCons ? (TxCons<?,?>)theTuple.getRest() : null);
                return head;
            }
            @Override
			public boolean hasNext() {		
                return theTuple != null;
            }
            @Override
			public void remove() {throw new UnsupportedOperationException();}
        };
    }
    
    private void initFromList(java.util.List<TxTerm<?>> termList) {
        if (!termList.isEmpty()) {
            // _theHead = (H)termList.remove(0);  
            _theHead = uncheckedCast(termList.remove(0));
            // _theRest = !termList.isEmpty() ? (R)new Cons<Term<?>, Compound<?>>(null,termList) : (R)new Nil();
            _theRest = uncheckedCast(!termList.isEmpty() ? uncheckedCast(new TxCons<TxTerm<?>, TxCompound<?>>(null,termList)) : uncheckedCast(new TxNil()));
            return;
        }
        throw new UnsupportedOperationException(); //cannot create a 0-sized compound
    }

    public TxCons(String name, TxTerm<?>[] termArr) {
        this(name,new Vector<TxTerm<?>>(Arrays.asList(termArr)));
    }

    public H getHead() {
        return _theHead;
    }

    public R getRest() {
        return _theRest;
    }

    @Override
	public String getName() {
        return _theName;
    }


    public <Z extends TxTerm<?>, R2 extends TxCons<Z,? extends TxCompound<?>>> TxCons<H, R2> append(Z z) {
        TxTerm<?>[] termArr = this.toJava();
        TxTerm<?>[] newTermArr = new TxTerm<?>[termArr.length+1];
        System.arraycopy(termArr,0,newTermArr,0,termArr.length);
        newTermArr[termArr.length] = z;            
        return new TxCons<H,R2>(_theName,newTermArr);
    }

    @Override
	public int arity() {return 1+_theRest.arity();}

    @Override
	public String toString() {
        String res = "Compound:'"+getName()+"'(";
        for (TxTerm<?> t : this) {        
            res += t+",";            
        }            
        if (res.lastIndexOf(',')!=-1) {
            res = res.substring(0,res.lastIndexOf(','));
        }
        return res+")";
    }    

    static <Z extends TxCons<?,?>> Z unmarshal(alice.tuprolog.TuStruct s) {
        if (!matches(s))
            throw new UnsupportedOperationException();
        Vector<TxTerm<?>> termList = new Vector<TxTerm<?>>();
        for (int i=0;i<s.getArity();i++) {       
            termList.add(TxTerm.unmarshal(s.getPlainArg(i)));
        }
        //return (Z)new Cons(s.getName(),termList);
        return TxCons.<Z>make(s.fname(),termList.toArray(new TxTerm<?>[termList.size()]));
    }

    static boolean matches(alice.tuprolog.Term t) {
        return (!(t instanceof alice.tuprolog.TuVar) && t.isCompound() && !t.isPlList());
    }
    
    @Override
	public <Z> Z toJava() {
    /*    if (isPrologObject())
            return (Z)toPrologObject();
        else {*/
            Vector<TxTerm<?>> _javaList = new Vector<TxTerm<?>>();            
            for (TxTerm<?> t : this) {
                _javaList.add(t/*((Compound<?,?>)c).getHead()*/);                
            }
            TxTerm<?>[] termArr = new TxTerm<?>[_javaList.size()];
            _javaList.toArray(termArr);
            //return (Z)termArr;
            return uncheckedCast(termArr);
        //}
    }

    @Override
	public alice.tuprolog.TuStruct marshal() {
        alice.tuprolog.Term[] termArray = new alice.tuprolog.Term[arity()];
        int i = 0;
        for (TxTerm<?> t: this) {
            termArray[i++]=t.marshal();        
        }
        return createTuStructA(_theName, termArray);
    }
    /*
    private Object toPrologObject() {            
        try {                
            if (!isPrologObject())
                throw new UnsupportedOperationException();
            Class<?> cl = Class.forName(getName());
            Object po = cl.newInstance();                
            java.beans.BeanInfo binfo = java.beans.Introspector.getBeanInfo(cl);            
            for (Term t : this) {
                Cons<Atom, Cons<Term<?>, Nil>> property = (Cons<Atom, Cons<Term<?>, Nil>>)t;
                assert(property.getName().equals("_property"));
                for (java.beans.PropertyDescriptor pdesc : binfo.getPropertyDescriptors()) {
                    if (pdesc.getName().equals(property.getHead())) {
                        pdesc.getWriteMethod().invoke(po, property.getRest().getHead().toJava());                    
                    }
                }
            }            
            return po;            
        }
        catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }        
    }
    
    private boolean isPrologObject() {
        Class<?> cl = null;
        try {
            cl = Class.forName(getName());
        }
        catch (ClassNotFoundException e) {
            
        }
        if (cl==null||!cl.isAnnotationPresent(Termifiable.class))
            return false;
        else
            return true;
    }*/
}




//