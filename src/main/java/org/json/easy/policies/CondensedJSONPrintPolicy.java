package org.json.easy.policies;

import java.util.function.Consumer;

/**
 * Print policy that generates compressed output
 */
public class CondensedJSONPrintPolicy implements JSONPrintPolicy
{
	/**
	 * Prints a line terminator
	 * 
	 * @param write Writes a single character
	 */
	@Override
	public void writeLineTerminator(Consumer<Character> write) {}
	
	/**
	 * Prints a whitespace
	 * 
	 * @param write Writes a single character
	 */
	@Override
	public void writeSpace(Consumer<Character> write) {}
	
	/**
	 * Prints specific amount of tabs
	 * 
	 * @param write Writes a single character
	 * @param amount Amount of tabs to print
	 */
	@Override
	public void writeTabs(Consumer<Character> write, int amount) {}
}
