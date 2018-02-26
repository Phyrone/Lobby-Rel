package de.phyrone.lobbyrel.lib;

import java.util.HashMap;

public class LongMap {

	private static HashMap<Object, Object[]> saves;

	Object[] values;

	public LongMap() {
		values = new Object[999];
		saves = new HashMap<Object, Object[]>();
	}

	public void setValue(Object saveObject, int position, Object value) {
		values = saves.containsKey(saveObject) ? saves.get(saveObject) : new Object[999];
		values[position] = value;
		saves.put(saveObject, values);
	}

	public void setValues(Object saveObject, Object... value) {
		values = value;
		saves.put(saveObject, values);
	}

	public Object[] getValues(Object saveObject) {
		values = saves.containsKey(saveObject) ? saves.get(saveObject) : new Object[999];
		return saves.containsKey(saveObject) ? values : null;
	}

	public Object getValue(Object saveObject, int position) {
		values = saves.containsKey(saveObject) ? saves.get(saveObject) : new Object[999];
		return (!(position > getValues(saveObject).length)) && saves.containsKey(saveObject) ? getValues(saveObject)[position] : null;
	}

}