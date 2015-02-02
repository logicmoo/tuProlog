using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using alice.tuprolog;
using DerivativesNamespace;

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
            try
            {
                if (prolog == null)
                {
                    labelError.Text = "Please select a Theory";
                    return;
                }
                if (InputTextBox.Text.Length == 0)
                {
                    OutputTextBox.Text = "Please insert a Goal";
                    return;
                }

                OutputTextBox.Clear();
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
                else if (prolog is PrologDerivatives)
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
            catch (InvalidTermException ex) { 
                OutputTextBox.AppendText("Malformed Goal\n"); 
            }
        }

        public List<String> Theories { 
            get { return new List<string> {"a","b" }; } 
        }

        private void TheoryChanged(object sender, EventArgs e)
        {
            switch (((ComboBox)sender).SelectedItem.ToString())
            {
                case "Permutations": 
                    prolog = new Perm(); 
                    labelError.Text = "Usage: [x,y,z, ... ]";
                    break;
                case "Derivatives":
                    try
                    {
                        prolog = new PrologDerivatives();
                        labelError.Text = "";
                    }catch{
                        labelError.Text = "Cannot find prolog source";
                    }
                    break;
            }
        }

        private void InputTextBox_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
                InputButton.PerformClick();
        }
    }
}
