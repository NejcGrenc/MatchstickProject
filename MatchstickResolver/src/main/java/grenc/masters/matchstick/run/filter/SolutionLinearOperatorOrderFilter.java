package grenc.masters.matchstick.run.filter;

import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.matchstick.backgroundtemplate.MatchstickOperator;
import grenc.masters.matchstick.confirm.valid.Validator;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Element;
import grenc.masters.matchstick.objects.main.Equation;


public class SolutionLinearOperatorOrderFilter
{
	public boolean isFlawed(List<EquationChangeSingle> originalList)
	{
		List<EquationChangeSingle> filteredList = filter(originalList);
		return (originalList.size() != filteredList.size());
	}
	
	public List<EquationChangeSingle> filter(List<EquationChangeSingle> originalList)
	{
		return originalList.stream().filter(eqsg -> matchesRestriction(eqsg.getChangedEquation())).collect(Collectors.toList());
	}
	
	private boolean matchesRestriction(Equation equation)
	{
		Element firstOperand = equation.getElements()[1];
		Element secondOperand = equation.getElements()[3];

		return ! (isAdditionSubtraction(firstOperand) && isMultiplicationDivision(secondOperand));
	}
	
	private boolean isAdditionSubtraction(Element element)
	{
		MatchstickOperator plus = MatchstickOperator.getAllValidElements()[0];
		MatchstickOperator minus = MatchstickOperator.getAllValidElements()[1];

		return (Validator.getValidElement(element).equals(plus) || Validator.getValidElement(element).equals(minus));
	}
	
	private boolean isMultiplicationDivision(Element element)
	{
		MatchstickOperator multiplication = MatchstickOperator.getAllValidElements()[2];
		MatchstickOperator division = MatchstickOperator.getAllValidElements()[3];

		return (Validator.getValidElement(element).equals(multiplication) || Validator.getValidElement(element).equals(division));
	}
}
