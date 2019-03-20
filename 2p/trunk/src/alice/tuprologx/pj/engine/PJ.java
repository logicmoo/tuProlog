package alice.tuprologx.pj.engine;

import alice.tuprolog.TuStruct;
import alice.tuprologx.pj.annotations.PrologMethod;
import alice.tuprologx.pj.annotations.WithTermifiable;
import alice.tuprologx.pj.annotations.Termifiable;
import alice.tuprologx.pj.model.*;
import alice.tuprologx.pj.meta.*;
import alice.tuprologx.pj.model.TxTheory;
import alice.tuprologx.pj.model.TxClause;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javassist.util.proxy.*;

public class PJ implements MethodHandler {
    static int reentrant = 0;
    private PJ() {}

    private static PJ pj = new PJ();

    private static ArrayList<PJProlog> _stack = new ArrayList<PJProlog>();
    private static int current = -1;

    static {pushEngine();}

    static private void pushEngine() {
        if (_stack.size() <= ++current) {
            PJProlog engine = new PJProlog();
            alice.tuprologx.pj.lib.PJLibraryNew jl = new alice.tuprologx.pj.lib.PJLibraryNew();
            engine.loadLibrary(jl);
            _stack.add(engine);
        }
    }

    static private void popEngine() {
        current--;
    }

    static private PJProlog engine() {
        return _stack.get(current);
    }
    

    public static <T> T newInstance(Class<?> cl) throws Exception {
        return PJ.<T>newInstance(cl,null);
    }

