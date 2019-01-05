package grenc.masters.webpage.common;

import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

/**
 * Idea for this object and associated css and js files came from
 *  https://www.w3schools.com/howto/howto_js_collapsible.asp
 */
public class Collapsible
{
	private static final String spaceBeforePlusMinus = "&nbsp;&nbsp;&nbsp;&nbsp;";
	
	
	public static void addCollapsible(WebpageBuilder builder, String fullScript)
	{
		builder.appendPageElement(fullScript);
	}
	
	public static void addCollapsible(boolean open, WebpageBuilder builder, String title, String... content)
	{
		builder.appendPageElement(html(open, title, content));
	}
	
	public static void addCollapsible(WebpageBuilder builder, String title, String... content)
	{
		builder.appendPageElement(html(title, content));
	}
	
	public static String html(boolean open, String title, String... contents)
	{
		String html = "";
		html += "<div>";
		html += title(title, open);
		for (String content : contents)
			 html += content(content, open);
		html += "</div>";
		return html;	}
	
	public static String html(String title, String... contents)
	{
		return html(false, title, contents);
	}
	
	public static void enableCollapsible(WebpageBuilder builder)
	{
		builder.addStyle(Style.collapsible);
		builder.addScript(Script.collapsible);
		builder.appendBodyScriptCommand("enableCollapsible();");
	}
	
	private static String title(String title, boolean openOnStart)
	{
		String open = (openOnStart) ? "active" : "";
		return "<button class=\"collapsible " + open + "\">" + title + spaceBeforePlusMinus + "</button>";
	}
	
	private static String content(String content, boolean openOnStart)
	{
		String open = (openOnStart) ? "style=\"display: block;\"" : "";
		return "<div class=\"collapsible_content\" " + open + ">" + content + "</div>";	
		
	}
	
}
