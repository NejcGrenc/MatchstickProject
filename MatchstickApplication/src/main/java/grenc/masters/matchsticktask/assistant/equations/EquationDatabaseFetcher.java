package grenc.masters.matchsticktask.assistant.equations;

import java.util.HashMap;
import java.util.Random;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;

public class EquationDatabaseFetcher
{
	private static HashMap<EquationSolutionsGroupType, Integer> groupContentCount;
	
	public int getNumberOfPossibleEquationsFor(EquationSolutionsGroupType equationType)
	{
		if (groupContentCount == null)
			groupContentCount = new HashMap<>();
		
		if (! groupContentCount.containsKey(equationType))
			groupContentCount.put(equationType, equationType.dao().countAllEntities());
		
		return groupContentCount.get(equationType);
	}
	

	public String fetchRandom(EquationSolutionsGroupType equationType)
	{
		Random r = new Random();
		int low = 1;
		int high = getNumberOfPossibleEquationsFor(equationType);
		int randomId = r.nextInt(high-low) + low;
		
		return equationType.dao().find(randomId);
	}
}
