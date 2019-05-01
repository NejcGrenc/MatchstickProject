package grenc.growscript.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class FileReader
{

	private PrintWriter out;
	private StringBuilder output;
	
	
	public FileReader(PrintWriter out)
	{
		this.out = out;
	}
	
	public FileReader(StringBuilder output)
	{
		this.output = output;
	}
	
	
	public void readFile(String fullFilename, StringBuilder output)
	{
		try 
		{
		    BufferedReader in = new BufferedReader(new java.io.FileReader(fullFilename));
		    String str;
		    while ((str = in.readLine()) != null) 
		    {
		    	output.append(str);
		    	output.append("\n");
		    }
		    in.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
		
	public void readFile(String fullFilename)
	{
		try 
		{
		    BufferedReader in = new BufferedReader(new java.io.FileReader(fullFilename));
		    String str;
		    while ((str = in.readLine()) != null) 
		    {
		    	out.println(str);
		    }
		    in.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	

}
