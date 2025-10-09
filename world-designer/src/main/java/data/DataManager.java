package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;

import data.DataEnums.ArticleCategory;
import data.abstracts.AbstractArticle;
import data.listeners.WorldListener;
import data.predefined.BasicArticle;
import data.predefined.WorldArticle;
import gui.dialogs.FXFileDialog;

public class DataManager
{
	public static final File appFolder = new File("dictionaries");
	private boolean worldEdited = false;
	
	private WorldArticle world;
	private ArrayList<WorldListener> worldListeners;
	
	private JFrame appFrame;
	
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
	
	public void removeArticleFromWorld(MapKey key) {
		world.articleMap.remove(key);
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
	
	public void registerAppFrame(JFrame frm) {
		this.appFrame = frm;
	}
	
	public void exit() {
		if(!worldEdited)
			System.exit(0);
	}
	
	public void saveWorld() {
//		JFileChooser fChoose = new JFileChooser();
		File f = FXFileDialog.saveWorldFile(appFrame);
		if(f != null) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(f));
				System.out.println("Writing World Cat: " + world.styles.toString());
				oos.writeObject(world);
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadWorld() {
//		JFileChooser fChoose = new JFileChooser();
//		if(fChoose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		File f = FXFileDialog.openWorldFile(appFrame);
		if(f != null) {
			try {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(f));
				Object read = ois.readObject();
				if(read instanceof WorldArticle w) {
					world = w;
					notifyWorldChange();
				}
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}