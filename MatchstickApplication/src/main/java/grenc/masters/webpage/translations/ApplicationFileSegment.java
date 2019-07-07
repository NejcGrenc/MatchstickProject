package grenc.masters.webpage.translations;

import javax.servlet.ServletContext;

import grenc.growscript.base.interfaces.GrowSegment;
import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.ReadFileBuilderAbstract;

public class ApplicationFileSegment extends ReadFileBuilderAbstract implements GrowSegment
{
	protected ServletContext context;
	private String file;
	
	public ApplicationFileSegment(ServletContext context, String file) {
		this.context = context;
		this.file = file;
	}
	public ApplicationFileSegment(ServletContext context, PageElement pageElement) {
		this.context = context;
		this.file = pageElement.path();
	}

	@Override
	public String getBaseText()
	{
		return readFile(context, file);
	}
	
}
