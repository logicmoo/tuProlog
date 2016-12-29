using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using OOLibrary;

namespace Conventions
{
    public class JavaConvention : Convention
    {
        public override string Name
        {
            get { return "java"; }
        }

        public override string GetNamespace(string oldNamespace)
        {
            return oldNamespace.ToLower();
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
            return DeCapitalize(oldMemberName);
        }

        public override string GetFieldName(string oldFieldName)
        {
            return DeCapitalize(oldFieldName);
        }

        private string DeCapitalize(string name)
        {
            return (Char.ToLower(name[0]) + name.Substring(1, name.Length - 1));
        }

        private string Capitalize(string name)
        {
            return (Char.ToUpper(name[0]) + name.Substring(1, name.Length - 1));
        }
    }
}
