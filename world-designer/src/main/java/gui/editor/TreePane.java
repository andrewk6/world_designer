package gui.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import data.abstracts.AbstractArticle;
import data.predefined.EmptyArticle;
import gui.editor.ProjectNode.NodeType;
import gui.utilities.CompFactory;
import gui.utilities.TreeModelAdapter;

public class TreePane extends JPanel
{
	private JPanel editPane;
	private JPopupMenu folderPop, filePop;
	
	private JTree workTree;
	private DefaultMutableTreeNode root;
	
	public TreePane(JPanel editPane) {
		super();
		this.editPane = editPane;
		this.setLayout(new BorderLayout());
		buildPopup();
		buildTreePane();
		this.setPreferredSize(new Dimension(200, 0));
	}
	
	private void buildTreePane() {
		// Root node
		root = new DefaultMutableTreeNode("Root");
		SortedTreeModel model = new SortedTreeModel(root);
		workTree = new JTree(model);
		workTree.setRootVisible(false);
		workTree.setShowsRootHandles(true);
		workTree.setDragEnabled(true);
		workTree.setDropMode(DropMode.ON_OR_INSERT);
		workTree.setEditable(true);
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
		Icon folderIcon = fsv.getSystemIcon(new File("C:\\"));
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer(){
		    @Override
		    public Component getTreeCellRendererComponent(JTree tree, Object value,
		                                                  boolean selected, boolean expanded,
		                                                  boolean leaf, int row,
		                                                  boolean hasFocus) {
		        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		        if (!leaf) { // folder
		            setIcon(folderIcon);   // use same icon for open or closed
		        }

		        return this;
		    }
		};
		renderer.setClosedIcon(folderIcon);
		renderer.setOpenIcon(folderIcon);
		workTree.setCellRenderer(renderer);
		
		workTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					int row = workTree.getRowForLocation(e.getX(), e.getY());
					TreePath path = workTree.getPathForLocation(e.getX(), e.getY());

					if (row != -1 && path != null) {
						// Select the clicked node
						workTree.setSelectionPath(path);
					}
					
					if(workTree.getSelectionPath() != null) {
						if (workTree.getSelectionPath().getLastPathComponent() 
								instanceof ProjectNode node) {
							JPanel newPane = CompFactory.createDesignPane(node.obj);
							if(newPane != null) {
								editPane.removeAll();
								editPane.add(newPane, BorderLayout.CENTER);
								editPane.revalidate();
								editPane.repaint();
							}
						}
					}
				}
				if (SwingUtilities.isRightMouseButton(e)) {

					int row = workTree.getRowForLocation(e.getX(), e.getY());
					TreePath path = workTree.getPathForLocation(e.getX(), e.getY());

					if (row != -1 && path != null) {
						// Select the clicked node
						workTree.setSelectionPath(path);
					}
					if (workTree.getSelectionPath().getLastPathComponent() 
							instanceof ProjectNode node) {
						if (node.type == NodeType.FOLDER || node.type == NodeType.PROJECT)
							folderPop.show(workTree, e.getX(), e.getY());
						else
							filePop.show(workTree, e.getX(), e.getY());
					}

				}
			}
		});
		workTree.getModel().addTreeModelListener(new TreeModelAdapter() {
			@Override
			public void treeNodesChanged(TreeModelEvent e) {
				Object[] childs = e.getChildren();
				if (childs.length == 1) {
					if (childs[0] instanceof ProjectNode node)
						System.out.println(node.getUserObject());
				} else {
					System.out.println("Multiple Children?");
				}
			}
		});
		this.add(workTree, BorderLayout.CENTER);
	}

	private void buildPopup() {
		folderPop = new JPopupMenu();
		filePop = new JPopupMenu();

		folderPop.add(CompFactory.createMenuItem("Add File", e -> {
			processNewNode(NodeType.FILE, null);
		}));

		folderPop.add(CompFactory.createMenuItem("Add Folder", e -> {
			String fName = JOptionPane.showInputDialog(this.getParent(), 
					"What is the folders name?");
			processNewNode(NodeType.FOLDER, new EmptyArticle(fName));
		}));
		folderPop.add(CompFactory.createMenuItem("Rename", this::renameNode));
		folderPop.add(CompFactory.createMenuItem("Delete", this::deleteNode));

		filePop.add(CompFactory.createMenuItem("Rename", this::renameNode));
		filePop.add(CompFactory.createMenuItem("Delete", this::deleteNode));
		
	}

	public void processNewNode(NodeType type, AbstractArticle d) {
		DefaultMutableTreeNode node= null;
		if( workTree.getSelectionPath() != null)
			node = (DefaultMutableTreeNode) workTree.getSelectionPath().getLastPathComponent();
		if(node == null || type == NodeType.PROJECT)
			node = root;
		
		if(type != NodeType.FOLDER) {
			
		}
		ProjectNode<AbstractArticle> newNode = new ProjectNode<AbstractArticle>(type, d);
		SortedTreeModel model = (SortedTreeModel) workTree.getModel();
		model.insertNodeInto(newNode, node, node.getChildCount());

		// Optional: scroll to make the new node visible
		workTree.scrollPathToVisible(new TreePath(newNode.getPath()));
	}

	public void deleteNode() {
		DefaultTreeModel model = (DefaultTreeModel) workTree.getModel();

		TreePath path = workTree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			if (node.getParent() != null) {
				model.removeNodeFromParent(node);
			}
		}
	}

	public void renameNode() {
		TreePath path = workTree.getSelectionPath();
		if (path != null)
			workTree.startEditingAtPath(path);
	}
}