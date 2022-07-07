package org.json.easy.dom;

import org.json.easy.serialization.JSONType;

/**
 * Represents JSON null value
 * 
 * @since 1.0.0
 */
public class JSONNullValue extends JSONValue
{
	/**
	 * Contains a reference to global null object
	 */
	public static final JSONNullValue NULL = new JSONNullValue();
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates new null value
	 */
	public JSONNullValue()
	{
		type = JSONType.NULL;
	}
	
	/**
	 * Generates a hash code for this object
	 * 
	 * @return Hash code for this object
	 */
	@Override
	public int hashCode()
	{
		return 0;
	}
	
	/**
	 * Converts this object into a string
	 * 
	 * @return Human readable string representation of this object
	 */
	@Override
	public String toString()
	{
		return "null";
	}
}
