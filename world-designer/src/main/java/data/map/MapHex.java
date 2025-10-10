package data.map;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import data.MapKey;

public class MapHex implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2597262684518598694L;
	public Color color;
	public boolean icon, displayName;
	public String name;
	public ArrayList<MapKey> associatedKeys;
	
	public final int row, col;
	
	public MapHex(int row, int col) {
		this.row = row;
		this.col = col;
		associatedKeys = new ArrayList<MapKey>();
	}
}