package org.jspace.io.test.objects;

import java.util.List;

public class SpecialFamily extends Family {
	public boolean hasSuperPowers = true;
	public String power = "Telekinetics";

	public SpecialFamily() {
		super();
	}

	public SpecialFamily(List<Person> members) {
		super(members);
	}

	public SpecialFamily(List<Person> members, List<Pet> pets) {
		super(members, pets);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder("specialfamily");
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
		builder.append(System.lineSeparator() + hasSuperPowers);
		builder.append(System.lineSeparator() + power);
		return builder.toString();
	}

}
