package gui.editor;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import data.abstracts.AbstractArticle;
import gui.editor.ProjectNode.NodeType;

public class SortedTreeModel extends DefaultTreeModel {

	private static final Collator collator = Collator.getInstance(Locale.getDefault());
	static {
	    collator.setStrength(Collator.PRIMARY); // ignore case, accents
	}
	
    public SortedTreeModel(DefaultMutableTreeNode root) {
        super(root);
    }

    private static final Comparator<ProjectNode<?>> NODE_COMPARATOR = (a, b) -> {
    	System.out.println("Comparing: " + a.toString() + "/" + b.toString());
        // First: PROJECT always first layer after root
        if (a.type == NodeType.PROJECT && b.type != NodeType.PROJECT) return -1;
        if (b.type == NodeType.PROJECT && a.type != NodeType.PROJECT) return 1;
        
        if (a.type == NodeType.PROJECT && b.type == NodeType.PROJECT) {
        	System.out.println("Project Compare : " + a.toString()  +" / " + b.toString());
            return collator.compare(String.valueOf(a.getUserObject()), String.valueOf(b.getUserObject()));
        }

        // Next: folders before files
        if (a.type == NodeType.FOLDER && b.type == NodeType.FILE) return -1;
        if (a.type == NodeType.FILE && b.type == NodeType.FOLDER) return 1;

        // Finally: alphabetical by name
        String name1 = String.valueOf(a.getUserObject());
        String name2 = String.valueOf(b.getUserObject());
        return collator.compare(name1, name2);
    };
    
    @Override
    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
        if (!(newChild instanceof ProjectNode<?>)) {
            super.insertNodeInto(newChild, parent, index);
            return;
        }

        ProjectNode<?> pChild = (ProjectNode<?>) newChild;

        int childCount = parent.getChildCount();
        int newIndex = 0;

        for (; newIndex < childCount; newIndex++) {
            MutableTreeNode siblingNode = (MutableTreeNode) parent.getChildAt(newIndex);
            if (!(siblingNode instanceof ProjectNode<?>)) continue;

            ProjectNode<?> sibling = (ProjectNode<?>) siblingNode;
            if (NODE_COMPARATOR.compare(pChild, sibling) < 0) {
                break;
            }
        }

        super.insertNodeInto(newChild, parent, newIndex);
    }
    
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        ProjectNode<?> node = (ProjectNode<?>) path.getLastPathComponent();
        Object oldUserObj = node.getUserObject();

        if (oldUserObj instanceof AbstractArticle obj) {
            // Keep the same ProjectFile, but update its name
            obj.name = newValue.toString();
            nodeChanged(node); // tells JTree to refresh display
        } else {
            // fallback: just replace
            node.setUserObject(newValue);
            nodeChanged(node);
        }
        node.setUserObject(newValue);

        // Trigger resort: remove and reinsert in correct place
        ProjectNode<?> parent = (ProjectNode<?>) node.getParent();
        if (parent != null) {
            removeNodeFromParent(node);
            insertNodeInto(node, parent, parent.getChildCount()); // insertNodeInto does the sorting
        } else {
            // root node renamed (rare, and usually root is hidden)
            nodeChanged(node);
        }
    }
}