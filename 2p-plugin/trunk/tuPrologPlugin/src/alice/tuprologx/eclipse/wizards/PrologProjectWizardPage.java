package alice.tuprologx.eclipse.wizards;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class PrologProjectWizardPage extends WizardPage {

	private Text projectText;
	private String[] libraries;
	private List libraryList;
//	private Button teorieButton;

	public PrologProjectWizardPage() {
		super("wizardPage");
		setTitle("Prolog Project");
		setDescription("This wizard creates a new Prolog project");
	}

	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);

		container.setLayout(new RowLayout(SWT.VERTICAL));

		final Group nomeProgettoGroup = new Group(container, SWT.NONE);
		final RowData rowData_2 = new RowData();
		rowData_2.width = 412;
		nomeProgettoGroup.setLayoutData(rowData_2);
		nomeProgettoGroup.setText("Project name");
		nomeProgettoGroup.setLayout(new RowLayout());

		projectText = new Text(nomeProgettoGroup, SWT.BORDER);
		final RowData rowData = new RowData();
		rowData.width = 391;
		projectText.setLayoutData(rowData);
		projectText.setToolTipText("Insert the name of the project to create");

		projectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		final Group librerieGroup = new Group(container, SWT.NONE);
		librerieGroup.setText("Libraries on \"Engine 1\"");
		final RowData rowData_8 = new RowData();
		rowData_8.height = 214;
		rowData_8.width = 408;
		librerieGroup.setLayoutData(rowData_8);
		librerieGroup.setLayout(new RowLayout(SWT.VERTICAL));

		libraryList = new List(librerieGroup, SWT.BORDER);
		final RowData rowData_1 = new RowData();
		rowData_1.height = 203;
		rowData_1.width = 326;
		libraryList.setLayoutData(rowData_1);
		libraryList.setToolTipText("Libreries loaded on \"Engine 1\"");
		// inserimento delle librerie di default
		libraryList.add("alice.tuprolog.lib.BasicLibrary");
		libraryList.add("alice.tuprolog.lib.IOLibrary");
		libraryList.add("alice.tuprolog.lib.ISOLibrary");
		libraryList.add("alice.tuprolog.lib.JavaLibrary");
		libraries = libraryList.getItems();

		final Composite composite = new Composite(librerieGroup, SWT.NONE);
		composite.setLayout(new GridLayout());
		final RowData rowData_3 = new RowData();
		rowData_3.width = 60;
		rowData_3.height = 184;
		composite.setLayoutData(rowData_3);

		final Button loadButton = new Button(composite, SWT.NONE);
		loadButton.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				InputDialog d = new InputDialog(null, "Load Library",
						"Library for \"Engine 1\":", "alice.tuprolog.lib.",
						null);
				d.open();
				String s = d.getValue();
				if (!(s.equals(""))) {
					libraryList.add(s);
					libraries = libraryList.getItems();
				}
			}
		});
		loadButton
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		loadButton.setToolTipText("Click to load new library on \"Engine1\"");
		loadButton.setText("Load");

		final Button rimuoviButton = new Button(composite, SWT.NONE);
		rimuoviButton.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				String[] selection = libraryList.getSelection();
				if (selection.length != 0)
					for (int i = 0; i < selection.length; i++) {
						libraryList.remove(selection[i]);
						libraries = libraryList.getItems();
					}
			}
		});
		rimuoviButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		rimuoviButton
				.setToolTipText("Click to remove the selected library from \"Engine1\"");
		rimuoviButton.setText("Remove");

		/*final Group motoreGroup = new Group(container, SWT.NONE);
		motoreGroup.setText("Default scope of \"Engine1\"");
		final RowData rowData_7 = new RowData();
		rowData_7.height = 33;
		rowData_7.width = 408;
		motoreGroup.setLayoutData(rowData_7);
		motoreGroup.setLayout(new RowLayout());

		teorieButton = new Button(motoreGroup, SWT.CHECK);
		teorieButton
				.setText("All theories will be included in default scope of \"Engine1\"");*/

		initialize();
		dialogChanged();
		setControl(container);
	}

	private void initialize() {
		projectText.setText("My_Prolog_Project");
	}

	private void dialogChanged() {
		String projectName = getProjectName();

		if (projectName.length() == 0) {
			updateStatus("Project name must be specified");
			return;
		}

		// controllo che non esista un progetto con quel nome
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			if (projectName.equalsIgnoreCase(projects[i].getName())) {
				updateStatus("A new project name must be specified");
				return;
			}
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/*
	 * METODI PUBBLICI CHE VERRANNO INVOCATI DAL WIZARD PER REPERIRE I DATI
	 * INSERITI
	 */

	public String getProjectName() {
		return projectText.getText();
	}

	public String[] getLibrariesToLoad() {
		return libraries;
	}

	public boolean allTheories() {
		return true;
		//return teorieButton.getSelection();
	}

}
