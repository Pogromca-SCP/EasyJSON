package org.json.easy.dom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import org.json.easy.serialization.JSONType;
import java.util.stream.Stream;

/**
 * Represents JSON object
 * 
 * @since 1.0.0
 */
public class JSONObject implements Serializable
{
	/**
	 * Contains a reference to global empty object
	 */
	public static final JSONObject EMPTY = new JSONObject();
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Converts a boolean to a JSON value
	 * 
	 * @param val Value to convert
	 * @return Converted value
	 */
	private static JSONBooleanValue asVal(final boolean val)
	{
		return val ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE;
	}
	
	/**
	 * Converts a boolean to a JSON value
	 * 
	 * @param val Value to convert
	 * @return Converted value
	 */
	private static JSONBooleanValue asVal(final Boolean val)
	{
		return asVal(val == null ? false : val);
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
	public JSONObject(final Map<String, ? extends JSONValue> src)
	{
		this();
		
		if (src != null)
		{
			final Set<? extends Map.Entry<String, ? extends JSONValue>> set = src.entrySet();
			
			if (set == null)
			{
				return;
			}
			
			for (final Map.Entry<String, ? extends JSONValue> ent : set)
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
		return obj instanceof JSONObject ? values.equals(((JSONObject) obj).values) : false;
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
	 * Checks if this object is empty
	 * 
	 * @return True if object is empty, false otherwise
	 * @since 1.1.0
	 */
	public boolean isEmpty()
	{
		return values.isEmpty();
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
	 * Performs an action on every field in this object
	 * 
	 * @param action Action to perform
	 * @since 1.1.0
	 */
	public final void forEach(final BiConsumer<String, JSONValue> action)
	{
		if (action != null)
		{
			for (final Map.Entry<String, JSONValue> ent : values.entrySet())
			{
				action.accept(ent.getKey(), ent.getValue());
			}
		}
	}
	
	/**
	 * Performs an action on every key in this object
	 * 
	 * @param action Action to perform
	 * @since 1.1.0
	 */
	public final void forEachKey(final Consumer<String> action)
	{
		if (action != null)
		{
			for (final String str : values.keySet())
			{
				action.accept(str);
			}
		}
	}
	
	/**
	 * Performs an action on every value in this object
	 * 
	 * @param action Action to perform
	 * @since 1.1.0
	 */
	public final void forEachValue(final Consumer<JSONValue> action)
	{
		if (action != null)
		{
			for (final JSONValue val : values.values())
			{
				action.accept(val);
			}
		}
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
			return fieldType == values.get(fieldName).getType();
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
	 * Returns a sequential stream object for this object
	 * 
	 * @return Sequential stream over the entries in this object
	 * @since 1.1.0
	 */
	public final Stream<Map.Entry<String, JSONValue>> stream()
	{
		return values.entrySet().stream();
	}
	
	/**
	 * Returns a sequential stream object for keys in this object
	 * 
	 * @return Sequential stream over the keys in this object
	 * @since 1.1.0
	 */
	public final Stream<String> keysStream()
	{
		return values.keySet().stream();
	}
	
	/**
	 * Returns a sequential stream object for values in this object
	 * 
	 * @return Sequential stream over the values in this object
	 * @since 1.1.0
	 */
	public final Stream<JSONValue> valuesStream()
	{
		return values.values().stream();
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
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public final boolean hasValue(final JSONValue value)
	{
		return values.containsValue(value);
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set, null will be converted to JSON null value
	 * @return True if set successfully, false otherwise
	 */
	public final boolean setField(final String fieldName, final JSONValue value)
	{
		if (fieldName == null || this == EMPTY)
		{
			return false;
		}
		
		values.put(fieldName, value == null ? JSONNullValue.NULL : value);
		return true;
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set, null will be converted to JSON null value
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public final boolean setFieldIfAbsent(final String fieldName, final JSONValue value)
	{
		if (hasField(fieldName))
		{
			return false;
		}
		
		return setField(fieldName, value);
	}
	
	/**
	 * Checks whether a field with the null value exists in the object
	 *
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasNullValue()
	{
		return hasValue(JSONNullValue.NULL);
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
	 * Sets the value of the field with the specified name to null if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setNullFieldIfAbsent(final String fieldName)
	{
		return setFieldIfAbsent(fieldName, JSONNullValue.NULL);
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
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final boolean value)
	{
		return hasValue(asVal(value));
	}
	
	/**
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final Boolean value)
	{
		return hasValue(asVal(value));
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
		return setField(fieldName, asVal(value));
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
		return setField(fieldName, asVal(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final boolean value)
	{
		return setFieldIfAbsent(fieldName, asVal(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final Boolean value)
	{
		return setFieldIfAbsent(fieldName, asVal(value));
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
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final double value)
	{
		return hasValue(new JSONNumberValue(value));
	}
	
	/**
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final Number value)
	{
		return hasValue(new JSONNumberValue(value));
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
	public boolean setField(final String fieldName, final Number value)
	{
		return setField(fieldName, new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final double value)
	{
		return setFieldIfAbsent(fieldName, new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final Number value)
	{
		return setFieldIfAbsent(fieldName, new JSONNumberValue(value));
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
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final String value)
	{
		return hasValue(new JSONStringValue(value));
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
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final String value)
	{
		return setFieldIfAbsent(fieldName, new JSONStringValue(value));
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
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final JSONArray value)
	{
		return hasValue(new JSONArrayValue(value));
	}
	
	/**
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param <T> Type of array values
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public <T extends JSONValue> boolean hasValue(final T[] value)
	{
		return hasValue(new JSONArrayValue(value));
	}
	
	/**
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final Iterable<? extends JSONValue> value)
	{
		return hasValue(new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final JSONArray value)
	{
		return setField(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param <T> Type of new values
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public <T extends JSONValue> boolean setField(final String fieldName, final T[] value)
	{
		return setField(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final Iterable<? extends JSONValue> value)
	{
		return setField(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final JSONArray value)
	{
		return setFieldIfAbsent(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param <T> Type of new values
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public <T extends JSONValue> boolean setFieldIfAbsent(final String fieldName, final T[] value)
	{
		return setFieldIfAbsent(fieldName, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final Iterable<? extends JSONValue> value)
	{
		return setFieldIfAbsent(fieldName, new JSONArrayValue(value));
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
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final JSONObject value)
	{
		return hasValue(new JSONObjectValue(value));
	}
	
	/**
	 * Checks whether a field with the specified value exists in the object
	 *
	 * @param value Value of the field to find
	 * @return True if the field exists, false otherwise
	 * @since 1.1.0
	 */
	public boolean hasValue(final Map<String, ? extends JSONValue> value)
	{
		return hasValue(new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
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
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setField(final String fieldName, final Map<String, ? extends JSONValue> value)
	{
		return setField(fieldName, new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final JSONObject value)
	{
		return setFieldIfAbsent(fieldName, new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the field with the specified name if the field is absent
	 *
	 * @param fieldName Name of the field to set, null strings are not allowed
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 * @since 1.1.0
	 */
	public boolean setFieldIfAbsent(final String fieldName, final Map<String, ? extends JSONValue> value)
	{
		return setFieldIfAbsent(fieldName, new JSONObjectValue(value));
	}
}
