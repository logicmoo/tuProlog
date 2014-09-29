using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using alice.tuprolog;

namespace VBPrologLibrary {
	 public class Theory1 : Prolog {
 
		 private string th = @"

"; 

		 public Theory1(){ 
					 setTheory(new Theory(th)); 
		} 

	 } 
}