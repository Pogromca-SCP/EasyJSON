package org.json.easy.dom;

import org.json.easy.serialization.JSONType;
import java.util.Map;

/**
 * Represents JSON object value
 * 
 * @since 1.0.0
 */
public class JSONObjectValue extends JSONValue
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contains the value
	 */
	protected final JSONObject value;
	
	/**
	 * Creates new object value
	 * 
	 * @param val Value to set
	 */
	public JSONObjectValue(final JSONObject val)
	{
		type = JSONType.OBJECT;
		value = val == null ? JSONObject.EMPTY : val;
	}
	
	/**
	 * Creates new object value
	 * 
	 * @param val Value to set
	 */
	public JSONObjectValue(final Map<String, ? extends JSONValue> val)
	{
		this(new JSONObject(val));
	}
	
	/**
	 * Creates new object value
	 * 
	 * @param val Value to set
	 */
	public JSONObjectValue(final JSONValue val)
	{
		this(val == null ? null : val.asObject());
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
	 * Retrieves this value as an object
	 * 
	 * @return This value as an object or empty object if not possible
	 */
	@Override
	public JSONObject asObject()
	{
		return value;
	}
}
