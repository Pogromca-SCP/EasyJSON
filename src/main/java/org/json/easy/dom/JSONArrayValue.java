package org.json.easy.dom;

import org.json.easy.serialization.JSONType;
import java.util.List;
import java.util.Arrays;

/**
 * Represents JSON array value
 * 
 * @since 1.0.0
 */
public class JSONArrayValue extends JSONValue
{
	/**
	 * Contains a reference to global empty array
	 */
	public static final JSONValue[] EMPTY = new JSONValue[0];
	
	/**
	 * Contains the value
	 */
	protected final JSONValue[] value;
	
	/**
	 * Creates new array value
	 * 
	 * @param val Value to set
	 */
	public JSONArrayValue(final JSONValue[] val)
	{
		type = JSONType.ARRAY;
		value = val == null ? EMPTY : val;
	}
	
	/**
	 * Creates new array value
	 * 
	 * @param val Value to set
	 */
	public JSONArrayValue(final List<JSONValue> val)
	{
		this(val == null ? null : new JSONValue[val.size()]);
		
		if (value.length > 0)
		{
			val.toArray(value);
		}
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
