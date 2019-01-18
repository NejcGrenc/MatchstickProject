package grenc.masters.matchstick.backgroundtemplate;

import grenc.masters.matchstick.confirm.valid.ValidElement;

public class MatchstickComparator extends ValidElement
{	
	
    //  Visual representation of 
    //  the comparator frame 
    //   _________
    //  |_________| 1
    //   _________
    //  |_________| 2
    //
	// The new mechanism only allows equals comparator
	
	private static final int noMatches = 2;
	
	private static final MatchstickComparator equal	   = new MatchstickComparator("=", 1, 1);
	
	
//	private static final int noMatches = 5;
//	private static final MatchstickComparator equal	   = new MatchstickComparator("=", 1, 1, 0, 0, 0);
//	private static final MatchstickComparator notEqual = new MatchstickComparator("!", 1, 1, 1, 0, 0);
//	private static final MatchstickComparator smaller  = new MatchstickComparator("<", 0, 0, 1, 0, 1);
//	private static final MatchstickComparator bigger   = new MatchstickComparator(">", 0, 0, 1, 1, 0);

	
	private MatchstickComparator (String symbol, int... n) 
	{
		super (symbol, n);
	}


	public static MatchstickComparator[] getAllValidElements() 
	{
		return new MatchstickComparator[] {equal};
		
//		if (Settings.onlyEqualsComparator)
//			return new MatchstickComparator[] {equal};
//		return new MatchstickComparator[] {equal, notEqual, smaller, bigger};
	}

	public static int getNumberOfPlaces() 
	{
		return noMatches;
	}
	
}
