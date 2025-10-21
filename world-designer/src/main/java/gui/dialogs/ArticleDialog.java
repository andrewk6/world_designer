package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

import data.DataManager;
import data.MapKey;
import gui.utilities.CompFactory;
import gui.utilities.TestingFrame;

public class ArticleDialog extends JDialog
{
	public static void main(String[]args) throws BadLocationException {
		TestingFrame test = new TestingFrame(new JPanel());
		test.testRun();
		ArticleDialog.showArticleSelectDialog(test, test.getData());
		System.exit(0);
	}
	private ArrayList<JComboBox<MapKey>> keyCombos;
	private DataManager data;
	
	public ArticleDialog(Window win, DataManager data)
	{
		super(win, "Article Selector", ModalityType.APPLICATION_MODAL);
		this.data = data;
		keyCombos = new ArrayList<JComboBox<MapKey>>();
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
		
		JPanel articlePane = new JPanel();
		articlePane.setLayout(new BorderLayout());
		cPane.add(articlePane, BorderLayout.CENTER);
		
		JPanel selectPane = new JPanel();
		selectPane.setLayout(new BoxLayout(selectPane, BoxLayout.Y_AXIS));
		JScrollPane selectScroll = CompFactory.createScroll(selectPane);
		articlePane.add(selectScroll, BorderLayout.CENTER);
		
		addComboBox(selectPane);
		
		Dimension dim = CompFactory.createCombo(
				data.getMapKeysSorted().toArray(new MapKey[0])).getPreferredSize();
		
		JButton addArticle = CompFactory.createButton("Additional Article", e->{
			addComboBox(selectPane);
			selectPane.revalidate();
			selectPane.repaint();
		});
		articlePane.add(addArticle, BorderLayout.SOUTH);
		
		cPane.add(CompFactory.createButtonFlow(FlowLayout.RIGHT, 
				new JButton[] {
					CompFactory.createButton("Finished", this::finish),
					CompFactory.createButton("Cancel", this::cancel)
				}), BorderLayout.SOUTH);
	}
	
	private void addComboBox(JPanel pane) {
		MapKey[] keyArray = data.getMapKeysSorted().toArray(new MapKey[0]);
		JComboBox<MapKey> keyCombo = CompFactory.createCombo(keyArray);
		pane.add(keyCombo);
	}
	
	private void finish() {
		this.setVisible(false);
	}
	
	private void cancel() {
		this.keyCombos = null;
		this.setVisible(false);
	}
	
	public List<MapKey> getKeys(){
		if(keyCombos != null) {
			List<MapKey> keys = new ArrayList<MapKey>();
			for(JComboBox<MapKey> combo : keyCombos) {
				if(!keys.contains(combo.getSelectedItem()))
					keys.add((MapKey) combo.getSelectedItem());
			}
			return Collections.unmodifiableList(keys);
		}
		return null;
	}
	
	public static List<MapKey> showArticleSelectDialog(Window win, DataManager data){
		ArticleDialog d = new ArticleDialog(win, data);
		d.pack();
		d.setSize(d.getSize().width, d.getSize().height * 2);
		d.setVisible(true);
		return d.getKeys();
	}
}