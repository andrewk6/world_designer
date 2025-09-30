import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Runner
{
	public static void main(String[]args) {
		SwingUtilities.invokeLater(()->{
			JFrame frm = new JFrame();
			frm.add(new JLabel("Hello World"));
			frm.pack();
			frm.setVisible(true);
		});
	}
}