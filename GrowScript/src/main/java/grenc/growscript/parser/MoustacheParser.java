package grenc.growscript.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoustacheParser
{
	private static final String startMoustache = "\\{\\{";
	private static final String endMoustache = "\\}\\}";
	
	private final Pattern moustacheAllPattern = Pattern.compile(startMoustache + "(.*?)" + endMoustache);
	
	private final Pattern moustacheVariablePattern(String variableName)
	{
		return Pattern.compile("\\{\\{(\\s*?)(" + variableName + ")(\\s*?)\\}\\}");
	}
	
	
	public List<String> allVariables(String text)
	{
		List<String> allVariables = new ArrayList<String>();
		Matcher matcher = moustacheAllPattern.matcher(text);
		while(matcher.find())
		{	
			// System.out.println(matcher.group());
			// System.out.println(matcher.start());
			// System.out.println(matcher.end());
			String interior = removeMoustaches(matcher.group());
			interior = interior.replaceAll("\\s+", "");
			allVariables.add(interior);
		}
		return allVariables;
	}
	
	public String replaceVariable(String text, String variable, String newValue)
	{
		String newText;
		Matcher matcher = moustacheVariablePattern(variable).matcher(text);
		if (matcher.find())
			newText = matcher.replaceFirst(newValue);
		else
			throw new MoustacheException("No variable [" + variable + "] found.");
		return newText;
	}
	
	private String removeMoustaches(String group)
	{
		return group.substring(2, group.length()-2);
	}
	
}
