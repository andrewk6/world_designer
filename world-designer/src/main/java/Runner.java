import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import data.DataManager;
import gui.WorldDesignerApp;
import gui.utilities.StyleManager;

/*TODO: Change implementation: Use a system to define the types, those types create
 * the different node folders, rather than let people define their own folders.  That will allow
 * easier serialization/deserialization.
 * 
 * Map structure / data storage ignores this, contained within just the Article class, specifically
 * the Basic Article
 */

public class Runner
{
	public static void main(String[]args) {
		DataManager data = new DataManager();
		SwingUtilities.invokeLater(()->{
			StyleManager.setLookAndFeel();
			
			WorldDesignerApp worldApp = new WorldDesignerApp(data);
			worldApp.launch();
		});
	}
}