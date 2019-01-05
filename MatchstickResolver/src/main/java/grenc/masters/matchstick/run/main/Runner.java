package grenc.masters.matchstick.run.main;

import java.util.ArrayList;
import java.util.List;

import grenc.masters.matchstick.confirm.correct.EquationCalculator;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.changes.EquationChanges;
import grenc.masters.matchstick.objects.main.Action;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.filter.AllValidEquations;
import grenc.masters.matchstick.run.filter.Group;
import grenc.masters.matchstick.writer.GroupWriter;


public class Runner
{

	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();
		
		GroupWriter w = new GroupWriter();
		for (Group g : Group.values())
			w.addWriterForGroup(g);
	
		AllValidEquations equationMaker = new AllValidEquations();
		for (Equation currEq = equationMaker.getNext(); 
				currEq != null; 
				currEq = equationMaker.getNext())
		{
			if (! Settings.allowStartWithCorrectEquation)
			{
				if (new EquationCalculator().isCorrect(currEq))
					continue;
			}
			
			List<EquationChangeSingle> correctFinalEquations = new ArrayList<>();
			for (Action action : Settings.actions)
			{
				EquationChanges chan = new EquationChanges(currEq, action);
				for (EquationChangeSingle curr = chan.findNext(); curr != null; curr = chan.findNext())
				{
					// Filter out only correct equations
					if (new EquationCalculator().isCorrect(curr.getChangedEquation()))
					{
						correctFinalEquations.add(curr);
					}
				}
			}
			
			for (Group g : Group.values())
				writeForGroupIfSuffices(w, g, correctFinalEquations);
			
			System.out.println(currEq + " (" + correctFinalEquations.size() + ")");
		}
		
		w.closeAll();
		
		System.out.println("Finished in " + (System.currentTimeMillis() - startTime) + "ms.");
	}
	
	private static void writeForGroupIfSuffices(GroupWriter w, Group group, List<EquationChangeSingle> ecsList)
	{
		if (group.sufficesRequirements(ecsList))
			w.writeListToGroup(group, ecsList);
	}

}
