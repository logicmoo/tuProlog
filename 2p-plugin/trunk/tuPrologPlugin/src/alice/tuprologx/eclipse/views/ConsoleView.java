package alice.tuprologx.eclipse.views;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import alice.tuprolog.Term;
import alice.tuprolog.event.ReadEvent;
import alice.tuprolog.event.ReadListener;
import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologQuery;
import alice.tuprologx.eclipse.core.PrologQueryFactory;
import alice.tuprologx.eclipse.core.PrologQueryResult;
import alice.tuprologx.eclipse.core.PrologQueryScope;

public class ConsoleView extends ViewPart implements ReadListener{
	private CTabFolder notebook;
	private Tree tree;
	private SashForm sash;
	private boolean queryIsValid;
	private Text fQueryText;
	private Text spy;
	private Text result;
	private Text output;
	/*Castagna 06/2011*/
	private Text exception;
	/**/
	private Text project;
	private Text engines;
	private Text files;
	private Button prev;
	private Button next;
	private Composite resultViewer;
	public Composite outputViewer;
	/***
	 * Added InputViewer
	 */
	public InputViewer inputViewer;
	private CTabItem Input;
	/*Castagna 06/2011*/
	public Composite exceptionViewer;
	/**/
	public Composite bindViewer;
	public PrologQuery query;
	private Vector<PrologQueryResult> queryResults;
	private int queryResultIndex;
	private JFrame frame;
	private ArrayList<String> list;
	private ArrayList<Term> list2;

	private String[] columnNames;
	private JTable bindTable;
	private Object[][] data; 
	private Button bind;

	public ConsoleView()
	{
		super();
	}
	
	@Override
	public void createPartControl(Composite parent) {

		GridData groupData = new GridData();
		groupData.grabExcessHorizontalSpace = true;
		groupData.horizontalAlignment = SWT.FILL;

		GridData filesData = new GridData();
		filesData.grabExcessHorizontalSpace = true;
		filesData.grabExcessVerticalSpace = true;
		filesData.horizontalAlignment = SWT.FILL;
		filesData.verticalAlignment = SWT.FILL;

		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 1;

		GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 3;

		GridLayout buttonLayout = new GridLayout();
		buttonLayout.numColumns = 3;

		Composite mainFrame = new Composite(parent, SWT.NONE);
		mainFrame.setLayout(groupLayout);
		mainFrame.setLayoutData(groupData);

		notebook = new CTabFolder(mainFrame, SWT.TOP | SWT.BORDER);
		notebook.setLayout(groupLayout);
		notebook.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		CTabItem queryResult = new CTabItem(notebook,SWT.NONE);
		queryResult.setImage(TuProlog.getIconFromResources("console.gif"));
		queryResult.setText("Results");

		CTabItem queryScope = new CTabItem(notebook, SWT.NONE);
		queryScope.setImage(TuProlog.getIconFromResources("scope.gif"));
		queryScope.setText("Scope");

		CTabItem debug = new CTabItem(notebook,SWT.NONE);
		debug.setImage(TuProlog.getIconFromResources("Debugger.gif"));
		debug.setText("Debug");

		CTabItem Output = new CTabItem(notebook,SWT.NONE);
		Output.setImage(TuProlog.getIconFromResources("sample.gif"));
		Output.setText("Output");
		
		/**
		 * Added CTabItem "Input" to conform to Java Platform
		 */
		Input = new CTabItem(notebook,SWT.NONE);
		Input.setImage(TuProlog.getIconFromResources("sample.gif"));
		Input.setText("Input");
		
		/*Castagna 06/2011*/
		CTabItem Exception = new CTabItem(notebook,SWT.NONE);
		Exception.setImage(TuProlog.getIconFromResources("exception.gif"));
//		Exception.setImage(TuProlog.getIconFromResources("EnginesManager.gif"));
		Exception.setText("Exceptions");
		/**/

		notebook.setSelection(queryResult);

		//Costruzione Tab Result
		sash = new SashForm(notebook, SWT.HORIZONTAL);
		sash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree = new Tree(sash, SWT.BORDER);
		tree.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				PrologEngine engine = (PrologEngine) item.getData();
				queryResults = query.getEngineSolutions(engine);
				queryResultIndex = 0;
				refreshResultViewer();
			}
		});
		queryResult.setControl(sash);

		resultViewer = new Composite(sash, SWT.NONE);
		resultViewer.setLayout(tabLayout);

		spy = new Text(notebook, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY);
		spy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		debug.setControl(spy);

		Label resultLabel = new Label(resultViewer,SWT.NONE);
		resultLabel.setText("Solution: ");

		result = new Text(resultViewer, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.SCROLL_LINE);
		result.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		sash.setWeights(new int[] { 1, 3});
		Group solExplorer = new Group(resultViewer,SWT.NONE);
		solExplorer.setText("Solution explorer");
		GridData solExplorerLayout = new GridData(SWT.FILL,SWT.BOTTOM,true,false);
		solExplorerLayout.horizontalSpan = 2;
		solExplorer.setLayout(buttonLayout);
		solExplorer.setLayoutData(solExplorerLayout);

		/**
		 * Andrea Mordenti 15/04/2011
		 */
		final Group queryGroup = new Group(mainFrame, SWT.NONE);
		queryGroup.setText("Query:");
		queryGroup.setLayout(tabLayout);
		queryGroup.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
		queryGroup.setSize(20,15);
		fQueryText = new Text(queryGroup, SWT.SINGLE | SWT.BORDER);
		fQueryText.setToolTipText("Here will be shown the query you'll editing");
		fQueryText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
