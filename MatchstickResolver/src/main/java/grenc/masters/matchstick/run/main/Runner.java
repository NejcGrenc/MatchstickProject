package grenc.masters.matchstick.run.main;

import java.util.ArrayList;
import java.util.List;

import grenc.masters.matchstick.confirm.correct.EquationCalculator;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.changes.EquationChanges;
import grenc.masters.matchstick.objects.main.Action;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.filter.AllValidEquations;
import grenc.masters.matchstick.run.filter.EquationStyle;
import grenc.masters.matchstick.run.filter.GroupSelector;
import grenc.masters.matchstick.run.filter.SolutionGroup;
import grenc.masters.matchstick.writer.GroupWriter;


public class Runner
{
	private static int maxMoves = Integer.parseInt(ResolverProperties.getProperty("resolver.maxMoves"));
	
	
	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();
		
		GroupWriter w = new GroupWriter();
		for (SolutionGroup g : SolutionGroup.values())
			w.addWriterForGroup(g);
	
		AllValidEquations equationMaker = new EquationStyle().prepareValidEquationOptions();
		for (Equation currEq = equationMaker.getNext(); 
				currEq != null; 
				currEq = equationMaker.getNext())
		{
			
			// Remove solved equations if starting with solved equations is prohibited
			if (! Settings.allowStartWithCorrectEquation)
			{
				if (new EquationCalculator().isCorrect(currEq))
					continue;
			}
			
			List<EquationChangeSingle> correctFinalEquations = new ArrayList<>();
			for (int moves = 1; moves <= maxMoves; moves++)
			{
				EquationChanges chan = new EquationChanges(currEq, new Action(moves));
				for (EquationChangeSingle curr = chan.findNext(); curr != null; curr = chan.findNext())
				{
					// Filter out only correct equations
					if (new EquationCalculator().isCorrect(curr.getChangedEquation()))
					{
						correctFinalEquations.add(curr);
					}
				}
			}
			
			SolutionGroup group = new GroupSelector(correctFinalEquations).findGroup();
			w.writeListToGroup(group, correctFinalEquations);
			
			System.out.println(currEq + " (" + correctFinalEquations.size() + ")");
		}
		
		w.closeAll();
		
		System.out.println("Finished in " + (System.currentTimeMillis() - startTime) + "ms.");
	}

}
