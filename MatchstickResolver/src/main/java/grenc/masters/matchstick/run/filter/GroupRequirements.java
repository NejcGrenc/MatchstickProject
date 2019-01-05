package grenc.masters.matchstick.run.filter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import grenc.masters.matchstick.objects.changes.AdvancedAction;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;


public interface GroupRequirements
{
	public boolean check(List<EquationChangeSingle> equations);
}

class Helper
{
	static Predicate<EquationChangeSingle> requirementsNumerals = a -> a.getAdvancedAction().isActionPerformedOnlyOnNumerals();
	static Predicate<EquationChangeSingle> requirementsOperators = a -> a.getAdvancedAction().isActionPerformedOnlyOnOperators();
		
	public static boolean solvableInOne(List<EquationChangeSingle> equations)
	{
		return equations.stream().anyMatch(a -> a.getAdvancedAction().isMove1());
	}
	
	public static boolean checkNumeralAndOperatorRequirements(List<EquationChangeSingle> equations, boolean strict)
	{
		List<EquationChangeSingle> numeralsEqs = filterForRequirement(equations, requirementsNumerals);
		List<EquationChangeSingle> operatorsEqs = filterForRequirement(equations, requirementsOperators);
		// Ensure that there is at least one equation in each category
		boolean numeralsNotNull = numeralsEqs.size() > 0;
		boolean operatorsNotNull = operatorsEqs.size() > 0;
		
		boolean noOthers = true;
		if (strict)
		{
			// Check if all equations fall into either category (no other categories)
			noOthers = numeralsEqs.size() + operatorsEqs.size() == equations.size();
		}
		
		return numeralsNotNull && operatorsNotNull && noOthers;
	}
	
	public static List<EquationChangeSingle> filterForRequirement(List<EquationChangeSingle> equations, Predicate<EquationChangeSingle> requirement)
	{
		return equations.stream().filter(requirement).collect(Collectors.toList());
	}
}



/**
 * Equations in this group can be solved solely by actions performed only on numerals
 */
class RequirementsGroupAA implements GroupRequirements
{
	Predicate<AdvancedAction> requirements = a -> a.isActionPerformedOnNumeralsAsWell();
	Predicate<AdvancedAction> strictRequirements = a -> a.isActionPerformedOnlyOnNumerals();

	
	@Override
	public boolean check(List<EquationChangeSingle> equations)
	{
		boolean move1 = equations.stream()
				.filter(a -> a.getAdvancedAction().isMove1())
				.map(EquationChangeSingle::getAdvancedAction).allMatch(strictRequirements);
		return Helper.solvableInOne(equations) && move1
				&& equations.stream().map(EquationChangeSingle::getAdvancedAction).allMatch(requirements);
	}
}

/**
 * Equations in this group can be solved in one move by actions performed only on numerals
 * and in 2 moves by either actions performed only on numerals or operators.
 */
class RequirementsGroupAB implements GroupRequirements
{
	Predicate<AdvancedAction> requirementsA = a -> a.isActionPerformedOnlyOnNumerals();

	
	@Override
	public boolean check(List<EquationChangeSingle> equations)
	{
		boolean move1 = equations.stream()
				.filter(a -> a.getAdvancedAction().isMove1())
				.map(EquationChangeSingle::getAdvancedAction).allMatch(requirementsA);
		List<EquationChangeSingle> move2Equations = equations.stream()
				.filter(a -> a.getAdvancedAction().isMove2()).collect(Collectors.toList());
		boolean move2 = Helper.checkNumeralAndOperatorRequirements(move2Equations, false);
		return Helper.solvableInOne(equations) && move1 && move2;
	}
}

/**
 * Equations in this group can be solved solely by actions performed only on operators
 * and in 2 moves by either actions performed only on numerals or operators.
 */
class RequirementsGroupBA implements GroupRequirements
{
	Predicate<AdvancedAction> requirementsB = a -> a.isActionPerformedOnlyOnOperators();

	@Override
	public boolean check(List<EquationChangeSingle> equations)
	{
		boolean move1 = equations.stream()
				.filter(a -> a.getAdvancedAction().isMove1())
				.map(EquationChangeSingle::getAdvancedAction).allMatch(requirementsB);
		List<EquationChangeSingle> move2Equations = equations.stream()
				.filter(a -> a.getAdvancedAction().isMove2()).collect(Collectors.toList());
		boolean move2 = Helper.checkNumeralAndOperatorRequirements(move2Equations, false);
		return Helper.solvableInOne(equations) && move1 && move2;
	}
}

/**
 * Equations in this group can be solved solely by actions performed only on operators
 */
class RequirementsGroupBB implements GroupRequirements
{
	Predicate<AdvancedAction> requirements = a -> a.isActionPerformedOnOperatorsAsWell();
	Predicate<AdvancedAction> strictRequirements = a -> a.isActionPerformedOnlyOnOperators();

	
	@Override
	public boolean check(List<EquationChangeSingle> equations)
	{
		boolean move1 = equations.stream()
				.filter(a -> a.getAdvancedAction().isMove1())
				.map(EquationChangeSingle::getAdvancedAction).allMatch(strictRequirements);
		return Helper.solvableInOne(equations) && move1
				&& equations.stream().map(EquationChangeSingle::getAdvancedAction).allMatch(requirements);
	}
}

/**
 * Equations in this group can be solved by one move actions performed only on numerals
 * or by one move actions performed only on operators.
 */
class RequirementsGroupCC implements GroupRequirements
{
	@Override
	public boolean check(List<EquationChangeSingle> equations)
	{
		List<EquationChangeSingle> move1Equations = equations.stream()
				.filter(a -> a.getAdvancedAction().isMove1()).collect(Collectors.toList());
		return Helper.solvableInOne(equations) && Helper.checkNumeralAndOperatorRequirements(move1Equations, true);
	}
}