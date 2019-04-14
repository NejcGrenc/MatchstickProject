package grenc.masters.resources;

public enum Style
{
	background ("background.css"),
	buttons ("buttons.css"),
	buttons_credits ("buttons-credits.css"),
	centered ("centered.css"),
	collapsible ("collapsible.css"),
	input ("input.css"),
	language_page ("language-page.css"),
	layout ("layout.css"),
	popup ("popup.css"),
	split_page ("split-page.css"),
	split_page_ie ("split-page-ie.css"),
	style ("style.css"),
	table ("table.css")
	;
	
	protected final String basePath = "style/";
	
	private String fileName;
	
	private Style(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String path() 
	{
		return this.basePath + this.fileName;
	}
}
