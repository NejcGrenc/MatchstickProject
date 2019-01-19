package grenc.masters.matchstick.writer;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.run.filter.SolutionGroup;
import grenc.masters.matchstick.run.main.ResolverProperties;


public class GroupWriter
{
	private static final String writerType = ResolverProperties.getProperty("resolver.writer.type");
	
	private HashMap<SolutionGroup, Writer> writers;
	
	public GroupWriter ()
	{
		writers = new HashMap<>();
		
		if ("document".equals(writerType))
			new File(WriterDocument.baseDirectory).mkdirs();
	}
	
	public void addWriterForGroup(SolutionGroup group)
	{
		writers.put(group, createFresh().forGroup(group).makeNew());
	}
	
	public void writeListToGroup(SolutionGroup group, List<EquationChangeSingle> ecsList)
	{
		writers.get(group).writeList(ecsList);
	}
	
	public void closeAll()
	{
		writers.values().forEach(Writer::close);
	}
	

	private Writer createFresh()
	{
		switch(writerType)
		{
			case "console":
				return new WriterConsole();
				
			case "document":
				return new WriterDocument();
				
			case "database":
				return new WriterDatabase();
				
			default:
				throw new RuntimeException("Unrecognizable writer property 'resolver.writer.type': " + writerType);
		}
	}
	
}
