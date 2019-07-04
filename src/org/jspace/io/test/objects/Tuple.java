package org.jspace.io.test.objects;

public class Tuple {
	public enum Type {
		Player, Game
	}
	private Object data;
	private Type type;
	
	public Tuple(){}
	public Tuple(Object data, Type type) {
		this.data = data;
		this.type = type;
	}

	public Object getData(){
		return data;
	}
	
	public Type getType(){
		return type;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj.getClass().equals(this.getClass())) {
			Tuple object = (Tuple) obj;
			return
					object.type.equals(this.type) &&
					((this.data != null && this.data.equals(object.data)));
		} else
			return false;
	}
}
