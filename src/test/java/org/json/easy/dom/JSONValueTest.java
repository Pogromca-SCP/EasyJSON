package org.json.easy.dom;

import org.junit.jupiter.api.Test;
import org.json.easy.serialization.JSONType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JSONValueTest
{
	private static void serializationTest(final JSONValue val)
	{	
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("SerializationTest")))
		{
			stream.writeObject(val);
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
		
		assertEquals(val, res);
	}
	
	@Test
	void testValueEquals()
	{
		final JSONValue[] values = { new JSONBooleanValue(false), new JSONNumberValue(115.4), new JSONNullValue(), new JSONArrayValue((JSONValue[]) null),
				new JSONStringValue("testing"), new JSONObjectValue(JSONObject.EMPTY), new JSONStringValue("test")};
		
		for (final JSONValue val : values)
		{
			for (final JSONValue v : values)
			{
				final boolean tmp = val.equals(v);
				assertEquals(val == v, tmp);
				assertEquals(tmp, v.equals(val));
			}
		}
	}
	
	@Test
	void testNullValue()
	{
		final JSONNullValue val = new JSONNullValue();
		assertEquals(0, val.hashCode());
		assertEquals("null", val.toString());
		
		assertEquals(JSONType.NULL, val.getType());
		assertEquals(true, val.isNull());
		
		assertEquals(false, val.asBoolean());
		assertEquals(0.0, val.asNumber());
		assertEquals("", val.asString());
		assertEquals(JSONArray.EMPTY, val.asArray());
		assertEquals(JSONObject.EMPTY, val.asObject());
		serializationTest(val);
	}

	@Test
	void testBooleanValue()
	{
		final Boolean[] values = { null, false, true };
		
		for (Boolean v : values)
		{
			final JSONBooleanValue val = new JSONBooleanValue(v);
			
			if (v == null)
			{
				v = false;
			}
			
			assertEquals(Boolean.hashCode(v), val.hashCode());
			assertEquals(Boolean.toString(v), val.toString());
			
			assertEquals(JSONType.BOOLEAN, val.getType());
			assertEquals(false, val.isNull());
			
			assertEquals(v, val.asBoolean());
			assertEquals(v ? 1.0 : 0.0, val.asNumber());
			assertEquals(Boolean.toString(v), val.asString());
			assertEquals(JSONArray.EMPTY, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
			serializationTest(val);
		}
	}
	
	@Test
	void testNumberValue()
	{
		final Double[] values = { 0.0, 3.14, null, -9.4, 5.6, 12.0 };
		
		for (Double v : values)
		{
			final JSONNumberValue val = new JSONNumberValue(v);
			
			if (v == null)
			{
				v = 0.0;
			}
			
			assertEquals(Double.hashCode(v), val.hashCode());
			assertEquals(Double.toString(v), val.toString());
			
			assertEquals(JSONType.NUMBER, val.getType());
			assertEquals(false, val.isNull());
			
			assertEquals(v != 0.0, val.asBoolean());
			assertEquals(v, val.asNumber());
			assertEquals(Double.toString(v), val.asString());
			assertEquals(JSONArray.EMPTY, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
			serializationTest(val);
		}
	}
	
	@Test
	void testStringValue()
	{
		final String[] values = { null, "hahafunny", "", "5.6", "bruh" };
		
		for (String v : values)
		{
			final JSONStringValue val = new JSONStringValue(v);
			
			if (v == null)
			{
				v = "";
			}
			
			assertEquals(v.hashCode(), val.hashCode());
			assertEquals(v, val.toString());
			
			assertEquals(JSONType.STRING, val.getType());
			assertEquals(false, val.isNull());
			
			assertEquals(Boolean.parseBoolean(v), val.asBoolean());
			double num = 0.0;
			
			try
			{
				num = Double.parseDouble(v);
			}
			catch (NumberFormatException n) {}
			
			assertEquals(num, val.asNumber());
			assertEquals(v, val.asString());
			assertEquals(JSONArray.EMPTY, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
			serializationTest(val);
		}
	}
	
	@Test
	void testArrayValue()
	{	
		final JSONArray arr = new JSONArray();
		arr.addElement(false);
		arr.addElement(115.4);
		final JSONArray arr2 = new JSONArray();
		arr2.addNullElement();
		arr2.addElement(JSONArray.EMPTY);
		arr2.addElement("testing");
		arr2.addNullElement();
		arr2.addElement(JSONObject.EMPTY);
		final JSONArray[] values = { null, JSONArray.EMPTY, arr, arr2 };
		
		for (JSONArray v : values)
		{
			final JSONArrayValue val = new JSONArrayValue(v);
			
			if (v == null)
			{
				v = JSONArray.EMPTY;
			}
			
			assertEquals(v.hashCode(), val.hashCode());
			assertEquals(v.toString(), val.toString());
			
			assertEquals(JSONType.ARRAY, val.getType());
			assertEquals(false, val.isNull());
			
			assertEquals(false, val.asBoolean());
			assertEquals(0.0, val.asNumber());
			assertEquals("", val.asString());
			assertEquals(v, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
			serializationTest(val);
		}
	}
	
	@Test
	void testObjectValue()
	{
		final HashMap<String, JSONValue> map = new HashMap<String, JSONValue>();
		map.put("null", null);
		map.put("bool", JSONBooleanValue.FALSE);
		map.put("test", new JSONStringValue("test"));
		map.put("34", new JSONNumberValue(34));
		final JSONObject[] values = { null, JSONObject.EMPTY, new JSONObject(), new JSONObject(map) };
		
		for (JSONObject v : values)
		{
			final JSONObjectValue val = new JSONObjectValue(v);
			
			if (v == null)
			{
				v = JSONObject.EMPTY;
			}
			
			assertEquals(v.hashCode(), val.hashCode());
			assertEquals(v.toString(), val.toString());
			
			assertEquals(JSONType.OBJECT, val.getType());
			assertEquals(false, val.isNull());
			
			assertEquals(false, val.asBoolean());
			assertEquals(0.0, val.asNumber());
			assertEquals("", val.asString());
			assertEquals(JSONArray.EMPTY, val.asArray());
			assertEquals(v, val.asObject());
			serializationTest(val);
		}
	}
}
