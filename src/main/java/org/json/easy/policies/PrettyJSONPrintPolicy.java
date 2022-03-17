package org.json.easy.policies;

import java.util.function.Consumer;
import org.json.easy.serialization.JSONToken;

/**
 * JSON print policy that generates human readable output
 * 
 * @since 1.0.0
 */
public class PrettyJSONPrintPolicy implements JSONPrintPolicy
{
	/**
	 * Prints object start prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeObjectStartPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous)
	{
		if (previous != JSONToken.NONE)
		{
			writeValuePrefix(write, indentation, previous);
		}
	}
	
	/**
	 * Prints array start prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeArrayStartPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous)
	{
		if (previous != JSONToken.NONE)
		{
			writeValuePrefix(write, indentation, previous);
		}
	}
	
	/**
	 * Prints object end prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeObjectEndPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous)
	{
		if (write != null && previous != JSONToken.CURLY_OPEN)
		{
			writeLineTerminator(write);
			writeTabs(write, indentation);
		}
	}
	
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
	public void writeIdentifierPrefix(final Consumer<Character> write, final int indentation, final JSONToken previous)
	{
		if (write != null)
		{
			writeLineTerminator(write);
			writeTabs(write, indentation);
		}
	}
	
	/**
	 * Prints value prefix
	 * 
	 * @param write Writes a single character
	 * @param indentation Current indentation level
	 * @param previous Previous JSON token type
	 */
	@Override
	public void writeValuePrefix(final Consumer<Character> write, final int indentation, final JSONToken previous)
	{
		if (write != null && previous != JSONToken.SQUARE_OPEN)
		{
			writeSpace(write);
		}
	}
	
	/**
	 * Prints a line terminator
	 * 
	 * @param write Writes a single character
	 */
	private void writeLineTerminator(final Consumer<Character> write)
	{
		write.accept('\n');
	}
	
	/**
	 * Prints a whitespace
	 * 
	 * @param write Writes a single character
	 */
	private void writeSpace(final Consumer<Character> write)
	{
		write.accept(' ');
	}
	
	/**
	 * Prints specific amount of tabs
	 * 
	 * @param write Writes a single character
	 * @param amount Amount of tabs to print
	 */
	private void writeTabs(final Consumer<Character> write, final int amount)
	{
		for (int i = 0; i < amount; ++i)
		{
			write.accept('\t');
		}
	}
}
