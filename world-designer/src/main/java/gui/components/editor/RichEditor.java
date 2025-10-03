package gui.components.editor;

import gui.utilities.CompFactory;
import gui.utilities.SpellCheckerUtilities;
import gui.utilities.StyleManager;
import gui.utilities.StyleManager.FontStyle;
import gui.utilities.documents.DocumentBuilder;
import io.github.geniot.jortho.SpellChecker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.print.attribute.AttributeSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class RichEditor extends JPanel
{
	public static void main(String[]args) {
		SpellCheckerUtilities.initSpellCheck();
		StyleManager.setLookAndFeel();
		
		SwingUtilities.invokeLater(()->{
			JFrame frm = new JFrame();
			Container c = frm.getContentPane();
			c.setLayout(new BorderLayout());
			RichEditor editor = new RichEditor();
			c.add(editor, BorderLayout.CENTER);
			c.add(CompFactory.createButtonFlow(FlowLayout.RIGHT, new JButton[] {
					CompFactory.createButton("Test", e->{
						try {
							String doc = DocumentBuilder.writeDocument(editor.getDocument());
							System.out.println(doc);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}),
					CompFactory.createButton("Test Load", e->{
						try {
							StyledDocument doc = DocumentBuilder.parseString(
									"</'18'Test/></'B18' Words/></'BI18' in/>"
									+ "</'BIU18' different/></'BU18' styles/>"
									+ "</'B18' like/>"
									+ "</'BI18' so/></'I18' to/>"
									+ "</'18' see /></'B34'Big />"
									+ "</'B48'Bigger/>");
							editor.loadDocument(doc);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					})
			}), BorderLayout.SOUTH);
			frm.pack();
			frm.setSize(800, 800);
			frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frm.setVisible(true);
		});
	}
	
	private SimpleAttributeSet typeSet = null;
	
	private JTextPane edit;
	private ControlButton boldBtn, italBtn, underBtn;
	
	public RichEditor() {
		this.setLayout(new BorderLayout());
		typeSet = new SimpleAttributeSet();
		initStyle();
		buildEditor();
		buildControlPane();
	}
	
	private void buildEditor() {
		edit = new JTextPane();
		edit.addCaretListener(e -> {
		    edit.setCharacterAttributes(typeSet, true);
		});
		SpellCheckerUtilities.addPopup(edit);
		resetSpellcheck();
		JScrollPane editScroll = new JScrollPane(edit);
		this.add(editScroll, BorderLayout.CENTER);
	}
	
	private void buildControlPane(){
		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(controls, BorderLayout.NORTH);
		
		boldBtn = new ControlButton("B", e->{
			ControlButton btn = (ControlButton) e.getSource();
			btn.changeState();
			StyleConstants.setBold(typeSet, btn.isActive());
			edit.setCharacterAttributes(typeSet, false);
			resetSpellcheck();
		});
		controls.add(boldBtn);
		
		italBtn = new ControlButton("I", e->{
			ControlButton btn = (ControlButton) e.getSource();
			btn.changeState();
			StyleConstants.setItalic(typeSet, btn.isActive());
			edit.setCharacterAttributes(typeSet, false);
			resetSpellcheck();
		});
		italBtn.setFont(italBtn.getFont().deriveFont(Font.ITALIC));
		controls.add(italBtn);
		
		underBtn = new ControlButton("U", e->{
			ControlButton btn = (ControlButton) e.getSource();
			btn.changeState();
			StyleConstants.setUnderline(typeSet, btn.isActive());
			edit.setCharacterAttributes(typeSet, false);
			resetSpellcheck();
		});
		Font base = underBtn.getFont();
		Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) base.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		underBtn.setFont(base.deriveFont(attributes));
		controls.add(underBtn);
		
		JComboBox<FontStyle> readyStyles = CompFactory.getEnumCombo(FontStyle.class);
		readyStyles.addItemListener(e->{
			FontStyle fs = (FontStyle) readyStyles.getSelectedItem();
			initStyle(fs.getFont());
		});
		readyStyles.setSelectedItem(FontStyle.PLAIN1);
		readyStyles.setFocusable(false);
		controls.add(readyStyles);
	}
	
	private void initStyle() {
	    StyleConstants.setFontFamily(typeSet, StyleManager.BODY_PLAIN.getFamily());
	    StyleConstants.setFontSize(typeSet, StyleManager.BODY_PLAIN.getSize());
	    StyleConstants.setBold(typeSet, false);
	    StyleConstants.setItalic(typeSet, false);
	    StyleConstants.setUnderline(typeSet, false);
	    StyleConstants.setForeground(typeSet, Color.BLACK);
	}
	
	private void initStyle(Font f) {
	    StyleConstants.setFontFamily(typeSet, f.getFamily());
	    StyleConstants.setFontSize(typeSet, f.getSize());
	    StyleConstants.setBold(typeSet, f.isBold());
	    boldBtn.setActive(f.isBold());
	    StyleConstants.setItalic(typeSet, f.isItalic());
	    italBtn.setActive(f.isItalic());
	    StyleConstants.setUnderline(typeSet, underBtn.isActive());
	    StyleConstants.setForeground(typeSet, Color.BLACK);
	    
	    resetSpellcheck();
	}
	
	public StyledDocument getDocument() {
		return edit.getStyledDocument();
	}
	
	public void loadDocument(StyledDocument doc) {
		resetSpellcheck();
		edit.setStyledDocument(doc);
		edit.revalidate();
		edit.repaint();
		
		
	}
	
	private void resetSpellcheck() {
		SpellCheckerUtilities.setDocument(edit);
	}
}