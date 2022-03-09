package org.json.easy.serialization;

import org.json.easy.dom.*;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Map;

/**
 * Helper class for handling JSON serialization
 */
public class JSONSerializer
{
	/**
	 * Helper class for containing stack state
	 */
	private static class StackState
	{
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
	 * Converts linked list into an array
	 * 
	 * @param list List to convert
	 * @return Array with list values
	 */
	private static JSONValue[] listToArray(LinkedList<JSONValue> list)
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
					
					currentState = new StackState();
					currentState.type = JSONType.OBJECT;
					currentState.identifier = identifier;
					currentState.object = new JSONObject();
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
					
					currentState = new StackState();
					currentState.type = JSONType.ARRAY;
					currentState.identifier = identifier;
					currentState.array = new LinkedList<JSONValue>();
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
}
