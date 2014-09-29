package alice.tuprologx.eclipse.properties;

import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.dialogs.PropertyPage;

import alice.tuprologx.eclipse.core.PrologEngine;
import alice.tuprologx.eclipse.core.PrologEngineFactory;

public class EnginesManagement extends PropertyPage {

	List listEngine = null;
	Group libraryGroup = null;
	Group scopeGroup = null;
	List listLibrary = null;
	Button[] theoriesButtons = null;
	String motoreScelto = "";
	Button deleteEngine;
	Button renameEngine;
	Button loadEngine;
	Button loadLibrary;
	Button unloadLibrary;

	// Costruttore
	public EnginesManagement() {
		super();
	}

	// Crea i componenti grafici e ne gestisce la visualizzazione
	protected Control createContents(final Composite parent) {
		final String name = ((IResource) getElement()).getName();

		final Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new RowLayout(SWT.VERTICAL));

		final Group engineGroup = new Group(container, SWT.NONE);
		engineGroup.setText("Engines");
		engineGroup.setLayout(new RowLayout(SWT.HORIZONTAL));

		listEngine = new List(engineGroup, SWT.BORDER);
		final RowData rowData_1 = new RowData();
		rowData_1.height = 120;
		rowData_1.width = 240;
		listEngine.setLayoutData(rowData_1);
		listEngine.setToolTipText("Loaded engines");

