package org.jspace.io.test.objects;

import java.util.ArrayList;
import java.util.List;

public class Family {
	public int count;
	public ArrayList<Person> members;
	public ArrayList<Pet> pets;
	private String familysecret;
	
	public Family(){}
	public Family(List<Person> members){
		this.members = new ArrayList<Person>(members);
		this.count = members.size();
	}
	
	public Family(List<Person> members, List<Pet> pets){
		this.members = new ArrayList<Person>(members);
		this.count = members.size();
		this.pets = new ArrayList<Pet>(pets);
	}
	
	public void addSecret(String secret){
		this.familysecret = secret;
	}
	
	public String getSecret(){
		return familysecret;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder("family");
		builder.append(System.lineSeparator() + count);
		if (members != null)
			for (Person person : members)
				builder.append(System.lineSeparator() + person);
		else
			builder.append(System.lineSeparator() + "null");
		if (pets != null)
			for (Pet pet : pets)
				builder.append(System.lineSeparator() + pet);
		else
			builder.append(System.lineSeparator() + "null");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj.getClass().equals(this.getClass())) {
			Family object = (Family) obj;
			return
					object.count == this.count &&
					((object.members != null && this.members != null && object.members.equals(this.members)) ||
					(object.members == null && this.members == null)) &&
					((object.pets != null && this.pets != null && object.pets.equals(this.pets)) ||
					(object.pets == null && this.pets == null));
		} else
			return false;
	}
}
