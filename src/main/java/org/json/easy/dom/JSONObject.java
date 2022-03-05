package org.json.easy.dom;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.easy.serialization.JSONType;

/**
 * Represents JSON object
 */
public final class JSONObject
{
	/**
	 * Contains a reference to global empty object
	 */
	private static JSONObject empty = null;
	
	/**
	 * Returns a reference to global empty JSON object
	 * 
	 * @return Reference to global empty JSON object
	 */
	public static JSONObject emptyObject()
	{
		if (empty == null)
		{
			empty = new JSONObject();
		}
		
		return empty;
	}
	
	/**
	 * Contains object values
	 */
	private final HashMap<String, JSONValue> values;
	
	/**
	 * Creates new empty JSON object
	 */
	public JSONObject()
	{
		values = new HashMap<String, JSONValue>();
	}
	
	/**
	 * Creates new JSON object based on a map
	 * 
	 * @param src Map to copy, null or blank keys are not copied
	 */
	public JSONObject(Map<String, JSONValue> src)
	{
		this();
		
		if (src != null)
		{
			Set<Map.Entry<String, JSONValue>> set = src.entrySet();
			
			if (set == null)
			{
				return;
			}
			
			for (Map.Entry<String, JSONValue> ent : set)
			{
				if (ent != null)
				{
					setField(ent.getKey(), ent.getValue());
				}
			}
		}
	}
	
	/**
	 * Creates a copy of an JSON object
	 * 
	 * @param src Object to copy
	 */
	public JSONObject(JSONObject src)
	{
		values = src == null ? new HashMap<String, JSONValue>() : new HashMap<String, JSONValue>(src.values);
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
		if (!(obj instanceof JSONObject))
		{
			return false;
		}
		
		JSONObject tmp = (JSONObject) obj;
		return values.equals(tmp.values);
	}
	
	/**
	 * Generates a hash code for this object
	 * 
	 * @return Hash code for this object
	 */
	@Override
	public int hashCode()
	{
		return values.hashCode();
	}
	
	/**
	 * Converts this object into a string
	 * 
	 * @return Human readable string representation of this object
	 */
	@Override
	public String toString()
	{
		return values.toString();
	}
	
	/**
	 * Returns this object size
	 * 
	 * @return Current amount of fields in this object
	 */
	public int size()
	{
		return values.size();
	}
	
	/**
	 * Attempts to get the field with the specified name and type
	 *
	 * @param fieldName The name of the field to get
	 * @param fieldType The type of the field to get, use null or JSONType.None value if type doesn't matter
	 * @return Field value, or null JSON value if the field doesn't exist or has different type
	 */
	public JSONValue getField(String fieldName, JSONType fieldType)
	{
		JSONValue val = values.get(fieldName);
		
		if (val != null && (fieldType == null || fieldType == JSONType.NONE || fieldType == val.getType()))
		{
			return val;
		}
		
		return JSONNullValue.get();
	}
	
	/**
	 * Attempts to get the field with the specified name
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or null JSON value if the field doesn't exist
	 */
	public JSONValue getField(String fieldName)
	{
		return getField(fieldName, null);
	}
	
	/**
	 * Checks whether a field with the specified name and type exists in the object
	 *
	 * @param fieldName Name of the field to check
	 * @param fieldType Type of the field to check, use null or JSONType.None value if type doesn't matter
	 * @return True if the field exists, false otherwise
	 */
	public boolean hasField(String fieldName, JSONType fieldType)
	{
		boolean res = values.containsKey(fieldName);
		
		if (fieldType == null || fieldType == JSONType.NONE)
		{
			return res;
		}
		else if (res)
		{
			JSONValue val = values.get(fieldName);
			return fieldType == val.getType();
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Checks whether a field with the specified name exists in the object
	 *
	 * @param fieldName Name of the field to check
	 * @return True if the field exists, false otherwise
	 */
	public boolean hasField(String fieldName)
	{
		return hasField(fieldName, null);
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set, null will be converted to JSON null value
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, JSONValue value)
	{
		if (fieldName == null || fieldName.isBlank() || this == emptyObject())
		{
			return false;
		}
		
		values.put(fieldName, value == null ? JSONNullValue.get() : value);
		return true;
	}
	
	/**
	 * Removes the field with the specified name
	 *
	 * @param fieldName Name of the field to remove
	 */
	public void removeField(String fieldName)
	{
		values.remove(fieldName);
	}
	
	/**
	 * Attempts to get the field with the specified name as a boolean
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or false if the field doesn't exist
	 */
	public boolean getBooleanField(String fieldName)
	{
		return getField(fieldName, JSONType.BOOLEAN).asBoolean();
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, boolean value)
	{
		return setField(fieldName, value ? JSONBooleanValue.getTrue() : JSONBooleanValue.getFalse());
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, Boolean value)
	{
		return setField(fieldName, value == null || !value ? JSONBooleanValue.getFalse() : JSONBooleanValue.getTrue());
	}
	
	/**
	 * Attempts to get the field with the specified name as a number
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or zero if the field doesn't exist
	 */
	public double getNumberField(String fieldName)
	{
		return getField(fieldName, JSONType.NUMBER).asNumber();
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, double value)
	{
		return setField(fieldName, new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, Double value)
	{
		return setField(fieldName, new JSONNumberValue(value));
	}
	
	/**
	 * Attempts to get the field with the specified name as a string
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or empty string if the field doesn't exist
	 */
	public String getStringField(String fieldName)
	{
		String res = getField(fieldName, JSONType.STRING).asString();
		return res == null ? "" : res;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, String value)
	{
		return setField(fieldName, new JSONStringValue(value));
	}
	
	/**
	 * Attempts to get the field with the specified name as an array
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or empty array if the field doesn't exist
	 */
	public JSONValue[] getArrayField(String fieldName)
	{
		JSONValue[] res = getField(fieldName, JSONType.ARRAY).asArray();
		return res == null ? JSONArrayValue.emptyArray() : res;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, JSONValue[] value)
	{
		return setField(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Attempts to get the field with the specified name as an object
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or empty object if the field doesn't exist
	 */
	public JSONObject getObjectField(String fieldName)
	{
		JSONObject res = getField(fieldName, JSONType.OBJECT).asObject();
		return res == null ? emptyObject() : res;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null or blank strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(String fieldName, JSONObject value)
	{
		return setField(fieldName, new JSONObjectValue(value));
	}
}
