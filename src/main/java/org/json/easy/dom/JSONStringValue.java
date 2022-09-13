package org.json.easy.dom;

import org.json.easy.serialization.JSONType;

/**
 * Represents JSON string value
 * 
 * @since 1.0.0
 */
public class JSONStringValue extends JSONValue
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contains the value
	 */
	protected final String value;
	
	/**
	 * Creates new string value
	 * 
	 * @param val Value to set
	 */
	public JSONStringValue(final String val)
	{
		type = JSONType.STRING;
		value = val == null ? "" : val;
	}
	
	/**
	 * Creates new string value
	 * 
	 * @param val Value to set
	 */
	public JSONStringValue(final JSONValue val)
	{
		this(val == null ? null : val.asString());
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
		return value;
	}
	
	/**
	 * Retrieves this value as a boolean
	 * 
	 * @return This value as a boolean or false if not possible
	 */
	@Override
	public boolean asBoolean()
	{
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * Retrieves this value as a number
	 * 
	 * @return This value as a number or zero if not possible
	 */
	@Override
	public double asNumber()
	{
		try
		{
			return Double.parseDouble(value);
		}
		catch (NumberFormatException n)
		{
			return 0.0;
		}
	}
	
	/**
	 * Retrieves this value as a string
	 * 
	 * @return This value as a string or empty string if not possible
	 */
	@Override
	public String asString()
	{
		return value;
	}
}
