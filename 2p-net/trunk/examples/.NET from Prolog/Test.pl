/* If the OOLibrary is not loaded by default follow the following steps: 
*	1) remove (or unload) the JavaLibrary
*	2) load the OOLibrary using this string "OOLibrary.OOLibrary, OOLibrary" and use the "Browse" button to select the correct dll file.
*/

/* Basic conventions available (lib/Conventions.dll):
*	- Conventions.CSharpConvention
*	- Conventions.FSharpConvention
*	- Conventions.JavaConvention
*	- Conventions.VBDotNetConvention
*/

% In this test you can see how to use basic .net constructs (objects, methods, properties, ...) from tuProlog
% using the right convention for the member names
testBasicsWithConvention :-
	% The c# convention is loaded so you can write the methods and property name in natuaral way in Prolog
	% without using the quotes ('')
	load_convention('lib/Conventions.dll','Conventions.CSharpConvention',CSConv),
	
	% The name of the class must be the Assembly Qualified Name.
	% The third argument is the arguments list to pass to the constructor.
	% It is possible to load classes that are reachable from the standard .net loading criteria 
	% (http://msdn.microsoft.com/en-us/library/yx7xezcf.aspx), in the near feature will be possible to load classes
	% from any location.
	new_object(CSConv,'CStudent.Student, CStudent',[311471,'ale','monta'], Student),

 	/* INSTANCE METHOD INVOCATION  */
 	Student <- printStudent returns StudentInfo,
 	write(StudentInfo),
 	
 	/* INSTANCE METHOD INVOCATION (WITH ARGUMENTS)  */
	Student <- signExam(30) returns Comment,
	write(Comment),
 	
 	/* STATIC METHOD INVOCATION  */
	class('CStudent.Student, CStudent') <- printInfoUni returns UniInfo,
 	write(UniInfo),
 	
 	/* As you can see below using a property or a field (static or of an instance) is quite similar,
 	*  the main difference is that to set a value you have to use the pseudo-method set and instead to
 	*  get the value you have to use the pseudo-method get.
 	*  Another difference is that for field of type String you have to call toString() to the value before
 	*  to print it with write/1, this is not necessary for propertis of type String.
 	*/
 	 /* PROPERTY GET */
 	Student.id <- get(Id),
 	write(Id),
 	
 	/* PROPERTY SET */
 	Student.id <- set(1234),
	Student.id <- get(NewId),
	write(NewId),

	/* INSTANCE FIELD GET */
	Student.publicField <- get(Field),
	Field <- toString returns FieldStr,
	write(FieldStr),

	/* INSTANCE FIELD SET */
 	Student.publicField <- set('Hello world!'),
	Student.publicField <- get(Field2),
	Field2 <- toString returns FieldStr2,
	write(FieldStr2),

	 /* STATIC FIELD */
	class('CStudent.Student, CStudent').publicStaticField <- get(StaticField),
	StaticField <- toString returns StaticFieldStr,
	write(StaticFieldStr),
	class('CStudent.Student, CStudent').publicStaticField <- set('Hello world from static field!'),
	class('CStudent.Student, CStudent').publicStaticField <- get(StaticField2),
	StaticField2 <- toString returns StaticFieldStr2,
	write(StaticFieldStr2),

 	 /* STATIC PROPERTY */
 	class('CStudent.Student, CStudent').staticProperty <- set('Hello world from static property!'),
 	class('CStudent.Student, CStudent').staticProperty <- get(StaticProperty),
 	write(StaticProperty),

 	/* ARRAY */
 	% To create an array you have to use square brackets in the name of the class and pass the 
 	% lenght of the array as argument.
 	% In this case we are using the C# Convention so we can use the sqaure brackets but if we use the VB.NET
 	% is possible to use the classic parentheses "()" in accordance to the language specidication.
 	% new_object/4 predicate creates only the array but the single elements are initialized  to null
 	new_object(CSConv, 'CStudent.student, CStudent[]',[10],StudentsArray),
 	
 	% with array_set/3 and array_get/3 is possible to insert a value inside the array or get a specified value
 	% from the array respectively.
 	% In both cases the first argument is the array, the second argument is the index where to set the value
 	% (or from where get the value) and the third is the value to set (or the value obtained from the array)
 	array_set(StudentsArray,1,Student),
 	array_get(StudentsArray, 1, Student1),
 	Student1.name <- get(Name),
 	write(Name),
 	
 	% with array_length/2 is possible to obtain (or check) the lenght of the array passed as first parameter
 	array_length(StudentsArray, Length),
 	write(Length),
 	
 	/* ARRAY IN VB.NET*/
 	% As mentioned earlier is possible to create VB.NET array using parentheses witn the right Convention
 	load_convention('lib/Conventions.dll','Conventions.VBDotNetConvention',VBNETConv),
	new_object(VBNETConv,'VBStudent.Student, VBStudent',[311471,'joe','smith'], VBStudent),
	new_object(VBNETConv, 'VBStudent.Student, VBStudent()',[20],VBStudentsArray),
	array_set(VBStudentsArray,1,VBStudent),
 	array_get(VBStudentsArray, 1, VBStudent1),
 	VBStudent1.name <- get(VBName),
 	write(VBName),
 	
 	array_length(VBStudentsArray, VBLength),
 	write(VBLength),
	
	% with destroy_object/1 is possible to remove the binding between the .net object 
	% and the Prolog Term specified
 	destroy_object(Student),	
 	destroy_object(VBStudent),
 
 	unload_convention(VBNETConv),
 	unload_convention(CSConv).
 	
% In this test you can see how to use basic .net constructs without the convention
testBasicsWithoutConvention :-
	new_object(_,'VBStudent.Student, VBStudent',[311471,'joe','smith'], VBStudent),
	
	% In method call you have to use the exact name of the method using quotes ('')
	VBStudent <- 'PrintStudent' returns StudentInfo,
	write(StudentInfo),
	
	% Since .net compiler compiles properties into methods to use a property you have to know the corresponding
	% methods name. In this case: get_<prop name> and set_<prop name>.
	VBStudent <- get_Id returns Id,
	write(Id),
	
	class('VBStudent.Student, VBStudent') <- get_StaticProperty returns StaticProperty,
	write(StaticProperty),
	
	% For the array you have to use square brackets "[]" even if it is not right in VB.NET (usually you use
	% parentheses)
	new_object(VBNETConv, 'VBStudent.Student, VBStudent[]',[20],VBStudentsArray),
	array_length(VBStudentsArray, VBLength),
 	write(VBLength),
 	
 	destroy_object(VBStudent).
 	

% Since tuProlog.NET is built using IKVM.NET it enables to load and use a java class (.class or .jar) in
% the .NET platform. The java classes are compiled in CIL on the fly by IKVM.NET when they are used.
testBasicsWithJava :-
 	load_convention('lib/Conventions.dll','Conventions.JavaConvention',JavaConv),
 	
 	% Notice the use of the new predicate set_classpath/1 that permits to specify a list of directories that has to be used to search the classes
 	% Refer to the manual for detailed informations
 	set_classpath(['examples']),
 	
 	% The name of the class can starts with a lower case: the Java Convention will capitalize it
 	new_object(JavaConv,'javastudent.student',[311471,'javaName','javaSurname'],JavaStudent),
 	
 	% Following the java language convention the methods name start with a lower case letter so in this case
 	% there is no problem to call the printStudent method.
 	JavaStudent <- printStudent returns StudentInfo,
 	write(StudentInfo),

	% However the convention can help in case you write first letter in upper case: in this case the convention
	% converts it into lower case.
 	JavaStudent <- 'PrintStudent' returns StudentInfo2,
 	write(StudentInfo2),

	% Java doesn't have the concept of property like in .NET languages so we use method to get the Id
	JavaStudent <- getId returns Id,
	write(Id),
	
	class('javastudent.Student') <- printInfoUni returns InfoUni,
	write(InfoUni),
	
	new_object(JavaConv,'javastudent.student[]',[5],JavaArray),
	array_length(JavaArray, JavaArrayLength),
	write(JavaArrayLength),

 	destroy_object(JavaStudent),
 	
 	unload_convention(JavaConv).

% In this test is possible to see how to use at the same time objects written in C#, VB.NET, F# and Java
testInteroperation(Tot) :- 
	load_convention('lib/Conventions.dll','Conventions.JavaConvention',JavaConv),
	load_convention('lib/Conventions.dll','Conventions.CSharpConvention',CSConv),
	load_convention('lib/Conventions.dll','Conventions.FSharpConvention',FSConv),
	load_convention('lib/Conventions.dll','Conventions.VBDotNetConvention',VBConv),
	
	set_classpath(['examples']),
	new_object(JavaConv,'javastudent.student',[311471,'javaName','javaSurname'],JavaStudent),
	new_object(CSConv,'CStudent.Student, CStudent',[311471,'ale','monta'], CSStudent),
	new_object(VBConv,'VBStudent.Student, VBStudent',[311471,'joe','smith'], VBStudent),
	new_object(FSConv,'FStudent.Student, FStudent',[311471,'ale','monta'], FSStudent),
	
	JavaStudent <- getExams returns Ex1,
	CSStudent.exams <- get(Ex2),
	VBStudent.exams <- get(Ex3),	
	FSStudent.exams <- get(Ex4),	
	
	Tot is Ex1 + Ex2 + Ex3 + Ex4,
	
	destroy_object(JavaStudent),
	destroy_object(CSStudent),
	destroy_object(VBStudent),
	destroy_object(FSStudent),
	
	unload_convention(JavaConv),
	unload_convention(CSConv),
	unload_convention(FSConv),
	unload_convention(VBConv).
	
% In this test is possible to see that from the .net platform is still possible to compile a java class and use it.
% Note: the java class is compiled on the fly in CIL by IKVM.NET before the use.
% .java and .class files will be created in the root directory of the application
testJavaDynamicCompilation :-
	java_class('public class MyProgram {public String showFileChooser(String title) { javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(); chooser.setDialogTitle(title); chooser.showOpenDialog(null); java.io.File file = chooser.getSelectedFile(); return file.getName();}}','MyProgram',[],Cl),
	new_object('java.lang.String',['Select a file from tuProlog!'], Message),
	Cl <- newInstance returns Object,
	Object <- showFileChooser(Message) returns FileName,
	write(FileName).
	
	
% Since tuProlog.NET is compiled using IKVM.NET is possible to use all types in the JDK
testJavaTypes :-
	new_object('java.util.StringTokenizer', ['This is my string'],Tokenizer),
	Tokenizer <- nextToken returns Token1,
	write(Token1).