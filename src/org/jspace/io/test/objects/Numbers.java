package org.jspace.io.test.objects;

import java.util.ArrayList;

public class Numbers {
	public ArrayList<Integer> list = new ArrayList<Integer>();
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder("numbers");
		for (Integer i : list)
			builder.append(System.lineSeparator() + i);
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj.getClass().equals(this.getClass())) {
			Numbers object = (Numbers) obj;
			return object.list.equals(this.list);
		} else
			return false;
	}
}
