package org.json.easy.serialization;

import java.util.Stack;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.IOException;

/**
 * Reader wrapper for reading JSON data
 * 
 * @since 1.0.0
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
	private JSONToken currentToken;
	
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
	 * Tells whether or not the reading of the root object is finished
	 */
	private boolean finishedReadingRootObject;
	
	/**
	 * Contains previously read notation
	 */
	private JSONNotation readNotation;
	
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
	 * Creates new JSON reader and wraps provided reader
	 * 
	 * @param reader Reader to wrap
	 */
	public JSONReader(Reader reader)
	{
		parseState = new Stack<JSONType>();
		currentToken = JSONToken.NONE;
		stream = reader == null ? null : new PushbackReader(reader);
		lineNumber = 1;
		characterNumber = 0;
		finishedReadingRootObject = false;
		readNotation = null;
		errorMessage = "";
		identifier = "";
		stringValue = "";
		numberValue = 0.0;
		booleanValue = false;
	}
	
	/**
	 * Returns previously read notation
	 * 
	 * @return Previously read notation type, can be null
	 */
	public final JSONNotation getReadNotation()
	{
		return readNotation;
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
			catch (IOException i)
			{
				errorMessage = "IOException occured while closing";
			}
			
			stream = null;
		}
	}
	
	/**
	 * Parses next JSON notation element
	 * 
	 * @return True if the reading can continue, false otherwise
	 */
	public final boolean readNext()
	{
		if (!errorMessage.isEmpty())
		{
			readNotation = JSONNotation.ERROR;
			return false;
		}

		if (stream == null)
		{
			setErrorMessage("Null stream error");
			readNotation = JSONNotation.ERROR;
			return true;
		}

		boolean atEndOfStream = isAtEnd();

		if (atEndOfStream && !finishedReadingRootObject)
		{
			setErrorMessage("Improperly formatted input");
			readNotation = JSONNotation.ERROR;
			return true;
		}

		if (finishedReadingRootObject && !atEndOfStream)
		{
			setErrorMessage("Unexpected additional input");
			readNotation = JSONNotation.ERROR;
			return true;
		}

		if (atEndOfStream)
		{
			readNotation = null;
			return false;
		}
		
		boolean readWasSuccess = false;
		identifier = "";

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
					readWasSuccess = readNextArrayValue();
					break;
				case OBJECT:
					readWasSuccess = readNextObjectValue();
					break;
				default:
					readWasSuccess = readStart();
			}
		}
		while (readWasSuccess && currentToken == JSONToken.NONE);
		
		readNotation = TOKEN_TO_NOTATION_TABLE[currentToken.ordinal()];
		finishedReadingRootObject = parseState.isEmpty();

		if (!readWasSuccess || readNotation == JSONNotation.ERROR)
		{
			readNotation = JSONNotation.ERROR;
			
			if (errorMessage.isEmpty())
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
	 * 
	 * @return True if skipped successfully, false otherwise
	 */
	public final boolean skipObject()
	{
		return readUntilMatching(JSONNotation.OBJECT_END);
	}

	/**
	 * Skips the array
	 * 
	 * @return True if skipped successfully, false otherwise
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

		while (readNext())
		{
			if (scopeCount == 0 && readNotation == expectedNotation)
			{
				return true;
			}

			switch (readNotation)
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
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean readStart()
	{
		parseWhiteSpace();
		currentToken = nextToken();

		if (currentToken == JSONToken.NONE)
		{
			return false;
		}

		if (currentToken != JSONToken.CURLY_OPEN && currentToken != JSONToken.SQUARE_OPEN)
		{
			setErrorMessage("Open Curly or Square Brace token expected");
			return false;
		}

		return true;
	}

	/**
	 * Parses next object value
	 * 
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean readNextObjectValue()
	{
		boolean useComma = currentToken != JSONToken.CURLY_OPEN;
		currentToken = nextToken();

		if (currentToken == JSONToken.NONE)
		{
			return false;
		}

		if (currentToken == JSONToken.CURLY_CLOSE)
		{
			return true;
		}
		
		if (useComma)
		{
			if (currentToken != JSONToken.COMMA)
			{
				setErrorMessage("Comma token expected");
				return false;
			}

			currentToken = nextToken();

			if (currentToken == JSONToken.NONE)
			{
				return false;
			}
		}

		if (currentToken != JSONToken.STRING)
		{
			setErrorMessage("String token expected");
			return false;
		}

		identifier = stringValue;
		currentToken = nextToken();

		if (currentToken == JSONToken.NONE)
		{
			return false;
		}

		if (currentToken != JSONToken.COLON)
		{
			setErrorMessage("Colon token expected");
			return false;
		}

		currentToken = nextToken();
		return currentToken != JSONToken.NONE;
	}
	
	/**
	 * Parses next array value
	 * 
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean readNextArrayValue()
	{
		boolean useComma = currentToken != JSONToken.SQUARE_OPEN;
		currentToken = nextToken();

		if (currentToken == JSONToken.NONE)
		{
			return false;
		}

		if (currentToken == JSONToken.SQUARE_CLOSE)
		{
			return true;
		}
		
		if (useComma)
		{
			if (currentToken != JSONToken.COMMA)
			{
				setErrorMessage("Comma token expected");
				return false;
			}

			currentToken = nextToken();

			if (currentToken == JSONToken.NONE)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Parses next JSON token
	 * 
	 * @return Parsed token type
	 */
	private JSONToken nextToken()
	{
		while (!isAtEnd())
		{	
			char ch = read();
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
						return JSONToken.NONE;
					}

					return JSONToken.NUMBER;
				}

				switch (ch)
				{
					case '{':
						parseState.push(JSONType.OBJECT);
						return JSONToken.CURLY_OPEN;
					case '}':
						if (!parseState.isEmpty())
						{
							parseState.pop();
						}
						
						return JSONToken.CURLY_CLOSE;
					case '[':
						parseState.push(JSONType.ARRAY);
						return JSONToken.SQUARE_OPEN;
					case ']':
						if (!parseState.isEmpty())
						{
							parseState.pop();
						}
						
						return JSONToken.SQUARE_CLOSE;
					case ':':
						return JSONToken.COLON;
					case ',':
						return JSONToken.COMMA;
					case '\"':
						if (!parseStringToken())
						{
							return JSONToken.NONE;
						}

						return JSONToken.STRING;
					case 't':
					case 'T':
					case 'f':
					case 'F':
					case 'n':
					case 'N':
						StringBuilder sb = new StringBuilder().append(ch);

						while (!isAtEnd())
						{
							ch = read();
							
							if (isLetter(ch))
							{
								++characterNumber;
								sb.append(ch);
							}
							else
							{
								backtrack(ch);
								break;
							}
						}
						
						String test = sb.toString();
						
						if (test.equalsIgnoreCase("false"))
						{
							booleanValue = false;
							return JSONToken.FALSE;
						}

						if (test.equalsIgnoreCase("true"))
						{
							booleanValue = true;
							return JSONToken.TRUE;
						}

						if (test.equalsIgnoreCase("null"))
						{
							return JSONToken.NULL;
						}

						setErrorMessage("Invalid JSON token (field name)");
						return JSONToken.NONE;
					default: 
						setErrorMessage("Invalid JSON token");
						return JSONToken.NONE;
				}
			}
		}

		setErrorMessage("Invalid JSON token");
		return JSONToken.NONE;
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
			if (isAtEnd())
			{
				setErrorMessage("String token abruptly ended");
				return false;
			}
			
			char ch = read();
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

						for (byte radix = 3; radix > -1; --radix)
						{
							if (isAtEnd())
							{
								setErrorMessage("String token abruptly ended");
								return false;
							}

							ch = read();
							++characterNumber;
							int hexDigit = Character.digit(ch, 16);

							if (hexDigit == -1)
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
		byte state = 0;
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
		while (!isAtEnd())
		{
			char ch = read();
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
		char ch = ' ';
		
		try
		{
			ch = (char) stream.read();
		}
		catch (IOException i)
		{
			setErrorMessage("IOException");
		}
		
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
		catch (IOException i)
		{
			setErrorMessage("IOException");
		}
	}
	
	/**
	 * Checks if the reader is at the end of the stream
	 * 
	 * @return True if the reader is at the end, false otherwise
	 */
	private boolean isAtEnd()
	{
		int num = ' ';
		boolean res = false;
		
		try
		{
			num = stream.read();
			res = num == -1;
			
			if (!res)
			{
				stream.unread(num);
			}
		}
		catch (IOException i)
		{
			setErrorMessage("IOException");
		}
		
		return res;
	}
}
