package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import data.DataManager;
import data.MapKey;
import gui.utilities.CompFactory;

public class MapLoadDialog extends JDialog {
	private MapKey selected = null;
	
	private JComboBox<MapKey> keys;
	
	private MapLoadDialog(Window frm, DataManager data) {
		super(frm, "Map Load Dialog", ModalityType.APPLICATION_MODAL);
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		MapKey[] mapKeys = data.getMapKeys().stream().toArray(MapKey[]::new);
		keys = CompFactory.createCombo(mapKeys);
		init(getContentPane());
	}
	
	private void init(Container c) {
		c.setLayout(new BorderLayout());
		
		c.add(keys, BorderLayout.CENTER);
		
		JPanel btnPane = CompFactory.createButtonFlow(FlowLayout.RIGHT, new JButton[] {
				CompFactory.createButton("Finish", this::finish),
				CompFactory.createButton("Cancel", this::cancel)
		});
		c.add(btnPane, BorderLayout.SOUTH);
	}
	
	private void finish() {
		selected = (MapKey) keys.getSelectedItem();
		setVisible(false);
	}
	
	private void cancel() {
		selected = null;
		setVisible(false);
	}
	
	public MapKey getSelected() {
		return selected;
	}
	
	public static MapKey showDialog(Window frm, DataManager data) {
		MapLoadDialog loadDialog = new MapLoadDialog(frm, data);
		loadDialog.pack();
		loadDialog.setVisible(true);
		loadDialog.dispose();
		return loadDialog.getSelected();
	}
}