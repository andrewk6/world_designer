package data.map;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.MapKey;

public class MapHex implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2597262684518598694L;
	public Color color;
	public boolean hasIcon, displayName;
	public String name;
	public Image icon;
	public ArrayList<MapKey> associatedKeys;
	private ArrayList<MapHex> neighbors = null;
	
	public final int row, col;
	
	public MapHex(int row, int col) {
		this.row = row;
		this.col = col;
		associatedKeys = new ArrayList<MapKey>();
	}
	
	public void setNeighbors(ArrayList<MapHex> neigh) {
		this.neighbors = neigh;
	}
	
	public List<MapHex> getNeighbors() {
		return Collections.unmodifiableList(neighbors);
	}
	
	public String printCoords() {
		return "(" + row + "," + col + ")";
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public Image getIcon() {
		return icon;
	}
}