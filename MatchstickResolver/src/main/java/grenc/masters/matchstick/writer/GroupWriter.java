package grenc.masters.matchstick.writer;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.run.filter.SolutionGroup;


public class GroupWriter
{
	private HashMap<SolutionGroup, Writer> writers;
	
	public GroupWriter ()
	{
		writers = new HashMap<>();
		new File(Writer.baseDirectory).mkdirs();
	}
	
	
	public void addWriterForGroup(SolutionGroup group)
	{
		writers.put(group, new Writer().makeNew(group.name()));
	}
	
	public void writeListToGroup(SolutionGroup group, List<EquationChangeSingle> ecsList)
	{
		writers.get(group).writeList(ecsList);
	}
	
	public void closeAll()
	{
		
		writers.values().forEach(Writer::close);
	}
	

}
