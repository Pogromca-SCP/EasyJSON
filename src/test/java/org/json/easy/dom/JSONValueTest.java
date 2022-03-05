package org.json.easy.dom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.json.easy.serialization.JSONType;
import java.util.Arrays;
import java.util.HashMap;

class JSONValueTest
{
	@Test
	void testValueEquals()
	{
		JSONValue[] values = { new JSONBooleanValue(false), new JSONNumberValue(115.4), new JSONNullValue(), new JSONArrayValue(null),
				new JSONStringValue("testing"), new JSONObjectValue(JSONObject.emptyObject()), new JSONStringValue("test")};
		
		for (JSONValue val : values)
		{
			for (JSONValue v : values)
			{
				boolean tmp = val.equals(v);
				Assertions.assertEquals(val == v, tmp);
				Assertions.assertEquals(tmp, v.equals(val));
			}
		}
	}
	
	@Test
	void testNullValue()
	{
		JSONNullValue val = new JSONNullValue();
		Assertions.assertEquals(0, val.hashCode());
		Assertions.assertEquals("null", val.toString());
		
		Assertions.assertEquals(JSONType.NULL, val.getType());
		Assertions.assertEquals(true, val.isNull());
		
		Assertions.assertEquals(false, val.asBoolean());
		Assertions.assertEquals(0.0, val.asNumber());
		Assertions.assertEquals("", val.asString());
		Assertions.assertArrayEquals(JSONArrayValue.emptyArray(), val.asArray());
		Assertions.assertEquals(JSONObject.emptyObject(), val.asObject());
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
			
			Assertions.assertEquals(Boolean.hashCode(v), val.hashCode());
			Assertions.assertEquals(Boolean.toString(v), val.toString());
			
			Assertions.assertEquals(JSONType.BOOLEAN, val.getType());
			Assertions.assertEquals(false, val.isNull());
			
			Assertions.assertEquals(v, val.asBoolean());
			Assertions.assertEquals(v ? 1.0 : 0.0, val.asNumber());
			Assertions.assertEquals(Boolean.toString(v), val.asString());
			Assertions.assertArrayEquals(JSONArrayValue.emptyArray(), val.asArray());
			Assertions.assertEquals(JSONObject.emptyObject(), val.asObject());
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
			
			Assertions.assertEquals(Double.hashCode(v), val.hashCode());
			Assertions.assertEquals(Double.toString(v), val.toString());
			
			Assertions.assertEquals(JSONType.NUMBER, val.getType());
			Assertions.assertEquals(false, val.isNull());
			
			Assertions.assertEquals(v != 0.0, val.asBoolean());
			Assertions.assertEquals(v, val.asNumber());
			Assertions.assertEquals(Double.toString(v), val.asString());
			Assertions.assertArrayEquals(JSONArrayValue.emptyArray(), val.asArray());
			Assertions.assertEquals(JSONObject.emptyObject(), val.asObject());
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
			
			Assertions.assertEquals(v.hashCode(), val.hashCode());
			Assertions.assertEquals(v, val.toString());
			
			Assertions.assertEquals(JSONType.STRING, val.getType());
			Assertions.assertEquals(false, val.isNull());
			
			Assertions.assertEquals(Boolean.parseBoolean(v), val.asBoolean());
			double num = 0.0;
			
			try
			{
				num = Double.parseDouble(v);
			}
			catch (NumberFormatException n) {}
			
			Assertions.assertEquals(num, val.asNumber());
			Assertions.assertEquals(v, val.asString());
			Assertions.assertArrayEquals(JSONArrayValue.emptyArray(), val.asArray());
			Assertions.assertEquals(JSONObject.emptyObject(), val.asObject());
		}
	}
	
	@Test
	void testArrayValue()
	{	
		JSONValue[][] values = {
				null,
				JSONArrayValue.emptyArray(),
				{ new JSONBooleanValue(false), new JSONNumberValue(115.4) },
				{ new JSONNullValue(), new JSONArrayValue(null), new JSONStringValue("testing"), null, new JSONObjectValue(JSONObject.emptyObject())}
		};
		
		for (JSONValue[] v : values)
		{
			JSONArrayValue val = new JSONArrayValue(v);
			
			if (v == null)
			{
				v = JSONArrayValue.emptyArray();
			}
			
			Assertions.assertEquals(Arrays.hashCode(v), val.hashCode());
			Assertions.assertEquals(Arrays.toString(v), val.toString());
			
			Assertions.assertEquals(JSONType.ARRAY, val.getType());
			Assertions.assertEquals(false, val.isNull());
			
			Assertions.assertEquals(false, val.asBoolean());
			Assertions.assertEquals(0.0, val.asNumber());
			Assertions.assertEquals("", val.asString());
			Assertions.assertArrayEquals(v, val.asArray());
			Assertions.assertEquals(JSONObject.emptyObject(), val.asObject());
		}
	}
	
	@Test
	void testObjectValue()
	{
		HashMap<String, JSONValue> map = new HashMap<String, JSONValue>();
		map.put("null", null);
		map.put("bool", JSONBooleanValue.getFalse());
		map.put("test", new JSONStringValue("test"));
		map.put("34", new JSONNumberValue(34));
		JSONObject[] values = { null, JSONObject.emptyObject(), new JSONObject(), new JSONObject(map)};
		
		for (JSONObject v : values)
		{
			JSONObjectValue val = new JSONObjectValue(v);
			
			if (v == null)
			{
				v = JSONObject.emptyObject();
			}
			
			Assertions.assertEquals(v.hashCode(), val.hashCode());
			Assertions.assertEquals(v.toString(), val.toString());
			
			Assertions.assertEquals(JSONType.OBJECT, val.getType());
			Assertions.assertEquals(false, val.isNull());
			
			Assertions.assertEquals(false, val.asBoolean());
			Assertions.assertEquals(0.0, val.asNumber());
			Assertions.assertEquals("", val.asString());
			Assertions.assertArrayEquals(JSONArrayValue.emptyArray(), val.asArray());
			Assertions.assertEquals(v, val.asObject());
		}
	}
}
