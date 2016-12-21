using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OOLibrary;

namespace Conventions
{
    public class VBDotNetConvention : Convention
    {
        public override string Name
        {
            get { return "visualbasic"; }
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

        public override bool IsArrayClass(string className)
        {
            return className.EndsWith("()");
        }

        private string Capitalize(string name)
        {
            return (Char.ToUpper(name[0]) + name.Substring(1, name.Length - 1));
        }
    }
}
