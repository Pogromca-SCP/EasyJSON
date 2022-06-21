package org.json.easy.dom;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JSONArrayTest
{
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
	}
	
	@Test
	void testArrayOperations()
	{
		final JSONArray arr = new JSONArray();
		assertEquals(false, arr.isIndexValid(0));
		arr.addElement(JSONBooleanValue.TRUE);
		assertEquals(true, arr.isIndexValid(0));
		assertEquals(false, arr.isIndexValid(1));
		assertEquals(true, arr.getBooleanElement(0));
		assertEquals(false, arr.getBooleanElement(1));
		assertEquals(JSONBooleanValue.TRUE, arr.getElement(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(1));
		arr.removeElement(0);
		assertEquals(JSONNullValue.NULL, arr.getElement(0));
		assertEquals(false, arr.isIndexValid(0));
		arr.addNullElement();
		assertEquals(1, arr.size());
		arr.addNullElement();
		assertEquals(2, arr.size());
		assertEquals(JSONNullValue.NULL, arr.getElement(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(1));
		arr.setElement(0, JSONBooleanValue.FALSE);
		assertEquals(2, arr.size());
		assertEquals(JSONBooleanValue.FALSE, arr.getElement(0));
		assertEquals(JSONNullValue.NULL, arr.getElement(1));
	}
	
	@Test
	void testArrayIterator()
	{
		final JSONArray arr = new JSONArray();
		arr.addNullElement();
		arr.addElement(3.14);
		arr.addElement("Test");
		final ArrayList<JSONValue> cp = new ArrayList<JSONValue>();
		arr.forEach(val -> cp.add(val));
		assertEquals(cp, arr.toList());
	}
}
