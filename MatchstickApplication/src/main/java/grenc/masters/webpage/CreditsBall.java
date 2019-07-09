package grenc.masters.webpage;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.CommonElement;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;


public class CreditsBall extends CommonElement
{
	private TranslationProcessor translateProcessor;

	public CreditsBall(WebpageBuilder builder, TranslationProcessor translateProcessor)
	{
		super(builder);
		this.translateProcessor = translateProcessor;
	}

	public void set(ServletContext servletContext, String language)
	{
		builder.addStyle(Style.buttons_credits);
		builder.addStyle(Style.layout);
		builder.addScript(Script.send);

		builder.appendOnlyAssociatedPageElements(PageElement.credits_ball);
		builder.appendPageElement(translateProcessor.process(new CreditsPage(servletContext), language));
	}
	
	@SuppressWarnings("unused")
	private class CreditsPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment credits = new SimpleTranslatableSegment(context, "translations/credits/credits.json");

		public CreditsPage(ServletContext servletContext)
		{
			super(servletContext, PageElement.credits_ball);
		}
	}
}
