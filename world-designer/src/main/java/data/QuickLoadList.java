package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class QuickLoadList<T> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1096092458262863389L;
	private final int maxSize;
	private final LinkedList<T> recentList;

	public QuickLoadList(int maxSize) {
		this.maxSize = maxSize;
		this.recentList = new LinkedList<>();
	}

	public void load(T item) {
		// Remove if it already exists
		recentList.remove(item);

		// Add to top
		recentList.addFirst(item);

		// Enforce max size
		if (recentList.size() > maxSize) {
			recentList.removeLast();
		}
	}

	public List<T> getList() {
		return List.copyOf(recentList); // Immutable view
	}

	public void clear() {
		recentList.clear();
	}
	
	public int size() {
		return recentList.size();
	}
}