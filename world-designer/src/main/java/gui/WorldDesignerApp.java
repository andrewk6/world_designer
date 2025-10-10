package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.BorderFactory;
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
import data.articles.predefined.WorldArticle;
import data.listeners.MapKeyNameListener;
import data.listeners.WorldListener;
import gui.design_panes.WorldDesignPane;
import gui.design_panes.article_editor.ArticleTreePane;
import gui.design_panes.article_editor.SelectedNodeListener;
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
	private final JTabbedPane editTabs = new JTabbedPane();
	
	private WorldDesignPane worldDetailPane;
	private ArticleTreePane artTreePane;
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
	}
	
	private void buildContent(Container c) {
		c.setLayout(new BorderLayout());
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		c.add(mainPane, BorderLayout.CENTER);
		
		JPanel cardWrapper = new JPanel();
		cardWrapper.setLayout(new CardLayout());
		CardLayout cl = (CardLayout) cardWrapper.getLayout();
//		WorldListener cardListen = new WorldListener() {
//			@Override
//			public void onWorldChanged() {
//				cl.show(cardWrapper, "edit");
//				data.deregisterWorldListener(this);
//				
//				WorldListener tabListener = new WorldListener() {
//					@Override
//					public void onWorldChanged() {
//						buildTabPanes(data.getWorld());
//					}
//				};
//				data.registerWorldListener(tabListener);
//			}
//		};
//		data.registerWorldListener(cardListen);
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
	}

	@Override
	public void onWorldChanged() {	
		Component tabComp = editTabs.getSelectedComponent();
		if(data.getWorld() != null && articleMenu.isVisible() == false) {
			articleMenu.setVisible(true);
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
		}
	}
}