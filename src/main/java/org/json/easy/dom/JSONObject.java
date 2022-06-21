package org.json.easy.dom;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.json.easy.serialization.JSONType;

/**
 * Represents JSON object
 * 
 * @since 1.0.0
 */
public class JSONObject
{
	/**
	 * Contains a reference to global empty object
	 */
	public static final JSONObject EMPTY = new JSONObject();
	
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
	public JSONObject(final Map<String, JSONValue> src)
	{
		this();
		
		if (src != null)
		{
			final Set<Map.Entry<String, JSONValue>> set = src.entrySet();
			
			if (set == null)
			{
				return;
			}
			
			for (final Map.Entry<String, JSONValue> ent : set)
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
	public JSONObject(final JSONObject src)
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
	public boolean equals(final Object obj)
	{
		if (!(obj instanceof JSONObject))
		{
			return false;
		}
		
		final JSONObject tmp = (JSONObject) obj;
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
	 * Creates a copy of this object and returns it as a map
	 * 
	 * @return Copy of this object as a map
	 */
	public final Map<String, JSONValue> toMap()
	{
		return new HashMap<String, JSONValue>(values);
	}
	
	/**
	 * Performs an action on every field in this object
	 * 
	 * @param action Action to perform
	 */
	public final void forEach(final Consumer<Map.Entry<String, JSONValue>> action)
	{
		if (action != null)
		{
			for (final Map.Entry<String, JSONValue> ent : values.entrySet())
			{
				action.accept(ent);
			}
		}
	}
	
	/**
	 * Checks if all values meet the condition
	 * 
	 * @param pred Condition to test
	 * @return True if all values met the condition, false otherwise
	 */
	public final boolean checkAll(final Predicate<JSONValue> pred)
	{
		if (pred != null)
		{
			for (final JSONValue val : values.values())
			{
				if (!pred.test(val))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Attempts to get the field with the specified name and type
	 *
	 * @param fieldName The name of the field to get
	 * @param fieldType The type of the field to get, use null or JSONType.None value if type doesn't matter
	 * @return Field value, or null JSON value if the field doesn't exist or has different type
	 */
	public final JSONValue getField(final String fieldName, final JSONType fieldType)
	{
		final JSONValue val = values.get(fieldName);
		
		if (val != null && (fieldType == null || fieldType == JSONType.NONE || fieldType == val.getType()))
		{
			return val;
		}
		
		return JSONNullValue.NULL;
	}
	
	/**
	 * Attempts to get the field with the specified name
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or null JSON value if the field doesn't exist
	 */
	public JSONValue getField(final String fieldName)
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
	public final boolean hasField(final String fieldName, final JSONType fieldType)
	{
		final boolean res = values.containsKey(fieldName);
		
		if (fieldType == null || fieldType == JSONType.NONE)
		{
			return res;
		}
		else if (res)
		{
			final JSONValue val = values.get(fieldName);
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
	public boolean hasField(final String fieldName)
	{
		return hasField(fieldName, null);
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set, null will be converted to JSON null value. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public final boolean setField(final String fieldName, final JSONValue value)
	{
		if (fieldName == null || this == EMPTY || (value != null && !isSafeToAdd(value)))
		{
			return false;
		}
		
		values.put(fieldName, value == null ? JSONNullValue.NULL : value);
		return true;
	}
	
	/**
	 * Removes the field with the specified name
	 *
	 * @param fieldName Name of the field to remove
	 */
	public final void removeField(final String fieldName)
	{
		values.remove(fieldName);
	}
	
	/**
	 * Removes all fields from this object
	 */
	public final void clear()
	{
		values.clear();
	}
	
	/**
	 * Sets the value of the field with the specified name to null
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setNullField(final String fieldName)
	{
		return setField(fieldName, JSONNullValue.NULL);
	}
	
	/**
	 * Attempts to get the field with the specified name as a boolean
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or false if the field doesn't exist
	 */
	public boolean getBooleanField(final String fieldName)
	{
		return getField(fieldName, JSONType.BOOLEAN).asBoolean();
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final boolean value)
	{
		return setField(fieldName, value ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE);
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final Boolean value)
	{
		return setField(fieldName, value == null || !value ? JSONBooleanValue.FALSE : JSONBooleanValue.TRUE);
	}
	
	/**
	 * Attempts to get the field with the specified name as a number
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or zero if the field doesn't exist
	 */
	public double getNumberField(final String fieldName)
	{
		return getField(fieldName, JSONType.NUMBER).asNumber();
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final double value)
	{
		return setField(fieldName, new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final Double value)
	{
		return setField(fieldName, new JSONNumberValue(value));
	}
	
	/**
	 * Attempts to get the field with the specified name as a string
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or empty string if the field doesn't exist
	 */
	public String getStringField(final String fieldName)
	{
		final String res = getField(fieldName, JSONType.STRING).asString();
		return res == null ? "" : res;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final String value)
	{
		return setField(fieldName, new JSONStringValue(value));
	}
	
	/**
	 * Attempts to get the field with the specified name as an array
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or empty array if the field doesn't exist
	 */
	public JSONArray getArrayField(final String fieldName)
	{
		final JSONArray res = getField(fieldName, JSONType.ARRAY).asArray();
		return res == null ? JSONArray.EMPTY : res;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final JSONArray value)
	{
		return setField(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final JSONValue[] value)
	{
		return setField(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final Iterable<JSONValue> value)
	{
		return setField(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Attempts to get the field with the specified name as an object
	 *
	 * @param fieldName The name of the field to get
	 * @return Field value, or empty object if the field doesn't exist
	 */
	public JSONObject getObjectField(final String fieldName)
	{
		final JSONObject res = getField(fieldName, JSONType.OBJECT).asObject();
		return res == null ? EMPTY : res;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final JSONObject value)
	{
		return setField(fieldName, new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final Map<String, JSONValue> value)
	{
		return setField(fieldName, new JSONObjectValue(value));
	}
	
	/**
	 * Checks if it is safe to add a value
	 * 
	 * @param value Value to check
	 * @return True if value can be added, false otherwise
	 */
	private boolean isSafeToAdd(final JSONValue value)
	{
		switch (value.getType())
		{
			case ARRAY:
				final JSONArray arr = value.asArray();
				return arr == null ? true : checkArray(arr);
			case OBJECT:
				final JSONObject obj = value.asObject();
				return obj == null ? true : checkObject(obj);
			default:
				return true;
		}
	}
	
	/**
	 * Checks if an object can be added
	 * 
	 * @param obj Object to check
	 * @return True if object can be added, false otherwise
	 */
	private boolean checkObject(final JSONObject obj)
	{
		if (obj == this)
		{
			return false;
		}
		
		for (final JSONValue val : obj.values.values())
		{
			if (!isSafeToAdd(val))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if an array can be added
	 * 
	 * @param arr Array to check
	 * @return True if array can be added, false otherwise
	 */
	private boolean checkArray(final JSONArray arr)
	{		
		return arr.checkAll(val -> isSafeToAdd(val));
	}
}
