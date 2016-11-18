package antext;

import java.util.ArrayList;
import java.util.List;

public class BuildOptions extends ConfigureTaskNestedElement {
	
	protected ArrayList<Platform> platforms;
	
	public BuildOptions(ConfigureTask owner) {
		super(owner);
		platforms = new ArrayList<>();
	}
	
	public Platform createPlatform() {
		Platform pf = new Platform(getOwner());
		platforms.add(pf);
		return pf;
	}
	
	public List<Platform> getPlatforms() {
		return platforms;
	}
}
