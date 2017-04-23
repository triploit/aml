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
public class Main
{
	public static ArrayList<String> files = new ArrayList<>();
	public static String code = "";
	public static File _afile;
	public static String _afile_name = "";
	private static int line = 1;

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
					System.out.println("AML 0.0.1");
					continue;
				}

				code = "";
				files.clear();
				Main.line = 1;

				System.out.println("MAIN: Processing " + arg + " ...");
				code = _read_file(arg);

				Parser.parse(Tokenizer.tokenize(code));
				code = Parser.code;

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

				System.out.println("MAIN: Saved in: " + _of + "!");

				try
				{
					BufferedWriter bw = new BufferedWriter(new FileWriter(_of));
					bw.write(code);
					bw.close();
				}
				catch (IOException e)
				{
					System.out.println("MAIN: Error: You don't have writing permissions!");
				}
			}
		}
	}

	private static String _read_file(String arg)
	{
		_afile = (new File(arg));
		File file = _afile;
		_afile_name = arg;
		code = "";

		try
		{
			if (file.isDirectory())
			{
				System.out.println("FILE_READER: Error: The \"file\" is a directory!");
				return "";
			}

			BufferedReader br = new BufferedReader(new FileReader(arg));
			String line;

			while ((line = br.readLine()) != null)
			{
				if (line.trim().startsWith("#!"))
				{
					_prae_command(Tokenizer.tokenize(line.trim().substring(2, line.trim().length())));
				}
				else
				{
					code = code + line + "\n";
				}

				Main.line++;
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("FILE_READER: Error: File not found!");
			return "";
		}
		catch (IOException e)
		{
			System.out.println("FILE_READER: Error: You don't have reading permissions!");
			return "";
		}

		return code;
	}

	public static void _prae_command(ArrayList<Token> t)
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
				else if (t.get(i).getType() == TokenType.TOKEN_TYPES.WORD)
				{
					val = t.get(i).getValue().substring(2, t.get(i).getValue().length());
				}
				else
				{
					System.out.println("PRAE: ERROR: NO VALUE!\nLINE: "+Main.line);
				}

				if (attr.equalsIgnoreCase("inc"))
				{
					if (System.getProperty("os.name").startsWith("Windows"))
					{
						val = _afile.getAbsolutePath().substring(0, _afile.getAbsolutePath().length() - _afile_name.split("/")[_afile_name.split("/").length - 1].length() - 1) + "\\" + val;
					}
					else
					{
						val = _afile.getAbsolutePath().substring(0, _afile.getAbsolutePath().length() - _afile_name.split("/")[_afile_name.split("/").length - 1].length() - 1) + "/" + val;
					}

					code = code + _read_file(val);
					System.out.println("VALUE: "+code);
				}

				continue;
			}
		}
	}
}
