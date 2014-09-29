using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using alice.tuprolog;
using java.io;

namespace DerivateNamespace {
	 public class PrologDerivate : Prolog { 
			 public PrologDerivate(){ 
					 setTheory(new Theory(new FileInputStream("Der.pl"))); 
		} 

	 } 
}