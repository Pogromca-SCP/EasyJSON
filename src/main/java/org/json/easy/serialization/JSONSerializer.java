package org.json.easy.serialization;

import org.json.easy.dom.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Map;

/**
 * Helper class for handling JSON serialization
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
		public StackState(String id, boolean isArray)
		{
			identifier = id;
			type = isArray ? JSONType.ARRAY : JSONType.OBJECT;
			
			if (isArray)
			{
				array = new LinkedList<JSONValue>();
			}
			else
			{
				object = new JSONObject();
			}
		}
		
		/**
		 * Contains stack state type
		 */
		public JSONType type;
		
		/**
		 * Contains stack state identifier
		 */
		public String identifier;
		
		/**
		 * Contains stack array value
		 */
		public LinkedList<JSONValue> array;
		
		/**
		 * Contains stack object value
		 */
		public JSONObject object;
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
		public Element(JSONValue val)
		{
			this(null, val);
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(JSONValue[] val)
		{
			this(null, new JSONArrayValue(val));
		}
		
		/**
		 * Creates new element with provided value
		 * 
		 * @param val Value to set
		 */
		public Element(JSONObject val)
		{
			this(null, new JSONObjectValue(val));
		}
		
		/**
		 * Creates new element with provided identifier and value
		 * 
		 * @param id Identifier to set
		 * @param val Value to set
		 */
		public Element(String id, JSONValue val)
		{
			identifier = id == null ? "" : id;
			value = val == null ? JSONNullValue.NULL : val;
			isProcessed = false;
		}
		
		/**
		 * Contains element identifier
		 */
		public String identifier;
		
		/**
		 * Contains element value
		 */
		public JSONValue value;
		
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
	public static JSONValue[] deserializeArray(JSONReader reader)
	{
		if (reader == null)
		{
			return JSONArrayValue.EMPTY;
		}
		
		StackState state = deserializeJSON(reader);
		
		if (state == null)
		{
			return JSONArrayValue.EMPTY;
		}
		
		LinkedList<JSONValue> res = state.array;
		return res == null ? JSONArrayValue.EMPTY : listToArray(res);
	}
	
	/**
	 * Deserializes JSON data and returns it as an object
	 * 
	 * @param reader Reader to get data from
	 * @return Deserialized data as a JSON object or empty JSON object if something went wrong
	 */
	public static JSONObject deserializeObject(JSONReader reader)
	{
		if (reader == null)
		{
			return JSONObject.EMPTY;
		}
		
		StackState state = deserializeJSON(reader);
		
		if (state == null)
		{
			return JSONObject.EMPTY;
		}
		
		JSONObject res = state.object;
		return res == null ? JSONObject.EMPTY : res;
	}
	
	/**
	 * Deserializes JSON data and returns it
	 * 
	 * @param reader Reader to get data from
	 * @return Deserialized data in JSON object/array or null JSON value if something went wrong
	 */
	public static JSONValue deserialize(JSONReader reader)
	{
		if (reader == null)
		{
			return JSONNullValue.NULL;
		}
		
		StackState state = deserializeJSON(reader);
		
		if (state == null)
		{
			return JSONNullValue.NULL;
		}

		switch (state.type)
		{
			case OBJECT:
				return new JSONObjectValue(state.object);
			case ARRAY:
				return new JSONArrayValue(state.array == null ? null : listToArray(state.array));
			default:
				return JSONNullValue.NULL;
		}
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param list List to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(List<JSONValue> list, JSONWriter writer)
	{
		if (writer == null || list == null)
		{
			return false;
		}
		
		serialize(new Element(listToArray(list)), writer);
		return true;
	}
	
	/**
	 * Serializes JSON data
	 * 
	 * @param array Array to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(JSONValue[] array, JSONWriter writer)
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
	 * @param object JSON object to serialize
	 * @param writer JSON writer to write data into
	 * @return True if data serialized successfully, false otherwise
	 */
	public static boolean serialize(JSONObject object, JSONWriter writer)
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
	public static boolean serialize(JSONValue value, String identifier, JSONWriter writer)
	{
		if (writer == null)
		{
			return false;
		}
		
		serialize(new Element(identifier, value), writer);
		return true;
	}
	
	/**
	 * Converts linked list into an array
	 * 
	 * @param list List to convert
	 * @return Array with list values
	 */
	private static JSONValue[] listToArray(List<JSONValue> list)
	{	
		JSONValue[] res = new JSONValue[list.size()];
		list.toArray(res);
		return res;
	}
	
	/**
	 * Deserializes JSON data and writes it into a stack state
	 * 
	 * @param reader JSON reader to use
	 * @return Deserialized data in a stack state or null if something went wrong
	 */
	private static StackState deserializeJSON(JSONReader reader)
	{
		Stack<StackState> scopeStack = new Stack<StackState>();
		StackState currentState = null;
		JSONValue newValue;
		Map.Entry<Boolean, JSONNotation> readResult;
		
		while ((readResult = reader.readNext()).getKey())
		{
			String identifier = reader.getIdentifier();
			newValue = null;
			
			switch (readResult.getValue())
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
						newValue = new JSONArrayValue(listToArray(currentState.array));
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
					currentState.array.add(newValue);
				}
			}
		}
		
		String error = reader.getErrorMessage();
		
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
	private static void serialize(Element element, JSONWriter writer)
	{
		Stack<Element> elementStack = new Stack<Element>();
		elementStack.push(element);
		
		while (!elementStack.isEmpty())
		{
			Element elem = elementStack.pop();
			
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

						if (elem.identifier.isEmpty())
						{
							writer.writeArrayStart();
						}
						else
						{
							writer.writeArrayStart(elem.identifier);
						}

						JSONValue[] values = elem.value.asArray();

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
						
						if (elem.identifier.isEmpty())
						{
							writer.writeObjectStart();
						}
						else
						{
							writer.writeObjectStart(elem.identifier);
						}
						
						Map<String, JSONValue> obj = elem.value.asObject().copyToMap();
						
						for (Map.Entry<String, JSONValue> ent : obj.entrySet())
						{
							elementStack.push(new Element(ent.getKey(), ent.getValue()));
						}
					}
					
					break;
				default:
					if (elem.identifier.isEmpty())
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
