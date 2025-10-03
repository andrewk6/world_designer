package data.predefined;

import java.util.ArrayList;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import data.DataEnums.SettingStyle;
import data.abstracts.AbstractArticle;
import gui.utilities.documents.DocumentHelper;

public class WorldArticle extends AbstractArticle
{
	public StyledDocument worldDoc;
	public ArrayList<SettingStyle> styles;
	
	public WorldArticle(String name) {
		super(name);
		worldDoc = DocumentHelper.getNewDocument();
		styles = new ArrayList<SettingStyle>();
	}
	
	public String printWorld() {
		String out = name + ":\n";
		try {
			out += worldDoc.getText(0, worldDoc.getLength());
			out += "\n";
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		out += "Styles:" + styles.toString().replace("[", "").replace("]", "");
		return out;
	}
	
	@Override
	public String insertString() {
		return name;
	}
	
}