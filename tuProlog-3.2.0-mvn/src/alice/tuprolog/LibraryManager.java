/*
 * Created on 1-ott-2005
 *
 */
package alice.tuprolog;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import cli.System.Reflection.Assembly;

import alice.tuprolog.event.LibraryEvent;
import alice.tuprolog.event.WarningEvent;
import alice.util.AssemblyCustomClassLoader;


public class LibraryManager
{

	
	private ArrayList<Library> currentLibraries;

	private Prolog prolog;
	private TheoryManager theoryManager;
	private PrimitiveManager primitiveManager;
	private Hashtable<String, URL> externalLibraries = new Hashtable<String, URL>();

	
	private String optimizedDirectory;

	LibraryManager()
	{
		currentLibraries = new ArrayList<Library>();
	}

	
	void initialize(Prolog vm)
	{
		prolog = vm;
		theoryManager = vm.getTheoryManager();
		primitiveManager = vm.getPrimitiveManager();
	}

	
	public synchronized Library loadLibrary(String className)
			throws InvalidLibraryException
	{
		Library lib = null;
		try
		{
			lib = (Library) Class.forName(className).newInstance();
			String name = lib.getName();
			Library alib = getLibrary(name);
			if (alib != null)
			{
				if (prolog.isWarning())
				{
					String msg = "library " + alib.getName()
							+ " already loaded.";
					prolog.notifyWarning(new WarningEvent(prolog, msg));
				}
				return alib;
			}
		} catch (Exception ex)
		{
			throw new InvalidLibraryException(className, -1, -1);
		}
		bindLibrary(lib);
		LibraryEvent ev = new LibraryEvent(prolog, lib.getName());
		prolog.notifyLoadedLibrary(ev);
		return lib;

	}

	
	public synchronized Library loadLibrary(String className, String[] paths)
			throws InvalidLibraryException
	{
		Library lib = null;
		URL[] urls = null;
		ClassLoader loader = null;
		String dexPath;

		try
		{
			
			if (System.getProperty("java.vm.name").equals("Dalvik"))
			{
				/*
				 * Only the first path is used. Dex file doesn't contain .class files 
				 * and therefore getResource() method can't be used to locate the files at runtime.
				 */
				
				dexPath = paths[0];

				
				loader = (ClassLoader) Class.forName("dalvik.system.DexClassLoader")
											.getConstructor(String.class, String.class, String.class, ClassLoader.class)
											.newInstance(dexPath, this.getOptimizedDirectory(), null, getClass().getClassLoader());
				lib = (Library) Class.forName(className, true, loader).newInstance();
			} else
			{
				urls = new URL[paths.length];

				for (int i = 0; i < paths.length; i++)
				{
					File file = new File(paths[i]);
					if (paths[i].contains(".class"))
						file = new File(paths[i].substring(0,
								paths[i].lastIndexOf(File.separator) + 1));
					urls[i] = (file.toURI().toURL());
				}
				// JVM
				if (!System.getProperty("java.vm.name").equals("IKVM.NET"))
				{
					loader = URLClassLoader.newInstance(urls, getClass()
							.getClassLoader());
					lib = (Library) Class.forName(className, true, loader)
							.newInstance();
				} else
				// .NET
				{
					Assembly asm = null;
					boolean classFound = false;
					className = "cli."
							+ className.substring(0, className.indexOf(","))
									.trim();
					for (int i = 0; i < paths.length; i++)
					{
						try
						{
							asm = Assembly.LoadFrom(paths[i]);
							loader = new AssemblyCustomClassLoader(asm, urls);
							lib = (Library) Class.forName(className, true, loader).newInstance();
							if (lib != null)
							{
								classFound = true;
								break;
							}
						} catch (Exception e)
						{
							e.printStackTrace();
							continue;
						}
					}
					if (!classFound)
						throw new InvalidLibraryException(className, -1, -1);
				}
			}

			String name = lib.getName();
			Library alib = getLibrary(name);
			if (alib != null)
			{
				if (prolog.isWarning())
				{
					String msg = "library " + alib.getName()
							+ " already loaded.";
					prolog.notifyWarning(new WarningEvent(prolog, msg));
				}
				return alib;
			}
		} catch (Exception ex)
		{
			throw new InvalidLibraryException(className, -1, -1);
		}
		
		
		if(System.getProperty("java.vm.name").equals("Dalvik"))
		{
			try
			{
				/* 
				 * getResource() can't be used with dex files.  
				 */
				
				File file = new File(paths[0]);
				URL url = (file.toURI().toURL());
				externalLibraries.put(className, url);
			} 
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			externalLibraries.put(className, getClassResource(lib.getClass()));
		}
		
		bindLibrary(lib);
		LibraryEvent ev = new LibraryEvent(prolog, lib.getName());
		prolog.notifyLoadedLibrary(ev);
		return lib;
	}

	
	public synchronized void loadLibrary(Library lib)
			throws InvalidLibraryException
	{
		String name = lib.getName();
		Library alib = getLibrary(name);
		if (alib != null)
		{
			if (prolog.isWarning())
			{
				String msg = "library " + alib.getName() + " already loaded.";
				prolog.notifyWarning(new WarningEvent(prolog, msg));
			}
			unloadLibrary(name);
		}
		bindLibrary(lib);
		LibraryEvent ev = new LibraryEvent(prolog, lib.getName());
		prolog.notifyLoadedLibrary(ev);
	}

	
	public synchronized String[] getCurrentLibraries()
	{
		String[] libs = new String[currentLibraries.size()];
		for (int i = 0; i < libs.length; i++)
		{
			libs[i] = ((Library) currentLibraries.get(i)).getName();
		}
		return libs;
	}

	
	public synchronized void unloadLibrary(String name)
			throws InvalidLibraryException
	{
		boolean found = false;
		Iterator<Library> it = currentLibraries.listIterator();
		while (it.hasNext())
		{
			Library lib = (Library) it.next();
			if (lib.getName().equals(name))
			{
				found = true;
				it.remove();
				lib.dismiss();
				primitiveManager.deletePrimitiveInfo(lib);
				break;
			}
		}
		if (!found)
		{
			throw new InvalidLibraryException();
		}
		if (externalLibraries.containsKey(name))
			externalLibraries.remove(name);
		theoryManager.removeLibraryTheory(name);
		theoryManager.rebindPrimitives();
		LibraryEvent ev = new LibraryEvent(prolog, name);
		prolog.notifyUnloadedLibrary(ev);
	}

	
	private Library bindLibrary(Library lib) throws InvalidLibraryException
	{
		try
		{
			String name = lib.getName();
			lib.setEngine(prolog);
			currentLibraries.add(lib);
			// set primitives
			primitiveManager.createPrimitiveInfo(lib);
			// set theory
			String th = lib.getTheory();
			if (th != null)
			{
				theoryManager.consult(new Theory(th), false, name);
				theoryManager.solveTheoryGoal();
			}
			// in current theory there could be predicates and functors
			// which become builtins after lib loading
			theoryManager.rebindPrimitives();
			//
			return lib;
		} catch (InvalidTheoryException ex)
		{
			// System.out.println(ex.getMessage());
			// System.out.println("line "+ex.line+"  "+ex.pos);
			throw new InvalidLibraryException(lib.getName(), ex.line, ex.pos);
		} catch (Exception ex)
		{
			// ex.printStackTrace();
			throw new InvalidLibraryException(lib.getName(), -1, -1);
		}

	}

	
	public synchronized Library getLibrary(String name)
	{
		for (Library alib : currentLibraries)
		{
			if (alib.getName().equals(name))
			{
				return alib;
			}
		}
		return null;
	}

	public synchronized void onSolveBegin(Term g)
	{
		for (Library alib : currentLibraries)
		{
			alib.onSolveBegin(g);
		}
	}

	public synchronized void onSolveHalt()
	{
		for (Library alib : currentLibraries)
		{
			alib.onSolveHalt();
		}
	}

	public synchronized void onSolveEnd()
	{
		for (Library alib : currentLibraries)
		{
			alib.onSolveEnd();
		}
	}

	public synchronized URL getExternalLibraryURL(String name)
	{
		return isExternalLibrary(name) ? externalLibraries.get(name) : null;
	}

	public synchronized boolean isExternalLibrary(String name)
	{
		return externalLibraries.containsKey(name);
	}

	private static URL getClassResource(Class<?> klass)
	{
		if (klass == null)
			return null;
		return klass.getClassLoader().getResource(
				klass.getName().replace('.', '/') + ".class");
	}

	
	
	public void setOptimizedDirectory(String optimizedDirectory)
	{
		this.optimizedDirectory = optimizedDirectory;
	}
	
	public String getOptimizedDirectory()
	{
		return optimizedDirectory;
	}

}