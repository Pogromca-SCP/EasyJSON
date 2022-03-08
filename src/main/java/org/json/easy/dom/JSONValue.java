package org.json.easy.dom;

import org.json.easy.serialization.JSONType;
import java.util.Arrays;

/**
 * Base class for JSON value representation
 */
public abstract class JSONValue
{
	/**
	 * Contains this value type
	 */
	protected JSONType type;
	
	/**
	 * Creates new JSON value
	 */
	protected JSONValue()
	{
		type = JSONType.NONE;
	}
	
	/**
	 * Retrieves this value type
	 * 
	 * @return Current value type
	 */
	public final JSONType getType()
	{
		return type;
	}
	
	/**
	 * Compares two objects
	 * 
	 * @param obj Object to compare
	 * @return True if objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof JSONValue ? equals((JSONValue) obj) : false;
	}
	
	/**
	 * Compares two JSON values
	 * 
	 * @param val Value to compare
	 * @return True if values are equal, false otherwise
	 */
	public final boolean equals(JSONValue val)
	{
		if (val == null || type != val.type)
		{
			return false;
		}
		
		switch (type)
		{
			case NONE:
			case NULL:
				return true;
			case STRING:
				String str1 = asString();
				String str2 = val.asString();
				return str1 == null ? str2 == null : str1.equals(str2);
			case BOOLEAN:
				return asBoolean() == val.asBoolean();
			case NUMBER:
				return asNumber() == val.asNumber();
			case ARRAY:
				return Arrays.equals(asArray(), val.asArray());
			case OBJECT:
				JSONObject obj1 = asObject();
				JSONObject obj2 = val.asObject();
				return obj1 == null ? obj2 == null : obj1.equals(obj2);
			default:
				return false;
		}
	}
	
	/**
	 * Checks if this value is null
	 * 
	 * @return True if this value is null, false otherwise
	 */
	public final boolean isNull()
	{
		return type == JSONType.NULL || type == JSONType.NONE;
	}
	
	/**
	 * Retrieves this value as a boolean
	 * 
	 * @return This value as a boolean or false if not possible
	 */
	public boolean asBoolean()
	{
		return false;
	}
	
	/**
	 * Retrieves this value as a number
	 * 
	 * @return This value as a number or zero if not possible
	 */
	public double asNumber()
	{
		return 0.0;
	}
	
	/**
	 * Retrieves this value as a string
	 * 
	 * @return This value as a string or empty string if not possible
	 */
	public String asString()
	{
		return "";
	}
	
	/**
	 * Retrieves this value as an array
	 * 
	 * @return This value as an array or empty array if not possible
	 */
	public JSONValue[] asArray()
	{
		return JSONArrayValue.EMPTY;
	}
	
	/**
	 * Retrieves this value as an object
	 * 
	 * @return This value as an object or empty object if not possible
	 */
	public JSONObject asObject()
	{
		return JSONObject.EMPTY;
	}
}
