package org.jspace.io.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TwoWayHashMap<K extends Object, V extends Object> implements Map<K, V> {
	private HashMap<K, V> dictionary = new HashMap<K, V>();
	private HashMap<V, K> xDictionary = new HashMap<V, K>();
	
	
	@Override
	public V get(Object key) {
		return dictionary.get(key);
	}
	
	public K reverse(Object value){
		return xDictionary.get(value);
	}

	@Override
	public V put(K key, V value) {
		xDictionary.put(value, key);
		return dictionary.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		dictionary.putAll(m);
		for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet()){
			xDictionary.put(entry.getValue(), entry.getKey());
		}
	}

	@Override
	public V remove(Object key) {
		V value = dictionary.remove(key);
		xDictionary.remove(value);
		return value;
	}
	
	@Override
	public void clear() {
		dictionary.clear();
		xDictionary.clear();
	}
	
	@Override
	public boolean containsKey(Object key) {
		return dictionary.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return xDictionary.containsKey(value);
	}

	@Override
	public int size() {
		return dictionary.size();
	}

	@Override
	public boolean isEmpty() {
		return dictionary.isEmpty();
	}
	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return dictionary.entrySet();
	}

	@Override
	public Set<K> keySet() {
		return dictionary.keySet();
	}

	@Override
	public Collection<V> values() {
		return dictionary.values();
	}

}
