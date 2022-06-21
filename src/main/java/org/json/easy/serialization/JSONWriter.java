package org.json.easy.serialization;

import java.util.Stack;
import java.io.Writer;
import org.json.easy.policies.JSONPrintPolicy;
import java.util.function.Consumer;
import org.json.easy.policies.PrettyJSONPrintPolicy;
import java.io.IOException;
import org.json.easy.dom.JSONValue;
import org.json.easy.dom.JSONNullValue;

/**
 * Writer wrapper for writing JSON data
 * 
 * @since 1.0.0
 */
public class JSONWriter implements AutoCloseable
{	
	/**
	 * Contains entire write state in a stack structure
	 */
	private final Stack<JSONType> stack;
	
	/**
	 * Contains data stream to write into
	 */
	private Writer stream;
	
	/**
	 * Contains previously written token
	 */
	private JSONToken previousToken;
	
	/**
	 * Contains current indent level
	 */
	private int indentLevel;
	
	/**
	 * Contains current printing policy
	 */
	private final JSONPrintPolicy policy;
	
	/**
	 * Contains write method for print policy
	 */
	private final Consumer<Character> write;
	
	/**
	 * Creates new JSON writer and wraps provided writer
	 * 
	 * @param writer Writer to wrap
	 */
	public JSONWriter(final Writer writer)
	{
		this(writer, 0, null);
	}
	
	/**
	 * Creates new JSON writer and wraps provided writer
	 * 
	 * @param writer Writer to wrap
	 * @param initIndent Initial indentation level
	 */
	public JSONWriter(final Writer writer, final int initIndent)
	{
		this(writer, initIndent, null);
	}
	
	/**
	 * Creates new JSON writer and wraps provided writer
	 * 
	 * @param writer Writer to wrap
	 * @param pol JSON printing policy to use
	 */
	public JSONWriter(final Writer writer, final JSONPrintPolicy pol)
	{
		this(writer, 0, pol);
	}
	
	/**
	 * Creates new JSON writer and wraps provided writer
	 * 
	 * @param writer Writer to wrap
	 * @param initIndent Initial indentation level
	 * @param pol JSON printing policy to use
	 */
	public JSONWriter(final Writer writer, final int initIndent, final JSONPrintPolicy pol)
	{
		stack = new Stack<JSONType>();
		stream = writer;
		previousToken = JSONToken.NONE;
		indentLevel = initIndent;
		policy = pol == null ? new PrettyJSONPrintPolicy() : pol;
		
		write = ch -> {
			if (ch != null)
			{
				writeChar(ch);
			}
		}; 
	}
	
	/**
	 * Returns current indentation level
	 * 
	 * @return Current indent level value
	 */
	public int getIndentLevel()
	{
		return indentLevel;
	}
	
	/**
	 * Closes the writer and releases any system resources associated with it
	 */
	@Override
	public void close()
	{
		if (stream != null)
		{
			try
			{
				stream.flush();
				stream.close();
			}
			catch (IOException i) {}
			
			stream = null;
		}
	}
	
	/**
	 * Writes a start of an object
	 */
	public final void writeObjectStart()
	{
		writeStart(false);
	}
	
	/**
	 * Writes a start of an object with an identifier
	 * 
	 * @param identifier Identifier to write
	 */
	public final void writeObjectStart(final String identifier)
	{
		writeStart(false, identifier);
	}
	
	/**
	 * Writes an end of an object
	 */
	public final void writeObjectEnd()
	{
		writeEnd(false);
	}
	
	/**
	 * Writes a start of an array
	 */
	public final void writeArrayStart()
	{
		writeStart(true);
	}
	
	/**
	 * Writes a start of an array with an identifier
	 * 
	 * @param identifier Identifier to write
	 */
	public final void writeArrayStart(final String identifier)
	{
		writeStart(true, identifier);
	}
	
	/**
	 * Writes an end of an array
	 */
	public final void writeArrayEnd()
	{
		writeEnd(true);
	}
	
