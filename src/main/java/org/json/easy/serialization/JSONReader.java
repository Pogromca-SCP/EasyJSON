package org.json.easy.serialization;

import java.io.Reader;
import java.util.Stack;
import org.json.easy.util.ValueWrapper;
import java.io.PushbackReader;
import java.io.IOException;

/**
 * Reader wrapper for reading JSON data
 */
public class JSONReader implements AutoCloseable
{
	/**
	 * Contains a table that converts JSON Token to JSON Notation, provide a token as an index in order to convert it
	 */
	private static final JSONNotation[] TOKEN_TO_NOTATION_TABLE = {
		JSONNotation.ERROR,
		JSONNotation.ERROR,
		JSONNotation.OBJECT_START,
		JSONNotation.OBJECT_END,
		JSONNotation.ARRAY_START,
		JSONNotation.ARRAY_END,
		JSONNotation.ERROR,
		JSONNotation.STRING,
		JSONNotation.NUMBER,
		JSONNotation.BOOLEAN,
		JSONNotation.BOOLEAN,
		JSONNotation.NULL
	};
	
	/**
	 * Creates new JSON reader and wraps provided reader
	 * 
	 * @param reader Reader to wrap
	 */
	public static JSONReader create(Reader reader)
	{
		return new JSONReader(reader);
	}
	
	/**
	 * Checks whether the character is a line break
	 * 
	 * @param ch Character to check
	 * @return True if character is a line break, false otherwise
	 */
	private static boolean isLineBreak(char ch)
	{
		return ch == '\n';
	}

	/**
	 * Checks whether the character is whitespace
	 * 
	 * @param ch Character to check
	 * @return True if character is whitespace, false otherwise
	 */
	private static boolean isWhitespace(char ch)
	{
		return ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r';
	}

	/**
	 * Checks whether the character is a JSON number
	 * 
	 * @param ch Character to check
	 * @return True if character is a JSON number, false otherwise
	 */
	private static boolean isJSONNumber(char ch)
	{
		return isDigit(ch) || ch == '-' || ch == '.' || ch == '+' || ch == 'e' || ch == 'E';
	}

	/**
	 * Checks whether the character is a digit
	 * 
	 * @param ch Character to check
	 * @return True if character is a digit, false otherwise
	 */
	private static boolean isDigit(char ch)
	{
		return ch >= '0' && ch <= '9';
	}

	/**
	 * Checks whether the character is a non zero digit
	 * 
	 * @param ch Character to check
	 * @return True if character is a non zero digit, false otherwise
	 */
	private static boolean isNonZeroDigit(char ch)
	{
		return ch >= '1' && ch <= '9';
	}

	/**
	 * Checks whether the character is a letter
	 * 
	 * @param ch Character to check
	 * @return True if character is a letter, false otherwise
	 */
	private static boolean isLetter(char ch)
	{
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}
	
	/**
	 * Contains entire parse state in a stack structure
	 */
	private Stack<JSONType> parseState;
	
	/**
	 * Contains current JSON Token
	 */
	private ValueWrapper<JSONToken> currentToken;
	
	/**
	 * Contains data stream to read
	 */
	private PushbackReader stream;
	
	/**
	 * Contains current line count
	 */
	private int lineNumber;
	
	/**
	 * Contains current character count
	 */
	private int characterNumber;
	
	/**
	 * Tells whether or not the reading of a root object is finished
	 */
	private boolean finishedReadingRootObject;
	
	/**
	 * Contains current error message
	 */
	private String errorMessage;
	
	/**
	 * Contains current value identifier for object parsing
	 */
	private String identifier;
	
	/**
	 * Contains current string value
	 */
	private String stringValue;
	
	/**
	 * Contains current number value
	 */
	private double numberValue;
	
	/**
	 * Contains current boolean value
	 */
	private boolean booleanValue;
	
