package grenc.masters.matchstick.confirm.valid;

import java.util.Arrays;
import java.util.stream.Stream;

import grenc.masters.matchstick.backgroundtemplate.MatchstickComparator;
import grenc.masters.matchstick.backgroundtemplate.MatchstickNumeral;
import grenc.masters.matchstick.backgroundtemplate.MatchstickOperator;
import grenc.masters.matchstick.objects.main.Element;

public abstract class ValidElement //implements MatchstickTemplate
{

	protected int[] display;
	protected String symbol;
	
	private static ValidElement[] allValidElements;
	
	
	protected ValidElement (String symbol, int... n) 
	{
		this.symbol = symbol;
		this.display = n;
	}
	
	 
	public int[] getDisplay() 
	{
		return display;
	}
	public String getSymbol() 
	{
		return symbol;
	}
	public Element getElement()
	{
		return new Element(Arrays.copyOf(display, display.length));
	}
	
	public boolean equal(int[] d)
	{
		if (d.length != display.length)
			return false;
		for (int i = 0; i < display.length; i++)
			if (d[i] != getDisplay()[i])
				return false;
		return true;
	}
	
	
	public static ValidElement[] allValidElements()
	{
		if (allValidElements == null)
			allValidElements = Stream.concat(Arrays.stream(MatchstickNumeral.getAllValidElements()),
		 		   			   Stream.concat(Arrays.stream(MatchstickOperator.getAllValidElements()),
		 		   					   		 Arrays.stream(MatchstickComparator.getAllValidElements())))
		   					   .toArray(ValidElement[]::new);
		return allValidElements;
	}

}
