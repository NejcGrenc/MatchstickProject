package grenc.masters.matchstick.run.main;

import java.util.List;

import grenc.masters.matchstick.confirm.correct.EquationCalculator;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.filter.AllValidEquations;
import grenc.masters.matchstick.run.filter.EquationStyle;
import grenc.masters.matchstick.run.filter.GroupSelector;
import grenc.masters.matchstick.run.filter.SolutionGroup;
import grenc.masters.matchstick.run.filter.SolutionsFinder;
import grenc.masters.matchstick.writer.GroupWriter;


public class Runner
{

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
			
			List<EquationChangeSingle> correctFinalEquations = new SolutionsFinder().getAllCorrectFinalEquations(currEq);
			
			SolutionGroup group = new GroupSelector(correctFinalEquations).findGroup();
			if (group != null)
				w.writeListToGroup(group, correctFinalEquations);
			
			System.out.println(currEq + " (" + correctFinalEquations.size() + ")");
		}
		
		w.closeAll();
		
		System.out.println("Finished in " + (System.currentTimeMillis() - startTime) + "ms.");
	}

}
