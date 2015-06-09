package antext;

public abstract class ConfigureTaskNestedElement {
	
	protected ConfigureTask configure;
	
	public ConfigureTaskNestedElement(ConfigureTask owner) {
		this.configure = owner;
	}
	
	public ConfigureTask getOwner() { 
		return this.configure; 
	}
}
