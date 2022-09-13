package org.json.easy.dom;

import org.json.easy.serialization.JSONType;

/**
 * Represents JSON array value
 * 
 * @since 1.0.0
 */
public class JSONArrayValue extends JSONValue
{	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contains the value
	 */
	protected final JSONArray value;
	
	/**
	 * Creates new array value
	 * 
	 * @param val Value to set
	 */
	public JSONArrayValue(final JSONArray val)
	{
		type = JSONType.ARRAY;
		value = val == null ? JSONArray.EMPTY : val;
	}
	
	/**
	 * Creates new array value
	 * 
	 * @param <T> Type of values in array
	 * @param val Value to set
	 */
	public <T extends JSONValue> JSONArrayValue(final T[] val)
	{
		this(new JSONArray(val));
	}
	
	/**
	 * Creates new array value
	 * 
	 * @param val Value to set
	 */
	public JSONArrayValue(final Iterable<? extends JSONValue> val)
	{
		this(new JSONArray(val));
	}
	
	/**
	 * Creates new array value
	 * 
	 * @param val Value to set
	 */
	public JSONArrayValue(final JSONValue val)
	{
		this(val == null ? null : val.asArray());
	}
	
	/**
	 * Generates a hash code for this object
	 * 
	 * @return Hash code for this object
	 */
	@Override
	public int hashCode()
	{
		return value.hashCode();
	}
	
	/**
	 * Converts this object into a string
	 * 
	 * @return Human readable string representation of this object
	 */
	@Override
	public String toString()
	{
		return value.toString();
	}
	
	/**
	 * Retrieves this value as an array
	 * 
	 * @return This value as an array or empty array if not possible
	 */
	@Override
	public JSONArray asArray()
	{
		return value;
	}
}
