package alice.tuprologx.eclipse.editors;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;
import alice.tuprologx.eclipse.core.PrologParser;
import alice.tuprologx.eclipse.preferences.PreferenceConstants;
import alice.tuprologx.eclipse.util.BracketMatcher;
import alice.tuprologx.eclipse.util.OperatorEvent;
import alice.tuprologx.eclipse.util.OperatorListener;
import alice.tuprologx.eclipse.util.TokenManager;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class PrologEditor extends TextEditor implements OperatorListener,
		IDocumentListener {

	// public static Display display;
	private Font textFont;
	protected final static char[] BRACKETS = { '{', '}', '(', ')', '[', ']' };

	private final TokenManager tokenManager;
	private Vector<String> lastDynOps;
	boolean first = true;
	public void documentAboutToBeChanged(DocumentEvent de) {
	}

	public void documentChanged(DocumentEvent de) {
		
	
		// effettua il build con il Parser
		Map<String, PrologEditor> args = new HashMap<String, PrologEditor>();
		args.put("Editor", this);

		try {
//			getProject().build(IncrementalProjectBuilder.FULL_BUILD, PrologParser.BUILDER_ID, args, null);
			getProject().build(IncrementalProjectBuilder.FULL_BUILD, PrologParser.BUILDER_ID, null, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

//		//effettua il build con il builder
//		 try{
//		 getProject().build(IncrementalProjectBuilder.FULL_BUILD,PrologBuilder.BUILDER_ID,null,null);
//		 }
//		 catch(CoreException e){}
//	}

	protected void performSave(boolean overwrite,
			IProgressMonitor progressMonitor) {

		super.performSave(overwrite, progressMonitor);

		 //effettua il build con il Parser
		 try{
		 getProject().build(IncrementalProjectBuilder.FULL_BUILD,PrologParser.BUILDER_ID,null,null);
		 }
		 catch(CoreException e){}

//		// effettua il build con il builder
//		try {
//			getProject().build(IncrementalProjectBuilder.FULL_BUILD,
//					PrologBuilder.BUILDER_ID, null, null);
//			PrologBuilder.setAlternativeBuild(false);
//		} catch (CoreException e) {
//		}

	}

	protected void initializeEditor() {
		super.initializeEditor();
		setDocumentProvider(new PrologDocumentProvider());
		setPreferenceStore(TuProlog.getDefault().getPreferenceStore());
		JFaceResources.getFontRegistry().put(
				"alice.tuprologx.eclipse.editor.editorfont",
				PreferenceConverter.getFontDataArray(getPreferenceStore(),
						PreferenceConstants.FONT));
		setSourceViewerConfiguration(new PrologSourceViewerConfiguration(
				TuProlog.getDefault().getTokenManager()));
	}

	public String getText() {
		return getDocumentProvider().getDocument(getEditorInput()).get();
	}

	public PrologEditor() {
		super();
		tokenManager = TuProlog.getDefault().getTokenManager();
	}

	public void createPartControl(Composite composite) {
		super.createPartControl(composite);
		getDocumentProvider().getDocument(getEditorInput())
				.addDocumentListener(this);
	}

	public void setFocus() {
		super.setFocus();

		// effettua il build con il Parser
		try {
			getProject().build(IncrementalProjectBuilder.FULL_BUILD,
					PrologParser.BUILDER_ID, null, null);
		} catch (CoreException e) {
		}
	}

	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {

		support.setCharacterPairMatcher(new BracketMatcher(BRACKETS));
		support.setMatchingCharacterPainterPreferenceKeys(
				PreferenceConstants.ENABLE_MATCHING_BRACKETS,
				PreferenceConstants.MATCHING_BRACKETS_COLOR);
		support.setSymbolicFontName("alice.tuprologx.eclipse.editor.editorfont");
		super.configureSourceViewerDecorationSupport(support);
	}

	protected boolean affectsTextPresentation(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.FONT))
			return true;
		return tokenManager.affectsTextPresentation(event);
	}

	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.FONT)) {
			FontData[] data = null;
			data = PreferenceConverter.getFontDataArray(
					this.getPreferenceStore(), PreferenceConstants.FONT);
			if (textFont != null)
				textFont.dispose();
			textFont = new Font(getSourceViewer().getTextWidget().getDisplay(),
					data);
			getSourceViewer().getTextWidget().setFont(textFont);
		} else
			tokenManager.handlePreferenceStoreChanged(event);
		super.handlePreferenceStoreChanged(event);
	}

	public PrologEngine getProjectEngine(int index) {
		return PrologEngineFactory.getInstance().getEngine(
				getProject().getName(), index);
	}

	public IProject getProject() {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		return file.getProject();
	}


	@SuppressWarnings("unchecked")
	public void opChanged(OperatorEvent e) {
		Vector<String> dynOps = e.getOpListAsStringList();
		if(hasChanged(dynOps)){
			lastDynOps = (Vector<String>)dynOps.clone();
			TokenManager tokenManager = TuProlog.getDefault().getTokenManager();
			ISourceViewer sv = this.getSourceViewer();
			if(sv!=null){
				try{
					sv.configure(new PrologSourceViewerConfiguration(tokenManager, dynOps));
				}catch(Exception e2){}
			}else{
				setSourceViewerConfiguration(new PrologSourceViewerConfiguration(tokenManager, dynOps));
			}
		}
	}
	@SuppressWarnings("unchecked")
	boolean hasChanged(Vector<String> newList){
		if(first){
			lastDynOps = (Vector<String>)newList.clone();
			first=false;
			return true;
		}
		if(lastDynOps!=null){

			for(String o:lastDynOps){
				if(!newList.contains(o)){
					return true;
				}
			}
			for(String o:newList){
				if(!lastDynOps.contains(o)){
					return true;
				}
			}
		}
		return false;
	}
}
