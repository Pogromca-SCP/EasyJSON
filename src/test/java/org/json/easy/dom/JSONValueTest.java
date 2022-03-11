package org.json.easy.dom;

import org.junit.jupiter.api.Test;
import org.json.easy.serialization.JSONType;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JSONValueTest
{
	@Test
	void testValueEquals()
	{
		JSONValue[] values = { new JSONBooleanValue(false), new JSONNumberValue(115.4), new JSONNullValue(), new JSONArrayValue((JSONValue[]) null),
				new JSONStringValue("testing"), new JSONObjectValue(JSONObject.EMPTY), new JSONStringValue("test")};
		
		for (JSONValue val : values)
		{
			for (JSONValue v : values)
			{
				boolean tmp = val.equals(v);
				assertEquals(val == v, tmp);
				assertEquals(tmp, v.equals(val));
			}
		}
	}
	
	@Test
	void testNullValue()
	{
		JSONNullValue val = new JSONNullValue();
		assertEquals(0, val.hashCode());
		assertEquals("null", val.toString());
		
		assertEquals(JSONType.NULL, val.getType());
		assertEquals(true, val.isNull());
		
		assertEquals(false, val.asBoolean());
		assertEquals(0.0, val.asNumber());
		assertEquals("", val.asString());
		assertArrayEquals(JSONArrayValue.EMPTY, val.asArray());
		assertEquals(JSONObject.EMPTY, val.asObject());
	}

	@Test
	void testBooleanValue()
	{
		Boolean[] values = { null, false, true };
		
		for (Boolean v : values)
		{
			JSONBooleanValue val = new JSONBooleanValue(v);
			
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
			assertArrayEquals(JSONArrayValue.EMPTY, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
		}
	}
	
	@Test
	void testNumberValue()
	{
		Double[] values = { 0.0, 3.14, null, -9.4, 5.6, 12.0 };
		
		for (Double v : values)
		{
			JSONNumberValue val = new JSONNumberValue(v);
			
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
			assertArrayEquals(JSONArrayValue.EMPTY, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
		}
	}
	
	@Test
	void testStringValue()
	{
		String[] values = { null, "hahafunny", "", "5.6", "bruh" };
		
		for (String v : values)
		{
			JSONStringValue val = new JSONStringValue(v);
			
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
			assertArrayEquals(JSONArrayValue.EMPTY, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
		}
	}
	
	@Test
	void testArrayValue()
	{	
		JSONValue[][] values = {
				null,
				JSONArrayValue.EMPTY,
				{ new JSONBooleanValue(false), new JSONNumberValue(115.4) },
				{ new JSONNullValue(), new JSONArrayValue((JSONValue[]) null), new JSONStringValue("testing"), null, new JSONObjectValue(JSONObject.EMPTY)}
		};
		
		for (JSONValue[] v : values)
		{
			JSONArrayValue val = new JSONArrayValue(v);
			
			if (v == null)
			{
				v = JSONArrayValue.EMPTY;
			}
			
			assertEquals(Arrays.hashCode(v), val.hashCode());
			assertEquals(Arrays.toString(v), val.toString());
			
			assertEquals(JSONType.ARRAY, val.getType());
			assertEquals(false, val.isNull());
			
			assertEquals(false, val.asBoolean());
			assertEquals(0.0, val.asNumber());
			assertEquals("", val.asString());
			assertArrayEquals(v, val.asArray());
			assertEquals(JSONObject.EMPTY, val.asObject());
		}
	}
	
	@Test
	void testObjectValue()
	{
		HashMap<String, JSONValue> map = new HashMap<String, JSONValue>();
		map.put("null", null);
		map.put("bool", JSONBooleanValue.FALSE);
		map.put("test", new JSONStringValue("test"));
		map.put("34", new JSONNumberValue(34));
		JSONObject[] values = { null, JSONObject.EMPTY, new JSONObject(), new JSONObject(map)};
		
		for (JSONObject v : values)
		{
			JSONObjectValue val = new JSONObjectValue(v);
			
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
			assertArrayEquals(JSONArrayValue.EMPTY, val.asArray());
			assertEquals(v, val.asObject());
		}
	}
}
