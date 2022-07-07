package org.json.easy.dom;

import org.json.easy.serialization.JSONType;

/**
 * Represents JSON number value
 * 
 * @since 1.0.0
 */
public class JSONNumberValue extends JSONValue
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Contains the value
	 */
	protected final double value;
	
	/**
	 * Creates new number value
	 * 
	 * @param val Value to set
	 */
	public JSONNumberValue(final double val)
	{
		type = JSONType.NUMBER;
		value = val;
	}
	
	/**
	 * Creates new number value
	 * 
	 * @param val Value to set
	 */
	public JSONNumberValue(final Number val)
	{
		this(val == null ? 0.0 : val.doubleValue());
	}
	
	/**
	 * Generates a hash code for this object
	 * 
	 * @return Hash code for this object
	 */
	@Override
	public int hashCode()
	{
		return Double.hashCode(value);
	}
	
	/**
	 * Converts this object into a string
	 * 
	 * @return Human readable string representation of this object
	 */
	@Override
	public String toString()
	{
		return Double.toString(value);
	}
	
	/**
	 * Retrieves this value as a boolean
	 * 
	 * @return This value as a boolean or false if not possible
	 */
	@Override
	public boolean asBoolean()
	{
		return value != 0.0;
	}
	
	/**
	 * Retrieves this value as a number
	 * 
	 * @return This value as a number or zero if not possible
	 */
	@Override
	public double asNumber()
	{
		return value;
	}
	
	/**
	 * Retrieves this value as a string
	 * 
	 * @return This value as a string or empty string if not possible
	 */
	@Override
	public String asString()
	{
		return toString();
	}
}
