package antext.ui.propertyfield;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import antext.PropertyFile;
import antext.res.ResourceLoader;

@SuppressWarnings("serial")
public abstract class PropertyField extends JPanel {
	
	public static final int STATUS_OK = 0;
	public static final int STATUS_WARNING = 1;
	
	private ArrayList<Listener> listeners;
	
	private JLabel lblStatus;
	
	protected PropertyFile propertyFile;
	protected String propertyName;
	
	public PropertyField(PropertyFile file, String propertyName) {
		this.propertyFile = file;
		this.propertyName = propertyName;
		
		this.listeners = new ArrayList<>();
		
		this.lblStatus = new JLabel();
		
		lblStatus.setPreferredSize(new Dimension(30, 20));
		
		setLayout(new BorderLayout());
		
		add(lblStatus, BorderLayout.EAST);
		
		initializeComponent();
		
		setValue(file.getProperties().getProperty(propertyName).toString());
	}
	
	
	public String getPropertyName() {
		return this.propertyName;
	}
	
	public PropertyFile getPropertyFile() {
		return this.propertyFile;
	}
	
	public void addListener(Listener l) {
		listeners.add(l);
	}
	
	public void removeListener(Listener l) {
		listeners.remove(l);
	}
	
	public abstract void initializeComponent();

	public abstract int getStatus();
	
	public abstract String getStatusToolTop();	
	
	public abstract String getValue();
	
	public abstract void setValue(String value);
	
	protected void fireValueChanged() {
		updateStatus();
		for(Listener l : listeners)
			l.valueChanged(this);
	}
	
	
	private void updateStatus() {
		switch(getStatus()) {
		case STATUS_OK:
			lblStatus.setIcon(ResourceLoader.getImage("ok"));
			lblStatus.setToolTipText("");
			break;
		case STATUS_WARNING:
			lblStatus.setIcon(ResourceLoader.getImage("remove"));
			lblStatus.setToolTipText(getStatusToolTop());
			break;
		default:
			lblStatus.setIcon(ResourceLoader.getImage("ok"));
			lblStatus.setToolTipText("");
		}
		
	}
	
	public static interface Listener {
		public void valueChanged(PropertyField source);
	}
	
	public static class NameComparator implements Comparator<PropertyField> {

		@Override
		public int compare(PropertyField a, PropertyField b) {
			return a.getPropertyName().compareTo(b.getPropertyName());
		}
		
	}
	
}
