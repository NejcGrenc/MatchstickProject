package grenc.masters.matchstick.writer;

import java.io.PrintWriter;
import java.util.List;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.run.filter.SolutionGroup;
import grenc.masters.matchstick.run.main.ResolverProperties;


public abstract class Writer
{	
	/**
	 * Instead of whole solution list, write just the original equation to output.
	 * This is not used by database writer as there is space reserved for only original equations.
	 */
	protected static final boolean writeJustOriginal = Boolean.parseBoolean(ResolverProperties.getProperty("resolver.writer.writeJustOriginal"));

	protected boolean writeOriginalAswell;
	protected PrintWriter writer;
	
	protected SolutionGroup group;
	
	public Writer forGroup(SolutionGroup group)
	{
		this.group = group;
		return this;
	}
	
	public abstract Writer makeNew();
	
	void close()
	{
		writer.close();
	}
	
	
	public void writeList(List<EquationChangeSingle> ecsList)
	{
		if (writeJustOriginal)
		{
			if (!ecsList.isEmpty())
				writer.println(ecsList.get(0).getOriginalEquation());
				writer.flush();
			return;
		}
		
		newOriginalEquation();
		for (EquationChangeSingle ecs : ecsList)
			write(ecs);
	}
	
	void write(EquationChangeSingle ecs)
	{
		String line = createLine(ecs);
		writer.println(line);
		writer.flush();
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
