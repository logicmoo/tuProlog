package antext.ui.propertyfield;

import java.io.File;

import antext.PropertyFile;

@SuppressWarnings("serial")
public class FilePropertyField extends AbstractFilePropertyField {
	public FilePropertyField(PropertyFile propertyFile, String propertyName) {
		super(propertyFile, propertyName, false);
	}
}
