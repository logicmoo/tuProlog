README file for JADE-S, the JADE Security Plug-in.


INTRODUCTION
============

This package contains the JADE Security Plug-in.
JADE-S is formed by the combination of the standard version of JADE with 
this JADE Security plug-in. It includes features such as user/agent 
authentication, authorization and secure communication.


LICENSE
=======
This plug-in is distributed by TILAB under the LGPL license.
See file COPYING for details.
The package jade.security.util.DES of this plug-in uses the file Crypt.java 
by Michael Glad and Pawel Veselov distributed itself under the LGPL license.
Included library BouncyCastle licence is into: security/lib/bc/license.txt


SYSTEM REQUIREMENTS
===================
In order to use the JADE security plug-in you need JADE 3.2 or later, 
and Sun Java SDK 1.4 or later. 
The file jadeSecurity.jar, which contains the JADE security plug-in, needs 
to be in your CLASSPATH variable.


INSTALLATION
============
Unpack the file jadesecurity.zip in the same location of your current JADE 
installation. All the needed files will be created into the directory 
jade/add-ons/security. 
In addition to all JADE jar files, you also need to add 
jade/add-ons/security/lib/jadeSecurity.jar to your Java CLASSPATH. 


USING JADE-S
============
Detailed explaination of JADE-S is provided in the 
JADE Security Guide, available at:
http://jade.tilab.com/doc/


EXAMPLES
========
Several working examples are in the directory: jade/add-ons/security/examples 
If you have Ant installed on your system, you can start each example 
just by typing 'ant'. (the build.xml file contains the starting class and parameters).
Additionally, Windows users can also use the .bat files, where present, 
into the various examples sub-directories.


---
by Giosue.Vitaglione@tilab.com
2004-07-12
