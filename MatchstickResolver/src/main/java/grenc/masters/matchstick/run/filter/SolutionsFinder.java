package grenc.masters.matchstick.run.filter;

import java.util.ArrayList;
import java.util.List;

import grenc.masters.matchstick.confirm.correct.EquationCalculator;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.changes.EquationChanges;
import grenc.masters.matchstick.objects.main.Action;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.main.ResolverProperties;

public class SolutionsFinder
{
	private static int maxMoves = Integer.parseInt(ResolverProperties.getProperty("resolver.maxMoves"));

	public List<EquationChangeSingle> getAllCorrectFinalEquations(Equation currentEquation)
	{
		List<EquationChangeSingle> correctFinalEquations = new ArrayList<>();
		for (int moves = 1; moves <= maxMoves; moves++)
		{
			EquationChanges chan = new EquationChanges(currentEquation, new Action(moves));
			for (EquationChangeSingle curr = chan.findNext(); curr != null; curr = chan.findNext())
			{
				// Filter out only correct equations
				if (new EquationCalculator().isCorrect(curr.getChangedEquation()))
				{
					correctFinalEquations.add(curr);
				}
			}
		}
		return correctFinalEquations;
	}
}
