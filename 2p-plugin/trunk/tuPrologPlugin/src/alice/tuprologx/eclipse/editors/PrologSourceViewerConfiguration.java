package alice.tuprologx.eclipse.editors;

import java.util.Vector;

import alice.tuprologx.eclipse.scanners.*;
import alice.tuprologx.eclipse.util.TokenManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * Source viewer configuration for Prolog code. Implements a presentation
 * reconciler for syntax highligting
 */
public class PrologSourceViewerConfiguration extends TextSourceViewerConfiguration {

	private final TokenManager tokenManager;
	private  Vector<String> newOps = null;

	public PrologSourceViewerConfiguration(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
		
	}
	public PrologSourceViewerConfiguration(TokenManager tokenManager,Vector<String> newOps) {
		this.tokenManager = tokenManager;
		this.newOps = newOps;
	}
	public void setOps(Vector<String> ops){
		this.newOps=ops;
	}
	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return PrologPartitionScanner.getLegalContentTypes();
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler
				.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr;
		dr = new DefaultDamagerRepairer(new DefaultScanner(tokenManager));
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(new CommentScanner(tokenManager));
		reconciler.setDamager(dr, PrologPartitionScanner.PROLOG_COMMENT);
		reconciler.setRepairer(dr, PrologPartitionScanner.PROLOG_COMMENT);
		dr = new ListDamagerRepairer(new ListScanner(tokenManager));
		reconciler.setDamager(dr, PrologPartitionScanner.PROLOG_LIST);
		reconciler.setRepairer(dr, PrologPartitionScanner.PROLOG_LIST);

		if(newOps!=null &&!newOps.isEmpty()){
			dr = new DefaultDamagerRepairer(new DynOpScanner(tokenManager,newOps));
			reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
			reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		}
		return reconciler;
		

	}

	
}