		String[] items = new String[PrologEngineFactory.getInstance()
				.getProjectEngines(name).size()];
		for (int j = 0; j < PrologEngineFactory.getInstance()
				.getProjectEngines(name).size(); j++) {
			items[j] = PrologEngineFactory.getInstance().getEngine(name, j)
					.getName();
		}
		listEngine.setItems(items);
		final Composite compositeEngine = new Composite(engineGroup, SWT.NONE);
		compositeEngine.setLayout(new GridLayout());
		final RowData rowData_2 = new RowData();
		rowData_2.width = 80;
		rowData_2.height = 100;
		compositeEngine.setLayoutData(rowData_2);
		listEngine.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				String[] selection = listEngine.getSelection();
				if (selection.length != 0) {
					listLibrary.removeAll();
					for (int j = 0; j < selection.length; j++) {
						motoreScelto = selection[j];
						libraryGroup.setText("Libraries on: \"" + motoreScelto
								+ "\"");
						listLibrary.setToolTipText("Loaded libraries on: \""
								+ motoreScelto + "\"");
						scopeGroup.setText("Default scope of: \""
								+ motoreScelto + "\"");
						for (int i = 0; i < PrologEngineFactory.getInstance()
								.getProjectEngines(name).size(); i++) {
							if (PrologEngineFactory.getInstance()
									.getEngine(name, i).getName()
									.equals(motoreScelto)) {
								Vector<String> lib = PropertyManager
										.getLibrariesFromProperties(
												(IProject) getElement(),
												motoreScelto);
								String[] librerie = new String[lib.size()];
								for (int k = 0; k < librerie.length; k++) {
									librerie[k] = (String) lib.elementAt(k);
								}
								listLibrary.setItems(librerie);
							}
						}
					}

					for (int j = 0; j < theoriesButtons.length; j++)
						theoriesButtons[j].setSelection(false);
					Vector<String> theories = new Vector<String>();
					try {
						IResource[] resources = ((IProject) getElement())
								.members();
						for (int j = 0; j < resources.length; j++)
							if ((resources[j] instanceof IFile)
									&& (resources[j].getName().endsWith(".pl")))
								theories.add(resources[j].getName());
					} catch (CoreException e1) {
					}
					Vector<?> theoriesFromProperties = PropertyManager
							.getTheoriesFromProperties((IProject) getElement(),
									motoreScelto);
					for (int j = 0; j < theories.size(); j++) {
						theoriesButtons[j].setText(theories
								.elementAt(j));
						if (PropertyManager.allTheories(
								(IProject) getElement(), motoreScelto))
							theoriesButtons[j].setSelection(true);
						else
							for (int k = 0; k < theoriesFromProperties.size(); k++)
								if (((String) theoriesFromProperties
										.elementAt(k)).equals(theories
										.elementAt(j))) {
									theoriesButtons[j].setSelection(true);
								}
					}
					if (PrologEngineFactory.getInstance()
							.getProjectEngines(name).size() != 1) {
						deleteEngine.setEnabled(true);
					} else
						deleteEngine.setEnabled(false);
					loadLibrary.setEnabled(true);
					for (int j = 0; j < theoriesButtons.length; j++)
						theoriesButtons[j].setEnabled(true);
					renameEngine.setEnabled(true);
				}
			}

		});

		loadEngine = new Button(compositeEngine, SWT.NONE);
		loadEngine.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				String scelta = LoadEngine.show(null, "LoadEngine",
						(IProject) getElement(), listEngine.getItems());

				if (scelta != null) {
					motoreScelto = "";
					listLibrary.removeAll();
					for (int j = 0; j < theoriesButtons.length; j++)
						theoriesButtons[j].setSelection(false);
					libraryGroup.setText("Libraries on:               ");
					listLibrary.setToolTipText("Loaded libraries on: ");
					scopeGroup.setText("Default scope of:                  ");
					listEngine.add(scelta);
					PrologEngine engine = PrologEngineFactory.getInstance()
							.addEngine(name, scelta);
					Vector<String> v = new Vector<String>();
					PropertyManager.setLibrariesOnEngine(v, engine);
					PropertyManager.setLibraryInProperties(
							(IProject) getElement(), scelta, new String[0]);
					listEngine.deselectAll();
					deleteEngine.setEnabled(false);
					renameEngine.setEnabled(false);
					loadLibrary.setEnabled(false);
					unloadLibrary.setEnabled(false);
					for (int j = 0; j < theoriesButtons.length; j++)
						theoriesButtons[j].setEnabled(false);
				}
			}
		});
		loadEngine
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		loadEngine.setToolTipText("Click to create new engine");
		loadEngine.setText("Create");

		deleteEngine = new Button(compositeEngine, SWT.NONE);
		deleteEngine.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				motoreScelto = "";
				String[] selection = listEngine.getSelection();
				listLibrary.removeAll();
				for (int j = 0; j < theoriesButtons.length; j++)
					theoriesButtons[j].setSelection(false);
				libraryGroup.setText("Libraries on:               ");
				listLibrary.setToolTipText("Loaded libraries on: ");
				scopeGroup.setText("Default scope of:                  ");
				for (int i = 0; i < selection.length; i++) {
					listEngine.remove(selection[i]);
					PrologEngineFactory.getInstance().deleteEngine(name,
							selection[i]);
					PropertyManager.deleteEngineInProperties(
							(IProject) getElement(), selection[i],
							listEngine.getItems());
				}
				deleteEngine.setEnabled(false);
				loadLibrary.setEnabled(false);
				unloadLibrary.setEnabled(false);
				for (int j = 0; j < theoriesButtons.length; j++)
					theoriesButtons[j].setEnabled(false);
				renameEngine.setEnabled(false);
			}
		});
		deleteEngine.setEnabled(false);
		deleteEngine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		deleteEngine.setToolTipText("Click to remove the selected engine");
		deleteEngine.setText("Remove");

		renameEngine = new Button(compositeEngine, SWT.NONE);
		renameEngine.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				InputDialog d = new InputDialog(null, "Rename Engine",
						"Engine name:", "", new EngineValidator(listEngine
								.getItems()));

				d.open();
				String scelta = d.getValue();
				if (scelta != null) {
					listLibrary.removeAll();
					for (int j = 0; j < theoriesButtons.length; j++)
						theoriesButtons[j].setSelection(false);
					libraryGroup.setText("Libraries on:               ");
					listLibrary.setToolTipText("Loaded libraries on: ");
					scopeGroup.setText("Default scope of:                  ");
					for (int i = 0; i < PrologEngineFactory.getInstance()
							.getProjectEngines(name).size(); i++)
						if (PrologEngineFactory.getInstance()
								.getEngine(name, i).getName()
								.equals(motoreScelto))
							PrologEngineFactory.getInstance()
									.getEngine(name, i).rename(scelta);

					Vector<?> lib = PropertyManager.getLibrariesFromProperties(
							(IProject) getElement(), motoreScelto);
					String[] library = new String[lib.size()];
					for (int i = 0; i < lib.size(); i++)
						library[i] = (String) lib.elementAt(i);

					Vector<?> theor = PropertyManager.getTheoriesFromProperties(
							(IProject) getElement(), motoreScelto);
					String[] theories = new String[theor.size()];
					boolean all = false;
					if (PropertyManager.allTheories((IProject) getElement(),
							motoreScelto)) {
						all = true;
					} else {
						for (int i = 0; i < theor.size(); i++) {
							theories[i] = (String) theor.elementAt(i);
						}
					}
					listEngine.setItem(listEngine.getSelectionIndex(), scelta);
					PropertyManager.deleteEngineInProperties(
							(IProject) getElement(), motoreScelto,
							listEngine.getItems());
					PropertyManager.setLibraryInProperties(
							(IProject) getElement(), scelta, library);
					if (all)
						PropertyManager.setTheoriesInProperty(
								(IProject) getElement(), scelta, null, true);
					else
						PropertyManager.setTheoriesInProperty(
								(IProject) getElement(), scelta, theories,
								false);
					listEngine.deselectAll();
					deleteEngine.setEnabled(false);
					renameEngine.setEnabled(false);
					loadLibrary.setEnabled(false);
					unloadLibrary.setEnabled(false);
					for (int j = 0; j < theoriesButtons.length; j++)
						theoriesButtons[j].setEnabled(false);

				}
			}
		});
		renameEngine.setEnabled(false);
		renameEngine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		renameEngine.setToolTipText("Click to rename the selected engine");
		renameEngine.setText("Rename");

		libraryGroup = new Group(container, SWT.RIGHT);
		libraryGroup.setText("Libraries on:               ");
		libraryGroup.setLayout(new RowLayout(SWT.HORIZONTAL));

		listLibrary = new List(libraryGroup, SWT.BORDER);
		final RowData rowData_3 = new RowData();
		rowData_3.height = 120;
		rowData_3.width = 240;
		listLibrary.setLayoutData(rowData_3);
		listLibrary.setToolTipText("Loaded libraries on: ");
		listLibrary.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				if (listLibrary.getSelection().length != 0)
					unloadLibrary.setEnabled(true);
			}
		});

		final Composite compositeLibrary = new Composite(libraryGroup, SWT.NONE);
		compositeLibrary.setLayout(new GridLayout());
		final RowData rowData_4 = new RowData();
		rowData_4.width = 80;
		rowData_4.height = 100;
		compositeLibrary.setLayoutData(rowData_4);

		loadLibrary = new Button(compositeLibrary, SWT.NONE);
		loadLibrary.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				InputDialog d = new InputDialog(null, "Load Library on \""
						+ motoreScelto + "\"", "Library:",
						"alice.tuprolog.lib.", new LibraryValidator(listLibrary
								.getItems()));
				d.open();
				String scelta = d.getValue();
				if (scelta != null) {
					Vector<String> r = new Vector<String>();
					String[] t = listLibrary.getItems();
					for (int i = 0; i < t.length; i++) {
						r.add(t[i]);
					}
					r.add(scelta);
					for (int i = 0; i < PrologEngineFactory.getInstance()
							.getProjectEngines(name).size(); i++) {
						if (PrologEngineFactory.getInstance()
								.getEngine(name, i).getName()
								.equals(motoreScelto)) {
							PropertyManager.setLibrariesOnEngine(r,
									PrologEngineFactory.getInstance()
											.getEngine(name, i));
							listLibrary.add(scelta);
							PropertyManager.setLibraryInProperties(
									(IProject) getElement(), motoreScelto,
									listLibrary.getItems());
						}
					}
					listLibrary.deselectAll();
				}

			}

		});
		loadLibrary.setEnabled(false);
		loadLibrary
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		loadLibrary
				.setToolTipText("Click to load new library on the selected engine");
		loadLibrary.setText("Load");

		unloadLibrary = new Button(compositeLibrary, SWT.NONE);
		unloadLibrary.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {
				String[] selection = listLibrary.getSelection();
				if (selection.length != 0) {
					for (int i = 0; i < selection.length; i++) {
						listLibrary.remove(selection[i]);
						PropertyManager.setLibraryInProperties(
								(IProject) getElement(), motoreScelto,
								listLibrary.getItems());
					}
					Vector<String> r = new Vector<String>();
					String[] t = listLibrary.getItems();
					for (int i = 0; i < t.length; i++) {
						r.add(t[i]);
					}
					for (int i = 0; i < PrologEngineFactory.getInstance()
							.getProjectEngines(name).size(); i++) {
						if (PrologEngineFactory.getInstance()
								.getEngine(name, i).getName() == motoreScelto) {
							PropertyManager.setLibrariesOnEngine(r,
									PrologEngineFactory.getInstance()
											.getEngine(name, i));
						}
					}
				}

			}
		});
		unloadLibrary.setEnabled(false);
		unloadLibrary.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		unloadLibrary
				.setToolTipText("Click to remove the selected library from the selected engine");
		unloadLibrary.setText("Remove");

		scopeGroup = new Group(container, SWT.NONE);
		scopeGroup.setText("Default scope of:                  ");
		scopeGroup.setLayout(new RowLayout(SWT.HORIZONTAL));

		Vector<String> theories = new Vector<String>();
		try {
			IResource[] resources = ((IProject) getElement()).members();
			for (int j = 0; j < resources.length; j++)
				if ((resources[j] instanceof IFile)
						&& (resources[j].getName().endsWith(".pl")))
					theories.add(resources[j].getName());
		} catch (CoreException e) {
		}

		theoriesButtons = new Button[theories.size()];
		for (int j = 0; j < theories.size(); j++) {
			theoriesButtons[j] = new Button(scopeGroup, SWT.CHECK);
			theoriesButtons[j].setEnabled(false);
			theoriesButtons[j].addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					onCheckChanged((Button) e.widget);
				}

				private void onCheckChanged(Button button) {
					int size = 0;
					for (int i = 0; i < theoriesButtons.length; i++)
						if (theoriesButtons[i].getSelection())
							size++;
					String[] theories = new String[size];
					for (int i = 0; i < size; i++) {
						boolean match = false;
						for (int j = i; j < theoriesButtons.length
								&& match == false; j++) {
							if (theoriesButtons[j].getSelection()) {
								match = true;
								theories[i] = theoriesButtons[j].getText();
							}
						}
					}
					PropertyManager.setTheoriesInProperty(
							(IProject) getElement(), motoreScelto, theories,
							false);

				}

			});
			theoriesButtons[j].setText(theories.elementAt(j));
		}
		return container;
	}

	// Setta i valori di default quando il bottone "Apply default" viene premuto
	protected void performDefaults() {
		final String name = ((IResource) getElement()).getName();
		for (int i = 0; i < listEngine.getItems().length; i++) {
			PrologEngineFactory.getInstance().deleteEngine(name,
					listEngine.getItem(i));
			listEngine.remove(listEngine.getItem(i));
			PropertyManager.deleteEngineInProperties((IProject) getElement(),
					listEngine.getItem(i), listEngine.getItems());
		}
		PrologEngine engine = PrologEngineFactory.getInstance().insertEntry(
				name, "Engine1");
		String[] libs = engine.getLibrary();
		for (int i = 0; i < libs.length; i++)
			engine.removeLibrary(libs[i]);
		String[] libraries = new String[4];
		libraries[0] = "alice.tuprolog.lib.BasicLibrary";
		libraries[1] = "alice.tuprolog.lib.IOLibrary";
		libraries[2] = "alice.tuprolog.lib.ISOLibrary";
		libraries[3] = "alice.tuprolog.lib.JavaLibrary";
		for (int i = 0; i < libraries.length; i++)
			engine.addLibrary(libraries[i]);
		PropertyManager.addEngineInProperty((IProject) getElement(),
				engine.getName());
		PropertyManager.setLibraryInProperties((IProject) getElement(),
				engine.getName(), libraries);
		PropertyManager.setTheoriesInProperty((IProject) getElement(),
				engine.getName(), null, true);

		listEngine.removeAll();
		listEngine.add("Engine1");
		listLibrary.removeAll();
	}

	// Metodo invocato quando il bottone "Ok" viene premuto
	public boolean performOk() {
		return true;
	}

}
