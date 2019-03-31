package alice.tuprologx.pj.model;

import alice.tuprologx.pj.annotations.*;

/**
 *
 * @author maurizio
 */
public abstract class TxTerm<X extends TxTerm<?>> {

    // Added by ED 2013-05-21 following MC suggestion
    @SuppressWarnings("unchecked")
    static <S, T> T uncheckedCast(S s) {
        return (T) s;
    }
    // END ADDITION

    public abstract <Z> Z toJava(); // {return null;}

    public static <Z extends TxTerm<?>> Z fromJava(Object o) {
        if (o instanceof Integer) {
            //return (Z)new Int((Integer)o);
            return uncheckedCast(new TxInt((Integer) o));
        } else if (o instanceof java.lang.Double) {
            //return (Z)new Double((java.lang.Double)o);
            return uncheckedCast(new TxDouble((java.lang.Double) o));
        } else if (o instanceof String) {
            //return (Z)new Atom((String)o);
            return uncheckedCast(new TxAtom((String) o));
        } else if (o instanceof Boolean) {
            //return (Z)new Bool((Boolean)o);
            return uncheckedCast(new TxBool((Boolean) o));
        } else if (o instanceof java.util.Collection<?>) {
            // return (Z)new List<Term<?>>((java.util.Collection<?>)o);
            return uncheckedCast(new TxList<TxTerm<?>>((java.util.Collection<?>) o));
        } else if (o instanceof TxTerm<?>[]) {
            // return (Z)new Cons<Term<?>, Compound<?>>("_",(Term<?>[])o);
            return uncheckedCast(new TxCons<TxTerm<?>, TxCompound<?>>("_", (TxTerm<?>[]) o));
        } else if (o instanceof TxTerm<?>) {
            //return (Z)o;
            return uncheckedCast(o);
        } else if (o.getClass().isAnnotationPresent(Termifiable.class)) {
            // return (Z)new JavaTerm<Object>(o);
            return uncheckedCast(new TxJavaTerm<Object>(o));
        }
        /*else {
        	throw new UnsupportedOperationException();
        }*/
        else {
            // return (Z)new JavaObject<Object>(o);
            return uncheckedCast(new TxJavaObject<Object>(o));
        }
    }

    public abstract alice.tuprolog.Term marshal() /*{
                                                  throw new UnsupportedOperationException();
                                                  }*/;

    public static <Z extends TxTerm<?>> Z unmarshal(alice.tuprolog.Term t) {
        if (TxInt.matches(t)) {
            // return (Z)Int.unmarshal((alice.tuprolog.Int)t);
            return uncheckedCast(TxInt.unmarshal((alice.tuprolog.TuInt) t));
        } else if (TxDouble.matches(t)) {
            //return (Z)Double.unmarshal((alice.tuprolog.Double)t);
            return uncheckedCast(TxDouble.unmarshal((alice.tuprolog.TuDouble) t));
        } else if (TxJavaObject.matches(t)) {
            //return (Z)JavaObject.unmarshalObject((alice.tuprolog.Struct)t);
            return uncheckedCast(TxJavaObject.unmarshalObject((alice.tuprolog.TuStruct) t));
        } else if (TxAtom.matches(t)) {
            //return (Z)Atom.unmarshal((alice.tuprolog.Struct)t);
            return uncheckedCast(TxAtom.unmarshal((alice.tuprolog.TuStruct) t));
        } else if (TxBool.matches(t)) {
            //return (Z)Bool.unmarshal((alice.tuprolog.Struct)t);
            return uncheckedCast(TxBool.unmarshal((alice.tuprolog.TuStruct) t));
        } else if (TxList.matches(t)) {
            //return (Z)List.unmarshal((alice.tuprolog.Struct)t);
            return uncheckedCast(TxList.unmarshal((alice.tuprolog.TuStruct) t));
        } else if (TxJavaTerm.matches(t)) {
            //return (Z)JavaTerm.unmarshalObject((alice.tuprolog.Struct)t.getTerm());
            return uncheckedCast(TxJavaTerm.unmarshalObject((alice.tuprolog.TuStruct) t.dref()));
        } else if (TxCons.matches(t)) {
            //return (Z)Cons.unmarshal((alice.tuprolog.Struct)t);
            return uncheckedCast(TxCons.unmarshal((alice.tuprolog.TuStruct) t));
        } else if (TxVar.matches(t)) {
            //return (Z)Var.unmarshal((alice.tuprolog.Var)t);
            return uncheckedCast(TxVar.unmarshal((alice.tuprolog.TuVar) t));
        } else {
            System.out.println(t);
            throw new UnsupportedOperationException();
        }
    }
}
