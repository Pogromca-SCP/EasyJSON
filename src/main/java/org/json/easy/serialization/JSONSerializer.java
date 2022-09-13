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
			array = isArray ? new JSONArray() : null;
			object = isArray ? null : new JSONObject();
		}
	}
	
	/**
	 * Helper class for containing element state
	 */
	private static class Element
	{
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
		 * @param <T> Type of values in array
		 * @param val Value to set
		 */
		public <T extends JSONValue> Element(final T[] val)
		{
			this(null, new JSONArrayValue(val));
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(final Iterable<? extends JSONValue> val)
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
		public Element(final Map<String, ? extends JSONValue> val)
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
	public static boolean serialize(final Iterable<? extends JSONValue> col, final JSONWriter writer)
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
	 * @param <T> Type of values in array
	 * @param array Array to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static <T extends JSONValue> boolean serialize(final T[] array, final JSONWriter writer)
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
	public static boolean serialize(final Map<String, ? extends JSONValue> map, final JSONWriter writer)
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
		
		while (reader.readNext())
		{
			String identifier = reader.getIdentifier();
			JSONValue newValue = null;
			
			switch (reader.getReadNotation())
			{
				case OBJECT_START:
				case ARRAY_START:
					if (currentState != null)
					{
						scopeStack.push(currentState);
					}
					
					currentState = new StackState(identifier, reader.getReadNotation() == JSONNotation.ARRAY_START);
					break;
				case OBJECT_END:
				case ARRAY_END:
					if (!scopeStack.isEmpty())
					{
						identifier = currentState.identifier;
						newValue = reader.getReadNotation() == JSONNotation.ARRAY_END ? new JSONArrayValue(currentState.array) : new JSONObjectValue(currentState.object);
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
			
			addNewValue(newValue, currentState, identifier);
		}
		
		final String error = reader.getErrorMessage();
		return (error != null && !error.isEmpty()) ? null : currentState;
	}
	
	/**
	 * Adds new value to current array or object
	 * 
	 * @param value Value to add
	 * @param state Current stack state to add into
	 * @param identifier Identifier for value added to the object
	 */
	private static void addNewValue(final JSONValue value, final StackState state, final String identifier)
	{
		if (value != null && state != null)
		{
			if (state.type == JSONType.OBJECT)
			{
				state.object.setField(identifier, value);
			}
			else
			{
				state.array.addElement(value);
			}
		}
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
					writeArray(elementStack, elem, writer);
					break;
				case OBJECT:
					writeObject(elementStack, elem, writer);
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
	 * Writes an array
	 * 
	 * @param stack Current serialization stack
	 * @param elem Element to write
	 * @param writer JSON writer to use
	 */
	private static void writeArray(final Stack<Element> stack, final Element elem, final JSONWriter writer)
	{
		if (elem.isProcessed)
		{
			writer.writeArrayEnd();
		}
		else if (canBeProcessed(stack, elem.value.asArray()))
		{
			elem.isProcessed = true;
			stack.push(elem);

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
				stack.push(new Element(values[i]));
			}
		}
	}
	
	/**
	 * Writes an object
	 * 
	 * @param stack Current serialization stack
	 * @param elem Element to write
	 * @param writer JSON writer to use
	 */
	private static void writeObject(final Stack<Element> stack, final Element elem, final JSONWriter writer)
	{
		if (elem.isProcessed)
		{
			writer.writeObjectEnd();
		}
		else if (canBeProcessed(stack, elem.value.asObject()))
		{
			elem.isProcessed = true;
			stack.push(elem);
			
			if (elem.identifier == null)
			{
				writer.writeObjectStart();
			}
			else
			{
				writer.writeObjectStart(elem.identifier);
			}
			
			elem.value.asObject().forEach(ent -> stack.push(new Element(ent.getKey(), ent.getValue())));
		}
	}
	
	/**
	 * Checks if the array can be processed by serializer
	 * 
	 * @param stack Current serialization stack
	 * @param arr Array to check
	 * @return True if array can be processed, false otherwise
	 */
	private static boolean canBeProcessed(final Stack<Element> stack, final JSONArray arr)
	{
		for (final Element el : stack)
		{
			final JSONArray tmp = el.value.asArray();
			
			if (tmp != JSONArray.EMPTY && tmp == arr)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the object can be processed by serializer
	 * 
	 * @param stack Current serialization stack
	 * @param obj Object to check
	 * @return True if object can be processed, false otherwise
	 */
	private static boolean canBeProcessed(final Stack<Element> stack, final JSONObject obj)
	{	
		for (final Element el : stack)
		{
			final JSONObject tmp = el.value.asObject();
			
			if (tmp != JSONObject.EMPTY && tmp == obj)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * No instances allowed
	 */
	private JSONSerializer() {}
}
