using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using fit;
using System.Reflection;

namespace NETFileRunner
{
    public class NETFileRunner : FileRunner
    {
        public NETFileRunner()
        {
            
        }

        public static void Main(string[] args)
        {
            if (args.Length == 3)
            {
                Assembly ass = Assembly.LoadFrom(args[2]);
                //Console.WriteLine(ass.FullName);
                //Console.WriteLine("-------");
                //AppDomain.CurrentDomain.Load(ass.FullName);
                //foreach (Assembly item in AppDomain.CurrentDomain.GetAssemblies())
                //{
                //    Console.WriteLine(item.FullName);
                //}
                //Console.WriteLine("-------");

                string[] args2 = new string[2];
                args2[0] = args[0];
                args2[1] = args[1];

                new NETFileRunner().run(args2);
            }
            else
            {
                Console.WriteLine("Usage: NETFileRunner input-file output-file fixture-lib-file");
            }
        }
    }
}
