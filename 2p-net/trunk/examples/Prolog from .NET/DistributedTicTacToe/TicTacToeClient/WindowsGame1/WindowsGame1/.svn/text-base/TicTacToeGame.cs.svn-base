using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;

using Idea3;

namespace TicTacToeGame
{   
    public class TicTacToeGame : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatchExt spriteBatch;        
        //Texture
        private Texture2D _xTexture;
        private Texture2D _oTexture;
        private Texture2D _grid;
        private Texture2D _background;
        private Texture2D _empty;
        private SpriteFont _font;
        private Vector2 _gridPosition;
        //Bottoni
        private List<IButton> _list;//Contiene la lista dei bottoni da controllare 
        ButtonWithLabel _buttonInvert;
        ButtonWithLabel _buttonSecond;
        ButtonWithLabel _buttonFirst;
        
        private bool _invertedIcon;
        private bool _selectMode;       

        private GameAdapter _gameAdapter;
        private char[,] _board;

        public TicTacToeGame()
        {
            IsMouseVisible = true;            
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";            
            _list = new List<IButton>();//Crea la lista di bottoni
        }        
        //Metodo utilizato per inizializare il gioco
        protected override void Initialize()
        {
            //inizializzo le varibili di stato
            _invertedIcon = false;
            _selectMode = false;
            _gameAdapter = new GameAdapter("adapterTheory.pl");          //imposto la teoria 
            base.Initialize();
        }

