# **Focus** #

**tuProlog and related packages are released under the GNU Lesser General Public License, so no payment is required for using it.**

tuProlog is a light-weight Prolog system for distributed applications and infrastructures, intentionally designed around a minimal core (containing only the most essential properties of a Prolog engine), to be later configured by (statically and dynamically) loading/unloading libraries of predicates. tuProlog also natively supports multi-paradigm programming, providing a clean, seamless integration model between Prolog and mainstream object-oriented languages -- namely Java, for tuProlog Java version, and any .NET-based language (C#, F#..), for tuProlog .NET version. It is also easily deployable, just requiring the presence of a Java/CLR virtual machine and an invocation upon a single self-contained archive file. Interoperability is further developed along the two main lines of Internet standard patterns and coordination models.

**Current version: tuProlog 3.3.0 (released on September 26, 2018)**

## **Requirements** ##

The required platform for tuProlog 3 is Java 8 *with JDK* (not just a pure JRE): users not interested in the new_lambda/3 predicate, however, can go for a standard JRE.

tuProlog 2.9.2 is intended to be the last stable version supporting Java 7.

The Eclipse plugin works on Eclipse Neon and Oxygen (64 bit).
             
The Android app works on Android 5.0.1 and above.

### **New features version 3.3.0**###
This version aims to provide a stable environment

* the new eclipse plugin;

* the new android application.

### **Download** ###
All tuProlog versions are now available from this site
except for the Eclipse plugin which must be downloaded and installed directly from the Eclipse Update Manager - see detailed instructions.

### **Installation** ###

**Java version
**Just unzip the release archive in a folder of your choice, and double click the executable 2p.jar To exploit the new_lambda/3 predicate, please launch tuProlog as "javaw -jar 2p.jar", after setting the system path to the JDK -- otherwise, most operating systems will use a JRE, which does not contain the Java compiler, causing the new primitive to fail. We suggest that you set up a desktop shortcut icon with the proper launch command.

**.NET version**
Just unzip the release archive in a folder of your choice, and double click the executable 2p.exe

**Android version**
Just copy the apk archive in your device (e.g. in the download folder) and double click to start installation. The new app can co-exist side by side with the previous one, as it is released under a different name.

**Eclipse plugin**
Installation must be performed via the Eclipse Update Manager, with the proper Update Site properly configured - see the detailed configuration instructions.
Please note that, due to the Eclipse installation policy, it is not possible to reinstall previous versions, or more generally to downgrade an installed plugin to an older version (unless re-installing from a fresh Eclipse install).

**tuProlog Numbering scheme**
The numbering scheme adopted aims to provide a clear cross-platform view of the version being used.

The first two digits represent the engine version. So, as long as you see that the same two digits are the same, you can count that the inner baheviour will be the same, too.
Coherently with this approach, major versions are normally released as 2.N.0 for all platforms.
The subsequent digits (usually, just the third one, possibly followed by some build indication) is platform specific and will be used to distinguish between different versions of platform-specific items (such as IDEs, plug-in IDEs, Android UIs, etc.)