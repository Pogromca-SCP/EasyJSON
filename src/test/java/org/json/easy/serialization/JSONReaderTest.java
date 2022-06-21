package org.json.easy.serialization;

import org.json.easy.dom.*;
import java.io.FileReader;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONReaderTest
{
	private void testTemplate(final String file, final String name, final JSONValue[] expectedArray, final JSONObject expectedObj)
	{
		String err = null;
		JSONValue val = null;
		JSONValue[] arr = null;
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
			arr = JSONSerializer.deserializeArray(reader).toArray();
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
		
		assertArrayEquals(expectedArray, val.asArray().toArray());
		assertEquals(expectedObj, val.asObject());
		assertArrayEquals(expectedArray, arr);
		assertEquals(expectedObj, obj);
	}
	
	@Test
	void testEmptyArrayFile()
	{
		testTemplate("samples/EmptyArray.json", "EmptyArrayTest", JSONArray.EMPTY.toArray(), JSONObject.EMPTY);
	}
	
	@Test
	void testEmptyObjectFile()
	{
		testTemplate("samples/EmptyObject.json", "EmptyObjectTest", JSONArray.EMPTY.toArray(), JSONObject.EMPTY);
	}
	
	@Test
	void testSmallArrayFile()
	{
		final JSONValue[] expected = { JSONBooleanValue.TRUE, JSONBooleanValue.FALSE, new JSONNumberValue(3.14),
				new JSONStringValue("Testing"), JSONNullValue.NULL, new JSONStringValue("Test")};
		
		testTemplate("samples/SmallArray.json", "SmallArrayTest", expected, JSONObject.EMPTY);
	}
	
	@Test
	void testSmallObjectFile()
	{
		final JSONObject expected = new JSONObject();
		expected.setField("boolean", true);
		expected.setField("none", (JSONValue) null);
		expected.setField("string", "Example Text");
		testTemplate("samples/SmallObject.json", "SmallObjectTest", JSONArray.EMPTY.toArray(), expected);
	}
	
	@Test
	void testBigArrayFile()
	{
		final JSONValue[] inner = { new JSONStringValue("This"), new JSONStringValue("is"), new JSONStringValue("an"), new JSONStringValue("inner"),
				new JSONStringValue("array") };
		
		final JSONValue[] last = { new JSONStringValue("InnerArray"), new JSONStringValue("") };
		final JSONObject obj = new JSONObject();
		obj.setField("inner", JSONObject.EMPTY);
		obj.setField("arr", last);
		obj.setField("number", 15);
		
		final JSONValue[] expected = { new JSONNumberValue(-34.2), JSONNullValue.NULL, new JSONArrayValue(inner), new JSONStringValue("Bottom TEXT"),
				new JSONObjectValue(obj), new JSONNumberValue(0e45), JSONBooleanValue.FALSE };
		
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
		last.setField("text", "Example");
		last.setField("inner", sup);
		final JSONValue[] arr = { new JSONObjectValue(empty), new JSONObjectValue(JSONObject.EMPTY), new JSONObjectValue(last) };
		final JSONValue[] nulls = { JSONNullValue.NULL, JSONNullValue.NULL, JSONBooleanValue.TRUE };
		final JSONObject inner = new JSONObject();
		inner.setField("num", 34);
		inner.setField("num2", -44);
		inner.setField("inner", JSONObject.EMPTY);
		inner.setField("nulls", nulls);
		final JSONObject expected = new JSONObject();
		expected.setField("text", "Hello\u7684 there\n!");
		expected.setField("obj", inner);
		expected.setField("arr", arr);
		testTemplate("samples/BigObject.json", "BigObjectTest", JSONArray.EMPTY.toArray(), expected);
	}
	
	@Test
	void testIncorrectFile()
	{
		testTemplate("samples/Incorrect.json", "IncorrectTest", JSONArray.EMPTY.toArray(), JSONObject.EMPTY);
	}
}
