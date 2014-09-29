package alice.tuprologx.android;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import dalvik.system.DexClassLoader;

import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.Library;
import alice.tuprolog.Prolog;
import alice.tuprolog.event.LibraryEvent;
import alice.tuprolog.event.LibraryListener;

/**
 * @author Alessio Mercurio
 *
 * Android LibraryManager
 */

public class LibraryManager implements LibraryListener
{
	/**
	 * The Prolog engine referenced by the Library Manager. 
	 */
	private Prolog engine;
	
	/**
	 * Stores classnames for managed libraries.
	 */
	private ArrayList<String> libraries;
	private Hashtable<String, String> externalLibraries = new Hashtable<String, String>();
	
	private String optimizedDirectory;

	public LibraryManager()
	{
		libraries = new ArrayList<String>();
	}

	/**
	 * Set the engine to be referenced by the library manager.
	 * @param engine  The engine to be referenced by the library manager.
	 */
	public void setEngine(Prolog engine)
	{
		this.engine = engine;
		initialize();
	}

	/**
     * Initialize the repository for managed libraries using the
     * standard libraries which come loaded with the tuProlog engine.
     */
	public void initialize()
	{
		CUIConsole.engine.addLibraryListener(this);
		
		String[] loadedLibraries = engine.getCurrentLibraries();

		for (int i = loadedLibraries.length - 1; i >= 0; i--)
		{
			libraries.add(loadedLibraries[i]);
		}
	}

	/**
	 * Get the engine referenced by the library manager.
	 * @return  the engine referenced by the library manager.
	 */
	public Prolog getEngine()
	{
		return engine;
	}

	/**
     * Check if a library is loaded into the Prolog engine.
     *
     * @param libraryClassname The complete name of the library class to check.
     * @return true if the library is loaded into the engine, false otherwise.
     */
	public boolean isLibraryLoaded(String libraryClassname)
	{
		return (engine.getLibrary(libraryClassname) != null);
	}

	/**
     * Add a library to the manager.
     *
     * @param libraryClassname The name of the library to be added.
     * @throws ClassNotFoundException if the library class cannot be found.
     * @throws InvalidLibraryException if the library is not a valid tuProlog library.
     */
	public void addLibrary(String libraryClassname) throws ClassNotFoundException, InvalidLibraryException 
	{
        if (libraryClassname.equals(""))
            throw new ClassNotFoundException();
        /** 
         * check for classpath without uppercase at the first char of the last word
         */
        StringTokenizer st=new StringTokenizer(libraryClassname,".");
        String str=null;
        while(st.hasMoreTokens())
            str=st.nextToken();
        if ((str.charAt(0)>'Z') ||(str.charAt(0)<'A'))
            throw new ClassNotFoundException();

        Library lib = null;
        try
        {
        	lib = (Library) Class.forName(libraryClassname).newInstance();
        	libraries.add(lib.getName());
        }
        catch(Exception ex)
        {
        	throw new InvalidLibraryException(libraryClassname,-1,-1);
        }
    }
	
	/**
     * Add a library to the manager.
     *
     * @param libraryClassname The name of the library to be added.
     * @param dexPath jar file path where is contained the library.
     * @throws ClassNotFoundException if the library class cannot be found.
     * @throws InvalidLibraryException if the library is not a valid tuProlog library.
     */
	public void addLibrary(String libraryClassname, String dexPath) throws ClassNotFoundException,
			InvalidLibraryException
	{
		if (libraryClassname.equals(""))
			throw new ClassNotFoundException();

		Library lib = null;
		ClassLoader loader = null;
		
		try
		{			
			/**
		     * A class loader that loads classes from .jar files containing a classes.dex entry. 
		     * This can be used to execute code not installed as part of an application.
		     * @param dexPath jar file path where is contained the library.
		     * @param optimizedDirectory directory where optimized dex files should be written; must not be null
		     * @param libraryPath the list of directories containing native libraries, delimited by File.pathSeparator; may be null
		     * @param parent the parent class loader
		     */
			loader = new DexClassLoader(dexPath, this.getOptimizedDirectory(), null, getClass().getClassLoader());

			//lib = (Library) loader.loadClass(libraryClassname).newInstance();
			lib = (Library) Class.forName(libraryClassname, true, loader).newInstance();
			libraries.add(lib.getName());
			externalLibraries.put(libraryClassname,	dexPath);
		}
		catch (Exception ex)
		{
			throw new InvalidLibraryException(libraryClassname, -1, -1);
		}
	}

