package org.json.easy.dom;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import org.json.easy.serialization.JSONType;

import static org.junit.jupiter.api.Assertions.*;

class JSONObjectTest
{
	private static void serializationTest(final JSONObject obj)
	{	
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("SerializationTest")))
		{
			stream.writeObject(obj);
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
		
		assertEquals(obj, res);
	}
	
	@Test
	void testObjectConstructors()
	{
		final HashMap<String, JSONValue> map = new HashMap<String, JSONValue>();
		final HashMap<String, JSONValue> empty = new HashMap<String, JSONValue>();
		map.put("bool", JSONBooleanValue.TRUE);
		map.put("null", JSONNullValue.NULL);
		map.put("test", new JSONStringValue("testing"));
		
		final JSONObject obj = new JSONObject(map);
		final JSONObject emptyObj = JSONObject.EMPTY;
		assertEquals(map.hashCode(), obj.hashCode());
		assertEquals(empty.hashCode(), emptyObj.hashCode());
		assertEquals(map.toString(), obj.toString());
		assertEquals(empty.toString(), emptyObj.toString());
		assertEquals(map.size(), obj.size());
		assertEquals(empty.size(), emptyObj.size());
		
		final JSONObject copy = new JSONObject(obj);
		assertEquals(copy.equals(obj), obj.equals(copy));
		assertEquals(obj.size(), copy.size());
		assertEquals(obj.hashCode(), copy.hashCode());
		assertEquals(obj.toString(), copy.toString());
		assertEquals(map, copy.toMap());
	}
	
	@Test
	void testEmptyObject()
	{
		final JSONObject obj = JSONObject.EMPTY;
		assertEquals(0, obj.size());
		obj.setField("test", true);
		obj.setField("true", true);
		obj.setField("null", true);
		obj.setField("one", 1);
		assertEquals(0, obj.size());
		assertEquals(false, obj.hasField("test"));
		assertEquals(false, obj.hasField("true"));
		assertEquals(false, obj.hasField("null"));
		serializationTest(obj);
	}
	
	@Test
	void testObjectOperations()
	{
		final JSONObject obj = new JSONObject();
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
		obj.setNullField("null");
		assertEquals(JSONNullValue.NULL, obj.getField("null"));
		obj.setField(null, JSONNullValue.NULL);
		assertEquals(false, obj.hasField(null));
		
		serializationTest(obj);
		obj.clear();
		assertEquals(JSONObject.EMPTY, obj);
	}
	
	@Test
	void testObjectIterator()
	{
		final JSONObject obj = new JSONObject();
		obj.setNullField("null");
		obj.setField("num", 3.14);
		obj.setField("text", "Test");
		
		final HashMap<String, JSONValue> cp = new HashMap<String, JSONValue>();
		obj.forEach(ent -> cp.put(ent.getKey(), ent.getValue()));
		assertEquals(cp, obj.toMap());
	}
}
