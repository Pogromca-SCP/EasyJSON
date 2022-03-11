package org.json.easy.serialization;

/**
 * Defines possible JSON tokens
 * 
 * @since 1.0.0
 */
public enum JSONToken
{
	NONE,
	COMMA,
	CURLY_OPEN,
	CURLY_CLOSE,
	SQUARE_OPEN,
	SQUARE_CLOSE,
	COLON,
	STRING,
	NUMBER,
	TRUE,
	FALSE,
	NULL,
	IDENTIFIER
}
