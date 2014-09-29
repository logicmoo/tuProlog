using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace TTTCS
{
    public class TicTacToe
    {
        //giocatore 1 usa TRUE, giocatore 2 usa FALSE
        private char[] _caselle = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        //giocatore 1 usa X, giocatore 2 usa O
        private int _Xstatus, _Ostatus;
        //mosse ancora effettuabili (9 inizialmente)
        private int _remaining;

        private readonly static int[] PowArray = { 1, 2, 4, 8, 16, 32, 64, 128, 256 };
        private readonly static int[] WinArray = { 7, 56, 73, 84, 146, 273, 292, 448 };

        //Costruttore
        public TicTacToe()
        {
            _Xstatus = 0;
            _Ostatus = 0;
            _remaining = 9;
        }

        #region properties

        public char[] Caselle { get { return _caselle; } }

        //Numero di caselle vuote restanti
        public int Remaining { get { return _remaining; } }

        //restituisce una stringa con i valori della tavola sotto forma di termine Prolog
        public string Board
        {
            get
            {
                // formato: board(_,x,o,x,_,_,_,_,x,o)  --- _ rappresenta caselle vuote
                StringBuilder sb = new StringBuilder();
                sb.Append("board(");
                for (int i = 0; i < _caselle.Length; i++)
                {
                    if (_caselle[i] == 'x' || _caselle[i] == 'o')
                        sb.Append(_caselle[i]);
                    else
                        sb.Append('_');

                    if (i != (_caselle.Length - 1))
                        sb.Append(',');
                }
                sb.Append(')');
                return sb.ToString();
            }
        }

        #endregion

        public void Gioca(int casella, string player)
        {
            casella--; //accetta come input numeri da 1 a 9
            if (_remaining > 0)
            {
                if (_caselle[casella] != 'x' || _caselle[casella] != 'o')
                {
                    if (player.Equals("x"))
                    {
                        _caselle[casella] = 'x';
                        _Xstatus += PowArray[casella];
                    }
                    else if (player.Equals("o"))
                    {
                        _caselle[casella] = 'o';
                        _Ostatus += PowArray[casella];
                    }
                }
                //un altra mossa è stata compiuta
                _remaining--;
            }
        }

        /// <summary>
        /// Il controllo per la vittoria viene fatto confrontando il valore delle posizioni accumulati in _Xstatus e _Ostatus
        /// I valori vengono assegnati come dei flag binari, l'array WinArray contiene i valori decimali delle combinazioni 
        /// vincenti di caselle, _Xstatus e _Ostatus contengono il valore decimale delle posizioni occupate dalle X e dalle O.
        /// Effettuando un AND binario con ognuno dei valori presenti nell'array è possibile scoprire rapidamente se è presente
        /// una combinazione vincente.
        /// 
        /// Non vengono considerati pareggi, per quello è sufficiente controllare il numero di caselle rimaste vuote.
        /// </summary>
        /// <returns>
        ///     1 se hanno vinto le X
        ///     2 se hanno vinto le O
        ///     0 in caso nessuno abbia vinto
        /// </returns>
        public int CheckWin()
        {
            for (int i = 0; i < WinArray.Length; i++)
                if ((_Xstatus & WinArray[i]) == WinArray[i])
                    return 1;
            for (int i = 0; i < WinArray.Length; i++)
                if ((_Ostatus & WinArray[i]) == WinArray[i])
                    return 2;
            return 0;
        }

        public void PrintBoard()
        {
            String format = "\n {0} | {1} | {2} \n";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _caselle.Length; i += 3)
            {
                sb.AppendFormat(format, _caselle[i], _caselle[i + 1], _caselle[i + 2]);
            }
            Console.WriteLine(sb.ToString());
        }

        public int InputMove()
        {
            int casella = 0;
            string read = null;
            do
            {
                try
                {
                    Console.Write("Your move: ");
                    read = Console.ReadLine();
                    casella = Int32.Parse(read);
                }
                catch (Exception) { continue; }
            } while (casella < 0 || casella > 10
                     || _caselle[(casella-1)] != read.ElementAt(0) );
            //condizione di uscita
            // casella numero tra 1 e 9 (estremi inclusi)
            // casella scelta non deve essere occupata
            return casella;
        }

        public static string InputResponse()
        {
            string response = null;
            do
            {
                response = Console.ReadLine();
                if (response.StartsWith("y"))
                    response = "yes";
                else if (response.StartsWith("n"))
                    response = "no";
                else
                {
                    Console.Write("Not a valid response! (y/n): ");
                    response = null;
                }
            } while (response == null);

            return response;
        }

        public static string InputPlayer()
        {
            string player = null;
            do
            {
                player = Console.ReadLine();
                if (player.StartsWith("x"))
                    player = "x";
                else if (player.StartsWith("o"))
                    player = "o";
                else
                {
                    Console.Write("Not a valid response! (o/x): ");
                    player = null;
                }
            } while (player == null);

            return player;
        }

    }
}
