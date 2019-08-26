package grenc.masters.webpage.element;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.simpleton.annotation.Bean;

@Bean
public class LanguageBall
{
	public void set(WebpageBuilder builder, String userLang, String currentPage)
	{
		builder.addStyle(Style.buttons);
		builder.addScript(Script.translate);
		builder.appendPageElementFile(PageElement.language_ball);
		builder.appendBodyScriptCommand("var userLang = '" + userLang + "';");
		builder.appendBodyScriptCommand("var currentPage = '" + currentPage + "';");
		builder.appendBodyScriptCommand("setLanguageBall();");
	}
}
