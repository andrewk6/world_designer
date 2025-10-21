package gui.components.editor;

import gui.utilities.CompFactory;
import gui.utilities.SpellCheckerUtilities;
import gui.utilities.StyleManager;
import gui.utilities.StyleManager.FontStyle;
import gui.utilities.documents.DocumentBuilder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import data.DataManager;
import data.MapKey;
import data.articles.predefined.BasicArticle;
import data.articles.predefined.WorldArticle;

public class RichEditor extends JPanel
{
	public static void main(String[]args) {
		DataManager data = new DataManager();
		data.createNewWorld("World");
		data.addArticleToWorld(new BasicArticle("Luna"));
		data.addArticleToWorld(new BasicArticle("Luni"));
		data.addArticleToWorld(new BasicArticle("Cheese"));
		SpellCheckerUtilities.initSpellCheck();
		StyleManager.setLookAndFeel();
		
		SwingUtilities.invokeLater(()->{
			JFrame frm = new JFrame();
			Container c = frm.getContentPane();
			c.setLayout(new BorderLayout());
			RichEditor editor = new RichEditor(data);
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
	public static final String LINK_ATTR = "(LINK)";
	private final DataManager data;
	
	private SimpleAttributeSet typeSet;
	private Style linkStyle;
	private ControlButton boldBtn, italBtn, underBtn;
	private JComboBox<FontStyle> readyStyles;
	private JPopupMenu suggestionPopup;
	
	private JTextPane edit;
	
	public RichEditor(DataManager data) {
		this.data = data;
		this.setLayout(new BorderLayout());
		typeSet = new SimpleAttributeSet();
		suggestionPopup = new JPopupMenu();
		
		initStyle();
		buildEditor();
		buildControlPane();
		buildLinkStyle();
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
		
		edit.registerKeyboardAction(e -> copyFunction(), 
        		KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), 
        		JComponent.WHEN_FOCUSED);
		edit.registerKeyboardAction(e-> {
			try {
				showAutoComplete();
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		},
				KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK), 
				JComponent.WHEN_FOCUSED);
		edit.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseAction(e);
			}
		});
		
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseAction(e);
			}
		});
	}
	
	private void mouseAction(MouseEvent e) {
		int pos = edit.viewToModel2D(e.getPoint());
		if(pos >= 0) {
			Element elem = edit.getStyledDocument().getCharacterElement(pos);
			AttributeSet attr = elem.getAttributes();
			
			MapKey link = (MapKey) attr.getAttribute(LINK_ATTR);
			if(link != null) {
				//TODO: Handle the link/click
			}
		}
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
		
		readyStyles = CompFactory.createEnumCombo(FontStyle.class);
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
	
	private void resetStyle() {
		FontStyle fs = (FontStyle) readyStyles.getSelectedItem();
		StyleConstants.setFontFamily(typeSet, fs.getFont().getFamily());
	    StyleConstants.setFontSize(typeSet, fs.getFont().getSize());
	    StyleConstants.setBold(typeSet, boldBtn.isActive());
	    StyleConstants.setItalic(typeSet, italBtn.isActive());
	    StyleConstants.setUnderline(typeSet, underBtn.isActive());
	    StyleConstants.setForeground(typeSet, Color.BLACK);
	    
	    edit.setCharacterAttributes(typeSet, true);
	    resetSpellcheck();
	}
	
	private void showAutoComplete() throws BadLocationException {
		int caretPos = edit.getCaretPosition();
		int atPos;
		String text = edit.getStyledDocument().getText(0, caretPos);
		if(text.contains("@")) {
			atPos = text.indexOf("@");
			text = text.substring(atPos, caretPos);
			text = text.replace("@", "");
			
			DefaultListModel<MapKey> listModel = new DefaultListModel<>();
			for(MapKey s : data.getArticleMap().keySet()) {
				if(s.getName().toLowerCase().contains(text.toLowerCase()))
					listModel.addElement(s);
			}
			if(data.getWorldKey().getName().contains(text))
				listModel.addElement(data.getWorldKey());
			
			suggestionPopup.removeAll();
			JList<MapKey> list = new JList<>(listModel);
			list.addKeyListener(new KeyAdapter() {
	            @Override
	            public void keyPressed(KeyEvent e) {
	                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                    e.consume();
	                    insertSuggestion(list.getSelectedValue(), caretPos, atPos);
	                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                    suggestionPopup.setVisible(false);
	                    edit.requestFocusInWindow();
	                }
	            }
	        });

			list.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                if (e.getClickCount() == 2) {
	                    insertSuggestion(list.getSelectedValue(), caretPos, atPos);
	                }
	            }
	        });
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			// Wrap in scroll pane
			JScrollPane scrollPane = new JScrollPane(list);
			scrollPane.setPreferredSize(new Dimension(200, 150)); // max size

			suggestionPopup.add(scrollPane);

			// Show popup at caret, mouse, etc.
			Rectangle2D caretCoords = edit.modelToView2D(caretPos);
			suggestionPopup.show(edit, (int)caretCoords.getX(), (int)caretCoords.getY() + 20);
			list.requestFocus();
		}
	}
	
	private void insertSuggestion(MapKey insert, int caretPos, int atPos) {
		suggestionPopup.setVisible(false);
		String insertText = insert.getName();
		if(data.getArticleMap().get(insert) != null)
			if(data.getArticleMap().get(insert).insertString().length() > 0)
				insertText = data.getArticleMap().get(insert).insertString();
		SimpleAttributeSet linkSet = new SimpleAttributeSet(linkStyle);
		linkSet.addAttribute(LINK_ATTR, insert);
		
		try {
			edit.getDocument().remove(atPos, caretPos - atPos);
			edit.getDocument().insertString(atPos, insertText, linkSet);
			resetStyle();
		} catch (BadLocationException e) {
			System.out.println("Doc Length: " + edit.getDocument().getLength());
			System.out.println("@: " + atPos + " / Caret: " + caretPos);
			e.printStackTrace();
		}
		
		
	}
	
	private void copyFunction() {
		String selectedText = edit.getSelectedText();
    	if (selectedText != null && !selectedText.isEmpty()) {
    	    StringSelection selection = new StringSelection(selectedText);
    	    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    	}
	}
	
	public StyledDocument getDocument() {
		return edit.getStyledDocument();
	}
	
	public void loadDocument(StyledDocument doc) {
		resetSpellcheck();
		edit.setStyledDocument(doc);
		edit.revalidate();
		edit.repaint();	
		buildLinkStyle();
	}
	
	private void buildLinkStyle() {
		linkStyle = edit.addStyle("link", null);
		StyleConstants.setItalic(linkStyle, true);
		StyleConstants.setUnderline(linkStyle, true);
		StyleConstants.setForeground(linkStyle, Color.BLUE);
	}
	
	private void resetSpellcheck() {
		SpellCheckerUtilities.setDocument(edit);
	}
}