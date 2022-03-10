package org.json.easy.policies;

import java.util.function.Consumer;

/**
 * Print policy that generates human readable output
 */
public class PrettyJSONPrintPolicy implements JSONPrintPolicy
{
	/**
	 * Prints a line terminator
	 * 
	 * @param write Writes a single character
	 */
	@Override
	public void writeLineTerminator(Consumer<Character> write)
	{
		if (write != null)
		{
			write.accept('\r');
			write.accept('\n');
		}
	}
	
	/**
	 * Prints a whitespace
	 * 
	 * @param write Writes a single character
	 */
	@Override
	public void writeSpace(Consumer<Character> write)
	{
		if (write != null)
		{
			write.accept(' ');
		}
	}
	
	/**
	 * Prints specific amount of tabs
	 * 
	 * @param write Writes a single character
	 * @param amount Amount of tabs to print
	 */
	@Override
	public void writeTabs(Consumer<Character> write, int amount)
	{
		if (write != null)
		{
			for (int i = 0; i < amount; ++i)
			{
				write.accept('\t');
			}
		}
	}
}