    @SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> cl, TxTheory init) throws Exception {
            J2PProxyFactory pf = new J2PProxyFactory();
            pf.setSuperclass(cl.isInterface() ? Object.class : cl);
            pf.setInterfaces(cl.isInterface() ?
                new Class[] {cl, PrologObject.class} :
                new Class[] {PrologObject.class});
            pf.setHandler(pj);
            PrologObject po = (PrologObject)pf.create(new Class[0],new Object[0]);
            PrologMetaClass metaClass = po.getMetaPrologClass();
            for (PrologMetaField f : metaClass.getPrologFields()) {
                f.init(po);
            }
            if (init != null) {                
                po.setTheory(init);
            }		
            return (T)po;
    }

    /**
     * External interface for calling a Prolog method - if a call is reentrant (e.g.
     * because it is coming from the PJLibrary and because the context of the current
     * Prolog call needs to be saved) a new engine is pushed onto the stack.
     */
    public static Object call(Object receiver, Method method, Object args[], boolean reentrant) throws Throwable {
        if (reentrant || PJ.reentrant > 0)
            pushEngine();
        try {
            return pj.invoke(receiver, method, method, args);
        }
        catch (Exception e) {
            return null;
        }
        finally {
            if (reentrant || PJ.reentrant > 0)
                popEngine();
        }
    }

    @Override
	public Object invoke(Object receiver, Method method, Method proceed, Object[] args) throws Throwable {                                        
        if (method.getDeclaringClass().equals(PrologObject.class)) {
            return invokeInternal(receiver, method, args); //dispatch PrologObject interface calls!
        }
        if (method.getAnnotation(PrologMethod.class) == null) {
            return proceed.invoke(receiver, args);
        }

        if (PJ.reentrant > 0) {
            pushEngine();
        }
        PJ.reentrant++;
        
        Object result = null;
        try {

        //some useful objects for dispatching the method call
        PrologObject po = (PrologObject)receiver;
        PrologMetaClass metaClass = getMetaClass(receiver);
        TxTheory class_t = metaClass.getTheory();//classTheory(receiver.getClass());            
        
        //PrologMetaField[] metaFields = metaClass.getPrologFields();
        PrologInvocationContext ctx = new PrologInvocationContext(method, args);        
        /* theory = class_theory + method_theory + fields_theories */
        WithTermifiable withTermifiable = metaClass.getJavaClass().getAnnotation(WithTermifiable.class);
        if (withTermifiable != null) {
            for (String className : withTermifiable.value()) {
                Class<?> klass = Class.forName(className);
                String termName = klass.getAnnotation(Termifiable.class).predicate();
                TxJavaTerm.hashtable.put(termName, klass);
            }
        }
        engine().setTheory(class_t);
        if (receiver != null) {
            TxJavaObject<Object> jo = new TxJavaObject<Object>(receiver);                        
            TxCompound1<TxJavaObject<?>> head = new TxCompound1<TxJavaObject<?>>("this", jo);
            TxClause<TxCompound1<?>, TxNil> ct = new TxClause<TxCompound1<?>, TxNil>(head,null);            
            ArrayList<TxClause<?,?>> list = new ArrayList<TxClause<?,?>>();
            list.add(ct);
            TxTheory t = new TxTheory(list);
            engine().addTheory(t);
        }
        
        for (PrologMetaMethod metaMethod : metaClass.getPrologMethods()) {
            TxTheory method_t = metaMethod.getTheory();
            engine().addTheory(method_t);
        }

        for (PrologMetaField metaField : metaClass.getPrologFields()) {
            TxTheory field_t = metaField.getTheory();
            engine().addTheory(field_t);
        }
        
        TxTheory t = po.getTheory();
        if (t != null)
            engine().addTheory(t);
        
        //System.out.println(engine.getTheory().marshal());
        //dispatch the Proog method call to a suitable PrologInvocationContext object
        result = ctx.dispatch(engine(),args);
    }
    finally {
        //FIXME: should remove engine when not needed!
    }
        return result;
    }

    public Object invokeInternal(Object receiver, Method method, Object[] args) {        
        if (method.getName().equals("getMetaPrologClass")) {
            return getMetaClass(receiver);
        } 
        else if (method.getName().equals("getTheory"))
            return getTheory(receiver);
        else if (method.getName().equals("setTheory"))
            return setTheory(receiver, (TxTheory)args[0]);
        return null;                        
    }

    private PrologMetaClass getMetaClass(Object o) {
        try {
            java.lang.reflect.Field metaclass_field = o.getClass().getField("_meta$Prolog$Class");
            PrologMetaClass metaClass = (PrologMetaClass)metaclass_field.get(o);                
            if (metaClass == null) {
                metaClass = new PrologMetaClass(o.getClass());
                metaclass_field.set(o,metaClass);                
            }
            return metaClass;
        }
        catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }            
    }
    
    private TxTheory getTheory(Object o) {
        try {
            java.lang.reflect.Field metaclass_field = o.getClass().getField("_prolog$Theory");
            TxTheory t = (TxTheory)metaclass_field.get(o);            
            return t;
        }
        catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }            
    }
    
    private Void setTheory(Object o, TxTheory t) {
        try {
            java.lang.reflect.Field metaclass_field = o.getClass().getField("_prolog$Theory");
            metaclass_field.set(o, t);  
            return null;
        }
        catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }            
    }
    
    public static alice.tuprolog.TuStruct registerJavaObject(Object o) {
        //return engine.registerJavaObject(o);        
        try {
            return engine().getPJLibrary().register(o);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static Object getRegisteredJavaObject(alice.tuprolog.TuStruct t) {
        //return engine.getJavaObject(t); 
        try {
            Object obj = engine().getPJLibrary().getRegisteredObject(t);
            if (obj == null)
                return engine().getPJLibrary().getRegisteredDynamicObject(t);
            else
                return obj;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static void assertClause(PrologObject po, alice.tuprolog.Term clause) {
        try {
            pushEngine();
            engine().setTheory(po.getTheory());
            TxCompound1<TxTerm<?>> goal = new TxCompound1<TxTerm<?>>("assert", TxTerm.unmarshal(clause));
            engine().solve(goal);
            po.setTheory(engine().getTheory());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            popEngine();
        }
    }

    public static void retractClause(PrologObject po, alice.tuprolog.Term clause) {
        try {
            pushEngine();
            engine().setTheory(po.getTheory());
            TxCompound1<TxTerm<?>> goal = new TxCompound1<TxTerm<?>>("retract", TxTerm.unmarshal(clause));
            engine().solve(goal);
            po.setTheory(engine().getTheory());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            popEngine();
        }
    }

    public static void retractAllClauses(PrologObject po, alice.tuprolog.Term clause) {
        try {
            pushEngine();
            engine().setTheory(po.getTheory());
            alice.tuprolog.TuStruct goal = TuStruct.createTuStruct1("retractall", clause);
            System.out.println(goal);
            engine().engine.solve(goal);
            po.setTheory(engine().getTheory());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            popEngine();
        }
    }
}

