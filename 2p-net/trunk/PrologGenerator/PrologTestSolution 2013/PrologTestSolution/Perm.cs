using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using alice.tuprolog;

namespace PrologTestSolution {
	 public class Perm : Prolog {
	  
	 private string th = @"
	 any([X|Xs],X,Xs).
any([X|Xs],E,[X|Ys]) :- any(Xs,E,Ys).
perm([],[]).
perm(Xs,[X|Ys]) :- any(Xs,X,Zs), perm(Zs,Ys).
"; 

		 public Perm(){ 
					 setTheory(new Theory(th)); 
		} 
	 } }