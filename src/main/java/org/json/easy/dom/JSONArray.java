package org.json.easy.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.Map;

/**
 * Represents JSON array
 * 
 * @since 1.0.0
 */
public class JSONArray implements Serializable
{
	/**
	 * Contains a reference to global empty array
	 */
	public static final JSONArray EMPTY = new JSONArray();
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
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
		return asVal(val == null ? false : val);
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
	 * @param <T> Type of values in array
	 * @param src Array to copy
	 */
	public <T extends JSONValue> JSONArray(final T[] src)
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
	public JSONArray(final Iterable<? extends JSONValue> src)
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
	 * Checks if this array is empty
	 * 
	 * @return True if array is empty, false otherwise
	 * @since 1.1.0
	 */
	public boolean isEmpty()
	{
		return values.isEmpty();
	}
	
	/**
	 * Creates a copy of this array and returns it as an array
	 * 
	 * @return Copy of this array as an array
	 */
	public final JSONValue[] toArray()
	{
		return values.toArray(new JSONValue[values.size()]);
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
	 * Removes all elements that met the provided condition
	 * 
	 * @param pred Condition that removed elements must meet
	 * @return True if anything was removed, false otherwise
	 * @since 1.1.0
	 */
	public final boolean removeIf(Predicate<JSONValue> pred)
	{
		return pred == null ? false : values.removeIf(pred);
	}
	
	/**
	 * Sorts this list according to the specified order
	 * 
	 * @param comp Comparator to use
	 * @since 1.1.0
	 */
	public final void sort(Comparator<JSONValue> comp)
	{
		if (comp != null)
		{
			values.sort(comp);
		}
	}
	
	/**
	 * Returns a portion of this array from the specified range
	 * 
	 * @param from Starting index of range (inclusive)
	 * @param to Ending index of range (exclusive)
	 * @return Portion of array generated from specified range or empty array if range is invalid
	 * @since 1.1.0
	 */
	public final JSONArray subArray(final int from, final int to)
	{
		final JSONArray res = new JSONArray();
		
		if (from >= 0 && to <= values.size() && from < to)
		{
			for (int i = from; i < to; ++i)
			{
				res.addElement(values.get(i));
			}
		}
		
		return res;
	}
	
	/**
	 * Returns a portion of this array from the specified range
	 * 
	 * @param from Starting index of range (inclusive)
	 * @param to Ending index of range (exclusive)
	 * @return Portion of array generated from specified range or empty array if range is invalid
	 * @since 1.1.0
	 */
	public JSONArray subArray(final Number from, final int to)
	{
		return subArray(asInt(from), to);
	}
	
	/**
	 * Returns a portion of this array from the specified range
	 * 
	 * @param from Starting index of range (inclusive)
	 * @param to Ending index of range (exclusive)
	 * @return Portion of array generated from specified range or empty array if range is invalid
	 * @since 1.1.0
	 */
	public JSONArray subArray(final int from, final Number to)
	{
		return subArray(from, asInt(to));
	}
	
	/**
	 * Returns a portion of this array from the specified range
	 * 
	 * @param from Starting index of range (inclusive)
	 * @param to Ending index of range (exclusive)
	 * @return Portion of array generated from specified range or empty array if range is invalid
	 * @since 1.1.0
	 */
	public JSONArray subArray(final Number from, final Number to)
	{
		return subArray(asInt(from), asInt(to));
	}
	
	/**
	 * Returns a sequential stream object for this array
	 * 
	 * @return Sequential stream over the elements in this array
	 * @since 1.1.0
	 */
	public final Stream<JSONValue> stream()
	{
		return values.stream();
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 */
	public final boolean contains(final JSONValue value)
	{
		return values.contains(value);
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
	 * Adds new element to this array
	 * 
	 * @param value Value to add, null will be converted to JSON null value
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
	 * @param value Value to set, null will be converted to JSON null value
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
	 * @param value Value to set, null will be converted to JSON null value
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final JSONValue value)
	{
		return setElement(asInt(index), value);
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public final int indexOf(final JSONValue value)
	{
		return values.indexOf(value);
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public final int lastIndexOf(final JSONValue value)
	{
		return values.lastIndexOf(value);
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public final boolean remove(final JSONValue value)
	{
		return values.remove(value);
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public final int removeAll(final JSONValue value)
	{
		int count = 0;
		
		while (values.remove(value))
		{
			++count;
		}
		
		return count;
	}
	
	/**
	 * Checks if the array contains null
	 * 
	 * @return True if array contains null, false otherwise
	 * @since 1.1.0
	 */
	public boolean containsNull()
	{
		return contains(JSONNullValue.NULL);
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
	 * Checks the index of the first occurrence of null
	 * 
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOfNull()
	{
		return indexOf(JSONNullValue.NULL);
	}
	
	/**
	 * Checks the index of the last occurrence null
	 * 
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOfNull()
	{
		return lastIndexOf(JSONNullValue.NULL);
	}
	
	/**
	 * Removes single occurrence of null from this array
	 * 
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean removeNull()
	{
		return remove(JSONNullValue.NULL);
	}
	
	/**
	 * Removes all occurrences of null from this array
	 * 
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAllNullValues()
	{
		return removeAll(JSONNullValue.NULL);
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final boolean value)
	{
		return contains(asVal(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final Boolean value)
	{
		return contains(asVal(value));
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
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final boolean value)
	{
		return indexOf(asVal(value));
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final Boolean value)
	{
		return indexOf(asVal(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final boolean value)
	{
		return lastIndexOf(asVal(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final Boolean value)
	{
		return lastIndexOf(asVal(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final boolean value)
	{
		return remove(asVal(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final Boolean value)
	{
		return remove(asVal(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final boolean value)
	{
		return removeAll(asVal(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final Boolean value)
	{
		return removeAll(asVal(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final double value)
	{
		return contains(new JSONNumberValue(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final Number value)
	{
		return contains(new JSONNumberValue(value));
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
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final double value)
	{
		return indexOf(new JSONNumberValue(value));
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final Number value)
	{
		return indexOf(new JSONNumberValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final double value)
	{
		return lastIndexOf(new JSONNumberValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final Number value)
	{
		return lastIndexOf(new JSONNumberValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final double value)
	{
		return remove(new JSONNumberValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final Number value)
	{
		return remove(new JSONNumberValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final double value)
	{
		return removeAll(new JSONNumberValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final Number value)
	{
		return removeAll(new JSONNumberValue(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final String value)
	{
		return contains(new JSONStringValue(value));
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
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final String value)
	{
		return indexOf(new JSONStringValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final String value)
	{
		return lastIndexOf(new JSONStringValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final String value)
	{
		return remove(new JSONStringValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final String value)
	{
		return removeAll(new JSONStringValue(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param <T> Type of array values
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public <T extends JSONValue> boolean contains(final T[] value)
	{
		return contains(new JSONArrayValue(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final Iterable<? extends JSONValue> value)
	{
		return contains(new JSONArrayValue(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final JSONArray value)
	{
		return contains(new JSONArrayValue(value));
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
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final JSONArray value)
	{
		return addElement(new JSONArrayValue(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param <T> Type of new values
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public <T extends JSONValue> boolean addElement(final T[] value)
	{
		return addElement(new JSONArrayValue(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final Iterable<? extends JSONValue> value)
	{
		return addElement(new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final JSONArray value)
	{
		return setElement(index, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 * 
	 * @param <T> Type of new values
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public <T extends JSONValue> boolean setElement(final int index, final T[] value)
	{
		return setElement(index, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final Iterable<? extends JSONValue> value)
	{
		return setElement(index, new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final JSONArray value)
	{
		return setElement(asInt(index), new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 * 
	 * @param <T> Type of new values
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public <T extends JSONValue> boolean setElement(final Number index, final T[] value)
	{
		return setElement(asInt(index), new JSONArrayValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final Iterable<? extends JSONValue> value)
	{
		return setElement(asInt(index), new JSONArrayValue(value));
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final JSONArray value)
	{
		return indexOf(new JSONArrayValue(value));
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param <T> Type of array values
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public <T extends JSONValue> int indexOf(final T[] value)
	{
		return indexOf(new JSONArrayValue(value));
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final Iterable<? extends JSONValue> value)
	{
		return indexOf(new JSONArrayValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final JSONArray value)
	{
		return lastIndexOf(new JSONArrayValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param <T> Type of array values
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public <T extends JSONValue> int lastIndexOf(final T[] value)
	{
		return lastIndexOf(new JSONArrayValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final Iterable<? extends JSONValue> value)
	{
		return lastIndexOf(new JSONArrayValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final JSONArray value)
	{
		return remove(new JSONArrayValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param <T> Type of array values
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public <T extends JSONValue> boolean remove(final T[] value)
	{
		return remove(new JSONArrayValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final Iterable<? extends JSONValue> value)
	{
		return remove(new JSONArrayValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final JSONArray value)
	{
		return removeAll(new JSONArrayValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param <T> Type of array values
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public <T extends JSONValue> int removeAll(final T[] value)
	{
		return removeAll(new JSONArrayValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final Iterable<? extends JSONValue> value)
	{
		return removeAll(new JSONArrayValue(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final JSONObject value)
	{
		return contains(new JSONObjectValue(value));
	}
	
	/**
	 * Checks if the array contains specific value
	 * 
	 * @param value Value to find
	 * @return True if array contains the value, false otherwise
	 * @since 1.1.0
	 */
	public boolean contains(final Map<String, ? extends JSONValue> value)
	{
		return contains(new JSONObjectValue(value));
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
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final JSONObject value)
	{
		return addElement(new JSONObjectValue(value));
	}
	
	/**
	 * Adds new element to this array
	 * 
	 * @param value Value to add
	 * @return True if added successfully, false otherwise
	 */
	public boolean addElement(final Map<String, ? extends JSONValue> value)
	{
		return addElement(new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set
	 * @param value Value to set
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
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final int index, final Map<String, ? extends JSONValue> value)
	{
		return setElement(index, new JSONObjectValue(value));
	}
	
	/**
	 * Sets the value of the element on the specified index
	 *
	 * @param index Index of the element to set, null is interpreted as 0
	 * @param value Value to set
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
	 * @param value Value to set
	 * @return True if set successfully, false otherwise
	 */
	public boolean setElement(final Number index, final Map<String, ? extends JSONValue> value)
	{
		return setElement(asInt(index), new JSONObjectValue(value));
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final JSONObject value)
	{
		return indexOf(new JSONObjectValue(value));
	}
	
	/**
	 * Checks the index of the first occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the first occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int indexOf(final Map<String, ? extends JSONValue> value)
	{
		return indexOf(new JSONObjectValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final JSONObject value)
	{
		return lastIndexOf(new JSONObjectValue(value));
	}
	
	/**
	 * Checks the index of the last occurrence of the specified value
	 * 
	 * @param value Value to check
	 * @return Index of the last occurrence, or -1 if not found
	 * @since 1.1.0
	 */
	public int lastIndexOf(final Map<String, ? extends JSONValue> value)
	{
		return lastIndexOf(new JSONObjectValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final JSONObject value)
	{
		return remove(new JSONObjectValue(value));
	}
	
	/**
	 * Removes single occurrence of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return True if value was removed, false otherwise
	 * @since 1.1.0
	 */
	public boolean remove(final Map<String, ? extends JSONValue> value)
	{
		return remove(new JSONObjectValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final JSONObject value)
	{
		return removeAll(new JSONObjectValue(value));
	}
	
	/**
	 * Removes all occurrences of specified value from this array
	 * 
	 * @param value Value to remove
	 * @return Amount of removed elements
	 * @since 1.1.0
	 */
	public int removeAll(final Map<String, ? extends JSONValue> value)
	{
		return removeAll(new JSONObjectValue(value));
	}
}
