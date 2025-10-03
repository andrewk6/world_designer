package data;

import java.io.File;
import java.util.HashMap;

import data.abstracts.AbstractArticle;

public class DataManager
{
	public static final File appFolder = new File("dictionaries");
	
	private final HashMap<String, AbstractArticle> objMap;
	
	public DataManager() {
		objMap = new HashMap<String, AbstractArticle>();
	}
}