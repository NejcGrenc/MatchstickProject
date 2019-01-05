package grenc.masters.matchstick.run.main;

import grenc.masters.matchstick.objects.main.Action;

public class Settings
{
	
	//
	// Settings for equation creation are in AllValidEquations
	//
	
	public static final boolean onlyCorrectFinalEquations = true;
	public static final boolean onlyEqualsComparator = true;
	
	public static final boolean allowStartWithCorrectEquation = false;
	
	public static final boolean useElementCache = true;
	public static final boolean useEquationCache = true;
	
	//
	// Settings for result groupings are in FInalResultGrouping
	//	- this includes file names to write into
	//
	
	public static final String actionStr = "move12";
	public static final Action[] actions;
	static 
	{
//		actions = new Action[1];
//		actions[0] = new Action(1, 1);
		actions = new Action[2];
		actions[0] = new Action(1, 1);
		actions[1] = new Action(2, 2);
	}

}
