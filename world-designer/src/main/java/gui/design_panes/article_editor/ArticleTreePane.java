package gui.design_panes.article_editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import data.DataManager;
import data.MapKey;
import data.predefined.BasicArticle;
import gui.utilities.CompFactory;
import gui.utilities.StyleManager;

public class ArticleTreePane extends JPanel
{
	private DataManager data;
	private ArticleTree tree;
	
	
	public ArticleTreePane(DataManager data) 
	{
		this.data = data;
		initTree();
	}
	
	private void initTree() {
		this.setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		this.add(splitPane, BorderLayout.CENTER);
		
		JTabbedPane editTabs = new JTabbedPane();
		splitPane.setRightComponent(editTabs);
		
		tree = new ArticleTree(data, editTabs);
		JScrollPane treeScroll = new JScrollPane(tree);
		treeScroll.setPreferredSize(new Dimension(300, 0));
		splitPane.setLeftComponent(treeScroll);
	}
	
	public void newFolder(DefaultMutableTreeNode parent) {
		tree.newFolder(parent);
	}
	
	public ArticleTree getTree() {
		return tree;
	}
}