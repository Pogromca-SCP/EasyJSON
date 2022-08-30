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
	 * Defines possible number parse states
	 */
	private static enum NumberParseState
	{
		NONE,
		START,
		ZERO,
		INT,
		POINT,
		LETTER,
		REAL,
		SIGN,
		EXPONENT,
		ERROR
	}
	
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
	private static boolean isLineBreak(final char ch)
	{
		return ch == '\n';
	}

	/**
	 * Checks whether the character is whitespace
	 * 
	 * @param ch Character to check
	 * @return True if character is whitespace, false otherwise
	 */
	private static boolean isWhitespace(final char ch)
	{
		return ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r';
	}

	/**
	 * Checks whether the character is a JSON number
	 * 
	 * @param ch Character to check
	 * @return True if character is a JSON number, false otherwise
	 */
	private static boolean isJSONNumber(final char ch)
	{
		return isDigit(ch) || ch == '-' || ch == '.' || ch == '+' || ch == 'e' || ch == 'E';
	}

	/**
	 * Checks whether the character is a digit
	 * 
	 * @param ch Character to check
	 * @return True if character is a digit, false otherwise
	 */
	private static boolean isDigit(final char ch)
	{
		return ch >= '0' && ch <= '9';
	}

	/**
	 * Checks whether the character is a non zero digit
	 * 
	 * @param ch Character to check
	 * @return True if character is a non zero digit, false otherwise
	 */
	private static boolean isNonZeroDigit(final char ch)
	{
		return ch >= '1' && ch <= '9';
	}

	/**
	 * Checks whether the character is a letter
	 * 
	 * @param ch Character to check
	 * @return True if character is a letter, false otherwise
	 */
	private static boolean isLetter(final char ch)
	{
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
	}
	
	/**
	 * Parses number initialization
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberInit(final char ch)
	{
		return ch == '-' ? NumberParseState.START : parseNumberStart(ch);
	}
	
	/**
	 * Parses number start
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberStart(final char ch)
	{
		if (ch == '0')
		{
			return NumberParseState.ZERO;
		}
		else if (isNonZeroDigit(ch))
		{
			return NumberParseState.INT;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Parses number zero
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberZero(final char ch)
	{
		if (ch == '.')
		{
			return NumberParseState.POINT;
		}
		else if (ch == 'e' || ch == 'E')
		{
			return NumberParseState.LETTER;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Parses number's integer part
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberInt(final char ch)
	{
		return isDigit(ch) ? NumberParseState.INT : parseNumberZero(ch);
	}
	
	/**
	 * Parses number's point
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberPoint(final char ch)
	{
		return isDigit(ch) ? NumberParseState.REAL : null;
	}
	
	/**
	 * Parses number's exponent letter
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberLetter(final char ch)
	{
		if (ch == '-' || ch == '+')
		{
			return NumberParseState.SIGN;
		}
		else if (isDigit(ch))
		{
			return NumberParseState.EXPONENT;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Parses number's real part
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberReal(final char ch)
	{
		if (isDigit(ch))
		{
			return NumberParseState.REAL;
		}
		else if (ch == 'e' || ch == 'E')
		{
			return NumberParseState.LETTER;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Parses number's exponent part
	 * 
	 * @param ch Character to parse
	 * @return New number parse state
	 */
	private static NumberParseState parseNumberExponent(final char ch)
	{
		return isDigit(ch) ? NumberParseState.EXPONENT : null;
	}
	
	/**
	 * Contains entire parse state in a stack structure
	 */
	private final Stack<JSONType> parseState;
	
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
	public JSONReader(final Reader reader)
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
		final Boolean err = checkPreReadErrors();
		
		if (err != null)
		{
			return err;
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
		
		return checkPostReadErrors(readWasSuccess);
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
	private void setErrorMessage(final String message)
	{
		errorMessage = message + " at Line: " +  lineNumber + " Ch: " + characterNumber;
	}
	
	/**
	 * Handles errors before reading JSON notation
	 * 
	 * @return True if the reading can continue, false otherwise or null if there are no errors
	 */
	private Boolean checkPreReadErrors()
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

		final boolean atEndOfStream = isAtEnd();

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
		
		return null;
	}
	
	/**
	 * Handles errors after reading JSON notation
	 * 
	 * @param readWasSuccess Tells whether or not reading was successful
	 * @return True if the reading can continue, false otherwise
	 */
	private boolean checkPostReadErrors(final boolean readWasSuccess)
	{
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
	 * Reads new characters until an expected notation is matched
	 * 
	 * @param expectedNotation Notation to match
	 * @return True if no errors occurred, false otherwise
	 */
	private boolean readUntilMatching(final JSONNotation expectedNotation)
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
		final Boolean val = readNextValue(false);
		
		if (val != null)
		{
			return val;
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
		final Boolean res = readNextValue(true);
		return res == null ? true : res;
	}
	
	/**
	 * Parses next array or object value
	 * 
	 * @param isArray Tells whether or not the next value is an array
	 * @return True if parsed successfully, false otherwise or null if parsing is not finished
	 */
	private Boolean readNextValue(final boolean isArray)
	{
		final boolean useComma = currentToken != (isArray ? JSONToken.SQUARE_OPEN : JSONToken.CURLY_OPEN);
		currentToken = nextToken();

		if (currentToken == JSONToken.NONE)
		{
			return false;
		}

		if (currentToken == (isArray ? JSONToken.SQUARE_CLOSE : JSONToken.CURLY_CLOSE))
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
		
		return null;
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
			final char ch = read();
			++characterNumber;

			if (ch == '\0')
			{
				break;
			}

			handleLineBreak(ch);

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

				return parseCharacter(ch);
			}
		}

		setErrorMessage("Invalid JSON token");
		return JSONToken.NONE;
	}
	
	/**
	 * Parses next character token
	 * 
	 * @param ch Character to parse
	 * @return Parsed token type
	 */
	private JSONToken parseCharacter(final char ch)
	{
		switch (ch)
		{
			case '{':
				return parseStart(false);
			case '}':
				return parseEnd(false);
			case '[':
				return parseStart(true);
			case ']':
				return parseEnd(true);
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
				return parseLiteral(ch);
			default: 
				setErrorMessage("Invalid JSON token");
				return JSONToken.NONE;
		}
	}
	
	/**
	 * Parses a start of an array or object
	 * 
	 * @param isArray Tells whether or not this is a start of an array
	 * @return Parsed token type
	 */
	private JSONToken parseStart(final boolean isArray)
	{
		parseState.push(isArray ? JSONType.ARRAY : JSONType.OBJECT);
		return isArray ? JSONToken.SQUARE_OPEN : JSONToken.CURLY_OPEN;
	}
	
	/**
	 * Parses an end of an array or object
	 * 
	 * @param isArray Tells whether or not this is an end of an array
	 * @return Parsed token type
	 */
	private JSONToken parseEnd(final boolean isArray)
	{
		if (!parseState.isEmpty())
		{
			parseState.pop();
		}
		
		return isArray ? JSONToken.SQUARE_CLOSE : JSONToken.CURLY_CLOSE;
	}
	
	/**
	 * Parses a JSON literal value
	 * 
	 * @param ch First character to parse
	 * @return Parsed token type
	 */
	private JSONToken parseLiteral(final char ch)
	{
		final StringBuilder sb = new StringBuilder().append(ch);
		loadLiteral(sb);
		final String test = sb.toString();
		
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
	}
	
	/**
	 * Loads a literal value into string builder
	 * 
	 * @param sb String builder to load into
	 */
	private void loadLiteral(final StringBuilder sb)
	{
		while (!isAtEnd())
		{
			final char ch = read();
			
			if (isLetter(ch))
			{
				++characterNumber;
				sb.append(ch);
			}
			else
			{
				backtrack(ch);
				return;
			}
		}
	}
	
	/**
	 * Parses a string token
	 * 
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean parseStringToken()
	{
		final StringBuilder sb = new StringBuilder();

		while (true)
		{
			if (isAtEnd())
			{
				setErrorMessage("String token abruptly ended");
				return false;
			}
			
			final char ch = read();
			++characterNumber;

			if (ch == '\"')
			{
				break;
			}

			if (ch == '\\' && !parseEscapedChar(sb))
			{
				return false;
			}
			else if (ch != '\\')
			{
				sb.append(ch);
			}
		}

		stringValue = sb.toString();
		return true;
	}
	
	/**
	 * Parses an escaped character
	 * 
	 * @param sb String builder to parse into
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean parseEscapedChar(final StringBuilder sb)
	{
		final char ch = read();
		++characterNumber;

		switch (ch)
		{
			case '\"':
			case '\\':
			case '/':
				sb.append(ch);
				return true;
			case 'f':
				sb.append('\f');
				return true;
			case 'r':
				sb.append('\r');
				return true;
			case 'n':
				sb.append('\n');
				return true;
			case 'b':
				sb.append('\b');
				return true;
			case 't':
				sb.append('\t');
				return true;
			case 'u':
				return parseHexChar(sb);
			default:
				setErrorMessage("Bad JSON escaped char");
				return false;
		}
	}
	
	/**
	 * Parses a hex character
	 * 
	 * @param sb String builder to parse into
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean parseHexChar(final StringBuilder sb)
	{
		int hexNum = 0;

		for (byte radix = 3; radix > -1; --radix)
		{
			if (isAtEnd())
			{
				setErrorMessage("String token abruptly ended");
				return false;
			}

			final char ch = read();
			++characterNumber;
			final int hexDigit = Character.digit(ch, 16);

			if (hexDigit == -1)
			{
				setErrorMessage("Invalid hexadecimal digit");
				return false;
			}

			hexNum += hexDigit * Math.pow(16, radix);
		}

		sb.append((char) hexNum);
		return true;
	}

	/**
	 * Parses a number token
	 * 
	 * @param first First character to parse
	 * @return True if parsed successfully, false otherwise
	 */
	private boolean parseNumberToken(final char first)
	{
		final StringBuilder sb = new StringBuilder();
		NumberParseState state = NumberParseState.NONE;
		
		if (!checkNumberParseError())
		{
			return false;
		}
		
		state = parseNumber(first, sb, state);
		
		if (state == NumberParseState.ERROR)
		{
			return false;
		}

		state = parseNumber(sb, state);
		
		if (state == NumberParseState.ERROR)
		{
			return false;
		}

		if (finishNumberParse(sb, state))
		{
			return true;
		}

		setErrorMessage("Poorly formed JSON number token");
		return false;
	}
	
	/**
	 * Parses a single number character
	 * 
	 * @param ch Character to parse
	 * @param sb String builder to parse into
	 * @param state Current number parse state
	 * @return New number parse state
	 */
	private NumberParseState parseNumber(final char ch, final StringBuilder sb, NumberParseState state)
	{
		if (isJSONNumber(ch))
		{
			state = parseNumber(ch, state);
			
			if (state != null && state != NumberParseState.ERROR)
			{
				sb.append(ch);
			}
		}
		else
		{
			backtrack(ch);
			--characterNumber;
		}
		
		return state;
	}
	
	/**
	 * Parses a number text
	 * 
	 * @param sb String builder to parse into
	 * @param state Current number parse state
	 * @return New number parse state
	 */
	private NumberParseState parseNumber(final StringBuilder sb, NumberParseState state)
	{
		while (true)
		{
			if (!checkNumberParseError())
			{
				return NumberParseState.ERROR;
			}
			
			final char ch = read();
			++characterNumber;
			
			if (isJSONNumber(ch))
			{
				state = parseNumber(ch, state);

				if (state == null || state == NumberParseState.ERROR)
				{
					return state;
				}

				sb.append(ch);
			}
			else
			{
				backtrack(ch);
				--characterNumber;
				return state;
			}
		}
	}
	
	/**
	 * Checks for number parse end error
	 * 
	 * @return True if parsing can continue, false otherwise
	 */
	private boolean checkNumberParseError()
	{
		if (isAtEnd())
		{
			setErrorMessage("Number token abruptly ended");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Parses a single number element
	 * 
	 * @param ch Character to parse
	 * @param state Current number parse state
	 * @return New number parse state
	 */
	private NumberParseState parseNumber(final char ch, final NumberParseState state)
	{
		switch (state)
		{
			case NONE:
				return parseNumberInit(ch);
			case START:
				return parseNumberStart(ch);
			case ZERO:
				return parseNumberZero(ch);
			case INT:
				return parseNumberInt(ch);
			case POINT:
				return parseNumberPoint(ch);
			case LETTER:
				return parseNumberLetter(ch);
			case REAL:
				return parseNumberReal(ch);
			case SIGN:
			case EXPONENT:
				return parseNumberExponent(ch);
			default:
				setErrorMessage("Unknown state reached in JSON number token");
				return NumberParseState.ERROR;
		}
	}
	
	/**
	 * Finishes number token parsing
	 * 
	 * @param sb String builder to read value from
	 * @param state Current number parse state
	 * @return True if parsing completed successfully, false otherwise
	 */
	private boolean finishNumberParse(final StringBuilder sb, final NumberParseState state)
	{
		if (state == NumberParseState.ZERO || state == NumberParseState.INT || state == NumberParseState.REAL || state == NumberParseState.EXPONENT)
		{
			stringValue = sb.toString();
			
			try
			{
				numberValue = Double.parseDouble(stringValue);
			}
			catch (NumberFormatException n) {}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Parses all whitespace characters until non whitespace character is reached
	 */
	private void parseWhiteSpace()
	{
		while (!isAtEnd())
		{
			final char ch = read();
			++characterNumber;
			handleLineBreak(ch);
			
			if (!isWhitespace(ch))
			{
				backtrack(ch);
				--characterNumber;
				return;
			}
		}
	}
	
	/**
	 * Checks if the character is a line break and updates line and character numbers accordingly
	 * 
	 * @param ch Character to check
	 */
	private void handleLineBreak(final char ch)
	{
		if (isLineBreak(ch))
		{
			++lineNumber;
			characterNumber = 0;
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
