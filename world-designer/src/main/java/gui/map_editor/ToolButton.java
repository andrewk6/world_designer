package gui.map_editor;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import gui.map_editor.ToolSet.Tool;
import gui.map_editor.map_listener.ToolListener;

public class ToolButton extends JButton implements ToolListener
{
	private Tool tool;
	
	public ToolButton(ImageIcon icon, Tool tool) {
		super(icon);
		this.tool = tool;
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		
		
	}
	
	public ToolButton(ImageIcon icon, Tool tool, ActionListener act) {
		this(icon, tool);
		addActionListener(act);
	}

	@Override
	public void onToolChanged() {
		
	}
	public void onColorChanged() {}
}