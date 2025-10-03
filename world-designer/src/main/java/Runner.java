import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import data.DataManager;
import gui.editor.WorldEditorPane;
import gui.utilities.StyleManager;
import io.github.geniot.jortho.FileUserDictionary;
import io.github.geniot.jortho.SpellChecker;

public class Runner
{
	public static void main(String[]args) {
		SwingUtilities.invokeLater(()->{
			StyleManager.setLookAndFeel();
			JFrame frm = new JFrame();
			frm.setContentPane(new WorldEditorPane(null));
			frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm.pack();
			frm.setSize(800, 800);
			frm.setVisible(true);
		});
	}
}