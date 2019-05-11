package grenc.masters.matchstick.run.main;

import java.util.Arrays;
import java.util.List;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.filter.GroupSelector;
import grenc.masters.matchstick.run.filter.SolutionGroup;
import grenc.masters.matchstick.run.filter.SolutionsFinder;

public class RunnerSingle
{
	private static List<String> equation = Arrays.asList("2*3-3=5", "5/3+2=3", "3*5/5=2",
													"5+5-5=3", "2/2*3=2", "2*5-5=3",
													"5-5+2=3" , "3*2/3=3", "2*3-2=3",
													"3+3-5=5", "3*3-2=3", "2-5+2=5",
													"2/5+5=2", "5/5+5=5", "2+5/2=5");
	
	public static void main(String[] args)
	{
		for (String eq : equation)
			single(eq);
	}
	
	private static void single(String equation)
	{
		Equation currEq = new Parser().parseEquation(equation);
		
		SolutionsFinder finder = new SolutionsFinder();
		finder.setMaxMoves(2);
		List<EquationChangeSingle> correctFinalEquations = finder.getAllCorrectFinalEquations(currEq);
			
		SolutionGroup group = new GroupSelector(correctFinalEquations).findGroup();
		
		System.out.println(currEq + " (" + correctFinalEquations.size() + ")");
		System.out.println("Belongs to group: " + group.name());
		
		for(EquationChangeSingle solution : correctFinalEquations)
			System.out.println("" + solution.toString());
		
		System.out.println();
	}
}
