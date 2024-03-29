/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tuprologx.pj.lib;
import static alice.tuprolog.TuPrologError.*;
import static alice.tuprolog.TuFactory.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import alice.tuprolog.TuInt;
import alice.tuprolog.TuLibrary;
import alice.tuprolog.TuNumber;
import alice.tuprolog.TuTerm;
import alice.tuprolog.TuTerm;
import alice.tuprolog.Term;
import alice.tuprolog.TuFactory;
import alice.tuprolog.TuVar;

import alice.tuprolog.lib.*;

/**
 *
 * This class represents a tuProlog library enabling the interaction
 * with the Java environment from tuProlog.
 *
 * Works only with JDK 1.2 (because of setAccessible method)
 *
 * The most specific method algorithm used to find constructors / methods
 * has been inspired by the article
 *     "What Is Interactive Scripting?", by Michael Travers
 *     Dr. Dobb's -- Software Tools for the Professional Programmer
 *     January 2000
 *     CMP  Media Inc., a United News and Media Company
 *
 * Library/Theory Dependency:  BasicLibrary
 *
 *
 *
 */
@SuppressWarnings("serial")
public class PJLibrary extends TuLibrary {

    /**
     * java objects referenced by prolog terms (keys)
     */
    private HashMap<String, Object> currentObjects = new HashMap<String, Object>();
    /**
     * inverse map useful for implementation issue
     */
    private IdentityHashMap<Object, TuTerm> currentObjects_inverse = new IdentityHashMap<Object, TuTerm>();

    private HashMap<String, Object> staticObjects = new HashMap<String, Object>();
    private IdentityHashMap<Object, TuTerm> staticObjects_inverse = new IdentityHashMap<Object, TuTerm>();

    /**
     * progressive conter used to identify registered objects
     */
    private int id = 0;

    /**
     * library theory
     */
    @Override
    public String getTheory() {
        return
        //
        // operators defined by the JavaLibrary theory
        //
        ":- op(800,xfx,'<-').\n" + ":- op(800,xfx,':=').\n" + ":- op(850,xfx,'returns').\n" + ":- op(200,xfx,'as').\n"
                + ":- op(600,xfx,'.'). \n" +
                //
                // flags defined by the JavaLibrary theory
                //
                //":- flag(java_object_backtrackable,[true,false],false,true).\n" +
                //
                //
                //"java_object(ClassName,Args,Id):- current_prolog_flag(java_object_backtrackable,false),!,java_object_nb(ClassName,Args,Id).\n" +
                //"java_object(ClassName,Args,Id):- !,java_object_bt(ClassName,Args,Id).\n" +

                "java_object_bt(ClassName,Args,Id):- java_object(ClassName,Args,Id).\n"
                + "java_object_bt(ClassName,Args,Id):- destroy_object(Id).\n"
                + "Obj <- What :- java_call(Obj,What,Res), Res \\== false.\n"
                + "Obj <- What returns Res :- java_call(Obj,What,Res).\n"
                + "java_array_set(Array,Index,Object):-           class('java.lang.reflect.Array') <- set(Array as 'java.lang.Object',Index,Object as 'java.lang.Object'),!.\n"
                + "java_array_set(Array,Index,Object):-			java_array_set_primitive(Array,Index,Object).\n"
                + "java_array_get(Array,Index,Object):-           class('java.lang.reflect.Array') <- get(Array as 'java.lang.Object',Index) returns Object,!.\n"
                + "java_array_get(Array,Index,Object):-       java_array_get_primitive(Array,Index,Object).\n" +

                "java_array_length(Array,Length):-              class('java.lang.reflect.Array') <- getLength(Array as 'java.lang.Object') returns Length.\n"
                + "java_object_string(Object,String):-    Object <- toString returns String.    \n";
    }

    @Override
    public void dismiss() {
        currentObjects.clear();
        currentObjects_inverse.clear();
    }

    public void dismissAll() {
        currentObjects.clear();
        currentObjects_inverse.clear();
        staticObjects.clear();
        staticObjects_inverse.clear();
    }

    @Override
    public void onSolveBegin(Term goal) {
        //id = 0;
        currentObjects.clear();
        currentObjects_inverse.clear();
        for (Map.Entry<Object, TuTerm> en : staticObjects_inverse.entrySet()) {
            bindDynamicObject(en.getValue(), en.getKey());
        }
        preregisterObjects();
    }

    @Override
    public void onSolveEnd() {
    }

