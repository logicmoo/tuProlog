package alice.tuprologx.eclipse.views;
//test
import java.util.ArrayList;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import alice.tuprolog.Term;
import alice.tuprolog.interfaces.IParser;
import alice.tuprolog.interfaces.ParserFactory;
import alice.tuprologx.eclipse.editors.PrologEditor;

public class Method2 extends ViewPart {
    private TableViewer viewer;
    private Action show;

    class Element implements IAdaptable{

		private String name;
		private int start=0,end=-1;
		
		public String toString(){
			return name;
		}
    	
    	public String getName() {
			return name;
		}



		public void setName(String name) {
			this.name = name;
		}



		public Element(String name) {
			super();
			this.name = name;
		}



		public Element(String name, int start, int end) {
			super();
			this.name = name;
			this.start = start;
			this.end = end;
		}



		public Object getAdapter(Class adapter) {
			// TODO Auto-generated method stub
			return null;
		}
    	
    }
    
    class TableObject implements IAdaptable{
    	private ArrayList<Element> voices;
    	
    	public TableObject(){
    		super();
    		voices=new ArrayList<Element>();
    	}
    	
    	public void addVoice(Element voice){
    		voices.add(voice);
    	}
    	
    	public void removeVoice(Element voice){
    		voices.remove(voice);
    	}
    	
    	public Element[] getVoices(){
    		return (Element[])voices.toArray(new Element[voices.size()]);
    	}
    	
    	public boolean hasVoices(){
    		return voices.size()>0;
    	}

		public Object getAdapter(Class adapter) {
			// TODO Auto-generated method stub
			return null;
		}
    	
    }
    
    class ViewContentProvider implements IStructuredContentProvider{

    	public TableObject table;
    	
		public Object[] getElements(Object listObject) {
			// TODO Auto-generated method stub
			if(listObject.equals(getViewSite())){
				if(table==null)
					initialize();
				return getVoices(table);
			}
			return getVoices(listObject);
		}

		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}
		
		public Object[] getVoices(Object tableObject){
			if(tableObject instanceof TableObject){
				return((TableObject)tableObject).getVoices();
			}
			return new Object[0];
		}
		
		public boolean hasVoices(Object tableObject){
			if(tableObject instanceof TableObject){
				return((TableObject)tableObject).hasVoices();
			}
			return false;
		}
    	
		private void initialize(){
			table=new TableObject();
			IEditorPart iep=getSite().getPage().getActiveEditor();
            if(iep instanceof PrologEditor){
                PrologEditor pe=(PrologEditor)iep;
                IParser ms = ParserFactory.createParser(pe.getText());
                //IDocument doc = pe.getDocumentProvider().getDocument(pe.getEditorInput());
                int off= 0, /*line= 0,*/ end= 0;
//               int i = ms.readTerm(true);
//                while(i != 1){
//                    //off = ms.getCurrentPos();
//                    Term term = ms.getCurrentTerm();
//                    //try {
//                    	if (term != null) {
//                        	//line = ms.getCurrentLine();
//                        	end = ms.getCurrentPos() - off - 2;
//                        	//end = doc.getLineOffset(line)- off - 4;
//                        	Element name = new Element(term.toString(),off,end);
//                        	table.addVoice(name);
//                        }
//                    //} catch (BadLocationException e) {}
//                    off = ms.getCurrentPos();
//                    i = ms.readTerm(false);
				/*
				 * Marco 12/04/11
				 * Hooked with new parser
				 * 
				 */
                int line=0;
                Term t=null;
                try{
                	t= ms.nextTerm(true);
                	if (t != null) {
                    	line = ms.getCurrentLine();
                    	//end = ms.getCurrentPos() - off - 2;
                    	//end = doc.getLineOffset(line)- off - 4;
                    	Element name = new Element(t.toString(),off,end);
                    	table.addVoice(name);
                	}
                }catch(Exception e){}
              //  off = ms.getCurrentPos();
                //i = ms.readTerm(false);*/
                ////////END////////////
                
            }
		}
    }
    class ViewLabelProvider extends LabelProvider{

		public String getText(Object obj){
			return obj.toString();
    	}
    	
    	public Image getImage(Object obj){
    		String imageKey=ISharedImages.IMG_OBJ_ELEMENT;
    		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);    		
    	}
    	
    	public void dispose(){   		
    	}
    }
    
    class NameSorter extends ViewerSorter{}
    
    public Method2(){}
    
    public void refresh(){
    	((ViewContentProvider)viewer.getContentProvider()).initialize();
    	viewer.refresh();
    }
    

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		viewer=new TableViewer(parent,SWT.MULTI|SWT.H_SCROLL|SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}
	private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                Method2.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(show);
    }

    private void fillContextMenu(IMenuManager manager) {
    	manager.add(show);
        manager.add(new Separator());
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                show.run();
            }
        });
    }
    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        show.setEnabled(getSite().getPage().getActiveEditor()instanceof PrologEditor);
        viewer.getControl().setFocus();
    }
    
    private void makeActions(){
    	show=new Action(){
    		public void run(){
    			StructuredSelection ss=(StructuredSelection)viewer.getSelection();
    			Object root=ss.getFirstElement();
                if(root instanceof Element){
                    try{
                        IEditorPart iep=getSite().getPage().getActiveEditor();
                        if(iep instanceof PrologEditor){
                            PrologEditor pe=(PrologEditor)iep;
                            Element obj=(Element) root;
                            pe.selectAndReveal(obj.start, obj.end);
                        }
                    }
                    catch(Exception e){}
                }
    		}
    	};
    	show.setText("Show All Methods");
        show.setToolTipText("Show Method");
        show.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
            getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
    }
}