package grenc.growscript.base.conditional;

import java.util.HashMap;
import java.util.Map;

import grenc.growscript.base.interfaces.ConditionalGrowSegment;


public class ConditionalParameters
{
	private Map<Class<?>, Object> parameterMap = new HashMap<>();
	
	@SuppressWarnings("rawtypes")
	public void addParameter(Class<? extends ConditionalGrowSegment> forConditionalGrowSegment, Object parameterObject)
	{
		parameterMap.put(forConditionalGrowSegment, parameterObject);
	}
	
	@SuppressWarnings("rawtypes")
	public Object getParameter(Class<? extends ConditionalGrowSegment> forConditionalGrowSegment)
	{
		return parameterMap.get(forConditionalGrowSegment);
	}
	
	
	public static ConditionalParameters empty()
	{
		return new ConditionalParameters();
	}
	
	@SuppressWarnings("rawtypes")
	public static ConditionalParameters single(Class<? extends ConditionalGrowSegment> forConditionalGrowSegment, Object parameterObject)
	{
		ConditionalParameters params = new ConditionalParameters();
		params.addParameter(forConditionalGrowSegment, parameterObject);
		return params;
	}
}