	/**
	 * Writes an atomic JSON value
	 * 
	 * @param value Value to write
	 */
	public final void writeValue(JSONValue value)
	{
		if (value == null)
		{
			value = JSONNullValue.NULL;
		}
		
		switch (value.getType())
		{
			case NULL:
				writeNull();
				break;
			case BOOLEAN:
				writeValue(value.asBoolean());
				break;
			case NUMBER:
				writeValue(value.asNumber());
				break;
			case STRING:
				writeValue(value.asString());
				break;
			default:
		}
	}
	
	/**
	 * Writes a null value
	 */
	public final void writeNull()
	{
		writeValuePrefix();
		previousToken = writeNullValueOnly();
	}
	
	/**
	 * Writes a boolean value
	 * 
	 * @param value Value to write
	 */
	public final void writeValue(final boolean value)
	{
		writeValuePrefix();
		previousToken = writeValueOnly(value);
	}
	
	/**
	 * Writes a boolean value
	 * 
	 * @param value Value to write
	 */
	public final void writeValue(final Boolean value)
	{
		writeValue(value == null ? false : value);
	}
	
	/**
	 * Writes a number value
	 * 
	 * @param value Value to write
	 */
	public final void writeValue(final double value)
	{
		writeValuePrefix();
		previousToken = writeValueOnly(value);
	}
	
	/**
	 * Writes a number value
	 * 
	 * @param value Value to write
	 */
	public final void writeValue(final Number value)
	{
		writeValue(value == null ? 0.0 : value.doubleValue());
	}
	
	/**
	 * Writes a string value
	 * 
	 * @param value Value to write
	 */
	public final void writeValue(final String value)
	{
		writeValuePrefix();
		previousToken = writeValueOnly(value == null ? "" : value);
	}
	
	/**
	 * Writes an atomic JSON value with an identifier
	 * 
	 * @param identifier Identifier to write
	 * @param value Value to write
	 */
	public final void writeValue(final String identifier, JSONValue value)
	{
		if (value == null)
		{
			value = JSONNullValue.NULL;
		}
		
		switch (value.getType())
		{
			case NULL:
				writeNull(identifier);
				break;
			case BOOLEAN:
				writeValue(identifier, value.asBoolean());
				break;
			case NUMBER:
				writeValue(identifier, value.asNumber());
				break;
			case STRING:
				writeValue(identifier, value.asString());
				break;
			default:
		}
	}
	
	/**
	 * Writes a null value with an identifier
	 * 
	 * @param identifier Identifier to write
	 */
	public final void writeNull(final String identifier)
	{
		writeValuePrefix(identifier);
		previousToken = writeNullValueOnly();
	}
	
	/**
	 * Writes a boolean value with an identifier
	 * 
	 * @param identifier Identifier to write
	 * @param value Value to write
	 */
	public final void writeValue(final String identifier, final boolean value)
	{
		writeValuePrefix(identifier);
		previousToken = writeValueOnly(value);
	}
	
	/**
	 * Writes a boolean value with an identifier
	 * 
	 * @param identifier Identifier to write
	 * @param value Value to write
	 */
	public final void writeValue(final String identifier, final Boolean value)
	{
		writeValue(identifier, value == null ? false : value);
	}
	
	/**
	 * Writes a number value with an identifier
	 * 
	 * @param identifier Identifier to write
	 * @param value Value to write
	 */
	public final void writeValue(final String identifier, final double value)
	{
		writeValuePrefix(identifier);
		previousToken = writeValueOnly(value);
	}
	
	/**
	 * Writes a number value with an identifier
	 * 
	 * @param identifier Identifier to write
	 * @param value Value to write
	 */
	public final void writeValue(final String identifier, final Number value)
	{
		writeValue(identifier, value == null ? 0.0 : value.doubleValue());
	}
	
