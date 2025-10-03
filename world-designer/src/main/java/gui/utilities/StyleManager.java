package gui.utilities;

import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class StyleManager
{
	public enum FontStyle{
		HEADER1("Header 1(" + StyleManager.HEADER1.getSize() +")", StyleManager.HEADER1),
		HEADER2("Header 2(" + StyleManager.HEADER2.getSize() +")", StyleManager.HEADER2),
		HEADER3("Header 3(" + StyleManager.HEADER3.getSize() +")", StyleManager.HEADER3),
		PLAIN1("Body 1: Plain(" + StyleManager.BODY_PLAIN.getSize() +")", StyleManager.BODY_PLAIN),
		BOLD1("Body 1: Bold(" + StyleManager.BODY_BOLD.getSize() +")", StyleManager.BODY_BOLD),
		ITALIC1("Body 1: Italic(" + StyleManager.BODY_ITALIC.getSize() +")", StyleManager.BODY_ITALIC),
		PLAIN2("Body 2: Plain(" + StyleManager.BODY2_PLAIN.getSize() +")", StyleManager.BODY2_PLAIN),
		BOLD2("Body 2: Bold(" + StyleManager.BODY2_BOLD.getSize() +")", StyleManager.BODY2_BOLD),
		ITALIC2("Body 2: Italic(" + StyleManager.BODY2_ITALIC.getSize() +")", StyleManager.BODY2_ITALIC);
		
		
		private String name;
		private Font font;
		
		FontStyle(String name, Font font){
			this.name = name;
			this.font = font;
		}
		
		public String toString() {
			return name;
		}
		
		public Font getFont() {
			return font;
		}
	}
	public static final Font HEADER1 = new Font("Monospaced", Font.BOLD, 48);
	public static final Font HEADER2 = new Font("Monospaced", Font.BOLD, 34);
	public static final Font HEADER3 = new Font("Monospaced", Font.BOLD, 22);
	public static final Font BODY_PLAIN = new Font("Monospaced", Font.PLAIN, 18);
	public static final Font BODY_BOLD = new Font("Monospaced", Font.BOLD, 18);
	public static final Font BODY_ITALIC = new Font("Monospaced", Font.ITALIC, 18);
	public static final Font BODY2_PLAIN = new Font("Monospaced", Font.PLAIN, 16);
	public static final Font BODY2_BOLD = new Font("Monospaced", Font.BOLD, 16);
	public static final Font BODY2_ITALIC = new Font("Monospaced", Font.ITALIC, 16);
	
	
	public static final Font BUTTON = new Font("Monospaced", Font.BOLD, 18);
	
	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		UIManager.put("Label.font", BODY_PLAIN);
		UIManager.put("Button.font", BUTTON);
		UIManager.put("TextPane.font", BODY_PLAIN);
	}
}