package grenc.growscript.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import grenc.growscript.base.FileGrowSegment;
import grenc.growscript.base.SimpleGrowSegment;
import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.base.interfaces.GrowSegment;


public class GrowScriptProcessorComponentTest
{
	@Test
	public void shouldFullyGrowScript()
	{
		GrowSegment mainSegment = new MainGrowSegment();
		ConditionalParameters param = ConditionalParameters.single(ActionConditionalSegment.class, Action.destroy);
		
		System.out.println(System.getProperty("user.dir"));
		
		String grownText = new GrowScriptProcessor().process(mainSegment, param);
		
		assertEquals("Luke Skywalker successfully destroyed the death star.", grownText);
	}
	
	
	
	@SuppressWarnings("unused")
	private class MainGrowSegment implements GrowSegment
	{
		GrowSegment name = new SimpleGrowSegment("Luke Skywalker");
		GrowSegment deed = new ActionConditionalSegment();
		GrowSegment target = new DeathSegment();
		
		@Override
		public String getBaseText()
		{
			return "{{name}} successfully {{deed}} the {{target}}.";
		}
		
	}
	
	private class ActionConditionalSegment implements ConditionalGrowSegment<Action>
	{
		@Override
		public String getBaseText() 
		{ 
			return "did nothing"; 
		}
		
		@Override 
		public String getConditionalText(Action parameter)
		{
			if (parameter == Action.build)
				return "built";
			else
				return "destroyed";
		}	
	}
	
	private enum Action
	{
		build, destroy;
	}
	
	@SuppressWarnings("unused")
	private class DeathSegment implements GrowSegment
	{
		FileGrowSegment star = new FileGrowSegment("src/test/resources/simple/star.txt");
		
		@Override public String getBaseText() 
		{ 
			return "death {{star}}"; 
		}	
	}
	
}
