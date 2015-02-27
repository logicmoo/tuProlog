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

using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using alice.tuprolog;
using System.Reflection;
using System.CodeDom.Compiler;
using System.IO;
using System.Threading;
using ikvm.runtime;

namespace OOLibrary
{
    public class OOLibrary : alice.tuprolog.lib.OOLibrary
    {
        /// <summary>
        /// Conventions loaded (keys) and objects bind to them (values)
        /// The objects are represented as a string, in particular the Assembly Qualified Name
        /// </summary>
        private Dictionary<Convention, List<string>> _conventionList = new Dictionary<Convention, List<string>>();

     
        public OOLibrary () : base()
	    {
	    }

        //public override string getName()
        //{
        //    return Type.GetType("OOLibrary.OOLibrary").AssemblyQualifiedName;
        //}

        public override string[][] getSynonymMap()
        {
            return new string[][]
                {
				    new string[] {"load_convention","dload_convention","directive"}
		        };
        }

        public override string getTheory()
        {
            return    ":- op(800,xfx,'<-').\n" +
                      ":- op(850,xfx,'returns').\n" +
                      ":- op(200,xfx,'as').\n" +
                      ":- op(600,xfx,'.'). \n" +

                      "new_object(ClassName,Args,Id):-              new_object(_,ClassName,_,Args,Id).\n" +
                      "new_object(Convention,ClassName,Args,Id):-   new_object(Convention,ClassName,_,Args,Id).\n" +

                      "Obj <- What :-                       method_call(Obj,What,Res), Res \\== false.\n" +
                      "Obj <- What returns Res :-           method_call(Obj,What,Res).\n" +
                      "array_set(Array,Index,Object):-      class('java.lang.reflect.Array') <- set(Array as 'java.lang.Object',Index,Object as 'java.lang.Object'), !.\n" +
                      "array_set(Array,Index,Object):-      java_array_set_primitive(Array,Index,Object).\n" +
                      "array_get(Array,Index,Object):-      class('java.lang.reflect.Array') <- get(Array as 'java.lang.Object',Index) returns Object,!.\n" +
                      "array_get(Array,Index,Object):-      java_array_get_primitive(Array,Index,Object).\n" +
                      "array_length(Array,Length):-         class('java.lang.reflect.Array') <- getLength(Array as 'java.lang.Object') returns Length.\n"+
                      "java_catch(Goal, List, Finally) :-   call(Goal), call(Finally).\n";
        
        }

        /// <summary>
        /// Deletes all non static registered objects
        /// </summary>
        public override void dismiss()
        {
            _conventionList.Clear();
            // Clear currentObjects in the superclass
            base.dismiss();
        }

        /// <summary>
        /// Deletes all registered objects and conventions
        /// </summary>
        public override void dismissAll()
        {
            _conventionList.Clear();
            // Clear currentObjects and staticObjects in the superclass
            base.dismissAll();
        }

        public override void onSolveBegin(Term goal)
        {
            base.onSolveBegin(goal);
            _conventionList.Clear();
        }

  



