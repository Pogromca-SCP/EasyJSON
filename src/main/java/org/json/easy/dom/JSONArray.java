package org.json.easy.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents JSON array
 * 
 * @since 1.0.0
 */
public class JSONArray
{
	/**
	 * Contains a reference to global empty object
	 */
	public static final JSONArray EMPTY = new JSONArray();
	
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
		if (!(obj instanceof JSONArray))
		{
			return false;
		}
		
		final JSONArray tmp = (JSONArray) obj;
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
	public final JSONValue[] copyToArray()
	{
		final JSONValue[] tmp = new JSONValue[values.size()];
		return values.toArray(tmp);
	}
	
	/**
	 * Creates a copy of this array and returns it as a list
	 * 
	 * @return Copy of this array as a list
	 */
	public final List<JSONValue> copyToList()
	{
		return new ArrayList<JSONValue>(values);
	}
	
	/**
	 * Attempts to get the element on the specified index
	 *
	 * @param index Index of the element to get
	 * @return Element value, or null JSON value if the index is out of bounds
	 */
	public final JSONValue getElement(final int index)
	{
		if (index < 0 || index >= values.size())
		{
			return JSONNullValue.NULL;
		}
		
		return values.get(index);
	}
	
	/**
	 * Attempts to get the element on the specified index
	 *
	 * @param index Index of the element to get
	 * @return Element value, or null JSON value if the index is out of bounds
	 */
	public JSONValue getElement(final Integer index)
	{
		return getElement(index == null ? 0 : index);
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set, null will be converted to JSON null value
	 * @return True if set successfully, false otherwise
	 */
	public final boolean setElement(final int index, final JSONValue value)
	{
		if (index < 0 || index >= values.size() || this == EMPTY)
		{
			return false;
		}
		
		values.set(index, value == null ? JSONNullValue.NULL : value);
		return true;
	}
}
