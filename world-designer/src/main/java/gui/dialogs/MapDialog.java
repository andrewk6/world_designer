package gui.dialogs;

import java.awt.Window;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import data.DataManager;
import data.MapKey;
import data.map.LandMap;
import gui.components.ReminderField;
import gui.utilities.CompFactory;

public class MapDialog extends JDialog {
	private LandMap map = null;
	private ReminderField nameField, rField, cField;
	
	private MapDialog(Window win) {
		super(win, "Article Selector", ModalityType.APPLICATION_MODAL);
		init(getContentPane());

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
	}

	private void init(Container c) {
		c.setLayout(new BorderLayout());
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new GridLayout(0,1));
		c.add(mainPane, BorderLayout.CENTER);
		
		nameField = new ReminderField("Map name...");
		rField = new ReminderField("Rows...");
		rField.setNumbersOnly();
		cField = new ReminderField("Columns...");
		cField.setNumbersOnly();
		
		mainPane.add(CompFactory.createSplitPane("Map Name: ", nameField));
		mainPane.add(CompFactory.createSplitPane("Rows: ", rField));
		mainPane.add(CompFactory.createSplitPane("Columns: ", cField));
		
		JPanel btnPane = CompFactory.createButtonFlow(FlowLayout.RIGHT, new JButton[] {
				CompFactory.createButton("Finish", this::finish),
				CompFactory.createButton("Cancel", this::cancel)
		});
		c.add(btnPane, BorderLayout.SOUTH);
	}

	private void finish() {
		map = new LandMap(new MapKey(nameField.getText()), 
				rField.getInteger(), cField.getInteger());
		setVisible(false);
	}
	
	private void cancel() {
		map = null;
		setVisible(false);
	}
	
	public LandMap getMap() {
		return map;
	}
	
	public static LandMap showDialog() {
		MapDialog dialog = new MapDialog(null);
		dialog.pack();
		dialog.setVisible(true);
		dialog.dispose();
		return dialog.getMap();
	}
	
	public static LandMap showDialog(Window frm) {
		MapDialog dialog = new MapDialog(frm);
		dialog.pack();
		dialog.setVisible(true);
		dialog.dispose();
		return dialog.getMap();
	}
}