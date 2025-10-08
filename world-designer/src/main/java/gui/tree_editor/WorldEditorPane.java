package gui.tree_editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import data.ActiveWorldListener;
import data.DataManager;
import data.predefined.FolderArticle;
import data.predefined.WorldArticle;
import gui.components.editor.RichEditor;
import gui.tree_editor.ProjectNode.NodeType;
import gui.utilities.CompFactory;
import gui.utilities.StyleManager.FontStyle;

public class WorldEditorPane extends JPanel implements ActiveWorldListener
{
	private final DataManager data;
	private JPanel editPane;
	private TreePane treePane;
	private JLabel worldLabel;

	public WorldEditorPane(DataManager data) {
		super();
		this.data = data;
		this.data.registerWorldListener(this);
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
		
		treePane = new TreePane(data, editPane);
		splitPane.setLeftComponent(treePane);
	}
	
	private void buildToolbar() {
		JPanel topPane = new JPanel();
		topPane.setLayout(new BorderLayout());
		this.add(topPane, BorderLayout.NORTH);
		
		JPanel menu = CompFactory.createButtonFlow(FlowLayout.LEFT, new JButton[] {
				CompFactory.createButton("New World", e->{
					String worldName = JOptionPane.showInputDialog(this, "What is the world's name?");
					if(worldName != null) {
						if(worldName.length() > 0) {
							WorldArticle w = new WorldArticle(worldName);
							data.addWorld(w);
							data.setActiveWorld(w);
							treePane.processNewWorld(w);
						}
					}
				}),
				CompFactory.createButton("New Category", e->{
					String name = JOptionPane.showInputDialog(this, "What is the category?");
					treePane.processNewCategory(new FolderArticle(name));
				})
		});
		topPane.add(menu, BorderLayout.CENTER);
		
		worldLabel = CompFactory.createLabel("No Active World", FontStyle.BOLD1);
		topPane.add(worldLabel, BorderLayout.EAST);
	}

	@Override
	public void onActiveWorldChange() {
		worldLabel.setText(data.getActiveWorld().name);
	}
}