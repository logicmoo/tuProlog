package alice.tuprologx.eclipse.util;

import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import alice.tuprologx.eclipse.TuProlog;
import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;
import alice.tuprologx.eclipse.core.PrologQuery;
import alice.tuprologx.eclipse.core.PrologQueryScope;

public class QueryDialog extends Dialog 
{
	PrologQueryScope queryScope;
	private Text fProjText;
	private Text fQueryText;
	private Tree tree;
	private IProject choosenProject;	
	private PrologQuery query;
	private Button buttonProject;
	private boolean queryIsValid;
	private String errorMessage;
	
	public QueryDialog(Shell parentShell) {
		super(parentShell);
		queryIsValid = false;
	}
	
	public QueryDialog(Shell parentShell, PrologQueryScope scope)
	{
		super(parentShell);
		this.queryScope = scope;
	}
	/*new costructor by morde*/
	public QueryDialog(Shell parentShell,Text t){
		super(parentShell);
		this.fQueryText=t;
		queryIsValid = true;
}
	public PrologQuery getQuery()
	{
		return query;
	}



	protected Control createDialogArea(Composite parent){
	      Composite composite = (Composite)super.createDialogArea(parent);
	      createControl(composite);
	      return composite;
	}
	
	public void createControl(Composite parent) {
		//Settaggio dei vari layout
		GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;
		tabLayout.verticalSpacing = 1;
		
		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 2;
		
		GridData tabData = new GridData();
		tabData.heightHint = 150;
		tabData.widthHint = 330;
		
		GridData groupData = new GridData();
		groupData.grabExcessHorizontalSpace = true;
		groupData.horizontalAlignment = SWT.FILL;
		
		GridData buttonData = new GridData(92,25);
		buttonData.verticalAlignment = SWT.BEGINNING;
		buttonData.horizontalAlignment = SWT.END;
		
		//Settaggio composite
		
		final Composite projComp = new Composite(parent, SWT.FILL);
		projComp.setLayout(tabLayout);
		projComp.setLayoutData(tabData);
		
		//Gruppo Project
		
		Group project = new Group(projComp, SWT.NONE);
		project.setLayout(groupLayout);
		project.setLayoutData(groupData);
		project.setText("Project:");

		fProjText = new Text(project, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		fProjText.setLayoutData(groupData);
		fProjText.setToolTipText("Select the project to run");

		buttonProject = new Button(project, SWT.PUSH);
		buttonProject.setLayoutData(buttonData);
		buttonProject.setText("Browse...");
		buttonProject.setToolTipText("Browse the workspace to select a valid project");
		
		//Gruppo Scope

		Group scope = new Group(projComp, SWT.NONE);
		scope.setText("Scope:");
		scope.setLayout(groupLayout);
		scope.setToolTipText("Choose the engines to enable and for each of them set the files to consult");
		scope.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		tree = new Tree (scope, SWT.CHECK | SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		//Gruppo Query
		//Qui eliminerei visto che non ci occorre più
		//@author morde :-)
	/*Group query = new Group(projComp, SWT.NONE);
		query.setText("Query:");
		query.setLayout(groupLayout);
		query.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));
		
		fQueryText = new Text(query, SWT.SINGLE | SWT.BORDER);
		fQueryText.setToolTipText("Here will be shown the query you'll editing");
		fQueryText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,false));*/
		
		updateWidgets();
		createActionListeners();
	}
	
	private void updateWidgets()
	{
		tree.removeAll();
		if(choosenProject == null)
			choosenProject = TuProlog.getActiveProject();
		
		if ( choosenProject != null)
		{
			fProjText.setText(choosenProject.getName());
			Vector<PrologEngine> engines = PrologEngineFactory.getInstance().getProjectEngines(choosenProject.getName());
			for (int j = 0 ; j < engines.size(); j ++)
			{
				PrologEngine currEngine = (PrologEngine) engines.get(j);
				TreeItem engineNode = new TreeItem(tree,0);
				engineNode.setText(currEngine.getName());
				engineNode.setData(currEngine);
				engineNode.setImage(TuProlog.getIconFromResources("theory.gif"));
				IResource[] files;
				try {
					files = choosenProject.members();
					for (int k = 0; k < files.length ; k ++)
					{
						String filename = files[k].getName();
						if( filename.endsWith(".pl") || filename.endsWith(".pro"))
						{
							TreeItem theoryNode = new TreeItem(engineNode,0);
							theoryNode.setText(files[k].getName());
							theoryNode.setImage(TuProlog.getIconFromResources("newTheory.gif"));
							theoryNode.setData(filename);
						}
					}
					engineNode.setExpanded(true);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	protected void okPressed()
	{
		boolean error = false;
		String message = "";
		Point position = null;
		
		String projectName = fProjText.getText();
		IProject project = null;
		try{
			project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		}catch(Exception e){}
		
		if(project == null){
			error = true;
			message = "Invalid project name.";
			position = fProjText.toDisplay(fQueryText.getSize());
			position.x -= fProjText.getSize().x/2;
		}
		else
		{
			TreeItem[] engines = tree.getItems();
			boolean checkedItems = false;
			for(int i = 0 ; i < engines.length ; i++)
			{
				if( engines[i].getChecked())
				{
					checkedItems = true;
					break;
				}
			}
			if(!checkedItems)
			{
				error = true;
				position = tree.toDisplay(tree.getSize());
				position.x -= tree.getSize().x/2;
				position.y -= tree.getSize().y/2;
				message = "Select at least an engine or file.";
			}else if( !queryIsValid )
			{
				error = true;
				//position = fQueryText.toDisplay(fQueryText.getSize());
				//position.x -= fQueryText.getSize().x/2;
				message = errorMessage;
			}
		}
		
		
		if( error )
		{
			ToolTip balloonTooltip = new ToolTip(getShell(),SWT.BALLOON | SWT.ICON_ERROR);
			balloonTooltip.setLocation(position);
			balloonTooltip.setText(message);
			balloonTooltip.setAutoHide(true);
			balloonTooltip.setVisible(true);
		}
		else
		{
			query = new PrologQuery(fQueryText.getText());
			TreeItem[] engItems = tree.getItems();
			if(engItems.length > 0)
			{
				for(int i = 0 ; i < engItems.length ; i++)
				{
					if(engItems[i].getChecked())
					{
						PrologEngine engine = (PrologEngine) engItems[i].getData();
						PrologQueryScope scope = new PrologQueryScope(engine);
						scope.setProject(choosenProject);
						TreeItem filesItems[] = engItems[i].getItems();
						if(filesItems.length > 0)
						{
							for(int j = 0; j < filesItems.length; j++)
							{
								if(filesItems[j].getChecked())
								{
									String file = (String) filesItems[j].getData();
									scope.addFile(file);
								}
							}
						}
						query.addScope(scope);
					}
				}
			}
			super.okPressed();
		}
	}
	//This method must be used with newer prolog engine versions...
	/*private boolean checkQuery()
	{
		try
		{
			Parser p = new Parser("?- " + fQueryText.getText());
			p.nextTerm(true);
		}
		catch(InvalidTermException e1)
		{
			this.errorMessage = e1.getMessage();
			if( this.errorMessage.endsWith("line: 1"))
			{
				this.errorMessage = this.errorMessage.replace("line: 1","");
			}
			return false;
		}
		return true;
	}*/
	private void createActionListeners()
	{
		tree.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(e.detail == SWT.CHECK)
				{
					TreeItem selection = (TreeItem)e.item;
					try
					{
//						PrologEngine engine = (PrologEngine) selection.getData();
						for( int j = 0 ; j < selection.getItemCount(); j++)
						{
							selection.getItem(j).setChecked(false);
						}							
					}
					catch(Exception e2){}
					try
					{	
//						String filename = (String) selection.getData();
						if(selection.getChecked())
						{
							selection.getParentItem().setChecked(true);
						}
					}
					catch(Exception e3){}
				}
			}
		});
		
		fQueryText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String message = "";
				Point position = fQueryText.toDisplay(fQueryText.getCaretLocation());
				position.y = position.y+fQueryText.getSize().y;
				
				String newText = fQueryText.getText();
//				Device dev = Display.getCurrent();
				if (newText.equals("")) {
					message = "The query is empty.";
					queryIsValid = false;
				}
				else if (newText.startsWith("?"))
				{
					message = "'?-' will be added automatically.";
					queryIsValid = false;
				}
				else
				{
//					IParser p = ParserFactory.createParser(newText);
					/*int result = p.readTerm(true);
					
					switch (result) {
					case (Parser.TERM):
							queryIsValid = true;
							message = "The query is valid.";
						break;
					case (Parser.ERROR):
							queryIsValid = false;
							message = "Syntax error.";
						break;
					}*/
					/*
					 * Marco 12/04/11
					 * Hooked with new parser
					 */
					try{	
//						Term t = p.nextTerm(true);
						queryIsValid = true;
						message = "The query is valid.";
					}catch(Exception e1){
						queryIsValid = false;
						message = "Syntax error.";
					}
	                ////////END////////////

				}
				ToolTip balloonTooltip;
				if ( !queryIsValid )
					balloonTooltip = new ToolTip(getShell(),SWT.BALLOON | SWT.ICON_ERROR);
				else
					balloonTooltip = new ToolTip(getShell(),SWT.BALLOON | SWT.ICON_INFORMATION);
				balloonTooltip.setLocation(position);
				balloonTooltip.setText(message);
				balloonTooltip.setAutoHide(true);
				balloonTooltip.setVisible(true);
			}
		});
		
		buttonProject.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ProjectChooser pj = new ProjectChooser(getShell());
				pj.run();
				choosenProject = pj.getChoosenProject();
				updateWidgets();
			}
		});
	}
	
	//Widget che permette di scegliere i progetti da configurare.
	public class ProjectChooser extends ContainerSelectionDialog{
//		private IProject choosenProjects;
		
		public ProjectChooser(Shell parent){
			super(parent,ResourcesPlugin.getWorkspace().getRoot(), true, "Select a project to constrain your search:");
			setTitle("Project Selection");
		}
		
		public IProject getChoosenProject(){
			return choosenProject;
		}
		
		public void run(){
			if (open() == Window.OK){
				Object[] projects = getResult();
				IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
				Path projPath = (Path)projects[0];
				IProject project = workspaceRoot.getProject(projPath.toOSString().substring(1));
				try {
					if (project.hasNature("alice.tuprologx.eclipse.prologNature"))
						choosenProject = project;
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
}