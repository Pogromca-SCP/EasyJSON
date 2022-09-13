package org.json.easy.dom;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		serializationTest(arr);
	}
	
	@Test
	void testArrayOperations()
	{
		final JSONArray arr = new JSONArray();
		assertEquals(false, arr.isIndexValid(0));
		assertEquals(false, arr.isIndexValid(null));
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
		assertEquals(JSONBooleanValue.TRUE, arr.getElement(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(1));
		
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
}
