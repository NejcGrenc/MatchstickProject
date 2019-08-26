package grenc.masters.webpage.element;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.ReturnableBean;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class CreditsBall
{
	@InsertBean
	private TranslationProcessor translateProcessor;
	@InsertBean
	private ReturnableBean returnableBean;

	public void set(WebpageBuilder builder, ServletContext servletContext, String language, String returnUrl)
	{
		builder.addStyle(Style.buttons_credits);
		builder.addStyle(Style.layout);
		builder.addScript(Script.send);
		
		returnableBean.prepareReturnTo(builder, returnUrl);

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
