package grenc.masters.matchstick.writer;

import java.util.HashMap;
import java.util.List;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.run.filter.Group;


public class GroupWriter
{
	private HashMap<Group, Writer> writers;
	
	public GroupWriter ()
	{
		writers = new HashMap<>();
	}
	
	
	public void addWriterForGroup(Group group)
	{
		writers.put(group, new Writer().makeNew(group.getGroupName()));
	}
	
	public void writeListToGroup(Group group, List<EquationChangeSingle> ecsList)
	{
		writers.get(group).writeList(ecsList);
	}
	
	public void closeAll()
	{
		writers.values().forEach(Writer::close);
	}
	

}
