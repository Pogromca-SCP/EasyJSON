package org.json.easy.util;

import org.json.easy.dom.*;
import org.json.easy.policies.JSONPrintPolicy;
import java.io.StringWriter;
import org.json.easy.serialization.JSONWriter;
import java.util.Map;
import java.util.function.Predicate;

import static org.json.easy.serialization.JSONSerializer.serialize;

/**
 * Helper class for creating JSON arrays
 * 
 * @since 1.0.0
 */
public class JSONArrayBuilder
{
	/**
	 * Contains array data
	 */
	private final JSONArray array;
	
	/**
	 * Creates new array
	 */
	public JSONArrayBuilder()
	{
		array = new JSONArray();
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
	 * Returns this JSON array
	 * 
	 * @return Reference to this array
	 */
	public final JSONArray asArray()
	{
		return array;
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
		array.addElement(value == null ? null : value.asValue());
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final JSONArray value)
	{
		array.addElement(value);
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param <T> Type of new values
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public <T extends JSONValue> JSONArrayBuilder add(final T[] value)
	{
		array.addElement(value);
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final Iterable<? extends JSONValue> value)
	{
		array.addElement(value);
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
		array.addElement(value == null ? null : value.asValue());
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
		array.addElement(value);
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final Map<String, ? extends JSONValue> value)
	{
		array.addElement(value);
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
		array.addElement(value);
		return this;
	}
	
	/**
	 * Adds a new value into this array
	 * 
	 * @param value Value to add
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add(final Number value)
	{
		array.addElement(value);
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
		array.addElement(value);
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
		array.addElement(value);
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
		array.addElement(value);
		return this;
	}
	
	/**
	 * Adds a new null value into this array
	 * 
	 * @return Reference to this object to allow method chaining
	 */
	public JSONArrayBuilder add()
	{
		array.addNullElement();
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
		array.addElement(value);
		return this;
	}
	
	/**
	 * Copies elements from provided array into this array
	 * 
	 * @param src Array to copy elements from
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONArrayBuilder copyAll(final JSONArrayBuilder src)
	{
		return src == null ? this : copyAll(src.asArray());
	}
	
	/**
	 * Copies elements from provided array into this array
	 * 
	 * @param src Array to copy elements from
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONArrayBuilder copyAll(final JSONArray src)
	{
		if (src != null)
		{
			src.forEach(val -> array.addElement(val));
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided collection into this array
	 * 
	 * @param src Collection to copy elements from
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONArrayBuilder copyAll(final Iterable<? extends JSONValue> src)
	{
		if (src != null)
		{
			for (final JSONValue val : src)
			{
				array.addElement(val);
			}
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided array into this array
	 * 
	 * @param <T> Type of copied values
	 * @param src Array to copy elements from
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final <T extends JSONValue> JSONArrayBuilder copyAll(final T[] src)
	{
		if (src != null)
		{
			for (final JSONValue val : src)
			{
				array.addElement(val);
			}
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided array into this array if condition is met
	 * 
	 * @param src Array to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONArrayBuilder copyIf(final JSONArrayBuilder src, final Predicate<JSONValue> pred)
	{
		return src == null ? this : copyIf(src.asArray(), pred);
	}
	
	/**
	 * Copies elements from provided array into this array if condition is met
	 * 
	 * @param src Array to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 */
	public final JSONArrayBuilder copyIf(final JSONArray src, final Predicate<JSONValue> pred)
	{
		if (src != null && pred != null)
		{
			src.forEach(val -> {
				if (pred.test(val))
				{
					array.addElement(val);
				}
			});
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided collection into this array if condition is met
	 * 
	 * @param <T> Type of copied values
	 * @param src Collection to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 */
	public final <T extends JSONValue> JSONArrayBuilder copyIf(final Iterable<T> src, final Predicate<T> pred)
	{
		if (src != null && pred != null)
		{
			for (final T val : src)
			{
				if (pred.test(val))
				{
					array.addElement(val);
				}
			}
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided array into this array if condition is met
	 * 
	 * @param <T> Type of copied values
	 * @param src Array to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 */
	public final <T extends JSONValue> JSONArrayBuilder copyIf(final T[] src, final Predicate<T> pred)
	{
		if (src != null && pred != null)
		{
			for (final T val : src)
			{
				if (pred.test(val))
				{
					array.addElement(val);
				}
			}
		}
		
		return this;
	}
}