	 /**
     * Remove a library to the manager.
     *
     * @param libraryClassname The name of the .class of the library to be removed.
     * @throws ClassNotFoundException if the library class cannot be found.
     * @throws InvalidLibraryException if the library is not a valid tuProlog library.
     */
	public void removeLibrary(String libraryClassname)
			throws InvalidLibraryException
	{
		libraries.remove(libraryClassname);
	}

	/**
     * Get the libraries managed by the library manager.
     *
     * @return The libraries managed by the library manager as an array of
     * <code>Object</code>s.
     */
	public Object[] getLibraries()
	{
		return libraries.toArray();
	}

	public void setLibraries(ArrayList<String> libraries)
	{
		this.libraries = libraries;
	}

	public void resetLibraries()
	{
		this.libraries = new ArrayList<String>();
	}

	public String toString()
	{
		String result = "";
		Object[] array = getLibraries();
		for (int i = 0; i < array.length; i++)
		{
			result = result + array[i] + "\n";
		}
		return result;
	}

	 /**
     * Load a library from the Library Manager into the engine.
     *
     * @param libraryClassname The library to be loaded into the engine.
     * @param dexPath jar file path where is contained the library.
     * @throws ClassNotFoundException
     * @throws InvalidLibraryException
     */
	public void loadLibrary(String libraryClassname, String dexPath) throws InvalidLibraryException,
			ClassNotFoundException
	{
		engine.loadLibrary(libraryClassname, new String[]{dexPath});
	}

	 /**
     * Unload a library from the Library Manager out of the engine.
     *
     * @param library The library to be unloaded out of the engine.
     * @throws InvalidLibraryException
     * @throws EngineRunningException
     */
	public void unloadLibrary(String library) throws InvalidLibraryException
	{
		engine.unloadLibrary(library);
	}

	public void unloadExternalLibrary(String library)
			throws InvalidLibraryException
	{
		if (externalLibraries.containsKey(library))
			externalLibraries.remove(library);
		if (engine.getLibrary(library) != null)
			engine.unloadLibrary(library);
	}

	/**
    * Check if a library is contained in the manager.
    * 
    * @param library The name of the library we want to check the load status on.
    * @since 1.3.0
    */
	public boolean contains(String library)
	{
		return libraries.contains(library);
	}

	public synchronized String getExternalLibraryPath(String name)
	{
		return isExternalLibrary(name) ? externalLibraries.get(name) : null;
	}

	public synchronized boolean isExternalLibrary(String name)
	{
		return externalLibraries.containsKey(name);
	}

	public String getOptimizedDirectory()
	{
		return optimizedDirectory;
	}

	public void setOptimizedDirectory(String optimizedDirectory)
	{
		this.optimizedDirectory = optimizedDirectory;
	}


	/** @see alice.tuprolog.event.LibraryListener#libraryLoaded(alice.tuprolog.event.LibraryEvent) */
	public void libraryLoaded(LibraryEvent event)
	{
		String libraryName = event.getLibraryName();
		if (!contains(libraryName))
		{
			try
			{
				alice.tuprolog.LibraryManager mainLibraryManager = getEngine().getLibraryManager();
				if (mainLibraryManager.isExternalLibrary(libraryName))
				{
					URL url = mainLibraryManager
							.getExternalLibraryURL(libraryName);

					addLibrary(libraryName, url.getPath());
				}
				else
				{
					addLibrary(libraryName);
				}
			} catch (ClassNotFoundException e)
			{
				
			} catch (InvalidLibraryException e)
			{
				
			}
		}
	}

	/** @see alice.tuprolog.event.LibraryListener#libraryUnloaded(alice.tuprolog.event.LibraryEvent) */
	public void libraryUnloaded(LibraryEvent event)
	{
		try
		{
			removeLibrary(event.getLibraryName());
		} catch (InvalidLibraryException e)
		{
			
		}
	}
}
