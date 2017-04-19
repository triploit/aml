package io.github.triploit;

import io.github.triploit.parser.Parser;
import io.github.triploit.parser.Tokenizer;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by survari on 14.04.17.
 */
public class Main
{
    public static ArrayList<String> files = new ArrayList<>();

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
			    files.clear();
				String code = "";
				System.out.println("MAIN: Processing "+arg+" ...");

				code = _read_file(arg);

				Parser.parse(Tokenizer.tokenize(code));
				code = Parser.code;

				code = code.replace("~\\n~", "\n");
				code = code.replace("~\\t~", "\t");

				/* System.out.println("[]=====================[ CODE BEGIN: "+arg+" ]=====================[]");
				System.out.println(code);
				System.out.println("[]=====================[ CODE END: "+arg+" ]=====================[]"); */

				System.out.println("MAIN: Saved in: "+arg+".out.html!");

				try
				{
					BufferedWriter bw = new BufferedWriter(new FileWriter(arg+".out.html"));
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
		String code = "";
		File file = (new File(arg));

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
			    if (line.trim().startsWith("#!(inc: "))
                {
                    // 8 to (len-1)
                    String f = line.trim().substring(8, line.trim().length()-1);
                    f = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-arg.split("/")[arg.split("/").length-1].length()-1)+"/"+f;

                    System.out.println("Reading "+f+"...");

                    if (!files.contains(f))
                    {
                        files.add(f);
                        code = code + _read_file(f);
                    }
                    else
                    {
                        System.out.println("FILE_READER: Warning: File ignored!");
                    }
                }
                else
				    code = code + line + "\n";
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
}