//		final PrologQueryFactory proQ = new PrologQueryFactory(); 
		fQueryText.addKeyListener(new KeyListener() {
			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
			}

			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if(e.keyCode==13){
					/*when enter key is pressed, the query in the fqueryText is executed*/
					String message = "";
					String newText = fQueryText.getText();
					if (newText.equals("")) {
						message = "The query is empty.";
						queryIsValid = false;
						ErrorDialog.openError(null, null,"Error", new Status(Status.ERROR, "alice.tuprologx.eclipse", message));
					}
					else if (newText.startsWith("?"))
					{
						message = "'?-' will be added automatically.";
						queryIsValid = false;
						ErrorDialog.openError(null, null,"Error", new Status(Status.ERROR, "alice.tuprologx.eclipse", message));
					}
					else
					{
//						IParser p = ParserFactory.createParser(newText);
						try{	
//							Term t = p.nextTerm(true);
							queryIsValid = true;
							message = "The query is valid.";
						}catch(Exception e1){
							queryIsValid = false;
							message = "Syntax error. The query isn't valid";
							ErrorDialog.openError(null, null,"Error", new Status(Status.ERROR, "alice.tuprologx.eclipse", message));
						}
					}
					if(queryIsValid){
						query = new PrologQuery(fQueryText.getText());
						bind.setEnabled(false);
						PrologQueryFactory.getInstance().executeQueryWS(query);
						queryResultIndex=0;
						tree.setSelection(tree.getItem(0));
						refreshResultViewer();
					}
				}
			}
		});
		final Button addQuery = new Button(queryGroup,SWT.BUTTON4);
		addQuery.setEnabled(true);
		addQuery.setImage(TuProlog.getIconFromResources("Solve18.png"));
		addQuery.setToolTipText("Solve");
		addQuery.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				query = new PrologQuery(fQueryText.getText());
				String message = "";
				String newText = fQueryText.getText();
				if (newText.equals("")) {
					message = "The query is empty.";
					queryIsValid = false;
					ErrorDialog.openError(null, null,"Error", new Status(Status.ERROR, "alice.tuprologx.eclipse", message));
				}
				else if (newText.startsWith("?"))
				{
					message = "'?-' will be added automatically.";
					queryIsValid = false;
					ErrorDialog.openError(null, null,"Error", new Status(Status.ERROR, "alice.tuprologx.eclipse", message));
				}
				else
				{
//					IParser p = ParserFactory.createParser(newText);
					try{	
//						Term t = p.nextTerm(true);
						queryIsValid = true;
						message = "The query is valid.";
					}catch(Exception e1){
						queryIsValid = false;
						message = "Syntax error.";
						ErrorDialog.openError(null, null,"Error", new Status(Status.ERROR, "alice.tuprologx.eclipse", message));
					}
					if(queryIsValid){
						bind.setEnabled(false);
						PrologQueryFactory.getInstance().executeQueryWS(query);
						queryResultIndex=0;
						tree.setSelection(tree.getItem(0));
						refreshResultViewer();
					}
				}}
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}});	

		prev = new Button(solExplorer,SWT.PUSH);
		prev.setText("Previous Result");
		prev.setLayoutData(new GridData(SWT.FILL,SWT.None,true,false));
		prev.setEnabled(false);
		prev.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				queryResultIndex--;
				refreshResultViewer();
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});


		next = new Button(solExplorer,SWT.PUSH);
		next.setText("Next Result");
		next.setLayoutData(new GridData(SWT.FILL,SWT.None,true,false));
		next.setEnabled(false);
		next.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				queryResultIndex++;
				refreshResultViewer();
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		bind = new Button(solExplorer, SWT.PUSH);
		bind.setText("All bindings");
		bind.setLayoutData(new GridData(SWT.FILL,SWT.NONE,true,false));
		bind.setEnabled(false);
		bind.addSelectionListener(new SelectionListener() {
			/**
			 * Andrea Mordenti 15/04/2011
			 * By pressing "All bindings" button, the following methods 
			 * is going to create the table of bindings
			 */
			public void widgetSelected(SelectionEvent e) {
				int numVars = list.size();
				int numTerm;
				numTerm= list2.size();
				numTerm= list2.size()/numVars;
				bind.setEnabled(true);
				columnNames = new String[numVars];
				data = new Term[numTerm][numVars];
				int z=0;
				for(int j =0; j<numTerm;j++){
					for(int i=0;i<numVars;i++){
						/*Filling the table columns with variables and terms*/
						columnNames[i]=list.get(i);
						data[j][i] = list2.get(z);
						z++;
					}
				}
				/*Creating and showing the bind table into a JFrame*/
				bindTable = new JTable(data, columnNames);
				bindTable.setEnabled(false);
				JScrollPane jt = new JScrollPane(bindTable);
				frame = new JFrame("All bindings");
				frame.add(jt);
				frame.setVisible(true);
				frame.setResizable(false);
				frame.setSize(300, 150);
				frame.setLocation(550,350);
				frame.setAlwaysOnTop(true);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub		
			}
		});
		//Costruzione Tab Scope

		Composite scopeViewer = new Composite(notebook,SWT.NONE);
		scopeViewer.setLayout(tabLayout);
		queryScope.setControl(scopeViewer);	
		Label projectLabel = new Label(scopeViewer,SWT.NONE);
		projectLabel.setText("Project: ");
		project = new Text(scopeViewer, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		project.setLayoutData(groupData);
		Label enginesLabel = new Label(scopeViewer,SWT.NONE);
		enginesLabel.setText("Engines: ");
		engines = new Text(scopeViewer, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		engines.setLayoutData(groupData);
		Label filesLabel = new Label(scopeViewer,SWT.NONE);
		filesLabel.setText("Files: ");
		files = new Text(scopeViewer, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY);
		files.setLayoutData(filesData);

		/**
		 * Andrea Mordenti 17/04/2011
		 *  Costruzione tab Output
		 */

		SashForm sashOut = new SashForm(notebook, SWT.HORIZONTAL);
		sashOut.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Tree tree2 = new Tree(sash, SWT.BORDER);
		tree2.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				PrologEngine engine = (PrologEngine) item.getData();
				queryResults = query.getEngineSolutions(engine);
				queryResultIndex = 0;
				refreshResultViewer();
			}
		});
		Output.setControl(sashOut);
		outputViewer = new Composite(sashOut, SWT.NONE);
		outputViewer.setLayout(tabLayout);
		Label outputLabel = new Label(outputViewer,SWT.NONE);
		outputLabel.setText("Output: ");
		output = new Text(outputViewer, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.SCROLL_LINE); /* Eliminated SWT.SINGLE to fix nl bug*/
		output.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		/**
		 * Creation tab Input
		 */
		SashForm sashIn = new SashForm(notebook, SWT.HORIZONTAL);
		sashIn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Input.setControl(sashIn);
		inputViewer = new InputViewer(sashIn);
		
		/*Castagna 06/2011*/
		SashForm sashException = new SashForm(notebook, SWT.HORIZONTAL);
		sashException.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Exception.setControl(sashException);
		exceptionViewer = new Composite(sashException, SWT.NONE);
		exceptionViewer.setLayout(tabLayout);
		Label exceptionLabel = new Label(exceptionViewer,SWT.NONE);
		exceptionLabel.setText("Exception: ");
		exception = new Text(exceptionViewer, SWT.MULTI | SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY | SWT.SCROLL_LINE);
		exception.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		/**/
	}
	@Override
	public void setFocus() {
		resultViewer.setFocus();
	}

	public void setQuery( PrologQuery query )
	{
		tree.removeAll();
		this.query = query;
		project.setText("");
		engines.setText("");
		files.setText("");
		spy.setText("");
		result.setText("");
		output.setText("");
		/*Castagna 06/2011*/
		exception.setText("");
		/**/
		fQueryText.setText(query.getQuery());
		if( query != null)
		{
			Vector<PrologEngine> engines = query.getAllEngines();
			for(int i = 0; i < engines.size() ; i++)
			{
				PrologEngine engine = engines.get(i);
				TreeItem item = new TreeItem(tree,SWT.NONE);
				item.setText(engine.getName());
				item.setData(engine);
				item.setImage(TuProlog.getIconFromResources("theory.gif"));
				tree.setSelection(item);	
			}
			Vector<PrologQueryScope> scopes = query.getAllScopes();
			String projStr = "";
			String engStr = "";
			String fileStr = "";
			for(int i = 0 ; i < scopes.size() ; i++)
			{
				PrologQueryScope scope = scopes.get(i);
				projStr += scope.getProject().getName() + ", ";
				engStr += scope.getEngine().getName() + ", ";
				fileStr += scope.getEngine().getName() + ": ";
				for(int j = 0; j < scope.getFiles().size() ; j++)
				{
					fileStr += scope.getFiles().get(j) + ", "; 
				}
				fileStr += "\n";
			}
			projStr = projStr.substring(0,projStr.length()-2);
			engStr = engStr.substring(0,engStr.length()-2);
			fileStr = fileStr.substring(0,fileStr.length()-2);
			this.project.setText(projStr);
			this.engines.setText(engStr);
			this.files.setText(fileStr);
			/*	bind.setEnabled(false);
			PrologQueryFactory.getInstance().executeQueryWS(query);
			queryResultIndex=0;
			tree.setSelection(tree.getItem(0));
			refreshResultViewer();*/
		}
	}

	private void refreshResultViewer() 
	{
		TreeItem engineNode = tree.getSelection()[0];
		PrologEngine engine = (PrologEngine) engineNode.getData();	
		queryResults = query.getEngineSolutions(engine);
		PrologQueryResult result = queryResults.get(queryResultIndex);

		/**Andrea Mordenti 01/05/2011
		/*obtain the variables and terms with those that do bind
		 * */

		list = engine.getSolveInfo();
		list2 = engine.getListTerm();
		this.output.setText(result.getOutput());
		this.result.setText(result.getResult());
		/*Castagna 06/2011*/
		this.exception.setText(result.getException());
		/**/
		if(list.size() != 0){
			bind.setEnabled(true);
		}
		this.spy.setText(result.getSpy());
		if(queryResultIndex <= 0)
		{
			queryResultIndex = 0;
			prev.setEnabled(false);
		}
		else
			prev.setEnabled(true);
		if(queryResultIndex >= queryResults.size() - 1)
		{
			queryResultIndex = queryResults.size() - 1;
			next.setEnabled(false);
		}
		else
			next.setEnabled(true);
	}
	public PrologQuery getQuery(){
		return query;
	}

	@Override
	public void readCalled(ReadEvent arg0) {
		notebook.setSelection(Input);
	}
}