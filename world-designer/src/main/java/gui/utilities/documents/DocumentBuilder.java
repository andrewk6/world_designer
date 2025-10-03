package gui.utilities.documents;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class DocumentBuilder
{
	public static String writeDocument(StyledDocument doc) throws BadLocationException {
		String out = "";
		AttributeSet attr = doc.getCharacterElement(0).getAttributes();
		String block = "";
		for(int i = 0; i < doc.getLength(); i ++) {
			Element elem = doc.getCharacterElement(i);
	        AttributeSet temp = elem.getAttributes();
	        
	        if(!temp.equals(attr)) {	        	
	        	out+= buildTagString(attr) + block + "/>";
	        	attr = temp;
	        	block = "" + doc.getText(i, 1).charAt(0);
	        }else {
	        	block += "" + doc.getText(i, 1).charAt(0);
	        }
		}
		out+= buildTagString(attr) + block + "/>";
		return out;
	}
	
	public static StyledDocument parseString(String in) throws BadLocationException {
		StyledDocument doc = DocumentHelper.getNewDocument();
		
		String[] blocks = in.split("</");
		for(String block : blocks) {
			if(block.trim().length() > 0) {
				int tagStart = block.indexOf("'");
				int tagEnd = block.indexOf("'", tagStart + 1);
				String insert = block.substring(tagEnd+1, block.length()-2);
				DocumentHelper.insertWithAttributes(doc, insert, 
						buildAttributes(block.substring(tagStart+1, tagEnd)));
			}else
				DocumentHelper.insertWithAttributes(doc, block, null);
		}
		
		
		return doc;
	}
	
	private static AttributeSet buildAttributes(String tag) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		if(tag.contains("B"))
			StyleConstants.setBold(attr, true);
		if(tag.contains("I"))
			StyleConstants.setItalic(attr, true);
		if(tag.contains("U"))
			StyleConstants.setUnderline(attr, true);
		Matcher m = Pattern.compile("\\d+").matcher(tag);
		if(m.find()) {
			int fSize = Integer.parseInt(m.group());
			StyleConstants.setFontSize(attr, fSize);
		}
		
		return attr;
	}
	
	private static String buildTagString(AttributeSet attr) {
		String tag = "</'";
    	if(StyleConstants.isBold(attr))
    		tag += "B";
    	if(StyleConstants.isItalic(attr))
    		tag += "I";
    	if(StyleConstants.isUnderline(attr))
    		tag += "U";
    	tag += StyleConstants.getFontSize(attr) + "'";
    	
    	return tag;
	}
}