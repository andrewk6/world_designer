package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import gui.utilities.CompFactory;
import gui.utilities.color_picker.ColorSelectPane;
import gui.utilities.color_picker.ColorSwatches;
import gui.utilities.color_picker.SwatchPane;

public class ColorDialog extends JDialog
{
	private ColorSwatches color;
	
	private ColorSelectPane colSelPane;
	private SwatchPane swatchPane;
	
	public ColorDialog(Window frm) 
	{
		super(frm, "Color Selector", ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		init(this.getContentPane());
		
		this.pack();
	    this.setLocationRelativeTo(frm);
	}
	
	private void init(Container cPane) {
		this.setLayout(new BorderLayout());
		
		JPanel btnPane = CompFactory.createButtonFlow(FlowLayout.RIGHT, new JButton[] {
				CompFactory.createButton("Finish", this::finish),
				CompFactory.createButton("Cancel", this::close)
		});
		this.add(btnPane, BorderLayout.SOUTH);
		
		JTabbedPane tabs = new JTabbedPane();
		this.add(tabs, BorderLayout.CENTER);
		
		colSelPane = new ColorSelectPane();
		tabs.addTab("Color Picker", colSelPane);
		
		swatchPane = new SwatchPane(30, 10);
		swatchPane.clearSwatches();
		swatchPane.setOnColorSelected(color -> {
			this.color = new ColorSwatches(color, null);
			finish();
		});
		tabs.addTab("Swatches", swatchPane);
	}
	
	private void finish() {
		if(color == null) {
			Color c = colSelPane.getSelectedColor();
			swatchPane.addSwatch(c);
			this.color = new ColorSwatches(c, swatchPane.getSwatches());
		}
		setVisible(false);
	}
	
	private void close() {
		color = null;
		setVisible(false);
	}
	
	public void addSwatches(List<Color> swatches) {
		swatchPane.addSwatch(swatches);
	}
	
	public ColorSwatches getColor() {
		return color;
	}
	
	public static ColorSwatches showDialog(Window frame) {
		ColorDialog cDialog = new ColorDialog(frame);
		cDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		cDialog.setVisible(true);
		return cDialog.getColor();
	}
	
	public static ColorSwatches showDialog(Window frame, List<Color> swatches) {
		ColorDialog cDialog = new ColorDialog(frame);
		cDialog.addSwatches(swatches);
		cDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		cDialog.setVisible(true);
		return cDialog.getColor();
	}
}