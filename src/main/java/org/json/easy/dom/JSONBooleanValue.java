package org.json.easy.dom;

import org.json.easy.serialization.JSONType;

/**
 * Represents JSON boolean value
 * 
 * @since 1.0.0
 */
public class JSONBooleanValue extends JSONValue
{
	/**
	 * Contains a reference to global true object
	 */
	public static final JSONBooleanValue TRUE = new JSONBooleanValue(true);
	
	/**
	 * Contains a reference to global false object
	 */
	public static final JSONBooleanValue FALSE = new JSONBooleanValue(false);
	
	/**
	 * Contains the value
	 */
	protected final boolean value;
	
	/**
	 * Creates new boolean value
	 * 
	 * @param val Value to set
	 */
	public JSONBooleanValue(boolean val)
	{
		type = JSONType.BOOLEAN;
		value = val;
	}
	
	/**
	 * Creates new boolean value
	 * 
	 * @param val Value to set
	 */
	public JSONBooleanValue(Boolean val)
	{
		this(val == null ? false : val);
	}
	
	/**
	 * Generates a hash code for this object
	 * 
	 * @return Hash code for this object
	 */
	@Override
	public int hashCode()
	{
		return Boolean.hashCode(value);
	}
	
	/**
	 * Converts this object into a string
	 * 
	 * @return Human readable string representation of this object
	 */
	@Override
	public String toString()
	{
		return Boolean.toString(value);
	}
	
	/**
	 * Retrieves this value as a boolean
	 * 
	 * @return This value as a boolean or false if not possible
	 */
	@Override
	public boolean asBoolean()
	{
		return value;
	}
	
	/**
	 * Retrieves this value as a number
	 * 
	 * @return This value as a number or zero if not possible
	 */
	@Override
	public double asNumber()
	{
		return value ? 1.0 : 0.0;
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
