package alice.tuprologx.eclipse.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tree;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.interfaces.IParser;
import alice.tuprolog.interfaces.ParserFactory;
import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.editors.PrologEditor;
import alice.tuprologx.eclipse.util.TreeView;

public class ASTView extends ViewPart implements IResourceChangeListener{
	private TreeViewer viewer; 
	private Vector<Struct> theoryTerms;
	private Action showAST;
    private Tree tree ;
    private Node root=null;
    private Table m_nodes = null;
    private HashMap<String, Node> mapping = new HashMap<String, Node>();

    public ASTView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer=new TreeViewer(parent);
		viewer.setContentProvider(new TermContentProvider());
		TuProlog.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_BUILD);

		createActions();
		createToolbar();
		createMenu();
		createContextMenu();
		updateTerms();
		viewer.setInput(theoryTerms);

	}
	public void createMenu()
	{
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
		menuManager.add(showAST);
	}

	public void createToolbar()
	{
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		toolbarManager.add(showAST);
	}

	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				mgr.add(showAST);
			}
		});

		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr,viewer);
	}

	public void createActions()
	{
		showAST = new Action("ShowAST") {
			public void run() {
		        String label = "name";
		        JComponent treeview = TreeView.demo(tree,label);
		        JFrame frame = new JFrame("AST View");
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setContentPane(treeview);
		        frame.pack();
		        frame.setVisible(true);
			}
		};
		showAST.setDescription("Displayt an interactive view of AST");
		showAST.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	
	/**
	 * Content provider Class provide content to view
	 * @author marco Prati
	 *
	 */
	class TermContentProvider implements ITreeContentProvider{

		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			return ((Vector<Struct>)inputElement).toArray();
		}

		public Object[] getChildren(Object parentElement) {
			Vector<Term> terms = null;
			//System.out.println("parentClass: "+parentElement.getClass().toString());

			if(parentElement instanceof Struct){
				Struct struct = (Struct)parentElement;
				terms = new Vector<Term>();
				for(int i=0;i<struct.getArity();i++){
					Term t = struct.getTerm(i);
					terms.add(t);
				}
			}

			return terms!=null?terms.toArray():new String[]{"Empty"};
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
//			System.out.println("elementClass: "+element.getClass().toString());
			if(element instanceof Struct){
				Struct struct = (Struct)element;
				return struct.getArity()!=0;
			}
			return false;
		}
		
	}
	public void updateTerms(){
		IWorkbenchWindow temp =TuProlog.getDefault().getWorkbench().getActiveWorkbenchWindow();
		IEditorPart editor=null;
		if(temp!=null){
			IWorkbenchPage page = temp.getActivePage();
			if(page!=null)
				editor = page.getActiveEditor(); 
		
		}
		if (editor!=null && editor instanceof PrologEditor){
			PrologEditor ed=(PrologEditor)editor;
			String newTheory = ed.getText();
			IParser parser = ParserFactory.createParser(newTheory);
			theoryTerms = new Vector<Struct>();
			while (true) {
				Term result = null;
				try {
					result = parser.nextTerm(true);
					if (result == null)
						break;
					theoryTerms.add((Struct)result);
				}
				catch (Exception e) {
					break;
				}
			}
		}
	}
	public void updateTree(Vector<Struct> terms){
		tree= new Tree();
		m_nodes = tree.getNodeTable();
		Schema schema = new Schema();
		schema.addColumn("name", String.class);
		m_nodes.addColumns(schema);
		root=tree.addRoot();
		root.set("name", "Root");
		if(terms!=null){
			Iterator<Struct> it = terms.iterator();
			while(it.hasNext()){
				Struct s = it.next();
				Node n = tree.addChild(root);
				n.setString("name", s.getName());	
				mapping.put(s.toString(),n);
				visit(s);
			}
		}

	}
	public void visit(Struct s){
		Node parentNode = (Node)mapping.get(s.toString());
		//System.out.println("Termine\\"+s.getArity()+": "+s.toString());
		for(int i=0;i<s.getArity();i++){
			Term t = s.getTerm(i);
			Node n = null;
			n = tree.addChild(parentNode);

			if(t instanceof Struct ){
				n.setString("name", ((Struct) t).getName());
			}else{
				n.setString("name", t.toString());
			}
			mapping.put(t.toString(),n);
			if(t instanceof Struct)visit((Struct)t);
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		updateTerms();
		updateTree(theoryTerms);
		Display display = Display.getDefault();
		display.asyncExec(new Runnable() {
			
			public void run() {
				viewer.setInput(theoryTerms);
			}
		});
	}
}
