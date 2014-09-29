package alice.tuprologx.eclipse.views;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import alice.tuprologx.eclipse.core.PrologQuery;
import alice.tuprologx.eclipse.core.PrologQueryFactory;

public class ViewSet implements Observer {

	private ConsoleView console;
	private QueryList queryList;
	//	private ASTView ASTview;
	
	public ViewSet() {
		PrologQueryFactory.getInstance().addObserver(this);
	}

	public void refresh(final String theoryToShow) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() { 
			@Override
			public void run() { 
				IWorkbenchWindow dwindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow(); 
				final IWorkbenchPage wp = dwindow.getActivePage(); 
				//final IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IViewReference[] viewList = wp.getViewReferences();
					for(IViewReference ref : viewList){
						if(ref.getId().equalsIgnoreCase("alice.tuprologx.eclipse.views.ConsoleView")){
							console = (ConsoleView) ref.getView(false);
						}
						if(ref.getId().equalsIgnoreCase("alice.tuprologx.eclipse.views.QueryList")){
							queryList = (QueryList) ref.getView(false);
						}
					}

					queryList.addSelectionListener(new SelectionListener(){
						public void widgetSelected(SelectionEvent e) {
							TreeItem selection = (TreeItem)e.item;
							PrologQuery query = (PrologQuery) selection.getData();
							console.setQuery(query);
							wp.activate(console);
						}
						public void widgetDefaultSelected(SelectionEvent e) {
						}
					});

					//} catch (PartInitException e) {
				} catch (NullPointerException e) {
				}
			}});
	}

	@Override
	public void update(Observable arg0, Object arg1){
		if(queryList!=null){
			queryList.update();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(console);
			console.setQuery(queryList.getSelectedQuery());

		}

	}
}