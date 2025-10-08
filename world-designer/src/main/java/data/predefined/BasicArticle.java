package data.predefined;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import data.abstracts.AbstractArticle;
import gui.utilities.documents.DocumentBuilder;
import gui.utilities.documents.DocumentHelper;

public class BasicArticle extends AbstractArticle
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1666887758314614993L;
	public String insert;
	public transient StyledDocument doc;
	
	public BasicArticle(String name) {
		super(name);
		insert = "";
		doc = DocumentHelper.getNewDocument();
	}

	@Override
	public String insertString() {
		return (insert.length() > 0) ? insert : key.getName();
	}
	
	public void setInsert(String in) {
		insert = in;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		
		try {
			String docAsString = DocumentBuilder.writeDocument(doc);
			out.writeObject(docAsString);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	
	 private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		 in.defaultReadObject();
		 
		 String docAsString = (String) in.readObject();
		 try {
			doc = DocumentBuilder.parseString(docAsString);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	 }
}