package grenc.growscript.processor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import grenc.growscript.base.GrowSegment;


public class GrowScriptProcessorSingleTest
{
	// Subject service
	private GrowScriptProcessor processor = new GrowScriptProcessor();
	
	@Test
	public void shouldSuccessfullyCheckValidConfiguration()
	{
		ValidConfiguration testClass = new ValidConfiguration();
		String combinedText = processor.process(testClass);
		assertEquals("Custom variable this simple text.", combinedText);
	}
	
	
	private class EmptyClass implements GrowSegment
	{
		@Override public String getBaseText() { return "this simple text"; }
	}
	
	private class ValidConfiguration implements GrowSegment
	{
		@SuppressWarnings("unused")
		GrowSegment var1 = new EmptyClass();
		@Override public String getBaseText() { return "Custom variable {{var1}}."; }	
	}
	
}
