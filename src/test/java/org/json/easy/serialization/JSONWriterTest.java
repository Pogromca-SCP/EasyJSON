package org.json.easy.serialization;

import org.json.easy.dom.*;
import java.io.StringWriter;
import org.json.easy.policies.CondensedJSONPrintPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JSONWriterTest
{
	private void testTemplate(final JSONValue val, final String expected)
	{
		final StringWriter res = new StringWriter();
		
		try (JSONWriter writer = new JSONWriter(res, new CondensedJSONPrintPolicy()))
		{
			switch (val.getType())
			{
				case ARRAY:
					JSONSerializer.serialize(val.asArray(), writer);
					break;
				case OBJECT:
					JSONSerializer.serialize(val.asObject(), writer);
					break;
				default:
			}
		}
		
		assertEquals(expected, res.toString());
	}
	
	@Test
	void testArrayWrite()
	{
		final JSONValue[] simple = { JSONBooleanValue.TRUE, null, new JSONStringValue("Testing"), JSONNullValue.NULL, new JSONNumberValue(-34.2) };
		final JSONArrayValue empty = new JSONArrayValue(JSONArrayValue.EMPTY);
		final JSONArrayValue simVal = new JSONArrayValue(simple);
		final JSONObject obj = new JSONObject();
		obj.setField("text", "Test");
		obj.setField("empty", "");
		obj.setField("arr", simple);
		obj.setField("bool", false);
		final JSONValue[] inner = { new JSONStringValue("This"), new JSONStringValue("is"), new JSONStringValue("inner"), new JSONStringValue("array") };
		
		final JSONValue[] complex = { simVal, null, new JSONObjectValue(obj), new JSONNumberValue(12),
				new JSONArrayValue(inner), empty, JSONBooleanValue.TRUE };
		
		final JSONArrayValue comVal = new JSONArrayValue(complex);
		testTemplate(empty, "[]");
		testTemplate(simVal, "[true,null,\"Testing\",null,-34.2]");
		testTemplate(comVal, "[[true,null,\"Testing\",null,-34.2],null,{\"empty\":\"\",\"bool\":false,\"text\":\"Test\",\"arr\":[true,null,\"Testing\",null,-34.2]},12.0,[\"This\",\"is\",\"inner\",\"array\"],[],true]");
	}
	
	@Test
	void testObjectWrite()
	{
		final JSONValue[] simple = { JSONBooleanValue.TRUE, null, new JSONStringValue("Testing"), JSONNullValue.NULL, new JSONNumberValue(-34.2) };
		final JSONObject obj = new JSONObject();
		obj.setField("text", "Test");
		obj.setField("empty", "");
		obj.setField("arr", simple);
		obj.setField("bool", false);
		obj.setField("obj", JSONObject.EMPTY);
		obj.setField("", (JSONValue) null);
		final JSONObjectValue val = new JSONObjectValue(obj);
		final JSONObjectValue em = new JSONObjectValue(JSONObject.EMPTY);
		testTemplate(em, "{}");
		testTemplate(val, "{\"empty\":\"\",\"text\":\"Test\",\"obj\":{},\"bool\":false,\"\":null,\"arr\":[true,null,\"Testing\",null,-34.2]}");
		final JSONObject no = new JSONObject();
		no.setField("text", "Example");
		no.setField("bottom", "Text");
		no.setField("test", "Bottom");
		obj.setField("inner", no);
		testTemplate(val, "{\"empty\":\"\",\"inner\":{\"bottom\":\"Text\",\"test\":\"Bottom\",\"text\":\"Example\"},\"text\":\"Test\",\"obj\":{},\"bool\":false,\"\":null,\"arr\":[true,null,\"Testing\",null,-34.2]}");
	}
}
