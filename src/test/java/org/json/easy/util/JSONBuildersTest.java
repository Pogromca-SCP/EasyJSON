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
		final JSONObjectBuilder obj = new JSONObjectBuilder();
		obj.set("true", true).set("nope").set("num", 34.98).set("text", "Bottom TEXT").set("empty", JSONObject.EMPTY).set("arr", JSONArray.EMPTY);
		out.println(obj);
		out.println();
		assertEquals(expected, obj.asObject());
		expected.setField("false", false);
		expected.setField("num2", -15);
		expected.setField("another", JSONObject.EMPTY);
		obj.copyIf(expected, (key, val) -> !obj.asObject().hasField(key));
		assertEquals(expected, obj.asObject());
	}
	
	@Test
	void testArrayBuilder()
	{
		final JSONArray expected = new JSONArray();
		expected.addElement(true);
		expected.addNullElement();
		expected.addElement(34.98);
		expected.addElement("Bottom TEXT");
		expected.addElement(JSONObject.EMPTY);
		expected.addElement(JSONArray.EMPTY);
		final JSONArrayBuilder arr = new JSONArrayBuilder();
		arr.add(true).add().add(34.98).add("Bottom TEXT").add(JSONObject.EMPTY).add(JSONArray.EMPTY);
		out.println(arr);
		out.println();
		assertEquals(expected, arr.asArray());
		expected.addElement(false);
		expected.addElement(-12.4);
		expected.addElement("Test");
		arr.copyIf(expected, val -> !arr.asArray().contains(val));
		assertEquals(expected, arr.asArray());
	}
}
