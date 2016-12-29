using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using System.Reflection;

namespace OOLibrary
{
    public abstract class Convention
    {
        /// <summary>
        /// Name of the convention
        /// </summary>
        public abstract string Name
        {
            get;
        }

        /// <summary>
        /// Converts the name of the namespace
        /// </summary>
        /// <param name="nameSpace">Name of the namespace that must be converted</param>
        /// <returns>Name of the converted namespace</returns>
        public virtual string GetNamespace(string oldNamespace)
        {
            return oldNamespace;
        }

        /// <summary>
        /// Converts the name of the class
        /// </summary>
        /// <param name="oldClassName">Name of the class that must be converted</param>
        /// <returns>Name of the converted class</returns>
        public virtual string GetClassName(string oldClassName)
        {
            return oldClassName;
        }

        /// <summary>
        /// Converts the name of the method that build the object
        /// </summary>
        /// <param name="oldConstructorName">Name of the constructor that must be converted</param>
        /// <returns>Name of the converted constructor</returns>
        public virtual string GetConstructorName(string oldConstructorName)
        {
            return oldConstructorName;
        }

        /// <summary>
        /// Converts the name of the class that build the object (for certain languages the class is different)
        /// </summary>
        /// <param name="oldConstructorName">Name of the class that must be converted</param>
        /// <returns>Name of the converted class</returns>
        public virtual string GetConstructorClass(string oldConstructorClass)
        {
            return oldConstructorClass;
        }

        /// <summary>
        /// Converts the name of the member
        /// </summary>
        /// <param name="oldMemberName">Name of the member that must be converted</param>
        /// <returns>Name of the converted member</returns>
        public virtual string GetMemberName(string oldMemberName)
        {
            return oldMemberName;
        }

        /// <summary>
        /// Converts the name of the property
        /// </summary>
        /// <param name="oldPropertyName">Name of the property that must be converted</param>
        /// <returns>Name of the converted property</returns>
        public virtual string GetPropertyGetterMethod(string oldPropertyName)
        {
            return oldPropertyName;
        }

        /// <summary>
        /// Converts the name of the property
        /// </summary>
        /// <param name="oldPropertyName">Name of the property that must be converted</param>
        /// <returns>Name of the converted property</returns>
        public virtual string GetPropertySetterMethod(string oldPropertyName)
        {
            return oldPropertyName;
        }

        /// <summary>
        /// Converts the name of the field
        /// </summary>
        /// <param name="oldFieldName">Name of the field that must be converted</param>
        /// <returns>Name of the converted field</returns>
        public virtual string GetFieldName(string oldFieldName)
        {
            return oldFieldName;
        }

        /// <summary>
        /// Gets a value indicating whether the specified class name is an array type
        /// </summary>
        /// <param name="className">Name of the class</param>
        /// <returns>True if the class is an array type</returns>
        public virtual bool IsArrayClass(string className)
        {
            return className.EndsWith("[]");
        }

        /// <summary>
        /// Load a convention class from a file
        /// </summary>
        /// <param name="assembly">Assembly file name containing convention</param>
        /// <param name="className">Name of the convention class</param>
        /// <returns>Instance of the convention loaded</returns>
        /// <exception cref="System.IO.FileNotFoundException">Unable to find assembly file</exception>
        /// <exception cref="System.IO.FileLoadException">Errors in loading assembly file</exception>
        /// <exception cref="System.Security.SecurityException">Assembly file cannot be accessed</exception>
        /// <exception cref="ArgumentException">Invalid arguments in retrieving type</exception>
        /// <exception cref="BadImageFormatException">Invalid or corrupt assembly file</exception>
        /// <exception cref="TypeLoadException">Unable to load class</exception>
        public static Convention LoadConvention(string assembly, string className)
        {
            Assembly a = Assembly.LoadFrom(assembly);
            Type cl = a.GetType(className);
            Object o = Activator.CreateInstance(cl);

            if (o is Convention)
                return (Convention)o;

            return null;
        }

        protected internal Type GetClass(string className)
        {
            if (className == null || className == String.Empty)
                return null;

            return Type.GetType(GetClassName(className), false);
        }

    }
}
