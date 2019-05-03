package grenc.growscript.processor.handler;

import java.util.ArrayList;
import java.util.List;

import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.exception.GrowScriptException;
import grenc.growscript.parser.MoustacheParser;


public class GrowScriptSegmentVariableHandler
{
	public List<String> variablesOf(GrowSegment segment)
	{
		List<String> textVariables = new MoustacheParser().allVariables(segment.getBaseText());
		List<String> allGrowSegments = ClassProcessor.allFieldsOfImplementing(segment.getClass(), GrowSegment.class);
		checkVariables(textVariables, allGrowSegments);
		return textVariables;
	}
	
	private void checkVariables(List<String> textVariables, List<String> allGrowSegments)
	{
		List<String> missingTextVariables = allMissing(textVariables, allGrowSegments);
		List<String> missingGrowSegments = allMissing(allGrowSegments, textVariables);
		if (! (missingTextVariables.isEmpty() && missingGrowSegments.isEmpty()))
		{
			throw error(missingTextVariables, missingGrowSegments);
		}
	}
	
	/**
	 * Returns a list of elements that are present in the first list, but not in the second.
	 */
	private <T> List<T> allMissing(List<T> originalList, List<T> listToCompare)
	{
		List<T> missing = new ArrayList<>();
		for (T originalEl : originalList)
			if (! listToCompare.contains(originalEl))
				missing.add(originalEl);
		return missing;
	}
	
	private GrowScriptException error(List<String> missingTextVariables, List<String> missingGrowSegments)
	{
		String errorMessage = "";
		if (! missingTextVariables.isEmpty())
			errorMessage += "Unassigned variables [" + String.join(", ", missingTextVariables) + "]. ";
		if (! missingGrowSegments.isEmpty())
			errorMessage += "Extra GrowSegments [" + String.join(", ", missingGrowSegments) + "]. ";
		return new GrowScriptException(errorMessage);
	}
}
