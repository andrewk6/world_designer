package gui.design_panes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import data.DataEnums.SettingStyle;
import data.predefined.WorldArticle;
import gui.components.ReminderField;
import gui.components.editor.RichEditor;
import gui.utilities.CompFactory;
import gui.utilities.StyleManager.FontStyle;

public class WorldDesignPane extends JPanel{
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			JFrame frm = new JFrame();
			Container c = frm.getContentPane();
			c.setLayout(new BorderLayout());
			WorldDesignPane wdPane = new WorldDesignPane(new WorldArticle("Test"));
			c.add(wdPane, BorderLayout.CENTER);
			c.add(CompFactory.createButtonFlow(FlowLayout.RIGHT, new JButton[] {
				CompFactory.createButton("Print World", e->{
					System.out.println(wdPane.getWorld().toString());
				})	
			}), BorderLayout.SOUTH);
			frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm.pack();
			frm.setSize(800, 800);
			frm.setVisible(true);
		});
	}
	private WorldArticle world;
	
	public WorldDesignPane(WorldArticle w) {
		this.world = w;
		init();
	}
	
	private void init() {
		this.setLayout(new BorderLayout());
		
		JPanel hPane = new JPanel();
		hPane.setLayout(new BorderLayout());
		this.add(hPane, BorderLayout.NORTH);
		ReminderField nameField = CompFactory.createUpdateField(
				"World name...", FontStyle.HEADER3, text -> world.name = text);
		nameField.setText(world.name);
		hPane.add(CompFactory.createSplitPane("Name: ", nameField),BorderLayout.NORTH);
		
		DefaultListModel<SettingStyle> styleModel = new DefaultListModel<SettingStyle>();
		for(SettingStyle s : world.styles)
			styleModel.addElement(s);
		JList<SettingStyle> list = CompFactory.createList(styleModel);
		styleModel.addListDataListener(new ListDataListener() {
			@Override
			public void intervalAdded(ListDataEvent e) { updateArray(); }
			@Override
			public void intervalRemoved(ListDataEvent e) { updateArray(); }
			@Override
			public void contentsChanged(ListDataEvent e) { updateArray(); }
			
			private void updateArray() {
				System.out.println("Updated");
				world.styles.clear();
				for(int i = 0; i < styleModel.getSize(); i ++)
					world.styles.add(styleModel.getElementAt(i));
			}
		});
		
		JPanel settingPane = new JPanel();
		settingPane.setLayout(new BorderLayout());
		JComboBox<SettingStyle>	styleCombo = CompFactory.getEnumCombo(SettingStyle.class);
		settingPane.add(styleCombo, BorderLayout.NORTH);
		
		JButton addBtn = CompFactory.createButton("Add Style", e->{
			if(!styleModel.contains(styleCombo.getSelectedItem()))
				styleModel.addElement((SettingStyle) styleCombo.getSelectedItem());
		});
		settingPane.add(addBtn, BorderLayout.CENTER);
		
		hPane.add(CompFactory.createSplitPane(settingPane, list));
		
		RichEditor editor = new RichEditor();
		editor.loadDocument(world.worldDoc);
		this.add(editor, BorderLayout.CENTER);
	}
	
	public WorldArticle getWorld() {
		return world;
	}
}