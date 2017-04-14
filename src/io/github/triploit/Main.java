package io.github.triploit;

import io.github.triploit.parser.Parser;
import io.github.triploit.parser.Tokenizer;

import java.io.*;

/**
 * Created by survari on 14.04.17.
 */
public class Main
{
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
				String code = "";
				System.out.println("Processing "+arg+" ...");

				try
				{
					BufferedReader br = new BufferedReader(new FileReader(arg));
					String line;

					while ((line = br.readLine()) != null)
					{
						code = code + line + "~\n";
					}
				}
				catch (FileNotFoundException e)
				{
					System.out.println("Error: File not found!");
					return;
				}
				catch (IOException e)
				{
					System.out.println("Error: You don't have reading permissions!");
					return;
				}

				Parser.parse(Tokenizer.tokenize(code));
				code = Parser.code;

				code = code.replace("~\\n~", "\n");
				code = code.replace("~\\t~", "\t");

				System.out.println("[]=====================[ CODE BEGIN: "+arg+" ]=====================[]");
				System.out.println(code);
				System.out.println("[]=====================[ CODE END: "+arg+" ]=====================[]");

				try
				{
					BufferedWriter bw = new BufferedWriter(new FileWriter(arg.replace(".aml", ".html")));
					bw.write(code);
					bw.close();
				}
				catch (IOException e)
				{
					System.out.println("Error: You don't have writing permissions!");
				}
			}
		}
	}
}
