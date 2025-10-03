package data.abstracts;

public abstract class AbstractArticle implements InsertHelper, Comparable<AbstractArticle>
{
	public String name;
	
	public AbstractArticle(String name)
	{
		this.name = name;
	}
	public String toString() {
		return name;
	}
	@Override
	public int compareTo(AbstractArticle o) {
		return this.name.compareToIgnoreCase(o.name);
	}
}