package gui.utilities;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class TreeModelAdapter implements TreeModelListener
{
	@Override
	public void treeNodesChanged(TreeModelEvent e) {}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {}
}