package grenc.masters.matchstick.backgroundtemplate;

import grenc.masters.matchstick.confirm.valid.ValidElement;

public class MatchstickOperator extends ValidElement
{

	private static final int noMatches = 4;
	
	private static final MatchstickOperator plus	 = new MatchstickOperator("+", 1, 1, 0, 0);
	private static final MatchstickOperator minus	 = new MatchstickOperator("-", 0, 1, 0, 0);
	private static final MatchstickOperator times	 = new MatchstickOperator("*", 0, 0, 1, 1);
	private static final MatchstickOperator divide 	 = new MatchstickOperator("/", 0, 0, 1, 0);

	
	private MatchstickOperator (String symbol, int... n) 
	{
		super (symbol, n);
	}


	public static MatchstickOperator[] getAllValidElements() 
	{
		return new MatchstickOperator[] {plus, minus, times, divide};
	}
	public static int getNumberOfPlaces() 
	{
		return noMatches;
	}
	
}
