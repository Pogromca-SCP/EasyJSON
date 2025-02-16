package org.json.easy.util;

import org.json.easy.dom.*;
import org.json.easy.policies.JSONPrintPolicy;
import java.io.StringWriter;
import org.json.easy.serialization.JSONWriter;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.Set;

import static org.json.easy.serialization.JSONSerializer.serialize;

/**
 * Helper class for creating JSON objects
 * 
 * @since 1.0.0
 */
public class JSONObjectBuilder
{
	/**
	 * Contains object data
	 */
	private final JSONObject object;
	
	/**
	 * Creates new object
	 */
	public JSONObjectBuilder()
	{
		object = new JSONObject();
	}
	
	/**
	 * Returns this JSON object as a JSON value
	 * 
	 * @return This object wrapped in a JSON value
	 */
	public final JSONObjectValue asValue()
	{
		return new JSONObjectValue(object);
	}
	
	/**
	 * Returns this JSON object
	 * 
	 * @return Reference to this object
	 */
	public final JSONObject asObject()
	{
		return object;
	}
	
	/**
	 * Returns this object size
	 * 
	 * @return Current amount of fields in this object
	 */
	public int size()
	{
		return object.size();
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
	 * Converts this object into a JSON string
	 * 
	 * @param policy JSON print policy to use
	 * @return JSON string representation of this object
	 */
	public final String toJSONString(final JSONPrintPolicy policy)
	{
		final StringWriter res = new StringWriter();
		
		try (JSONWriter writer = new JSONWriter(res, policy))
		{
			serialize(object, writer);
		}
		
		return res.toString();
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final JSONArrayBuilder value)
	{	
		object.setField(key, value == null ? null : value.asValue());
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final JSONArray value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param <T> Type of new values
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public <T extends JSONValue> JSONObjectBuilder set(final String key, final T[] value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final Iterable<? extends JSONValue> value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final JSONObjectBuilder value)
	{
		object.setField(key, value == null ? null : value.asValue());
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final JSONObject value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final Map<String, ? extends JSONValue> value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final String value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final Number value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final double value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final Boolean value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key, final boolean value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Sets the null value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @return Reference to this object to allow method chaining
	 */
	public JSONObjectBuilder set(final String key)
	{
		object.setNullField(key);
		return this;
	}
	
	/**
	 * Sets the value of the field with the specified name
	 * 
	 * @param key Name of the field to set, null string are not allowed
	 * @param value Value to set
	 * @return Reference to this object to allow method chaining
	 */
	public final JSONObjectBuilder set(final String key, final JSONValue value)
	{
		object.setField(key, value);
		return this;
	}
	
	/**
	 * Copies elements from provided object into this object
	 * 
	 * @param src Object to copy elements from
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONObjectBuilder copyAll(final JSONObjectBuilder src)
	{
		return src == null ? this : copyAll(src.asObject());
	}
	
	/**
	 * Copies elements from provided object into this object
	 * 
	 * @param src Object to copy elements from
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONObjectBuilder copyAll(final JSONObject src)
	{
		if (src != null)
		{
			src.forEach(ent -> object.setField(ent.getKey(), ent.getValue()));
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided map into this object
	 * 
	 * @param src Map to copy elements from
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONObjectBuilder copyAll(final Map<String, ? extends JSONValue> src)
	{
		if (src != null)
		{
			Set<? extends Map.Entry<String, ? extends JSONValue>> entries = src.entrySet();
			
			if (entries == null)
			{
				return this;
			}
			
			for (final Map.Entry<String, ? extends JSONValue> ent : entries)
			{
				if (ent != null)
				{
					object.setField(ent.getKey(), ent.getValue());
				}
			}
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided object into this object if condition is met
	 * 
	 * @param src Object to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 * @since 1.1.0
	 */
	public final JSONObjectBuilder copyIf(final JSONObjectBuilder src, final BiPredicate<String, JSONValue> pred)
	{
		return src == null ? this : copyIf(src.asObject(), pred);
	}
	
	/**
	 * Copies elements from provided object into this object if condition is met
	 * 
	 * @param src Object to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 */
	public final JSONObjectBuilder copyIf(final JSONObject src, final BiPredicate<String, JSONValue> pred)
	{
		if (src != null && pred != null)
		{
			src.forEach(ent -> {
				final String key = ent.getKey();
				final JSONValue val = ent.getValue();
				
				if (pred.test(key, val))
				{
					object.setField(key, val);
				}
			});
		}
		
		return this;
	}
	
	/**
	 * Copies elements from provided map into this object if condition is met
	 * 
	 * @param <T> Type of copied values
	 * @param src Map to copy elements from
	 * @param pred Condition that elements must meet in order to be copied
	 * @return Reference to this object to allow method chaining
	 */
	public final <T extends JSONValue> JSONObjectBuilder copyIf(final Map<String, T> src, final BiPredicate<String, T> pred)
	{
		if (src != null && pred != null)
		{
			Set<Map.Entry<String, T>> entries = src.entrySet();
			
			if (entries == null)
			{
				return this;
			}
			
			for (final Map.Entry<String, T> ent : entries)
			{
				if (ent != null)
				{
					final String key = ent.getKey();
					final T val = ent.getValue();
					
					if (pred.test(key, val))
					{
						object.setField(key, val);
					}
				}
			}
		}
		
		return this;
	}
}
