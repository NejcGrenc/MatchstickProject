package grenc.masters.webpage.common;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

public class LanguageBall extends CommonElement
{
	
	private String userLang;
	private String currentPage;
	
	@Deprecated
	public LanguageBall(WebpageBuilder builder, String userLang)
	{
		super(builder);
		this.userLang = userLang;
	}
	
	public LanguageBall(WebpageBuilder builder, String userLang, String currentPage)
	{
		super(builder);
		this.userLang = userLang;
		this.currentPage = currentPage;
	}

	public void set()
	{
		builder.addStyle(Style.buttons);
		builder.addScript(Script.translate);
		builder.appendPageElementFile(PageElement.language_ball);
		builder.appendBodyScriptCommand("var userLang = '" + userLang + "';");
		builder.appendBodyScriptCommand("var currentPage = '" + currentPage + "';");
		builder.appendBodyScriptCommand("setLanguageBall();");
	}
}
