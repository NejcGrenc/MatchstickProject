package grenc.growscript.service.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.service.exception.GrowScriptException;
import grenc.growscript.service.handler.GrowScriptSubSegmentHandler;


public class GrowScriptSubSegmentHandlerTest
{
	private GrowScriptSubSegmentHandler handler = new GrowScriptSubSegmentHandler();
	private EmptyClass emptyClassInstance = new EmptyClass();
	
	@Test
	public void shouldSuccessfullyCheckValidConfiguration()
	{
		ValidConfiguration testClass = new ValidConfiguration();
		GrowSegment subSegment = handler.getSubSegment(testClass, "var1");
		assertEquals(emptyClassInstance, subSegment);
	}
	
	@Test
	public void shouldSuccessfullyCheckValidConfigurationEvenWhenSpeciffic()
	{
		ValidConfigurationSpecific testClass = new ValidConfigurationSpecific();
		GrowSegment subSegment = handler.getSubSegment(testClass, "var1");
		assertEquals(emptyClassInstance, subSegment);
	}
	
	@Test (expected = GrowScriptException.class)
	public void shouldFailIfGrowSegmentIsNull()
	{
		ValidConfigurationMissingVar testClass = new ValidConfigurationMissingVar();
		handler.getSubSegment(testClass, "var1");
		fail();
	}
	

	private class EmptyClass implements GrowSegment
	{
		@Override public String getBaseText() { return "Some simple text"; }
	}
	
	private class ValidConfiguration implements GrowSegment
	{
		@SuppressWarnings("unused")
		GrowSegment var1 = emptyClassInstance;
		@Override public String getBaseText() { return "Custom variable {{var1}}."; }	
	}
	
	private class ValidConfigurationSpecific implements GrowSegment
	{
		@SuppressWarnings("unused")
		EmptyClass var1 = emptyClassInstance;
		@Override public String getBaseText() { return "Custom variable {{var1}}."; }	
	}
	
	private class ValidConfigurationMissingVar implements GrowSegment
	{
		@SuppressWarnings("unused")
		GrowSegment var1;
		@Override public String getBaseText() { return "Custom variable {{var1}}."; }	
	}
	
}
