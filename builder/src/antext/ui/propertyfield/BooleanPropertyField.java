package antext.ui.propertyfield;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;

import antext.PropertyFile;

@SuppressWarnings("serial")
public class BooleanPropertyField extends PropertyField {
	
	private JCheckBox chkValue;

	public BooleanPropertyField(PropertyFile file, String propertyName) {
		super(file, propertyName);
	}

	@Override
	public void initializeComponent() {
		chkValue = new JCheckBox();
		add(chkValue, BorderLayout.WEST);
	}

	@Override
	public String getValue() {
		return new Boolean(chkValue.isSelected()).toString();
	}

	@Override
	public void setValue(String value) {
		chkValue.setSelected(new Boolean(value));
	}

	@Override
	public int getStatus() {
		return STATUS_HIDE;
	}

	@Override
	public String getStatusToolTop() {
		return "";
	}

}
