package gui.design_panes.article_editor;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import data.MapKey;
import data.listeners.MapKeyNameListener;

public class ArticleNode extends DefaultMutableTreeNode implements MapKeyNameListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8181358492987291910L;
	
	private transient JTree tree;

	public ArticleNode(MapKey key, JTree tree) {
		this.tree = tree;
		key.registerNameListener(this);
		this.setUserObject(key);
	}

	@Override
	public void onNameChange() {
		SortedTreeModel model = (SortedTreeModel) tree.getModel();
		model.nodeChanged(this);
	}
	
	private JTree findTree() {
        // walk up parent nodes to find JTree
        TreeNode parent = getParent();
        while (parent != null && !(parent instanceof JTree)) {
            parent = parent.getParent();
        }
        return (JTree) parent;
    }
}