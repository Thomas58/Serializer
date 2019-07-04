package org.jspace.io.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jspace.io.tools.ClassRegistry;
import org.jspace.io.tools.exceptions.ParseException;
import org.jspace.io.xml.exceptions.XMLSyntaxException;

public class XMLSerializer {
	private StringBuilder builder;
	private int tabs;
	private NumberFormat nf;
	
	public XMLSerializer(){
		this(Locale.getDefault());
	}
	
	public XMLSerializer(Locale locale){
		nf = NumberFormat.getInstance(locale);
	}
	
	/**
	 * 
	 * @param object The object to be serialized into XML.
	 * @return An XML string.
	 * @throws IllegalAccessException Thrown if the security manager prevents access to the given object or some underlying field in the object's structure.
	 */
	public String Serialize(Object object) throws IllegalAccessException {
		builder = new StringBuilder();
		tabs = 1;
		String type = ClassRegistry.get(object.getClass());
		builder.append("<"+ type +">" + System.lineSeparator());
		RecursiveFieldSerialization(object);
		builder.append("</"+ type +">");
		return builder.toString();
	}
	
	public void Serialize(Object object, OutputStream stream) throws IllegalAccessException, IOException {
		String xml = Serialize(object);
		try {
			stream.write(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param object
	 * @throws IllegalAccessException
	 */
	private void RecursiveFieldSerialization(Object object) throws IllegalAccessException {
		// If the object is of a primitive type, write it into the stringbuilder.
		if (ClassRegistry.isPrimitive(object.getClass()) || object.getClass().isEnum()){
			for (int i = 0; i < tabs; i++) builder.append('\t');
			builder.append(WritePrimitive(object) + System.lineSeparator());
		// Else if the object is a collection (an object supporting an array of objects), iterate over them.
		} else if (Collection.class.isInstance(object)){
			for (Object entry : (Collection<?>)object){
				String type = ClassRegistry.get(entry.getClass());
				for (int i = 0; i < tabs; i++) builder.append('\t'); tabs++;
				builder.append("<"+ type +">" + System.lineSeparator());
				RecursiveFieldSerialization(entry); tabs--;
				for (int i = 0; i < tabs; i++) builder.append('\t');
				builder.append("</"+ type +">" + System.lineSeparator());
			}
		// Else iterate over the objects fields and recursively write them down from top to bottom.
		} else {
			// Take the declared fields and fields array and join them together. Creating a field array with all fields from this class and all public fields from superclasses.
			HashSet<Field> set = new HashSet<Field>();
			for (Field entry : object.getClass().getFields())
				set.add(entry);
			for (Field entry : object.getClass().getDeclaredFields())
				set.add(entry);
			Field[] fields = set.toArray(new Field[set.size()]);
			Field.setAccessible(fields, true);
			for (Field field : fields){
				if (Modifier.isTransient(field.getModifiers()))
					continue;
				Object fieldobject = field.get(object);
				if (fieldobject != null){
					for (int i = 0; i < tabs; i++) builder.append('\t'); tabs++;
					builder.append("<"+ field.getName() +">" + System.lineSeparator());
					RecursiveFieldSerialization(fieldobject); tabs--;
					for (int i = 0; i < tabs; i++) builder.append('\t');
					builder.append("</"+ field.getName() +">" + System.lineSeparator());
				}
			}
		}
	}

	private String xml;
	private Pattern mainRegex = Pattern.compile("<\\/?([a-zA-Z0-9_]+)(?:\\s([^>]+))?>");
	private Pattern attributeRegex = Pattern.compile("([a-zA-Z0-9_]+)='([^']+)'");
	private Pattern valueRegex = Pattern.compile("([\\s\\S]*?)<\\/([a-zA-Z0-9_]+)>");
	private Matcher mainMatcher;
	private Matcher attributeMatcher;
	private Matcher valueMatcher;
	
	/**
	 * 
	 * @param xml The XML string to deserialize into a java object.
	 * @return An object
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws XMLSyntaxException
	 * @throws ParseException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public Object Deserialize(String xml) throws IllegalAccessException, InstantiationException, XMLSyntaxException, ParseException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this.xml = xml;
		mainMatcher = mainRegex.matcher(xml);
		valueMatcher = valueRegex.matcher(xml);
		if (!mainMatcher.find())
			throw new XMLSyntaxException("Input does not contain XML formatting.");
		return NewObjectDeserialization();
	}
	
	public Object Deserialize(InputStream stream) throws IllegalAccessException, IOException, XMLSyntaxException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ParseException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = stream.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		buffer.flush();
		this.xml = buffer.toString("UTF-8");
		mainMatcher = mainRegex.matcher(xml);
		valueMatcher = valueRegex.matcher(xml);
		if (!mainMatcher.find())
			throw new XMLSyntaxException("Input does not contain XML formatting.");
		return NewObjectDeserialization();
	}
	
	private Object NewObjectDeserialization() throws IllegalAccessException, InstantiationException, XMLSyntaxException, ParseException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> mainclass = ClassRegistry.reverse(mainMatcher.group(1));
		return RecursiveObjectDeserialization(mainclass);
	}
	
	private Object RecursiveObjectDeserialization(Class<?> objectclass) throws IllegalAccessException, InstantiationException, XMLSyntaxException, ParseException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// The given class is a primitive class.
		if (ClassRegistry.isPrimitive(objectclass) || objectclass.isEnum()) {
			String objectName = mainMatcher.group(1);
			if (valueMatcher.find(mainMatcher.end()) && mainMatcher.find() && mainMatcher.group(1).equals(objectName)) {
				return ParsePrimitive(objectclass, valueMatcher.group(1).trim());
			}
			else
				throw ConstructXMLSyntaxException("Parse error, missing endtag for '"+ mainMatcher.group(1) +"'.", mainMatcher.start());
		// The given class implements the Collection interface.
		} else if (Collection.class.isAssignableFrom(objectclass)){
			String objectName = mainMatcher.group(1);
			@SuppressWarnings("unchecked") // Is actually checked by above if statement.
			Collection<Object> newList = (Collection<Object>) objectclass.newInstance();
			while (mainMatcher.find() && !objectName.equals(mainMatcher.group(1))) {
				Object newObject = NewObjectDeserialization();
				newList.add(newObject);
			}
			return newList;
		// The class is a standard object.
		} else {
			Object object = objectclass.newInstance();
			String objectName = mainMatcher.group(1);
			// Go through each field and deserialize them recursively.
			while (mainMatcher.find() && !objectName.equals(mainMatcher.group(1))) {
				String fieldname = mainMatcher.group(1);
				try {
					Field field = null;
					try {
						field = objectclass.getDeclaredField(fieldname);
					} catch (NoSuchFieldException e) {
						field = objectclass.getField(fieldname);
					}
					if (!field.isAccessible())
						field.setAccessible(true);
					field.set(object, RecursiveObjectDeserialization(field.getType()));
				} catch (NoSuchFieldException e) {
					int tags = 0;
					do {
						if (!mainMatcher.find())
							throw ConstructXMLSyntaxException("Parse error, missing endtag for '"+ fieldname +"'.", mainMatcher.start());
						if (fieldname.equals(mainMatcher.group(1))){
							if (mainMatcher.group().charAt(1) != '/')
								tags++;
							else
								tags--;
						}
					} while(!(fieldname.equals(mainMatcher.group(1)) && tags < 1));
				}
			}
			return object;
		}
	}
	
	private String WritePrimitive(Object obj) {
		Class<?> c = obj.getClass();
		if (c.isEnum()){
			return obj.toString();
		} else {
			switch(ClassRegistry.PrimitiveInstanceOf(c)){
			case SHORT: // Short
				return nf.format(obj);
			case INT: // Integer
				return nf.format(obj);
			case LONG: // Long
				return nf.format(obj);
			case FLOAT: // Float
				return nf.format(obj);
			case DOUBLE: // Double
				return nf.format(obj);
			case BOOLEAN: // Boolean
				return obj.toString();
			case CHAR: // Character
				return obj.toString();
			case STRING: // String
				return obj.toString();
			default:
				throw new IllegalArgumentException("Error!: " + c.toString() +" is not a primitive class or an enum.");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T ParsePrimitive(Class<T> out, String string) throws ParseException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object result = null;
		if (out.isEnum()){
			for (T e : out.getEnumConstants()){
				if (string.equals(e.toString())){
					result = e;
					break;
				}
			}
			if (result == null)
				throw new ParseException("Error: No enum of type "+ out +" matches the string '"+ string +"'.");
		} else {
			switch(ClassRegistry.PrimitiveInstanceOf(out)){
			case SHORT: // Short
				result = Short.parseShort(string);
				break;
			case INT: // Integer
				result = Integer.parseInt(string);
				break;
			case LONG: // Long
				result = Long.parseLong(string);
				break;
			case FLOAT: // Float
				result = Float.parseFloat(string);
				break;
			case DOUBLE: // Double
				result = Double.parseDouble(string);
				break;
			case BOOLEAN: // Boolean
				result = Boolean.parseBoolean(string);
				break;
			case CHAR: // Character
				result = new Character(string.charAt(0));
				break;
			case STRING: // String
				result = new String(string);
				break;
			default:
				throw new IllegalArgumentException("Error!: " + out +" is not a primitive class or an enum.");
			}
		}
		return (T) result;
	}

	private XMLSyntaxException ConstructXMLSyntaxException(String message, int end){
		Matcher matcher = Pattern.compile(System.lineSeparator()).matcher(xml);
		int line = 1;
		int index = 0;
		while (matcher.find() && matcher.start() < end){
			line++;
			index = matcher.start();
		}
		return new XMLSyntaxException(message + " Line "+ line + ", index " + (end - index) + ".");
	}
}
