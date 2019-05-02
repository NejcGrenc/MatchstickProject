package grenc.growscript.processor;

import java.util.ArrayList;
import java.util.List;

import grenc.growscript.base.GrowSegment;
import grenc.growscript.exception.GrowScriptException;
import grenc.growscript.parser.MoustacheParser;


public class GrowScriptProcessor
{
	public void process(GrowSegment segment)
	{
		String text = segment.getBaseText();
	}
	
	public void checkVariables(GrowSegment segment)
	{
		String text = segment.getBaseText();
		List<String> textVariables = new MoustacheParser().allVariables(text);
		List<String> allGrowSegments = ClassProcessor.allFieldsOfType(segment.getClass(), GrowSegment.class);
		
		List<String> missingTextVariables = allMissing(textVariables, allGrowSegments);
		List<String> missingGrowSegments = allMissing(allGrowSegments, textVariables);
		if (! (missingTextVariables.isEmpty() && missingGrowSegments.isEmpty()))
		{
			String errorMessage = "";
			if (missingTextVariables.isEmpty())
				errorMessage += "Unassigned variables [" + String.join(", ", missingTextVariables) + "]. ";
			if (missingGrowSegments.isEmpty())
				errorMessage += "Extra GrowSegments [" + String.join(", ", missingGrowSegments) + "]. ";
			throw new GrowScriptException(errorMessage);
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
	
	
	
}
