package org.json.easy.policies;

import java.util.function.Consumer;

/**
 * Interface to implement in order to create new JSON printing policy
 */
public interface JSONPrintPolicy
{
	/**
	 * Prints a line terminator
	 * 
	 * @param write Writes a single character
	 */
	void writeLineTerminator(Consumer<Character> write);
	
	/**
	 * Prints a whitespace
	 * 
	 * @param write Writes a single character
	 */
	void writeSpace(Consumer<Character> write);
	
	/**
	 * Prints specific amount of tabs
	 * 
	 * @param write Writes a single character
	 * @param amount Amount of tabs to print
	 */
	void writeTabs(Consumer<Character> write, int amount);
}
