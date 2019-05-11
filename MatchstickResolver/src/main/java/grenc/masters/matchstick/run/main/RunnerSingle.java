package grenc.masters.matchstick.run.main;

import java.util.List;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.filter.GroupSelector;
import grenc.masters.matchstick.run.filter.SolutionGroup;
import grenc.masters.matchstick.run.filter.SolutionsFinder;

public class RunnerSingle
{
	public static void main(String[] args)
	{
		Equation currEq = new Parser().parseEquation("2 + 5 - 1 = 3");
		
		List<EquationChangeSingle> correctFinalEquations = new SolutionsFinder().getAllCorrectFinalEquations(currEq);
			
		SolutionGroup group = new GroupSelector(correctFinalEquations).findGroup();
		
		System.out.println(currEq + " (" + correctFinalEquations.size() + ")");
		System.out.println("Belongs to group: " + group.name());
		
		for(EquationChangeSingle solution : correctFinalEquations)
			System.out.println("---- " + solution.toString());

	}
}
