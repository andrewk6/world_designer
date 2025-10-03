package data.predefined;

import javax.swing.text.StyledDocument;

import data.abstracts.AbstractArticle;
import gui.utilities.documents.DocumentHelper;

public class BasicArticle extends AbstractArticle
{
	public String insert;
	public StyledDocument doc;
	
	public BasicArticle(String name) {
		super(name);
		insert = "";
		doc = DocumentHelper.getNewDocument();
	}

	@Override
	public String insertString() {
		return (insert.length() > 0) ? insert : name;
	}
	
}