package antext.ui.propertyfield;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import antext.PropertyFile;

@SuppressWarnings("serial")
public abstract class AbstractFilePropertyField extends PropertyField implements ActionListener, KeyListener {
	
	private boolean directory;
	
	private JFileChooser fcBrowse;
	private JButton btnBrowse;
	private JTextField txtFileName;
	

	public AbstractFilePropertyField(PropertyFile propertyFile, String propertyName) {
		this(propertyFile, propertyName, false);
	}
	
	public AbstractFilePropertyField(PropertyFile propertyFile, String propertyName, boolean directory) {
		super(propertyFile, propertyName);
		this.directory = directory;
	}

	@Override
	public void initializeComponent() {
		btnBrowse = new JButton("...");
		txtFileName = new JTextField();
		
		txtFileName.addActionListener(this);
		
		txtFileName.addKeyListener(this);
		
		fcBrowse = new JFileChooser();
		
		btnBrowse.addActionListener(this);
		
		add(btnBrowse, BorderLayout.WEST);
		add(txtFileName, BorderLayout.CENTER);
	}
	
	@Override
	public int getStatus() {
		return checkFileExists() ? STATUS_OK : STATUS_WARNING;
	}

	@Override
	public String getStatusToolTop() {
		return checkFileExists() ? "" : "The selected file does not exist";
	}


	@Override
	public String getValue() {
		return txtFileName.getText();
	}

	@Override
	public void setValue(String value) {
		txtFileName.setText(value);
		fireValueChanged();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnBrowse) {
			
			if(directory)
				fcBrowse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			if(fcBrowse.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				setValue(fcBrowse.getSelectedFile().getAbsolutePath());
			}
		}
	}
	
	private boolean checkFileExists() {
		File file = new File(txtFileName.getText());
		return file.exists() && ((directory && file.isDirectory()) || (!directory));
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getSource() == txtFileName) {
			fireValueChanged();
		}
	}
}
