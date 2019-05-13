package grenc.masters.matchstick.confirm.valid;

import grenc.masters.matchstick.backgroundtemplate.MatchstickComparator;
import grenc.masters.matchstick.backgroundtemplate.MatchstickNumeral;
import grenc.masters.matchstick.backgroundtemplate.MatchstickOperator;
import grenc.masters.matchstick.objects.main.Element;


public class Validator 
{
	
	public static boolean isValid(Element e)
	{
		return getSymbol(e) != null;
	}
	
	public static String getSymbol(Element e)
	{
		ValidElement ve = getValidElement(e);
		return (ve != null) ? ve.getSymbol() : null;
	}
	
	public static ValidElement getValidElement(Element e)
	{
		ValidElement num = getValidNumeral(e);
		if (num != null) return num;
		ValidElement op  = getValidOperator(e);;
		if (op  != null) return op;
		ValidElement cal = getValidComparator(e);
		if (cal != null) return cal;
		return null;
	}
	private static ValidElement getValidElement(int[] display, int validDisplaySize, ValidElement[] allValids)
	{
		if (display.length != validDisplaySize)
			return null;
		for (ValidElement n : allValids)
			if (n.equal(display))
				return n;
		return null;
	}
	
	
	public static boolean isValidNumeral(Element e)
	{
		return getValidNumeral(e) != null;
	}
	private static ValidElement getValidNumeral(Element e)
	{
		return getValidElement(e.getDisplay(), MatchstickNumeral.getNumberOfPlaces(), MatchstickNumeral.getAllValidElements());
	}
	
	public static boolean isValidOperator(Element e)
	{
		return getValidOperator(e) != null;
	}
	private static ValidElement getValidOperator(Element e)
	{
		return getValidElement(e.getDisplay(), MatchstickOperator.getNumberOfPlaces(), MatchstickOperator.getAllValidElements());
	}
	
	public static boolean isValidComparator(Element e)
	{
		return getValidComparator(e) != null;
	}
	private static ValidElement getValidComparator(Element e)
	{
		return getValidElement(e.getDisplay(), MatchstickComparator.getNumberOfPlaces(), MatchstickComparator.getAllValidElements());
	}

}