	/**
	 * Writes a string value with an identifier
	 * 
	 * @param identifier Identifier to write
	 * @param value Value to write
	 */
	public final void writeValue(final String identifier, final String value)
	{
		writeValuePrefix(identifier);
		previousToken = writeValueOnly(value == null ? "" : value);
	}
	
	/**
	 * Writes an identifier prefix
	 * 
	 * @param identifier Identifier to write
	 */
	public final void writeIdentifierPrefix(final String identifier)
	{
		writeValuePrefix(identifier);
		previousToken = JSONToken.IDENTIFIER;
	}
	
	/**
	 * Writes a start of an array or object
	 * 
	 * @param isArray Set to true if should write array start
	 */
	private void writeStart(final boolean isArray)
	{
		if (!canWriteValueWithoutIdentifier())
		{
			return;
		}
		
		if (previousToken != JSONToken.NONE)
		{
			writeCommaIfNeeded();
		}
		
		if (isArray)
		{
			policy.writeArrayStartPrefix(write, indentLevel, previousToken);
		}
		else
		{
			policy.writeObjectStartPrefix(write, indentLevel, previousToken);
		}
		
		writeChar(isArray ? '[' : '{');
		++indentLevel;
		stack.push(isArray ? JSONType.ARRAY : JSONType.OBJECT);
		previousToken = isArray ? JSONToken.SQUARE_OPEN : JSONToken.CURLY_OPEN;
	}
	
	/**
	 * Writes a start of an array or object with an identifier
	 * 
	 * @param isArray Set to true if should write array start
	 * @param identifier Identifier to write
	 */
	private void writeStart(final boolean isArray, final String identifier)
	{
		if (!canWriteValueWithIdentifier(identifier))
		{
			return;
		}
		
		writeIdentifier(identifier);
		
		if (isArray)
		{
			policy.writeArrayStartPrefix(write, indentLevel, previousToken);
		}
		else
		{
			policy.writeObjectStartPrefix(write, indentLevel, previousToken);
		}
		
		writeChar(isArray ? '[' : '{');
		++indentLevel;
		stack.push(isArray ? JSONType.ARRAY : JSONType.OBJECT);
		previousToken = isArray ? JSONToken.SQUARE_OPEN : JSONToken.CURLY_OPEN;
	}
	
	/**
	 * Writes an end of an array or object
	 * 
	 * @param isArray Set to true if should write array end
	 */
	private void writeEnd(final boolean isArray)
	{
		if (stack.isEmpty() || (isArray ? stack.peek() != JSONType.ARRAY : stack.peek() != JSONType.OBJECT))
		{
			return;
		}
		
		--indentLevel;
		
		if (isArray)
		{
			policy.writeArrayEndPrefix(write, indentLevel, previousToken);
		}
		else
		{
			policy.writeObjectEndPrefix(write, indentLevel, previousToken);
		}
		
		writeChar(isArray ? ']' : '}');
		stack.pop();
		previousToken = isArray ? JSONToken.SQUARE_CLOSE : JSONToken.CURLY_CLOSE;
	}
	
	/**
	 * Checks if the writer can write a value without an identifier
	 * 
	 * @return True if it's possible, false otherwise
	 */
	private boolean canWriteValueWithoutIdentifier()
	{
		return stack.isEmpty() || stack.peek() == JSONType.ARRAY || previousToken == JSONToken.IDENTIFIER;
	}
	
	/**
	 * Checks if the writer can write a value with an identifier
	 * 
	 * @param identifier Identifier to check
	 * @return True if it's possible, false otherwise
	 */
	private boolean canWriteValueWithIdentifier(final String identifier)
	{
		return !stack.isEmpty() && stack.peek() == JSONType.OBJECT && previousToken != JSONToken.IDENTIFIER && identifier != null;
	}
	
	/**
	 * Writes a comma if it's needed
	 */
	private void writeCommaIfNeeded()
	{
		if (previousToken != JSONToken.CURLY_OPEN && previousToken != JSONToken.SQUARE_OPEN && previousToken != JSONToken.IDENTIFIER)
		{
			writeChar(',');
		}
	}
	
