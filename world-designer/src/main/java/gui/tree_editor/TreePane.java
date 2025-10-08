package gui.tree_editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.NoSuchElementException;

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

import data.DataManager;
import data.abstracts.AbstractArticle;
import data.predefined.BasicArticle;
import data.predefined.FolderArticle;
import data.predefined.WorldArticle;
import gui.tree_editor.ProjectNode.NodeType;
import gui.utilities.CompFactory;
import gui.utilities.TreeModelAdapter;

public class TreePane extends JPanel
{
	private final DataManager data;
	
	private JPanel editPane;
	private JPopupMenu worldPop, catPop, artPop;
	
	private JTree workTree;
	private DefaultMutableTreeNode root;
	
	public TreePane(DataManager data, JPanel editPane) {
		super();
		this.data = data;
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
				ProjectNode node = null;
				int row = workTree.getRowForLocation(e.getX(), e.getY());
				TreePath path = workTree.getPathForLocation(e.getX(), e.getY());

				if (row != -1 && path != null) {
					// Select the clicked node
					workTree.setSelectionPath(path);
				}
				
				if(workTree.getSelectionPath() != null) {
					if (workTree.getSelectionPath().getLastPathComponent() 
							instanceof ProjectNode foundNode) {
						node = foundNode;
					}
				}
				
				ProjectNode<?> projNode = getProjectAncestor(node);
				if(projNode != null)
					if(projNode.getUserObject() instanceof WorldArticle w) {
						if(data.getActiveWorld() == null) {
							data.setActiveWorld(w);
						} else if(!data.getActiveWorld().equals(w)) {
							data.setActiveWorld(w);
							editPane.removeAll();
							editPane.revalidate();
							editPane.repaint();
						}
						
					}
				
				if(SwingUtilities.isLeftMouseButton(e)) {
					if(node != null) {
						JPanel newPane = CompFactory.createDesignPane(data, node.obj);
						if(newPane != null) {
							editPane.removeAll();
							editPane.add(newPane, BorderLayout.CENTER);
							editPane.revalidate();
							editPane.repaint();
						}
	
						}
					}
				if (SwingUtilities.isRightMouseButton(e)) {

					if (node != null) {
						if (node.type == NodeType.PROJECT)
							worldPop.show(workTree, e.getX(), e.getY());
						else if(node.type == NodeType.FOLDER)
							catPop.show(workTree, e.getX(), e.getY());
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
						if(node.getUserObject() instanceof BasicArticle a) {
//							data.getWorldMap().remove(a)
						}
				} else {
					System.out.println("Multiple Children?");
				}
			}
		});
		this.add(workTree, BorderLayout.CENTER);
	}

	private void buildPopup() {
		worldPop = new JPopupMenu();
		catPop = new JPopupMenu();
		artPop = new JPopupMenu();

		worldPop.add(CompFactory.createMenuItem("Add Category", e -> {
			String aName = JOptionPane.showInputDialog(this.getParent(), 
					"What is the Categories name?");
			data.getActiveWorld().addCategory(aName);
			processNewCategory(new FolderArticle(aName));
		}));
		catPop.add(CompFactory.createMenuItem("Add article", e->{
			String aName = JOptionPane.showInputDialog(this.getParent(), 
					"What is the Article name?");
			BasicArticle art = new BasicArticle(aName);
			processNewArticle(art);
			data.addArticleToWorld(art);
		}));
		

//		folderPop.add(CompFactory.createMenuItem("Add Folder", e -> {
//			String fName = JOptionPane.showInputDialog(this.getParent(), 
//					"What is the folders name?");
//			processNewNode(NodeType.FOLDER, new EmptyArticle(fName));
//		}));
//		folderPop.add(CompFactory.createMenuItem("Rename", this::renameNode));
//		folderPop.add(CompFactory.createMenuItem("Delete", this::deleteNode));
//
//		filePop.add(CompFactory.createMenuItem("Rename", this::renameNode));
//		filePop.add(CompFactory.createMenuItem("Delete", this::deleteNode));	
	}
	
	public void processNewWorld(WorldArticle w) {
		ProjectNode<WorldArticle> newNode = 
				new ProjectNode<WorldArticle>(NodeType.PROJECT, w);
		insertNode(newNode, root);
	}
	
	public void processNewCategory(FolderArticle e) {
		ProjectNode<?> worldNode = getWorldNode(data.getActiveWorld());
		ProjectNode<FolderArticle> newNode = 
				new ProjectNode<FolderArticle>(NodeType.FOLDER, e);
		insertNode(newNode, worldNode);
	}
	
	public void processNewArticle(BasicArticle b) {
		ProjectNode<?> catNode = getCategoryNode(b.category, 
				getWorldNode(data.getActiveWorld()));
		ProjectNode<BasicArticle> newNode = 
				new ProjectNode<BasicArticle>(NodeType.FILE, b);
		insertNode(newNode, catNode);
	}
	
	private void insertNode(DefaultMutableTreeNode newNode, DefaultMutableTreeNode node) {
		SortedTreeModel model = (SortedTreeModel) workTree.getModel();
		model.insertNodeInto(newNode, node, node.getChildCount());
		workTree.scrollPathToVisible(new TreePath(newNode.getPath()));
	}
	/*
	public void processNewNode(NodeType type, AbstractArticle d) {
		DefaultMutableTreeNode node= null;
		
		if(workTree.getSelectionPath() != null)
			node = (DefaultMutableTreeNode) workTree.getSelectionPath().getLastPathComponent();
		if(node == null || type == NodeType.PROJECT)
			node = root;
		
		ProjectNode<?> newNode = null;
		if(d instanceof EmptyArticle e) {
			newNode = new ProjectNode<EmptyArticle>(type, e);
		}else if(d instanceof BasicArticle a) {
			if(a.category != "None") {
				node = getCategoryNode(a.category, getWorldNode(data.getActiveWorld()));
			}else {
				node = getWorldNode(data.getActiveWorld());
			}
			newNode = new ProjectNode<BasicArticle>(type, a);
		}else if(d instanceof WorldArticle w)
			newNode = new ProjectNode<WorldArticle>(type, w);
		
		if(newNode != null) {
			insertNode(newNode, node);
		}
	}

	public void processNewNodeOLD(NodeType type, AbstractArticle d) {
		DefaultMutableTreeNode node= null;
		if( workTree.getSelectionPath() != null)
			node = (DefaultMutableTreeNode) workTree.getSelectionPath().getLastPathComponent();
		if(node == null || type == NodeType.PROJECT)
			node = root;
		
		if(type != NodeType.FOLDER) {
			
		}
		ProjectNode<?> newNode;
		if(d instanceof EmptyArticle e)
			newNode = new ProjectNode<EmptyArticle>(type, e);
		else if(d instanceof BasicArticle a) {
			newNode = new ProjectNode<BasicArticle>(type, a);
			data.addArticleToWorld(a);
		}
		else
			newNode = new ProjectNode<AbstractArticle>(type, d);
		
		SortedTreeModel model = (SortedTreeModel) workTree.getModel();
		model.insertNodeInto(newNode, node, node.getChildCount());
		workTree.scrollPathToVisible(new TreePath(newNode.getPath()));
	}
	*/
	public void deleteNode() {
		DefaultTreeModel model = (DefaultTreeModel) workTree.getModel();

		TreePath path = workTree.getSelectionPath();
		if (path != null) {
			ProjectNode<?> node = (ProjectNode<?>) path.getLastPathComponent();
			if (node.getParent() != null) {
				model.removeNodeFromParent(node);
				if(node.getUserObject() instanceof BasicArticle a)
					data.getArticleMap().remove(a.name);
			}
		}
	}

	public void renameNode() {
		TreePath path = workTree.getSelectionPath();
		if (path != null)
			workTree.startEditingAtPath(path);
	}
	
	public static ProjectNode<?> getProjectAncestor(ProjectNode<?> node) {
	    if (node == null) return null;

	    ProjectNode<?> current = node;
	    while (current != null && current instanceof ProjectNode) {
	        ProjectNode<?> projNode = (ProjectNode<?>) current;
	        if (projNode.type == NodeType.PROJECT) {
	            return projNode; // found the highest PROJECT
	        }
	        current = (ProjectNode<?>) projNode.getParent();
	    }
	    return null; // none found
	}
	
	public ProjectNode<?> getWorldNode(WorldArticle a){
		for(int i = 0; i < root.getChildCount(); i ++) {
			if(root.getChildAt(i) instanceof ProjectNode<?> p) {
				if(p.obj instanceof WorldArticle w) {
					if(w.equals(a))
						return p;
				}
			}
		}
		throw new NoSuchElementException("Failed to find World");
	}
	
	public ProjectNode<?> getCategoryNode(String cat, ProjectNode<?> worldNode){
		for(int i = 0; i < worldNode.getChildCount(); i ++) {
			if(worldNode.getChildAt(i) instanceof ProjectNode<?> p)
				if(p.type == NodeType.FOLDER)
					if(p.obj instanceof FolderArticle a)
						if(a.name.equals(cat))
							return p;
		}
		throw new NoSuchElementException("Failed to find Category");
	}
}