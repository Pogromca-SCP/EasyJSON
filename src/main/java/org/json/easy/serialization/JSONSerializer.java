package org.json.easy.serialization;

import org.json.easy.dom.*;
import java.io.Reader;
import org.json.easy.util.ValueWrapper;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Helper class for handling JSON serialization
 */
public class JSONSerializer
{
	/**
	 * Deserializes JSON data and returns it as an array
	 * 
	 * @param src Reader to get data from
	 * @return Deserialized data as an array or empty array if something went wrong
	 */
	public static JSONValue[] deserializeArray(Reader src)
	{
		ValueWrapper<StackState> state = new ValueWrapper<StackState>();
		
		try (JSONReader reader = JSONReader.create(src))
		{
			if (!deserialize(reader, state))
			{
				return JSONArrayValue.EMPTY;
			}
		}
		
		LinkedList<JSONValue> res = state.value.array;
		return res == null ? JSONArrayValue.EMPTY : listToArray(res);
	}
	
	/**
	 * Deserializes JSON data and returns it as an object
	 * 
	 * @param src Reader to get data from
	 * @return Deserialized data as a JSON object or empty JSON object if something went wrong
	 */
	public static JSONObject deserializeObject(Reader src)
	{
		ValueWrapper<StackState> state = new ValueWrapper<StackState>();
		
		try (JSONReader reader = JSONReader.create(src))
		{
			if (!deserialize(reader, state))
			{
				return JSONObject.EMPTY;
			}
		}
		
		JSONObject res = state.value.object;
		return res == null ? JSONObject.EMPTY : res;
	}
	
	/**
	 * Deserializes JSON data and returns it
	 * 
	 * @param src Reader to get data from
	 * @return Deserialized data in JSON object/array or null JSON value if something went wrong
	 */
	public static JSONValue deserialize(Reader src)
	{
		ValueWrapper<StackState> state = new ValueWrapper<StackState>();
		
		try (JSONReader reader = JSONReader.create(src))
		{
			if (!deserialize(reader, state))
			{
				return JSONNullValue.NULL;
			}
		}

		switch (state.value.type)
		{
			case OBJECT:
				return new JSONObjectValue(state.value.object);
			case ARRAY:
				return new JSONArrayValue(state.value.array == null ? null : listToArray(state.value.array));
			default:
				return JSONNullValue.NULL;
		}
	}
	
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
	 * @param outStackState Stack state to write into
	 * @return True if JSON deserialized successfully, false otherwise
	 */
	private static boolean deserialize(JSONReader reader, ValueWrapper<StackState> outStackState)
	{
		Stack<StackState> scopeStack = new Stack<StackState>();
		StackState currentState = null;
		JSONValue newValue;
		ValueWrapper<JSONNotation> notation = new ValueWrapper<JSONNotation>();
		
		while (reader.readNext(notation))
		{
			String identifier = reader.getIdentifier();
			newValue = null;
			
			switch (notation.value)
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
					return false;
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
		
		if (currentState == null || (error != null && !error.isEmpty()))
		{
			return false;
		}
		
		outStackState.value = currentState;
		return true;
	}
}
