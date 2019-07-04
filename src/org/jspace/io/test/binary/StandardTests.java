package org.jspace.io.test.binary;

import java.io.IOException;
import java.util.ArrayList;

import org.jspace.io.binary.BinarySerializer;
import org.jspace.io.binary.Configurations;
import org.jspace.io.binary.TypeConverter;
import org.jspace.io.test.objects.Family;
import org.jspace.io.test.objects.Numbers;
import org.jspace.io.test.objects.Person;
import org.jspace.io.test.objects.Pet;
import org.jspace.io.test.objects.SpecialFamily;
import org.jspace.io.test.objects.Tuple;
import org.jspace.io.test.objects.Pet.Type;
import org.jspace.io.tools.ClassRegistry;
import org.jspace.io.tools.exceptions.ParseException;
import org.jspace.io.xml.XMLSerializer;
import org.junit.*;
import org.junit.Test;

public class StandardTests {
	public BinarySerializer serial;
	
	@Before
	public void setup(){
		serial = new BinarySerializer();
	}
	
	private void testEquality(Object obj) throws IllegalArgumentException, IllegalAccessException, IOException, ParseException, InstantiationException{
		Class<?> c = obj.getClass();
		byte[] bytes = serial.serialize(obj);
		Object newObj = (Object)serial.deserialize(c, bytes);
		Assert.assertEquals(obj, newObj);
	}
	
	@Test
	public void TestBinaryArrayMultiple() throws IllegalArgumentException, IllegalAccessException, InstantiationException, IOException, ParseException
    {
        double[][] obj = new double[][]
        {
            { 0.1,
            1.5453,
            2.8732 },
            { 3.333333,
            4.45679082,
            5.542 },
            { 6.121321,
            7.0,
            8.01212 },
            { 9.999078,
            10.101010,
            11.0 }
        };
        byte[] bytes = serial.serialize(obj);
		double[][] newObj = (double[][])serial.deserialize(obj.getClass(), bytes);
		Assert.assertTrue(obj.length == newObj.length);
		for (int i = 0; i < obj.length; i++){
			Assert.assertArrayEquals(obj[i], newObj[i], 0);
		}
    }
	
	@Test
	public void TestBinaryArray() throws IllegalArgumentException, IllegalAccessException, InstantiationException, IOException, ParseException
    {
        double[] obj = new double[]
        {
            0.1,
            1.5453,
            2.8732,
            3.333333,
            4.45679082,
            5.542,
            6.121321,
            7.0,
            8.01212,
            9.999078,
            10.101010
        };
		byte[] bytes = serial.serialize(obj);
		double[] newObj = (double[])serial.deserialize(obj.getClass(), bytes);
		Assert.assertArrayEquals(obj, newObj, 0);
    }
	
	@Test
	public void testUndefinedTuple() throws Exception {
		Numbers numbers = new Numbers();
		ArrayList<Integer> list = numbers.list;
		list.add(Integer.MIN_VALUE);
		list.add(Integer.MAX_VALUE);
		list.add(Integer.MAX_VALUE / 2);
		list.add(Integer.MAX_VALUE / 4);
		list.add(Integer.MIN_VALUE / 2);
		Tuple tuple = new Tuple(numbers, Tuple.Type.Game);
		
		byte[] xml = serial.serialize(tuple);
		Tuple newTuple = (Tuple)serial.deserialize(Tuple.class, xml);
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
		
		testEquality(family);
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
		
		testEquality(family);
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
		
		testEquality(family);
	}

	@Test
	public void testAdvancedList() throws Exception {
		ArrayList<Person> members = new ArrayList<Person>();
		members.add(new Person("Miranda", 56));
		members.add(new Person("Greham", 54));
		members.add(new Person("Maria", 16));
		Family family = new Family(members);
		
		testEquality(family);
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
		
		testEquality(numbers);
	}

	@Test
	public void testSimple() throws Exception {
		Person maria = new Person("Maria", 16);
		
		testEquality(maria);
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
		
		testEquality(sh);
		testEquality(in);
		testEquality(lo);
		testEquality(fl);
		testEquality(dou);
		testEquality(bo);
		testEquality(ch);
		byte[] bytes = TypeConverter.getBytes(str, Configurations.CharEncodings.UTF16);
		String nstr = TypeConverter.toString(bytes, Configurations.CharEncodings.UTF16);
		Assert.assertEquals(str, nstr);
		testEquality(str);
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
