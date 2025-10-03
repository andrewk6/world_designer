package gui.editor;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import data.predefined.WorldArticle;
import gui.components.editor.RichEditor;
import gui.editor.ProjectNode.NodeType;
import gui.utilities.CompFactory;

public class WorldEditorPane extends JPanel {
	private JPanel editPane;
	private TreePane treePane;

	public WorldEditorPane(File root) {
		super();
		init();
		buildToolbar();
	}

	private void init() {
		this.setLayout(new BorderLayout());

		JSplitPane splitPane = new JSplitPane();
		this.add(splitPane, BorderLayout.CENTER);
		
		editPane = new JPanel();
		editPane.setLayout(new BorderLayout());
		splitPane.setRightComponent(editPane);
		
		treePane = new TreePane(editPane);
		splitPane.setLeftComponent(treePane);
	}
	
	private void buildToolbar() {
		JToolBar menu = new JToolBar();
		this.add(menu, BorderLayout.NORTH);
		
		menu.add(CompFactory.createMenuItem("New Campaign", e->{
			String worldName = JOptionPane.showInputDialog(this, "What is the world's name?");
			if(worldName.length() > 0) {
				WorldArticle w = new WorldArticle(worldName);
				treePane.processNewNode(NodeType.PROJECT, w);
			}			
		}));
	}
}