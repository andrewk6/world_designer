package gui.map_editor;
import java.awt.Color;
import java.awt.Polygon;
import java.util.Arrays;

import data.map.MapHex;

public class Hex extends Polygon
{
	private final int size;
//	public int row, col;
	public int[] xco, yco;
	public boolean selected;
	public MapHex hexInfo;
	
	public Hex(int x1, int y1, MapHex info, int size)
	{
		super();
		if(size % 2 != 0)
			throw new IllegalArgumentException("Uneven Hexagon");
		this.size = size;
		xco = new int[6];
		yco = new int[6];
		
		setCoords(x1, y1);
		npoints = 6;
		this.hexInfo = info;
	}
	
	private void setCoords(int x1, int y1)
	{
		xco[0] = x1;
		xco[1] = x1 + size;
		xco[2] = x1 + size + (size / 2);
		xco[3] = x1 + size;
		xco[4] = x1;
		xco[5] = x1 - (size / 2);
		
		
		yco[0] = y1;
		yco[1] = y1;
		yco[2] = y1 + size;
		yco[3] = y1 + size + size;
		yco[4] = y1 + size + size;
		yco[5] = y1 + size;
		
		xpoints = xco;
		ypoints = yco;
	}
	
	public Color getColor() {
		return hexInfo.color;
	}
	
	public int getRow() {
		return hexInfo.row;
	}
	
	public int getCol() {
		return hexInfo.col;
	}
	
	public MapHex getHexInfo() {
		return hexInfo;
	}
}