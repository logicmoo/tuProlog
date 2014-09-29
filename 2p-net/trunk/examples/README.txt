NOTE ABOUT THE USE OF .NET TYPES FROM TUPROLOG.NET
The .NET types can be loaded using Prolog from those assemblies that are
reachable using the standard steps adopted by the CLR to locate
assemblies (http://msdn.microsoft.com/en-us/library/yx7xezcf.aspx) or using the set_classpath/1 predicate.

In this distribution, the assemblies contained in CStudent.dll, VBStudent.dll
and FStudent.dll can be loaded without problems because in the application
configuration file (2p.exe.config) is inserted the "examples\.NET from Prolog"
directory into the privatePath, so the CLR can reach and load them, instead for lading the javastudent class it is demonstrated the use of the predicate set_classpath/1 (refer to the manual for mor information on this predicate).


NOTE ABOUT THE USE OF JAVA TYPES FROM TUPROLOG.NET
From Prolog is possible to use almost all java types that are present in the OpenJDK because they are provided by IKVM.NET (rember that those types are implemented in .NET by IKVM.NET).