	/**
	 * Creates new JSON reader
	 */
	protected JSONReader()
	{
		parseState = new Stack<JSONType>();
		currentToken = new ValueWrapper<JSONToken>(JSONToken.NONE);
		stream = null;
		lineNumber = 1;
		characterNumber = 0;
		finishedReadingRootObject = false;
		errorMessage = null;
		identifier = null;
		stringValue = null;
		numberValue = 0.0;
		booleanValue = false;
	}
	
	/**
	 * Creates new JSON reader and wraps provided reader
	 * 
	 * @param reader Reader to wrap
	 */
	protected JSONReader(Reader reader)
	{
		this();
		
		if (reader != null)
		{
			stream = new PushbackReader(reader);
		}
	}
	
	/**
	 * Returns current error message
	 * 
	 * @return Current error message value
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	/**
	 * Returns current identifier
	 * 
	 * @return Current identifier value
	 */
	public String getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * Returns current string value
	 * 
	 * @return Current string value
	 */
	public String getStringValue()
	{
		return stringValue;
	}
	
	/**
	 * Returns current number value
	 * 
	 * @return Current number value
	 */
	public double getNumberValue()
	{
		return numberValue;
	}
	
	/**
	 * Returns current boolean value
	 * 
	 * @return Current boolean value
	 */
	public boolean getBooleanValue()
	{
		return booleanValue;
	}
	
