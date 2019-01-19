package grenc.masters.matchstick.writer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import grenc.masters.matchstick.run.main.ResolverProperties;


public class WriterDocument extends Writer
{
	public static final String baseDirectory = "target/groups";
	private static final String baseFileName = "/" +  ResolverProperties.getProperty("resolver.writer.document.fileName");
	private static final String equationStyle = ResolverProperties.getProperty("resolver.equationStyle");
	
		
	@Override
	public Writer makeNew()
	{
		try
		{
			writer = new PrintWriter(makeFullFileName(group.name()), "UTF-8");
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		writeOriginalAswell = true;
		return this;
	}
	private String makeFullFileName(String addedFileName)
	{
		return baseDirectory + baseFileName + "-" + equationStyle + "_" + addedFileName + ".txt";
	}
	
	public Writer makeNewToConsole()
	{
		writer = new PrintWriter(System.out, true);
		writeOriginalAswell = true;
		return this;
	}
	


	
}
