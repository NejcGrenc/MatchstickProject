package grenc.growscript.processor.handler;

import java.lang.reflect.Field;

import grenc.growscript.base.GrowSegment;
import grenc.growscript.exception.GrowScriptException;

public class GrowScriptSubSegmentHandler
{
	public GrowSegment getSubSegment(GrowSegment parentSegment, String fieldName)
	{
		Field field = ClassProcessor.findFieldByName(parentSegment.getClass(), fieldName);
		GrowSegment subSegment = getValueOfField(parentSegment, field);
		if (subSegment == null)
			throw new GrowScriptException("Null value of GrowSegment field [" + fieldName + "].");
		return subSegment;
	}
	
	private GrowSegment getValueOfField(Object obj, Field field)
	{
		try
		{
			field.setAccessible(true);
			return (GrowSegment) field.get(obj);
		} 
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new GrowScriptException(e);
		}
	}
}
