package org.json.easy.policies;

import java.util.function.Consumer;
import org.json.easy.serialization.JSONToken;

/**
 * JSON print policy that generates compressed output
 * 
 * @since 1.0.0
 */
public class CondensedJSONPrintPolicy implements JSONPrintPolicy
{
	/**
	 * Prints object start prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeObjectStartPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous) {}
	
	/**
	 * Prints array start prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeArrayStartPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous) {}
	
	/**
	 * Prints object end prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeObjectEndPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous) {}
	
	/**
	 * Prints array end prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeArrayEndPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous) {}
	
	/**
	 * Prints object key prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeIdentifierPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous) {}
	
	/**
	 * Prints value prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeValuePrefix(final Consumer<Character> write, final int indentation, final JSONToken previous) {}
}
