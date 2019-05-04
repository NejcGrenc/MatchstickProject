package grenc.growscript.processor.handler;

import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.conditional.ConditionalParameters;


public class ConditionalGrowScriptSegmentHandler
{
	
	public boolean isConditional(GrowSegment segment)
	{
		return segment instanceof ConditionalGrowSegment;
	}
	
	@SuppressWarnings("unchecked")
	public <T> String conditionalText(ConditionalGrowSegment<T> segment, ConditionalParameters params)
	{
		try
		{
			Object parameter = params.getParameter(segment.getClass());
			if (parameter == null)
				return errorAndGetBase(segment, "No parameter found for class [" + segment.getClass() + "].");
						
			return segment.getConditionalText((T) parameter);
		}
		catch (Exception e) 
		{
			return errorAndGetBase(segment, e);
		}
	}
	
	
	private String errorAndGetBase(GrowSegment segment, Exception error)
	{
		System.out.println("ERROR occured while processing conditionalGrow segment [" + segment.getClass() + "]. Returning baseText instead. Error: " + error.getMessage());
		return segment.getBaseText();
	}
	
	private String errorAndGetBase(GrowSegment segment, String errorMessage)
	{
		System.out.println("ERROR: " + errorMessage + " Returning baseText instead.");
		return segment.getBaseText();
	}
}
