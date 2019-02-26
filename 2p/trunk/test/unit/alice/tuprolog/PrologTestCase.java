package alice.tuprolog;

import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.TuLibrary;
import alice.tuprolog.TuProlog;
import alice.tuprolog.event.SpyEvent;
import alice.tuprolog.event.SpyListener;

import junit.framework.TestCase;

public class PrologTestCase extends TestCase {
	
	public void testEngineInitialization() {
		TuProlog engine = new TuProlog();
		assertEquals(4, engine.getCurrentLibraries().length);
		assertNotNull(engine.getLibrary("alice.tuprolog.lib.BasicLibrary"));
		assertNotNull(engine.getLibrary("alice.tuprolog.lib.ISOLibrary"));
		assertNotNull(engine.getLibrary("alice.tuprolog.lib.IOLibrary"));
		assertNotNull(engine.getLibrary("alice.tuprolog.lib.OOLibrary"));
	}
	
	public void testLoadLibraryAsString() throws InvalidLibraryException {
		TuProlog engine = new TuProlog();
		engine.loadLibrary("alice.tuprolog.StringLibrary");
		assertNotNull(engine.getLibrary("alice.tuprolog.StringLibrary"));
	}
	
	public void testLoadLibraryAsObject() throws InvalidLibraryException {
		TuProlog engine = new TuProlog();
		TuLibrary stringLibrary = new StringLibrary();
		engine.loadLibrary(stringLibrary);
		assertNotNull(engine.getLibrary("alice.tuprolog.StringLibrary"));
		TuLibrary javaLibrary = new alice.tuprolog.lib.OOLibrary();
		engine.loadLibrary(javaLibrary);
		assertSame(javaLibrary, engine.getLibrary("alice.tuprolog.lib.OOLibrary"));
	}
	
	public void testGetLibraryWithName() throws InvalidLibraryException {
		TuProlog engine = new TuProlog(new String[] {"alice.tuprolog.TestLibrary"});
		assertNotNull(engine.getLibrary("TestLibraryName"));
	}
	
	public void testUnloadLibraryAfterLoadingTheory() throws Exception {
		TuProlog engine = new TuProlog();
		assertNotNull(engine.getLibrary("alice.tuprolog.lib.IOLibrary"));
		TuTheory t = new TuTheory("a(1).\na(2).\n");
		engine.setTheory(t);
		engine.unloadLibrary("alice.tuprolog.lib.IOLibrary");
		assertNull(engine.getLibrary("alice.tuprolog.lib.IOLibrary"));
	}
	
	public void testAddTheory() throws InvalidTheoryException {
		TuProlog engine = new TuProlog();
		TuTheory t = new TuTheory("test :- notx existing(s).");
		try {
			engine.addTheory(t);
			fail();
		} catch (InvalidTheoryException expected) {
			assertEquals("", engine.getTheory().toString());
		}
	}
	
	public void testSpyListenerManagement() {
		TuProlog engine = new TuProlog();
		SpyListener listener1 = new SpyListener() {
			@Override
			public void onSpy(SpyEvent e) {}
		};
		SpyListener listener2 = new SpyListener() {
			@Override
			public void onSpy(SpyEvent e) {}
		};
		engine.addSpyListener(listener1);
		engine.addSpyListener(listener2);
		assertEquals(2, engine.getSpyListenerList().size());
	}
	
	public void testLibraryListener() throws InvalidLibraryException {
		TuProlog engine = new TuProlog(new String[]{});
		engine.loadLibrary("alice.tuprolog.lib.BasicLibrary");
		engine.loadLibrary("alice.tuprolog.lib.IOLibrary");
		TestPrologEventAdapter a = new TestPrologEventAdapter();
		engine.addLibraryListener(a);
		engine.loadLibrary("alice.tuprolog.lib.JavaLibrary");
		assertEquals("alice.tuprolog.lib.JavaLibrary", a.firstMessage);
		engine.unloadLibrary("alice.tuprolog.lib.JavaLibrary");
		assertEquals("alice.tuprolog.lib.JavaLibrary", a.firstMessage);
	}
	
	public void testTheoryListener() throws InvalidTheoryException {
		TuProlog engine = new TuProlog();
		TestPrologEventAdapter a = new TestPrologEventAdapter();
		engine.addTheoryListener(a);
		TuTheory t = new TuTheory("a(1).\na(2).\n");
		engine.setTheory(t);
		assertEquals("", a.firstMessage);
		assertEquals("a(1).\n\na(2).\n\n", a.secondMessage);
		t = new TuTheory("a(3).\na(4).\n");
		engine.addTheory(t);
		assertEquals("a(1).\n\na(2).\n\n", a.firstMessage);
		assertEquals("a(1).\n\na(2).\n\na(3).\n\na(4).\n\n", a.secondMessage);
	}
	
	public void testQueryListener() throws Exception {
		TuProlog engine = new TuProlog();
		TestPrologEventAdapter a = new TestPrologEventAdapter();
		engine.addQueryListener(a);
		engine.setTheory(new TuTheory("a(1).\na(2).\n"));
		engine.solve("a(X).");
		assertEquals("a(X)", a.firstMessage);
		assertEquals("yes.\nX / 1", a.secondMessage);
		engine.solveNext();
		assertEquals("a(X)", a.firstMessage);
		assertEquals("yes.\nX / 2", a.secondMessage);
	}

}
