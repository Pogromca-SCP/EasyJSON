package org.json.easy.util;

import org.junit.jupiter.api.Test;
import org.json.easy.dom.*;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

class JSONBuildersTest
{
	@Test
	void testObjectBuilder()
	{
		final JSONObject expected = new JSONObject();
		expected.setField("true", true);
		expected.setNullField("nope");
		expected.setField("num", 34.98);
		expected.setField("text", "Bottom TEXT");
		expected.setField("empty", JSONObject.EMPTY);
		expected.setField("arr", JSONArray.EMPTY);
		expected.setField("bool", false);
		expected.setField("pie", 3.14);
		expected.setField("native", JSONArray.EMPTY);
		expected.setField("list", JSONArray.EMPTY);
		expected.setField("arrb", JSONArray.EMPTY);
		expected.setField("map", JSONObject.EMPTY);
		expected.setField("objb", JSONObject.EMPTY);
		expected.setField("val", "BRUH");
		
		final JSONObjectBuilder obj = new JSONObjectBuilder();
		obj.set("true", true).set("nope").set("num", 34.98).set("text", "Bottom TEXT").set("empty", JSONObject.EMPTY).set("arr", JSONArray.EMPTY).set("bool", Boolean.FALSE);
		obj.set("pie", Double.valueOf(3.14)).set("native", JSONArray.EMPTY.toArray()).set("list", JSONArray.EMPTY.toList()).set("arrb", new JSONArrayBuilder()).set("map", JSONObject.EMPTY.toMap());
		obj.set("objb", new JSONObjectBuilder()).set("val", new JSONStringValue("BRUH"));
		out.println(obj);
		out.println();
		
		assertEquals(expected, obj.asObject());
		assertEquals(expected.size(), obj.size());
		expected.setField("false", false);
		expected.setField("num2", -15);
		expected.setField("another", JSONObject.EMPTY);
		obj.copyIf(expected, (key, val) -> !obj.asObject().hasField(key));
		assertEquals(expected, obj.asObject());
		assertEquals(expected.size(), obj.size());
		
		final JSONObjectBuilder map = new JSONObjectBuilder();
		map.copyIf(expected.toMap(), (key, val) -> !map.asObject().hasField(key));
		assertEquals(expected, map.asObject());
		assertEquals(expected.size(), map.size());
		assertEquals(expected, new JSONObjectBuilder().copyAll(expected).asObject());
	}
	
	@Test
	void testArrayBuilder()
	{
		JSONArray expected = new JSONArray();
		expected.addElement(true);
		expected.addNullElement();
		expected.addElement(34.98);
		expected.addElement("Bottom TEXT");
		expected.addElement(JSONObject.EMPTY);
		expected.addElement(JSONArray.EMPTY);
		expected.addElement(false);
		expected.addElement(3.14);
		expected.addElement(JSONArray.EMPTY);
		expected.addElement(JSONArray.EMPTY);
		expected.addElement(JSONArray.EMPTY);
		expected.addElement(JSONObject.EMPTY);
		expected.addElement(JSONObject.EMPTY);
		expected.addElement("BRUH");
		
		final JSONArrayBuilder arr = new JSONArrayBuilder();
		arr.add(true).add().add(34.98).add("Bottom TEXT").add(JSONObject.EMPTY).add(JSONArray.EMPTY).add(Boolean.FALSE).add(Double.valueOf(3.14)).add(JSONArray.EMPTY.toArray());
		arr.add(JSONArray.EMPTY.toList()).add(new JSONArrayBuilder()).add(JSONObject.EMPTY.toMap()).add(new JSONObjectBuilder()).add(new JSONStringValue("BRUH"));
		out.println(arr);
		out.println();
		
		assertEquals(expected, arr.asArray());
		assertEquals(expected.size(), arr.size());
		expected.addElement(-12.4);
		expected.addElement("Test");
		arr.copyIf(expected, val -> !arr.asArray().contains(val));
		assertEquals(expected, arr.asArray());
		assertEquals(expected.size(), arr.size());
		expected = new JSONArray();
		expected.addElement(true);
		expected.addNullElement();
		expected.addElement(34.98);
		expected.addElement("Bottom TEXT");
		expected.addElement(JSONObject.EMPTY);
		expected.addElement(JSONArray.EMPTY);
		expected.addElement(false);
		expected.addElement(3.14);
		expected.addElement("BRUH");
		
		final JSONArrayBuilder arr2 = new JSONArrayBuilder();
		arr2.copyIf(expected.toArray(), val -> !arr2.asArray().contains(val));
		assertEquals(expected, arr2.asArray());
		assertEquals(expected.size(), arr2.size());
		
		final JSONArrayBuilder list = new JSONArrayBuilder();
		list.copyIf(expected.toList(), val -> !list.asArray().contains(val));
		assertEquals(expected, list.asArray());
		assertEquals(expected.size(), list.size());
		assertEquals(expected, new JSONArrayBuilder().copyAll(expected).asArray());
	}
}
