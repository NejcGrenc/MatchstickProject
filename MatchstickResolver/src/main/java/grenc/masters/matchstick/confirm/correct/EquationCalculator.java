package grenc.masters.matchstick.confirm.correct;

import java.util.ArrayList;
import java.util.List;

import grenc.masters.matchstick.backgroundtemplate.MatchstickComparator;
import grenc.masters.matchstick.backgroundtemplate.MatchstickNumeral;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.main.Settings;

public class EquationCalculator 
{

	private List<String> left;
	private List<String> right;
	private String comparator;
	
	private String[] allElements;
	

	public boolean isCorrect(Equation equation)
	{
		return isCorrect(equation.toString());
	}
	public boolean isCorrect(String stringEquation)
	{
		Boolean result = null;
		
		if (Settings.useEquationCache)
			result = EquationCorrectCache.attemptToGet(stringEquation);
		if (result != null)
			return result;
		
		// Reset all variables
		allElements = stringEquation.trim().split("");
		left = new ArrayList<>();	
		right = new ArrayList<>();
		comparator = null;
		
		try 
		{
			split();
			combineNumbers(left);
			combineNumbers(right);
			calculatePart(left);
			calculatePart(right);
		} 
		catch (EquationCalculatorException e)
		{
			// Caught division by zero
			if (Settings.useEquationCache)
				EquationCorrectCache.put(stringEquation, false);
			
			return false;
		}
		
		result = compare();
		
		if (Settings.useEquationCache)
			EquationCorrectCache.put(stringEquation, result);
		
		return result;
	}
	
	private void split()
	{
		int noComparators = 0;
		for (int i = 0; i < allElements.length; i++)
		{
			String element = allElements[i];
			if (isComaparator(element))
			{
				if (noComparators != 0)
					throw new EquationCalculatorException("Too many comparators.");
				comparator = element;
				noComparators++;
				continue;
			}
			
			if (noComparators == 0)
				left.add(element);
			else
				right.add(element);					
		}
		
		// Check
		if (comparator == null)
			throw new EquationCalculatorException("No comparator found.");
	}
	private boolean isComaparator(String single)
	{
		for (MatchstickComparator c : MatchstickComparator.getAllValidElements())
			if (c.getSymbol().equals(single))
				return true;
		return false;
	}
	
	
	private void combineNumbers(List<String> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			if (isNumeral(list.get(i)))
			{
				int sequence = 1;
				while (i+sequence < list.size() && isNumeral(list.get(i+sequence)) ) 
					sequence ++;
				if (sequence > 1)
				{
					String newElement = String.join("", list.subList(i, i+sequence));
					for (int rem = i; rem < i+sequence; rem++)
						list.remove(i);
					list.add(i, newElement);
				}
			}
		}
	}
	private boolean isNumeral(String single)
	{
		for (MatchstickNumeral n : MatchstickNumeral.getAllValidElements())
			if (n.getSymbol().equals(single))
				return true;
		return false;
	}
	
	private void calculatePart(List<String> list)
	{
		tryMultiplicationDivision(list);
		tryAdditionSubtraction(list);
	}
	private void tryMultiplicationDivision(List<String> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			if (!list.get(i).equals("*") && !list.get(i).equals("/"))
				continue;
			
			int leftNumeralValue = Integer.parseInt(list.get(i-1));
			int rightNumeralValue = Integer.parseInt(list.get(i+1));			
			int newValue;
			if (list.get(i).equals("*"))
			{
				newValue = leftNumeralValue * rightNumeralValue;
			}
			else
			{
				if (rightNumeralValue == 0)
					throw new EquationCalculatorException("Division by zero.");
				newValue = leftNumeralValue / rightNumeralValue;
				if (newValue * rightNumeralValue != leftNumeralValue)
					throw new EquationCalculatorException("Division with remainder.");
			}
			
			for (int rem = 0; rem < 3; rem++)
				list.remove(i-1);
			list.add(i-1, String.valueOf(newValue));
			
			i--;
		}
	}
	private void tryAdditionSubtraction(List<String> list) 
	{
		for (int i = 0; i < list.size(); i++) 
		{
			if (!list.get(i).equals("+") && !list.get(i).equals("-"))
				continue;
			
			int leftNumeralValue = Integer.parseInt(list.get(i-1));
			int rightNumeralValue = Integer.parseInt(list.get(i+1));			
			int newValue;
			if (list.get(i).equals("+"))
			{
				newValue = leftNumeralValue + rightNumeralValue;
			}
			else
			{
				newValue = leftNumeralValue - rightNumeralValue;
			}
			
			for (int rem = 0; rem < 3; rem++)
				list.remove(i-1);
			list.add(i-1, String.valueOf(newValue));
			
			i--;
		}
	}

	private boolean compare()
	{
		switch (comparator)
		{
			case "=":
				return Integer.valueOf(left.get(0)) == Integer.valueOf(right.get(0));
			case "!":
				return Integer.valueOf(left.get(0)) != Integer.valueOf(right.get(0));
			case "<":
				return Integer.valueOf(left.get(0))  < Integer.valueOf(right.get(0));
			case ">":
				return Integer.valueOf(left.get(0))  > Integer.valueOf(right.get(0));
			default:
				throw new EquationCalculatorException("Unknown comparator: " + comparator);
		}
	}
	
}

class EquationCalculatorException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	public EquationCalculatorException(String message)
	{
		super (message);
	}
}

