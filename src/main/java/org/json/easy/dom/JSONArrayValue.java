package org.json.easy.dom;

import org.json.easy.serialization.JSONType;
import java.util.Arrays;

/**
 * Represents JSON array value
 */
public class JSONArrayValue extends JSONValue
{
	/**
	 * Contains a reference to global empty array
	 */
	private static JSONValue[] empty = null;
	
	/**
	 * Returns a reference to global empty array
	 * 
	 * @return Reference to global empty array
	 */
	public static JSONValue[] emptyArray()
	{
		if (empty == null)
		{
			empty = new JSONValue[0];
		}
		
		return empty;
	}
	
	/**
	 * Contains the value
	 */
	protected final JSONValue[] value;
	
	/**
	 * Creates new array value
	 * 
	 * @param val Value to set
	 */
	public JSONArrayValue(JSONValue[] val)
	{
		type = JSONType.ARRAY;
		value = val == null ? emptyArray() : val;
	}
	
	/**
	 * Generates a hash code for this object
	 * 
	 * @return Hash code for this object
	 */
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(value);
	}
	
	/**
	 * Converts this object into a string
	 * 
	 * @return Human readable string representation of this object
	 */
	@Override
	public String toString()
	{
		return Arrays.toString(value);
	}
	
	/**
	 * Retrieves this value as an array
	 * 
	 * @return This value as an array or empty array if not possible
	 */
	@Override
	public JSONValue[] asArray()
	{
		return value;
	}
}
