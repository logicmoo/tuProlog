using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using alice.tuprolog;

namespace DerivativeExample
{
    public partial class Form1 : Form
    {
        Prolog engine = null;

        public Form1()
        {
            InitializeComponent();

            engine = new Prolog();
            try
            {
                Theory t = new Theory(new java.io.FileInputStream(@"..\..\..\der.pl"));
                engine.setTheory(t);
            }
            catch (InvalidTheoryException e1)
            {
                Console.WriteLine("Theory not valid.");
                Application.Exit();
            }
            catch (java.io.IOException e2)
            {
                Console.WriteLine("Errors in reading from file.");
                Application.Exit();
            }
        }

        private void btnOk_Click(object sender, EventArgs e)
        {
            String inputString = inputTextBox.Text;
            try
            {
                Term goal = new Struct("dExpr", Term.createTerm(inputString), new Var("Der"));
                SolveInfo answer = engine.solve(goal);
                if (answer.isSuccess())
                {
                    Term derivata = answer.getTerm("Der");
                    lblRes.Text = derivata.toString();
                }
                else
                {
                    lblRes.Text = "Error: no solution";
                }
            }
            catch (InvalidTermException e3)
            {
                Console.WriteLine("The esxpression is not a valid Prolog Term.");
            }
            catch (UnknownVarException e4)
            {
                Console.WriteLine("Variable not valid.");
            }
            catch (NoSolutionException e5)
            {
                Console.WriteLine("No solutions.");
            }
        }

        private void btnAnnulla_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