        /// <summary>
        /// Load a convention by its assembly and classname
        /// </summary>
        /// <param name="assemblyName">Assembly file that contains the convention</param>
        /// <param name="conventionName">Class name of the convention</param>
        /// <param name="id">Identifier of the convention</param>
        /// <returns>True if the operation is successful</returns>
        public bool load_convention_3(Term assemblyName, Term conventionName, Term id)
        {
            Struct convName = (Struct)conventionName.getTerm();
            Struct asName = (Struct)assemblyName.getTerm();

            if (convName == null || !convName.isAtom() || asName == null || !asName.isAtom() || (!(id is Var) && !id.isAtom()))
                return false;

            try
            {
                Convention newConv = Convention.LoadConvention(asName.getName(), convName.getName());
                Convention alreadyPresent = FindConvention(newConv.Name);

                // Convention not loaded
                if (alreadyPresent == null)
                {
                    _conventionList.Add(newConv, new List<string>());

                    // Id is an atom or a variable bound to an atom
                    if (id.isAtom() || id.getTerm().isAtom())
                    {
                        // Check if the id specified is already in use
                        if (this.getRegisteredObject((Struct)id) == null)
                            return this.bindDynamicObject(id.getTerm(), newConv);
                        else
                            return false;
                    }
                    else if (id is Var)
                    {
                        // Id set to Convention.Name and unified with the variable
                        Struct newId = new Struct(newConv.Name);
                        return (this.bindDynamicObject(newId, newConv)) && (this.unify(id, newId));
                    }
                }
                else
                {
                    // Convention already loaded
                    if (id.isAtom() || id.getTerm().isAtom())
                    {
                        string key = RemoveApices(id.getTerm().ToString());
                        object conv = this.getRegisteredDynamicObject((Struct)id);
                        if (conv != null && conv is Convention)
                        {
                            Convention found = (Convention)conv;
                            // If convention found in objects and in conventions lists are equals, ok!
                            if (found.Name.Equals(alreadyPresent.Name))
                                return true;
                            else
                                return false;
                        }
                        else
                            return false;
                    }
                    else
                    {
                        // TO CHECK -----------------------------------------------------
                        // If id is var then find the atom identifier and bind to id

                        // Here the Convention is already loaded so (in theory) the following call will return the identifier
                        // without register the Convention another time, however this call could be unsafe
                        Struct oldId = this.registerDynamic(alreadyPresent);
                        return this.unify(id, oldId);

                        #region Albertin Code
                        //if (_currentObjects_inverse.Contains(alreadyPresent))
                        //    return Unify(id, (Struct)_currentObjects_inverse[alreadyPresent]);
                        //else
                        //    return false;
                        #endregion
                    }
                }

                return false;
            }
            catch (Exception ex)
            {
                return false;
            }
        }

