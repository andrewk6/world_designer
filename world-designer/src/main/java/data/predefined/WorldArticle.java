package data.predefined;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;

import data.DataEnums.SettingStyle;
import data.DataEnums;
import data.MapKey;
import data.abstracts.AbstractArticle;
import gui.design_panes.article_editor.SortedTreeModel;
import gui.utilities.documents.DocumentBuilder;
import gui.utilities.documents.DocumentHelper;

public class WorldArticle extends AbstractArticle
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4366991999873118989L;
	public transient StyledDocument worldDoc;
	public DefaultMutableTreeNode root;
	public ArrayList<SettingStyle> styles;
	public HashMap<MapKey, BasicArticle> articleMap;
	public Set<String> categories;
	
	public WorldArticle(String name) {
		super(name);
		root = SortedTreeModel.buildBaseTree(DataEnums.getDefaultFoldersArray());
		articleMap = new HashMap<MapKey, BasicArticle>();
		worldDoc = DocumentHelper.getNewDocument();
		styles = new ArrayList<SettingStyle>();
		categories = new TreeSet<String>();
	}
	
	public void addCategory(String cat) {
		categories.add(cat);
	}
	
	public void removeCategory(String cat) {
		categories.remove(cat);
	}
	
	@Override
	public String insertString() {
		return key.getName();
	}
	
	public String printWorld() {
		String out = key + ":\n";
		try {
			out += worldDoc.getText(0, worldDoc.getLength());
			out += "\n";
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		out += "Styles:" + styles.toString().replace("[", "").replace("]", "");
		return out;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		
		try {
			String docAsString = DocumentBuilder.writeDocument(worldDoc);
			out.writeObject(docAsString);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	
	 private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		 in.defaultReadObject();
		 
		 String docAsString = (String) in.readObject();
		 try {
			worldDoc = DocumentBuilder.parseString(docAsString);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	 }
}