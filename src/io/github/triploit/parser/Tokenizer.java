package io.github.triploit.parser;

import io.github.triploit.parser.token.Token;
import io.github.triploit.parser.token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by survari on 14.04.17.
 */
public class Tokenizer
{
	public static ArrayList<Token> tokenize(String code)
	{
		code = code.trim();
		String[] lines = code.split("\n");
		boolean isStr = false;

		String tmp = "";
		ArrayList<Token> tokens = new ArrayList<>();

		for (String line : Arrays.asList(lines))
		{
			code = line;

			for (int i = 0; i < code.length(); i++)
			{
				if (code.charAt(i) == '\"')
				{
					if (isStr)
					{

						if (tmp != null &&
								tmp.length() != 0 &&
								tmp != "")
							tokens.add((new Token(TokenType.TOKEN_TYPES.WORD, tmp)));

						tmp = "";

						isStr = false;
					}
					else
						isStr = true;
				}
				else
				{
					if (isStr)
					{
						if (code.charAt(i) == '~' && code.charAt(i-1) != '\\')
						{
							tmp += "~\\n~";
							continue;
						}

						tmp += code.charAt(i);
						continue;
					}

					if (code.charAt(i) == '~')
					{
						tokens.add((new Token(TokenType.TOKEN_TYPES.NEW_LINE, null)));
						continue;
					}

					if (isIgnore(code.charAt(i)))
					{
						if (tmp != null &&
								tmp.length() != 0 &&
								tmp != "")
							tokens.add((new Token(TokenType.getRightType(tmp), tmp)));
						tmp = "";
					}
					else if (isSpecial(code.charAt(i)))
					{
						if (tmp != null &&
								tmp.length() != 0 &&
								tmp != "")
							tokens.add((new Token(TokenType.getRightType(tmp), tmp)));

						tmp = ""+code.charAt(i);

						if (tmp != null &&
								tmp.length() != 0 &&
								tmp != "")
							tokens.add((new Token(TokenType.getRightType(tmp), tmp)));

						tmp = "";
					}
					else
					{
						tmp += code.charAt(i);
					}
				}
			}
		}

		return tokens;
	}

	public static boolean isSpecial(char c)
	{
		switch (c)
		{
			case '{':
				return true;
			case '}':
				return true;
			case '(':
				return true;
			case ')':
				return true;
			case ':':
				return true;
			case '\"':
				return true;
			default:
				return false;
		}
	}

	public static boolean isIgnore(char c)
	{
		switch (c)
		{
			case ' ':
				return true;
			case ',':
				return true;
			case ';':
				return true;
			case '\n':
				return true;
			default:
				return false;
		}
	}
}

