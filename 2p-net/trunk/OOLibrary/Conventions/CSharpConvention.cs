using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OOLibrary;

namespace Conventions
{
    public class CSharpConvention : Convention
    {
        public override string Name
        {
            get { return "csharp"; }
        }

        public override string GetClassName(string oldClassName)
        {
            int dotindex = oldClassName.LastIndexOf('.') + 1;
            string className = oldClassName.Substring(dotindex, (oldClassName.Length - dotindex));
            return GetNamespace(oldClassName.Substring(0, dotindex)) +
                Capitalize(className);
        }

        public override string GetMemberName(string oldMemberName)
        {
            return Capitalize(oldMemberName);
        }

        public override string GetConstructorName(string oldConstructorName)
        {
            return Capitalize(oldConstructorName);
        }

        public override string GetPropertyGetterMethod(string oldPropertyName)
        {
            return "get_" + Capitalize(oldPropertyName);
        }

        public override string GetPropertySetterMethod(string oldPropertyName)
        {
            return "set_" + Capitalize(oldPropertyName);
        }

        public override string GetFieldName(string oldFieldName)
        {
            return (Char.ToLower(oldFieldName[0]) + oldFieldName.Substring(1, oldFieldName.Length - 1));
        }

        private string Capitalize(string name)
        {
            return (Char.ToUpper(name[0]) + name.Substring(1, name.Length - 1));
        }
    }
}
