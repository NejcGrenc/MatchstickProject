package grenc.masters.matchstick.run.actualdata;

import org.junit.Test;

import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.matchstick.run.filter.SolutionGroup;

public class CountEachTest
{
	@Test
	public void shouldCountEachGroup()
	{
		for (SolutionGroup group : SolutionGroup.values())
			printForGroup(group);
	}
	
	private void printForGroup(SolutionGroup group)
	{
		int count = QueryBuilder.newCount().fromTable(group.name()).execute(); 
		String text = String.format("Group %16s: %5d", group.name(), count);
		System.out.println(text);
	}
}
