package data.articles.abstracts;

import java.io.Serializable;

import data.MapKey;
import data.articles.InsertHelper;

public abstract class AbstractArticle implements 
	InsertHelper, Comparable<AbstractArticle>, Serializable
{
	public MapKey key;
	
	public AbstractArticle(String name)
	{
		this.key = new MapKey(name);
	}
	public String toString() {
		return key.toString();
	}
	@Override
	public int compareTo(AbstractArticle o) {
		return this.key.compareTo(o.key);
	}
	
}