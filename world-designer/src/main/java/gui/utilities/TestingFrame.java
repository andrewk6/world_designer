package gui.utilities;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

import data.DataManager;
import data.MapKey;
import data.articles.predefined.BasicArticle;

public class TestingFrame extends JFrame
{
	private final DataManager data;
	public TestingFrame(Container pane) throws BadLocationException {
		super();
		data = TestingFrame.buildDatabaseTest();
		StyleManager.setLookAndFeel();
		Container cPane = getContentPane();
		cPane.setLayout(new BorderLayout());
		cPane.add(pane, BorderLayout.CENTER);
		
		addWindowListener(CompFactory.createSafeExitListener(null, data));
	}
	
	public TestingFrame(Container pane, Runnable r) throws BadLocationException {
		this(pane);
		Container cPane = getContentPane();
		cPane.add(CompFactory.createButtonFlow(FlowLayout.RIGHT, new JButton[] {
				CompFactory.createButton("Test", r)
		}), BorderLayout.SOUTH);
	}
	
	public void testRun() {
		pack();
		setSize(800, 800);
		setVisible(true);
	}
	
	public DataManager getData() {
		return data;
	}
	
	public static DataManager buildDatabaseTest() throws BadLocationException {
		DataManager data = new DataManager();
		data.createNewWorld("Test world");
		data.addArticleToWorld(new BasicArticle("Test"));
		data.addArticleToWorld(new BasicArticle("Test2"));
		data.addArticleToWorld(new BasicArticle("Test3"));
		data.addArticleToWorld(new BasicArticle("Test4"));
		data.addArticleToWorld(new BasicArticle("Test5"));
		data.addArticleToWorld(new BasicArticle("Test6"));
		data.addArticleToWorld(new BasicArticle("Test7"));
		data.addArticleToWorld(new BasicArticle("Test8"));
		int iter = 0;
		for(MapKey key : data.getArticleMap().keySet()) {
			data.getArticleMap().get(key).doc.insertString(0, ("Test article " + iter), null);
			iter++;
		}	
		return data;
	}
}