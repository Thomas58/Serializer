package org.jspace.io.test.xml;

import java.util.ArrayList;

import org.jspace.io.test.objects.Family;
import org.jspace.io.test.objects.Numbers;
import org.jspace.io.test.objects.Person;
import org.jspace.io.test.objects.Pet;
import org.jspace.io.test.objects.SpecialFamily;
import org.jspace.io.test.objects.Tuple;
import org.jspace.io.test.objects.Pet.Type;
import org.jspace.io.tools.ClassRegistry;
import org.jspace.io.xml.XMLSerializer;
import org.junit.*;
import org.junit.Test;

public class StandardTests {
	public XMLSerializer serial;
	
	@Before
	public void setup(){
		serial = new XMLSerializer();
	}
	
	@Test
	public void TestUndefinedTuple() throws Exception {
		Numbers numbers = new Numbers();
		ArrayList<Integer> list = numbers.list;
		list.add(Integer.MIN_VALUE);
		list.add(Integer.MAX_VALUE);
		list.add(Integer.MAX_VALUE / 2);
		list.add(Integer.MAX_VALUE / 4);
		list.add(Integer.MIN_VALUE / 2);
		Tuple tuple = new Tuple(numbers, Tuple.Type.Game);
		
		ClassRegistry.put(tuple.getClass(), "tuple");
		ClassRegistry.put(numbers.getClass(), "numbers");
		String xml = serial.Serialize(tuple);
		Tuple newTuple = (Tuple)serial.Deserialize(xml);
		Assert.assertEquals(tuple, newTuple);
	}
	
	@Test
	public void testPrivateField() throws Exception {
		ArrayList<Person> members = new ArrayList<Person>();
		members.add(new Person("Miranda", 56));
		members.add(new Person("Greham", 54));
		members.add(new Person("Maria", 16));
		Family family = new Family(members);
		family.addSecret("Our little secret.");
		
		ClassRegistry.put(Family.class, "family");
		ClassRegistry.put(Person.class, "person");
		String xml = serial.Serialize(family);
		Family newFamily = (Family)serial.Deserialize(xml);
		Assert.assertEquals(family, newFamily);
		Assert.assertEquals(family.getSecret(), newFamily.getSecret());
	}
	
	@Test
	public void testSuperClasses() throws Exception {
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
		String xml = serial.Serialize(family);
		SpecialFamily newFamily = (SpecialFamily)serial.Deserialize(xml);
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
		String xml = serial.Serialize(family);
		Family newFamily = (Family)serial.Deserialize(xml);
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
		String xml = serial.Serialize(family);
		Family newFamily = (Family)serial.Deserialize(xml);
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
		String xml = serial.Serialize(numbers);
		Numbers newNumbers = (Numbers)serial.Deserialize(xml);
		Assert.assertEquals(numbers, newNumbers);
	}

	@Test
	public void testSimple() throws Exception {
		Person maria = new Person("Maria", 16);
		
		ClassRegistry.put(Person.class, "person");
		String xml = serial.Serialize(maria);
		Person newMaria = (Person) serial.Deserialize(xml);
		Assert.assertEquals(maria, newMaria);
	}

	@Test
	public void testPrimitives() throws Exception {
		short sh = 21;
		int in = 22;
		long lo = 23;
		float fl = 24.5f;
		double dou = 25.5;
		boolean bo = true;
		char ch = 'c';
		String str = "string";
		
		String shs = serial.Serialize(sh);
		String ins = serial.Serialize(in);
		String los = serial.Serialize(lo);
		String fls = serial.Serialize(fl);
		String dous = serial.Serialize(dou);
		String bos = serial.Serialize(bo);
		String chs = serial.Serialize(ch);
		String strs = serial.Serialize(str);
		
		short newsh = ((Integer) serial.Deserialize(shs)).shortValue();
		int newin = (int) serial.Deserialize(ins);
		long newlo = ((Integer) serial.Deserialize(los)).longValue();
		float newfl = ((Double) serial.Deserialize(fls)).floatValue();
		double newdou = (double) serial.Deserialize(dous);
		boolean newbo = (Boolean) serial.Deserialize(bos);
		char newch = (Character) serial.Deserialize(chs);
		String newstr = (String) serial.Deserialize(strs);
		
		Assert.assertEquals(sh, newsh);
		Assert.assertEquals(in, newin);
		Assert.assertEquals(lo, newlo);
		Assert.assertEquals(fl, newfl, 0.01);
		Assert.assertEquals(dou, newdou, 0.01);
		Assert.assertEquals(bo, newbo);
		Assert.assertEquals(ch, newch);
		Assert.assertEquals(str, newstr);
		// Remember to check for special cases, specially string need to be done differently.
	}
	
