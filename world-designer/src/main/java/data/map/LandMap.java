package data.map;

import java.io.Serializable;
import java.util.Iterator;
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
	
	public LandMap(MapKey name, int rows, int cols) {
		hexGrid = new MapHex[rows][cols];
		this.rows = rows;
		this.cols = cols;
		key= name;
		
		for(int r = 0; r < rows; r ++)
			for(int c = 0; c < cols; c ++)
				hexGrid[r][c] = new MapHex(r, c);
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
}