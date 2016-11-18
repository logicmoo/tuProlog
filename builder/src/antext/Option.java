package antext;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Target;

import antext.res.ResourceLoader;

public class Option extends ConfigureTaskNestedElement {
	
	protected Option parent;
	protected String name;
	protected String display;
	protected ImageIcon icon;
	protected Platform platform;
	protected ArrayList<Option> childrenOptions;
	protected Vector<String> targets;
	protected int nestingLevel;
	
	public Option(ConfigureTask owner, Option parent, Platform platform) {
		super(owner);
		
		this.platform = platform;
		
		childrenOptions = new ArrayList<>();
		targets = new Vector<>();
		this.parent = parent;
		
		if(parent == null) {
			nestingLevel = 0;
		} else {
			nestingLevel = parent.getNestingLevel() + 1;
		}
		
	}
	
	public void setIcon(String img) throws BuildException {
		icon = ResourceLoader.getImage(img);
		if(icon == null) {
			throw new BuildException("Invalid option image: " + img);
		}
	}
	
	public ImageIcon getIcon() {
		return icon;
	}
	
	public void setTargets(String targetList) throws Exception {
		String[] split = targetList.split(",");
		Hashtable<String, Target> existingTargets = getOwner().getProject().getTargets();
		
		for(String target : split) {		
			target = target.trim();
			if(existingTargets.containsKey(target)) {
				targets.addElement(target);
			} else if(!target.isEmpty()) {
				throw new Exception("Target not found: " + target);
			}
		}
	}	

	public Vector<String> getTargets() {
		return targets;
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
		return display;
	}
	
	public Platform getPlatform() {
		return platform;
	}
	
	public Option getParent() {
		return parent;
	}
	
	public int getNestingLevel() {
		return nestingLevel;
	}
	
	public Option createOption() {
		Option b = new Option(getOwner(),this, platform);
		childrenOptions.add(b);
		return b;
	}
	
	public List<Option> getChildrenOptions() {
		return childrenOptions;
	}
	
	public boolean equals(Object other) {
		if(!(other instanceof Option))
			return false;
		else
			return ((Option)other).getName().equals(this.getName());
	}
	
	public static class NestingLevelComparator implements Comparator<Option> {
		@Override
		public int compare(Option o1, Option o2) {
			return o1.getNestingLevel() - o2.getNestingLevel();
		}
	}
}
