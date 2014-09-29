using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using alice;
using alice.tuprolog;
using alice.tuprolog.@event;

using java.io;

namespace Idea3
{
    //Classe adapter per la Client.java
    class ClientAdapter
    {
        private readonly Prolog _engine;        
        
        public ClientAdapter(String theoryPath)
        {
            _engine = new Prolog();
            FileInputStream fis = new FileInputStream(new File(theoryPath));
            Theory t = new Theory(fis);
            _engine.setTheory(t);            
            Listener ls = new Listener();
            _engine.addExceptionListener(ls);
            _engine.addOutputListener(ls);
            _engine.addSpyListener(ls);
        }
        //Espone i metodi della classe Java Client
        public int Login()
        {
            SolveInfo si;
            si = _engine.solve("login(Id).");
            if (si.isSuccess())
            {
                int id = Convert.ToInt32(si.getVarValue("Id").toString());
                return id;
            }
            else
                return -1;
        }
        public String SelectMode(int id,int mode)
        {
            SolveInfo si;
            si = _engine.solve("selectMode(" + id + "," + mode + ",Ex).");
            if (si.isSuccess())
                return si.getVarValue("Ex").toString();
            else
                return "errore";
        }
        public String Play(int id,int place)
        {
            SolveInfo si;
            si = _engine.solve("play(" + id + "," + place + ",Board).");
            if (si.isSuccess())
                return si.getVarValue("Board").toString();
            else
                return "errore";
        }
        
        public String GetBoard(int id)
        {
            SolveInfo si;
            si = _engine.solve("getBoard(" + id + ",Board).");
            if (si.isSuccess())
                return si.getVarValue("Board").toString();
            else
                return "errore";
        }
        public int Logout(int id)
        {
            SolveInfo si;
            si = _engine.solve("logout("+id+",Ex).");
            if (si.isSuccess())
                return Convert.ToInt32(si.getVarValue("Ex").toString());
            else
                return -1;
        }
        //Metodi utilità della classe Board.java
        public int GetRemaning(String board)
        {
            SolveInfo si;
            si = _engine.solve("getRemaning(" + board + ",Rem).");
            if (si.isSuccess())
                return Convert.ToInt32(si.getVarValue("Rem").toString());
            else 
                return -1;
        }
        
        public int CheckWin(String board)
        {
            SolveInfo si;
            si = _engine.solve("checkWin(" + board + ",Res).");
            if (si.isSuccess())
                return Convert.ToInt32(si.getVarValue("Res").toString());
            else
                return -1;
        }
        public String setClasspath(String[] paths)//Metodo per impostare il classpath
        {
            SolveInfo si;
            String pathsStr="[";
            int n=paths.Length;
            for (int i = 0; i < n;i++ )
            {
                pathsStr += "'" + paths[i] + "'";
                if (i + 1 != n)
                    pathsStr += ",";
            }
            pathsStr += "]";
                si = _engine.solve("set_classpath(" + pathsStr + "),get_classpath(Path).");
                if (si.isSuccess())
                    return si.getVarValue("Path").toString();
                else
                    return "errore";
        }
        public String getClassPath()//Metodo per ottenere il classpath attuale
        {
            return _engine.solve("get_classpath(Path).").getVarValue("Path").toString();
        }
        //Converte la stringa board(_,_,_,_,_,_,_,_,_) in una matrice di caratteri
        public char[,] GetBoardMatrix(String board)
        {            
            char[,] matrix=new char[3,3];
            String[] arrayStr = board.Split(',', '(', ')',' ').ToArray();
            for (int i = 0; i < 9;i++ )
            {
                matrix[i / 3, i % 3] = arrayStr[i+1][0];
            }
            return matrix;

        }
        //Converte la stringa board(_,_,_,_,_,_,_,_,_) in un altra formato di stringa
        public String GetBoardMatrixPretty(String board)
        {
            String str="";
            char[,] matrix=GetBoardMatrix(board);
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (matrix[i, j]=='_')
                        str += "" + (i * 3 + j + 1) + "\t";
                    else
                        str += "" + matrix[i, j]+"\t";
                    
                }
                str += "\n";
            }
            return str;
        }
    }
    //Permette di scrivere nella console gli output dell'inteprete
    class Listener : ExceptionListener, OutputListener, SpyListener
    {
        private void Write(String str)
        {
            System.Console.Write(str);
        }
        public void onException(ExceptionEvent e)
        {
            Write(e.getMsg());
        }
        public void onOutput(OutputEvent e)
        {
            Write(e.getMsg());
        }
        public void onSpy(SpyEvent e)
        {
            Write(e.getMsg());
        }

    }
}
