package gui.design_panes.article_editor;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import data.MapKey;

public class SortedTreeModel extends DefaultTreeModel {

    private static final Collator collator = Collator.getInstance(Locale.getDefault());
    static {
        collator.setStrength(Collator.PRIMARY); // ignore case, accents
    }

    public SortedTreeModel(DefaultMutableTreeNode root) {
        super(root);
    }

    /**
     * Natural string comparison: treats numbers as numbers (so "2" < "10").
     */
    private static int naturalCompare(String s1, String s2) {
        // Simple regex-based split into digit and non-digit chunks
        Pattern chunkPattern = Pattern.compile("(\\d+|\\D+)");
        Matcher m1 = chunkPattern.matcher(s1);
        Matcher m2 = chunkPattern.matcher(s2);

        while (m1.find() && m2.find()) {
            String c1 = m1.group(1);
            String c2 = m2.group(1);

            // If both chunks are numbers, compare numerically
            if (c1.chars().allMatch(Character::isDigit) && c2.chars().allMatch(Character::isDigit)) {
                long n1 = Long.parseLong(c1);
                long n2 = Long.parseLong(c2);
                if (n1 != n2)
                    return Long.compare(n1, n2);
            } else {
                // Otherwise, compare alphabetically using collator
                int result = collator.compare(c1, c2);
                if (result != 0)
                    return result;
            }
        }

        // If all chunks equal so far, shorter string wins
        return Integer.compare(s1.length(), s2.length());
    }

    /**
     * Comparator for nodes: folders first, then alphabetical (natural order).
     */
    private static final Comparator<DefaultMutableTreeNode> NODE_COMPARATOR = (a, b) -> {
        // Folders (branches) before files (leafs)
        if (!a.isLeaf() && b.isLeaf()) return -1;
        if (a.isLeaf() && !b.isLeaf()) return 1;

        // Both same type  sort by name (MapKey or String)
        String name1, name2;
        Object o1 = a.getUserObject();
        Object o2 = b.getUserObject();

        if (o1 instanceof MapKey m1 && o2 instanceof MapKey m2) {
            name1 = m1.getName();
            name2 = m2.getName();
        } else {
            name1 = String.valueOf(o1);
            name2 = String.valueOf(o2);
        }

        return naturalCompare(name1, name2);
    };

    @Override
    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
        if (!(newChild instanceof DefaultMutableTreeNode)) {
            super.insertNodeInto(newChild, parent, index);
            return;
        }

        int childCount = parent.getChildCount();
        int newIndex = 0;

        for (; newIndex < childCount; newIndex++) {
            MutableTreeNode siblingNode = (MutableTreeNode) parent.getChildAt(newIndex);
            if (NODE_COMPARATOR.compare((DefaultMutableTreeNode) newChild,
                                        (DefaultMutableTreeNode) siblingNode) < 0) {
                break;
            }
        }

        super.insertNodeInto(newChild, parent, newIndex);
    }
    
    public static DefaultMutableTreeNode buildBaseTree(String[] folders) {
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    	SortedTreeModel sorter = new SortedTreeModel(root);
    	for(String folder : folders) {
    		sorter.insertNodeInto(new DefaultMutableTreeNode(folder, true), root, root.getChildCount());
    	}
    	return root;
    }
}
