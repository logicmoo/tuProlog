package antext;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Platform extends ConfigureTaskNestedElement {
	
	protected ArrayList<Option> rootOptions;	
	
	protected String name;
	protected String display;
	protected Color color;
	
	public Platform(ConfigureTask owner) {
		super(owner);
		rootOptions = new ArrayList<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getDisplay() {
		return this.display;
	}
	
	public void setColor(String color) {
		this.color = Color.decode(color);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Option createOption() {
		Option opt = new Option(getOwner(), null, this);
		rootOptions.add(opt);
		return opt;
	}
	
	public List<Option> getRootOptions() {
		return rootOptions;
	}
	
	
}
