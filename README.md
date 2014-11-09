# **Focus** #

tuProlog is a light-weight Prolog system for distributed applications and infrastructures, intentionally designed around a minimal core (containing only the most essential properties of a Prolog engine), to be later configured by (statically and dynamically) loading/unloading libraries of predicates. tuProlog also natively supports multi-paradigm programming, providing a clean, seamless integration model between Prolog and mainstream object-oriented languages -- namely Java, for tuProlog Java version, and any .NET-based language (C#, F#..), for tuProlog .NET version. It is also easily deployable, just requiring the presence of a Java/CLR virtual machine and an invocation upon a single self-contained archive file. Interoperability is further developed along the two main lines of Internet standard patterns and coordination models.

tuProlog and related packages are released under the GNU Lesser General Public License, so no payment is required for using it. However, we welcome donations (see menu aside).

**Current version: tuProlog 2.9.1 (released on October 16, 2014)**

## **Highlights** ##

This version is intended to be the last stable version supporting Java 7: the next major release, tuProlog 3.0, will include support for lambda expressions and therefore require Java 8.

### **Bug fixes ** ###
* the behaviour of setof / bagof has been finally fixed, correcting the old-dated bug causing variable bindings to be erroneously lost in some corner cases;
* the behaviour of the disjunction predicate (’;’) has also been fixed, correcting the other old-dated bug causing the disjunction to be associated in the wrong way in some specific corner cases;
* the behaviour of the functor predicate, previosly entering an endless loop in the corner case when the ?rst argument was unbound and the second referred to the term itself, has also been fixed.

### **New features**###
* improved class loading in both JavaSE and Android editions, enabling external resources to be loaded on the Android platform, too;
* improved support to keyboard input on the Java IDE; a new input tab has been added to the console, replacing the old-fashioned input dialog;
* compliance with the Java scripting engine specification (JSR-233), with new Java methods to explicitly retrieve and set the standard I/O streams.

### **Download** ###
All tuProlog versions are now available from this site
except for the Eclipse plugin which must be downloaded and installed directly from the Eclipse Update Manager - see detailed instructions.

Java: tuProlog 2.9.0 (zip) (requires Java 7)
.NET:tuProlog 2.9-0-NET (zip) (requires .NET framework 4)
Android:tuProlog 2.9.0 Android (apk) (requires Android 2.3.3)
Eclipse:  install from Eclipse Update Manager -- see below for details.
MANUAL: tuProlog 2.9.0 User Guide (pdf)

### **Installation** ###

**Java version
**Just unzip the release archive in a folder of your choice, and double click the executable 2p.jar

**.NET version**
Just unzip the release archive in a folder of your choice, and double click the executable 2p.exe

**Android version**
Just copy the 2p-android.apk archive in your device (e.g. in the download folder) and double click to start installation. Please remove any previous versions first, otherwise the install process may fail.

**Eclipse plugin**
Installation must be performed via the Eclipse Update Manager: to this end, the proper Update Site must be configured first - see the detailed configuration instructions.

**Please note:** due to a renaming of the plugin deployment package, the Eclipse Check for Update feature will NOT be able to upgrade the plugin from version 2.6 or earlier versions -- please use the Install new software feature instead, manually looking for new software in the tuProlog update site and looking the new tuProlog feature.

**Eclipse requirements:** tuProlog 2.9 (and 2.8, 2.7) requires Eclipse Juno or Kepler SR1, older tuProlog versions require Eclipse Indigo. A compatibility issue exists for Eclipse Kepler SR2 and Luna, where internal core components (parts of the Lucene package) have been unexpectedly removed. The lack of such components will prevent the tuProlog plugin (all versions but 2.9) to install unless the two missing packages are manually copied into the ECLIPSE/plugins directory. We are working to patch this issue in tuProlog 2.9: this is why this plugin is NOT currently available yet.

**Numbering scheme**
The numbering scheme adopted aims to provide a clear cross-platform view of the version being used.

The first two digits represent the engine version. So, as long as you see that the same two digits are the same, you can count that the inner baheviour will be the same, too.
Coherently with this approach, major versions are normally released as 2.N.0 for all platforms.
The subsequent digits (usually, just the third one, possibly followed by some build indication) is platform specific and will be used to distinguish between different versions of platform-specific items (such as IDEs, plug-in IDEs, Android UIs, etc.)