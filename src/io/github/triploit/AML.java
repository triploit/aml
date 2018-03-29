package io.github.triploit;

import io.github.triploit.parser.Parser;
import io.github.triploit.parser.Tokenizer;
import io.github.triploit.parser.token.Token;
import io.github.triploit.parser.token.TokenType;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by survari on 14.04.17.
 */
 
public class AML
{
	private static ArrayList<String> files = new ArrayList<>();
	private static String code = "";
	public static File _afile;
	public static Parser parser = new Parser();
	public static String curdir = "";

	public static int line = 1;
	public static int errors = 0;
	public static int warnings = 0;

	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.out.println("Error: Please give me a file :(!");
			return;
		}
		else
		{
			for (String arg : args)
			{
				if (arg.equalsIgnoreCase("-v"))
				{
					System.out.println("AML 0.1.4");
					continue;
				}

				compileFile(arg);
			}
		}
	}

	public static void compileFile(String arg)
	{
		code = "";
		files.clear();
		Main.line = 1;

		System.out.println("MAIN: Processing " + arg + " ...");
		File f = new File(arg);
		curdir = f.getAbsolutePath().substring(0, f.getAbsolutePath().length()-f.getName().length());

		System.out.println(curdir);
		code = readFile(arg);

		if (parser.parse(Tokenizer.tokenize(code)) > 0 && (errors > 0 || warnings > 0))
		{
			System.out.println("Build of "+arg+" cancelled with "+errors+" errors and "+warnings+" warnings.");
			System.exit(1);
			return;
		}

		code = parser.code;

		code = code.replace("~\\n~", "\n");
		code = code.replace("~<br>~", "\n");
		code = code.replace("~\\t~", "\t");

		/* System.out.println("[]=====================[ CODE BEGIN: "+arg+" ]=====================[]");
		System.out.println(code);
		System.out.println("[]=====================[ CODE END: "+arg+" ]=====================[]"); */
		String _of = arg;

		if (_of.contains(".aml"))
		{
			_of = _of.replace(".aml", ".html");
		}
		else
		{
			_of = _of + ".out.html";
		}

		if (errors > 0 || warnings > 0)
		{
			System.out.println("Build of "+arg+" cancelled with "+errors+" errors and "+warnings+" warnings.");
			System.exit(1);
		}

		System.out.println("Build of "+arg+" finished. Saved in \"" + _of + "\".");

		try
		{

			BufferedWriter bw = new BufferedWriter(new FileWriter(_of));
			bw.write(code);
			bw.close();
		}
		catch (IOException e)
		{
			System.out.println("MAIN: Error: You don't have writing permissions!");
			errors++;
		}
	}

	public static String parseAMLFile(String arg)
	{
		String code = readFile(arg);
		Parser p = new Parser();

		if (p.parse(Tokenizer.tokenize(code)) > 0 && (errors > 0 || warnings > 0))
		{
			System.out.println("Build of "+arg+" cancelled with "+errors+" errors and "+warnings+" warnings.");
			System.exit(1);
		}

		return p.code;
	}

	public static String readFile(String arg)
	{
		_afile = (new File(arg));
		File file = _afile;
		code = "";

		try
		{
			if (file.isDirectory())
			{
				System.out.println("FILE_READER: Error: The object \""+file.getAbsolutePath()+"\" is a directory!");
				errors++;
				return "";
			}

			BufferedReader br = new BufferedReader(new FileReader(arg));
			String line;

			while ((line = br.readLine()) != null)
			{
				if (line.trim().startsWith("#!"))
				{
					praeCommand(Tokenizer.tokenize(line.trim().substring(2, line.trim().length())));
				}
				else
				{
					code = parser.tab(code, parser.tags.size());
					code = code + line + "\n";
				}

				Main.line++;
			}

		}
		catch (FileNotFoundException e)
		{
			System.out.println("FILE_READER: Error: File "+file.getName()+" not found!");
			System.out.println("(LINE: "+Main.line+")");
			errors++;
			return "";
		}
		catch (IOException e)
		{
			System.out.println("FILE_READER: Error: You don't have reading permissions!");
			errors++;
			return "";
		}

		return code;
	}

	public static void praeCommand(ArrayList<Token> t)
	{
		for (int i = 0; i < t.size(); i++)
		{
			// System.out.println("TOKEN: <" + t[i].getType() + ", \"" + t.get(i).getValue() + "\">");

			if (t.get(i).getType() == TokenType.TOKEN_TYPES.IGNORE)
			{
				continue;
			}
			else if (t.get(i).getType() == TokenType.TOKEN_TYPES.ATTRIBUTE)
			{
				String attr = t.get(i).getValue().replace(":", "");
				String val = "";

				i++;
				while (t.get(i).getType() == TokenType.TOKEN_TYPES.IGNORE)
				{
					i++;
				}

				if (t.get(i).getType() == TokenType.TOKEN_TYPES.TAG)
				{
					val = t.get(i).getValue();
				}
				else if (t.get(i).getType() == TokenType.TOKEN_TYPES.STRING)
				{
					val = t.get(i).getValue();
				}
				else if (t.get(i).getType() == TokenType.TOKEN_TYPES.WORD)
				{
					val = t.get(i).getValue();
				}
				else
				{
					System.out.println("PRAE: ERROR: NO VALUE!\nLINE: "+Main.line);
					errors++;
				}

				if (val.startsWith("\"") && val.endsWith("\""))
					val = val.substring(1, val.length()-1);

				if (attr.equalsIgnoreCase("inc"))
				{
					if (System.getProperty("os.name").startsWith("Windows"))
					{
						val = curdir + "\\" + val;
					}
					else
					{
						val = curdir + "/" + val;
					}

					if (!(new File(val)).exists())
					{
						System.out.println("PRAE: ERROR: FILE NOT FOUND: \""+val+"\"\nLINE: "+Main.line);
						errors++;
					}
					else code = code + readFile(val);
				}

				continue;
			}
		}
	}
}
