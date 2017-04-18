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
		code = code.replace("\n", "~");
		boolean isStr = false;

		String tmp = "";
		ArrayList<Token> tokens = new ArrayList<>();

		for (int i = 0; i < code.length(); i++)
		{
			if (code.charAt(i) == '\"')
			{
				if ((i-1) >= 0 && code.charAt(i-1) != '\\')
				{
					tmp += code.charAt(i);

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
					tmp += code.charAt(i);
			}
			else
			{
				if (isStr)
				{
					if (code.charAt(i) == '~')
					{
						if (code.length() >= 1 && i >= 1)
						{
							if (code.charAt(i-1) != '\\')
							{
								tmp += "~\\n~";
							}
						}
						else
							tmp += "~\\n~";

						continue;
					}

					tmp += code.charAt(i);
					continue;
				}

				if (code.charAt(i) == '~')
				{
					tokens.add((new Token(TokenType.TOKEN_TYPES.NEW_LINE, "\n")));
					continue;
				}

				if (isIgnore(code.charAt(i)))
				{
					if (tmp != null &&
							tmp.length() != 0 &&
							tmp != "")
						tokens.add((new Token(TokenType.getRightType(tmp), tmp)));

					tmp = ""+code.charAt(i);
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
			case '\t':
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

