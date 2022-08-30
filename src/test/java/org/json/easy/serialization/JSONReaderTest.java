package org.json.easy.serialization;

import org.json.easy.dom.*;
import java.io.FileReader;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONReaderTest
{
	private static void testTemplate(final String file, final String name, final JSONArray expectedArray, final JSONObject expectedObj)
	{
		String err = null;
		JSONValue val = null;
		JSONArray arr = null;
		JSONObject obj = null;
		
		try (JSONReader reader = new JSONReader(new FileReader(file)))
		{
			val = JSONSerializer.deserialize(reader);
			err = reader.getErrorMessage();
		}
		catch (FileNotFoundException f)
		{
			f.printStackTrace();
		}
		
		if (err != null && !err.isEmpty())
		{
			System.out.println(name + ": " + err);
		}
		
		try (JSONReader reader = new JSONReader(new FileReader(file)))
		{
			arr = JSONSerializer.deserializeArray(reader);
		}
		catch (FileNotFoundException f)
		{
			f.printStackTrace();
		}
		
		try (JSONReader reader = new JSONReader(new FileReader(file)))
		{
			obj = JSONSerializer.deserializeObject(reader);
		}
		catch (FileNotFoundException f)
		{
			f.printStackTrace();
		}
		
		assertEquals(expectedArray, val.asArray());
		assertEquals(expectedObj, val.asObject());
		assertEquals(expectedArray, arr);
		assertEquals(expectedObj, obj);
	}
	
	@Test
	void testEmptyArrayFile()
	{
		testTemplate("samples/EmptyArray.json", "EmptyArrayTest", JSONArray.EMPTY, JSONObject.EMPTY);
	}
	
	@Test
	void testEmptyObjectFile()
	{
		testTemplate("samples/EmptyObject.json", "EmptyObjectTest", JSONArray.EMPTY, JSONObject.EMPTY);
	}
	
	@Test
	void testSmallArrayFile()
	{
		final JSONArray expected = new JSONArray();
		expected.addElement(true);
		expected.addElement(false);
		expected.addElement(3.14);
		expected.addElement("Testing");
		expected.addNullElement();
		expected.addElement("Test");
		testTemplate("samples/SmallArray.json", "SmallArrayTest", expected, JSONObject.EMPTY);
	}
	
	@Test
	void testSmallObjectFile()
	{
		final JSONObject expected = new JSONObject();
		expected.setField("boolean", true);
		expected.setNullField("none");
		expected.setField("string", "Example Text");
		expected.setField("num", 1.0E+3);
		testTemplate("samples/SmallObject.json", "SmallObjectTest", JSONArray.EMPTY, expected);
	}
	
	@Test
	void testBigArrayFile()
	{
		final JSONArray inner = new JSONArray();
		inner.addElement("This");
		inner.addElement("is");
		inner.addElement("an");
		inner.addElement("inner");
		inner.addElement("array");
		final JSONValue[] last = { new JSONStringValue("InnerArray"), new JSONStringValue("") };
		final JSONObject obj = new JSONObject();
		obj.setField("inner", JSONObject.EMPTY);
		obj.setField("arr", last);
		obj.setField("number", 15);
		final JSONArray expected = new JSONArray();
		expected.addElement(-34.2);
		expected.addNullElement();
		expected.addElement(inner);
		expected.addElement("Bottom TEXT");
		expected.addElement(obj);
		expected.addElement(0e45);
		expected.addElement(false);
		expected.addElement(0);
		testTemplate("samples/BigArray.json", "BigArrayTest", expected, JSONObject.EMPTY);
	}
	
	@Test
	void testBigObjectFile()
	{
		final JSONObject empty = new JSONObject();
		empty.setField("nope", (JSONValue) null);
		final JSONObject sup = new JSONObject();
		sup.setField("inner", JSONObject.EMPTY);
		sup.setField("arr", JSONArray.EMPTY);
		final JSONObject last = new JSONObject();
		last.setField("text", "Examp\\e");
		last.setField("inner", sup);
		final JSONValue[] arr = { new JSONObjectValue(empty), new JSONObjectValue(JSONObject.EMPTY), new JSONObjectValue(last) };
		final JSONValue[] nulls = { JSONNullValue.NULL, JSONNullValue.NULL, JSONBooleanValue.TRUE };
		final JSONObject inner = new JSONObject();
		inner.setField("num", 34);
		inner.setField("num2", 1.2E-2);
		inner.setField("num3", -1.2e2);
		inner.setField("inner", JSONObject.EMPTY);
		inner.setField("nulls", nulls);
		final JSONObject expected = new JSONObject();
		expected.setField("text", "Hello\u7684 there\n!");
		expected.setField("obj", inner);
		expected.setField("arr", arr);
		testTemplate("samples/BigObject.json", "BigObjectTest", JSONArray.EMPTY, expected);
	}
	
	@Test
	void testIncorrectFile()
	{
		testTemplate("samples/Incorrect.json", "IncorrectTest", JSONArray.EMPTY, JSONObject.EMPTY);
	}
}
