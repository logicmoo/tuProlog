package antext.res;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceLoader {
	private static File resDirectory = new File("res");
	private static HashMap<String, ImageIcon> images;
	
	static {
		loadPNGImages();
	}
	
	public static ImageIcon getImage(String key) {
		return images.get(key);
	}
	
	private static void loadPNGImages() {
		images = new HashMap<>();
		for(File file : resDirectory.listFiles()) {
			String fileName = file.getName();
			if(fileName.endsWith(".png")) {
				try {
					Image img = ImageIO.read(file);
					images.put(fileName.substring(0, fileName.length() - 4), new ImageIcon(img));
				}
				catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
