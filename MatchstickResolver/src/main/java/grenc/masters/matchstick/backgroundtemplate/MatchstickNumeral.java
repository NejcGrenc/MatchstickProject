package grenc.masters.matchstick.backgroundtemplate;

import grenc.masters.matchstick.confirm.valid.ValidElement;

public class MatchstickNumeral extends ValidElement
{
	private static final int noMatches = 7;
	
	private static final MatchstickNumeral n0 = new MatchstickNumeral("0", 1, 1, 1, 1, 1, 1, 0);
	private static final MatchstickNumeral n1 = new MatchstickNumeral("1", 0, 1, 1, 0, 0, 0, 0);
	private static final MatchstickNumeral n2 = new MatchstickNumeral("2", 1, 1, 0, 1, 1, 0, 1);
	private static final MatchstickNumeral n3 = new MatchstickNumeral("3", 1, 1, 1, 1, 0, 0, 1);
	private static final MatchstickNumeral n4 = new MatchstickNumeral("4", 0, 1, 1, 0, 0, 1, 1);
	private static final MatchstickNumeral n5 = new MatchstickNumeral("5", 1, 0, 1, 1, 0, 1, 1);
	private static final MatchstickNumeral n6 = new MatchstickNumeral("6", 1, 0, 1, 1, 1, 1, 1);
	private static final MatchstickNumeral n7 = new MatchstickNumeral("7", 1, 1, 1, 0, 0, 0, 0);
	private static final MatchstickNumeral n8 = new MatchstickNumeral("8", 1, 1, 1, 1, 1, 1, 1);
	private static final MatchstickNumeral n9 = new MatchstickNumeral("9", 1, 1, 1, 1, 0, 1, 1);


	private MatchstickNumeral (String symbol, int... n) 
	{
		super (symbol, n);
	}
	
	
	public static MatchstickNumeral[] getAllValidElements()
	{
		return new MatchstickNumeral[] {n0, n1, n2, n3, n4, n5, n6, n7, n8, n9};
	}
	public static int getNumberOfPlaces() 
	{
		return noMatches;
	}

}
