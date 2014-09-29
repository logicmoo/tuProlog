using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using alice.tuprolog;
using DerivateNamespace;

namespace PrologTestSolution
{
    public partial class GUI : Form
    {
        private Prolog prolog;
        public GUI()
        {
            InitializeComponent();
        }

        private void InputButton_Click(object sender, EventArgs e)
        {
            OutputTextBox.Clear();
            try
            {
                if (prolog == null)
                {
                    labelError.Text = "Please Select Theory";
                    return;
                }
                if (prolog is Perm)
                {
                    Term goal = new Struct("perm",Term.createTerm(InputTextBox.Text),new Var("R"));
                    SolveInfo SI = prolog.solve(goal);

                    if (!SI.isSuccess())
                        OutputTextBox.AppendText("no");

                    while (SI.isSuccess())
                    {
                        OutputTextBox.AppendText(SI.getTerm("R").toString() + "\n");
                        if (prolog.hasOpenAlternatives())
                            SI = prolog.solveNext();
                        else
                            break;
                    }
                }
                else if (prolog is PrologDerivate)
                {
                    Term goal = new Struct("dExpr", Term.createTerm(InputTextBox.Text), new Var("DT"));
                    SolveInfo SI = prolog.solve(goal);

                    if (!SI.isSuccess())
                        OutputTextBox.AppendText("no");
                    else
                        OutputTextBox.AppendText(SI.getTerm("DT").toString() + "\n");
                        
                }
                
            }
            catch (MalformedGoalException ex) { 
                OutputTextBox.AppendText("Malformed Goal\n"); 
            }
        }

        public List<String> Teorie { 
            get { return new List<string> {"a","b" }; } 
        }

        private void TheoryChanged(object sender, EventArgs e)
        {
            switch (((ComboBox)sender).SelectedItem.ToString())
            {
                case "Permutazioni": 
                    prolog = new Perm(); 
                    labelError.Text = "Use: [1,2,3]";
                    break;
                case "Derivate":
                    try
                    {
                        prolog = new PrologDerivate();
                        labelError.Text = "";
                    }catch{
                        labelError.Text = "Cannot find prolog source";
                    }
                    break;
            }
        }
    }
}
