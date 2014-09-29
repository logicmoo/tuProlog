using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Idea3
{
    //Reimplementa i metodi della classe ClientAdapter nascondendo alcuni dettagli 
    class GameAdapter : ClientAdapter
    {
        private int _id;
        private String _board;
        
        
        public GameAdapter(String theoryPath) : base(theoryPath) 
        {
            setClasspath(new String[]{"."});
            _id=-1;
            _board = "board(_,_,_,_,_,_,_,_,_)";
        }
        //Indica se si è raggiunta una delle condizioni di conclusione
        public bool Finished {
            get
            {
                return CheckWin != 0 || Remaning == 0;
            }            
        }        
        public new void Login()
        {
            _board = "board(_,_,_,_,_,_,_,_,_)";
            _id = base.Login();            
        }
        public  String SelectMode(int mode)
        {
            _board = base.SelectMode(_id, mode);
            return _board;
        }
        public void Play(int place)
        {
            _board = base.Play(_id, place);
        }
         
        public void Logout()
        {
            base.Logout(_id);
        }
        public String BoardStr { get
            {
                return _board;
            }
        }
        public String BoardPretty
        {
            get
            {
                return base.GetBoardMatrixPretty(_board);
            }
        }
        public int Remaning { 
            get 
            {
                return base.GetRemaning(_board);
            } 
        }
        public int Winner
        {
            get
            {
                return base.CheckWin(_board);
            }
        }
        public  String GetBoard()
        {
            return _board;
        }
        public char[,] Board
        {
            get
            {                
                return base.GetBoardMatrix(_board);
            }
        }
        public bool GameActive
        {
            get { return _id >= 0; }
        }
        public new int CheckWin
        {
            get { return base.CheckWin(_board); }
        }       

    }
}
