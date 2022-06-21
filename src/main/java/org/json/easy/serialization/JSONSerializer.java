package org.json.easy.serialization;

import org.json.easy.dom.*;
import java.util.Map;
import java.util.Stack;

/**
 * Helper class for handling JSON serialization
 * 
 * @since 1.0.0
 */
public final class JSONSerializer
{
	/**
	 * Helper class for containing stack state
	 */
	private static class StackState
	{
		/**
		 * Creates new stack state
		 * 
		 * @param id Identifier to set
		 * @param isArray Whether or not this should be an array state
		 */
		public StackState(final String id, final boolean isArray)
		{
			identifier = id;
			type = isArray ? JSONType.ARRAY : JSONType.OBJECT;
			
			if (isArray)
			{
				array = new JSONArray();
				object = null;
			}
			else
			{
				object = new JSONObject();
				array = null;
			}
		}
		
		/**
		 * Contains stack state type
		 */
		public final JSONType type;
		
		/**
		 * Contains stack state identifier
		 */
		public final String identifier;
		
		/**
		 * Contains stack array value
		 */
		public final JSONArray array;
		
		/**
		 * Contains stack object value
		 */
		public final JSONObject object;
	}
	
	/**
	 * Helper class for containing element state
	 */
	private static class Element
	{
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(final JSONValue val)
		{
			this(null, val);
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(final JSONArray val)
		{
			this(null, new JSONArrayValue(val));
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(final JSONValue[] val)
		{
			this(null, new JSONArrayValue(val));
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(final Iterable<JSONValue> val)
		{
			this(null, new JSONArrayValue(val));
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(final JSONObject val)
		{
			this(null, new JSONObjectValue(val));
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(final Map<String, JSONValue> val)
		{
			this(null, new JSONObjectValue(val));
		}
		
		/**
		 * Creates new element with provided identifier and value
		 * 
		 * @param id Identifier to set
		 * @param val Value to set
		 */
		public Element(final String id, final JSONValue val)
		{
			identifier = id;
			value = val == null ? JSONNullValue.NULL : val;
			isProcessed = false;
		}
		
		/**
		 * Contains element identifier
		 */
		public final String identifier;
		
		/**
		 * Contains element value
		 */
		public final JSONValue value;
		
		/**
		 * Tells whether or not the element has been processed
		 */
		public boolean isProcessed;
	}
	
	/**
	 * Deserializes JSON data and returns it as an array
	 * 
	 * @param reader Reader to get data from
	 * @return Deserialized data as an array or empty array if something went wrong
	 */
	public static JSONArray deserializeArray(final JSONReader reader)
	{
		if (reader == null)
		{
			return JSONArray.EMPTY;
		}
		
		final StackState state = deserializeJSON(reader);
		
		if (state == null)
		{
			return JSONArray.EMPTY;
		}
		
		final JSONArray res = state.array;
		return res == null ? JSONArray.EMPTY : res;
	}
	
	/**
	 * Deserializes JSON data and returns it as an object
	 * 
	 * @param reader Reader to get data from
	 * @return Deserialized data as a JSON object or empty JSON object if something went wrong
	 */
	public static JSONObject deserializeObject(final JSONReader reader)
	{
		if (reader == null)
		{
			return JSONObject.EMPTY;
		}
		
		final StackState state = deserializeJSON(reader);
		
		if (state == null)
		{
			return JSONObject.EMPTY;
		}
		
		final JSONObject res = state.object;
		return res == null ? JSONObject.EMPTY : res;
	}
	
	/**
	 * Deserializes JSON data and returns it
	 * 
	 * @param reader Reader to get data from
	 * @return Deserialized data in JSON object/array or null JSON value if something went wrong
	 */
	public static JSONValue deserialize(final JSONReader reader)
	{
		if (reader == null)
		{
			return JSONNullValue.NULL;
		}
		
		final StackState state = deserializeJSON(reader);
		
		if (state == null)
		{
			return JSONNullValue.NULL;
		}

		switch (state.type)
		{
			case OBJECT:
				return new JSONObjectValue(state.object);
			case ARRAY:
				return new JSONArrayValue(state.array);
			default:
				return JSONNullValue.NULL;
		}
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param col Collection to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(final Iterable<JSONValue> col, final JSONWriter writer)
	{
		if (writer == null)
		{
			return false;
		}
		
		serialize(new Element(col), writer);
		return true;
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param array Array to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(final JSONArray array, final JSONWriter writer)
	{
		if (writer == null)
		{
			return false;
		}
		
		serialize(new Element(array), writer);
		return true;
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param array Array to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(final JSONValue[] array, final JSONWriter writer)
	{
		if (writer == null)
		{
			return false;
		}
		
		serialize(new Element(array), writer);
		return true;
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param map Map to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(final Map<String, JSONValue> map, final JSONWriter writer)
	{
		if (writer == null)
		{
			return false;
		}
		
		serialize(new Element(map), writer);
		return true;
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param object JSON object to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(final JSONObject object, final JSONWriter writer)
	{
		if (writer == null)
		{
			return false;
		}
		
		serialize(new Element(object), writer);
		return true;
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param value Value to serialize
	 * @param identifier Value identifier to set
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(final JSONValue value, final String identifier, final JSONWriter writer)
	{
		if (writer == null)
		{
			return false;
		}
		
		serialize(new Element(identifier, value), writer);
		return true;
	}
	
	/**
	 * Deserializes JSON data and writes it into a stack state
	 * 
	 * @param reader JSON reader to use
	 * @return Deserialized data in a stack state or null if something went wrong
	 */
	private static StackState deserializeJSON(final JSONReader reader)
	{
		final Stack<StackState> scopeStack = new Stack<StackState>();
		StackState currentState = null;
		JSONValue newValue;
		
		while (reader.readNext())
		{
			String identifier = reader.getIdentifier();
			newValue = null;
			
			switch (reader.getReadNotation())
			{
				case OBJECT_START:
					if (currentState != null)
					{
						scopeStack.push(currentState);
					}
					
					currentState = new StackState(identifier, false);
					break;
				case OBJECT_END:
					if (!scopeStack.isEmpty())
					{
						identifier = currentState.identifier;
						newValue = new JSONObjectValue(currentState.object);
						currentState = scopeStack.pop();
					}
					
					break;
				case ARRAY_START:
					if (currentState != null)
					{
						scopeStack.push(currentState);
					}
					
					currentState = new StackState(identifier, true);
					break;
				case ARRAY_END:
					if (!scopeStack.isEmpty())
					{
						identifier = currentState.identifier;
						newValue = new JSONArrayValue(currentState.array);
						currentState = scopeStack.pop();
					}
					
					break;
				case BOOLEAN:
					newValue = reader.getBooleanValue() ? JSONBooleanValue.TRUE : JSONBooleanValue.FALSE;
					break;
				case STRING:
					newValue = new JSONStringValue(reader.getStringValue());
					break;
				case NUMBER:
					newValue = new JSONNumberValue(reader.getNumberValue());
					break;
				case NULL:
					newValue = JSONNullValue.NULL;
					break;
				case ERROR:
					return null;
				default:
			}
			
			if (newValue != null && currentState != null)
			{
				if (currentState.type == JSONType.OBJECT)
				{
					currentState.object.setField(identifier, newValue);
				}
				else
				{
					currentState.array.addElement(newValue);
				}
			}
		}
		
		final String error = reader.getErrorMessage();
		
		if (error != null && !error.isEmpty())
		{
			return null;
		}
		
		return currentState;
	}
	
	/**
	 * Serializes JSON data from an element
	 * 
	 * @param element Element to serialize
	 * @param writer JSON writer to use
	 */
	private static void serialize(final Element element, final JSONWriter writer)
	{
		final Stack<Element> elementStack = new Stack<Element>();
		elementStack.push(element);
		
		while (!elementStack.isEmpty())
		{
			final Element elem = elementStack.pop();
			
			switch (elem.value.getType())
			{
				case ARRAY:
					if (elem.isProcessed)
					{
						writer.writeArrayEnd();
					}
					else
					{
						elem.isProcessed = true;
						elementStack.push(elem);

						if (elem.identifier == null)
						{
							writer.writeArrayStart();
						}
						else
						{
							writer.writeArrayStart(elem.identifier);
						}

						final JSONValue[] values = elem.value.asArray().toArray();

						for (int i = values.length - 1; i > -1; --i)
						{
							elementStack.push(new Element(values[i]));
						}
					}
					
					break;
				case OBJECT:
					if (elem.isProcessed)
					{
						writer.writeObjectEnd();
					}
					else
					{
						elem.isProcessed = true;
						elementStack.push(elem);
						
						if (elem.identifier == null)
						{
							writer.writeObjectStart();
						}
						else
						{
							writer.writeObjectStart(elem.identifier);
						}
						
						elem.value.asObject().forEach(ent -> elementStack.push(new Element(ent.getKey(), ent.getValue())));
					}
					
					break;
				default:
					if (elem.identifier == null)
					{
						writer.writeValue(elem.value);
					}
					else
					{
						writer.writeValue(elem.identifier, elem.value);
					}
			}
		}
	}
	
	/**
	 * No instances allowed
	 */
	private JSONSerializer() {}
}