	/**
	 * Closes the reader and releases any system resources associated with it
	 */
	@Override
	public void close()
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			}
			catch (IOException i) {}
			
			stream = null;
		}
	}
	
	/**
	 * Parses next element
	 * 
	 * @param notation Notation to set while reading
	 * @return True if parsed successfully, false otherwise
	 */
	public final boolean readNext(ValueWrapper<JSONNotation> notation)
	{
		if (notation == null)
		{
			notation = new ValueWrapper<JSONNotation>();
		}
		
		if (errorMessage != null && !errorMessage.isEmpty())
		{
			notation.value = JSONNotation.ERROR;
			return false;
		}

		if (stream == null)
		{
			notation.value = JSONNotation.ERROR;
			setErrorMessage("Null stream error");
			return true;
		}

		boolean atEndOfStream = isAtEnd();

		if (atEndOfStream && !finishedReadingRootObject)
		{
			notation.value = JSONNotation.ERROR;
			setErrorMessage("Improperly formatted error");
			return true;
		}

		if (finishedReadingRootObject && !atEndOfStream)
		{
			notation.value = JSONNotation.ERROR;
			setErrorMessage("Unexpected additional input error");
			return true;
		}

		if (atEndOfStream)
		{
			return false;
		}
		
		boolean readWasSuccess = false;
		identifier = null;

		do
		{
			JSONType currentState = JSONType.NONE;

			if (!parseState.isEmpty())
			{
				currentState = parseState.peek();
			}

			switch (currentState)
			{
				case ARRAY:
					readWasSuccess = readNextArrayValue(currentToken);
					break;
				case OBJECT:
					readWasSuccess = readNextObjectValue(currentToken);
					break;
				default:
					readWasSuccess = readStart(currentToken);
					break;
			}
		}
		while (readWasSuccess && currentToken.value == JSONToken.NONE);
		
		notation.value = TOKEN_TO_NOTATION_TABLE[currentToken.value.ordinal()];
		finishedReadingRootObject = parseState.isEmpty();

		if (!readWasSuccess || notation.value == JSONNotation.ERROR)
		{
			notation.value = JSONNotation.ERROR;

			if (errorMessage == null || errorMessage.isEmpty())
			{
				setErrorMessage("Unknown error");
			}

			return true;
		}

		if (finishedReadingRootObject && !isAtEnd())
		{
			parseWhiteSpace();
		}

		return readWasSuccess;
	}
	
	/**
	 * Skips the object
	 */
	public final boolean skipObject()
	{
		return readUntilMatching(JSONNotation.OBJECT_END);
	}

	/**
	 * Skips the array
	 */
	public final boolean skipArray()
	{
		return readUntilMatching(JSONNotation.ARRAY_END);
	}
	
	/**
	 * Sets new error message with line and character numbers
	 * 
	 * @param message Message to set
	 */
	private void setErrorMessage(String message)
	{
		errorMessage = message + " at Line: " +  lineNumber + " Ch: " + characterNumber;
	}
	
	/**
	 * Reads new characters until an expected notation is matched
	 * 
	 * @param expectedNotation Notation to match
	 * @return True if no errors occurred, false otherwise
	 */
	private boolean readUntilMatching(JSONNotation expectedNotation)
	{
		int scopeCount = 0;
		ValueWrapper<JSONNotation> notation = new ValueWrapper<JSONNotation>();

		while (readNext(notation))
		{
			if (scopeCount == 0 && notation.value == expectedNotation)
			{
				return true;
			}

			switch (notation.value)
			{
				case OBJECT_START:
				case ARRAY_START:
					++scopeCount;
					break;
				case OBJECT_END:
				case ARRAY_END:
					--scopeCount;
					break;
				case ERROR:
					return false;
				default:
			}
		}

		return true;
	}
	
	/**
	 * Parses a start of an object or array
	 * 
	 * @param token Token to set while reading
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean readStart(ValueWrapper<JSONToken> token)
	{
		parseWhiteSpace();
		token.value = JSONToken.NONE;

		if (!nextToken(token))
		{
			return false;
		}

		if (token.value != JSONToken.CURLY_OPEN && token.value != JSONToken.SQUARE_OPEN)
		{
			setErrorMessage("Open Curly or Square Brace token expected");
			return false;
		}

		return true;
	}

	/**
	 * Parses next object value
	 * 
	 * @param token Token to set while reading
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean readNextObjectValue(ValueWrapper<JSONToken> token)
	{
		boolean commaPrepend = token.value != JSONToken.CURLY_OPEN;
		token.value = JSONToken.NONE;

		if (!nextToken(token))
		{
			return false;
		}

		if (token.value == JSONToken.CURLY_CLOSE)
		{
			return true;
		}
		
		if (commaPrepend)
		{
			if (token.value != JSONToken.COMMA)
			{
				setErrorMessage("Comma token expected");
				return false;
			}

			token.value = JSONToken.NONE;

			if (!nextToken(token))
			{
				return false;
			}
		}

		if (token.value != JSONToken.STRING)
		{
			setErrorMessage("String token expected");
			return false;
		}

		identifier = stringValue;
		token.value = JSONToken.NONE;

		if (!nextToken(token))
		{
			return false;
		}

		if (token.value != JSONToken.COLON)
		{
			setErrorMessage("Colon token expected");
			return false;
		}

		token.value = JSONToken.NONE;

		if (!nextToken(token))
		{
			return false;
		}

		return true;
	}
	
	/**
	 * Parses next object value
	 * 
	 * @param token Token to set while reading
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean readNextArrayValue(ValueWrapper<JSONToken> token)
	{
		boolean commaPrepend = token.value != JSONToken.SQUARE_OPEN;
		token.value = JSONToken.NONE;

		if (!nextToken(token))
		{
			return false;
		}

		if (token.value == JSONToken.SQUARE_CLOSE)
		{
			return true;
		}
		
		if (commaPrepend)
		{
			if (token.value != JSONToken.COMMA)
			{
				setErrorMessage("Comma token expected");
				return false;
			}

			token.value = JSONToken.NONE;

			if (!nextToken(token))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Parses next JSON token
	 * 
	 * @param token Token to set while reading
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean nextToken(ValueWrapper<JSONToken> token)
	{
		char ch;
		
		while ((ch = read()) != -1)
		{
			++characterNumber;

			if (ch == '\0')
			{
				break;
			}

			if (isLineBreak(ch))
			{
				++lineNumber;
				characterNumber = 0;
			}

			if (!isWhitespace(ch))
			{
				if (isJSONNumber(ch))
				{
					if (!parseNumberToken(ch))
					{
						return false;
					}

					token.value = JSONToken.NUMBER;
					return true;
				}

				switch (ch)
				{
					case '{':
						token.value = JSONToken.CURLY_OPEN;
						parseState.push(JSONType.OBJECT);
						return true;
					case '}':
						token.value = JSONToken.CURLY_CLOSE;
						parseState.pop();
						return true;
					case '[':
						token.value = JSONToken.SQUARE_OPEN;
						parseState.push(JSONType.ARRAY);
						return true;
					case ']':
						token.value = JSONToken.SQUARE_CLOSE;
						parseState.pop();
						return true;
					case ':':
						token.value = JSONToken.COLON;
						return true;
					case ',':
						token.value = JSONToken.COMMA;
						return true;
					case '\"':
						if (!parseStringToken())
						{
							return false;
						}

						token.value = JSONToken.STRING;
						return true;
					case 't':
					case 'T':
					case 'f':
					case 'F':
					case 'n':
					case 'N':
						StringBuilder test = new StringBuilder();
						test.append(ch);

						while ((ch = read()) != -1)
						{
							if (isLetter(ch))
							{
								++characterNumber;
								test.append(ch);
							}
							else
							{
								backtrack(ch);
								break;
							}
						}
						
						String testString = test.toString();
						
						if (testString.equalsIgnoreCase("false"))
						{
							booleanValue = false;
							token.value = JSONToken.FALSE;
							return true;
						}

						if (testString.equalsIgnoreCase("true"))
						{
							booleanValue = true;
							token.value = JSONToken.TRUE;
							return true;
						}

						if (testString.equalsIgnoreCase("null"))
						{
							token.value = JSONToken.NULL;
							return true;
						}

						setErrorMessage("Invalid JSON token (field name)");
						return false;
					default: 
						setErrorMessage("Invalid JSON token");
						return false;
				}
			}
		}

		setErrorMessage("Invalid JSON token");
		return false;
	}
	
	/**
	 * Parses a string token
	 * 
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean parseStringToken()
	{
		StringBuilder sb = new StringBuilder();

		while (true)
		{
			char ch = read();
			
			if (ch == -1)
			{
				backtrack(ch);
				setErrorMessage("String token abruptly ended");
				return false;
			}

			++characterNumber;

			if (ch == '\"')
			{
				break;
			}

			if (ch == '\\')
			{
				ch = read();
				++characterNumber;

				switch (ch)
				{
					case '\"':
					case '\\':
					case '/':
						sb.append(ch);
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'r':
						sb.append('\r');
						break;
					case 'n':
						sb.append('\n');
						break;
					case 'b':
						sb.append('\b');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'u':
						int hexNum = 0;

						for (int radix = 3; radix >= 0; --radix)
						{
							char numChar = read();
							
							if (numChar == -1)
							{
								backtrack(numChar);
								setErrorMessage("String token abruptly ended");
								return false;
							}

							++characterNumber;
							int hexDigit = Character.digit(numChar, 16);

							if (hexDigit == 0 && ch != '0')
							{
								setErrorMessage("Invalid hexadecimal digit");
								return false;
							}

							hexNum += hexDigit * Math.pow(16, radix);
						}

						sb.append((char) hexNum);
						break;
					default:
						setErrorMessage("Bad JSON escaped char");
						return false;
				}
			}
			else
			{
				sb.append(ch);
			}
		}

		stringValue = sb.toString();
		return true;
	}

	/**
	 * Parses a number token
	 * 
	 * @param first First character to parse
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean parseNumberToken(char first)
	{
		StringBuilder sb = new StringBuilder();
		int state = 0;
		boolean useFirstChar = true;
		boolean error = false;

		while (true)
		{
			if (isAtEnd())
			{
				setErrorMessage("Number token abruptly ended");
				return false;
			}
			
			char ch;

			if (useFirstChar)
			{
				ch = first;
				useFirstChar = false;
			}
			else
			{
				ch = read();
				++characterNumber;
			}

			if (isJSONNumber(ch))
			{
				switch (state)
				{
					case 0:
						if (ch == '-')
						{
							state = 1;
						}
						else if (ch == '0')
						{
							state = 2;
						}
						else if (isNonZeroDigit(ch))
						{
							state = 3;
						}
						else
						{
							error = true;
						}
						
						break;
					case 1:
						if (ch == '0')
						{
							state = 2;
						}
						else if (isNonZeroDigit(ch))
						{
							state = 3;
						}
						else
						{
							error = true;
						}
						
						break;
					case 2:
						if (ch == '.')
						{
							state = 4;
						}
						else if (ch == 'e' || ch == 'E')
						{
							state = 5;
						}
						else
						{
							error = true;
						}
						
						break;
					case 3:
						if (isDigit(ch))
						{
							state = 3;
						}
						else if (ch == '.')
						{
							state = 4;
						}
						else if (ch == 'e' || ch == 'E')
						{
							state = 5;
						}
						else
						{
							error = true;
						}
						
						break;
					case 4:
						if (isDigit(ch))
						{
							state = 6;
						}
						else
						{
							error = true;
						}
						
						break;
					case 5:
						if (ch == '-' || ch == '+')
						{
							state = 7;
						}
						else if (isDigit(ch))
						{
							state = 8;
						}
						else
						{
							error = true;
						}
						
						break;
					case 6:
						if (isDigit(ch))
						{
							state = 6;
						}
						else if (ch == 'e' || ch == 'E')
						{
							state = 5;
						}
						else
						{
							error = true;
						}
						
						break;
					case 7:
						if (isDigit(ch))
						{
							state = 8;
						}
						else
						{
							error = true;
						}
						
						break;
					case 8:
						if (isDigit(ch))
						{
							state = 8;
						}
						else
						{
							error = true;
						}
						
						break;
					default:
						setErrorMessage("Unknown state reached in JSON number token");
						return false;
				}

				if (error)
				{
					break;
				}

				sb.append(ch);
			}
			else
			{
				backtrack(ch);
				--characterNumber;
				break;
			}
		}

		if (!error && (state == 2 || state == 3 || state == 6 || state == 8))
		{
			stringValue = sb.toString();
			
			try
			{
				numberValue = Double.parseDouble(stringValue);
			}
			catch (NumberFormatException n) {}
			
			return true;
		}

		setErrorMessage("Poorly formed JSON number token");
		return false;
	}
	
	/**
	 * Parses all whitespace characters until non whitespace character is reached
	 */
	private void parseWhiteSpace()
	{
		char ch;
		
		while ((ch = read()) != -1)
		{
			++characterNumber;
			
			if (isLineBreak(ch))
			{
				++lineNumber;
				characterNumber = 0;
			}
			
			if (!isWhitespace(ch))
			{
				backtrack(ch);
				--characterNumber;
				break;
			}
		}
	}
	
	/**
	 * Reads a single character
	 * 
	 * @return Read character or -1 if the end of the stream has been reached
	 */
	private char read()
	{
		char ch = 0;
		
		try
		{
			ch = (char) stream.read();
		}
		catch (IOException i) {}
		
		return ch;
	}
	
	/**
	 * Pushes back a single character
	 * 
	 * @param ch Character to push back
	 */
	private void backtrack(char ch)
	{
		try
		{
			stream.unread(ch);
		}
		catch (IOException i) {}
	}
	
	/**
	 * Checks if the reader is at the end of the stream
	 * 
	 * @return True if the reader is at the end, false otherwise
	 */
	private boolean isAtEnd()
	{
		char ch = read();
		backtrack(ch);
		return ch == -1;
	}
}
