package grenc.masters.matchstick.confirm.correct;

import java.util.HashMap;


public class EquationCorrectCache
{
	private HashMap<String, Boolean> cacheMap;
	
	// Sole instance
	private static EquationCorrectCache cache = new EquationCorrectCache ();
	
	private EquationCorrectCache ()
	{
		cacheMap = new HashMap<>();
	}
	
	private HashMap<String, Boolean> getMap()
	{
		return cacheMap;
	}
	
	public static Boolean attemptToGet(String equation)
	{	
		return cache.getMap().get(equation);
	}
	
	public static void put(String equation, Boolean correct)
	{
		cache.getMap().put(equation, correct);
	}
}
