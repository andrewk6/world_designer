package gui.design_panes.article_editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import data.DataManager;
import data.MapKey;
import data.listeners.MapKeyNameListener;
import data.predefined.BasicArticle;
import gui.design_panes.ArticleDesignPane;
import gui.utilities.CompFactory;
import gui.utilities.StyleManager;

public class ArticleTree extends JTree {
	private DataManager data;
	private SortedTreeModel sortedModel;
	private NodePopup rootMenu, folderMenu, articleMenu;
	private DefaultMutableTreeNode selectedNode;
	private ArrayList<SelectedNodeListener> selectedNodeListeners;

	private JTabbedPane editTabs;

	public ArticleTree(DataManager data, JTabbedPane tabs) {
		this(data);
		editTabs = tabs;
	}

	public ArticleTree(DataManager data) {
		super();
		sortedModel = new SortedTreeModel(data.getWorldTreeRoot());
		setModel(sortedModel);
		this.data = data;
		selectedNodeListeners = new ArrayList<SelectedNodeListener>();
		buildPopups();

		setRootVisible(false);
		setShowsRootHandles(true);
		setEditable(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath path = getPathForLocation(e.getX(), e.getY());
				DefaultMutableTreeNode node = null;
				if (path != null) {

					setSelectionPath(path); // select before showing menu
					node = (DefaultMutableTreeNode) path.getLastPathComponent();

					if (node.getUserObject() instanceof String)
						folderMenu.node = node;
					else
						articleMenu.node = (ArticleNode) node;
				}
				
				if(node != null) {
					if(selectedNode != node) {
						selectedNode = node;
						notifySelectionChange();
					}
				}else{
					selectedNode = null;
					clearSelection();
					notifySelectionChange();
				}

				if (SwingUtilities.isRightMouseButton(e)) {
					if (node != null) {
						if (node.getUserObject() instanceof String) {

							folderMenu.show(ArticleTree.this, e.getX(), e.getY());
						} else {
							articleMenu.show(ArticleTree.this, e.getX(), e.getY());
						}
					} else {
						rootMenu.show(ArticleTree.this, e.getX(), e.getY());
					}
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					if(node != null) {						
						if(node.getUserObject() instanceof MapKey)
							editArticle(node);
					}
				}
			}
		});

		setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {

				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

				// If it's meant to be a "folder" (e.g. userObject is a String)
				// force it to always use the folder icon.
				boolean isFolder = node.getUserObject() instanceof String;
				if (isFolder) {
					leaf = false;
					setIcon(expanded ? getOpenIcon() : getClosedIcon());
				} else if (node.getUserObject() instanceof MapKey) {
					URL iconURL = getClass().getResource(StyleManager.DOC_ICON_RESOURCE);
					Icon leafIcon;
					if (iconURL != null) {
						ImageIcon original = new ImageIcon(iconURL);
						Image scaled = original.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
						leafIcon = new ImageIcon(scaled);
					} else {
						leafIcon = UIManager.getIcon("FileView.fileIcon"); // fallback
						System.err.println("Could not find /doc_icon.png in resources!");
					}
					setIcon(leafIcon);
				}
				
				return this;
			}
		});

		setCellEditor(new DefaultTreeCellEditor(this, (DefaultTreeCellRenderer) this.getCellRenderer()) {
			@Override
			public Object getCellEditorValue() {
				Object value = super.getCellEditorValue();

				if (lastPath != null) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPath.getLastPathComponent();
					Object userObj = node.getUserObject();

					// If it's a MapKey, update its name
					if (userObj instanceof MapKey mk) {
						mk.setName(value.toString());
						return mk; // keep the same object
					}
				}
				return value; // fallback for normal nodes
			}
		});
	}

	private void buildPopups() {
		rootMenu = new NodePopup();
		rootMenu.add(CompFactory.createMenuItem("Add Folder", e -> newFolder(data.getWorldTreeRoot())));

		folderMenu = new NodePopup();
		folderMenu.add(CompFactory.createMenuItem(
				"Add Folder", e -> newFolder(folderMenu.node)));
		folderMenu.add(CompFactory.createMenuItem(
				"Add Article", e -> newArticle(folderMenu.node)));
		folderMenu.add(CompFactory.createMenuItem(
				"Rename Folder", e-> renameNode(folderMenu.node)));
		folderMenu.add(CompFactory.createMenuItem(
				"Delete Folder", e-> deleteNode(folderMenu.node)));

		articleMenu = new NodePopup();
		articleMenu.add(CompFactory.createMenuItem(
				"Edit Article", e -> editArticle(articleMenu.node)));
		articleMenu.add(CompFactory.createMenuItem(
				"Rename Article", e->renameNode(articleMenu.node)));
		articleMenu.add(CompFactory.createMenuItem(
				"Delete Article", e->deleteNode(articleMenu.node)));
	}

	public void editArticle(DefaultMutableTreeNode node) {
		if (node instanceof ArticleNode artNode) {
			if (editTabs != null) {
				boolean existingTab = false;
				for(int i = 0; i < editTabs.getTabCount(); i++) {
					if(editTabs.getComponentAt(i) instanceof ArticleDesignPane a) {
						if(a.getArticleKey().equals(artNode.getUserObject())) {
							existingTab = true;
						}
					}
				}
						
				if(!existingTab) {
					MapKey art = (MapKey) artNode.getUserObject();
					ArticleDesignPane artPane = new ArticleDesignPane(data, data.getArticleMap().get(art));
					editTabs.addTab(art.getName(), artPane);
					editTabs.setSelectedComponent(artPane);
					MapKeyNameListener tabRename = new MapKeyNameListener() {
						@Override
						public void onNameChange() {
							editTabs.setTitleAt(editTabs.indexOfComponent(artPane), art.getName());
						}
					};
					art.registerNameListener(tabRename);
				}
			}
		}
	}

	public void newArticle(DefaultMutableTreeNode parent) {
		if (parent != null) {
			String articleName = JOptionPane.showInputDialog(this, "What is the article name?");
			newArticle(parent, articleName);
		}
	}

	public void newArticle(DefaultMutableTreeNode parent, String name) {
		if (parent != null) {
			BasicArticle art = new BasicArticle(name);
			data.addArticleToWorld(art);

			ArticleNode node = new ArticleNode(art.key, this);
			art.key.registerNameListener(node);
			sortedModel.insertNodeInto(node, parent, parent.getChildCount());

			// Expand parent so the new node is visible
			TreePath parentPath = new TreePath(sortedModel.getPathToRoot(parent));
			expandPath(parentPath);

			// Scroll to the newly added node
			TreePath newPath = parentPath.pathByAddingChild(node);
			scrollPathToVisible(newPath);
		}
	}

	public void newFolder(DefaultMutableTreeNode parent) {
		if (parent != null) {
			String folderName = JOptionPane.showInputDialog(this, "What is the folder's name?");
			newFolder(parent, folderName);
		}

	}

	public void newFolder(DefaultMutableTreeNode parent, String parentName) {
		if (parent != null) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(parentName);
			node.setAllowsChildren(true);
			sortedModel.insertNodeInto(node, parent, parent.getChildCount());

			// Expand parent so the new node is visible
			TreePath parentPath = new TreePath(sortedModel.getPathToRoot(parent));
			expandPath(parentPath);

			// Scroll to the newly added node
			TreePath newPath = parentPath.pathByAddingChild(node);
			scrollPathToVisible(newPath);
		}
	}
	
	public void deleteNode(DefaultMutableTreeNode node) {
		if(node.getUserObject() instanceof MapKey m) {
			data.removeArticleFromWorld(m);
			
			for(int i = editTabs.getTabCount() - 1; i >= 0; i--)
				if(editTabs.getComponentAt(i) instanceof ArticleDesignPane aPane)
					if(aPane.getArticleKey().equals(m))
						editTabs.removeTabAt(i);
		}
		sortedModel.removeNodeFromParent(node);
	}

	public void renameNode(DefaultMutableTreeNode node) {
		if (node != null)
			startEditingAtPath(new TreePath(node.getPath()));
	}
	
	public DefaultMutableTreeNode getSelectedNode() {
		return selectedNode;
	}
	
	public void registerSelectedNodeListener(SelectedNodeListener listen) {
		this.selectedNodeListeners.add(listen);
	}
	
	public void notifySelectionChange() {
		for(SelectedNodeListener list : selectedNodeListeners) {
			if(selectedNode != null)
				list.onNodeSelected();
			else
				list.onNodeDeselected();
		}
	}
	
	public void setModel(TreeModel model) {
	    super.setModel(model);

	    if (model instanceof SortedTreeModel m) {
	        m.sortTree();
	        // optional: force UI refresh
	        this.updateUI();
	    }
	}
	
	public void expandAll(boolean expand) {
        TreeNode root = (TreeNode) getModel().getRoot();
        expandAll(new TreePath(root), expand);
    }

    private void expandAll(TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();

        // Recurse into children first
        if (node.getChildCount() >= 0) {
            for (int i = 0; i < node.getChildCount(); i++) {
                TreeNode child = node.getChildAt(i);
                TreePath path = parent.pathByAddingChild(child);
                expandAll(path, expand);
            }
        }

        // Don't collapse the (invisible) root
        if (parent.getParentPath() == null && !isRootVisible()) {
            return;
        }

        // Expand or collapse this path
        if (expand) {
            expandPath(parent);
        } else {
            collapsePath(parent);
        }
    }
}