package grenc.growscript.service.utils.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import grenc.growscript.service.exception.GrowScriptException;
import grenc.growscript.service.utils.Encoding;


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
	
	
	public String readFileFromResources(String fileName)
	{
		return readFile(getFilePathFromResources(fileName));
	}
	
	public String getFilePathFromResources(String fileName)
	{
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null)
		{
			throw new GrowScriptException("Cannot find file: " + fileName);
		}
		return new File(resource.getFile()).getAbsolutePath();
	}
	
	public String readFile(String fullFilename)
	{
		BufferedReader in = null;
		try 
		{
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fullFilename), Encoding.common));
			
		    String str;
		    while ((str = in.readLine()) != null) 
		    {
		    	output.append(str);
		    	output.append("\n");
		    }
		    output = output.delete((output.length() - "\n".length()), output.length());
		}
		catch (IOException e)
		{
			throw new GrowScriptException(e);
		}
		finally
		{	
			close(in);
		}
		return output.toString();
	}
	
	private void close(BufferedReader reader)
	{
		try
		{
			if (reader != null)
				reader.close();
		} 
		catch (IOException e)
		{
			throw new GrowScriptException(e);
		}
	}

}
