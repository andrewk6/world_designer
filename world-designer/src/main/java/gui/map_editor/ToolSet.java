package gui.map_editor;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import data.DataManager;
import gui.map_editor.map_listener.ToolListener;
import gui.utilities.ImageManager;
import gui.utilities.ImageManager.MapIcon;

public class ToolSet
{
	public enum Tool{
		SELECT("Select", ImageManager.SELECT_TOOL),
		BRUSH("Brush", ImageManager.PAINT_TOOL),
		ERASE("Eraser", ImageManager.ERASER_TOOL),
		FILL("Fill", ImageManager.FILL_TOOL),
		ICON("Icon", null);
		
		private String desc;
		private Image icon;
		
		Tool(String desc, Image icon){
			this.desc = desc;
			this.icon = icon;
		}
		
		public String toString() {
			return desc;
		}
		
		public Image getIcon() {
			return icon;
		}
	}
	
	private Tool tool;
	private Color color;
	private MapIcon icon;
	private final DataManager data;
	
	private ArrayList<ToolListener> toolTrackers;
	
	public ToolSet(DataManager data) {
		this.data = data;
		toolTrackers = new ArrayList<ToolListener>();
		tool = Tool.BRUSH;
		color = Color.GREEN;
		icon = null;
	}
	
	public Tool getTool() {
		return tool;
	}
	
	public void setTool(Tool tool) {
		this.tool = tool;
		if(this.tool != Tool.ICON)
			icon = null;
		for(ToolListener t : toolTrackers)
			t.onToolChanged();
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		this.color = c;
		for(ToolListener t : toolTrackers)
			t.onColorChanged();
	}
	
	public void setIcon(MapIcon i) {
		this.icon = i;
	}
	
	public MapIcon getIcon() {
		return icon;
	}
	
	public DataManager getData() {
		return data;
	}
	
	public void registerToolListener(ToolListener reg) {
		toolTrackers.add(reg);
	}
	
	public void deregisterToolListener(ToolListener reg) {
		toolTrackers.remove(reg);
	}
}