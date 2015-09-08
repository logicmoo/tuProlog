package antext;

import org.apache.tools.ant.BuildException;

public class PropertyDef extends ConfigureTaskNestedElement {
	
	protected String name;
	protected String display;
	protected String type;
	
	public PropertyDef(ConfigureTask owner) {
		super(owner);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getDisplay() {
		return this.display;
	}
	
	public void setType(String type) throws BuildException {
		/*
		if(type.equals("string"))
			this.type = TYPE_STRING;
		else if(type.equals("file"))
			this.type = TYPE_FILE;
		else if(type.equals("directory"))
			this.type = TYPE_DIRECTORY;
		else if(type.equals("boolean"))
			this.type = TYPE_BOOLEAN;
		else
			throw new BuildException("PropertyDef.setType(): type not found: " + type);
		*/
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}

}
