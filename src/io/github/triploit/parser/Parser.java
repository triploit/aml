package io.github.triploit.parser;

import io.github.triploit.Main;
import io.github.triploit.parser.token.Token;
import io.github.triploit.parser.token.TokenType;

import java.util.ArrayList;

public class Parser
{
	public ArrayList<String> tags = new ArrayList<>();
	public String attributes = "";
	private int line = 1;
	public String code = "";

	public int parse(final ArrayList<Token> tokens)
	{
		code = "";
		Token[] toks = new Token[tokens.size()];
		toks = tokens.toArray(toks);

		int brace = 0;
		int parenthis = 0;

		for (int i = 0; i < toks.length; i++)
		{
			if (toks[i].getType() == TokenType.TOKEN_TYPES.IGNORE)
				continue;

			// System.out.println("TOKEN: <"+toks[i].getValue().replace("\n", "")+", "+toks[i].getType().toString()+">");

			if (toks[i].getType() == TokenType.TOKEN_TYPES.NEW_LINE)
			{
				line++;
				continue;
			}

			if (toks[i].getType() == TokenType.TOKEN_TYPES.TAG)
			{
				tags.add(toks[i].getValue());
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.SEMICOLON)
			{
				String tag = tags.get(tags.size() - 1);

				code = tab(code, tags.size());
				code += "<"+tag+" "+attributes+">";

				if (tags.size() == 0)
					return 0;

				if (!code.endsWith("\n"))
					code = code + "\n";

				code = tab(code, tags.size());
				code = code + "</" + tag + ">\n";

				tags.remove(tags.size() - 1);
				attributes = "";

				code = code + "\n";
				Main.line++;
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.PARENTHIS_OPEN)
			{
				parenthis++;
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.PARENTHIS_CLOSE)
			{
				parenthis--;
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.ATTRIBUTE && parenthis >= 1)
			{
				String attr = toks[i].getValue();
				String value = "";

				if (!attr.endsWith(":"))
				{
					return error("Syntaxerror!");
				}
				else
				{
					i = ignoreSpaces(toks, i);
					value = toks[i + 1].getValue();

					attributes = attributes + " " + attr.replace(":", "") + "=" + value + "";
					i += 1;
					continue;
				}
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.BRACE_OPEN)
			{
				brace++;

				if (tags.size() == 0)
					return error("Syntax Error! Missing Tag!?");

				String tag = tags.get(tags.size() - 1);

				if (!code.endsWith("\n"))
					code = code + "\n";

				for (int c = 1; c < tags.size(); c++)
				{
					code = code + "\t";
				}

				if (tag.equalsIgnoreCase("script") || tag.equalsIgnoreCase("style") || tag.equalsIgnoreCase("php"))
				{
					if (tag.equalsIgnoreCase("php"))
						code = code + "<php?";
					else
						code = code + "<" + tag + attributes + ">";
					i++;
					code = tab(code, tags.size()) + "\t";

					for (int c = 1; c > 0; i++)
					{
						if (toks[i].getType() == TokenType.TOKEN_TYPES.IGNORE)
						{
							code += toks[i].getValue();
							i = ignoreSpaces(toks, i) - 1;
						}

						if (toks[i].getValue().equals("\n"))
						{
							i++;
							i = ignoreSpaces(toks, i) - 1;
							code += "\n";

							if (toks[i].getType() == TokenType.TOKEN_TYPES.BRACE_CLOSE)
								code = tab(code, tags.size()+c-1);
							else
								code = tab(code, tags.size()+c);
						}

						if (toks[i].getType() == TokenType.TOKEN_TYPES.NEW_LINE)
						{
							line++;
							continue;
						}

						if (toks[i].getType() == TokenType.TOKEN_TYPES.BRACE_CLOSE)
						{
							c--;

							if (c == 0)
								brace--;
						}
						else if (toks[i].getType() == TokenType.TOKEN_TYPES.BRACE_OPEN)
						{
							c++;
						}

						if (c != 0)
						{
							code += toks[i].getValue();
						}
					}

					if (tag.equalsIgnoreCase("php")) code = code + "?>\n\n";
					else code = code + "</" + tag + ">\n\n";
					tags.remove(tags.size() - 1);
				}
				else
					code = code + "<" + tag + attributes + ">\n";

				attributes = "";
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.BRACE_CLOSE)
			{
				brace--;
				String tag = tags.get(tags.size() - 1);

				if (tags.size() == 0)
					return 0;

				if (!code.endsWith("\n"))
					code = code + "\n";

				code = tab(code, tags.size());

				code = code + "</" + tag + ">\n";
				tags.remove(tags.size() - 1);

				code = code + "\n";
				Main.line++;
			}
			else if (toks[i].getType() == TokenType.TOKEN_TYPES.STRING)
			{
				if (toks[i].getValue().length() < 2)
					return 0;

				code = tab(code, tags.size()+1);

				String word = toks[i].getValue().substring(1, toks[i].getValue().length() - 1);
				word = word.replace("\\\"", "\"");
				word = word.replace("\\t", "\t");

				word = word.replace("\\n", "<br>");
				code = code + word + "\n";
			}
		}

		if (parenthis != 0)
			return error("You forgot to close/open a parenthis!");

		if (brace < 0)
			return error("You have to few \"{\" !");
		else if (brace > 0)
			return error("You have to few  \"}\" !");

		return 0;
	}

	private int ignoreSpaces(Token[] toks, int i)
	{
		for (; toks[i].getType() == TokenType.TOKEN_TYPES.IGNORE ||
				toks[i].getValue().equals(" "); i++)
		{
		}

		return i + 1;
	}

	public String tab(String code, int size)
	{
		for (int c = 1; c < size; c++)
		{
			code = code + "\t";
		}

		return code;
	}

	public int error(String msg)
	{
		System.out.println("Error --: " + msg);
		System.out.println("Line ---: " + line);
		Main.errors++;
		return 1;
	}
}