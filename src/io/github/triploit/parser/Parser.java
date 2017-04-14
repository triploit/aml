package io.github.triploit.parser;

import io.github.triploit.parser.token.Token;
import io.github.triploit.parser.token.TokenType;

import java.util.ArrayList;

public class Parser
{
	private static ArrayList<String> tags = new ArrayList<>();
	private static String attributes = "";
	private static int line = 1;
	public static String code = "";
	private static boolean parenthis = false;

	public static void parse(final ArrayList<Token> tokens)
	{
		code = "";
		Token[] toks = new Token[tokens.size()];
		toks = tokens.toArray(toks);

		for (int i = 0; i < toks.length; i++)
		{
			if (toks[i].getType() == TokenType.TOKEN_TYPES.NEW_LINE)
			{
				line++;
				continue;
			}

			if (toks[i].getType() == TokenType.TOKEN_TYPES.TAG)
			{
				tags.add(toks[i].getValue());
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.PARENTHIS_OPEN)
			{
				parenthis = true;
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.PARENTHIS_CLOSE)
			{
				parenthis = false;
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.ATTRIBUTE && parenthis)
			{
				if (TokenType.isRightAttribut(toks[i].getValue(), tags.get(tags.size() - 1)))
				{
					String attr = toks[i].getValue();
					String value = "";

					if (toks[i + 1].getType() != TokenType.TOKEN_TYPES.DOUBLE_POINT)
					{
						error("Syntaxerror!");
					}
					else
					{
						value = toks[i + 2].getValue();

						attributes = attributes + " " + attr + "=\"" + value + "\"";
						i+=2;
						continue;
					}
				}
				else
				{
					error("Error: The attribute " + toks[i].getValue() + " doesn't likes " + tags.get(tags.size() - 1) + "!");
				}
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.BRACE_OPEN)
			{
				if (tags.size() == 0)
					error("Syntax Error! Missing Tag!?");

				String tag = tags.get(tags.size() - 1);

				if (!TokenType.existsTag(tag))
				{
					error(tag+" is a invalid tag!");
				}

				if (!code.endsWith("\n"))
					code = code + "\n";

				for (int c = 1; c < tags.size(); c++)
				{
					code = code + "\t";
				}

				code = code + "<" + tag + attributes + ">\n";
				attributes = "";
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.BRACE_CLOSE)
			{
				if (tags.size() == 0)
					return;

				if (!code.endsWith("\n"))
					code = code + "\n";

				for (int c = 1; c < tags.size(); c++)
				{
					code = code + "\t";
				}

				code = code + "</" + tags.get(tags.size() - 1) + ">\n";
				tags.remove(tags.size() - 1);
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.WORD)
				code = code + toks[i].getValue();
		}
	}

	public static void error(String msg)
	{
		System.out.println("Error --: "+msg);
		System.out.println("Line ---: " + line);
		System.exit(1);
	}
}