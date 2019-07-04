package org.jspace.io.test.objects;

public class Person {
	public String name;
	public int age;
	public Pet favoritePet;
	
	public Person(){}
	public Person(String name, int age){
		this.name = name;
		this.age = age;
	}
	
	public Person(String name, int age, Pet favoritePet){
		this.name = name;
		this.age = age;
		this.favoritePet = favoritePet;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder("person");
		builder.append(System.lineSeparator() + name);
		builder.append(System.lineSeparator() + age);
		builder.append(System.lineSeparator() + favoritePet);
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj.getClass().equals(this.getClass())) {
			Person object = (Person) obj;
			return
					object.name.equals(this.name) &&
					object.age == this.age &&
					((object.favoritePet != null && this.favoritePet != null && object.favoritePet.equals(this.favoritePet)) ||
					(object.favoritePet == null && this.favoritePet == null));
		} else
			return false;
	}
}
