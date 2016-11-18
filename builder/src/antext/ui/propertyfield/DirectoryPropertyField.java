package antext.ui.propertyfield;

import antext.PropertyFile;

@SuppressWarnings("serial")
public class DirectoryPropertyField extends AbstractFilePropertyField {

	public DirectoryPropertyField(PropertyFile propertyFile, String propertyName) {
		super(propertyFile, propertyName, true);
	}

}
