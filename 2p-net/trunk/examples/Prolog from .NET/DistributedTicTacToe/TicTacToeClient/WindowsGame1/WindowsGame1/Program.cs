using System;

namespace TicTacToeGame
{
#if WINDOWS || XBOX
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        static void Main(string[] args)
        {
            using (TicTacToeGame game = new TicTacToeGame())
            {
                game.Run();
            }
        }
    }
#endif
}

