package alice.tuprologx.eclipse.toolbar;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;

public class PrologGetTheoryAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow win;

	public void dispose() {
	};

	public void init(IWorkbenchWindow window) {
		this.win=window;
	};

	public void run(IAction action) {
		IProject currentProject = TuProlog.getActiveProject();
		String dynTheory = "";
		PrologEngine engine = PrologEngineFactory.getInstance().getEngine(currentProject.getName(),0);
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
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}



}
