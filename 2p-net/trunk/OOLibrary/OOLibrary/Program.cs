using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using alice.tuprolog;
using java.io;

using OOLibrary;


namespace OOLibrary
{
    class Program
    {
        public static void Main(string[] args)
        {
            //OpenFileDialog dialog = new OpenFileDialog();
            //dialog.ShowDialog();

            OOLibrary lib = new OOLibrary();
            InputStream stream = new FileInputStream(@"Test.pl");
            // InputStream stream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\Test.pl");
            
            
            Prolog engine = new Prolog();
            //engine.unloadLibrary("alice.tuprolog.lib.JavaLibrary");
            //engine.loadLibrary(lib);
           
            Theory th = new Theory(stream);
            engine.addTheory(th);
            SolveInfo info = engine.solve("testInteroperation(X)."); 
            System.Console.WriteLine(info.isSuccess());
            System.Console.WriteLine(info.isHalted());

            //info = engine.solve("testBasicsWithJava.");
            //System.Console.WriteLine(info.isSuccess());
            //System.Console.WriteLine(info.isHalted());


            //java.util.List[] primitives = lib.getPrimitives();

            //for (int i = 0; i < primitives.Length; i++)
            //{
            //    PrimitiveInfo p = (PrimitiveInfo)primitives[i];
            //}



            System.Console.ReadLine();
        }
    }
}
