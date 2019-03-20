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
package alice.tuprolog.lib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EventListener;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import alice.tuprolog.TuInt;
import alice.tuprolog.TuLibrary;
import alice.tuprolog.TuNumber;
import alice.tuprolog.TuStruct;
import alice.tuprolog.TuTerm;
import alice.tuprolog.Term;
import alice.tuprolog.TuVar;
import alice.tuprolog.lib.annotations.OOLibraryEnableLambdas;
import alice.tuprolog.TuJavaException;
import alice.util.AbstractDynamicClassLoader;
import alice.util.AndroidDynamicClassLoader;
import alice.util.InspectionUtils;
import alice.util.JavaDynamicClassLoader;
/**
 * 
 * This class represents a tuProlog library enabling the interaction with the
 * Java environment from tuProlog.
 * 
 * Warning we use the setAccessible method 
 * 
 * The most specific method algorithm used to find constructors / methods has
 * been inspired by the article "What Is Interactive Scripting?", by Michael
 * Travers Dr. Dobb's -- Software Tools for the Professional Programmer January
 * 2000 CMP Media Inc., a United News and Media Company
 * 
 * Library/Theory Dependency: BasicLibrary
 */
@SuppressWarnings("serial") 
@OOLibraryEnableLambdas(mode = "active") //Alberto
public class OOLibrary extends TuLibrary {

    /**
     * java objects referenced by prolog terms (keys)
     */
    private HashMap<String,Object> currentObjects = new HashMap<String, Object>();
    /**
         * inverse map useful for implementation issue
         */
    private IdentityHashMap<Object,TuStruct> currentObjects_inverse = new IdentityHashMap<Object, TuStruct>();

    private HashMap<String,Object> staticObjects = new HashMap<String, Object>();
    private IdentityHashMap<Object,TuStruct> staticObjects_inverse = new IdentityHashMap<Object, TuStruct>();

    /**
         * progressive counter used to identify registered objects
         */
    private int id = 0;
    /**
     * progressive counter used to generate lambda function dinamically
     */
    private int counter = 0;
    
    private OOLibraryEnableLambdas lambdaPlugin;
    
    /**
	 * @author Alessio Mercurio
	 * 
	 * used to manage different classloaders.
	 */
    private AbstractDynamicClassLoader dynamicLoader;     
    
    /**
     * library theory
     */
    
    public OOLibrary()
    {
    	Class<OOLibrary> ooLibrary = OOLibrary.class;
		lambdaPlugin = ooLibrary.getAnnotation(OOLibraryEnableLambdas.class);
		
    	if (System.getProperty("java.vm.name").equals("Dalvik"))
		{
			dynamicLoader = new AndroidDynamicClassLoader(new URL[] {}, getClass().getClassLoader());
		} 
		else
		{
			dynamicLoader = new JavaDynamicClassLoader(new URL[] {}, getClass().getClassLoader());
		}
    }
    
