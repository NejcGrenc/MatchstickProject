package grenc.growscript.service.handler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.base.interfaces.GrowSegment;

public class SegmentTextHandlerTest
{
	private SegmentTextHandler handler = new SegmentTextHandler();
	
	@Test
	public void shouldGetBaseTextFromGrowSegment()
	{
		assertEquals("Base text!", handler.getText(new Basic(), ConditionalParameters.empty()));
	}
	
	@Test
	public void shouldGetConditionalTextFromConditionalGrowSegment()
	{
		assertEquals("Conditional true", handler.getText(new Conditional(), ConditionalParameters.single(Conditional.class, true)));
	}
	
	
	private class Basic implements GrowSegment
	{
		@Override public String getBaseText() { return "Base text!"; }
	}
	
	private class Conditional implements ConditionalGrowSegment<Boolean>
	{
		@Override public String getBaseText() { return "Base text!"; }

		@Override
		public String getConditionalText(Boolean parameter)
		{
			return (parameter) ? "Conditional true" : "Conditional false";
		}
	}
}
