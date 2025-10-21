package gui.map_editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import data.DataManager;
import data.map.LandMap;
import data.map.MapHex;
import gui.utilities.CompFactory;
import gui.utilities.ImageManager;
import gui.dialogs.ColorDialog;
import gui.map_editor.ToolSet.Tool;
import gui.map_editor.map_listener.ToolChangeAdapter;
import gui.utilities.StyleManager;
import gui.utilities.TestingFrame;
import gui.utilities.ImageManager.MapIcon;
import gui.utilities.color_picker.ColorSwatches;

public class MapEditorPane extends JPanel
{
	public static void main(String[]args) {
		LandMap map = new LandMap("Test", 200, 200);
		Iterator<MapHex> mapIter = map.iterator();
		while(mapIter.hasNext()) {
			MapHex hex = mapIter.next();
			hex.name = hex.row + "," + hex.col;
			hex.color = Color.WHITE;
		}
		try {
			TestingFrame frame = new TestingFrame(
					new MapEditorPane(TestingFrame.buildDatabaseTest(), map));
			frame.setVisible(true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private LandMap map;
	
	private HexPane hPane;
	private JColorChooser colSelect;
	private ToolSet toolSet;
	private JScrollPane mapScroll;
	
	private DataManager data;
	
	public MapEditorPane(DataManager data, LandMap map) {
		this.map = map;
		toolSet = new ToolSet(data);
		colSelect = new JColorChooser();
		
		buildContent();
	}
	
	private void buildContent() {
		this.setLayout(new BorderLayout());
		
		JPanel controlPane = new JPanel();
		controlPane.setLayout(new BorderLayout());
		this.add(controlPane, BorderLayout.NORTH);
		buildMenu(controlPane);
		
		hPane = new HexPane(map, toolSet);
		mapScroll = CompFactory.createScroll(hPane);
		this.add(mapScroll, BorderLayout.CENTER);
	}
	
	public void loadMap(LandMap map) {
		this.map = map;
		hPane.loadGrid(map);
//		mapScroll.upd
		
	}
	
	private void buildMenu(JPanel controlPane) {
		JPanel btnFlow = CompFactory.createButtonFlow(FlowLayout.LEFT, new JButton[] {
				buildToolButton(Tool.SELECT),
				buildToolButton(Tool.BRUSH),
				buildToolButton(Tool.ERASE),
				buildToolButton(Tool.FILL)
		});
		toolSet.setTool(Tool.BRUSH);
		controlPane.add(btnFlow, BorderLayout.CENTER);
		
		JPanel lblPane = CompFactory.createButtonFlow(FlowLayout.LEFT, null);
		controlPane.add(lblPane, BorderLayout.EAST);
		
		JPopupMenu colorMenu = new JPopupMenu();
		btnFlow.add(buildButton("Color", colorMenu));
		colorMenu.add(CompFactory.createMenuItem("Green", e->{
			toolSet.setColor(Color.GREEN);
		}));
		colorMenu.add(CompFactory.createMenuItem("Blue", e->{
			toolSet.setColor(Color.BLUE);
		}));
		colorMenu.add(CompFactory.createMenuItem("Yellow", e->{
			toolSet.setColor(Color.YELLOW);
		}));
		colorMenu.add(CompFactory.createMenuItem("Brown", e ->{
			toolSet.setColor(new Color(92, 64, 51));
		}));
		colorMenu.add(CompFactory.createMenuItem("Color Selector", e->{			
			ColorSwatches selectedColor = ColorDialog.showDialog(
					SwingUtilities.getWindowAncestor(this), map.swatches);
			if(selectedColor != null) {
				toolSet.setColor(selectedColor.color);
				if(selectedColor.swatches != null)
					map.swatches = selectedColor.swatches;
			}
			
		}));
		
		JPopupMenu iconMenu = new JPopupMenu();
		btnFlow.add(buildButton("Icons", iconMenu));
		iconMenu.add(CompFactory.createMenuItem("City", e->{
			toolSet.setIcon(MapIcon.CITY);
			toolSet.setTool(Tool.ICON);
		}));
		iconMenu.add(CompFactory.createMenuItem("Remove Icon", e->{

			toolSet.setIcon(MapIcon.EMPTY);
			toolSet.setTool(Tool.ICON);
		}));

		JLabel toolLabel = CompFactory.createLabel(toolSet.getTool().toString());
		toolLabel.setForeground(toolSet.getColor());
		Color background = getContrastingColor(toolSet.getColor());
		if(background != null) {
			toolLabel.setBackground(background);
			toolLabel.setOpaque(true);
		}
		lblPane.add(toolLabel);
		ToolChangeAdapter toolLblUpdate = new ToolChangeAdapter() {
			@Override
			public void onToolChanged() {
				if(toolSet.getTool() == Tool.ICON)
					toolLabel.setText("Icon: " + toolSet.getIcon().getName());
				else
					toolLabel.setText(toolSet.getTool().toString());
			}
			
			@Override
			public void onColorChanged() {
				Color background = getContrastingColor(toolSet.getColor());
				toolLabel.setForeground(toolSet.getColor());
				if(background != null) {
					toolLabel.setBackground(background);
					toolLabel.setOpaque(true);
					toolLabel.repaint();
				}else {
					toolLabel.setBackground(UIManager.getColor("Label.background"));
					toolLabel.setOpaque(false);
					toolLabel.repaint();
				}
			}
		};
		toolSet.registerToolListener(toolLblUpdate);
	}
	
	private JButton buildToolButton(Tool tool) {
		ImageIcon img = null;
		
		if(tool.getIcon() != null)
			img = new ImageIcon(tool.getIcon().getScaledInstance(
				32, 32, Image.SCALE_SMOOTH));
		
		if(img == null)
			return CompFactory.createButton(tool.toString(), e->{
				toolSet.setTool(tool);
			});
		else {
			JButton btn = CompFactory.createButton(img, e->{
				toolSet.setTool(tool);
			});
			btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
			ToolChangeAdapter manager = new ToolChangeAdapter() {
				@Override
				public void onToolChanged() {
					if(toolSet.getTool() == tool)
						btn.setBorderPainted(true);
					else
						btn.setBorderPainted(false);
				}
			};
			toolSet.registerToolListener(manager);
			return btn;
		}
	}
	
	public static Color getContrastingColor(Color background) {
	    int r = background.getRed();
	    int g = background.getGreen();
	    int b = background.getBlue();

	    double brightness = 0.299 * r + 0.587 * g + 0.114 * b;
	    return brightness < 128 ? null : Color.BLACK;
	}
	
	private JButton buildButton(String btnText, JPopupMenu menu) {
		JButton btn = CompFactory.createButton(btnText);
		btn.addActionListener(e-> menu.show(btn, 0, btn.getHeight()));
		return btn;
	}
}