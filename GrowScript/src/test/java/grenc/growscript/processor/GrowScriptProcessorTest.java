package grenc.growscript.processor;

import static org.junit.Assert.fail;

import org.junit.Test;

import grenc.growscript.base.GrowSegment;
import grenc.growscript.exception.GrowScriptException;


public class GrowScriptProcessorTest
{
	// Subject
	private GrowScriptProcessor processor = new GrowScriptProcessor();
	
	@Test
	public void shouldSuccessfullyCheckValidConfiguration()
	{
		ValidConfiguration testClass = new ValidConfiguration();
		processor.checkVariables(testClass);
		// should throw no exception
	}
	
	@Test (expected = GrowScriptException.class)
	public void shouldFailProcessingMissingVariables()
	{
		InvalidConfiguration1 testClass = new InvalidConfiguration1();
		processor.checkVariables(testClass);
		fail();
	}
	
	@Test (expected = GrowScriptException.class)
	public void shouldFailProcessingMissingGrowSegments()
	{
		InvalidConfiguration2 testClass = new InvalidConfiguration2();
		processor.checkVariables(testClass);
		fail();
	}
	
	
	
	private class ValidConfiguration implements GrowSegment
	{
		@SuppressWarnings("unused")
		GrowSegment var1;
		@Override public String getBaseText() { return "Custom variable {{var1}}."; }	
	}
	
	private class InvalidConfiguration1 implements GrowSegment
	{
		@Override public String getBaseText() {return "Custom variable {{var1}}."; }	
	}
	
	private class InvalidConfiguration2 implements GrowSegment
	{
		@SuppressWarnings("unused")
		GrowSegment var1;		
		@Override public String getBaseText() { return "No custom variable."; }	
	}
}
