package grenc.growscript.service.handler;

import static org.junit.Assert.fail;

import org.junit.Test;

import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.service.exception.GrowScriptException;
import grenc.growscript.service.handler.GrowScriptSegmentVariableHandler;


public class GrowScriptSegmentVariableHandlerTest
{
	private GrowScriptSegmentVariableHandler variableHandler = new GrowScriptSegmentVariableHandler();
	
	@Test
	public void shouldSuccessfullyCheckValidConfiguration()
	{
		ValidConfiguration testClass = new ValidConfiguration();
		variableHandler.variablesOf(testClass);
		// should throw no exception
	}
	
	@Test
	public void shouldSuccessfullyCheckValidConfigurationEvenWhenSpeciffic()
	{
		ValidConfigurationSpecific testClass = new ValidConfigurationSpecific();
		variableHandler.variablesOf(testClass);
		// should throw no exception
	}
	
	@Test (expected = GrowScriptException.class)
	public void shouldFailProcessingMissingVariables()
	{
		InvalidConfiguration1 testClass = new InvalidConfiguration1();
		variableHandler.variablesOf(testClass);
		fail();
	}
	
	@Test (expected = GrowScriptException.class)
	public void shouldFailProcessingMissingGrowSegments()
	{
		InvalidConfiguration2 testClass = new InvalidConfiguration2();
		variableHandler.variablesOf(testClass);
		fail();
	}
	
	
	
	private class EmptyClass implements GrowSegment
	{
		@Override public String getBaseText() { return "Some simple text"; }
	}
	
	private class ValidConfiguration implements GrowSegment
	{
		@SuppressWarnings("unused")
		GrowSegment var1;
		@Override public String getBaseText() { return "Custom variable {{var1}}."; }	
	}
	
	private class ValidConfigurationSpecific implements GrowSegment
	{
		@SuppressWarnings("unused")
		EmptyClass var1;
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
