package grenc.masters.webpage.common;

import grenc.masters.resources.Script;
import grenc.masters.webpage.builder.WebpageBuilder;

public class Translate extends CommonElement
{
	public Translate(WebpageBuilder builder)
	{
		super(builder);
		builder.addScript(Script.translate);
	}

	public Translate translate(String... elementIds)
	{
		for (String elementId : elementIds)
			translate(elementId);
		return this;
	}
	
	public Translate translate(String elementId)
	{
		builder.appendBodyScriptCommand("translate('" + elementId + "');");
		return this;
	}
	
	public Translate translateAll()
	{
		builder.appendBodyScriptCommand("translateAllId();");
		return this;
	}
	
	public Translate translateSpecial(String elementId, String attribute)
	{
		builder.appendBodyScriptCommand("translate('" + elementId + "', '" + attribute + "');");
		return this;
	}
}
