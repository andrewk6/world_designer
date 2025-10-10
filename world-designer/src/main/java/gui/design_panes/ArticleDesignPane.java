package gui.design_panes;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import data.DataManager;
import data.MapKey;
import data.articles.predefined.BasicArticle;
import data.listeners.MapKeyNameListener;
import gui.components.ReminderField;
import gui.components.editor.RichEditor;
import gui.utilities.CompFactory;
import gui.utilities.StyleManager.FontStyle;

public class ArticleDesignPane extends JPanel
{
	public static void main(String[]args) {
		DataManager data = new DataManager();
		BasicArticle art = new BasicArticle("Article");
		CompFactory.buildTestWindow(new ArticleDesignPane(data, art), ()->{
			System.out.println("Name: " + art.key.getName());
			System.out.println("Insert: " + art.insert);
			try {
				System.out.println("Doc: " + art.doc.getText(0, art.doc.getLength()));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		});
	}
	private DataManager data;
	private BasicArticle article;
	
	public ArticleDesignPane(DataManager data, BasicArticle art) {
		super();
		this.article = art;
		this.data = data;
		init();
	}
	
	private void init() {
		this.setLayout(new BorderLayout());
		
		JPanel headPane = new JPanel();
		headPane.setLayout(new BorderLayout());
		this.add(headPane, BorderLayout.NORTH);
		JLabel artNameLbl = CompFactory.createLabel(article.key.getName());
		MapKeyNameListener nameUpdateList = new MapKeyNameListener() {
			@Override
			public void onNameChange() {
				artNameLbl.setText(article.key.getName());
			}
		};
		article.key.registerNameListener(nameUpdateList);
		
		headPane.add(CompFactory.createSplitPane(
				CompFactory.createLabel("Article Name: ", FontStyle.BOLD1),
				artNameLbl), BorderLayout.CENTER);
		headPane.add(CompFactory.createSplitPane(
				CompFactory.createLabel("Insert Text for Documents: ", FontStyle.BOLD1),
				CompFactory.createUpdateField("Insert Text...", article.insert, 
						text-> article.setInsert(text))
				), BorderLayout.SOUTH);
		
		RichEditor edit = new RichEditor(data);
		edit.loadDocument(article.doc);
		this.add(edit, BorderLayout.CENTER);
	}
	
	public MapKey getArticleKey() {
		return article.key;
	}
}