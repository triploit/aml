package io.github.triploit.parser.token;

/**
 * Created by survari on 14.04.17.
 */

public class Token
{
	private TokenType.TOKEN_TYPES type;
	private String value;

	public Token(TokenType.TOKEN_TYPES type, String value)
	{
		this.type = type;
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public TokenType.TOKEN_TYPES getType()
	{
		return type;
	}
}
