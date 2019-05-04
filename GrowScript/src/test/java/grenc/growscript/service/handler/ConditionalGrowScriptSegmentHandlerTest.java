package grenc.growscript.service.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.service.handler.ConditionalGrowScriptSegmentHandler;


public class ConditionalGrowScriptSegmentHandlerTest
{	
	// Subject
	private ConditionalGrowScriptSegmentHandler handler = new ConditionalGrowScriptSegmentHandler();
	
	@Test
	public void shouldDetermineConditional()
	{
		assertTrue(handler.isConditional(new SimpleConditionalSegment()));
		assertFalse(handler.isConditional(new NonConditionalSegment()));
	}
	
	@Test
	public void shouldProperlyReturnConditionalText()
	{
		ConditionalParameters params1 = ConditionalParameters.single(SimpleConditionalSegment.class, SimpleParameter.Option1);		
		ConditionalParameters params2 = ConditionalParameters.single(SimpleConditionalSegment.class, SimpleParameter.Option2);
		
		assertEquals("Option version 1", handler.conditionalText(new SimpleConditionalSegment(), params1));
		assertEquals("Option version 2", handler.conditionalText(new SimpleConditionalSegment(), params2));
	}
	
	@Test
	public void shouldReturnDefaultIfParameterIsMissing()
	{
		ConditionalParameters emptyParams = ConditionalParameters.empty();		
		assertEquals("Default base text", handler.conditionalText(new SimpleConditionalSegment(), emptyParams));
	}
	
	@Test
	public void shouldReturnDefaultIfParameterIsWrongType()
	{
		ConditionalParameters emptyParams = ConditionalParameters.single(SimpleConditionalSegment.class, new String("I am wrong."));				
		assertEquals("Default base text", handler.conditionalText(new SimpleConditionalSegment(), emptyParams));
	}
	
	
	
	private class NonConditionalSegment implements GrowSegment
	{
		@Override public String getBaseText() { return "Sample base text"; }	
	}

	private class SimpleConditionalSegment implements ConditionalGrowSegment<SimpleParameter>
	{
		@Override public String getBaseText() { return "Default base text"; }
		
		@Override 
		public String getConditionalText(SimpleParameter parameter)
		{
			if (parameter == SimpleParameter.Option1)
				return "Option version 1";
			else
				return "Option version 2";
		}	
	}
	
	private enum SimpleParameter
	{
		Option1, Option2;
	}
}
