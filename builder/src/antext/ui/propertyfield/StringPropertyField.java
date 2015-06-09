package antext.ui.propertyfield;

import java.awt.BorderLayout;

import javax.swing.JTextField;

import antext.PropertyFile;

@SuppressWarnings("serial")
public class StringPropertyField extends PropertyField {

	private JTextField txtPropertyValue;
	
	public StringPropertyField(PropertyFile file, String propertyName) {
		super(file, propertyName);
	}

	@Override
	public String getValue() {
		return txtPropertyValue.getText();
	}

	@Override
	public void setValue(String value) {
		txtPropertyValue.setText(value);
	}

	@Override
	public void initializeComponent() {
		txtPropertyValue = new JTextField();
		add(txtPropertyValue, BorderLayout.CENTER);	
	}

	@Override
	public int getStatus() {
		return STATUS_OK;
	}

	@Override
	public String getStatusToolTop() {
		return "";
	}

}
