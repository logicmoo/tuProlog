using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using alice.tuprolog;
using alice.tuprolog.@event;
using java.io;

namespace tictactoe
{
    class Program
    {
        static void Main(string[] args)
        {
            SolveInfo info;
                        
            string res;
            //carico il file di test
            InputStream stream = new FileInputStream("../../../prolog/tictac.pl");
            Theory t = new Theory(stream);
            //preparo il motore prolog
            Prolog engine = new Prolog();
            //OOLibrary.OOLibrary lib = new OOLibrary.OOLibrary();            
            //engine.unloadLibrary("alice.tuprolog.lib.JavaLibrary");
            //engine.loadLibrary(lib);            
            engine.setTheory(t);
            
            

            //Listener per output e spy
            engine.addOutputListener(new MyOutputListener());
            engine.addSpyListener(new MySpyListener());
            //engine.setSpy(true);

            do
            {
                System.Console.Write("Select the version to be loaded (cs, vb, fs, java. default: cs)");
                res = System.Console.ReadLine();

                switch (res)
                {
                    case "cs":
                        info = engine.solve("loadgameCS.");
                        break;
                    case "vb":
                        info = engine.solve("loadgameVB.");
                        break;
                    case "fs":
                        info = engine.solve("loadgameFS.");
                        break;
                    case "java":
                        info = engine.solve("loadgameJava.");
                        break;
                    default:
                        info = engine.solve("loadgameCS.");
                        break;
                }

                System.Console.Write("Play again? (y/n):");
                res = System.Console.ReadLine();

            } while (res.StartsWith("y"));

            System.Console.WriteLine(info.toString());

        }
    }

    public class MySpyListener : SpyListener
    {
        public void onSpy(SpyEvent se)
        {
            System.Console.WriteLine(se.toString());
        }
    }


    public class MyOutputListener : OutputListener
    {
        public void onOutput(OutputEvent e)
        {
            string message = e.getMsg();
            if (!(message.StartsWith("'board") || message.StartsWith("Board"))) //filtra i messaggi derivanti da text_term
                System.Console.Write(e.getMsg());
        }
    }
}
