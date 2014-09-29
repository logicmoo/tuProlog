package alice.tuprologx.eclipse.properties;

import org.eclipse.jface.dialogs.IInputValidator;

public class EngineValidator implements IInputValidator {
	private String[] list;

	public EngineValidator(String[] strings) {
		this.list = strings;
	}

	public String isValid(String scelta) {
		if (scelta.equals(""))
			return "";
		if (scelta.indexOf(";") >= 0)
			return "Engine name cannot contain the character ';'";
		for (int i = 0; i < list.length; i++)
			if (list[i].equals(scelta))
				return "Engine name already used";
		return null;
	}

}
