package grenc.masters.matchstick.run.filter;

import java.util.LinkedList;
import java.util.List;

import grenc.masters.matchstick.backgroundtemplate.MatchstickComparator;
import grenc.masters.matchstick.backgroundtemplate.MatchstickNumeral;
import grenc.masters.matchstick.backgroundtemplate.MatchstickOperator;
import grenc.masters.matchstick.confirm.valid.ValidElement;
import grenc.masters.matchstick.run.main.ResolverProperties;

public class EquationStyle
{
	private String styleScript;
	
	EquationStyle(String styleScript)
	{
		this.styleScript = styleScript;
	}
	public EquationStyle()
	{
		this (ResolverProperties.getProperty("resolver.equationStyle"));
	}
	
	/**
	 * Parse the style String.
	 * N - Numeral
	 * O - Operator
	 * C - Comparator
	 */
	public AllValidEquations prepareValidEquationOptions()
	{
		List<ValidElement[]> equationOptions = new LinkedList<>();
		for (char elementType : styleScript.toCharArray())
		{
			switch (elementType) 
			{
				case 'N':
					equationOptions.add(MatchstickNumeral.getAllValidElements());
					break;
				
				case 'O':
					equationOptions.add(MatchstickOperator.getAllValidElements());
					break;
					
				case 'C':
					equationOptions.add(MatchstickComparator.getAllValidElements());
					break;
					
				default:
					throw new RuntimeException("Invalid schema character: " + elementType);
			}
		}
		return new AllValidEquations(equationOptions);
	}
	
}
