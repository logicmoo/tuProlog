using System;
using System.Collections.Generic;
using System.Text;

using Stock;

using alice.tuprolog;
using alice.tuprolog.@event;
using java.io;

namespace Idea2
{
    class Program
    {
        static void Main(string[] args)
        {
            //Inizializza interprete e imposta la teoria
            SolveInfo si;
            InputStream ins = new FileInputStream("../../../prolog/adapterTheory.pl");
            Theory t = new Theory(ins);
            Prolog engine = new Prolog();
            engine.setTheory(t);            
            engine.addExceptionListener(new MyExcptioListener());
            engine.addOutputListener(new MyOutputListener());
            engine.addSpyListener(new MySpyListener());
            //Richiede all'utente quale azioni analizzare
            List<String> stocks = new List<string>();
            String str;
            System.Console.WriteLine("The program downloads the history of prices of a bunch of stocks, then upload an analysis on a MySQL database");//TODO
            do
            {
                System.Console.WriteLine("Write a stock name(e.g. msft, goog) to add a stock to analyze or return to finish");
                str = System.Console.ReadLine();
                if(str.Length !=0)
                stocks.Add(str);
            } while (str.Length != 0);
            //Richoede il periodo d'analisi
            System.Console.WriteLine("Insert Days of the Analysis");
            str = System.Console.ReadLine();
            int days = Int32.Parse(str);
            System.Console.WriteLine("Wait please...");
            //Usa il metodo factory (F#) per ottenere gli StockAnalyzer
            var analyzers = StockAnalyzer.GetAnalyzers(stocks, days);
            IEnumerator<StockAnalyzer> ie = analyzers.GetEnumerator();
            //Inserisce nel database gli StockAnalyzer usando l'inteprete tuProlog.NET
            for (int i = 0; i < stocks.Count; i++)
            {
                ie.MoveNext();
                str = stocks[i] + ";" + ie.Current.ToString;
                System.Console.WriteLine(str);
                si = engine.solve("addStockAnalysis('" + str + "',RS).");                
                if (("" + si.getVarValue("RS")) == "0")
                    System.Console.WriteLine("Tuple added");
                else if (("" + si.getVarValue("RS")) == "1")
                    System.Console.WriteLine("Tuple already presents");
                
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
                System.Console.WriteLine(e.getMsg());
            }
        }
        public class MyExcptioListener : ExceptionListener
        {
            public void onException(ExceptionEvent e)
            {
                System.Console.WriteLine(e.getMsg());
            }
        }
    }
}
