package org.json.easy.util;

import org.json.easy.dom.*;
import org.json.easy.policies.JSONPrintPolicy;
import java.io.StringWriter;
import org.json.easy.serialization.JSONWriter;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.Set;
import java.util.LinkedList;
import java.util.function.Predicate;

import static org.json.easy.serialization.JSONSerializer.serialize;

/**
 * Helper class for creating JSON trees
 * 
 * @since 1.1.0
 */
public final class JSONDOMBuilder
{
	/**
	 * Handler class for objects
	 */
	public static class Object
	{
		/**
		 * Contains object data
		 */
		private final JSONObject object;
		
		/**
		 * Creates new object
		 */
		public Object()
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
		public Object set(final String key, final Array value)
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
		public Object set(final String key, final JSONValue[] value)
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
		public Object set(final String key, final List<JSONValue> value)
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
		public Object set(final String key, final Object value)
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
		public Object set(final String key, final JSONObject value)
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
		public Object set(final String key, final Map<String, JSONValue> value)
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
		public Object set(final String key, final String value)
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
		public Object set(final String key, final Double value)
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
		public Object set(final String key, final double value)
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
		public Object set(final String key, final Boolean value)
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
		public Object set(final String key, final boolean value)
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
		public Object set(final String key)
		{
			object.setField(key, JSONNullValue.NULL);
			return this;
		}
		
		/**
		 * Sets the value of the field with the specified name
		 * 
		 * @param key Name of the field to set, null string are not allowed
		 * @param value Value to set
		 * @return Reference to this object to allow method chaining
		 */
		public final Object set(final String key, final JSONValue value)
		{
			object.setField(key, value);
			return this;
		}
		
		/**
		 * Copies elements from provided map into this object if condition is met
		 * 
		 * @param src Map to copy elements from
		 * @param pred Condition that elements must meet in order to be copied
		 * @return Reference to this object to allow method chaining
		 */
		public final Object copyIf(final Map<String, JSONValue> src, final BiPredicate<String, JSONValue> pred)
		{
			if (src != null && pred != null)
			{
				Set<Map.Entry<String, JSONValue>> entries = src.entrySet();
				
				if (entries == null)
				{
					return this;
				}
				
				for (final Map.Entry<String, JSONValue> ent : entries)
				{
					if (ent != null)
					{
						final String key = ent.getKey();
						final JSONValue val = ent.getValue();
						
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
	
	/**
	 * Handler class for arrays
	 */
	public static class Array
	{
		/**
		 * Contains array data
		 */
		private final LinkedList<JSONValue> array;
		
		/**
		 * Creates new array
		 */
		public Array()
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
		public Array add(final Array value)
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
		public Array add(final JSONValue[] value)
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
		public Array add(final List<JSONValue> value)
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
		public Array add(final Object value)
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
		public Array add(final JSONObject value)
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
		public Array add(final Map<String, JSONValue> value)
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
		public Array add(final String value)
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
		public Array add(final Double value)
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
		public Array add(final double value)
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
		public Array add(final Boolean value)
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
		public Array add(final boolean value)
		{
			array.add(value ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE);
			return this;
		}
		
		/**
		 * Adds a new null value into this array
		 * 
		 * @return Reference to this object to allow method chaining
		 */
		public Array add()
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
		public final Array add(final JSONValue value)
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
		public final Array copyIf(final List<JSONValue> src, final Predicate<JSONValue> pred)
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
		public final Array copyIf(final JSONValue[] src, final Predicate<JSONValue> pred)
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
	
	/**
	 * No instances allowed
	 */
	private JSONDOMBuilder() {}
}
