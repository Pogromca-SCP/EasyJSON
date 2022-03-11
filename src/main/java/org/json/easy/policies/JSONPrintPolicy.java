package org.json.easy.policies;

import java.util.function.Consumer;
import org.json.easy.serialization.JSONToken;

/**
 * Interface to implement in order to create new JSON printing policy
 * 
 * @since 1.0.0
 */
public interface JSONPrintPolicy
{
	/**
	 * Prints object start prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	void writeObjectStartPrefix(Consumer<Character> write, int indentation, JSONToken previous);
	
	/**
	 * Prints array start prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	void writeArrayStartPrefix(Consumer<Character> write, int indentation, JSONToken previous);
	
	/**
	 * Prints object end prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	void writeObjectEndPrefix(Consumer<Character> write, int indentation, JSONToken previous);
	
	/**
	 * Prints array end prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	void writeArrayEndPrefix(Consumer<Character> write, int indentation, JSONToken previous);
	
	/**
	 * Prints object key prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	void writeIdentifierPrefix(Consumer<Character> write, int indentation, JSONToken previous);
	
	/**
	 * Prints value prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	void writeValuePrefix(Consumer<Character> write, int indentation, JSONToken previous);
}
