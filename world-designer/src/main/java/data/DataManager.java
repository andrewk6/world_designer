package data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import data.abstracts.AbstractArticle;
import data.listeners.WorldListener;
import data.predefined.BasicArticle;
import data.predefined.WorldArticle;

public class DataManager
{
	public static final File appFolder = new File("dictionaries");
	private boolean worldEdited = false;
	
	private WorldArticle world;
	private ArrayList<WorldListener> worldListeners;
	
	public DataManager() {
		world = null;
		worldListeners = new ArrayList<WorldListener>();
	}
	
	public void createNewWorld(String worldName) {
		if(!worldEdited) {
			world = new WorldArticle(worldName);
			notifyWorldChange();
		}
		
	}
	
	public WorldArticle getWorld() {
		return world;
	}
	
	public MapKey getWorldKey() {
		return world.key;
	}
	
	public DefaultMutableTreeNode getWorldTreeRoot() {
		return world.root;
	}
	
	public void addArticleToWorld(BasicArticle ba) {
		world.articleMap.put(ba.key, ba);
	}
	
	public Map<MapKey, BasicArticle> getArticleMap(){
		return Collections.unmodifiableMap(world.articleMap);
	}
	
	public void registerWorldListener(WorldListener wl) {
		worldListeners.add(wl);
	}
	
	public void deregisterWorldListener(WorldListener wl) {
		worldListeners.remove(wl);
	}
	
	public void notifyWorldChange() {
		for(WorldListener wl : new ArrayList<>(worldListeners)) {
			wl.onWorldChanged();
		}
	}
	
	public void Exit() {
		if(!worldEdited)
			System.exit(0);
	}
}