    /**
     * objects actually pre-registered in order to be
     * available since the beginning of demonstration
     */
    protected void preregisterObjects() {
        try {
            bindDynamicObject(TuFactory.createTuAtom("stdout"), System.out);
            bindDynamicObject(TuFactory.createTuAtom("stderr"), System.err);
            bindDynamicObject(TuFactory.createTuAtom("runtime"), Runtime.getRuntime());
            bindDynamicObject(TuFactory.createTuAtom("current_thread"), Thread.currentThread());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------

    /**
     * Creates of a java object - not backtrackable case
     */
    public boolean java_object_3(Term className, Term argl, Term id) {
        className = className.dref();
        TuTerm arg = (TuTerm) argl.dref();
        id = id.dref();
        try {
            if (!className.isAtomSymbol()) {
                return false;
            }
            String clName = ((TuTerm) className).fname();
            // check for array type
            if (clName.endsWith("[]")) {
                Object[] list = getArrayFromList(arg);
                int nargs = ((TuNumber) list[0]).intValue();
                return java_array(clName, nargs, id);
            }
            Signature args = parseArg(getArrayFromList(arg));
            if (args == null) {
                return false;
            }
            // object creation with argument described in args
            try {
                Class<?> cl = Class.forName(clName);
                Object[] args_value = args.getValues();
                //
                //Constructor co=cl.getConstructor(args.getTypes());
                Constructor<?> co = lookupConstructor(cl, args.getTypes(), args_value);
                //
                //
                if (co == null) {
                    getEngine().warn("Constructor not found: class " + clName);
                    return false;
                }

                Object obj = co.newInstance(args_value);
                return bindDynamicObject(id, obj);
            } catch (ClassNotFoundException ex) {
                getEngine().warn("Java class not found: " + clName);
                return false;
            } catch (InvocationTargetException ex) {
                getEngine().warn("Invalid constructor arguments.");
                return false;
            } catch (NoSuchMethodException ex) {
                getEngine().warn("Constructor not found: " + args.getTypes());
                return false;
            } catch (InstantiationException ex) {
                getEngine().warn("Objects of class " + clName + " cannot be instantiated");
                return false;
            } catch (IllegalArgumentException ex) {
                getEngine().warn("Illegal constructor arguments  " + args);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Destroy the link to a java object - called not directly, but from
     * predicate java_object (as second choice, for backtracking)
     */
    public boolean destroy_object_1(Term id) {
        id = id.dref();
        try {
            if (id.isGround()) {
                unregisterDynamic((TuTerm) id);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Creates of a java class
     */
    public boolean java_class_4(Term clSource, Term clName, Term clPathes, Term id) {
        TuTerm classSource = (TuTerm) clSource.dref();
        TuTerm className = (TuTerm) clName.dref();
        TuTerm classPathes = (TuTerm) clPathes.dref();
        id = id.dref();
        try {
            String fullClassName = alice.util.Tools.removeApices(className.toString());

            String fullClassPath = fullClassName.replace('.', '/');
            Iterator<? extends Term> it = classPathes.listIteratorProlog();
            String cp = "";
            while (it.hasNext()) {
                if (cp.length() > 0) {
                    cp += ";";
                }
                cp += alice.util.Tools.removeApices(it.next().toString());
            }
            if (cp.length() > 0) {
                cp = " -classpath " + cp;
            }

            String text = alice.util.Tools.removeApices(classSource.toString());

            //System.out.println("class source: "+text+
            //                   "\nid: "+id+
            //                   "\npath: "+fullClassPath);
            try {
                FileWriter file = new FileWriter(fullClassPath + ".java");
                file.write(text);
                file.close();
            } catch (IOException ex) {
                getEngine().warn("Compilation of java sources failed");
                getEngine().warn("(creation of " + fullClassPath + ".java fail failed)");
                return false;
            }
            String cmd = "javac " + cp + " " + fullClassPath + ".java";
            //System.out.println("EXEC: "+cmd);
            try {
                Process jc = Runtime.getRuntime().exec(cmd);
                int res = jc.waitFor();
                if (res != 0) {
                    getEngine().warn("Compilation of java sources failed");
                    getEngine().warn("(java compiler (javac) has stopped with errors)");
                    return false;
                }
            } catch (IOException ex) {
                getEngine().warn("Compilation of java sources failed");
                getEngine().warn("(java compiler (javac) invocation failed)");
                return false;
            }
            try {
                Class<?> the_class = Class.forName(fullClassName, true, new ClassLoader());
                return bindDynamicObject(id, the_class);
            } catch (ClassNotFoundException ex) {
                getEngine().warn("Compilation of java sources failed");
                getEngine().warn("(Java Class compiled, but not created: " + fullClassName + " )");
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Calls a method of a Java object
     *
     */
    public boolean java_call_3(Term objId, Term method_name, Term idResult) {
        objId = objId.dref();
        idResult = idResult.dref();
        TuTerm method = (TuTerm) method_name.dref();
        Object obj = null;
        Signature args = null;
        String methodName = null;
        try {
            methodName = method.fname();
            // check for accessing field   Obj.Field <- set/get(X)
            //  in that case: objId is '.'(Obj, Field)

            if (!objId.isAtomSymbol()) {
                if (objId .isVar()) {
                    return false;
                }
                TuTerm sel = (TuTerm) objId;
                if (sel.fname().equals(".") && sel.getPlArity() == 2 && method.getPlArity() == 1) {
                    if (methodName.equals("set"))
                        return java_set(sel.getDerefArg(0), sel.getDerefArg(1), method.getDerefArg(0));
                    else if (methodName.equals("get"))
                        return java_get(sel.getDerefArg(0), sel.getDerefArg(1), method.getDerefArg(0));
                }
            }

            args = parseArg(method);

            // object and argument must be instantiated
            if (objId .isVar() || args == null)
                return false;

            //System.out.println(args);
            String objName = alice.util.Tools.removeApices(objId.toString());
            obj = currentObjects.get(objName);
            Object res = null;

            if (obj != null) {
                Class<?> cl = obj.getClass();
                //
                //
                Object[] args_values = args.getValues();
                Method m = lookupMethod(cl, methodName, args.getTypes(), args_values);
                //
                //
                if (m == null) {
                    Object[] newValues = new Object[args_values.length];
                    Class<?>[] newTypes = new Class<?>[args_values.length];
                    //boolean ok = true;
                    for (int i = 0; i < method.getPlArity(); i++) {
                        newValues[i] = alice.tuprologx.pj.model.TxTerm.unmarshal(method.getPlainArg(i));
                        newTypes[i] = newValues[i].getClass();
                    }
                    m = lookupMethod(cl, methodName, newTypes, newValues);
                    if (m != null)
                        args_values = newValues;
                }
                if (m != null) {
                    try {
                        // works only with JDK 1.2, NOT in Sun Application Server!
                        //m.setAccessible(true);
                        res = m.invoke(obj, args_values);

                    } catch (IllegalAccessException ex) {
                        getEngine().warn("Method invocation failed: " + methodName + "( signature: " + args + " )");
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    getEngine().warn("Method not found: " + methodName + "( signature: " + args + " )");
                    return false;
                }
            } else {
                if (objId.isCompound()) {
                    TuTerm id = (TuTerm) objId;
                    if (id.getPlArity() == 1 && id.fname().equals("class")) {
                        try {
                            Class<?> cl = Class.forName(alice.util.Tools.removeApices(id.getPlainArg(0).toString()));
                            Method m = cl.getMethod(methodName, args.getTypes());
                            m.setAccessible(true);
                            res = m.invoke(null, args.getValues());
                        } catch (ClassNotFoundException ex) {
                            // if not found even as a class id -> consider as a String object value
                            getEngine().warn("Unknown class.");
                            ex.printStackTrace();
                            return false;
                        }
                    } else {
                        // the object is the string itself
                        Method m = java.lang.String.class.getMethod(methodName, args.getTypes());
                        m.setAccessible(true);
                        res = m.invoke(objName, args.getValues());
                    }
                } else {
                    // the object is the string itself
                    Method m = java.lang.String.class.getMethod(methodName, args.getTypes());
                    m.setAccessible(true);
                    res = m.invoke(objName, args.getValues());
                }
            }
            return parseResult(idResult, res);
        } catch (InvocationTargetException ex) {
            getEngine().warn("Method failed: " + methodName + " - ( signature: " + args + " ) - Original Exception: "
                    + ex.getTargetException());
            ex.printStackTrace();
            return false;
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            getEngine().warn("Method not found: " + methodName + " - ( signature: " + args + " )");
            return false;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            getEngine().warn("Invalid arguments " + args + " - ( method: " + methodName + " )");
            //ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            getEngine().warn("Generic error in method invocation " + methodName);
            return false;
        }
    }

    /*
     * set the field value of an object
     */
    private boolean java_set(Term objId, Term fieldTerm, Term what) {
        //System.out.println("SET "+objId+" "+fieldTerm+" "+what);
        what = what.dref();
        if (!fieldTerm.isAtomSymbol() || what .isVar())
            return false;
        String fieldName = ((TuTerm) fieldTerm).fname();
        Object obj = null;
        try {
            Class<?> cl = null;
            if (objId.isCompound() && ((TuTerm) objId).getPlArity() == 1 && ((TuTerm) objId).fname().equals("class")) {
                String clName = alice.util.Tools.removeApices(((TuTerm) objId).getPlainArg(0).toString());
                try {
                    cl = Class.forName(clName);
                } catch (ClassNotFoundException ex) {
                    getEngine().warn("Java class not found: " + clName);
                    return false;
                } catch (Exception ex) {
                    getEngine().warn("Static field " + fieldName + " not found in class "
                            + alice.util.Tools.removeApices(((TuTerm) objId).getPlainArg(0).toString()));
                    return false;
                }
            } else {
                String objName = alice.util.Tools.removeApices(objId.toString());
                obj = currentObjects.get(objName);
                if (obj != null) {
                    cl = obj.getClass();
                } else {
                    return false;
                }
            }

            // first check for primitive data field
            Field field = cl.getField(fieldName);
            if (field.isAnnotationPresent(alice.tuprologx.pj.annotations.PrologField.class)) {
                alice.tuprologx.pj.model.TxTerm<?> t = alice.tuprologx.pj.model.TxTerm.unmarshal(what);
                field.set(obj, t);
                return true;
            }
            if (what .isNumber()) {
                TuNumber wn = (TuNumber) what;
                if (wn .isInt()) {
                    field.setInt(obj, wn.intValue());
                } else if (wn instanceof alice.tuprolog.TuDouble) {
                    field.setDouble(obj, wn.doubleValue());
                } else if (wn instanceof alice.tuprolog.TuLong) {
                    field.setLong(obj, wn.longValue());
                } else if (wn instanceof alice.tuprolog.TuFloat) {
                    field.setFloat(obj, wn.floatValue());
                } else {
                    return false;
                }
            } else {
                String what_name = alice.util.Tools.removeApices(what.toString());
                Object obj2 = currentObjects.get(what_name);
                if (obj2 != null) {
                    field.set(obj, obj2);
                } else {
                    // consider value as a simple string
                    field.set(obj, what_name);
                }
            }

            return true;
        } catch (NoSuchFieldException ex) {
            getEngine().warn("Field " + fieldName + " not found in class " + objId);
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /*
     * get the value of the field
     */
    private boolean java_get(Term objId, Term fieldTerm, Term what) {
        //System.out.println("GET "+objId+" "+fieldTerm+" "+what);
        if (!fieldTerm.isAtomSymbol()) {
            return false;
        }
        String fieldName = ((TuTerm) fieldTerm).fname();
        Object obj = null;
        try {
            Class<?> cl = null;
            if (objId.isCompound() && ((TuTerm) objId).getPlArity() == 1 && ((TuTerm) objId).fname().equals("class")) {
                String clName = alice.util.Tools.removeApices(((TuTerm) objId).getPlainArg(0).toString());
                try {
                    cl = Class.forName(clName);
                } catch (ClassNotFoundException ex) {
                    getEngine().warn("Java class not found: " + clName);
                    return false;
                } catch (Exception ex) {
                    getEngine().warn("Static field " + fieldName + " not found in class "
                            + alice.util.Tools.removeApices(((TuTerm) objId).getPlainArg(0).toString()));
                    return false;
                }
            } else {
                String objName = alice.util.Tools.removeApices(objId.toString());
                obj = currentObjects.get(objName);
                if (obj == null) {
                    return false;
                }
                cl = obj.getClass();
            }

            Field field = cl.getField(fieldName);
            Class<?> fc = field.getType();
            // work only with JDK 1.2
            field.setAccessible(true);

            // first check for primitive types
            if (fc.equals(Integer.TYPE) || fc.equals(Byte.TYPE)) {
                int value = field.getInt(obj);
                return unify(what, createTuInt(value));
            } else if (fc.equals(java.lang.Long.TYPE)) {
                long value = field.getLong(obj);
                return unify(what, createTuLong(value));
            } else if (fc.equals(java.lang.Float.TYPE)) {
                float value = field.getFloat(obj);
                return unify(what, createTuFloat(value));
            } else if (fc.equals(java.lang.Double.TYPE)) {
                double value = field.getDouble(obj);
                return unify(what, createTuDouble(value));
            } else {
                // the field value is an object
                Object res = field.get(obj);
                return bindDynamicObject(what, res);
            }
            //} catch (ClassNotFoundException ex){
            //    getEngine().warn("object of unknown class "+objId);
            //ex.printStackTrace();
            //    return false;
        } catch (NoSuchFieldException ex) {
            getEngine().warn("Field " + fieldName + " not found in class " + objId);
            return false;
        } catch (Exception ex) {
            getEngine().warn("Generic error in accessing the field");
            //ex.printStackTrace();
            return false;
        }
    }

    public boolean java_array_set_primitive_3(Term obj_id, Term i, Term what) {
        TuTerm objId = (TuTerm) obj_id.dref();
        TuNumber index = (TuNumber) i.dref();
        what = what.dref();
        //System.out.println("SET "+objId+" "+fieldTerm+" "+what);
        Object obj = null;
        if (!index.isInteger()) {
            return false;
        }
        try {
            Class<?> cl = null;
            String objName = alice.util.Tools.removeApices(objId.toString());
            obj = currentObjects.get(objName);
            if (obj != null) {
                cl = obj.getClass();
            } else {
                return false;
            }

            if (!cl.isArray()) {
                return false;
            }
            String name = cl.toString();
            if (name.equals("class [I")) {
                if (!(what .isNumber())) {
                    return false;
                }
                byte v = (byte) ((TuNumber) what).intValue();
                Array.setInt(obj, index.intValue(), v);
            } else if (name.equals("class [D")) {
                if (!(what .isNumber())) {
                    return false;
                }
                double v = ((TuNumber) what).doubleValue();
                Array.setDouble(obj, index.intValue(), v);
            } else if (name.equals("class [F")) {
                if (!(what .isNumber())) {
                    return false;
                }
                float v = ((TuNumber) what).floatValue();
                Array.setFloat(obj, index.intValue(), v);
            } else if (name.equals("class [L")) {
                if (!(what .isNumber())) {
                    return false;
                }
                long v = ((TuNumber) what).longValue();
                Array.setFloat(obj, index.intValue(), v);
            } else if (name.equals("class [C")) {
                String s = what.toString();
                Array.setChar(obj, index.intValue(), s.charAt(0));
            } else if (name.equals("class [Z")) {
                String s = what.toString();
                if (s.equals("true")) {
                    Array.setBoolean(obj, index.intValue(), true);
                } else if (s.equals("false")) {
                    Array.setBoolean(obj, index.intValue(), false);
                } else {
                    return false;
                }
            } else if (name.equals("class [B")) {
                if (!(what .isNumber())) {
                    return false;
                }
                int v = ((TuNumber) what).intValue();
                Array.setByte(obj, index.intValue(), (byte) v);
            } else if (name.equals("class [S")) {
                if (!(what .isNumber())) {
                    return false;
                }
                short v = (short) ((TuNumber) what).intValue();
                Array.setShort(obj, index.intValue(), v);
            } else {
                return false;
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean java_array_get_primitive_3(Term obj_id, Term i, Term what) {
        TuTerm objId = (TuTerm) obj_id.dref();
        TuNumber index = (TuNumber) i.dref();
        what = what.dref();
        //System.out.println("SET "+objId+" "+fieldTerm+" "+what);
        Object obj = null;
        if (!index.isInteger()) {
            return false;
        }
        try {
            Class<?> cl = null;
            String objName = alice.util.Tools.removeApices(objId.toString());
            obj = currentObjects.get(objName);
            if (obj != null) {
                cl = obj.getClass();
            } else {
                return false;
            }

            if (!cl.isArray()) {
                return false;
            }
            String name = cl.toString();
            if (name.equals("class [I")) {
                Term value = createTuInt(Array.getInt(obj, index.intValue()));
                return unify(what, value);
            } else if (name.equals("class [D")) {
                Term value = createTuDouble(Array.getDouble(obj, index.intValue()));
                return unify(what, value);
            } else if (name.equals("class [F")) {
                Term value = createTuFloat(Array.getFloat(obj, index.intValue()));
                return unify(what, value);
            } else if (name.equals("class [L")) {
                Term value = createTuLong(Array.getLong(obj, index.intValue()));
                return unify(what, value);
            } else if (name.equals("class [C")) {
                Term value = createTuAtom("" + Array.getChar(obj, index.intValue()));
                return unify(what, value);
            } else if (name.equals("class [Z")) {
                boolean b = Array.getBoolean(obj, index.intValue());
                if (b) {
                    return unify(what, alice.tuprolog.Term.TRUE);
                } else {
                    return unify(what, alice.tuprolog.Term.FALSE);
                }
            } else if (name.equals("class [B")) {
                Term value = createTuInt(Array.getByte(obj, index.intValue()));
                return unify(what, value);
            } else if (name.equals("class [S")) {
                Term value = createTuInt(Array.getInt(obj, index.intValue()));
                return unify(what, value);
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    private boolean java_array(String type, int nargs, Term id) {
        try {
            Object array = null;
            String obtype = type.substring(0, type.length() - 2);
            if (obtype.equals("boolean")) {
                array = new boolean[nargs];
            } else if (obtype.equals("byte")) {
                array = new byte[nargs];
            } else if (obtype.equals("char")) {
                array = new char[nargs];
            } else if (obtype.equals("short")) {
                array = new short[nargs];
            } else if (obtype.equals("int")) {
                array = new int[nargs];
            } else if (obtype.equals("long")) {
                array = new long[nargs];
            } else if (obtype.equals("float")) {
                array = new float[nargs];
            } else if (obtype.equals("double")) {
                array = new double[nargs];
            } else {
                Class<?> cl = Class.forName(obtype);
                array = Array.newInstance(cl, nargs);
            }
            return bindDynamicObject(id, array);
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    /**
     * creation of method signature from prolog data
     */
    private Signature parseArg(TuTerm method) {
        Object[] values = new Object[method.getPlArity()];
        Class<?>[] types = new Class[method.getPlArity()];
        for (int i = 0; i < method.getPlArity(); i++) {
            if (!parse_arg(values, types, i, method.getDerefArg(i)))
                return null;
        }
        return new Signature(values, types);
    }

    private Signature parseArg(Object[] objs) {
        Object[] values = new Object[objs.length];
        Class<?>[] types = new Class[objs.length];
        for (int i = 0; i < objs.length; i++) {
            if (!parse_arg(values, types, i, (Term) objs[i]))
                return null;
        }
        return new Signature(values, types);
    }

    private boolean parse_arg(Object[] values, Class<?>[] types, int i, Term term) {
        try {
            if (term == null) {
                values[i] = null;
                types[i] = null;
            } else if (term.isAtomSymbol()) {
                String name = alice.util.Tools.removeApices(term.toString());
                if (name.equals("true")) {
                    values[i] = Boolean.TRUE;
                    types[i] = Boolean.TYPE;
                } else if (name.equals("false")) {
                    values[i] = Boolean.FALSE;
                    types[i] = Boolean.TYPE;
                } else {
                    Object obj = currentObjects.get(name);
                    if (obj == null) {
                        values[i] = name;
                    } else {
                        values[i] = obj;
                    }
                    types[i] = values[i].getClass();
                }
            } else if (term .isNumber()) {
                TuNumber t = (TuNumber) term;
                if (t .isInt()) {
                    values[i] = new java.lang.Integer(t.intValue());
                    types[i] = java.lang.Integer.TYPE;
                } else if (t instanceof alice.tuprolog.TuDouble) {
                    values[i] = new java.lang.Double(t.doubleValue());
                    types[i] = java.lang.Double.TYPE;
                } else if (t instanceof alice.tuprolog.TuLong) {
                    values[i] = new java.lang.Long(t.longValue());
                    types[i] = java.lang.Long.TYPE;
                } else if (t instanceof alice.tuprolog.TuFloat) {
                    values[i] = new java.lang.Float(t.floatValue());
                    types[i] = java.lang.Float.TYPE;
                }
            } else if (term .isTuStruct()) {
                // argument descriptors
                TuTerm tc = (TuTerm) term;
                if (tc.fname().equals("as")) {
                    return parse_as(values, types, i, tc.getDerefArg(0), tc.getDerefArg(1));
                } else {
                    Object obj = currentObjects.get(alice.util.Tools.removeApices(tc.toString()));
                    if (obj == null) {
                        values[i] = alice.util.Tools.removeApices(tc.toString());
                    } else {
                        values[i] = obj;
                    }
                    types[i] = values[i].getClass();
                }
            } else if (term .isVar() && !((TuVar) term).isBound()) {
                values[i] = null;
                types[i] = Object.class;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     *
     * parsing 'as' operator, which makes it possible
     * to define the specific class of an argument
     *
     */
    private boolean parse_as(Object[] values, Class<?>[] types, int i, Term castWhat, Term castTo) {
        try {
            if (!(castWhat .isNumber())) {
                String castTo_name = alice.util.Tools.removeApices(((TuTerm) castTo).fname());
                String castWhat_name = alice.util.Tools.removeApices(castWhat.dref().toString());
                //System.out.println(castWhat_name+" "+castTo_name);
                if (castTo_name.equals("java.lang.String") && castWhat_name.equals("true")) {
                    values[i] = "true";
                    types[i] = String.class;
                    return true;
                } else if (castTo_name.equals("java.lang.String") && castWhat_name.equals("false")) {
                    values[i] = "false";
                    types[i] = String.class;
                    return true;
                } else if (castTo_name.endsWith("[]")) {
                    if (castTo_name.equals("boolean[]")) {
                        castTo_name = "[Z";
                    } else if (castTo_name.equals("byte[]")) {
                        castTo_name = "[B";
                    } else if (castTo_name.equals("short[]")) {
                        castTo_name = "[S";
                    } else if (castTo_name.equals("char[]")) {
                        castTo_name = "[C";
                    } else if (castTo_name.equals("int[]")) {
                        castTo_name = "[I";
                    } else if (castTo_name.equals("long[]")) {
                        castTo_name = "[L";
                    } else if (castTo_name.equals("float[]")) {
                        castTo_name = "[F";
                    } else if (castTo_name.equals("double[]")) {
                        castTo_name = "[D";
                    } else {
                        castTo_name = "[L" + castTo_name.substring(0, castTo_name.length() - 2) + ";";
                    }
                }
                if (!castWhat_name.equals("null")) {
                    Object obj_to_cast = currentObjects.get(castWhat_name);
                    if (obj_to_cast == null) {
                        if (castTo_name.equals("boolean")) {
                            if (castWhat_name.equals("true")) {
                                values[i] = new Boolean(true);
                            } else if (castWhat_name.equals("false")) {
                                values[i] = new Boolean(false);
                            } else {
                                return false;
                            }
                            types[i] = Boolean.TYPE;
                        } else {
                            // conversion to array
                            return false;
                        }
                    } else {
                        values[i] = obj_to_cast;
                        try {
                            types[i] = (Class.forName(castTo_name));
                        } catch (ClassNotFoundException ex) {
                            getEngine().warn("Java class not found: " + castTo_name);
                            return false;
                        }
                    }
                } else {
                    values[i] = null;
                    if (castTo_name.equals("byte")) {
                        types[i] = Byte.TYPE;
                    } else if (castTo_name.equals("short")) {
                        types[i] = Short.TYPE;
                    } else if (castTo_name.equals("char")) {
                        types[i] = Character.TYPE;
                    } else if (castTo_name.equals("int")) {
                        types[i] = java.lang.Integer.TYPE;
                    } else if (castTo_name.equals("long")) {
                        types[i] = java.lang.Long.TYPE;
                    } else if (castTo_name.equals("float")) {
                        types[i] = java.lang.Float.TYPE;
                    } else if (castTo_name.equals("double")) {
                        types[i] = java.lang.Double.TYPE;
                    } else if (castTo_name.equals("boolean")) {
                        types[i] = java.lang.Boolean.TYPE;
                    } else {
                        try {
                            types[i] = (Class.forName(castTo_name));
                        } catch (ClassNotFoundException ex) {
                            getEngine().warn("Java class not found: " + castTo_name);
                            return false;
                        }
                    }
                }
            } else {
                TuNumber num = (TuNumber) castWhat;
                String castTo_name = ((TuTerm) castTo).fname();
                if (castTo_name.equals("byte")) {
                    values[i] = new Byte((byte) num.intValue());
                    types[i] = Byte.TYPE;
                } else if (castTo_name.equals("short")) {
                    values[i] = new Short((short) num.intValue());
                    types[i] = Short.TYPE;
                } else if (castTo_name.equals("int")) {
                    values[i] = new Integer(num.intValue());
                    types[i] = Integer.TYPE;
                } else if (castTo_name.equals("long")) {
                    values[i] = new java.lang.Long(num.longValue());
                    types[i] = java.lang.Long.TYPE;
                } else if (castTo_name.equals("float")) {
                    values[i] = new java.lang.Float(num.floatValue());
                    types[i] = java.lang.Float.TYPE;
                } else if (castTo_name.equals("double")) {
                    values[i] = new java.lang.Double(num.doubleValue());
                    types[i] = java.lang.Double.TYPE;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            getEngine().warn("Casting " + castWhat + " to " + castTo + " failed");
            return false;
        }
        return true;
    }

    /**
     *  parses return value
     *  of a method invokation
     */
    private boolean parseResult(Term id, Object obj) {
        if (obj == null) {
            //return unify(id,Term.TRUE);
            return unify(id, createTuVar());
        }
        try {
            if (Boolean.class.isInstance(obj)) {
                if (((Boolean) obj).booleanValue()) {
                    return unify(id, Term.TRUE);
                } else {
                    return unify(id, Term.FALSE);
                }
            } else if (Byte.class.isInstance(obj)) {
                return unify(id, createTuInt(((Byte) obj).intValue()));
            } else if (Short.class.isInstance(obj)) {
                return unify(id, createTuInt(((Short) obj).intValue()));
            } else if (Integer.class.isInstance(obj)) {
                return unify(id, createTuInt(((Integer) obj).intValue()));
            } else if (java.lang.Long.class.isInstance(obj)) {
                return unify(id, createTuLong(((java.lang.Long) obj).longValue()));
            } else if (java.lang.Float.class.isInstance(obj)) {
                return unify(id, createTuFloat(((java.lang.Float) obj).floatValue()));
            } else if (java.lang.Double.class.isInstance(obj)) {
                return unify(id, createTuDouble(((java.lang.Double) obj).doubleValue()));
            } else if (String.class.isInstance(obj)) {
                return unify(id, createTuAtom((String) obj));
            } else if (Character.class.isInstance(obj)) {
                return unify(id, createTuAtom(((Character) obj).toString()));
            } else {
                return bindDynamicObject(id, obj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Object[] getArrayFromList(TuTerm list) {
        Object args[] = new Object[list.listSize()];
        Iterator<? extends Term> it = list.listIteratorProlog();
        int count = 0;
        while (it.hasNext()) {
            args[count++] = it.next();
        }
        return args;
    }

    /**
     * Register an object with the specified id.
     * The life-time of the link to the object is engine life-time,
     * available besides the individual query.
     * 
     * The identifier must be a ground object.   
     * 
     * @param id object identifier 
     * @param obj the object
     * @return true if the operation is successful 
     * @throws InvalidObjectIdException if the object id is not valid
     */
    public boolean register(TuTerm id, Object obj) throws InvalidObjectIdException {
        /*
         * note that this method act on the staticObject
         * and staticObject_inverse hashmaps
         */
        if (!id.isGround()) {
            throw new InvalidObjectIdException();
        }
        // already registered object?
        synchronized (staticObjects) {
            Object aKey = staticObjects_inverse.get(obj);

            if (aKey != null) {
                // object already referenced
                return false;
            } else {
                String raw_name = alice.util.Tools.removeApices(id.dref().toString());
                staticObjects.put(raw_name, obj);
                staticObjects_inverse.put(obj, id);
                return true;
            }
        }
    }

    /**
     * Registers an object, with automatic creation of the identifier.
     * 
     * If the object is already registered,
     * its identifier is returned
     * 
     * @param obj object to be registered. 
     * @return fresh id
     */
    public TuTerm register(Object obj) {
        //System.out.println("lib: "+this+" current id: "+this.id);

        // already registered object?
        synchronized (staticObjects) {
            Object aKey = staticObjects_inverse.get(obj);
            if (aKey != null) {
                // object already referenced -> unifying terms
                // referencing the object
                //log("obj already registered: unify "+id+" "+aKey);
                return (TuTerm) aKey;
            } else {
                TuTerm id = generateFreshId();
                staticObjects.put(id.fname(), obj);
                staticObjects_inverse.put(obj, id);
                return id;
            }
        }
    }

    /**
     * Gets the reference to an object previously registered
     * 
     * @param id object id
     * @return the object, if present
     * @throws InvalidObjectIdException
     */
    public Object getRegisteredObject(TuTerm id) throws InvalidObjectIdException {
        if (!id.isGround()) {
            throw new InvalidObjectIdException();
        }
        synchronized (staticObjects) {
            return staticObjects.get(alice.util.Tools.removeApices(id.toString()));
        }
    }

    /**
     * Unregisters an object, given its identifier 
     * 
     * 
     * @param id object identifier
     * @return true if the operation is successful
     * @throws InvalidObjectIdException if the id is not valid (e.g. is not ground)
     */
    public boolean unregister(TuTerm id) throws InvalidObjectIdException {
        if (!id.isGround()) {
            throw new InvalidObjectIdException();
        }
        synchronized (staticObjects) {
            String raw_name = alice.util.Tools.removeApices(id.toString());
            Object obj = staticObjects.remove(raw_name);
            if (obj != null) {
                staticObjects_inverse.remove(obj);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Registers an object only for the running query life-time
     * 
     * @param id object identifier
     * @param obj object 
     */
    public void registerDynamic(TuTerm id, Object obj) {
        synchronized (currentObjects) {
            String raw_name = alice.util.Tools.removeApices(id.toString());
            currentObjects.put(raw_name, obj);
            currentObjects_inverse.put(obj, id);
        }
    }

    /**
     * Registers an object for the query life-time, 
     * with the automatic generation of the identifier.
     * 
     * If the object is already registered,
     * its identifier is returned
     * 
     * @param obj object to be registered
     * @return identifier
     */
    public TuTerm registerDynamic(Object obj) {
        //System.out.println("lib: "+this+" current id: "+this.id);

        // already registered object?
        synchronized (currentObjects) {
            Object aKey = currentObjects_inverse.get(obj);
            if (aKey != null) {
                // object already referenced -> unifying terms
                // referencing the object
                //log("obj already registered: unify "+id+" "+aKey);
                return (TuTerm) aKey;
            } else {
                TuTerm id = generateFreshId();
                currentObjects.put(id.fname(), obj);
                currentObjects_inverse.put(obj, id);
                return id;
            }
        }
    }

    /**
     * Gets a registered dynamic object
     * (returns null if not presents)
     */
    public Object getRegisteredDynamicObject(TuTerm id) throws InvalidObjectIdException {
        if (!id.isGround()) {
            throw new InvalidObjectIdException();
        }
        synchronized (currentObjects) {
            return currentObjects.get(alice.util.Tools.removeApices(id.toString()));
        }
    }

    /**
     * Unregister the object, only for dynamic case
     * 
     * @param id object identifier
     * @return true if the operation is successful
     */
    public boolean unregisterDynamic(TuTerm id) {
        synchronized (currentObjects) {
            String raw_name = alice.util.Tools.removeApices(id.toString());
            Object obj = currentObjects.remove(raw_name);
            if (obj != null) {
                currentObjects_inverse.remove(obj);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Tries to bind specified id to a provided java object.
     * 
     * Term id can be a variable or a ground term.
     */
    protected boolean bindDynamicObject(Term id, Object obj) {
        // null object are considered to _ variable
        if (obj == null) {
            return unify(id, createTuVar());
        }
        /*if (obj instanceof alice.tuprologx.pj.model.Term<?>) {
            alice.tuprologx.pj.model.Term<?> t = (alice.tuprologx.pj.model.Term<?>)obj;
            return unify(id, t.marshal());
        }*/
        // already registered object?
        synchronized (currentObjects) {
            Object aKey = currentObjects_inverse.get(obj);
            if (aKey != null) {
                // object already referenced -> unifying terms
                // referencing the object
                //log("obj already registered: unify "+id+" "+aKey);
                return unify(id, (Term) aKey);
            } else {
                // object not previously referenced
                if (id .isVar()) {
                    // get a ground term
                    TuTerm idTerm = generateFreshId();
                    unify(id, idTerm);
                    registerDynamic(idTerm, obj);
                    //log("not ground id for a new obj: "+id+" as ref for "+obj);
                    return true;
                } else {
                    // verify of the id is already used
                    String raw_name = alice.util.Tools.removeApices(id.dref().toString());
                    Object linkedobj = currentObjects.get(raw_name);
                    if (linkedobj == null) {
                        registerDynamic((TuTerm) (id.dref()), obj);
                        //log("ground id for a new obj: "+id+" as ref for "+obj);
                        return true;
                    } else {
                        // an object with the same id is already
                        // present: must be the same object
                        return obj == linkedobj;
                    }
                }
            }
        }
    }

    /**
     * Generates a fresh numeric identifier
     * @return
     */
    protected TuTerm generateFreshId() {
        return createTuAtom("$obj_" + id++);
    }

    /**
     *  handling writeObject method is necessary in order to
     *  make the library serializable, 'nullyfing'  eventually
     *  objects registered in maps
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        HashMap<String, Object> bak00 = currentObjects;
        IdentityHashMap<Object, TuTerm> bak01 = currentObjects_inverse;
        try {
            currentObjects = null;
            currentObjects_inverse = null;
            out.defaultWriteObject();
        } catch (IOException ex) {
            currentObjects = bak00;
            currentObjects_inverse = bak01;
            throw new IOException();
        }
        currentObjects = bak00;
        currentObjects_inverse = bak01;
    }

    /**
     *  handling readObject method is necessary in order to
     *  have the library reconstructed after a serialization
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        currentObjects = new HashMap<String, Object>();
        currentObjects_inverse = new IdentityHashMap<Object, TuTerm>();
        preregisterObjects();
    }

    // --------------------------------------------------

    private static Method lookupMethod(Class<?> target, String name, Class<?>[] argClasses, Object[] argValues)
            throws NoSuchMethodException {
        // first try for exact match
        try {
            Method m = target.getMethod(name, argClasses);
            return m;
        } catch (NoSuchMethodException e) {
            if (argClasses.length == 0) { // if no args & no exact match, out of luck
                return null;
            }
        }

        // go the more complicated route
        Method[] methods = target.getMethods();
        Vector<Method> goodMethods = new Vector<Method>();
        for (int i = 0; i != methods.length; i++) {
            if (name.equals(methods[i].getName()) && matchClasses(methods[i].getParameterTypes(), argClasses))
                goodMethods.addElement(methods[i]);
        }
        switch (goodMethods.size()) {
            case 0:
                // no methods have been found checking for assignability
                // and (int -> long) conversion. One last chance:
                // looking for compatible methods considering also
                // type conversions:
                //    double --> float
                // (the first found is used - no most specific
                //  method algorithm is applied )

                for (int i = 0; i != methods.length; i++) {
                    if (name.equals(methods[i].getName())) {
                        Class<?>[] types = methods[i].getParameterTypes();
                        Object[] val = matchClasses(types, argClasses, argValues);
                        if (val != null) {
                            // found a method compatible
                            // after type conversions
                            for (int j = 0; j < types.length; j++) {
                                argClasses[j] = types[j];
                                argValues[j] = val[j];
                            }
                            return methods[i];
                        }
                    }
                }

                return null;
            case 1:
                return goodMethods.firstElement();
            default:
                return mostSpecificMethod(goodMethods);
        }
    }

    private static Constructor<?> lookupConstructor(Class<?> target, Class<?>[] argClasses, Object[] argValues)
            throws NoSuchMethodException {
        // first try for exact match
        try {
            return target.getConstructor(argClasses);
        } catch (NoSuchMethodException e) {
            if (argClasses.length == 0) { // if no args & no exact match, out of luck
                return null;
            }
        }

        // go the more complicated route
        Constructor<?>[] constructors = target.getConstructors();
        Vector<Constructor<?>> goodConstructors = new Vector<Constructor<?>>();
        for (int i = 0; i != constructors.length; i++) {
            if (matchClasses(constructors[i].getParameterTypes(), argClasses))
                goodConstructors.addElement(constructors[i]);
        }
        switch (goodConstructors.size()) {
            case 0:
                // no constructors have been found checking for assignability
                // and (int -> long) conversion. One last chance:
                // looking for compatible methods considering also
                // type conversions:
                //    double --> float
                // (the first found is used - no most specific
                //  method algorithm is applied )

                for (int i = 0; i != constructors.length; i++) {
                    Class<?>[] types = constructors[i].getParameterTypes();
                    Object[] val = matchClasses(types, argClasses, argValues);
                    if (val != null) {
                        // found a method compatible
                        // after type conversions
                        for (int j = 0; j < types.length; j++) {
                            argClasses[j] = types[j];
                            argValues[j] = val[j];
                        }
                        return constructors[i];
                    }
                }

                return null;
            case 1:
                return goodConstructors.firstElement();
            default:
                return mostSpecificConstructor(goodConstructors);
        }
    }

    // 1st arg is from method, 2nd is actual parameters
    private static boolean matchClasses(Class<?>[] mclasses, Class<?>[] pclasses) {
        if (mclasses.length == pclasses.length) {
            for (int i = 0; i != mclasses.length; i++) {
                if (!matchClass(mclasses[i], pclasses[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean matchClass(Class<?> mclass, Class<?> pclass) {
        boolean assignable = mclass.isAssignableFrom(pclass);
        if (assignable) {
            return true;
        } else {
            if (mclass.equals(java.lang.Long.TYPE) && (pclass.equals(java.lang.Integer.TYPE))) {
                return true;
            } else if (pclass.isPrimitive() && mclass.equals(java.lang.Object.class)) //boxing
                return true;
        }
        return false;
    }

    private static Method mostSpecificMethod(Vector<Method> methods) throws NoSuchMethodException {
        for (int i = 0; i != methods.size(); i++) {
            for (int j = 0; j != methods.size(); j++) {
                if ((i != j) && (moreSpecific(methods.elementAt(i), methods.elementAt(j)))) {
                    methods.removeElementAt(j);
                    if (i > j)
                        i--;
                    j--;
                }
            }
        }
        if (methods.size() == 1)
            return methods.elementAt(0);
        else
            throw new NoSuchMethodException(">1 most specific method");
    }

    // true if c1 is more specific than c2
    private static boolean moreSpecific(Method c1, Method c2) {
        Class<?>[] p1 = c1.getParameterTypes();
        Class<?>[] p2 = c2.getParameterTypes();
        int n = p1.length;
        for (int i = 0; i != n; i++) {
            if (!matchClass(p2[i], p1[i])) {
                return false;
            }
        }
        return true;
    }

    private static Constructor<?> mostSpecificConstructor(Vector<Constructor<?>> constructors)
            throws NoSuchMethodException {
        for (int i = 0; i != constructors.size(); i++) {
            for (int j = 0; j != constructors.size(); j++) {
                if ((i != j) && (moreSpecific(constructors.elementAt(i), constructors.elementAt(j)))) {
                    constructors.removeElementAt(j);
                    if (i > j)
                        i--;
                    j--;
                }
            }
        }
        if (constructors.size() == 1)
            return constructors.elementAt(0);
        else
            throw new NoSuchMethodException(">1 most specific constructor");
    }

    // true if c1 is more specific than c2
    private static boolean moreSpecific(Constructor<?> c1, Constructor<?> c2) {
        Class<?>[] p1 = c1.getParameterTypes();
        Class<?>[] p2 = c2.getParameterTypes();
        int n = p1.length;
        for (int i = 0; i != n; i++) {
            if (!matchClass(p2[i], p1[i])) {
                return false;
            }
        }
        return true;
    }

    // Checks compatibility also considering explicit type conversion.
    // The method returns the argument values, since they could be changed
    // after a type conversion.
    //
    // In particular the check must be done for the DEFAULT type of tuProlog,
    // that are int and double; so
    //   (required X, provided a DEFAULT -
    //        with DEFAULT to X conversion 'conceivable':
    //        for instance *double* to *int* is NOT considered good
    //
    //   required a float,  provided an  int  OK
    //   required a double, provided a   int  OK
    //   required a long,   provided a   int ==> already considered by
    //                                   previous match test
    //   required a float,  provided a   double OK
    //   required a int,    provided a   double => NOT CONSIDERED
    //   required a long,   provided a   double => NOT CONSIDERED
    //
    private static Object[] matchClasses(Class<?>[] mclasses, Class<?>[] pclasses, Object[] values) {
        if (mclasses.length == pclasses.length) {
            Object[] newvalues = new Object[mclasses.length];

            for (int i = 0; i != mclasses.length; i++) {
                boolean assignable = mclasses[i].isAssignableFrom(pclasses[i]);
                if (assignable
                        || (mclasses[i].equals(java.lang.Long.TYPE) && pclasses[i].equals(java.lang.Integer.TYPE))) {
                    newvalues[i] = values[i];
                } else if (mclasses[i].equals(java.lang.Float.TYPE) && pclasses[i].equals(java.lang.Double.TYPE)) {
                    // arg required: a float, arg provided: a double
                    // so we need an explicit conversion...
                    newvalues[i] = new java.lang.Float(((java.lang.Double) values[i]).floatValue());
                } else if (mclasses[i].equals(java.lang.Float.TYPE) && pclasses[i].equals(java.lang.Integer.TYPE)) {
                    // arg required: a float, arg provided: an int
                    // so we need an explicit conversion...
                    newvalues[i] = new java.lang.Float(((java.lang.Integer) values[i]).intValue());
                } else if (mclasses[i].equals(java.lang.Double.TYPE) && pclasses[i].equals(java.lang.Integer.TYPE)) {
                    // arg required: a double, arg provided: an int
                    // so we need an explicit conversion...
                    newvalues[i] = new java.lang.Double(((java.lang.Integer) values[i]).doubleValue());
                } else if (values[i] == null && !mclasses[i].isPrimitive()) {
                    newvalues[i] = null;
                } else {
                    return null;
                }
            }
            return newvalues;
        } else {
            return null;
        }
    }

}

/**
 * Signature class mantains information
 * about type and value of a method
 * arguments
 */
@SuppressWarnings("serial")
class Signature implements Serializable {
    Class<?>[] types;
    Object[] values;

    public Signature(Object[] v, Class<?>[] c) {
        values = v;
        types = c;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    Object[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        String st = "";
        for (int i = 0; i < types.length; i++) {
            st = st + "\n  Argument " + i + " -  VALUE: " + values[i] + " TYPE: " + types[i];
        }
        return st;
    }
}

/** used to load new classes without touching system class loader */
class ClassLoader extends java.lang.ClassLoader {
}

/**
 * Information about an EventListener
 */
@SuppressWarnings("serial")
class ListenerInfo implements Serializable {
    public String listenerInterfaceName;
    public EventListener listener;
    //public String eventName;
    public String eventFullClass;

    public ListenerInfo(EventListener l, String eventClass, String n) {
        listener = l;
        //this.eventName=eventName;
        this.eventFullClass = eventClass;
        listenerInterfaceName = n;
    }
}
