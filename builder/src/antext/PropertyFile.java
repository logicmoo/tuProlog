package antext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class PropertyFile extends ConfigureTaskNestedElement {
	
	protected String display;
	protected Properties properties;
	protected ArrayList<PropertyDef> propertyDefs;
	protected File propertyFile;
	
	public PropertyFile(ConfigureTask owner) {
		super(owner);
		propertyDefs = new ArrayList<>();
	}
	
	public void store() throws IOException {
		properties.store(new FileWriter(propertyFile), null);
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getDisplay() {
		if(display == null || display.isEmpty())
			return propertyFile.getName();
		else
			return display;
	}
	
	public void setPath(String path) throws FileNotFoundException, IOException {
		propertyFile = new File(path);
		
		if(!propertyFile.exists())
			throw new FileNotFoundException("Property file not found: " + propertyFile.getAbsolutePath());
		
		FileInputStream in = new FileInputStream(propertyFile);
		properties = new Properties();
		properties.load(in);
		in.close();
		
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	public File getPropertyFile() {
		return this.propertyFile;
	}
	
	public PropertyDef createPropertyDef() {
		PropertyDef def = new PropertyDef(this.getOwner());	
		propertyDefs.add(def);
		return def;
	}
	
	public int getPropertyType(String propertyName) {
		
		for(PropertyDef def : propertyDefs) {
			if(def.getName().equals(propertyName)) {
				return def.getType();
			}
		}
		
		return PropertyDef.TYPE_STRING;
	}
	
	public String getPropertyDisplay(String propertyName) {
		for(PropertyDef def : propertyDefs) {
			if(def.getName().equals(propertyName)) {
				return def.getDisplay();
			}
		}
		
		return propertyName;
	}

}
