package org.json.easy.dom;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import org.json.easy.serialization.JSONType;

import static org.junit.jupiter.api.Assertions.*;

class JSONObjectTest
{
	@Test
	void testObjectConstructors()
	{
		HashMap<String, JSONValue> map = new HashMap<String, JSONValue>();
		HashMap<String, JSONValue> empty = new HashMap<String, JSONValue>();
		map.put("bool", JSONBooleanValue.TRUE);
		map.put("null", JSONNullValue.NULL);
		map.put("test", new JSONStringValue("testing"));
		JSONObject obj = new JSONObject(map);
		JSONObject emptyObj = JSONObject.EMPTY;
		assertEquals(map.hashCode(), obj.hashCode());
		assertEquals(empty.hashCode(), emptyObj.hashCode());
		assertEquals(map.toString(), obj.toString());
		assertEquals(empty.toString(), emptyObj.toString());
		assertEquals(map.size(), obj.size());
		assertEquals(empty.size(), emptyObj.size());
		JSONObject copy = new JSONObject(obj);
		assertEquals(copy.equals(obj), obj.equals(copy));
		assertEquals(obj.size(), copy.size());
		assertEquals(obj.hashCode(), copy.hashCode());
		assertEquals(obj.toString(), copy.toString());
		assertEquals(map, copy.copyToMap());
	}
	
	@Test
	void testEmptyObject()
	{
		JSONObject obj = JSONObject.EMPTY;
		assertEquals(0, obj.size());
		obj.setField("test", true);
		obj.setField("true", true);
		obj.setField("null", true);
		assertEquals(0, obj.size());
		assertEquals(false, obj.hasField("test"));
		assertEquals(false, obj.hasField("true"));
		assertEquals(false, obj.hasField("null"));
	}
	
	@Test
	void testObjectOperations()
	{
		JSONObject obj = new JSONObject();
		obj.setField("true", JSONBooleanValue.TRUE);
		assertEquals(true, obj.hasField("true", JSONType.BOOLEAN));
		assertEquals(true, obj.hasField("true"));
		assertEquals(false, obj.hasField("true", JSONType.OBJECT));
		assertEquals(true, obj.getBooleanField("true"));
		assertEquals(false, obj.getBooleanField("bruh"));
		assertEquals(JSONBooleanValue.TRUE, obj.getField("true", JSONType.BOOLEAN));
		assertEquals(JSONBooleanValue.TRUE, obj.getField("true"));
		assertEquals(JSONNullValue.NULL, obj.getField("true", JSONType.NUMBER));
		assertEquals(JSONNullValue.NULL, obj.getField("bruh"));
		obj.removeField("true");
		assertEquals(JSONNullValue.NULL, obj.getField("true"));
		assertEquals(false, obj.hasField("true"));
		obj.setField("", (JSONValue) null);
		assertEquals(true, obj.hasField(""));
		obj.setField("   \t", JSONNullValue.NULL);
		assertEquals(true, obj.hasField("   \t"));
		obj.setField("null", (JSONValue) null);
		assertEquals(JSONNullValue.NULL, obj.getField("null"));
		obj.setField(null, JSONNullValue.NULL);
		assertEquals(false, obj.hasField(null));
	}
}
