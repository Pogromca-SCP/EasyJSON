package org.json.easy.util;

import org.junit.jupiter.api.Test;
import org.json.easy.dom.*;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class JSONDOMBuilderTest
{
	@Test
	void testObjectBuilder()
	{
		final JSONObject expected = new JSONObject();
		expected.setField("true", true);
		expected.setField("nope", (JSONValue) null);
		expected.setField("num", 34.98);
		expected.setField("text", "Bottom TEXT");
		expected.setField("empty", JSONObject.EMPTY);
		expected.setField("arr", JSONArrayValue.EMPTY);
		final JSONDOMBuilder.Object obj = new JSONDOMBuilder.Object();
		obj.set("true", true);
		obj.set("nope");
		obj.set("num", 34.98);
		obj.set("text", "Bottom TEXT");
		obj.set("empty", JSONObject.EMPTY);
		obj.set("arr", JSONArrayValue.EMPTY);
		out.println(obj);
		out.println();
		assertEquals(expected, obj.asObject());
		expected.setField("false", false);
		expected.setField("num2", -15);
		expected.setField("another", JSONObject.EMPTY);
		
		obj.copyIf(expected.copyToMap(), (key, val) -> {
			return !obj.asObject().hasField(key);
		});
		
		assertEquals(expected, obj.asObject());
	}
	
	@Test
	void testArrayBuilder()
	{
		final JSONValue[] expected = { JSONBooleanValue.TRUE, null, new JSONNumberValue(34.98), new JSONStringValue("Bottom TEXT"),
				new JSONObjectValue(JSONObject.EMPTY), new JSONArrayValue(JSONArrayValue.EMPTY) };
		
		final JSONDOMBuilder.Array arr = new JSONDOMBuilder.Array();
		arr.add(true);
		arr.add((JSONValue) null);
		arr.add(34.98);
		arr.add("Bottom TEXT");
		arr.add(JSONObject.EMPTY);
		arr.add(JSONArrayValue.EMPTY);
		out.println(arr);
		out.println();
		assertArrayEquals(expected, arr.asValue().asArray());
		
		final JSONValue[] second = { JSONBooleanValue.TRUE, null, new JSONNumberValue(34.98), new JSONStringValue("Bottom TEXT"),
				new JSONObjectValue(JSONObject.EMPTY), new JSONArrayValue(JSONArrayValue.EMPTY), JSONBooleanValue.FALSE, new JSONNumberValue(-15) };
		
		arr.copyIf(second, (val) -> {
			for (final JSONValue v : arr.asValue().asArray())
			{
				if (v == null ? val == null : v.equals(val))
				{
					return false;
				}
			}
			
			return true;
		});
		
		assertArrayEquals(second, arr.asValue().asArray());
	}
}
