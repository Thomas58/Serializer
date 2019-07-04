package org.jspace.io.test.binary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.jspace.io.test.objects.Family;
import org.jspace.io.test.objects.Numbers;
import org.jspace.io.test.objects.Person;
import org.jspace.io.test.objects.Pet;
import org.jspace.io.test.objects.SpecialFamily;
import org.jspace.io.tools.ClassRegistry;
import org.jspace.io.xml.XMLSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

public class FileTests {
//	public String directoryPath = Paths.get("").toAbsolutePath().toString() + "/";
	public String directoryPath = "C:\\Users\\Tengux\\Documents\\Visual Studio 2017\\Projects\\XMLSerializer\\XMLSerializerTests\\";
	public String filename = "WorkingTest.file";
	public boolean readOnlyTest = true;
	public XMLSerializer serial;
	
	@Before
	public void setup(){
		serial = new XMLSerializer(Locale.US);
	}
	
	@Test
	public void testPrivateField() throws Exception {
		ArrayList<Person> members = new ArrayList<Person>();
		members.add(new Person("Miranda", 56));
		members.add(new Person("Greham", 54));
		members.add(new Person("Maria", 16));
		Family family = new Family(members);
		family.addSecret("Our little secret." + System.lineSeparator() + "In a file.");
		
		ClassRegistry.put(Family.class, "family");
		ClassRegistry.put(Person.class, "person");
		String filename = "TestPrivateField.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(family, output);
		}
		Family newFamily = new Family();
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newFamily = (Family) serial.Deserialize(input);
		}
		Assert.assertEquals(family, newFamily);
		Assert.assertEquals(family.getSecret(), newFamily.getSecret());
	}
	
	@Test
	public void testSuperClass() throws Exception {
		ArrayList<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Snowball", Pet.Type.Cat));
		pets.add(new Pet("Mickey", Pet.Type.Dog));
		ArrayList<Person> members = new ArrayList<Person>();
		members.add(new Person("Miranda", 56, pets.get(0)));
		members.add(new Person("Greham", 54, pets.get(1)));
		members.add(new Person("Maria", 16, pets.get(0)));
		SpecialFamily family = new SpecialFamily(members, pets);
		
		ClassRegistry.put(SpecialFamily.class, "specialfamily");
		ClassRegistry.put(Person.class, "person");
		ClassRegistry.put(Pet.class, "pet");
		String filename = "TestSuperClass.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(family, output);
		}
		SpecialFamily newFamily = new SpecialFamily();
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newFamily = (SpecialFamily)serial.Deserialize(input);
		}
		Assert.assertEquals(family, newFamily);
	}
	
	@Test
	public void testAdvanced() throws Exception {
		ArrayList<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Snowball", Pet.Type.Cat));
		pets.add(new Pet("Mickey", Pet.Type.Dog));
		ArrayList<Person> members = new ArrayList<Person>();
		members.add(new Person("Miranda", 56, pets.get(0)));
		members.add(new Person("Greham", 54, pets.get(1)));
		members.add(new Person("Maria", 16, pets.get(0)));
		Family family = new Family(members, pets);
		
		ClassRegistry.put(Family.class, "family");
		ClassRegistry.put(Person.class, "person");
		ClassRegistry.put(Pet.class, "pet");
		String filename = "TestAdvanced.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(family, output);
		}
		Family newFamily = new Family();
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newFamily = (Family)serial.Deserialize(input);
		}
		Assert.assertEquals(family, newFamily);
	}

	@Test
	public void testAdvancedList() throws Exception {
		ArrayList<Person> members = new ArrayList<Person>();
		members.add(new Person("Miranda", 56));
		members.add(new Person("Greham", 54));
		members.add(new Person("Maria", 16));
		Family family = new Family(members);
		
		ClassRegistry.put(Family.class, "family");
		ClassRegistry.put(Person.class, "person");
		String filename = "TestAdvancedList.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(family, output);
		}
		Family newFamily = new Family();
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newFamily = (Family)serial.Deserialize(input);
		}
		Assert.assertEquals(family, newFamily);
	}

	@Test
	public void testSimpleList() throws Exception {
		Numbers numbers = new Numbers();
		ArrayList<Integer> list = numbers.list;
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		
		ClassRegistry.put(numbers.getClass(), "numbers");
		String filename = "TestSimpleList.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(numbers, output);
		}
		Numbers newNumbers = new Numbers();
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newNumbers = (Numbers)serial.Deserialize(input);
		}
		Assert.assertEquals(numbers, newNumbers);
	}

	@Test
	public void testSimple() throws Exception {
		Person maria = new Person("Maria", 16);
		
		ClassRegistry.put(Person.class, "person");
		String filename = "TestSimple.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(maria, output);
		}
		Person newMaria = new Person();
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newMaria = (Person) serial.Deserialize(input);
		}
		Assert.assertEquals(maria, newMaria);
	}
	
	@Test
	public void testFilePrimitives() throws Exception {
		short sh = 21;
		int in = 22;
		long lo = 23;
		float fl = 24.5f;
		double dou = 25.5;
		boolean bo = true;
		char ch = 'c';
		String str = "string";
		
		String filename = "TestPrimitiveShort.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(sh, output);
		}
		short newsh = 0;
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newsh = ((Integer) serial.Deserialize(input)).shortValue();
		}
		Assert.assertEquals(sh, newsh);

		filename = "TestPrimitiveInt.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(in, output);
		}
		int newin = 0;
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newin = (int) serial.Deserialize(input);
		}
		Assert.assertEquals(in, newin);

		filename = "TestPrimitiveLong.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(lo, output);
		}
		long newlo = 0;
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newlo = ((Integer) serial.Deserialize(input)).longValue();
		}
		Assert.assertEquals(lo, newlo);

		filename = "TestPrimitiveFloat.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(fl, output);
		}
		float newfl = 0.0f;
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newfl = ((Double) serial.Deserialize(input)).floatValue();
		}
		Assert.assertEquals(fl, newfl, 0.01);

		filename = "TestPrimitiveDouble.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(dou, output);
		}
		double newdou = 0.0;
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newdou = (double) serial.Deserialize(input);
		}
		Assert.assertEquals(dou, newdou, 0.01);

		filename = "TestPrimitiveBoolean.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(bo, output);
		}
		boolean newbo = false;
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newbo = (Boolean) serial.Deserialize(input);
		}
		Assert.assertEquals(bo, newbo);

		filename = "TestPrimitiveChar.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(ch, output);
		}
		char newch = 'a';
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newch = (Character) serial.Deserialize(input);
		}
		Assert.assertEquals(ch, newch);

		filename = "TestPrimitiveString.file";
		if (!readOnlyTest)
		try (FileOutputStream output = new FileOutputStream(directoryPath + filename)){
			serial.Serialize(str, output);
		}
		String newstr = "";
		try (FileInputStream input = new FileInputStream(directoryPath + filename)){
			newstr = (String) serial.Deserialize(input);
		}
		Assert.assertEquals(str, newstr);
	}
}
