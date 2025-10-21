package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import data.DataManager;
import data.MapKey;
import data.articles.predefined.WorldArticle;
import data.listeners.MapKeyNameListener;
import data.listeners.WorldListener;
import data.map.LandMap;
import gui.design_panes.WorldDesignPane;
import gui.design_panes.article_editor.ArticleTreePane;
import gui.design_panes.article_editor.SelectedNodeListener;
import gui.dialogs.MapDialog;
import gui.dialogs.MapLoadDialog;
import gui.map_editor.MapEditorPane;
import gui.utilities.CompFactory;
import gui.utilities.StyleManager.FontStyle;

public class WorldDesignerApp extends JFrame implements WorldListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3031407343997357300L;

	private final MapKeyNameListener tabTitleUpdate = new MapKeyNameListener() {
		@Override
		public void onNameChange() {
			editTabs.setTitleAt(0, data.getWorldKey().getName() + " - Details");
			editTabs.setTitleAt(1, data.getWorldKey().getName() + " - Articles");
		}
	};
	
	private final DataManager data;
	
	private final JMenu articleMenu = CompFactory.createMenu("Articles");
	private final JMenu mapMenu = CompFactory.createMenu("Maps");
	private final JMenu quickLoadMenu = CompFactory.createMenu("Quick Load Map");
	private final JTabbedPane editTabs = new JTabbedPane();
	
	private WorldDesignPane worldDetailPane;
	private ArticleTreePane artTreePane;
	private MapEditorPane mapEditPane;
	
	private SelectedNodeListener nodeListen;
	
	public WorldDesignerApp(DataManager data)
	{
		this.data = data;
		this.data.registerAppFrame(this);
		this.data.registerWorldListener(this);
		
		buildContent(getContentPane());
		buildToolbar();
		configFrame();
	}
	
	public void launch() {
		this.setSize(800, 800);
		this.setVisible(true);
	}
	
	private void configFrame() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(CompFactory.createSafeExitListener(null, data));
		this.pack();
	}
	
	private void buildToolbar() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu worldMenu = CompFactory.createMenu("Worlds", new JMenuItem[] {
				CompFactory.createMenuItem("New World", e->{
					String worldName = JOptionPane.showInputDialog(this, "What is the world's name?");
					if(worldName != null)
						if(worldName.length() > 0)
							data.createNewWorld(worldName);
				}),
				CompFactory.createMenuItem("Load World", e->data.loadWorld())
		});
		menuBar.add(worldMenu);
		
		JMenuItem saveItem = CompFactory.createMenuItem("Save World", e->data.saveWorld());
		saveItem.setEnabled(false);
		worldMenu.add(saveItem);
		WorldListener saveEnable = new WorldListener() {
			@Override
			public void onWorldChanged() {
				SwingUtilities.invokeLater(()->saveItem.setEnabled(true));
				data.deregisterWorldListener(this);
			}
		};
		data.registerWorldListener(saveEnable);
		
		articleMenu.setVisible(false);
		menuBar.add(articleMenu);
		articleMenu.add(CompFactory.createMenuItem("New Folder", 
				e->artTreePane.newFolder(data.getWorldTreeRoot())));
		JMenuItem renameItem = CompFactory.createMenuItem("Rename", e->{
			artTreePane.getTree().renameNode(artTreePane.getTree().getSelectedNode());
		});
		renameItem.setEnabled(false);
		articleMenu.add(renameItem);
		
		JMenuItem deleteItem = CompFactory.createMenuItem("Delete", e->{
			artTreePane.getTree().deleteNode(artTreePane.getTree().getSelectedNode());
		});
		deleteItem.setEnabled(false);
		articleMenu.add(deleteItem);
		
		nodeListen = new SelectedNodeListener() {
			@Override
			public void onNodeSelected() {
				SwingUtilities.invokeLater(()->{
					if(!renameItem.isEnabled())
						renameItem.setEnabled(true);
					if(!deleteItem.isEnabled())
						deleteItem.setEnabled(true);
					
					if(artTreePane.getTree().getSelectedNode().getUserObject() instanceof String) {
						renameItem.setText("Rename Folder");
						deleteItem.setText("Delete Folder");
					}else {
						renameItem.setText("Rename Article");
						deleteItem.setText("Delete Article");
					}
				});
			}
			@Override
			public void onNodeDeselected() {
				SwingUtilities.invokeLater(()->{
					renameItem.setEnabled(false);
					deleteItem.setEnabled(false);
				});
			}
		};
		
		articleMenu.add(CompFactory.createMenuItem(
				"Expand All", e->artTreePane.getTree().expandAll(true)));
		articleMenu.add(CompFactory.createMenuItem(
				"Collapse All", e->artTreePane.getTree().expandAll(false)));
		
		mapMenu.setVisible(false);
		menuBar.add(mapMenu);
		
		mapMenu.add(CompFactory.createMenuItem("New Map", e->{
			LandMap map = MapDialog.showDialog(SwingUtilities.getWindowAncestor(this));
			if(map != null) {
				data.addMapToWorld(map);
				loadMap(map.key);
				updateQuickList();
			}
		}));
		
		mapMenu.add(CompFactory.createMenuItem("Load Map", e->{
			MapKey loadKey = MapLoadDialog.showDialog(
					SwingUtilities.getWindowAncestor(this), data);
			if(loadKey != null) {
				loadMap(loadKey);
				updateQuickList();
			}
		}));
		
		mapMenu.add(quickLoadMenu);
	}
	
	private void updateQuickList() {
		if(data.getWorld() == null) return;
		List<MapKey> keys = data.getQuickLoads();
		if(keys == null) return;
		
		SwingUtilities.invokeLater(() -> {
			quickLoadMenu.removeAll();
			if(keys.size() == 0)
				quickLoadMenu.setEnabled(false);
			else {
				quickLoadMenu.setEnabled(true);
				
				for(MapKey key : keys) {
					quickLoadMenu.add(CompFactory.createMenuItem(
							key.getName(), e-> {
								loadMap(key);
								updateQuickList();
							}));
				}
			}
		});
	}
	
	private void loadMap(MapKey key) {
		if(mapEditPane == null) {
			mapEditPane = new MapEditorPane(data, data.getMap(key));
			editTabs.addTab("Map Editor", mapEditPane);
		}else {
//			editTabs.removeTabAt(editTabs.indexOfComponent(mapEditPane));
//			mapEditPane = new MapEditorPane(data, data.getMap(key));
//			editTabs.addTab("Map Editor", mapEditPane);
			mapEditPane.loadMap(data.getMap(key));
		}
	}
	
