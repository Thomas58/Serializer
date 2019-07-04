package org.jspace.io.test.objects;

public class Pet {
	public enum Type {
		Cat, Dog
	}
	public String name;
	public Type type;
	
	public Pet(){}
	public Pet(String name, Type type){
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder("pet");
		builder.append(System.lineSeparator() + name);
		builder.append(System.lineSeparator() + type);
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj.getClass().equals(this.getClass())) {
			Pet object = (Pet) obj;
			return object.name.equals(this.name) && object.type.equals(this.type);
		} else
			return false;
	}
}
