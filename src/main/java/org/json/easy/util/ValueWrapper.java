package org.json.easy.util;

/**
 * Wrapper class for passing an out parameter
 * 
 * @param <E> Wrapped value type
 */
public class ValueWrapper<E>
{
	/**
	 * Contains the value
	 */
	public E value;
	
	/**
	 * Creates new value wrapper
	 */
	public ValueWrapper() {}
	
	/**
	 * Creates new value wrapper
	 * 
	 * @param val Value to set
	 */
	public ValueWrapper(E val)
	{
		value = val;
	}
}
