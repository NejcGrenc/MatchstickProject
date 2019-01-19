package grenc.masters.matchstick.writer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.run.main.ResolverProperties;


public class Writer
{
	public static final String baseDirectory = "target/groups";
	private static final String baseFileName = "/matchstickEquationChanges";
	
	private boolean writeOriginalAswell;
	private PrintWriter writer;
	
	
	Writer makeNew(String addedFileName)
	{
		try
		{
			writer = new PrintWriter(makeFullFileName(addedFileName), "UTF-8");
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		writeOriginalAswell = true;
		return this;
	}
	void close()
	{
		writer.close();
	}
	
	private String makeFullFileName(String addedFileName)
	{
		return baseDirectory + baseFileName + "_" + addedFileName + ".txt";
	}
	
	
	void writeList(List<EquationChangeSingle> ecsList)
	{
		newOriginalEquation();
		for (EquationChangeSingle ecs : ecsList)
			write(ecs);
	}
	void write(EquationChangeSingle ecs)
	{
		String line = createLine(ecs);
		writer.println(line);
	}
	void newOriginalEquation()
	{
		writeOriginalAswell = true;
	}
	
	private String createLine(EquationChangeSingle ecs)
	{
		String line = "";
		if (writeOriginalAswell)
			line += ecs.getOriginalEquation();
		else
			line += makeEmprty(ecs.getOriginalEquation().getElements().length);
		
		line += " -- ";
		line += ecs.getAdvancedAction();
		line += " -> ";
		line += ecs.getChangedEquation();
		
		writeOriginalAswell = false;
		return line;
	}
	private String makeEmprty(int length)
	{
		String s = "";
		for (int i = 0; i < length; i++)
			s += " ";
		return s;
	}
	
}
