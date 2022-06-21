package org.json.easy.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.Map;

/**
 * Represents JSON array
 * 
 * @since 1.0.0
 */
public class JSONArray
{
	/**
	 * Contains a reference to global empty array
	 */
	public static final JSONArray EMPTY = new JSONArray();
	
	/**
	 * Converts a number object to an int value
	 * 
	 * @param num Number to convert
	 * @return Converted value or zero if number was null
	 */
	private static int asInt(final Number num)
	{
		return num == null ? 0 : num.intValue();
	}
	
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
		return (val != null && val) ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE;
	}
	
	/**
	 * Contains array values
	 */
	private final ArrayList<JSONValue> values;
	
	/**
	 * Creates new empty JSON array
	 */
	public JSONArray()
	{
		values = new ArrayList<JSONValue>();
	}
	
	/**
	 * Creates new JSON array based on an array
	 * 
	 * @param src Array to copy
	 */
	public JSONArray(final JSONValue[] src)
	{
		this();
		
		if (src != null)
		{
			for (final JSONValue val : src)
			{
				addElement(val);
			}
		}
	}
	
	/**
	 * Creates new JSON array based on an iterable object
	 * 
	 * @param src Iterable object to copy
	 */
	public JSONArray(final Iterable<JSONValue> src)
	{
		this();
		
		if (src != null)
		{
			for (final JSONValue val : src)
			{
				addElement(val);
			}
		}
	}
	
	/**
	 * Creates a copy of an JSON array
	 * 
	 * @param src Array to copy
	 */
	public JSONArray(final JSONArray src)
	{
		values = src == null ? new ArrayList<JSONValue>() : new ArrayList<JSONValue>(src.values);
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
		return obj instanceof JSONArray ? values.equals(((JSONArray) obj).values) : false;
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
	 * Returns this array size
	 * 
	 * @return Current amount of elements in this array
	 */
	public int size()
	{
		return values.size();
	}
	
	/**
	 * Creates a copy of this array and returns it as an array
	 * 
	 * @return Copy of this array as an array
	 */
	public final JSONValue[] toArray()
	{
		final JSONValue[] tmp = new JSONValue[values.size()];
		return values.toArray(tmp);
	}
	
	/**
	 * Creates a copy of this array and returns it as a list
	 * 
	 * @return Copy of this array as a list
	 */
	public final List<JSONValue> toList()
	{
		return new ArrayList<JSONValue>(values);
	}
	
	/**
	 * Performs an action on every element in this array
	 * 
	 * @param action Action to perform
	 */
	public final void forEach(final Consumer<JSONValue> action)
	{
		if (action != null)
		{
			for (final JSONValue val : values)
			{
				action.accept(val);
			}
		}
	}
	
	/**
	 * Attempts to get the element on the specified index
	 *
	 * @param index Index of the element to get
	 * @return Element value, or null JSON value if the index is out of bounds
	 */
	public final JSONValue getElement(final int index)
	{
		if (!isIndexValid(index))
		{
			return JSONNullValue.NULL;
		}
		
		return values.get(index);
	}
	
	/**
	 * Attempts to get the element on the specified index
	 *
	 * @param index Index of the element to get, null is interpreted as 0
	 * @return Element value, or null JSON value if the index is out of bounds
	 */
	public JSONValue getElement(final Number index)
	{
		return getElement(asInt(index));
	}
	
	/**
	 * Checks if the index is valid
	 * 
	 * @param index Index to check
	 * @return True if index is valid, false otherwise
	 */
	public final boolean isIndexValid(final int index)
	{
		return index >= 0 && index < values.size();
	}
	
	/**
	 * Checks if the index is valid
	 * 
	 * @param index Index to check, null is interpreted as 0
	 * @return True if index is valid, false otherwise
	 */
	public boolean isIndexValid(final Number index)
	{
		return isIndexValid(asInt(index));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add, null will be converted to JSON null value. Recursive values are not allowed
	 * @return True if added successfully, false otherwise
	 */
	public final boolean addElement(final JSONValue value)
	{
		if (this == EMPTY)
		{
			return false;
		}
		
		return values.add(value == null ? JSONNullValue.NULL : value);
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set, null will be converted to JSON null value. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public final boolean setElement(final int index, final JSONValue value)
	{
		if (!isIndexValid(index) || this == EMPTY)
		{
			return false;
		}
		
		values.set(index, value == null ? JSONNullValue.NULL : value);
		return true;
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set, null will be converted to JSON null value. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final JSONValue value)
	{
		return setElement(asInt(index), value);
	}
	
	/**
	 * Removes the element on the specified index
	 *
	 * @param index Index of the element to remove
	 */
	public final void removeElement(final int index)
	{
		if (isIndexValid(index))
		{
			values.remove(index);
		}
	}
	
	/**
	 * Removes the element on the specified index
	 *
	 * @param index Index of the element to remove, null is interpreted as 0
	 */
	public void removeElement(final Number index)
	{
		removeElement(asInt(index));
	}
	
	/**
	 * Removes all elements from this array
	 */
	public final void clear()
	{
		values.clear();
	}
	
	/**
	 * Adds new null element to this array
	 * 
	 * @return True if added successfully, false otherwise
	 */
	public boolean addNullElement()
	{
		return addElement(JSONNullValue.NULL);
	}
	
	/**
	 * Sets the value of the element on the specified index to null
	 *
	 * @param index Index of the element to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setNullElement(final int index)
	{
		return setElement(index, JSONNullValue.NULL);
	}
	
	/**
	 * Sets the value of the element on the specified index to null
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @return True if set successfully, false otherwise
	 */
	public boolean setNullElement(final Number index)
	{
		return setElement(asInt(index), JSONNullValue.NULL);
	}
	
	/**
	 * Attempts to get the element on the specified index as a boolean
	 *
	 * @param index Index of the element to get
	 * @return Element value, or false if the index is out of bounds
	 */
	public boolean getBooleanElement(final int index)
	{
		return getElement(index).asBoolean();
	}
	
	/**
	 * Attempts to get the element on the specified index as a boolean
	 *
	 * @param index Index of the element to get
	 * @return Element value, or false if the index is out of bounds
	 */
	public boolean getBooleanElement(final Number index)
	{
		return getElement(asInt(index)).asBoolean();
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final boolean value)
	{
		return addElement(asVal(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final Boolean value)
	{
		return addElement(asVal(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final boolean value)
	{
		return setElement(index, asVal(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final Boolean value)
	{
		return setElement(index, asVal(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final boolean value)
	{
		return setElement(asInt(index), asVal(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final Boolean value)
	{
		return setElement(asInt(index), asVal(value));
	}
	
	/**
	 * Attempts to get the element on the specified index as a number
	 *
	 * @param index Index of the element to get
	 * @return Element value, or zero if the index is out of bounds
	 */
	public double getNumberElement(final int index)
	{
		return getElement(index).asNumber();
	}
	
	/**
	 * Attempts to get the element on the specified index as a number
	 *
	 * @param index Index of the element to get
	 * @return Element value, or zero if the index is out of bounds
	 */
	public double getNumberElement(final Number index)
	{
		return getElement(asInt(index)).asNumber();
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final double value)
	{
		return addElement(new JSONNumberValue(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final Number value)
	{
		return addElement(new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final double value)
	{
		return setElement(index, new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final Number value)
	{
		return setElement(index, new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final double value)
	{
		return setElement(asInt(index), new JSONNumberValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final Number value)
	{
		return setElement(asInt(index), new JSONNumberValue(value));
	}
	
	/**
	 * Attempts to get the element on the specified index as a string
	 *
	 * @param index Index of the element to get
	 * @return Element value, or empty string if the index is out of bounds
	 */
	public String getStringElement(final int index)
	{
		final String tmp = getElement(index).asString();
		return tmp == null ? "" : tmp;
	}
	
	/**
	 * Attempts to get the element on the specified index as a string
	 *
	 * @param index Index of the element to get
	 * @return Element value, or empty string if the index is out of bounds
	 */
	public String getStringElement(final Number index)
	{
		final String tmp = getElement(asInt(index)).asString();
		return tmp == null ? "" : tmp;
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final String value)
	{
		return addElement(new JSONStringValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final String value)
	{
		return setElement(index, new JSONStringValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final String value)
	{
		return setElement(asInt(index), new JSONStringValue(value));
	}
	
	/**
	 * Attempts to get the element on the specified index as an array
	 *
	 * @param index Index of the element to get
	 * @return Element value, or empty array if the index is out of bounds
	 */
	public JSONArray getArrayElement(final int index)
	{
		final JSONArray tmp = getElement(index).asArray();
		return tmp == null ? EMPTY : tmp;
	}
	
	/**
	 * Attempts to get the element on the specified index as an array
	 *
	 * @param index Index of the element to get
	 * @return Element value, or empty array if the index is out of bounds
	 */
	public JSONArray getArrayElement(final Number index)
	{
		final JSONArray tmp = getElement(asInt(index)).asArray();
		return tmp == null ? EMPTY : tmp;
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add. Recursive values are not allowed
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final JSONArray value)
	{
		return addElement(new JSONArrayValue(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add. Recursive values are not allowed
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final JSONValue[] value)
	{
		return addElement(new JSONArrayValue(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add. Recursive values are not allowed
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final Iterable<JSONValue> value)
	{
		return addElement(new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final JSONArray value)
	{
		return setElement(index, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final JSONValue[] value)
	{
		return setElement(index, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final Iterable<JSONValue> value)
	{
		return setElement(index, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final JSONArray value)
	{
		return setElement(asInt(index), new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final JSONValue[] value)
	{
		return setElement(asInt(index), new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final Iterable<JSONValue> value)
	{
		return setElement(asInt(index), new JSONArrayValue(value));
	}
	
	/**
	 * Attempts to get the element on the specified index as an object
	 *
	 * @param index Index of the element to get
	 * @return Element value, or empty object if the index is out of bounds
	 */
	public JSONObject getObjectElement(final int index)
	{
		final JSONObject tmp = getElement(index).asObject();
		return tmp == null ? JSONObject.EMPTY : tmp;
	}
	
	/**
	 * Attempts to get the element on the specified index as an object
	 *
	 * @param index Index of the element to get
	 * @return Element value, or empty object if the index is out of bounds
	 */
	public JSONObject getObjectElement(final Number index)
	{
		final JSONObject tmp = getElement(asInt(index)).asObject();
		return tmp == null ? JSONObject.EMPTY : tmp;
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add. Recursive values are not allowed
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final JSONObject value)
	{
		return addElement(new JSONObjectValue(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add. Recursive values are not allowed
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final Map<String, JSONValue> value)
	{
		return addElement(new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final JSONObject value)
	{
		return setElement(index, new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final Map<String, JSONValue> value)
	{
		return setElement(index, new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final JSONObject value)
	{
		return setElement(asInt(index), new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final Map<String, JSONValue> value)
	{
		return setElement(asInt(index), new JSONObjectValue(value));
	}
}
