package alice.tuprologx.eclipse.properties;

import org.eclipse.jface.dialogs.IInputValidator;

public class LibraryValidator implements IInputValidator {
	private String[] list;

	public LibraryValidator(String[] strings) {
		this.list = strings;
	}

	public String isValid(String scelta) {
		if (scelta.equals("alice.tuprolog.lib."))
			return "";
		for (int i = 0; i < list.length; i++)
			if (list[i].equals(scelta))
				return "Library already loaded";
		if (!scelta.equals("alice.tuprolog.lib.BasicLibrary")
				&& !scelta.equals("alice.tuprolog.lib.IOLibrary")
				&& !scelta.equals("alice.tuprolog.lib.JavaLibrary")
				&& !scelta.equals("alice.tuprolog.lib.ISOLibrary"))
			return "Library not exist";
		return null;
	}
}
