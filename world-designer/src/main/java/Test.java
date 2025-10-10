import data.MapKey;
import data.map.LandMap;
import data.map.MapHex;

public class Test
{
	public static void main(String[]args) {
		LandMap map = new LandMap(new MapKey("Test Grid"), 5, 7);
		
		int iter = 1;
		for(int r = 0; r < map.hexGrid.length; r ++)
			for(int c = 0; c < map.hexGrid[r].length; c++) {
				MapHex hex = new MapHex(r, c);
				hex.name = "" + iter;
				map.hexGrid[r][c] = hex;
				iter++;
			}
		
		for(int r = 0; r < map.hexGrid.length; r ++) {
			for(int c = 0; c < map.hexGrid[r].length; c++)
				System.out.print(map.hexGrid[r][c].name + ", ");
			System.out.println();
		}
	}
}