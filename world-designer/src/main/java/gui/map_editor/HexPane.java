package gui.map_editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.map.LandMap;
import data.map.MapHex;
import gui.map_editor.ToolSet.Tool;
import gui.utilities.CompFactory;
import gui.utilities.ImageManager.MapIcon;
import gui.utilities.StyleManager;

public class HexPane extends JPanel {
	public final ToolSet toolSet;
	
	private HashMap<MapHex, Hex> hexs;
	private final int startX = 60, startY = 60;
	private final int alpha = 150; // 0 = fully transparent, 255 = fully opaque

	private int rows, cols, size;
	private BufferedImage staticLayer;

	private Hex workingHex;
	private LandMap map;
	
	public HexPane(LandMap map, ToolSet tool) {
		super();
		this.toolSet = tool;
		hexs = new HashMap<MapHex, Hex>();
		workingHex = null;
		this.map = map;
		buildGrid(map.rows, map.cols, StyleManager.GRID_SIZE);
		buildStaticLayer();
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
				MapHex hexInfo = hexIter.next();
				Hex h = new Hex(
						startX + ((size + size / 2) * col),
						((col % 2 == 0) ? startY + (size + size) * row : 
							startY + size + (size + size) * row), 
						hexInfo, 
						size);
				hexs.put(hexInfo, h);
			}
		repaint();
	}
	
	private void buildMouseControls() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Hex edit;
				if(workingHex == null) 
					edit = getHexGlobal(e);
				else if(!workingHex.contains(e.getPoint()))
					edit = getHexGlobal(e);
				else
					edit = workingHex;
				
				switch(toolSet.getTool()) {
				case SELECT -> processSelect(edit);
				case BRUSH -> changeColor(edit, toolSet.getColor());
				case FILL -> floodFill(edit, toolSet.getColor());
				case ICON -> addIcon(edit, toolSet.getIcon());
				
				}
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				HexAdjust(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				HexAdjust(e);
				
				switch(toolSet.getTool()) {
				case SELECT -> processSelect(null);
				case BRUSH -> changeColor(workingHex, toolSet.getColor());
				case FILL -> changeColor(null, null);
				case ICON ->{
					if(toolSet.getIcon() == MapIcon.EMPTY)
						addIcon(workingHex, toolSet.getIcon());
					}
				}
			}
			public void HexAdjust(MouseEvent e) {
				Hex tmp;
				if(workingHex == null) {
					tmp = getHexGlobal(e);
				} else {
					tmp = getHexNeighbors(e);
				}
				if(workingHex != tmp) {
					workingHex = tmp;
					repaint();
				}
			}
		});
	}
	
	private Hex getHexGlobal(MouseEvent e) {
		for (Hex h : hexs.values()) {
			if (h.contains(e.getPoint())) {
				return h;
			}
		}
		return null;
	}
	
	private Hex getHexNeighbors(MouseEvent e) {
		if(workingHex != null) {
			if(workingHex.contains(e.getPoint())) {
				return workingHex;
			} else {
				for(MapHex mHex : workingHex.hexInfo.getNeighbors())
					if(hexs.get(mHex).contains(e.getPoint()))
						return hexs.get(mHex);
				return getHexGlobal(e);
			}
		}else
			return getHexGlobal(e);
	}
	
	private void processSelect(Hex sel) {
		if(sel != null) {
			if(sel.getIcon() != null) {
				
			}
		}
	}
	
	private void addIcon(Hex edit, MapIcon icon) {
		if(edit != null) {
			edit.setIcon(icon.getIcon());
			updateStaticLayer(edit);
		}
	}
	
	private void changeColor(Hex edit, Color c) {
		if(edit != null) {
			edit.setColor(c);
			updateStaticLayer(edit);
		}
	}
	
	private void floodFill(Hex start, Color tar) {
		if (start == null) return;

	    Color srcColor = start.getColor();
	    if (srcColor.equals(tar)) return; // nothing to do

	    
	    Set<Hex> visited = new HashSet<>();
	    Stack<Hex> stack = new Stack<>();
	    ArrayList<Hex> edited = new ArrayList<Hex>();
	    stack.push(start);

	    while (!stack.isEmpty()) {
	        Hex current = stack.pop();
	        if (visited.contains(current)) continue;
	        visited.add(current);

	        // Only process if color matches original source color
	        if (!current.getColor().equals(srcColor)) continue;

	        current.setColor(tar);
	        edited.add(current);

	        // Push neighbors that have the same original color
	        for (MapHex neighbor : current.hexInfo.getNeighbors()) {
	            Hex h = hexs.get(neighbor);
	            if (h != null && !visited.contains(h) && h.getColor().equals(srcColor)) {
	                stack.push(h);
	            }
	        }
	    }
	    
	    updateStaticLayer(edited);
	}
	
	private void updateStaticLayer(Hex update) {
		Graphics2D g2 = staticLayer.createGraphics();
		g2.setColor(update.getColor());
        g2.fillPolygon(update);
        
        g2.setColor(getContrastingTextColor(update.getColor()));
		g2.setFont(new Font("Monospaced", Font.BOLD, 12));
		g2.drawString("" + update.getRow() + "," + update.getCol(), 
				update.xco[5] + (size / 2), update.yco[2]);
		
		if(update.getIcon() != null) {
			g2.drawImage(update.getIcon().getScaledInstance(32, 32, Image.SCALE_SMOOTH),
					update.xco[5] + (size / 2), 
					update.yco[2], null);
		}
		
		g2.setColor(Color.black);
	    g2.setStroke(new BasicStroke(2.0f));
	    g2.drawPolygon(update);
	    
	    repaint();
	}
	
	private void updateStaticLayer(ArrayList<Hex> updates) {
		Graphics2D g2 = staticLayer.createGraphics();
		for(Hex update : updates) {
			
			g2.setColor(update.getColor());
	        g2.fillPolygon(update);
	        
	        g2.setColor(getContrastingTextColor(update.getColor()));
			g2.setFont(new Font("Monospaced", Font.BOLD, 12));
			g2.drawString("" + update.getRow() + "," + update.getCol(), 
					update.xco[5] + (size / 2), update.yco[2]);
			
			if(update.getIcon() != null) {
				g2.drawImage(update.getIcon().getScaledInstance(32, 32, Image.SCALE_SMOOTH),
						update.xco[5] + (size / 2), 
						update.yco[2], null);
			}
			
			g2.setColor(Color.black);
		    g2.setStroke(new BasicStroke(2.0f));
		    g2.drawPolygon(update);
		}
		repaint();		
	}
	
	private void buildStaticLayer() {
	    staticLayer = new BufferedImage(this.getPreferredSize().width, 
	    		getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = staticLayer.createGraphics();
	    for (Hex h : hexs.values()) {
	        g2.setColor(h.getColor());
	        g2.fillPolygon(h);
			
	        g2.setColor(getContrastingTextColor(h.getColor()));
			g2.setFont(new Font("Monospaced", Font.BOLD, 12));
			g2.drawString("" + (h.getRow() + 1) + "," + (h.getCol() + 1), 
					h.xco[5] + (size / 2), h.yco[2]);
			
			if(h.getIcon() != null) {
				g2.drawImage(h.getIcon().getScaledInstance(32, 32, Image.SCALE_SMOOTH),
						h.xco[5] + (size / 2), 
						h.yco[2], null);
			}
			
			g2.setColor(Color.black);
		    g2.setStroke(new BasicStroke(2.0f));
		    g2.drawPolygon(h);
	    }
	    g2.dispose();
	}
	
	public static Color getContrastingTextColor(Color background) {
	    int r = background.getRed();
	    int g = background.getGreen();
	    int b = background.getBlue();

	    double brightness = 0.299 * r + 0.587 * g + 0.114 * b;

	    return brightness < 128 ? Color.WHITE : Color.BLACK;
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
		if(staticLayer != null)
			g.drawImage(staticLayer, 0, 0, null);
		if(workingHex != null) {
			Color base = Color.YELLOW; // solid yellow
			Color translucentYellow = new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
			g.setColor(translucentYellow);
			g.fillPolygon(workingHex);
			
		}
	}
	
	
}