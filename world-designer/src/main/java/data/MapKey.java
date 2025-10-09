package data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import data.listeners.MapKeyNameListener;

public class MapKey implements Comparable<MapKey>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4475803395870535022L;

	private String name;
	private UUID id;
	
	private transient ArrayList<MapKeyNameListener> nameUpdates;
	
	
	public MapKey() {
		id = UUID.randomUUID();
		nameUpdates = new ArrayList<MapKeyNameListener>();
	}
	
	public MapKey(String name) {
		this.name = name;
		id = UUID.randomUUID();
		nameUpdates = new ArrayList<MapKeyNameListener>();
	}
	
	public UUID getUUID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		notifyNameChange();
	}
	
	public void registerNameListener(MapKeyNameListener list) {
		nameUpdates.add(list);
	}
	
	public void deregisterNameListener(MapKeyNameListener list) {
		nameUpdates.remove(list);
	}
	
	private void notifyNameChange() {
		for(MapKeyNameListener list : new ArrayList<>(nameUpdates))
			list.onNameChange();
	}
	
	public String print() {
		return name + ": " + id.toString();
	}
	
	public String toString() {
		return name;
	}
	
	public boolean equals(Object o) {
		if(this == o) 
			return true;
		if(!(o instanceof MapKey))
			return false;
		MapKey other = (MapKey) o;
		return id.equals(other.getUUID());
	}
	
	public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public int compareTo(MapKey o) {
		return 0;
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	    in.defaultReadObject(); // Restore non-transient fields
	    nameUpdates = new ArrayList<>(); // Reinitialize transient fields
	}
}