package org.json.easy.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
	 * Checks if all elements meet the condition
	 * 
	 * @param pred Condition to test
	 * @return True if all elements met the condition, false otherwise
	 */
	public final boolean checkAll(final Predicate<JSONValue> pred)
	{
		if (pred != null)
		{
			for (final JSONValue val : values)
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
		return getElement(index == null ? 0 : index.intValue());
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
		return isIndexValid(index == null ? 0 : index.intValue());
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add, null will be converted to JSON null value. Recursive values are not allowed
	 * @return True if added successfully, false otherwise
	 */
	public final boolean addElement(final JSONValue value)
	{
		if (this == EMPTY || (value != null && !isSafeToAdd(value)))
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
		if (!isIndexValid(index) || this == EMPTY || (value != null && !isSafeToAdd(value)))
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
		return setElement(index == null ? 0 : index.intValue(), value);
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
		removeElement(index == null ? 0 : index.intValue());
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
		return setElement(index == null ? 0 : index.intValue(), JSONNullValue.NULL);
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
		return getElement(index == null ? 0 : index.intValue()).asBoolean();
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final boolean value)
	{
		return addElement(value ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE);
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set, null will be converted to JSON null value. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final boolean value)
	{
		return setElement(index, value ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE);
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set, null will be converted to JSON null value. Recursive values are not allowed
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final boolean value)
	{
		return setElement(index == null ? 0 : index.intValue(), value ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE);
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
		return obj.checkAll(val -> isSafeToAdd(val));
	}
	
	/**
	 * Checks if an array can be added
	 * 
	 * @param arr Array to check
	 * @return True if array can be added, false otherwise
	 */
	private boolean checkArray(final JSONArray arr)
	{
		if (arr == this)
		{
			return false;
		}
		
		for (final JSONValue val : arr.values)
		{
			if (!isSafeToAdd(val))
			{
				return false;
			}
		}
		
		return true;
	}
}
