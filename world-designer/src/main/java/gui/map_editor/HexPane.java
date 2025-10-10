package gui.map_editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import data.map.LandMap;
import data.map.MapHex;
import gui.utilities.CompFactory;

public class HexPane extends JPanel {
	public static void main(String[]args) {
		LandMap map = new LandMap("Test", 10, 10);
		Iterator<MapHex> mapIter = map.iterator();
		while(mapIter.hasNext()) {
			MapHex hex =mapIter.next();
			hex.name = hex.row + "," + hex.col;
			hex.color = new Color(rNum(), rNum(), rNum());
		}
		
		CompFactory.buildTestWindow(new HexPane(map, 32));
	}
	
	public static int rNum() {
		return (int)(Math.random() * 255) + 1;
	}
	private ArrayList<Hex> hexs;
	private final int startX = 60, startY = 60;
	private int rows, cols, size;

	private Hex workingHex;
	private LandMap map;
	
	public HexPane(LandMap map, int gridSize) {
		super();
		hexs = new ArrayList<Hex>();
		workingHex = null;
		this.map = map;
		buildGrid(map.rows, map.cols, gridSize);
		this.setLayout(null);
		
		buildMouseControls();
	}

	private void buildGrid(int rows, int cols, int size) {
		this.rows = rows;
		this.cols = cols;
		this.size = size;
		Iterator<MapHex> hexIter = map.iterator();
		this.setPreferredSize(new Dimension(rows * (size + size / 2) + 
				startX * 2, cols * (2 * size) + startY * 2));
		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++) {
				Hex h = new Hex(
						startX + ((size + size / 2) * col),
						((col % 2 == 0) ? startY + (size + size) * row : 
							startY + size + (size + size) * row), 
						hexIter.next(), 
						size);
				hexs.add(h);
			}
		repaint();
	}
	
	private void buildMouseControls() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for(Hex h : hexs)
					if(h.contains(e.getPoint()))
						System.out.println(h.getHexInfo().name);
			}
		});
	}
	
	public Hex getWorking() {
		return workingHex;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("Painting");
		for (Hex h : hexs) {
			g.setColor(h.getColor());
			g.fillPolygon(h);
			g.setColor(Color.black);
			g.drawPolygon(h);
			g.setFont(new Font("Monospaced", Font.BOLD, 12));
			g.drawString("" + h.getRow() + "," + h.getCol(), h.xco[5] + (size / 2), h.yco[2]);
		}
	}
	
	
}