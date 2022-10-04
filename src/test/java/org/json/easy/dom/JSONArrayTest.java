package org.json.easy.dom;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.json.easy.serialization.JSONType;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JSONArrayTest
{
	private static void serializationTest(final JSONArray arr)
	{	
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("SerializationTest")))
		{
			stream.writeObject(arr);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		Object res;
		
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("SerializationTest")))
		{
			res = stream.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		assertEquals(arr, res);
	}
	
	@Test
	void testArrayConstructors()
	{
		final ArrayList<JSONValue> list = new ArrayList<JSONValue>();
		final ArrayList<JSONValue> empty = new ArrayList<JSONValue>();
		list.add(JSONBooleanValue.TRUE);
		list.add(JSONNullValue.NULL);
		list.add(new JSONStringValue("testing"));
		
		final JSONArray arr = new JSONArray(list);
		final JSONArray emptyArr = JSONArray.EMPTY;
		assertEquals(list.hashCode(), arr.hashCode());
		assertEquals(empty.hashCode(), emptyArr.hashCode());
		assertEquals(list.toString(), arr.toString());
		assertEquals(empty.toString(), emptyArr.toString());
		assertEquals(list.size(), arr.size());
		assertEquals(empty.size(), emptyArr.size());
		
		final JSONArray copy = new JSONArray(arr);
		assertEquals(copy.equals(arr), arr.equals(copy));
		assertEquals(arr.size(), copy.size());
		assertEquals(arr.hashCode(), copy.hashCode());
		assertEquals(arr.toString(), copy.toString());
		assertEquals(list, copy.toList());
		final JSONValue[] ar = { JSONBooleanValue.TRUE, JSONNullValue.NULL, new JSONStringValue("testing") };
		assertArrayEquals(ar, copy.toArray());
	}
	
	@Test
	void testEmptyArray()
	{
		final JSONArray arr = JSONArray.EMPTY;
		assertEquals(0, arr.size());
		arr.addElement("test");
		arr.addElement(true);
		arr.addNullElement();
		assertEquals(0, arr.size());
		assertEquals(true, arr.isEmpty());
		serializationTest(arr);
	}
	
	@Test
	void testArrayOperations()
	{
		final JSONArray arr = new JSONArray();
		assertEquals(false, arr.isIndexValid(0));
		assertEquals(false, arr.isIndexValid(null));
		assertEquals(true, arr.isEmpty());
		arr.addElement(JSONBooleanValue.TRUE);
		
		assertEquals(true, arr.isIndexValid(0));
		assertEquals(true, arr.isIndexValid(null));
		assertEquals(true, arr.contains(JSONBooleanValue.TRUE));
		assertEquals(false, arr.contains(JSONBooleanValue.FALSE));
		assertEquals(false, arr.isIndexValid(1));
		assertEquals(false, arr.isIndexValid(Integer.valueOf(1)));
		assertEquals(true, arr.getBooleanElement(0));
		assertEquals(true, arr.getBooleanElement(Integer.valueOf(0)));
		assertEquals(false, arr.getBooleanElement(1));
		assertEquals(false, arr.getBooleanElement(Integer.valueOf(1)));
		assertEquals(true, arr.contains(true));
		assertEquals(false, arr.contains(false));
		assertEquals(JSONBooleanValue.TRUE, arr.getElement(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(1));
		assertEquals(false, arr.isEmpty());
		
		arr.setNullElement(0);
		arr.setNullElement(null);
		assertEquals(JSONNullValue.NULL, arr.getElement(0));
		arr.removeElement(0);
		arr.addNullElement();
		arr.removeElement(Integer.valueOf(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(0));
		assertEquals(false, arr.contains(JSONNullValue.NULL));
		assertEquals(false, arr.isIndexValid(0));
		
		arr.addNullElement();
		assertEquals(1, arr.size());
		arr.addElement((JSONValue) null);
		assertEquals(2, arr.size());
		assertEquals(true, arr.contains(JSONNullValue.NULL));
		assertEquals(JSONNullValue.NULL, arr.getElement(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(1));
		
		arr.setElement(0, JSONBooleanValue.TRUE);
		arr.setElement(null, JSONBooleanValue.FALSE);
		assertEquals(2, arr.size());
		assertEquals(JSONBooleanValue.FALSE, arr.getElement(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(1));
		assertEquals(JSONBooleanValue.FALSE, arr.getElement(Integer.valueOf(0)));
		assertEquals(JSONNullValue.NULL, arr.getElement(Integer.valueOf(1)));
		assertEquals(true, arr.containsNull());
		assertEquals(true, arr.contains(false));
		assertEquals(-1, arr.indexOf(true));
		assertEquals(0, arr.indexOf(false));
		assertEquals(-1, arr.lastIndexOf(true));
		assertEquals(0, arr.lastIndexOf(false));
		
		assertEquals(1, arr.removeAllNullValues());
		assertEquals(1, arr.size());
		
		serializationTest(arr);
		arr.clear();
		assertEquals(JSONArray.EMPTY, arr);
	}
	
	@Test
	void testArrayIterator()
	{
		final JSONArray arr = new JSONArray();
		arr.addNullElement();
		arr.addElement(3.14);
		arr.addElement("Test");
		assertEquals(true, arr.contains(new JSONNumberValue(3.14)));
		assertEquals(false, arr.contains(new JSONNumberValue(5.25)));
		
		final ArrayList<JSONValue> cp = new ArrayList<JSONValue>();
		arr.forEach(val -> cp.add(val));
		assertEquals(cp, arr.toList());
	}
	
	@Test
	void testArrayStream()
	{
		final JSONArray arr = new JSONArray();
		arr.addNullElement();
		arr.addElement(3.14);
		arr.addElement("Test");
		assertEquals(1, arr.stream().filter(val -> val.getType() == JSONType.NUMBER).count());
	}
	
	@Test
	void testUtilMethods()
	{
		final JSONArray arr = new JSONArray();
		arr.addElement(24);
		arr.addElement(78);
		arr.addElement("test");
		arr.addElement(90);
		arr.addElement("example");
		arr.addElement("bruh");
		arr.addElement(21);
		arr.addElement(-34);
		assertEquals(8, arr.size());
		
		arr.removeIf(val -> val.getType() != JSONType.NUMBER);
		assertEquals(5, arr.size());
		arr.sort((v1, v2) -> (int) (v1.asNumber() - v2.asNumber()));
		final JSONValue[] exp = { new JSONNumberValue(-34), new JSONNumberValue(21), new JSONNumberValue(24), new JSONNumberValue(78), new JSONNumberValue(90) };
		assertArrayEquals(exp, arr.toArray());
	}
	
	@Test
	void testSubArrays()
	{
		final JSONArray arr = new JSONArray();
		arr.addElement(false);
		arr.addElement("second");
		arr.addElement(2.1);
		arr.addElement(false);
		arr.addNullElement();
		arr.addElement(1.1);
		arr.addElement(true);
		arr.addNullElement();
		arr.addElement("almost there");
		arr.addElement("last");
		
		assertEquals(arr.subArray(0, 4), arr.subArray(Integer.valueOf(0), 4));
		assertEquals(arr.subArray(0, 4), arr.subArray(0, Integer.valueOf(4)));
		assertEquals(arr.subArray(0, 4), arr.subArray(Integer.valueOf(0), Integer.valueOf(4)));
		assertEquals(JSONArray.EMPTY, arr.subArray(-2, 4));
		assertEquals(JSONArray.EMPTY, arr.subArray(1, 1));
		assertEquals(JSONArray.EMPTY, arr.subArray(0, arr.size() + 1));
		
		final JSONArray sub = arr.subArray(0, 4);
		assertEquals(true, sub.contains(false));
		assertEquals(true, sub.contains(2.1));
		assertEquals(false, sub.containsNull());
		assertEquals(false, sub.contains(1.1));
		
		final JSONArray sub2 = arr.subArray(6, arr.size());
		assertEquals(true, sub2.contains(true));
		assertEquals(true, sub2.containsNull());
		assertEquals(true, sub2.contains("last"));
		assertEquals(false, sub2.contains(false));
		assertEquals(false, sub2.contains(1.1));
	}
}
