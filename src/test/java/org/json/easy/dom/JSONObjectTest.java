package org.json.easy.dom;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.json.easy.serialization.JSONType;

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
		Assertions.assertEquals(map.hashCode(), obj.hashCode());
		Assertions.assertEquals(empty.hashCode(), emptyObj.hashCode());
		Assertions.assertEquals(map.toString(), obj.toString());
		Assertions.assertEquals(empty.toString(), emptyObj.toString());
		Assertions.assertEquals(map.size(), obj.size());
		Assertions.assertEquals(empty.size(), emptyObj.size());
		JSONObject copy = new JSONObject(obj);
		Assertions.assertEquals(copy.equals(obj), obj.equals(copy));
		Assertions.assertEquals(obj.size(), copy.size());
		Assertions.assertEquals(obj.hashCode(), copy.hashCode());
		Assertions.assertEquals(obj.toString(), copy.toString());
	}
	
	@Test
	void testEmptyObject()
	{
		JSONObject obj = JSONObject.EMPTY;
		Assertions.assertEquals(0, obj.size());
		obj.setField("test", true);
		obj.setField("true", true);
		obj.setField("null", true);
		Assertions.assertEquals(0, obj.size());
		Assertions.assertEquals(false, obj.hasField("test"));
		Assertions.assertEquals(false, obj.hasField("true"));
		Assertions.assertEquals(false, obj.hasField("null"));
	}
	
	@Test
	void testObjectOperations()
	{
		JSONObject obj = new JSONObject();
		obj.setField("true", JSONBooleanValue.TRUE);
		Assertions.assertEquals(true, obj.hasField("true", JSONType.BOOLEAN));
		Assertions.assertEquals(true, obj.hasField("true"));
		Assertions.assertEquals(false, obj.hasField("true", JSONType.OBJECT));
		Assertions.assertEquals(true, obj.getBooleanField("true"));
		Assertions.assertEquals(false, obj.getBooleanField("bruh"));
		Assertions.assertEquals(JSONBooleanValue.TRUE, obj.getField("true", JSONType.BOOLEAN));
		Assertions.assertEquals(JSONBooleanValue.TRUE, obj.getField("true"));
		Assertions.assertEquals(JSONNullValue.NULL, obj.getField("true", JSONType.NUMBER));
		Assertions.assertEquals(JSONNullValue.NULL, obj.getField("bruh"));
		obj.removeField("true");
		Assertions.assertEquals(JSONNullValue.NULL, obj.getField("true"));
		Assertions.assertEquals(false, obj.hasField("true"));
		obj.setField("", (JSONValue) null);
		Assertions.assertEquals(false, obj.hasField(""));
		obj.setField("   \t", JSONNullValue.NULL);
		Assertions.assertEquals(false, obj.hasField("   \t"));
		obj.setField("null", (JSONValue) null);
		Assertions.assertEquals(JSONNullValue.NULL, obj.getField("null"));
		obj.setField(null, JSONNullValue.NULL);
		Assertions.assertEquals(false, obj.hasField(null));
	}
}