	@Test
	public void testWrappers() throws Exception {
		Person p1 = new Person("Maria", 16);
		Person p2 = new Person("Maria", 16);
		Person p3 = new Person("Nanna", 16);
		Person p4 = new Person("Maria", 17);
		Person p5 = new Person("Nanna", 17);
		
		Assert.assertEquals(p1, p2);
		Assert.assertNotEquals(p1, p3);
		Assert.assertNotEquals(p1, p4);
		Assert.assertNotEquals(p1, p5);
		
		Pet pe1 = new Pet("Snowball", Type.Cat);
		Pet pe2 = new Pet("Snowball", Type.Cat);
		Pet pe3 = new Pet("Snowball", Type.Dog);
		Pet pe4 = new Pet("Mickey", Type.Cat);
		Pet pe5 = new Pet("Mickey", Type.Dog);
		
		Assert.assertEquals(pe1, pe2);
		Assert.assertNotEquals(pe1, pe3);
		Assert.assertNotEquals(pe1, pe4);
		Assert.assertNotEquals(pe1, pe5);
		
		ArrayList<Person> m1 = new ArrayList<Person>();
		m1.add(p1); m1.add(p2); m1.add(p3); m1.add(p4); m1.add(p5);
		Family f1 = new Family(m1);
		ArrayList<Person> m2 = new ArrayList<Person>();
		m2.add(p1); m2.add(p2); m2.add(p3); m2.add(p4); m2.add(p5);
		Family f2 = new Family(m2);
		ArrayList<Person> m3 = new ArrayList<Person>();
		m3.add(p1); m3.add(p2); m3.add(p3); m3.add(p4);
		Family f3 = new Family(m3);
		ArrayList<Person> m4 = new ArrayList<Person>();
		m4.add(p1); m4.add(p2); m4.add(p3); m4.add(p4); m4.add(p4);
		Family f4 = new Family(m4);
		
		Assert.assertEquals(f1, f2);
		Assert.assertNotEquals(f1, f3);
		Assert.assertNotEquals(f1, f4);
		
		ArrayList<Pet> pet1 = new ArrayList<Pet>();
		pet1.add(pe1); pet1.add(pe2); pet1.add(pe3); pet1.add(pe4); pet1.add(pe5);
		Family f5 = new Family(m1, pet1);
		ArrayList<Pet> pet2 = new ArrayList<Pet>();
		pet2.add(pe1); pet2.add(pe2); pet2.add(pe3); pet2.add(pe4); pet2.add(pe5);
		Family f6 = new Family(m1, pet2);
		ArrayList<Pet> pet3 = new ArrayList<Pet>();
		pet3.add(pe1); pet3.add(pe2); pet3.add(pe3); pet3.add(pe4);
		Family f7 = new Family(m1, pet3);
		ArrayList<Pet> pet4 = new ArrayList<Pet>();
		pet4.add(pe1); pet4.add(pe2); pet4.add(pe3); pet4.add(pe4); pet4.add(pe4);
		Family f8 = new Family(m1, pet4);
		
		Assert.assertEquals(f5, f6);
		Assert.assertNotEquals(f5, f7);
		Assert.assertNotEquals(f5, f8);
	}

}
