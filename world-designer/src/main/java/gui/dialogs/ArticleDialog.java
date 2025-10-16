package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import data.MapKey;
import gui.utilities.CompFactory;

public class ArticleDialog extends JDialog
{
	private ArrayList<MapKey> keys;
	
	public ArticleDialog(Window win)
	{
		super(win, "Article Selector", ModalityType.APPLICATION_MODAL);
		keys = new ArrayList<MapKey>();
		init(getContentPane());
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
	}
	
	private void init(Container cPane) {
		cPane.setLayout(new BorderLayout());
		
		JPanel selectPane = new JPanel();
		selectPane.setLayout(new BoxLayout(selectPane, BoxLayout.Y_AXIS));
		cPane.add(CompFactory.createScroll(selectPane), BorderLayout.CENTER);
		
		
		
		cPane.add(CompFactory.createButtonFlow(FlowLayout.RIGHT, 
				new JButton[] {
					CompFactory.createButton("Finished", this::finish),
					CompFactory.createButton("Cancel", this::cancel)
				}), BorderLayout.SOUTH);
	}
	
	private void finish() {
		this.setVisible(false);
	}
	
	private void cancel() {
		this.keys = null;
		this.setVisible(false);
	}
	
	public List<MapKey> getKeys(){
		return Collections.unmodifiableList(keys);
	}
	
	public static List<MapKey> showArticleSelectDialog(Window win){
		ArticleDialog d = new ArticleDialog(win);
		d.setVisible(true);
		return d.getKeys();
	}
}