package antext.ui;

import java.awt.Dialog;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import antext.ConfigureTask;
import antext.PropertyFile;

@SuppressWarnings("serial")
//public class ConfigureDialog extends JFrame {
public class ConfigureDialog extends JDialog {	
	private ConfigureTask configure;
	
	private JTabbedPane uiTabbedPane;
	private PropertyFilePanel[] uiPropertyFilePanels;
	
	public ConfigureDialog(ConfigureTask configure) {
		super((Dialog)null, true); // modal
		
		this.configure = configure;
		
		initComponents();
		
		setTitle(configure.getProject().getName());
		
		pack();
		
		validate();
	}
	
	public void updatePropertyFiles() throws IOException {
		for(PropertyFilePanel p: uiPropertyFilePanels) {
			p.updatePropertyFile();
		}	
	}
	
	public ConfigureTask getConfigureTask() {
		return this.configure;
	}
	
	private void initComponents() {
		uiTabbedPane = new JTabbedPane();
		uiPropertyFilePanels = new PropertyFilePanel[configure.getPropertyFiles().size()];
		
		uiTabbedPane.addTab("Build Options", new BuildOptionsPanel(this));
		
		for(int i = 0; i < configure.getPropertyFiles().size(); i++) {
			PropertyFile pf = configure.getPropertyFiles().get(i);
			uiPropertyFilePanels[i] = new PropertyFilePanel(pf);
			uiTabbedPane.addTab(pf.getDisplay(), null, uiPropertyFilePanels[i], null);
		}		
		
		getContentPane().add(uiTabbedPane);
	}
	
	
	

}
