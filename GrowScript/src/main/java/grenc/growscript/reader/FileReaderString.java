package grenc.growscript.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import grenc.growscript.exception.GrowScriptException;


public class FileReaderString
{
	private StringBuilder output;
		
	public FileReaderString(StringBuilder output)
	{
		this.output = output;
	}
	public FileReaderString()
	{
		this.output = new StringBuilder();
	}
	
	
	public File getFileFromResources(String fileName)
	{
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null)
			throw new GrowScriptException("Cannot find file: " + fileName);
		return new File(resource.getFile());
	}
	
	public void readFile(String fullFilename)
	{
		BufferedReader in = null;
		try 
		{
			java.io.FileReader reader = new java.io.FileReader(getFileFromResources(fullFilename));

		    in = new BufferedReader(reader);
		    String str;
		    while ((str = in.readLine()) != null) 
		    {
		    	output.append(str);
		    	output.append("\n");
		    }
		    
		}
		catch (IOException e)
		{
			throw new GrowScriptException(e);
		}
		finally
		{
			
			try
			{
				if (in != null)
					in.close();
			} 
			catch (IOException e)
			{
				throw new GrowScriptException(e);
			}
		}
	}
	
	

}
