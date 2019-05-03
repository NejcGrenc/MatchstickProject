package grenc.growscript.base;

import grenc.growscript.base.interfaces.GrowSegment;

public class SimpleGrowSegment implements GrowSegment
{
	private String text;
	
	public SimpleGrowSegment(String text)
	{
		this.text = text;
	}
	
	@Override
	public String getBaseText()
	{
		return text;
	}
}
