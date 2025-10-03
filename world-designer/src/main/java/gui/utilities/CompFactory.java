package gui.utilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import data.abstracts.AbstractArticle;
import data.predefined.WorldArticle;
import gui.components.ReminderField;
import gui.design_panes.WorldDesignPane;
import gui.utilities.StyleManager.FontStyle;

public class CompFactory {
	private final static DefaultListCellRenderer seperateItems = new DefaultListCellRenderer() {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 2197727826324863692L;
	    public Component getListCellRendererComponent(JList<?> list,
	                                                  Object value,
	                                                  int index,
	                                                  boolean isSelected,
	                                                  boolean cellHasFocus) {
	        String display = value.toString();
	        if (index < list.getModel().getSize() - 1) {
	            display += " | "; 
	        }
	        JLabel label = (JLabel) super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
	        
	        // Use the font from the list itself or apply a custom one
	        label.setFont(list.getFont());
	        return label;
	    }
	};
	
	public static final int SCROLL_SPEED = 16;
	
	public static JLabel createLabel(String txt) {
		JLabel out = new JLabel(txt);
		out.setFont(StyleManager.BODY_PLAIN);
		return out;
	}
	
	public static JLabel createLabel(String txt, FontStyle style) {
		JLabel out = new JLabel(txt);
		out.setFont(style.getFont());
		return out;
	}
	
	public static ReminderField createReminderField(String remind) {
		ReminderField out = new ReminderField(remind);
		out.setFont(StyleManager.BODY_PLAIN);
		return out;
	}
	
	public static ReminderField createReminderField(String remind, FontStyle style) {
		ReminderField out = new ReminderField(remind);
		out.setFont(style.getFont());
		return out;
	}
	
	public static ReminderField createUpdateField(String remind, Consumer<String> onChange) {
		ReminderField out = createReminderField(remind);
		out.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) { update(); }
			@Override
			public void removeUpdate(DocumentEvent e) { update(); }
			@Override
			public void changedUpdate(DocumentEvent e) { update(); }
			
			private void update() {
				onChange.accept(out.getText());
			}
		});
		return out;
	}
	
	public static ReminderField createUpdateField(String remind, FontStyle style,
			Consumer<String> onChange) {
		ReminderField out = createReminderField(remind, style);
		out.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) { update(); }
			@Override
			public void removeUpdate(DocumentEvent e) { update(); }
			@Override
			public void changedUpdate(DocumentEvent e) { update(); }
			
			private void update() {
				onChange.accept(out.getText());
			}
		});
		return out;
	}

	public static JButton createButton(String txt) {
		JButton out = new JButton(txt);
		return out;
	}
	
	public static JButton createButton(String txt, ActionListener act) {
		JButton out = new JButton(txt);
		out.addActionListener(act);
		return out;
	}
	
	public static JButton createButton(String txt, Runnable act) {
		JButton out = new JButton(txt);
		out.addActionListener(e-> act.run());
		return out;
	}
	
	public static<T extends Enum<T>> JComboBox<T> getEnumCombo(Class<T> enumType){
		JComboBox<T> combo = new JComboBox<T>(enumType.getEnumConstants());
		return combo;
	}
	
	public static JMenuItem createMenuItem(String txt, ActionListener act) {
		JMenuItem out = new JMenuItem(txt);
		out.addActionListener(act);
		return out;
	}
	
	public static JMenuItem createMenuItem(String txt, Runnable act) {
		return createMenuItem(txt, e->act.run());
	}
	
	public static <T> JList<T> createList(ListModel<T> model){
		JList<T> list = new JList<T>(model);
		list.setCellRenderer(seperateItems);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		return list;
	}
	
	public static JPanel createDesignPane(AbstractArticle obj) {
		JPanel out = null;
		if(obj instanceof WorldArticle w)
			out = new WorldDesignPane(w);
		return out;
	}
	
	public static JPanel createButtonFlow(int layout, JButton[] btns) {
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(layout));
		for(JButton btn : btns) {
			pane.add(btn);
		}
		return pane;
	}
	
	public static JPanel createSplitPane(String lbl, Component comp) {
		JPanel out = new JPanel();
		out.setLayout(new BorderLayout());
		out.add(createLabel(lbl, FontStyle.BOLD1), BorderLayout.WEST);
		out.add(comp, BorderLayout.CENTER);
		return out;
	}
	
	public static JPanel createSplitPane(Component compWest, Component compCenter) {
		JPanel out = new JPanel();
		out.setLayout(new BorderLayout());
		out.add(compWest, BorderLayout.WEST);
		out.add(compCenter, BorderLayout.CENTER);
		return out;
	}
}