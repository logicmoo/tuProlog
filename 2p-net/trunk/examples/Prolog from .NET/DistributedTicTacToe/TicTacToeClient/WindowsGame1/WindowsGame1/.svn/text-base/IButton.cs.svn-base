using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace TicTacToeGame
{
    interface IButton
    {
        bool isInside(int x ,int y);                //Controlla se il punto x,y è all'interno il bottone
        Rectangle Rectangle { get; }                //Proprietà che indica la posizione e la dimensione dell'oggetto
        Texture2D Texture { get; set; }             //Proprietà della texture usata dal bottone
        Action<IButton> Operation { get; set; }     //Proprietà che definisce il delegato che può essere assegnato al pulsante
        bool Activated { get; set; }                //Proprietà che indica se il bottone è attivato o no
    }
}
