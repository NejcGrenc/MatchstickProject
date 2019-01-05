package grenc.masters.matchstick.confirm.parse;

import java.util.ArrayList;
import java.util.List;

import grenc.masters.matchstick.confirm.valid.ValidElement;
import grenc.masters.matchstick.objects.main.Element;
import grenc.masters.matchstick.objects.main.Equation;


public class Parser 
{	
	private static String unwantedspace = " "; 
			
	public Equation parseEquation(String unparsedEquation)
	{
		List<Element> elementList = new ArrayList<>();
		unparsedEquation = unparsedEquation.replace(unwantedspace, "");
		for (int i = 0; i < unparsedEquation.length(); i++)
			elementList.add(parseSymbol(Character.toString(unparsedEquation.charAt(i))));
		return new Equation(elementList.toArray(new Element[elementList.size()]));
	}
	
	public Element parseSymbol(String symbol)
	{
		for (ValidElement ve : ValidElement.allValidElements())
		{
			if (ve.getSymbol().equals(symbol))
				return newElement(ve);
		}
		throw new ParserException("No valid element for " + symbol);
	}
	private Element newElement(ValidElement ve)
	{
		return new Element(copyDisplay(ve));
	}
	private int[] copyDisplay(ValidElement ve)
	{
		int[] newDisplay = new int[ve.getDisplay().length];
		for (int i = 0; i < ve.getDisplay().length; i++)
			newDisplay[i] = ve.getDisplay()[i];
		return newDisplay;
	}
}

class ParserException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	public ParserException(String message)
	{
		super (message);
	}
}
