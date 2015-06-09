package antext;

import org.apache.tools.ant.BuildException;

public class PropertyDef extends ConfigureTaskNestedElement {

	public static final int TYPE_STRING = 1;
	public static final int TYPE_FILE = 2;
	public static final int TYPE_DIRECTORY = 3;
	public static final int TYPE_BOOLEAN = 4;
	
	protected String name;
	protected String display;
	protected int type;
	
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
	}
	
	public int getType() {
		return this.type;
	}

}