//	private void addMapPane(LandMap map) {
//		JPanel mapPane = new JPanel();
//		mapPane.setLayout(new BorderLayout());
//		
//		MapEditorPane mEdit = new MapEditorPane(data, map);
//		mapPane.add(mEdit, BorderLayout.CENTER);
//		
//		JPanel btnFlow = CompFactory.createButtonFlow(FlowLayout.RIGHT, new JButton[] {
//				CompFactory.createButton("Remove " + map.key.getName(), e->{
//					editTabs.removeTabAt(editTabs.indexOfComponent(mapPane));
//				})
//		});
//		mapPane.add(btnFlow, BorderLayout.SOUTH);
//		editTabs.addTab(map.key.getName(), mapPane);
//	}
	
	private void buildContent(Container c) {
		c.setLayout(new BorderLayout());
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		c.add(mainPane, BorderLayout.CENTER);
		
		JPanel cardWrapper = new JPanel();
		cardWrapper.setLayout(new CardLayout());
		CardLayout cl = (CardLayout) cardWrapper.getLayout();
		mainPane.add(cardWrapper, BorderLayout.CENTER);
		
		JPanel noLoadPane = new JPanel();
		noLoadPane.setLayout(new BorderLayout());
		noLoadPane.add(CompFactory.createLabel("No World Loaded.", FontStyle.HEADER2));
		cardWrapper.add(noLoadPane, "noload");
		
		cardWrapper.add(editTabs, "edit");
		
		cl.show(cardWrapper, "noload");
		
		JLabel worldLbl = CompFactory.createLabel("");
		MapKeyNameListener lblNameListen = new MapKeyNameListener() {
			@Override
			public void onNameChange() {
				worldLbl.setText(data.getWorldKey().getName());
			}
		};
		WorldListener lblListen = new WorldListener() {
			@Override
			public void onWorldChanged() {
				worldLbl.setText(data.getWorldKey().getName());
				data.getWorldKey().registerNameListener(lblNameListen);
			}
		};

		data.registerWorldListener(lblListen);
		JPanel worldPane = CompFactory.createSplitPane("World: ", worldLbl);
		worldPane.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		mainPane.add(worldPane, BorderLayout.NORTH);
	}
	
	private void buildTabPanes(WorldArticle w) {
		worldDetailPane = new WorldDesignPane(data, w);
		editTabs.addTab(w.key.getName() + " - Details", worldDetailPane);
		
		artTreePane = new ArticleTreePane(data);
		artTreePane.getTree().registerSelectedNodeListener(nodeListen);
		editTabs.addTab(w.key.getName() + " - Articles", artTreePane);
		
//		mapPane = new MapEditorPane(data, null)
	}

	@Override
	public void onWorldChanged() {	
		Component tabComp = editTabs.getSelectedComponent();
		if(data.getWorld() != null && articleMenu.isVisible() == false) {
			articleMenu.setVisible(true);
			mapMenu.setVisible(true);
			updateQuickList();
		}
		
		if(editTabs.isVisible() == false || 
				(worldDetailPane == null && artTreePane == null))
		{
			SwingUtilities.invokeLater(()->{
				buildTabPanes(data.getWorld());
				CardLayout cl = (CardLayout) editTabs.getParent().getLayout();
				cl.show(editTabs.getParent(), "edit");
				
				SwingUtilities.invokeLater(()->{
					data.getWorldKey().registerNameListener(tabTitleUpdate);
				});
				
			});			
		}else {
			worldDetailPane.loadWorld(data.getWorld());
			artTreePane.loadTree(data.getWorldTreeRoot());
			if(tabComp != null)
				editTabs.setSelectedComponent(tabComp);
			for(int i = editTabs.getTabCount(); i > 0; i++) {
				if(!(editTabs.getTabComponentAt(i) instanceof WorldDesignPane) &&
						!(editTabs.getTabComponentAt(i) instanceof ArticleTreePane))
					editTabs.removeTabAt(i);
			}
		}
	}
}