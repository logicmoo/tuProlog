using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using alice.tuprolog;
using java.io;

namespace DerivativesNamespace {
	 public class PrologDerivatives : Prolog { 
			 public PrologDerivatives(){ 
					 setTheory(new Theory(new FileInputStream("Der.pl"))); 
		} 

	 } 
}