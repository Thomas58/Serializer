package org.jspace.io.tools;

import java.util.Map;

public class ClassEntry implements Map.Entry<Class<?>, String> {
	private Class<?> key;
	private String value;
	
	public ClassEntry(Class<?> key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public Class<?> getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String setValue(String value) {
		String oldValue = this.value;
		this.value = value;
		return oldValue;
	}

}
