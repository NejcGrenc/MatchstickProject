package grenc.masters.matchstick.run.filter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.run.main.ResolverProperties;

public class GroupSelector
{
	private static int maxMoves = Integer.parseInt(ResolverProperties.getProperty("resolver.maxMoves"));
	
	static Predicate<EquationChangeSingle> requirementsNumerals = a -> a.getAdvancedAction().isActionPerformedOnlyOnNumerals();
	static Predicate<EquationChangeSingle> requirementsOperators = a -> a.getAdvancedAction().isActionPerformedOnlyOnOperators();
	
	/** This requirement mixes numeral and operand moves within single action */
	static Predicate<EquationChangeSingle> requirementsNumeralsAndOperators = a -> a.getAdvancedAction().isActionPerformedOnNumeralsAsWell() 
																				&& a.getAdvancedAction().isActionPerformedOnOperatorsAsWell();

	
	private final List<EquationChangeSingle> equationList;
	private boolean notSolvable = false;

	public GroupSelector(List<EquationChangeSingle> equations)
	{
		this.equationList = equations;
	}
	
	
	private boolean solvableInNumberOfMoves(List<EquationChangeSingle> equations, int moves)
	{
		return equations.stream().anyMatch(a -> solvableInNumberOfMoves(a, moves));
	}
	
	private boolean solvableInNumberOfMoves(EquationChangeSingle equations, int noMoves)
	{
		return equations.getAdvancedAction().isMove(noMoves);
	}
	
	private List<EquationChangeSingle> filterForNoMoves(List<EquationChangeSingle> equations, int moves)
	{
		return equations.stream().filter(a -> solvableInNumberOfMoves(a, moves)).collect(Collectors.toList());

	}
	
	
	private FitType findListFitType(List<EquationChangeSingle> equations)
	{
		List<EquationChangeSingle> numeralsEqs = filterForRequirement(equations, requirementsNumerals);
		List<EquationChangeSingle> operatorsEqs = filterForRequirement(equations, requirementsOperators);
		List<EquationChangeSingle> mixedEqs = filterForRequirement(equations, requirementsNumeralsAndOperators);
		
		// Ensure that there is at least one equation in each category
		boolean mixedNotNull = mixedEqs.size() > 0;
		boolean numeralsNotNull = numeralsEqs.size() > 0;
		boolean operatorsNotNull = operatorsEqs.size() > 0;
		
		if (mixedNotNull)
			return FitType.NUMERALS_AND_OPERATORS;
		if (numeralsNotNull && operatorsNotNull)
			return FitType.NUMERALS_OR_OPERATORS;
		if (numeralsNotNull)
			return FitType.NUMERALS_ONLY;
		if (operatorsNotNull)
			return FitType.OPERATORS_ONLY;
		
		notSolvable = true;
		throw new RuntimeException("No valid FitType found!");
	}
	
	private static List<EquationChangeSingle> filterForRequirement(List<EquationChangeSingle> equations, Predicate<EquationChangeSingle> requirement)
	{
		return equations.stream().filter(requirement).collect(Collectors.toList());
	}
	
	
	public SolutionGroup findGroup()
	{
		String builtGroupName = "group" + parseForMoves(1);
		
		if (notSolvable)
			return null;
		
		return SolutionGroup.findTheBestGroup(builtGroupName);
	}
	
	String parseForMoves(int moves)
	{
		if (moves > maxMoves)
			return "";
		
		if (solvableInNumberOfMoves(equationList, moves))
		{
			switch (findListFitType(filterForNoMoves(equationList, moves)))
			{	
				case NUMERALS_ONLY:
					return "_" + moves + FitType.NUMERALS_ONLY.title + parseForMoves(moves+1);
				case OPERATORS_ONLY:
					return "_" + moves + FitType.OPERATORS_ONLY.title + parseForMoves(moves+1);
				case NUMERALS_AND_OPERATORS:
					return "_" + moves + FitType.NUMERALS_AND_OPERATORS.title;
				case NUMERALS_OR_OPERATORS:
					return "_" + moves + FitType.NUMERALS_OR_OPERATORS.title;
				default:
					throw new RuntimeException ("Should not happen");
			}
		}
		else
		{
			//  Do for number 1 anyways
			if (moves == 1)
			{
				if (! solvableInNumberOfMoves(equationList, 2))
				{
					notSolvable = true;
					return "";
				}
				return "_" + moves + FitType.NONE.title + parseForMoves(2);
			}
			return "";
		}
	}
	
	
	
	enum FitType
	{
		NUMERALS_ONLY ("N"),
		OPERATORS_ONLY ("O"),
		NUMERALS_AND_OPERATORS ("MA"), // Mixed - mixed within action
		NUMERALS_OR_OPERATORS ("MO"), // Mixed - operator_only and numeral_only action
		NONE ("X");
		
		public String title;
		private FitType (String title)
		{
			this.title = title;
		}
	}
}
