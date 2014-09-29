using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System.Reflection;
namespace TicTacToeGame
{
    //Estende la classe SpriteBatch per poter disegnare la gerachia di classi Button
    class SpriteBatchExt : SpriteBatch
    {
        
        public SpriteBatchExt(GraphicsDevice gd) : base(gd) { }
        public void DrawButton(IButton button, Color color)
        {
            Type typeObject = button.GetType();
            MethodInfo methodInfo = null;
            IEnumerable<MethodInfo> methodInfos=this.GetType().GetMethods().Where(mi => mi.Name=="DrawButton");
            foreach (MethodInfo mi in methodInfos) 
            {
                if (mi.GetParameters()[0].ParameterType == typeObject)
                {
                    methodInfo = mi;
                }
            }
            methodInfo.Invoke(this, new object[] { button, color });
        }
        public void DrawButton(ButtonWithLabel button,Color color)//Disegna un ButtonWithLabel
        {
            base.Draw(button.Texture, button.Rectangle, Color.White);
            base.DrawString(button.Font, button.Text, button.Position, color);
        }
        public void DrawButton(ButtonGrid button, Color color)//Disegna un ButtonGrid
        {
            base.Draw(button.Texture, button.Rectangle, Color.White);
           
        }
    }
}
