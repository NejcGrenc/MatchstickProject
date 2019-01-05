package grenc.masters.webpage.common;

import grenc.masters.webpage.builder.WebpageBuilder;

public abstract class CommonElement
{
	protected WebpageBuilder builder;
	
	public CommonElement(WebpageBuilder builder)
	{
		this.builder = builder;
	}
}
