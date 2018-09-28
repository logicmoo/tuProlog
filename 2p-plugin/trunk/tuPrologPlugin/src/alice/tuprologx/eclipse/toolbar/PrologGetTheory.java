package alice.tuprologx.eclipse.toolbar;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;

public class PrologGetTheory extends AbstractHandler {
	// dentro PrologGetTheoryAction.java win veniva passato come parametro in init()
	private IWorkbenchWindow win;
	// A workbench window is a top level window in a workbench. (dalla documentazione)

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		IWorkbench wb = PlatformUI.getWorkbench();
		win = wb.getActiveWorkbenchWindow();
		
		IProject currentProject = TuProlog.getActiveProject();
		String dynTheory = "";
		PrologEngine engine = PrologEngineFactory.getInstance().getEngine(currentProject,0);
		dynTheory = engine.getTheory();
		InputStream is=null;
		try {
			is = new ByteArrayInputStream(dynTheory.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
//			IEditorPart editor= win.getActivePage().getActiveEditor();
//			if (editor instanceof PrologEditor){
//				PrologEditor ed = (PrologEditor) editor;
//			}
			// get the active Workbench page
			IWorkbenchPage page = win.getActivePage();
			IFile file = currentProject.getFile(".tmp.pl");
			if(currentProject.exists(new Path(".tmp.pl"))){
				file.setContents(is, 332,null);
			}else{
				file.create(is, true, null);

			}
				
			page.openEditor(new FileEditorInput(file),"alice.tuprologx.eclipse.editors.PrologEditor");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
