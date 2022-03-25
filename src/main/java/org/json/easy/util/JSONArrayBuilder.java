package org.json.easy.util;

import java.util.LinkedList;
import org.json.easy.dom.*;
import org.json.easy.policies.JSONPrintPolicy;
import java.io.StringWriter;
import org.json.easy.serialization.JSONWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.json.easy.serialization.JSONSerializer.serialize;

/**
 * Helper class for creating JSON arrays
 * 
 * @since 1.1.0
 */
public class JSONArrayBuilder
{
	/**
	 * Contains array data
	 */
	private final LinkedList<JSONValue> array;
	
	/**
	 * Creates new array
	 */
	public JSONArrayBuilder()
	{
		array = new LinkedList<JSONValue>();
	}
	
	/**
	 * Returns this JSON array as a JSON value
	 * 
	 * @return This array wrapped in a JSON value
	 */
	public final JSONArrayValue asValue()
	{
		return new JSONArrayValue(array);
	}
	
	/**
	 * Returns this array size
	 * 
	 * @return Current amount of elements in this array
	 */
	public int size()
	{
		return array.size();
	}
	
	/**
	 * Converts this object into a string
	 * 
	 * @return Human readable string representation of this object
	 */
	@Override
	public String toString()
	{
		return toJSONString(null);
	}
	
	/**
	 * Converts this array into a JSON string
	 * 
	 * @param policy JSON print policy to use
	 * @return JSON string representation of this array
	 */
	public final String toJSONString(final JSONPrintPolicy policy)
	{
		final StringWriter res = new StringWriter();
		
		try (JSONWriter writer = new JSONWriter(res, policy))
		{
			serialize(array, writer);
		}
		
		return res.toString();
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final JSONArrayBuilder value)
	{
		array.add(value == null ? null : value.asValue());
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final JSONValue[] value)
	{
		array.add(new JSONArrayValue(value));
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final List<JSONValue> value)
	{
		array.add(new JSONArrayValue(value));
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final JSONObjectBuilder value)
	{
		array.add(value == null ? null : value.asValue());
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final JSONObject value)
	{
		array.add(new JSONObjectValue(value));
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final Map<String, JSONValue> value)
	{
		array.add(new JSONObjectValue(value));
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final String value)
	{
		array.add(new JSONStringValue(value));
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final Double value)
	{
		array.add(new JSONNumberValue(value));
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final double value)
	{
		array.add(new JSONNumberValue(value));
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final Boolean value)
	{
		array.add(value == null || !value ? JSONBooleanValue.FALSE : JSONBooleanValue.TRUE);
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final boolean value)
	{
		array.add(value ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE);
		return this;
	}
	
	/**
	 * Adds a new null value into this array
	 * 
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add()
	{
		array.add(JSONNullValue.NULL);
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public final JSONArrayBuilder add(final JSONValue value)
	{
		array.add(value);
		return this;
	}
	
	/**
	 * Copies elements from provided list into this array if condition is met
	 * 
	 * @param src List to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 */
	public final JSONArrayBuilder copyIf(final List<JSONValue> src, final Predicate<JSONValue> pred)
	{
		if (src != null && pred != null)
		{
			for (final JSONValue val : src)
			{
				if (pred.test(val))
				{
					array.add(val);
				}
			}
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided list into this array if condition is met
	 * 
	 * @param src List to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 */
	public final JSONArrayBuilder copyIf(final JSONValue[] src, final Predicate<JSONValue> pred)
	{
		if (src != null && pred != null)
		{
			for (final JSONValue val : src)
			{
				if (pred.test(val))
				{
					array.add(val);
				}
			}
		}
		
		return this;
	}
}