        //Metodo utilizato per caricare i contenuti
        protected override void LoadContent()
        {            
            spriteBatch = new SpriteBatchExt(GraphicsDevice);
            //Carica i Texture
            _oTexture = Content.Load<Texture2D>("O");
            _xTexture = Content.Load<Texture2D>("X");
            _grid = Content.Load<Texture2D>("Grid");
            _background = Content.Load<Texture2D>("Sfondo");
            _empty = Content.Load<Texture2D>("Empty");
            //Carica i font
            _font = Content.Load<SpriteFont>("SpriteFont1");
            //Inizializzazzione  interfaccia utente
            //Griglia
            _gridPosition.X = (Window.ClientBounds.Width - 320) / 2;
            _gridPosition.Y = (Window.ClientBounds.Height - 320) / 2;            
            for (int i = 0; i <3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    IButton button = new ButtonGrid(_empty, new Rectangle(((int)_gridPosition.X) + 110 * i,((int)_gridPosition.Y)+ 110 * j, 100, 100),i*3+j+1);
                    button.Operation = OperationGrid;
                    _list.Add(button);
                }             
            }
            //Bottone New Play
            ButtonWithLabel buttonPlay = new ButtonWithLabel(_font, "New Game", new Vector2(20, 20), _background);
            buttonPlay.Operation = OperationNewGame;
            _list.Add(buttonPlay);
            //Bottoni della selectMode
            _buttonFirst = new ButtonWithLabel(_font, "I want to go first!", new Vector2(Window.ClientBounds.Width / 8, Window.ClientBounds.Height-40), _background);
            _buttonSecond = new ButtonWithLabel(_font, "I want to go second!", new Vector2(5 * Window.ClientBounds.Width / 8, Window.ClientBounds.Height - 40), _background);
            _buttonFirst.Operation = OperationFirst;
            _buttonSecond.Operation = OperationSecond;    
            //Bottone inverti icona
            _buttonInvert = new ButtonWithLabel(_font, "Your icon "+(_invertedIcon?"O":"X"), new Vector2(20, 50), _background);
            _buttonInvert.Operation = OperationInvert;
            _list.Add(_buttonInvert);            
        }

        //Metodo per "scaricare" le risorse
        protected override void UnloadContent()
        {            
            //Effettua il logout alla fine del 
            if (_gameAdapter.GameActive)            
                _gameAdapter.Logout();
        }

        //Metodo eseguito ad continuamento per modellare la logica di gioco
        protected override void Update(GameTime gameTime)
        {            
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
                this.Exit();
            //Inversione dell'icona giocatore/computer
            _buttonInvert.Text = "Your icon " + (_invertedIcon ? "O" : "X");
            if (_gameAdapter.GameActive)
            {
                _board = _gameAdapter.Board;
                for (int i = 0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        foreach (ButtonGrid bg in _list.Where(b => b.GetType() == typeof(ButtonGrid)))
                        {
                            if (bg.Place == i * 3 + j + 1)
                                switch (_board[i, j])
                                {
                                    case 'x': bg.Texture = _invertedIcon?_oTexture:_xTexture; break;
                                    case 'o': bg.Texture = _invertedIcon?_xTexture:_oTexture; break;
                                    default: bg.Texture = _empty; break;
                                }
                        }
                    }
                }
            }
            //Controllo se il gioco è concluso
            if (_gameAdapter.Finished)
            {
                foreach (ButtonGrid bg in _list.Where(b => b.GetType() == typeof(ButtonGrid)))
                {
                    bg.Used = true;
                }
            }
            //Controllo se uno dei pulsanti è stato premuto ed invocazione del relativo delegato
            if (Mouse.GetState().LeftButton == ButtonState.Pressed)
            {                
                int x = Mouse.GetState().X;
                int y = Mouse.GetState().Y;
                foreach (IButton b in _list)
                {
                    if (b.isInside(x, y)&&(!b.Activated))
                        b.Operation(b);
                }
                if (_selectMode && _buttonFirst.isInside(x, y)&& !_buttonFirst.Activated)
                {
                    _buttonFirst.Operation(_buttonFirst);
                }
                if (_selectMode && _buttonSecond.isInside(x, y)&& !_buttonSecond.Activated)
                {
                    _buttonSecond.Operation(_buttonSecond);
                }
            }
            //Disattivazione del pulsante se il mouse è stato rilasciato
            if (Mouse.GetState().LeftButton == ButtonState.Released)
            {
                foreach (IButton b in _list)
                {
                    b.Activated = false;
                }
                _buttonInvert.Activated = false;
                _buttonFirst.Activated = false;
                _buttonSecond.Activated = false;
            }
            base.Update(gameTime);
        }
        
       //Metodo per disegnare gli oggetti a schermo
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.White);
            spriteBatch.Begin();
            spriteBatch.Draw(_grid, new Rectangle((int)_gridPosition.X, (int)_gridPosition.Y, 320, 320),Color.White);
            //Disegna i bottoni
            foreach(IButton b in _list)
            {
                spriteBatch.DrawButton(b, Color.Black);
            }
            //Disegna la label "Press New Game" se il gioco non è attivo
            if (!_gameAdapter.GameActive)
            {
                ButtonWithLabel B=new ButtonWithLabel(_font,"Press \"New Game\"", new Vector2(0, 0), _background);
                
                B.Position =new Vector2((Window.ClientBounds.Width - B.Dimension.X) / 2,20);
                spriteBatch.DrawButton(B, Color.White);
            }
            //Disegna il risultato della partita se il gioco è finito
            if (_gameAdapter.Finished)
            {
                String winnerString = "Draw!";
                switch (_gameAdapter.CheckWin)
                {
                    case 1: winnerString = "You Win!"; break;
                    case 2: winnerString = "You Lose!"; break;
                }
                ButtonWithLabel winner = new ButtonWithLabel(_font, winnerString, new Vector2(0, 0), _background);
                winner.Position = new Vector2((Window.ClientBounds.Width - winner.Dimension.X) / 2, (Window.ClientBounds.Height - winner.Dimension.Y) / 2);
                spriteBatch.DrawButton(winner, Color.White);
            }
            //Disegna i pulsanti della selectMode se è attiva
            if (_selectMode&& _gameAdapter.GameActive)
            {
                spriteBatch.DrawButton(_buttonFirst, Color.Red);
                spriteBatch.DrawButton(_buttonSecond, Color.Red);
            }
            spriteBatch.End();

            base.Draw(gameTime);
        }
        //Metodo che viene assegnato ai bottoni della griglia
        private void OperationGrid(IButton button)
        {
            if (button.GetType() == typeof(ButtonGrid))
            {
                button.Activated = true;
                ButtonGrid bg = (ButtonGrid)button;
                if (_gameAdapter.GameActive && !bg.Used)
                {
                    _gameAdapter.Play(bg.Place);
                    bg.Used = true;
                }
            }
        }
        //Metodo assegnato al pulsante "New Game" che inizializza un altro gioco
        private void OperationNewGame(IButton button)
        {
            if (button.GetType() == typeof(ButtonWithLabel))
            {
                foreach (ButtonGrid bg in _list.Where(b => b.GetType() == typeof(ButtonGrid)))
                {
                    bg.Used = true ;
                }
                button.Activated = true;
                ButtonWithLabel bl = (ButtonWithLabel)button;
                if (_gameAdapter.GameActive)
                {
                    _gameAdapter.Logout();

                }                
                _gameAdapter.Login();
                _selectMode = true;        
            }
        }
        //Metodo utilizato per impostare la prima mossa all'utente
        private void OperationFirst(IButton button)
        {
            if (button.GetType() == typeof(ButtonWithLabel))
            {                
                button.Activated = true;                
                _gameAdapter.SelectMode(0);
                _selectMode = false;
                foreach (ButtonGrid bg in _list.Where(b => b.GetType() == typeof(ButtonGrid)))
                {
                    bg.Used = false;
                }
            }
        }
        //Metodo utilizato per impostare la prima mossa al computer
        private void OperationSecond(IButton button)
        {
            if (button.GetType() == typeof(ButtonWithLabel))
            {
                button.Activated = true;
                _gameAdapter.SelectMode(1);
                _selectMode = false;
                foreach (ButtonGrid bg in _list.Where(b => b.GetType() == typeof(ButtonGrid)))
                {
                    bg.Used = false;
                }
            }
        }
        //Metodo utilizato per invertire le icone
        private void OperationInvert(IButton button)
        {
            if (button.GetType() == typeof(ButtonWithLabel))
            {
                button.Activated = true;
                _invertedIcon = !_invertedIcon;
            }
        }
    }
}
