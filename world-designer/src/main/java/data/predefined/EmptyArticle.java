package data.predefined;

import data.abstracts.AbstractArticle;

public class EmptyArticle extends AbstractArticle
{

	public EmptyArticle(String name) {
		super(name);
	}

	@Override
	public String insertString() {
		return null;
	}
	
}