        // Da rivedere
        // Loading conventions directive
        public void dload_convention_3(Term assemblyName, Term conventionName, Term id)
        {
            Struct convName = (Struct)conventionName.getTerm();
            Struct asName = (Struct)assemblyName.getTerm();

            if (convName == null || !convName.isAtom() || asName == null || !asName.isAtom() || (!(id is Var) && !id.isAtom()))
                return;

            try
            {
                Convention newConv = Convention.LoadConvention(asName.getName(), convName.getName());

                // Check if the convention is already registered in the staticObjects
                if (this.getRegisteredObject((Struct)id) != null)
                    return;

                // Register the convention in the staticObjects
                this.register((Struct)id, newConv);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }

        /// <summary>
        /// Unload a convention
        /// </summary>
        /// <param name="conventionId">Name of the loaded convention </param>
        /// <returns>True if the operation is successful</returns>
        public bool unload_convention_1(Term conventionId)
        {
            Struct convName = (Struct)conventionId.getTerm();

            if (!convName.isAtom())
                return false;

            try
            {
                Convention c = FindConvention(convName.getName());
                if (c == null)
                    return false;
                // Remove the convention from the convention list
                _conventionList.Remove(c);
                // Remove the object convention from current object list
                this.unregisterDynamic(convName);
                // If conventionId was a var deunify it
                if (conventionId is Var)
                    this.unify(conventionId, new Var());
                return true;
            }
            catch (Exception ex)
            {
                return false;
            }
        }

        /// <summary>
        /// Creates of a .NET object from an assembly - not backtrackable case
        /// </summary>
        /// <param name="conventionName">Name of the convention to bind with the object</param>
        /// <param name="className">Name of the class with namespace informations</param>
        /// <param name="constructorName">Name of the constructor used to instantiate the class</param>
        /// <param name="argl">List of arguments for the constructor</param>
        /// <param name="id">Reference to the instantiated object (out)</param>
        /// <returns>True if the operation is successful</returns>
        public bool new_object_5(Term conventionName, Term className, Term constructorName, Term argl, Term id)
        {
            className = className.getTerm();
            Struct arg = (Struct)argl.getTerm();
            id = id.getTerm();

            // Class name must be a simple atom
            if (!className.isAtom())
                return false;

            string clName = ((Struct)className).getName();

            // Retrieving Convention name if present
            string convName = null;
            if (conventionName != null && (conventionName.getTerm() is Struct))
                convName = ((Struct)conventionName.getTerm()).getName();

            // Retrieving Constructor name if present
            string constrName = null;
            if (constructorName != null && (constructorName.getTerm() is Struct))
                constrName = ((Struct)constructorName.getTerm()).getName();

            Convention conv = FindConvention(convName);

            if (conv != null)
            {
                // Convention found

                Struct revisedClassNameStruct = null;
                string revisedClassName = conv.GetClassName(clName);
                revisedClassNameStruct = new Struct(revisedClassName);

                if (conv.IsArrayClass(revisedClassName))
                {
                    // Construction of an array
                    string arrayClassName = revisedClassName.Substring(0, revisedClassName.Length - 2);
                    arrayClassName = arrayClassName + "[]";
                    revisedClassNameStruct = new Struct(arrayClassName);
                }

                if (constrName != null && !constrName.Equals(String.Empty))
                {
                    // If it is specified the static method that build the object, I have to call it
                    string revisedConstrName = conv.GetConstructorName(constrName);
                    string constrClassName = conv.GetConstructorClass(revisedClassName);
                    Struct constrNameStruct = new Struct(constrClassName);
                    Struct constr = new Struct("class", constrNameStruct);
                    Var constrClass = new Var();
                    this.unify(constrClass, constr);

                    Struct constrMethod = new Struct(revisedConstrName, getArrayFromList(arg));
                    Var constrMethodName = new Var();
                    this.unify(constrMethodName, constrMethod);

                    return base.java_call_3(constrClass, constrMethodName, id);
                }
                
                // Call the super class method after the modification of the names 
                bool returnValue = base.java_object_3(revisedClassNameStruct, argl, id);

                // If everything went fine, the type has been loaded correctly by the JavaLibrary class loader
                // therefore we can bind the type to the convention
                if (returnValue)
                {
                    Type classType = Type.GetType(revisedClassName);

                    if (!IsBoundToConvention(classType))
                    {
                        // The class is not link to the convention: link it!
                        BindClassToConvention(classType, conv);
                    }
                }

                return returnValue;
            }
            else
            {
                // Convention NOT found
                // For arrays nothing to do: I guess that the user used []

                if (constrName != null && !constrName.Equals(String.Empty))
                {
                    // If it is specified the static method that build the object, I have to call it
                    // but this time without convention
                    Struct constrNameStruct = new Struct(clName);
                    Struct constr = new Struct("class", constrNameStruct);
                    Var constrClass = new Var();
                    this.unify(constrClass, constr);

                    Struct constrMethod = new Struct(constrName, getArrayFromList(arg));
                    Var constrMethodName = new Var();
                    this.unify(constrMethodName, constrMethod);

                    return base.java_call_3(constrClass, constrMethodName, id);
                }
            }
               
            // No modifications 
            return base.java_object_3(className, argl, id);
        }

        public bool destroy_object_1(Term id)
        {
            id = id.getTerm();
            try
            {
                if (id.isGround())
                {
                    this.unregisterDynamic((Struct)id);
                }
                return true;
            }
            catch (Exception ex)
            {
                Console.Out.WriteLine(ex.ToString());
                return false;
            }
        }

        // In the following three methods is possible to factorize the code
        // Check this!!!
        public bool method_call_3(Term objId, Term method_name, Term idResult)
        {
            Struct objIdTerm = (Struct)objId.getTerm();
            Struct methodNameStruct = (Struct)method_name.getTerm();
            string methodName = methodNameStruct.getName();

            // check for accessing field or property  Obj.Field <- set/get(X)
            // in that case: objId is '.'(Obj, Field)
            if (!objIdTerm.isAtom())
            {
                if (objIdTerm is Var)
                    return false;

                Struct objIdStruct = (Struct)objIdTerm;
                if (objIdStruct.getName().Equals(".") && objIdStruct.getArity() == 2 && methodNameStruct.getArity() == 1)
                {
                    if (methodName.Equals("set"))
                        return oo_set(objIdStruct.getTerm(0), objIdStruct.getTerm(1), methodNameStruct.getTerm(0));
                    else if (methodName.Equals("get"))
                        return oo_get(objIdStruct.getTerm(0), objIdStruct.getTerm(1), methodNameStruct.getTerm(0));
                }
            }

            // Object must be instantiated
            if (objIdTerm is Var)
                return false;

            // Retrieving object instance
            object obj = this.getRegisteredDynamicObject((Struct)objIdTerm);

            if (obj != null)
            {
                // Instance method

                Convention conv = FindConvention(obj.GetType());

                if (conv != null)
                {
                    // Convention found
                    string revisedMethodName = conv.GetMemberName(methodName);
                    Struct methodStruct = new Struct(revisedMethodName, getArrayFromMethod(methodNameStruct));
                    return base.java_call_3(objId, methodStruct, idResult);
                }
                else
                {
                    // Convention NOT found
                    return base.java_call_3(objId, method_name, idResult);
                }

            }
            else
            {
                // Object not found: could be a static call

                if (objId.isCompound())
                {
                    if (objIdTerm.getArity() == 1 && objIdTerm.getName().Equals("class"))
                    {
                        // Static method
                        string className = RemoveApices(objIdTerm.getArg(0).ToString());
                        Convention conv = FindConventionByClassName(className); 

                        if (conv != null)
                        {
                            // Convention found
                            string revisedClassName = conv.GetClassName(className);
                            string revisedMethodName = conv.GetMemberName(methodName);

                            Struct classNameStruct = new Struct(revisedClassName);
                            Struct classStruct = new Struct("class", classNameStruct);
                            Var classVar = new Var();
                            this.unify(classVar, classStruct);

                            Struct staticMethod = new Struct(revisedMethodName, getArrayFromMethod(methodNameStruct));
                            Var staticMethodVar = new Var();
                            this.unify(staticMethodVar, staticMethod);

                            return base.java_call_3(classVar, staticMethodVar, idResult);
                        }
                        else
                        {
                            
                            // Convention NOT found
                            return base.java_call_3(objId, method_name, idResult);
                        }
                    }
                }
            }

            return false;
        }

        private bool oo_set(Term objId, Term memberTerm, Term what)
        {
            what = what.getTerm();

            // If memberTerm is not an atom and what is a not instantiated variable
            if (!memberTerm.isAtom() || what is Var)
               return false;

            string memberName = ((Struct)memberTerm).getName();

            // Object must be instantiated
            if (objId is Var)
                return false;

            // Retrieving object instance
            object obj = this.getRegisteredDynamicObject((Struct)objId);

            if (obj != null)
            {
                // Instance property or field
                Convention conv = FindConvention(obj.GetType());

                if (conv != null)
                {
                    // Convention found
                    string revisedFieldName = conv.GetFieldName(memberName);
                    string revisedPropertyMethod = conv.GetPropertySetterMethod(memberName);

                    FieldInfo field = obj.GetType().GetField(revisedFieldName);
                    MethodInfo method = obj.GetType().GetMethod(revisedPropertyMethod);

                    // Field or property?
                    if (field != null && method == null)
                    {
                        // Field
                        Struct filedStruct = new Struct(revisedFieldName);
                        Struct objStruct = new Struct(".", objId, filedStruct);
                        Var objVar = new Var();
                        this.unify(objVar, objStruct);

                        Struct methodStruct = new Struct("set", what);
                        Var methodVar = new Var();
                        this.unify(methodVar, methodStruct);

                        return base.java_call_3(objVar, methodVar, new Struct());
                    }
                    else if (field == null && method != null)
                    {
                        // Property
                        // So invocation method set_<PropName>
                        Struct methodStruct = new Struct(revisedPropertyMethod, what);
                        return base.java_call_3(objId, methodStruct, new Struct());
                    }
                }
                else
                {
                    // Convention NOT found
                    // Possible factorization of the code

                    FieldInfo field = obj.GetType().GetField(memberName);
                    MethodInfo method = obj.GetType().GetMethod(memberName);

                    // Field or property?
                    if (field != null && method == null)
                    {
                        // Field
                        Struct fieldStruct = new Struct(memberName);
                        Struct objStruct = new Struct(".", objId, fieldStruct);
                        Var objVar = new Var();
                        this.unify(objVar, objStruct);

                        Struct methodStruct = new Struct("set", what);
                        Var methodVar = new Var();
                        this.unify(methodVar, methodStruct);

                        return base.java_call_3(objVar, methodVar, new Struct());
                    }
                    else if (field == null && method != null)
                    {
                        // PROPERTY
                        // So invocation method set_<PropName>
                        Struct methodStruct = new Struct(memberName, what);
                        return base.java_call_3(objId, methodStruct, new Struct());
                    }
                }
            }
            else
            {
                // Object not found: could be a static call

                if (objId.isCompound())
                {
                    Struct objIdStruct = (Struct)objId;
                    if (objIdStruct.getArity() == 1 && objIdStruct.getName().Equals("class"))
                    {
                        // Static field or property
                        string className = RemoveApices(objIdStruct.getArg(0).ToString());
                        Convention conv = FindConventionByClassName(className);

                        if (conv != null)
                        {
                            // Convention found
                            string revisedClassname = conv.GetClassName(className);
                            Type classType = Type.GetType(revisedClassname);
                            string revisedFieldName = conv.GetFieldName(memberName);
                            string revisedPropertyMethod = conv.GetPropertySetterMethod(memberName);

                            FieldInfo field = classType.GetField(revisedFieldName);
                            MethodInfo method = classType.GetMethod(revisedPropertyMethod);

                            if (field != null && method == null)
                            {
                                // Field
                                Struct fieldStruct = new Struct(revisedFieldName);
                                Struct classNameStruct = new Struct(revisedClassname);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Struct st = new Struct(".", classStruct, fieldStruct);
                                Var classVar = new Var();
                                this.unify(classVar, st);

                                Struct methodStruct = new Struct("set", what);
                                Var methodVar = new Var();
                                this.unify(methodVar, methodStruct);

                                return base.java_call_3(classVar, methodVar, new Struct());
                            }
                            else if (field == null && method != null)
                            {
                                // PROPERTY
                                Struct classNameStruct = new Struct(revisedClassname);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Var classVar = new Var();
                                this.unify(classVar, classStruct);

                                Struct staticMethod = new Struct(revisedPropertyMethod, what);
                                Var staticMethodVar = new Var();
                                this.unify(staticMethodVar, staticMethod);

                                return base.java_call_3(classVar, staticMethodVar, new Struct());
                            }
                        }
                        else
                        {
                            // Convention not found

                            Type classType = Type.GetType(className);
                            FieldInfo field = classType.GetField(memberName);
                            MethodInfo method = classType.GetMethod(memberName);

                            if (field != null && method == null)
                            {
                                // Field
                                Struct fieldStruct = new Struct(memberName);
                                Struct classNameStruct = new Struct(className);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Struct st = new Struct(".", classStruct, fieldStruct);
                                Var classVar = new Var();
                                this.unify(classVar, st);

                                Struct methodStruct = new Struct("set", what);
                                Var methodVar = new Var();
                                this.unify(methodVar, methodStruct);

                                return base.java_call_3(classVar, methodVar, new Struct());
                            }
                            else if (field == null && method != null)
                            {
                                // Property
                                Struct classNameStruct = new Struct(className);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Var classVar = new Var();
                                this.unify(classVar, classStruct);

                                Struct staticMethod = new Struct(memberName, what);
                                Var staticMethodVar = new Var();
                                this.unify(staticMethodVar, staticMethod);

                                return base.java_call_3(classVar, staticMethodVar, new Struct());
                            }
                        }
                    }
                }

            }

            return false;
        }

        private bool oo_get(Term objId, Term memberTerm, Term what)
        {
            what = what.getTerm();
            // If memberTerm is not an atom and what is a not instantiated variable
            if (!memberTerm.isAtom())
                return false;

            string memberName = ((Struct)memberTerm).getName();

            // Object must be instantiated
            if (objId is Var)
                return false;

            // Retrieving object instance
            object obj = this.getRegisteredDynamicObject((Struct)objId);

            if (obj != null)
            {
                // Instance property or field
                Convention conv = FindConvention(obj.GetType());

                if (conv != null)
                {
                    // Convention found
                    string revisedFieldName = conv.GetFieldName(memberName);
                    string revisedPropertyMethod = conv.GetPropertyGetterMethod(memberName);

                    FieldInfo field = obj.GetType().GetField(revisedFieldName);
                    MethodInfo method = obj.GetType().GetMethod(revisedPropertyMethod);

                    if (field != null && method == null)
                    {
                        // Field
                        Struct filedStruct = new Struct(revisedFieldName);
                        Struct objStruct = new Struct(".", objId, filedStruct);
                        Var objVar = new Var();
                        this.unify(objVar, objStruct);

                        Struct methodStruct = new Struct("get", what);
                        Var methodVar = new Var();
                        this.unify(methodVar, methodStruct);

                        return base.java_call_3(objVar, methodVar, new Struct());
                    }
                    else if (field == null && method != null)
                    {
                        // Property
                        Struct methodStruct = new Struct(revisedPropertyMethod);
                        return base.java_call_3(objId, methodStruct, what);
                    }
                }
                else
                {
                    // Convention NOT found
                    FieldInfo field = obj.GetType().GetField(memberName);
                    MethodInfo method = obj.GetType().GetMethod(memberName);

                    if (field != null && method == null)
                    {
                        // Field
                        Struct fieldStruct = new Struct(memberName);
                        Struct objStruct = new Struct(".", objId, fieldStruct);
                        Var objVar = new Var();
                        this.unify(objVar, objStruct);

                        Struct methodStruct = new Struct("get", what);
                        Var methodVar = new Var();
                        this.unify(methodVar, methodStruct);

                        return base.java_call_3(objVar, methodVar, new Struct());
                    }
                    else if (field == null && method != null)
                    {
                        // Property
                        Struct methodStruct = new Struct(memberName);
                        return base.java_call_3(objId, methodStruct, what);
                    }
                }
            }
            else
            {
                // Could be a static invocation

                if (objId.isCompound())
                {
                    Struct objIdStruct = (Struct)objId;
                    if (objIdStruct.getArity() == 1 && objIdStruct.getName().Equals("class"))
                    {
                        // Static property or field
                        string className = RemoveApices(objIdStruct.getArg(0).ToString());
                        Convention conv = FindConventionByClassName(className);

                        if (conv != null)
                        {
                            // Convention found
                            string revisedClassname = conv.GetClassName(className);
                            Type classType = Type.GetType(revisedClassname);
                            string revisedFieldName = conv.GetFieldName(memberName);
                            string revisedPropertyMethod = conv.GetPropertyGetterMethod(memberName);

                            FieldInfo field = classType.GetField(revisedFieldName);
                            MethodInfo method = classType.GetMethod(revisedPropertyMethod);

                            if (field != null && method == null)
                            {
                                // Field
                                Struct fieldStruct = new Struct(revisedFieldName);
                                Struct classNameStruct = new Struct(revisedClassname);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Struct st = new Struct(".", classStruct, fieldStruct);
                                Var classVar = new Var();
                                this.unify(classVar, st);

                                Struct methodStruct = new Struct("get", what);
                                Var methodVar = new Var();
                                this.unify(methodVar, methodStruct);

                                return base.java_call_3(classVar, methodVar, new Struct());
                            }
                            else if (field == null && method != null)
                            {
                                // Property
                                Struct classNameStruct = new Struct(revisedClassname);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Var classVar = new Var();
                                this.unify(classVar, classStruct);

                                Struct staticMethod = new Struct(revisedPropertyMethod);
                                Var staticMethodVar = new Var();
                                this.unify(staticMethodVar, staticMethod);

                                return base.java_call_3(classVar, staticMethodVar, what);
                            }
                        }
                        else
                        {
                            // Convention NOT found
                            Type classType = Type.GetType(className);
                            FieldInfo field = classType.GetField(memberName);
                            MethodInfo method = classType.GetMethod(memberName);

                            if (field != null && method == null)
                            {
                                // Field
                                Struct fieldStruct = new Struct(memberName);
                                Struct classNameStruct = new Struct(className);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Struct st = new Struct(".", classStruct, fieldStruct);
                                Var classVar = new Var();
                                this.unify(classVar, st);

                                Struct methodStruct = new Struct("get", what);
                                Var methodVar = new Var();
                                this.unify(methodVar, methodStruct);

                                return base.java_call_3(classVar, methodVar, new Struct());
                            }
                            else if (field == null && method != null)
                            {
                                // Property
                                Struct classNameStruct = new Struct(className);
                                Struct classStruct = new Struct("class", classNameStruct);
                                Var classVar = new Var();
                                this.unify(classVar, classStruct);

                                Struct staticMethod = new Struct(memberName);
                                Var staticMethodVar = new Var();
                                this.unify(staticMethodVar, staticMethod);

                                return base.java_call_3(classVar, staticMethodVar, what);
                            }
                        }
                    }
                }

            }

            return false;
        }



        private Term[] getArrayFromList(Struct list)
        {
            Term[] args = new Term[list.listSize()];
            java.util.Iterator it = list.listIterator();
            int count = 0;
            while (it.hasNext())
            {
                args[(count++)] = (Term)it.next();
            }
            return args;
        }

        private Term[] getArrayFromMethod(Struct method)
        {
            Term[] args = new Term[method.getArity()];
            for (int i = 0; i < method.getArity(); i++)
            {
                args[i] = method.getArg(i);
            }
            return args;
        }



        /// <summary>
        /// Check if a type is already bound to a convention
        /// </summary>
        /// <param name="classType">Type of the class to be checked</param>
        /// <returns>True if it is bound, false otherwise</returns>
        private bool IsBoundToConvention(Type classType)
        {
            if (classType == null)
                return false;
            foreach (Convention convention in _conventionList.Keys)
            {
                List<string> values = (List<string>)_conventionList[convention];
                foreach (Object o in values)
                {
                    if (o is string)
                    {
                        string t = (string)o;
                        if (t.Equals(classType.AssemblyQualifiedName))
                            return true;
                    }
                }
            }
            return false;
        }

        /// <summary>
        /// Bind a class to a specific convention
        /// </summary>
        /// <param name="classType">Type of the class to bind</param>
        /// <param name="convention">Convention with which the type has to be bound</param>
        /// <returns>void</returns>
        private void BindClassToConvention(Type classType, Convention convention)
        {
            if (classType == null || convention == null)
                return;

            List<string> a = (List<string>)_conventionList[convention];
            if (!a.Contains(classType.AssemblyQualifiedName))
                a.Add(classType.AssemblyQualifiedName);
        }

        /// <summary>
        /// Find a convention by its name
        /// </summary>
        /// <param name="convName">Name of the convention which has to be found</param>
        /// <returns>Convention with the specified name or null if it doesn't exist</returns>
        private Convention FindConvention(string convName)
        {
            foreach (Convention conv in _conventionList.Keys)
                if (conv.Name.Equals(convName))
                    return conv;
            return null;
        }

        /// <summary>
        /// Find a convention bound to  specific type
        /// </summary>
        /// <param name="classType">Type of the class</param>
        /// <returns>Convention bound with the specified type or null if it doesn't exist</returns>
        private Convention FindConvention(Type classType)
        {
            if (classType == null)
                return null;

            foreach (Convention convention in _conventionList.Keys)
            {
                List<string> values = (List<string>)_conventionList[convention];
                foreach (Object o in values)
                {
                    if (o is string)
                    {
                        string t = (string)o;
                        if (t.Equals(classType.AssemblyQualifiedName))
                            return convention;
                    }
                }
            }
            return null;
        }

        // Comportamento particolare utilizzato per trovare le convenzioni in caso di chimate a metodi
        // (proprietà, campi pubbici) statici: se la convenzione non viene trovata per nome della classe 
        // specificato si provano tutte le convenzioni caricate
        /// <summary>
        /// Find a convention bound to a specific class providing the name of the class
        /// </summary>
        /// <param name="className">Name of the class</param>
        /// <returns>Convention bound with the specified class or null if it doesn't exist</returns>
        private Convention FindConventionByClassName(string className)
        {
            Convention conv = null;
            foreach (Convention convention in _conventionList.Keys)
            {
                List<string> values = (List<string>)_conventionList[convention];
                foreach (Object o in values)
                {
                    if (o is string)
                    {
                        string t = (string)o;
                        if (t.Equals(className))
                            conv = convention;
                    }
                }
            }

            if (conv == null)
            {
                // Convention unknown, then try them all (the first that works)
                foreach (Convention convention in _conventionList.Keys)
                {
                    Type cl = convention.GetClass(className);

                    if (cl != null)
                        return convention;
                }
            }

            return conv;
        }

        public static string RemoveApices(string st)
        {
            string result;
            if (st.StartsWith("'") && st.EndsWith("'"))
            {
                result = st.Substring(1, st.Length - 2);
            }
            else
            {
                result = st;
            }
            return result;
        }

       
    }
}