package gui.utilities;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageManager
{
	public enum MapIcon{
		CITY("City", CITY_ICON),
		EMPTY("Icon Eraser", null);
		
		Image img;
		String name;
		MapIcon(String name, Image img){
			this.name = name;
			this.img = img;
		}
		
		public String getName() {
			return name;
		}
		
		public Image getIcon() {
			return img;
		}
	}
	public static final Image CITY_ICON;
	

	public static final Image SELECT_TOOL;
	public static final Image PAINT_TOOL;
	public static final Image FILL_TOOL;

	static {
		//Read in CityIcon
	    Image temp = null;
	    try (InputStream in = ImageManager.class.getResourceAsStream("/mapicons/CityIcon.png")) {
	        if (in != null) {
	            temp = ImageIO.read(in);
	        } else {
	            System.err.println("Resource not found: mapicons/CityIcon.png");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    CITY_ICON = temp;
	    
	    temp = null;
	    try (InputStream in = ImageManager.class.getResourceAsStream("/toolicons/selector.png")) {
	        if (in != null) {
	            temp = ImageIO.read(in);
	        } else {
	            System.err.println("Resource not found: /toolicons/selector.png");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    SELECT_TOOL = temp;
	    
	    temp = null;
	    try (InputStream in = ImageManager.class.getResourceAsStream("/toolicons/paintbrush.png")) {
	        if (in != null) {
	            temp = ImageIO.read(in);
	        } else {
	            System.err.println("Resource not found: /toolicons/paintbrush.png");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    PAINT_TOOL = temp;
	    
	    temp = null;
	    try (InputStream in = ImageManager.class.getResourceAsStream("/toolicons/paintbucket.png")) {
	        if (in != null) {
	            temp = ImageIO.read(in);
	        } else {
	            System.err.println("Resource not found: /toolicons/paintbucket.png");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    FILL_TOOL = temp;
	}
}