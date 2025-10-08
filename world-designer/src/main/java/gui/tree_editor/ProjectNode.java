package gui.tree_editor;

import javax.swing.tree.DefaultMutableTreeNode;

import data.abstracts.AbstractArticle;

public class ProjectNode<T extends AbstractArticle> extends DefaultMutableTreeNode 
	implements Comparable<ProjectNode<T>>
{
	public enum NodeType { 
		PROJECT("Project"), 
		FOLDER("Folder"), 
		FILE(("File"));
		
		private String desc;
		
		NodeType(String desc){
			this.desc = desc;
		}
		
		public String toString() {
			return desc;
		}
	}
	
    public NodeType type;
    public T obj;
    
	public ProjectNode(NodeType type, T obj) {
		super(obj);
		this.type = type;
		this.obj = obj;
		
		boolean folder;
		switch(type) {
		case PROJECT: 
		case FOLDER: folder = true; break;
		case FILE:
		default: folder = false;
		}
		this.setAllowsChildren(folder);
	}
	
	@Override
	public Object getUserObject() {
		return obj;
	}

	@Override
	public int compareTo(ProjectNode<T> o) {
		return this.obj.compareTo(o.obj);
	}
	
	
}