	/**
	 * Writes an identifier
	 * 
	 * @param identifier Identifier to write
	 */
	private void writeIdentifier(final String identifier)
	{
		writeCommaIfNeeded();
		policy.writeIdentifierPrefix(write, indentLevel, previousToken);
		writeStringValue(identifier);
		writeChar(':');
	}
	
	/**
	 * Writes a value prefix
	 */
	private void writeValuePrefix()
	{
		if (!canWriteValueWithoutIdentifier())
		{
			return;
		}
		
		writeCommaIfNeeded();
		policy.writeValuePrefix(write, indentLevel, previousToken);
	}
	
	/**
	 * Writes a value prefix with an identifier
	 * 
	 * @param identifier Identifier to write
	 */
	private void writeValuePrefix(final String identifier)
	{
		if (!canWriteValueWithIdentifier(identifier))
		{
			return;
		}
		
		writeIdentifier(identifier);
		policy.writeValuePrefix(write, indentLevel, previousToken);
	}
	
	/**
	 * Writes a null value
	 * 
	 * @return Written token type
	 */
	private JSONToken writeNullValueOnly()
	{
		writeString("null");
		return JSONToken.NULL;
	}
	
	/**
	 * Writes a boolean value
	 * 
	 * @param value Value to write
	 * @return Written token type
	 */
	private JSONToken writeValueOnly(final boolean value)
	{
		writeString(Boolean.toString(value));
		return value ? JSONToken.TRUE : JSONToken.FALSE;
	}
	
	/**
	 * Writes a number value
	 * 
	 * @param value Value to write
	 * @return Written token type
	 */
	private JSONToken writeValueOnly(final double value)
	{
		writeString(Double.toString(value));
		return JSONToken.NUMBER;
	}
	
	/**
	 * Writes a string value
	 * 
	 * @param value Value to write
	 * @return Written token type
	 */
	private JSONToken writeValueOnly(final String value)
	{
		writeStringValue(value);
		return JSONToken.STRING;
	}
	
	/**
	 * Writes a string value
	 * 
	 * @param value Value to write
	 */
	private void writeStringValue(final String value)
	{
		final StringBuilder sb = new StringBuilder().append('\"');
		
		for (int i = 0; i < value.length(); ++i)
		{
			final char ch = value.charAt(i);
			
			switch (ch)
			{
				case '\\':
					sb.append("\\").append('\\');
					break;
				case '\n':
					sb.append("\\").append('n');
					break;
				case '\t':
					sb.append("\\").append('t');
					break;
				case '\b':
					sb.append("\\").append('b');
					break;
				case '\f':
					sb.append("\\").append('f');
					break;
				case '\r':
					sb.append("\\").append('r');
					break;
				case '\"':
					sb.append("\\").append('\"');
					break;
				default:
					if (ch > 31)
					{
						sb.append(ch);
					}
					else
					{
						sb.append("\\").append('u').append(toHexString(ch));
					}
			}
		}
		
		writeString(sb.append('\"').toString());
	}
	
	/**
	 * Converts a character into a hex string
	 * 
	 * @param ch Character to convert
	 * @return Result hex string
	 */
	private String toHexString(final char ch)
	{
		final StringBuilder sb = new StringBuilder();
		final String hex = Integer.toHexString(ch);
		
		for (int i = hex.length(); i < 4; ++i)
		{
			sb.append('0');
		}
		
		return sb.append(hex).toString();
	}
	
	/**
	 * Writes a single character
	 * 
	 * @param ch Character to write
	 */
	private void writeChar(final char ch)
	{
		if (stream != null)
		{
			try
			{
				stream.write(ch);
			}
			catch (IOException i) {}
		}
	}
	
	/**
	 * Writes an entire string
	 * 
	 * @param str String to write
	 */
	private void writeString(final String str)
	{
		if (stream != null)
		{
			try
			{
				stream.write(str);
			}
			catch (IOException i) {}
		}
	}
}
