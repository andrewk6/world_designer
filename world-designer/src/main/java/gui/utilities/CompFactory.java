package gui.utilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Consumer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import data.DataManager;
import data.articles.abstracts.AbstractArticle;
import data.articles.predefined.WorldArticle;
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
	
	public static ReminderField createUpdateField(String remind, String text, Consumer<String> onChange) {
		ReminderField out = createReminderField(remind);
		out.setText(text);
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
	
	public static JMenu createMenu(String menu) {
		JMenu out = new JMenu(menu);
		out.setFont(FontStyle.BOLD1.getFont());
		return out;
	}
	
	public static JMenu createMenu(String menu, JMenuItem[] items) {
		JMenu out = createMenu(menu);
		for(JMenuItem it : items)
			out.add(it);
		return out;
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
	
	public static JPanel createDesignPane(DataManager d, AbstractArticle obj) {
		JPanel out = null;
		if(obj instanceof WorldArticle w)
			out = new WorldDesignPane(d, w);
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
	
	public static WindowListener createSafeExitListener(Runnable r, DataManager data) {
		return new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(r != null)
					r.run();
				e.getWindow().setVisible(false);
				data.exit();
			}
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		};
	}
	
	public static void buildTestWindow(JPanel pane) {
		DataManager data = new DataManager();
		SwingUtilities.invokeLater(()->{
			JFrame frm = new JFrame();
			frm.setContentPane(pane);
			frm.addWindowListener(createSafeExitListener(null, data));
			frm.pack();
			frm.setSize(800, 800);
			frm.setVisible(true);
		});
	}
	
	public static void buildTestWindow(JPanel pane, Runnable test) {
		DataManager data = new DataManager();
		SwingUtilities.invokeLater(()->{
			JFrame frm = new JFrame();
			Container cPane = frm.getContentPane();
			cPane.setLayout(new BorderLayout());
			cPane.add(pane, BorderLayout.CENTER);
			cPane.add(createButtonFlow(FlowLayout.RIGHT, new JButton[] {
					CompFactory.createButton("Test", test)
			}), BorderLayout.SOUTH);
			frm.addWindowListener(createSafeExitListener(null, data));
			frm.pack();
			frm.setSize(800, 800);
			frm.setVisible(true);
		});
	}
}