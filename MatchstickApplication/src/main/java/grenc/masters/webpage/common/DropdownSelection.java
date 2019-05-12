package grenc.masters.webpage.common;

import static java.util.Comparator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import grenc.growscript.base.interfaces.GrowSegment;


public class DropdownSelection<K extends Comparable<K>, V> implements GrowSegment
{
	public static final String selectTagName = "select";
	public static final String optionTagName = "option";
	public static final String valueParamName = "value";
	public static final String selectedParam = "selected";

	
	private K defaultSelected;
	private Map<K, V> selectionMap;
	
	public DropdownSelection(Map<K, V> selectionMap)
	{
		this(selectionMap, null);
	}
	public DropdownSelection(Map<K, V> selectionMap, K defaultSelected)
	{
		this.selectionMap = selectionMap;
		this.defaultSelected = defaultSelected;
	}

	@Override
	public String getBaseText()
	{
		return createHtmlCode();
	}
	
	
	public String createHtmlCode()
	{
		StringBuilder code = new StringBuilder();
		code.append(startTag(selectTagName));
		
		List<K> orderedKeys = new ArrayList<>(selectionMap.keySet());
		orderedKeys.sort(naturalOrder());
		
		for(K key : orderedKeys)
		{
			if (key.equals(defaultSelected))
				code.append(startTagWith2Param(optionTagName, valueParamName, key.toString(), selectedParam, selectedParam));
			else
				code.append(startTagWithParam(optionTagName, valueParamName, key.toString()));
			code.append(selectionMap.get(key).toString());
			code.append(endTag(optionTagName));
		}
		
		code.append(endTag(selectTagName));
		return code.toString();
	}


	private String startTag(String tag)
	{
		return String.format("<%s>", tag);
	}
	private String startTagWithParam(String tag, String paramName, String paramValue)
	{
		return String.format("<%s %s=\"%s\">", tag, paramName, paramValue);
	}
	private String startTagWith2Param(String tag, String paramName, String paramValue, String paramName2, String paramValue2)
	{
		return String.format("<%s %s=\"%s\" %s=\"%s\">", tag, paramName, paramValue, paramName2, paramValue2);
	}
	private String endTag(String tag)
	{
		return String.format("</%s>", tag);
	}
}