    @Override
	public String getTheory() {
        return
        //
        // operators defined by the JavaLibrary theory
        //
        ":- op(800,xfx,'<-').\n"
                + ":- op(850,xfx,'returns').\n"
                + ":- op(200,xfx,'as').\n"
                + ":- op(600,xfx,'.'). \n"
                + 
                "new_object_bt(ClassName,Args,Id):- new_object(ClassName,Args,Id).\n"
                + "new_object_bt(ClassName,Args,Id):- destroy_object(Id).\n"
                
                + "Obj <- What :- java_call(Obj,What,Res), Res \\== false.\n"
                + "Obj <- What returns Res :- java_call(Obj,What,Res).\n"
                
                + "array_set(Array,Index,Object):- class('java.lang.reflect.Array') <- set(Array as 'java.lang.Object',Index,Object as 'java.lang.Object'), !.\n"
                + "array_set(Array,Index,Object):- java_array_set_primitive(Array,Index,Object).\n"
                + "array_get(Array,Index,Object):- class('java.lang.reflect.Array') <- get(Array as 'java.lang.Object',Index) returns Object,!.\n"
                + "array_get(Array,Index,Object):- java_array_get_primitive(Array,Index,Object).\n"
                
				+ "array_length(Array,Length):- class('java.lang.reflect.Array') <- getLength(Array as 'java.lang.Object') returns Length.\n"

                
                + //**** following section deprecated from tuProlog 3.0  ***//
                "java_object_bt(ClassName,Args,Id):- java_object(ClassName,Args,Id).\n"
                + "java_object_bt(ClassName,Args,Id):- destroy_object(Id).\n"
                
                + "java_array_set(Array,Index,Object):- class('java.lang.reflect.Array') <- set(Array as 'java.lang.Object',Index,Object as 'java.lang.Object'), !.\n"
                + "java_array_set(Array,Index,Object):- java_array_set_primitive(Array,Index,Object).\n"
                + "java_array_get(Array,Index,Object):- class('java.lang.reflect.Array') <- get(Array as 'java.lang.Object',Index) returns Object,!.\n"
                + "java_array_get(Array,Index,Object):- java_array_get_primitive(Array,Index,Object).\n"
               
                + "java_array_length(Array,Length):- class('java.lang.reflect.Array') <- getLength(Array as 'java.lang.Object') returns Length.\n"
                + "java_object_string(Object,String):- Object <- toString returns String.    \n"
                +//**** end section deprecated from tuProlog 3.0  ***//
                "java_catch(JavaGoal, List, Finally) :- call(JavaGoal), call(Finally).\n";
        
        		
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
        currentObjects.clear();
        currentObjects_inverse.clear();
        Iterator<Map.Entry<Object,TuStruct>> it = staticObjects_inverse.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object,TuStruct> en = it.next();
            bindDynamicObject( en.getValue(), en.getKey());
        }
        preregisterObjects();
    }

    @Override
	public void onSolveEnd() {
    }

    /**
     * objects actually pre-registered in order to be available since the
     * beginning of demonstration
     */
    protected void preregisterObjects() {
        try {
            bindDynamicObject(TuTerm.createAtomTerm("stdout"), System.out);
            bindDynamicObject(TuTerm.createAtomTerm("stderr"), System.err);
            bindDynamicObject(TuTerm.createAtomTerm("runtime"), Runtime.getRuntime());
            bindDynamicObject(TuTerm.createAtomTerm("current_thread"), Thread
                    .currentThread());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

     /**
     * Deprecated from tuProlog 3.0 use new_object
     */
    public boolean java_object_3(Term className, Term argl, Term id) throws TuJavaException {
    	return new_object_3(className, argl,id);
    }
    
    /**
     * Creates of a java object - not backtrackable case
     * @param className
     * @param argl
     * @param id
     * @return
     * @throws TuJavaException
     */
    public boolean new_object_3(Term className, Term argl, Term id) throws TuJavaException {
        className = className.getTerm();
        TuStruct arg = (TuStruct) argl.getTerm();
        id = id.getTerm();
        try {
            if (!className.isAtomSymbol()) {
                throw new TuJavaException(new ClassNotFoundException(
                        "Java class not found: " + className));
            }
            String clName = ((TuStruct) className).getName();
            // check for array type
            if (clName.endsWith("[]")) {
                Object[] list = getArrayFromList(arg);
                int nargs = ((TuNumber) list[0]).intValue();
                if (java_array(clName, nargs, id))
                    return true;
                else
                    throw new TuJavaException(new Exception());
            }
            Signature args = parseArg(getArrayFromList(arg));
            if (args == null) {
                throw new IllegalArgumentException(
                        "Illegal constructor arguments  " + arg);
            }
            // object creation with argument described in args
            try {
            	Class<?> cl = Class.forName(clName, true, dynamicLoader);
                Object[] args_value = args.getValues();
                Constructor<?> co = lookupConstructor(cl, args.getTypes(),args_value);
                if (co == null) {
                    getEngine().warn("Constructor not found: class " + clName);
                    throw new TuJavaException(new NoSuchMethodException(
                            "Constructor not found: class " + clName));
                }

                Object obj = co.newInstance(args_value);
                if (bindDynamicObject(id, obj))
                    return true;
                else
                    throw new TuJavaException(new Exception());
            } catch (ClassNotFoundException ex) {
                getEngine().warn("Java class not found: " + clName);
                throw new TuJavaException(ex);
            } catch (InvocationTargetException ex) {
                getEngine().warn("Invalid constructor arguments.");
                throw new TuJavaException(ex);
            } catch (NoSuchMethodException ex) {
                getEngine().warn("Constructor not found: " + args.getTypes());
                throw new TuJavaException(ex);
            } catch (InstantiationException ex) {
                getEngine().warn(
                        "Objects of class " + clName
                                + " cannot be instantiated");
                throw new TuJavaException(ex);
            } catch (IllegalArgumentException ex) {
                getEngine().warn("Illegal constructor arguments  " + args);
                throw new TuJavaException(ex);
            }
        } catch (Exception ex) {
            throw new TuJavaException(ex);
        }
    }
    
    /**
     * @author Roberta Calegari revisione 4 ott 2018 post passaggio java 9
     * 
     * Creates of a lambda object - not backtrackable case
     * @param interfaceName represent the name of the target interface i.e. 'java.util.function.Predicate<String>'
     * @param implementation contains the function implementation i.e. 's -> s.length()>4 '
     * @param id represent the identification_name of the created object function i.e. MyLambda
     * 
     * @throws TuJavaException, Exception
     */
	@SuppressWarnings("unchecked") 
	public <T> boolean new_lambda_3(Term interfaceName, Term implementation, Term id)throws TuJavaException, Exception {
		if(lambdaPlugin != null){
			String mode = lambdaPlugin.mode();
			if(mode.equalsIgnoreCase("active")){
				try {
		    		counter++;
		    		String target_class=(interfaceName.toString()).substring(1, interfaceName.toString().length()-1);
		    		String lambda_expression=(implementation.toString()).substring(1, implementation.toString().length()-1);
		    		target_class = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(target_class);
		    		lambda_expression = org.apache.commons.lang3.StringEscapeUtils.unescapeJava(lambda_expression);
		    	
		    		String className ="MyLambdaFactory"+counter;
		    		String lambdaClassCode =            
				            	"public class "+className+" {\n" +
				            "  public "+target_class+" getFunction() {\n" + 
						    " 		return "+lambda_expression+"; \n"+ 
						    "  }\n" +
				            "}\n";
		    		Path lambdaPath = saveSource(lambdaClassCode, className);
		    			    		
		    		Path lambdaPathCompiled = compileSource(lambdaPath, className);
		    		 
		    		Object myLambdaFactory = createClassInstance(lambdaPathCompiled, className);
		    		
		    		Class<?> myLambdaClass = myLambdaFactory.getClass(); 
		    		
		    		Method[] allMethods = myLambdaClass.getDeclaredMethods();
		    		T myLambdaInstance=null; 
		    		for (Method m : allMethods) {
		    			String mname = m.getName();
		    			if (mname.startsWith("getFunction"))
		    				myLambdaInstance=(T) m.invoke(myLambdaFactory);
		    		}
		    		System.out.println("arrivo step 6");
		    		id = id.getTerm();
		    		if (bindDynamicObject(id, myLambdaInstance))
		    			return true;
		    		else
		    			throw new TuJavaException(new Exception());
		    	} catch (Exception ex) {
		            throw new TuJavaException(ex);
		        }
			}
		}
		return false;
    }
	
	private Path saveSource(String source, String className) throws IOException {
        String tmpProperty = System.getProperty("java.io.tmpdir");
        Path sourcePath = Paths.get(tmpProperty, className+".java");
        Files.write(sourcePath, source.getBytes(StandardCharsets.UTF_8.name()));
        return sourcePath;
    }

    private Path compileSource(Path javaFile,String className) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, javaFile.toFile().getAbsolutePath());
        return javaFile.getParent().resolve(className+"class");
    }

    private Object createClassInstance(Path javaClass,String className)
            throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, SecurityException {
        URL classUrl = javaClass.getParent().toFile().toURI().toURL();
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
        Class<?> clazz = Class.forName(className, true, classLoader);
        return clazz.getDeclaredConstructors()[0].newInstance();
        //clazz.newInstance(); deprecato da java 9
    }
	
	/**
     * Destroy the link to a java object - called not directly, but from
     * predicate java_object (as second choice, for backtracking)
     * 
     * @throws TuJavaException
     */
    public boolean destroy_object_1(Term id) throws TuJavaException {
        id = id.getTerm();
        try {
            if (id.isGround()) {
                unregisterDynamic((TuStruct) id);
            }
            return true;
        } catch (Exception ex) {
            throw new TuJavaException(ex);
        }
    }

    /**
     * Deprecated from tuProlog 3.0 use new_class
     * 
     * @throws TuJavaException
     */
    public boolean java_class_4(Term clSource, Term clName, Term clPathes,Term id) throws TuJavaException {
    	return new_class_4(clSource,  clName,  clPathes, id);
    }
    
    /**
     * The java class/4 creates, compiles and loads a new Java class from a source text
     * @param clSource: is a string representing the text source of the new Java class
     * @param clName: full class name
     * @param clPathes: is a (possibly empty) Prolog list of class paths that may be required for a successful dynamic compilation of this class
     * @param id: reference to an instance of the meta-class java.lang.Class rep- resenting the newly-created class
     * @return boolean: true if created false otherwise
     * @throws TuJavaException
     */
	public boolean new_class_4(Term clSource, Term clName, Term clPathes,Term id) throws TuJavaException {
		TuStruct classSource = (TuStruct) clSource.getTerm();
		TuStruct className = (TuStruct) clName.getTerm();
		TuStruct classPathes = (TuStruct) clPathes.getTerm();
		id = id.getTerm();
		try {
            String fullClassName = alice.util.Tools.removeApices(className.toString());

            String fullClassPath = fullClassName.replace('.', '/');
            Iterator<? extends Term> it = classPathes.listIterator();
            String cp = "";
            while (it.hasNext()) {
                if (cp.length() > 0) {
                    cp += ";";
                }
                cp += alice.util.Tools.removeApices(((TuStruct) it.next())
                        .toString());
            }
            if (cp.length() > 0) {
                cp = " -classpath " + cp;
            }

            String text = alice.util.Tools.removeApices(classSource.toString());
            try {
                FileWriter file = new FileWriter(fullClassPath + ".java");
                file.write(text);
                file.close();
            } catch (IOException ex) {
                getEngine().warn("Compilation of java sources failed");
                getEngine().warn(
                        "(creation of " + fullClassPath + ".java fail failed)");
                throw new TuJavaException(ex);
            }
            String cmd = "javac " + cp + " " + fullClassPath + ".java";

            try {
                Process jc = Runtime.getRuntime().exec(cmd);
                int res = jc.waitFor();
                if (res != 0) {
                    getEngine().warn("Compilation of java sources failed");
                    getEngine().warn(
                            "(java compiler (javac) has stopped with errors)");
                    throw new IOException("Compilation of java sources failed");
                }
            } catch (IOException ex) {
                getEngine().warn("Compilation of java sources failed");
                getEngine().warn("(java compiler (javac) invocation failed)");
                throw new TuJavaException(ex);
            }
            try 
            {
            	Class<?> the_class;
            	
            	/**
            	 * @author Alessio Mercurio
            	 * 
            	 * On Dalvik VM we can only use the DexClassLoader.
            	 */
            	
            	if (System.getProperty("java.vm.name").equals("Dalvik"))
        		{
            		the_class = Class.forName(fullClassName, true, dynamicLoader);
        		}
            	else
            	{
            		the_class = Class.forName(fullClassName, true, new ClassLoader());
            	}
                
                if (bindDynamicObject(id, the_class))
                    return true;
                else
                    throw new TuJavaException(new Exception());
            } catch (ClassNotFoundException ex) {
                getEngine().warn("Compilation of java sources failed");
                getEngine().warn(
                        "(Java Class compiled, but not created: "
                                + fullClassName + " )");
                throw new TuJavaException(ex);
            }
        } catch (Exception ex) {
            throw new TuJavaException(ex);
        }
    }

    /**
	 * 
	 * Calls a method of a Java object
	 * 
	 * @throws TuJavaException
	 * 
	 */
	public boolean java_call_3(Term objId, Term method_name, Term idResult)
			throws TuJavaException {
		objId = objId.getTerm();
		idResult = idResult.getTerm();
		TuStruct method = (TuStruct) method_name.getTerm();
		Object obj = null;
		Signature args = null;
		String methodName = null;
		try {
			methodName = method.getName();
			if (!objId.isAtomSymbol()) {
				if (objId .isVar()) {
					throw new TuJavaException(new IllegalArgumentException(objId
							.toString()));
				}
				TuStruct sel = (TuStruct) objId;
				if (sel.getName().equals(".") && sel.getArity() == 2
						&& method.getArity() == 1) {
					if (methodName.equals("set")) {
						return java_set(sel.getTerm(0), sel.getTerm(1), method
								.getTerm(0));
					} else if (methodName.equals("get")) {
						return java_get(sel.getTerm(0), sel.getTerm(1), method
								.getTerm(0));
					}
				}
			}
			args = parseArg(method);
			// object and argument must be instantiated
			if (objId .isVar())
				throw new TuJavaException(new IllegalArgumentException(objId
						.toString()));
			if (args == null) {
				throw new TuJavaException(new IllegalArgumentException());
			}
			String objName = alice.util.Tools.removeApices(objId.toString());
			obj = staticObjects.containsKey(objName) ? staticObjects.get(objName) : currentObjects.get(objName);
			Object res = null;

			if (obj != null) {
				Class<?> cl = obj.getClass();
				Object[] args_values = args.getValues();
				Method m = lookupMethod(cl, methodName, args.getTypes(),args_values);
				if (m != null) {
					try {
						m.setAccessible(true);
						res = m.invoke(obj, args_values);
					} catch (IllegalAccessException ex) {
						getEngine().warn("Method invocation failed: " + methodName+ "( signature: " + args + " )");
						throw new TuJavaException(ex);
					}
				} else {
					getEngine().warn("Method not found: " + methodName + "( signature: "+ args + " )");
					throw new TuJavaException(new NoSuchMethodException("Method not found: " + methodName + "( signature: "+ args + " )"));
				}
			} else {
				if (objId.isCompound()) {
					TuStruct id = (TuStruct) objId;

					if (id.getArity() == 1 && id.getName().equals("class")) {
						try {
							String clName = alice.util.Tools
									.removeApices(id.getArg(0).toString());
							Class<?> cl = Class.forName(clName, true, dynamicLoader);
							
							Method m = InspectionUtils.searchForMethod(cl, methodName, args.getTypes());
							m.setAccessible(true);
							res = m.invoke(null, args.getValues());
						} catch (ClassNotFoundException ex) {
							// if not found even as a class id -> consider as a
							// String object value
							getEngine().warn("Unknown class.");
							throw new TuJavaException(ex);
						}
					}
					else {
						// the object is the string itself
						Method m = java.lang.String.class.getMethod(methodName, args.getTypes());
						m.setAccessible(true);
						res = m.invoke(objName, args.getValues());
					}
				} else {
					// the object is the string itself
					Method m = java.lang.String.class.getMethod(methodName,
							args.getTypes());
					m.setAccessible(true);
					res = m.invoke(objName, args.getValues());
				}
			}
			if (parseResult(idResult, res))
				return true;
			else
				throw new TuJavaException(new Exception());
		} catch (InvocationTargetException ex) {
			getEngine().warn(
					"Method failed: " + methodName + " - ( signature: " + args
					+ " ) - Original Exception: "
					+ ex.getTargetException());
			throw new TuJavaException(new IllegalArgumentException());
		} catch (NoSuchMethodException ex) {
			getEngine().warn(
					"Method not found: " + methodName + " - ( signature: "
							+ args + " )");
			throw new TuJavaException(ex);
		} catch (IllegalArgumentException ex) {
			getEngine().warn(
					"Invalid arguments " + args + " - ( method: " + methodName
					+ " )");
			throw new TuJavaException(ex);
		} catch (Exception ex) {
			getEngine()
			.warn("Generic error in method invocation " + methodName);
			throw new TuJavaException(ex);
		}
	}
	
    /**
     * @author Michele Mannino
     * 
     * Set global classpath
     * 
     * @throws TuJavaException
     * 
     */
    public boolean set_classpath_1(Term paths) throws TuJavaException
    {
    	try {
    		paths = paths.getTerm();
        	if(!paths.isList())
        		throw new IllegalArgumentException();
        	String[] listOfPaths = getStringArrayFromStruct((TuStruct) paths);
        	dynamicLoader.removeAllURLs();
        	dynamicLoader.addURLs(getURLsFromStringArray(listOfPaths));
        	return true;
    	}catch(IllegalArgumentException e)
        {
        	getEngine().warn("Illegal list of paths " + paths);
            throw new TuJavaException(e);
        }
        catch (Exception e) {
        	throw new TuJavaException(e);
		}
    }
    
    /**
     * @author Michele Mannino
     * 
     * Get global classpath
     * 
     * @throws TuJavaException
     * 
     */
    
	public boolean get_classpath_1(Term paths) throws TuJavaException
    {
    	try {
    		paths = paths.getTerm();
    		if(!(paths .isVar()))
    			throw new IllegalArgumentException();
    		URL[] urls = dynamicLoader.getURLs();
        	String stringURLs = null;
        	Term pathTerm = null;
        	if(urls.length > 0)
        	{
	        	stringURLs = "[";
	     
	        	for (URL url : urls) {
	        		File file = new File(java.net.URLDecoder.decode(url.getFile(), "UTF-8"));
	        		stringURLs = stringURLs + "'" + file.getPath() + "',";
				}
	        	
	        	stringURLs = stringURLs.substring(0, stringURLs.length() - 1);
	        	stringURLs = stringURLs + "]";
        	}
        	else
        		stringURLs = "[]";
        	pathTerm = Term.createTerm(stringURLs);
        	return unify(paths, pathTerm);
    	}catch(IllegalArgumentException e)
        {
        	getEngine().warn("Illegal list of paths " + paths);
            throw new TuJavaException(e);
        }
        catch (Exception e) {
        	throw new TuJavaException(e);
		}
    }
	
    /**
     * set the field value of an object
     */
    private boolean java_set(Term objId, Term fieldTerm, Term what) {
        what = what.getTerm();
        if (!fieldTerm.isAtomSymbol() || what .isVar())
            return false;
        String fieldName = ((TuStruct) fieldTerm).getName();
        Object obj = null;
        try {
            Class<?> cl = null;
            if(objId.isCompound() && ((TuStruct) objId).getName().equals("class"))
            {
            	String clName = null;
            	// Case: class(className)
            	if(((TuStruct) objId).getArity() == 1)         	
            		 clName = alice.util.Tools.removeApices(((TuStruct) objId).getArg(0).toString());
            	if(clName != null)
            	{
            		try {
                        cl = Class.forName(clName, true, dynamicLoader);
                    } catch (ClassNotFoundException ex) {
                        getEngine().warn("Java class not found: " + clName);
                        return false;
                    } catch (Exception ex) {
                        getEngine().warn(
                                "Static field "
                                        + fieldName
                                        + " not found in class "
                                        + alice.util.Tools
                                                .removeApices(((TuStruct) objId)
                                                        .getArg(0).toString()));
                        return false;
                    }
            	}
            }
            else {
                String objName = alice.util.Tools
                        .removeApices(objId.toString());
                obj = currentObjects.get(objName);
                if (obj != null) {
                    cl = obj.getClass();
                } else {
                    return false;
                }
            }

            // first check for primitive data field
            Field field = cl.getField(fieldName);
            if (what .isNumber()) {
                TuNumber wn = (TuNumber) what;
                if (wn .isInt()) {
                    field.setInt(obj, wn.intValue());
                } else if (wn .isDouble()) {
                    field.setDouble(obj, wn.doubleValue());
                } else if (wn .isLong()) {
                    field.setLong(obj, wn.longValue());
                } else if (wn .isFloat()) {
                    field.setFloat(obj, wn.floatValue());
                } else {
                    return false;
                }
            } else {
                String what_name = alice.util.Tools.removeApices(what
                        .toString());
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
            getEngine().warn(
                    "Field " + fieldName + " not found in class " + objId);
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * get the value of the field
     */
    private boolean java_get(Term objId, Term fieldTerm, Term what) {
        if (!fieldTerm.isAtomSymbol()) {
            return false;
        }
        String fieldName = ((TuStruct) fieldTerm).getName();
        Object obj = null;
        try {
            Class<?> cl = null;
            if(objId.isCompound() && ((TuStruct) objId).getName().equals("class"))
            {
            	String clName = null;
            	if(((TuStruct) objId).getArity() == 1)         	
            		 clName = alice.util.Tools.removeApices(((TuStruct) objId).getArg(0).toString());
            	if(clName != null)
            	{
            		try {
                        cl = Class.forName(clName, true, dynamicLoader);
                    } catch (ClassNotFoundException ex) {
                        getEngine().warn("Java class not found: " + clName);
                        return false;
                    } catch (Exception ex) {
                        getEngine().warn(
                                "Static field "
                                        + fieldName
                                        + " not found in class "
                                        + alice.util.Tools
                                                .removeApices(((TuStruct) objId)
                                                        .getArg(0).toString()));
                        return false;
                    }
            	}
            }
            else {
                String objName = alice.util.Tools.removeApices(objId.toString());
                obj = currentObjects.get(objName);
                if (obj == null) {
                    return false;
                }
                cl = obj.getClass();
            }

            Field field = cl.getField(fieldName);
            Class<?> fc = field.getType();
            field.setAccessible(true);
            if (fc.equals(Integer.TYPE) || fc.equals(Byte.TYPE)) {
                int value = field.getInt(obj);
                return unify(what, TuTerm.i32(value));
            } else if (fc.equals(java.lang.Long.TYPE)) {
                long value = field.getLong(obj);
                return unify(what, TuTerm.i64(value));
            } else if (fc.equals(java.lang.Float.TYPE)) {
                float value = field.getFloat(obj);
                return unify(what, TuTerm.f32(value));
            } else if (fc.equals(java.lang.Double.TYPE)) {
                double value = field.getDouble(obj);
                return unify(what, TuTerm.f64(value));
            } else {
                // the field value is an object
                Object res = field.get(obj);
                return bindDynamicObject(what, res);
            }
            
        } catch (NoSuchFieldException ex) {
            getEngine().warn(
                    "Field " + fieldName + " not found in class " + objId);
            return false;
        } catch (Exception ex) {
            getEngine().warn("Generic error in accessing the field");
            return false;
        }
    }
    
    public boolean java_array_set_primitive_3(Term obj_id, Term i, Term what)
            throws TuJavaException {
        TuStruct objId = (TuStruct) obj_id.getTerm();
        TuNumber index = (TuNumber) i.getTerm();
        what = what.getTerm();
        Object obj = null;
        if (!index.isInteger()) {
            throw new TuJavaException(new IllegalArgumentException(index
                    .toString()));
        }
        try {
            Class<?> cl = null;
            String objName = alice.util.Tools.removeApices(objId.toString());
            obj = currentObjects.get(objName);
            if (obj != null) {
                cl = obj.getClass();
            } else {
                throw new TuJavaException(new IllegalArgumentException(objId
                        .toString()));
            }

            if (!cl.isArray()) {
                throw new TuJavaException(new IllegalArgumentException(objId
                        .toString()));
            }
            String name = cl.toString();
            if (name.equals("class [I")) {
                if (!(what .isNumber())) {
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
                }
                byte v = (byte) ((TuNumber) what).intValue();
                Array.setInt(obj, index.intValue(), v);
            } else if (name.equals("class [D")) {
                if (!(what .isNumber())) {
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
                }
                double v = ((TuNumber) what).doubleValue();
                Array.setDouble(obj, index.intValue(), v);
            } else if (name.equals("class [F")) {
                if (!(what .isNumber())) {
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
                }
                float v = ((TuNumber) what).floatValue();
                Array.setFloat(obj, index.intValue(), v);
            } else if (name.equals("class [L")) {
                if (!(what .isNumber())) {
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
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
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
                }
            } else if (name.equals("class [B")) {
                if (!(what .isNumber())) {
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
                }
                int v = ((TuNumber) what).intValue();
                Array.setByte(obj, index.intValue(), (byte) v);
            } else if (name.equals("class [S")) {
                if (!(what .isNumber())) {
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
                }
                short v = (short) ((TuNumber) what).intValue();
                Array.setShort(obj, index.intValue(), v);
            } else {
                throw new TuJavaException(new Exception());
            }
            return true;
        } catch (Exception ex) {
            throw new TuJavaException(ex);
        }
    }
    
    
    /**
     * Sets the value of the field 'i' with 'what'
     * @param obj_id
     * @param i
     * @param what
     * @return
     * @throws TuJavaException
     */
    public boolean java_array_get_primitive_3(Term obj_id, Term i, Term what) throws TuJavaException {
        TuStruct objId = (TuStruct) obj_id.getTerm();
        TuNumber index = (TuNumber) i.getTerm();
        what = what.getTerm();
        Object obj = null;
        if (!index.isInteger()) {
            throw new TuJavaException(new IllegalArgumentException(index.toString()));
        }
        try {
            Class<?> cl = null;
            String objName = alice.util.Tools.removeApices(objId.toString());
            obj = currentObjects.get(objName);
            if (obj != null) {
                cl = obj.getClass();
            } else {
                throw new TuJavaException(new IllegalArgumentException(objId.toString()));
            }

            if (!cl.isArray()) {
                throw new TuJavaException(new IllegalArgumentException(objId.toString()));
            }
            String name = cl.toString();
            if (name.equals("class [I")) {
                Term value = TuTerm.i32(Array.getInt(obj, index.intValue()));
                if (unify(what, value))
                    return true;
                else
                    throw new TuJavaException(new IllegalArgumentException(what.toString()));
            } else if (name.equals("class [D")) {
                Term value = TuTerm.f64(Array.getDouble(obj,index.intValue()));
                if (unify(what, value))
                    return true;
                else
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
            } else if (name.equals("class [F")) {
                Term value = TuTerm.f32(Array.getFloat(obj, index
                        .intValue()));
                if (unify(what, value))
                    return true;
                else
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
            } else if (name.equals("class [L")) {
                Term value = TuTerm.i64(Array.getLong(obj, index
                        .intValue()));
                if (unify(what, value))
                    return true;
                else
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
            } else if (name.equals("class [C")) {
                Term value = TuTerm.createAtomTerm(""
                        + Array.getChar(obj, index.intValue()));
                if (unify(what, value))
                    return true;
                else
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
            } else if (name.equals("class [Z")) {
                boolean b = Array.getBoolean(obj, index.intValue());
                if (b) {
                    if (unify(what, alice.tuprolog.Term.TRUE))
                        return true;
                    else
                        throw new TuJavaException(new IllegalArgumentException(
                                what.toString()));
                } else {
                    if (unify(what, alice.tuprolog.Term.FALSE))
                        return true;
                    else
                        throw new TuJavaException(new IllegalArgumentException(
                                what.toString()));
                }
            } else if (name.equals("class [B")) {
                Term value = TuTerm.i32(Array.getByte(obj, index
                        .intValue()));
                if (unify(what, value))
                    return true;
                else
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
            } else if (name.equals("class [S")) {
                Term value = TuTerm.i32(Array.getInt(obj, index
                        .intValue()));
                if (unify(what, value))
                    return true;
                else
                    throw new TuJavaException(new IllegalArgumentException(what
                            .toString()));
            } else {
                throw new TuJavaException(new Exception());
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            throw new TuJavaException(ex);
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
                Class<?> cl = Class.forName(obtype, true, dynamicLoader);
                array = Array.newInstance(cl, nargs);
            }
            return bindDynamicObject(id, array);
        } catch (Exception ex) {
            // ex.printStackTrace();
            return false;
        }
    }

    /**
     * Returns an URL array from a String array
     *
     * @throws TuJavaException
     */
    private URL[] getURLsFromStringArray(String[] paths) throws MalformedURLException  
    {
    	URL[] urls = null;
    	if(paths != null)
    	{
	    	urls = new URL[paths.length];
			
			for (int i = 0; i < paths.length; i++) 
			{
				if(paths[i] == null)
					continue;
				if(paths[i].contains("http") || paths[i].contains("https") || paths[i].contains("ftp"))
					urls[i] = new URL(paths[i]);
				else{
					File file = new File(paths[i]);
					urls[i] = (file.toURI().toURL());
				}
			}
    	}
		return urls;
    }
    
    /**
     * Returns a String array from a Struct contains a list
     *
     * @throws TuJavaException
     */
    
    private String[] getStringArrayFromStruct(TuStruct list) {
        String args[] = new String[list.listSize()];
        Iterator<? extends Term> it = list.listIterator();
        int count = 0;
        while (it.hasNext()) {
        	String path = alice.util.Tools.removeApices(it.next().toString());
            args[count++] = path;
        }
        return args;
    }
    
    
    /**
     * creation of method signature from prolog data
     */
    private Signature parseArg(TuStruct method) {
        Object[] values = new Object[method.getArity()];
        Class<?>[] types = new Class[method.getArity()];
        for (int i = 0; i < method.getArity(); i++) {
            if (!parse_arg(values, types, i, method.getTerm(i)))
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
                } else if (t .isDouble()) {
                    values[i] = new java.lang.Double(t.doubleValue());
                    types[i] = java.lang.Double.TYPE;
                } else if (t .isLong()) {
                    values[i] = new java.lang.Long(t.longValue());
                    types[i] = java.lang.Long.TYPE;
                } else if (t .isFloat()) {
                    values[i] = new java.lang.Float(t.floatValue());
                    types[i] = java.lang.Float.TYPE;
                }
            } else if (term .isCallable()) {
                // argument descriptors
                TuStruct tc = (TuStruct) term;
                if (tc.getName().equals("as")) {
                    return parse_as(values, types, i, tc.getTerm(0), tc
                            .getTerm(1));
                } else {
                    Object obj = currentObjects.get(alice.util.Tools
                            .removeApices(tc.toString()));
                    if (obj == null) {
                        values[i] = alice.util.Tools
                                .removeApices(tc.toString());
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
     * parsing 'as' operator, which makes it possible to define the specific
     * class of an argument
     * 
     */
    private boolean parse_as(Object[] values, Class<?>[] types, int i,
            Term castWhat, Term castTo) {
        try {
            if (!(castWhat .isNumber())) {
                String castTo_name = alice.util.Tools
                        .removeApices(((TuStruct) castTo).getName());
                String castWhat_name = alice.util.Tools.removeApices(castWhat
                        .getTerm().toString());
                // System.out.println(castWhat_name+" "+castTo_name);
                if (castTo_name.equals("java.lang.String")
                        && castWhat_name.equals("true")) {
                    values[i] = "true";
                    types[i] = String.class;
                    return true;
                } else if (castTo_name.equals("java.lang.String")
                        && castWhat_name.equals("false")) {
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
                        castTo_name = "[L"
                                + castTo_name.substring(0,
                                        castTo_name.length() - 2) + ";";
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
                            types[i] = Class.forName(castTo_name, true, dynamicLoader);
                        } catch (ClassNotFoundException ex) {
                            getEngine().warn(
                                    "Java class not found: " + castTo_name);
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
                            types[i] = Class.forName(castTo_name, true, dynamicLoader);
                        } catch (ClassNotFoundException ex) {
                            getEngine().warn(
                                    "Java class not found: " + castTo_name);
                            return false;
                        }
                    }
                }
            } else {
                TuNumber num = (TuNumber) castWhat;
                String castTo_name = ((TuStruct) castTo).getName();
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
            getEngine().warn(
                    "Casting " + castWhat + " to " + castTo + " failed");
            return false;
        }
        return true;
    }

    /**
     * parses return value of a method invokation
     */
    private boolean parseResult(Term id, Object obj) {
        if (obj == null) {
            // return unify(id,Term.TRUE);
            return unify(id, TuTerm.createTuVar());
        }
        try {
            if (Boolean.class.isInstance(obj)) {
                if (((Boolean) obj).booleanValue()) {
                    return unify(id, Term.TRUE);
                } else {
                    return unify(id, Term.FALSE);
                }
            } else if (Byte.class.isInstance(obj)) {
                return unify(id, TuTerm.i32(((Byte) obj).intValue()));
            } else if (Short.class.isInstance(obj)) {
                return unify(id, TuTerm.i32(((Short) obj).intValue()));
            } else if (Integer.class.isInstance(obj)) {
                return unify(id, TuTerm.i32(((Integer) obj).intValue()));
            } else if (java.lang.Long.class.isInstance(obj)) {
                return unify(id, TuTerm.i64(((java.lang.Long) obj)
                        .longValue()));
            } else if (java.lang.Float.class.isInstance(obj)) {
                return unify(id, TuTerm.f32(((java.lang.Float) obj).floatValue()));
            } else if (java.lang.Double.class.isInstance(obj)) {
                return unify(id, TuTerm.f64(((java.lang.Double) obj).doubleValue()));
            } else if (String.class.isInstance(obj)) {
                return unify(id, TuTerm.createAtomTerm((String) obj));
            } else if (Character.class.isInstance(obj)) {
                return unify(id, TuTerm.createAtomTerm(((Character) obj).toString()));
            } else {
                return bindDynamicObject(id, obj);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            return false;
        }
    }

    private Object[] getArrayFromList(TuStruct list) {
        Object args[] = new Object[list.listSize()];
        Iterator<? extends Term> it = list.listIterator();
        int count = 0;
        while (it.hasNext()) {
            args[count++] = it.next();
        }
        return args;
    }

    /**
     * Register an object with the specified id. The life-time of the link to
     * the object is engine life-time, available besides the individual query.
     * 
     * 
     * @param id
     *            object identifier
     * @param obj
     *            the object
     * @return true if the operation is successful
     * @throws InvalidObjectIdException
     *             if the object id is not valid
     */
    public boolean register(TuStruct id, Object obj)
            throws InvalidObjectIdException {
        /*
         * note that this method act on the staticObject and
         * staticObject_inverse hashmaps
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
                String raw_name = alice.util.Tools.removeApices(id.getTerm()
                        .toString());
                staticObjects.put(raw_name, obj);
                staticObjects_inverse.put(obj, id);
                return true;
            }
        }
    }
    
    /**
     * Register an object with the specified id. The life-time of the link to
     * the object is engine life-time, available besides the individual query.
     * 
     * The identifier must be a ground object.
     * 
     * @param id
     *            object identifier
     *            
     * @return true if the operation is successful
     * @throws TuJavaException
     *             if the object id is not valid
     */
    public boolean register_1(Term id) throws TuJavaException
    {
    	id = id.getTerm();
    	Object obj =  null; 
    	try
        {
        	obj = getRegisteredDynamicObject((TuStruct) id);
        	return register((TuStruct)id, obj);
        }catch(InvalidObjectIdException e)
        {
        	getEngine().warn("Illegal object id " + id.toString());
            throw new TuJavaException(e);
        }
    }
    
    /**
     * Unregister an object with the specified id.
     * 
     * The identifier must be a ground object.
     * 
     * @param id
     *            object identifier
     *            
     * @return true if the operation is successful
     * @throws TuJavaException
     *             if the object id is not valid
     */
    public boolean unregister_1(Term id) throws TuJavaException
    {
    	id = id.getTerm(); 
    	try
        {
        	return unregister((TuStruct)id);
        }catch(InvalidObjectIdException e)
        {
        	getEngine().warn("Illegal object id " + id.toString());
            throw new TuJavaException(e);
        }
    }
    
    /**
     * Registers an object, with automatic creation of the identifier.
     * 
     * If the object is already registered, its identifier is returned
     * 
     * @param obj
     *            object to be registered.
     * @return fresh id
     */
    public TuStruct register(Object obj) {
    	// already registered object?
        synchronized (staticObjects) {
            Object aKey = staticObjects_inverse.get(obj);
            if (aKey != null) {
                // object already referenced -> unifying terms
                // referencing the object
                // log("obj already registered: unify "+id+" "+aKey);
                return (TuStruct) aKey;
            } else {
                TuStruct id = generateFreshId();
                staticObjects.put(id.getName(), obj);
                staticObjects_inverse.put(obj, id);
                return id;
            }
        }
    }

    /**
     * Gets the reference to an object previously registered
     * 
     * @param id
     *            object id
     * @return the object, if present
     * @throws InvalidObjectIdException
     */
    public Object getRegisteredObject(TuStruct id)
            throws InvalidObjectIdException {
        if (!id.isGround()) {
            throw new InvalidObjectIdException();
        }
        synchronized (staticObjects) {
            return staticObjects.get(alice.util.Tools.removeApices(id
                    .toString()));
        }
    }

    /**
     * Unregisters an object, given its identifier
     * 
     * 
     * @param id
     *            object identifier
     * @return true if the operation is successful
     * @throws InvalidObjectIdException
     *             if the id is not valid (e.g. is not ground)
     */
    public boolean unregister(TuStruct id) throws InvalidObjectIdException {
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
     * @param id
     *            object identifier
     * @param obj
     *            object
     */
    public void registerDynamic(TuStruct id, Object obj) {
        synchronized (currentObjects) {
            String raw_name = alice.util.Tools.removeApices(id.toString());
            currentObjects.put(raw_name, obj);
            currentObjects_inverse.put(obj, id);
        }
    }

    /**
     * Registers an object for the query life-time, with the automatic
     * generation of the identifier.
     * 
     * If the object is already registered, its identifier is returned
     * 
     * @param obj
     *            object to be registered
     * @return identifier
     */
    public TuStruct registerDynamic(Object obj) {
        // System.out.println("lib: "+this+" current id: "+this.id);

        // already registered object?
        synchronized (currentObjects) {
            Object aKey = currentObjects_inverse.get(obj);
            if (aKey != null) {
                // object already referenced -> unifying terms
                // referencing the object
                // log("obj already registered: unify "+id+" "+aKey);
                return (TuStruct) aKey;
            } else {
                TuStruct id = generateFreshId();
                currentObjects.put(id.getName(), obj);
                currentObjects_inverse.put(obj, id);
                return id;
            }
        }
    }

    /**
     * Gets a registered dynamic object (returns null if not presents)
     */
    public Object getRegisteredDynamicObject(TuStruct id)
            throws InvalidObjectIdException {
        if (!id.isGround()) {
            throw new InvalidObjectIdException();
        }
        synchronized (currentObjects) {
            return currentObjects.get(alice.util.Tools.removeApices(id
                    .toString()));
        }
    }

    /**
     * Unregister the object, only for dynamic case
     * 
     * @param id
     *            object identifier
     * @return true if the operation is successful
     */
    public boolean unregisterDynamic(TuStruct id) {
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
            return unify(id, TuTerm.createTuVar());
        }
        // already registered object?
        synchronized (currentObjects) {
            Object aKey = currentObjects_inverse.get(obj);
            if (aKey != null) {
                // object already referenced -> unifying terms
                // referencing the object
                // log("obj already registered: unify "+id+" "+aKey);
                return unify(id, (Term) aKey);
            } else {
                // object not previously referenced
                if (id .isVar()) {
                    // get a ground term
                    TuStruct idTerm = generateFreshId();
                    unify(id, idTerm);
                    registerDynamic(idTerm, obj);
                    // log("not ground id for a new obj: "+id+" as ref for "+obj);
                    return true;
                } else {
                    // verify of the id is already used
                    String raw_name = alice.util.Tools.removeApices(id
                            .getTerm().toString());
                    Object linkedobj = currentObjects.get(raw_name);
                    if (linkedobj == null) {
                        registerDynamic((TuStruct) (id.getTerm()), obj);
                        // log("ground id for a new obj: "+id+" as ref for "+obj);
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
     * 
     * @return
     */
    protected TuStruct generateFreshId() {
        return TuTerm.createAtomTerm("$obj_" + id++);
    }

    /**
     * handling writeObject method is necessary in order to make the library
     * serializable, 'nullyfing' eventually objects registered in maps
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        HashMap<String,Object> bak00 = currentObjects;
        IdentityHashMap<Object,TuStruct> bak01 = currentObjects_inverse;
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
     * handling readObject method is necessary in order to have the library
     * reconstructed after a serialization
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        currentObjects = new HashMap<String, Object>();
        currentObjects_inverse = new IdentityHashMap<Object, TuStruct>();
        preregisterObjects();
    }

    // --------------------------------------------------

    private static Method lookupMethod(Class<?> target, String name,
            Class<?>[] argClasses, Object[] argValues)
            throws NoSuchMethodException {
        // first try for exact match
        try {
            Method m = target.getMethod(name, argClasses);
            return m;
        } catch (NoSuchMethodException e) {
            if (argClasses.length == 0) { // if no args & no exact match, out of
                // luck
                return null;
            }
        }

        // go the more complicated route
        Method[] methods = target.getMethods();
        Vector<Method> goodMethods = new Vector<Method>();
        for (int i = 0; i != methods.length; i++) {
            if (name.equals(methods[i].getName())
                    && matchClasses(methods[i].getParameterTypes(), argClasses))
                goodMethods.addElement(methods[i]);
        }
        switch (goodMethods.size()) {
        case 0:
            // no methods have been found checking for assignability
            // and (int -> long) conversion. One last chance:
            // looking for compatible methods considering also
            // type conversions:
            // double --> float
            // (the first found is used - no most specific
            // method algorithm is applied )

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

    private static Constructor<?> lookupConstructor(Class<?> target,
            Class<?>[] argClasses, Object[] argValues)
            throws NoSuchMethodException {
        // first try for exact match
        try {
            return target.getConstructor(argClasses);
        } catch (NoSuchMethodException e) {
            if (argClasses.length == 0) { // if no args & no exact match, out of
                // luck
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
            // double --> float
            // (the first found is used - no most specific
            // method algorithm is applied )

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
            if (mclass.equals(java.lang.Long.TYPE)
                    && (pclass.equals(java.lang.Integer.TYPE))) {
                return true;
            }
        }
        return false;
    }

    private static Method mostSpecificMethod(Vector<Method> methods)
            throws NoSuchMethodException {
        for (int i = 0; i != methods.size(); i++) {
            for (int j = 0; j != methods.size(); j++) {
                if ((i != j)
                        && (moreSpecific(methods.elementAt(i),
                                methods.elementAt(j)))) {
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
                if ((i != j)
                        && (moreSpecific(constructors.elementAt(i)
                                , constructors.elementAt(j)))) {
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
    // (required X, provided a DEFAULT -
    // with DEFAULT to X conversion 'conceivable':
    // for instance *double* to *int* is NOT considered good
    //
    // required a float, provided an int OK
    // required a double, provided a int OK
    // required a long, provided a int ==> already considered by
    // previous match test
    // required a float, provided a double OK
    // required a int, provided a double => NOT CONSIDERED
    // required a long, provided a double => NOT CONSIDERED
    //
    private static Object[] matchClasses(Class<?>[] mclasses, Class<?>[] pclasses,
            Object[] values) {
        if (mclasses.length == pclasses.length) {
            Object[] newvalues = new Object[mclasses.length];

            for (int i = 0; i != mclasses.length; i++) {
                boolean assignable = mclasses[i].isAssignableFrom(pclasses[i]);
                if (assignable
                        || (mclasses[i].equals(java.lang.Long.TYPE) && pclasses[i]
                                .equals(java.lang.Integer.TYPE))) {
                    newvalues[i] = values[i];
                } else if (mclasses[i].equals(java.lang.Float.TYPE)
                        && pclasses[i].equals(java.lang.Double.TYPE)) {
                    // arg required: a float, arg provided: a double
                    // so we need an explicit conversion...
                    newvalues[i] = new java.lang.Float(
                            ((java.lang.Double) values[i]).floatValue());
                } else if (mclasses[i].equals(java.lang.Float.TYPE)
                        && pclasses[i].equals(java.lang.Integer.TYPE)) {
                    // arg required: a float, arg provided: an int
                    // so we need an explicit conversion...
                    newvalues[i] = new java.lang.Float(
                            ((java.lang.Integer) values[i]).intValue());
                } else if (mclasses[i].equals(java.lang.Double.TYPE)
                        && pclasses[i].equals(java.lang.Integer.TYPE)) {
                    // arg required: a double, arg provided: an int
                    // so we need an explicit conversion...
                    newvalues[i] = new java.lang.Double(
                            ((java.lang.Integer) values[i]).doubleValue());
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
 * Signature class mantains information about type and value of a method
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
            st = st + "\n  Argument " + i + " -  VALUE: " + values[i]
                    + " TYPE: " + types[i];
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
    // public String eventName;
   public String eventFullClass;

    public ListenerInfo(EventListener l, String eventClass, String n) {
        listener = l;
        // this.eventName=eventName;
        this.eventFullClass = eventClass;
        listenerInterfaceName = n;
    }
}