package grenc.masters.matchstick.backgroundtemplate;

import grenc.masters.matchstick.confirm.valid.ValidElement;
import grenc.masters.matchstick.run.main.Settings;

public class MatchstickComparator extends ValidElement
{	
	private static final int noMatches = 5;
	
	private static final MatchstickComparator equal	   = new MatchstickComparator("=", 1, 1, 0, 0, 0);
	private static final MatchstickComparator notEqual = new MatchstickComparator("!", 1, 1, 1, 0, 0);
	private static final MatchstickComparator smaller  = new MatchstickComparator("<", 0, 0, 1, 0, 1);
	private static final MatchstickComparator bigger   = new MatchstickComparator(">", 0, 0, 1, 1, 0);

	
	private MatchstickComparator (String symbol, int... n) 
	{
		super (symbol, n);
	}


	public static MatchstickComparator[] getAllValidElements() 
	{
		if (Settings.onlyEqualsComparator)
			return new MatchstickComparator[] {equal};
		return new MatchstickComparator[] {equal, notEqual, smaller, bigger};
	}

	public static int getNumberOfPlaces() 
	{
		return noMatches;
	}
	
}
