using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;

namespace TicTacToeGame
{
    //Classe astratta per descrivre le funzionalità comuni alle sottoclassi
    abstract class Button : IButton
    {
        private Action<IButton> _operation;
        private bool _activated;
        private Texture2D _texture;

        public bool isInside(int x, int y)
        {
            return ((Rectangle.Left <= x) && (Rectangle.Right >= x) && (Rectangle.Top <= y) && (Rectangle.Bottom >= y));
        }
        abstract public Rectangle Rectangle { get; }

        public Action<IButton> Operation
        {
            get { return _operation; }
            set { _operation += value; }
        }
        public bool Activated
        {
            get { return _activated; }
            set { _activated = value; }
        }
        public Texture2D Texture
        {
            get { return _texture; }
            set { _texture = value; }
        }
    }
    //Modella un bottone con un testo al suo interno
    class ButtonWithLabel:Button
    {
        private string _label;        //Indica il testo che dovrà essere visualizato nel bottone
        private Vector2 _position;
        private SpriteFont _font;
       

        public SpriteFont Font
        {
            get { return _font; }
            set { _font = value; }
        }
        
        public Vector2 Position
        {
            get { return _position; }
            set { _position = value; }
        }
        public Vector2 Dimension
        {
            get { return _font.MeasureString(Text); }
        }
        public override Rectangle Rectangle
        {
            get {
                return new Rectangle((int)Position.X, (int)Position.Y, (int)Dimension.X, (int)Dimension.Y);
            }
        }
        public string Text
        {
            get { return _label; }
            set { _label = value; }
        }
        public ButtonWithLabel(SpriteFont font, String text,Vector2 position,Texture2D texture)
        {
            Font = font;
            Text = text;
            Position = position;
            Texture = texture;
        }        
    }
    //Buttone che modella una cesella della griglia 
    class ButtonGrid:Button
    {           
        private Rectangle _rectangle;
        private int _place;//Indica la posizione relativa al bottone
        private bool _used;//Indica se il bottone è già stato premuto
        public ButtonGrid(Texture2D texture, Rectangle rectangle,int place)
        {
            Texture = texture;
            _rectangle = rectangle;
            _place = place;            
        }
        public int Place 
        {
            get { return _place; }
        }
        
        public override Rectangle Rectangle
        {
            get { return _rectangle; }            
        }
        public void SetRectangle(Rectangle rect)
        {
            _rectangle = rect;
        }
        public bool Used
        {
            get { return _used; }
            set { _used = value; }
        }
    }
    
}
