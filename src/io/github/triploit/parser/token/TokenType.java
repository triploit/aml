package io.github.triploit.parser.token;

import io.github.triploit.parser.Tokenizer;

/**
 * Created by survari on 14.04.17.
 */
public class TokenType
{
	public enum TOKEN_TYPES
	{
		TAG {
			@Override
			public String toString() {
				return "Tag";
			}
		},
		ATTRIBUTE {
			@Override
			public String toString() {
				return "Attribute";
			}
		},
		BRACE_OPEN {
			@Override
			public String toString() {
				return "Brace_Open";
			}
		},
		BRACE_CLOSE {
			@Override
			public String toString() {
				return "Brace_Close";
			}
		},
		PARENTHIS_OPEN {
			@Override
			public String toString() {
				return "Parenthis_Open";
			}
		},
		PARENTHIS_CLOSE {
			@Override
			public String toString() {
				return "Parenthis_Close";
			}
		},
		DOUBLE_POINT {
			@Override
			public String toString() {
				return "Double_Point";
			}
		},
		NEW_LINE {
			@Override
			public String toString() {
				return "New_Line";
			}
		},
		WORD {
			@Override
			public String toString() {
				return "Word";
			}
		},
		COMMA {
			@Override
			public String toString() {
				return "Comma";
			}
		},
		IGNORE {
			@Override
			public String toString() {
				return "Ignore";
			}
		},
		STRING {
			@Override
			public String toString() {
				return "String";
			}
		},
		SEMICOLON {
			@Override
			public String toString() {
				return "Semicolon";
			}
		}
	}

	public static final TOKEN_TYPES getRightType(String value)
	{
		if (!value.equals(" ") || value.length() != 1)
			value = value.trim();

		if (value.isEmpty())
			return TOKEN_TYPES.IGNORE;

		if ((value.startsWith("\"") && value.endsWith("\"")) ||
				(value.startsWith("\'") && value.endsWith("\'")) )
			return TOKEN_TYPES.WORD;

		else if (value.equals("\n"))
			return TOKEN_TYPES.NEW_LINE;

		else if (value.equals("{"))
			return TOKEN_TYPES.BRACE_OPEN;

		else if (value.equals("}"))
			return TOKEN_TYPES.BRACE_CLOSE;

		else if (value.endsWith(":") && value.length() > 1)
			return TOKEN_TYPES.ATTRIBUTE;

		else if (value.equalsIgnoreCase(";"))
			return TOKEN_TYPES.SEMICOLON;

		else if (value.equals("("))
			return TOKEN_TYPES.PARENTHIS_OPEN;

		else if (value.equals(")"))
			return TOKEN_TYPES.PARENTHIS_CLOSE;

		else if (value.equals(":"))
			return TOKEN_TYPES.DOUBLE_POINT;

		else if (value.equals(","))
			return TOKEN_TYPES.COMMA;

		else if (Tokenizer.isIgnore(value.charAt(0)))
			return TOKEN_TYPES.IGNORE;

		else
			return TOKEN_TYPES.TAG;
	}
}