package grenc.masters.matchstick.writer;

import java.util.List;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;


public class WriterDatabase extends Writer
{
	@Override
	public Writer makeNew()
	{
		return this;
	}
	
	@Override
	public void writeList(List<EquationChangeSingle> ecsList)
	{
		if (!ecsList.isEmpty())
			write(ecsList.get(0));
	}
	
	@Override
	void write(EquationChangeSingle ecs)
	{
		group.mapForDatabase().dao().insert(ecs.getOriginalEquation().toString());
	}
}
