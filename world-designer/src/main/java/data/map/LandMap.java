package data.map;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import data.MapKey;

public class LandMap implements Serializable, Iterable<MapHex>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5008900922331250201L;
	
	public final MapKey key;
	public final MapHex[][] hexGrid;
	public final int rows, cols;
	
	public List<Color> swatches;
	
	public LandMap(MapKey name, int rows, int cols) {
		hexGrid = new MapHex[rows][cols];
		this.rows = rows;
		this.cols = cols;
		key= name;
		swatches = new ArrayList<Color>();
		
		for(int r = 0; r < rows; r ++)
			for(int c = 0; c < cols; c ++)
				hexGrid[r][c] = new MapHex(r, c);
		
		Iterator<MapHex> mapIter = iterator();
		while(mapIter.hasNext()) {
			MapHex hex = mapIter.next();
			hex.name = hex.row + "," + hex.col;
			hex.color = Color.WHITE;
		}
		
		for(int r = 0; r < rows; r ++)
			for(int c = 0; c < cols; c ++)
				hexGrid[r][c].setNeighbors(buildNeighbors(r, c));
	}
	
	public LandMap(String name, int rows, int cols) {
		this(new MapKey(name), rows, cols);
	}

	@Override
    public Iterator<MapHex> iterator() {
        return new Iterator<>() {
            private int row = 0;
            private int col = 0;

            @Override
            public boolean hasNext() {
                return row < hexGrid.length && col < hexGrid[0].length;
            }

            @Override
            public MapHex next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                MapHex current = hexGrid[row][col];
                col++;
                if (col >= hexGrid[0].length) {
                    col = 0;
                    row++;
                }
                return current;
            }
        };
    }
	
//	public ArrayList<MapHex> buildNeighbors(int row, int col) {
//        ArrayList<MapHex> neighbors = new ArrayList<>();
//
//        int totalRows = hexGrid.length;
//        int totalCols = hexGrid[0].length;
//
//        // All 8 directions around a cell
//        int[] dRows = {-1, -1, -1, 0, 0, 1, 1, 1};
//        int[] dCols = {-1, 0, 1, -1, 1, -1, 0, 1};
//
//        for (int i = 0; i < dRows.length; i++) {
//            int newRow = row + dRows[i];
//            int newCol = col + dCols[i];
//
//            // Skip out of bounds
//            if (newRow < 0 || newCol < 0 || newRow >= totalRows || newCol >= totalCols)
//                continue;
//
//            neighbors.add(hexGrid[newRow][newCol]);
//        }
//
//        return neighbors;
//    }
	
	public ArrayList<MapHex> buildNeighbors(int row, int col) {
	    ArrayList<MapHex> neighbors = new ArrayList<>();
	    int totalRows = hexGrid.length;
	    int totalCols = hexGrid[0].length;

	    boolean isEvenCol = (col % 2 == 0);

	    // Offsets for even vs odd columns
	    int[][] offsets;
	    if (isEvenCol) {
	        offsets = new int[][] {
	            {-1, 0}, {-1, 1}, {0, 1}, {1, 0}, {0, -1}, {-1, -1}
	        };
	    } else {
	        offsets = new int[][] {
	            {-1, 0}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}
	        };
	    }

	    for (int[] off : offsets) {
	        int newRow = row + off[0];
	        int newCol = col + off[1];

	        if (newRow >= 0 && newRow < totalRows && newCol >= 0 && newCol < totalCols) {
	            neighbors.add(hexGrid[newRow][newCol]);
	        }
	    }

	    return neighbors;
	}

}