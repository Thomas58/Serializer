package org.jspace.io.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClassRegistry {
	private static TwoWayHashMap<Class<?>, String> dictionary = new TwoWayHashMap<Class<?>, String>();
	private static HashMap<Class<?>, JavaPrimitives> primitives = new HashMap<Class<?>, JavaPrimitives>();
	private static HashMap<String, Class<?>> xmlprimitives = new HashMap<String, Class<?>>();
	
	public static enum JavaPrimitives {
		SHORT("int"), INT("int"), LONG("int"), FLOAT("double"), DOUBLE("double"), BOOLEAN("boolean"), CHAR("char"), STRING("string");
		
		private String xmlFormat;
		
		JavaPrimitives(String xmlFormat){
			this.xmlFormat = xmlFormat;
		}
		
		public String getXML(){
			return xmlFormat;
		}
	}
	
	/**
	 * The static constructor for the ClassDictionary class.
	 * Should add all Java primitives (including String and wrappers) to the primitives map.
	 */
	static {
		// Add Java primitives to the primitives map.
		primitives.put(Short.class, JavaPrimitives.SHORT);
		primitives.put(Integer.class, JavaPrimitives.INT);
		primitives.put(Long.class, JavaPrimitives.LONG);
		primitives.put(Float.class, JavaPrimitives.FLOAT);
		primitives.put(Double.class, JavaPrimitives.DOUBLE);
		primitives.put(Boolean.class, JavaPrimitives.BOOLEAN);
		primitives.put(Character.class, JavaPrimitives.CHAR);
		primitives.put(String.class, JavaPrimitives.STRING);
		primitives.put(short.class, JavaPrimitives.SHORT);
		primitives.put(int.class, JavaPrimitives.INT);
		primitives.put(long.class, JavaPrimitives.LONG);
		primitives.put(float.class, JavaPrimitives.FLOAT);
		primitives.put(double.class, JavaPrimitives.DOUBLE);
		primitives.put(boolean.class, JavaPrimitives.BOOLEAN);
		primitives.put(char.class, JavaPrimitives.CHAR);
		xmlprimitives.put("int", Integer.class);
		xmlprimitives.put("double", Double.class);
		xmlprimitives.put("boolean", Boolean.class);
		xmlprimitives.put("char", Character.class);
		xmlprimitives.put("string", String.class);
	}
	
	public static JavaPrimitives PrimitiveInstanceOf(Class<?> c){
		return primitives.get(c);
	}
	
	protected static Class<?> GetStandard(String xmlFormat){
		return xmlprimitives.get(xmlFormat);
	}
	
	/**
	 * Associates the specified keyword (value) with the specified class (key) in this dictionary.
	 * Will throw an IllegalArgumentException if a primitive class type is entered as a key, this includes all Java primitiv types, their object wrappers and the String class.
	 * Use isPrimitive(key) to check if class is considered a primitive by the dictionary.  
	 * @param key Key with which the specified value is to be associated
	 * @param value Value to be associated with the specified key
	 * @return The previous value associated with key, or null if there was no entry for key.½½½½½½
	 */
	public static synchronized String put(Class<?> key, String value){
		if (ClassRegistry.primitives.containsKey(key) || ClassRegistry.xmlprimitives.containsKey(value))
			throw new IllegalArgumentException("A primitive type cannot be added to the class dictionary.");
		return ClassRegistry.dictionary.put(key, value);
	}
	
	/**
	 * Copies all of the entries from the specified array of class entries to this dictionary (optional operation). The effect of this call is equivalent to that of calling put(k, v) on this dictionary once for each instance of ClassEntry from key k to value v in the specified array of entries.
	 * @param entries The array of entries to be stored in this dictionary
	 */
	public static synchronized void putAll(ClassEntry[] entries){
		for (ClassEntry entry : entries){
			ClassRegistry.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Returns the value to which the specified key is entered.
	 * Throws an IllegalArgumentException if the dictionary does not contain an entry with the specified key.
	 * Use the containsKey(key) method to check for dictionary entries.
	 * @param key The key whose associated value is to be returned
	 * @return The value to which the specified key is entered
	 */
	public static synchronized String get(Class<?> key) {
		if (ClassRegistry.primitives.containsKey(key))
			return ClassRegistry.primitives.get(key).getXML();
		else {
			if (!ClassRegistry.dictionary.containsKey(key))
				throw new IllegalArgumentException("Missing keyword for class: " + key.toString());
			return ClassRegistry.dictionary.get(key);
		}
	}
	
	/**
	 * Returns the key to which the specified value is entered.
	 * Throws an IllegalArgumentException if the dictionary does not contain an entry with the specified value.
	 * Use the containsValue(value) method to check for dictionary entries.
	 * @param value The value whose associated key is to be returned
	 * @return The key to which the specified value is entered
	 */
	public static synchronized Class<?> reverse(String value){
		if (ClassRegistry.xmlprimitives.containsKey(value))
			return ClassRegistry.xmlprimitives.get(value);
		else {
			if (!ClassRegistry.dictionary.containsValue(value))
				throw new IllegalArgumentException("Unknown class: " + value.toString() +". Please add keyword '"+ value.toString() + "' to class dictionary.");
			return ClassRegistry.dictionary.reverse(value);
		}
	}
	
	/**
	 * Returns the key to which the specified value is entered.
	 * Throws an IllegalArgumentException if the dictionary does not contain an entry with the specified value.
	 * Use the containsValue(value) method to check for dictionary entries.
	 * @param value The value whose associated key is to be returned
	 * @return The key to which the specified value is entered
	 * @throws ClassNotFoundException Thrown if there is no key for the given class and the class is not found by the class loader.
	 */
	protected static synchronized Class<?> reverseOrDefault(String value) throws ClassNotFoundException{
		if (ClassRegistry.xmlprimitives.containsKey(value))
			return ClassRegistry.xmlprimitives.get(value);
		else if (ClassRegistry.dictionary.containsValue(value))
			return ClassRegistry.dictionary.reverse(value);
		else
			return Class.forName(value);
	}
	
	/**
	 * Returns the value to which the specified key is entered or the default value if there is no entry with the specified key.
	 * Use the containsKey(key) method to check for dictionary entries.
	 * @param key The key whose associated value is to be returned
	 * @param def The default value to return if no entry is found containing the specified key
	 * @return The value to which the specified key is entered, otherwise the default value is returned
	 */
	public static synchronized String getOrDefault(Class<?> key){
		if (ClassRegistry.primitives.containsKey(key))
			return ClassRegistry.primitives.get(key).getXML();
		else if (ClassRegistry.dictionary.containsKey(key))
			return ClassRegistry.dictionary.get(key);
		else
			return key.getName();
	}

	/**
	 * Copies all of the mappings from the specified map to this dictionary (optional operation). The effect of this call is equivalent to that of calling put(k, v) on this dictionary once for each mapping from key k to value v in the specified map. The behavior of this operation is undefined if the specified map is modified while the operation is in progress.
	 * @param map The array of entries to be stored in this dictionary
	 */
	public static synchronized void putAll(Map<? extends Class<?>, ? extends String> map) {
		for (Class<?> entry : ClassRegistry.primitives.keySet()){
			if (map.containsKey(entry))
				throw new IllegalArgumentException("A primitive type cannot be added to the class dictionary.");
		}
		ClassRegistry.dictionary.putAll(map);
	}

	/**
	 * Removes the entry for the specified key from this dictionary if it is present (optional operation).
	 * Returns the value to which this dictionary previously associated the key, or null if the dictionary contained no entry with key.
	 * 
	 * The dictionary will not contain an entry with the specified key once the call returns.
	 * Primitive types cannot be removed.
	 * @param key Key whose entry is to be removed from the dictionary
	 * @return The previous value associated with key, or null if there was no entry with key.
	 */
	public static synchronized String remove(Class<?> key) {
		return ClassRegistry.dictionary.remove(key);
	}
	
	/**
	 * Returns true if this dictionary considers the specified key to be a primitive type.
	 * @param key Key whose relation to this dictionary is to be tested
	 * @return True if this dictionary considers the specified class to be a primitive class
	 */
	public static synchronized boolean isPrimitive(Class<?> key){
		return ClassRegistry.primitives.containsKey(key);
	}
	
	/**
	 * Returns true if this dictionary considers the specified value to be a primitive type.
	 * @param value Value whose relation to this dictionary is to be tested
	 * @return True if this dictionary considers the specified string to be the keyword of a primitive class
	 */
	public static synchronized boolean isPrimitive(String value){
		return ClassRegistry.xmlprimitives.containsKey(value);
	}

	/**
	 * Removes all of the entries from this dictionary (optional operation).
	 * Primitive types cannot be removed and will remain after the call returns.
	 */
	public static synchronized void clear() {
		ClassRegistry.dictionary.clear();
	}

	/**
	 * Returns true if this dictionary contains an entry for the specified key.
	 * @param key Key whose presence in this dictionary is to be tested
	 * @return True if this dictionary contains an entry with the specified key
	 */
	public static synchronized boolean containsKey(Class<?> key) {
		return ClassRegistry.dictionary.containsKey(key);
	}

	/**
	 * Returns true if this dictionary contains an entry for the specified value.
	 * @param value Value whose presence in this dictionary is to be tested
	 * @return True if this dictionary contains an entry with the specified value
	 */
	public static synchronized boolean containsValue(String value) {
		return ClassRegistry.dictionary.containsValue(value);
	}

	/**
	 * Returns a Set view of the entries contained in this dictionary. The set is backed by the dictionary, so changes to the dictionary are reflected in the set, and vice-versa. If the dictionary is modified while an iteration over the set is in progress (except through the iterator's own remove operation, or through the setValue operation on a dictionary entry returned by the iterator) the results of the iteration are undefined. The set supports element removal, which removes the corresponding entry from the dictionary, via the Iterator.remove, Set.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations.
	 * @return A set view of the entries contained in this dictionary
	 */
	public static synchronized Set<Map.Entry<Class<?>, String>> entrySet() {
		return ClassRegistry.dictionary.entrySet();
	}

	/**
	 * Returns true if this dictionary contains no key-value entries.
	 * @return True if this dictionary contains no key-value entries
	 */
	public static synchronized boolean isEmpty() {
		return ClassRegistry.dictionary.isEmpty();
	}

	/**
	 * Returns a Set view of the keys contained in this dictionary. The set is backed by the dictionary, so changes to the dictionary are reflected in the set, and vice-versa. If the dictionary is modified while an iteration over the set is in progress (except through the iterator's own remove operation, or through the setValue operation on a dictionary entry returned by the iterator) the results of the iteration are undefined. The set supports element removal, which removes the corresponding entry from the dictionary, via the Iterator.remove, Set.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations.
	 * @return A set view of the keys contained in this dictionary
	 */
	public static synchronized Set<Class<?>> keySet() {
		return ClassRegistry.dictionary.keySet();
	}

	/**
	 * Returns the number of key-value entries in this dictionary. If the dictionary contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
	 * @return The number of key-value entries in this dictionary
	 */
	public static synchronized int size() {
		return ClassRegistry.dictionary.size();
	}

	/**
	 * Returns a Collection view of the values contained in this dictionary. The collection is backed by the dictionary, so changes to the dictionary are reflected in the collection, and vice-versa. If the dictionary is modified while an iteration over the collection is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The collection supports element removal, which removes the corresponding entry from the dictionary, via the Iterator.remove, Collection.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations.
	 * @return
	 */
	public static synchronized Collection<String> values() {
		return ClassRegistry.dictionary.values();
	}
}
