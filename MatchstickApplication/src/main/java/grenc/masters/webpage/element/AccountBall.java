package grenc.masters.webpage.element;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class AccountBall
{
	@InsertBean
	private TranslationProcessor translateProcessor;
	
	public void set(WebpageBuilder builder, ServletContext servletContext, String lang)
	{
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.popup);
		builder.appendPageElementFile(PageElement.account_ball);
		attachPopup(builder, servletContext, lang);
	}
	
	private void attachPopup(WebpageBuilder builder, ServletContext servletContext, String lang)
	{
		String content = translateProcessor.process(new AccountBallPage(servletContext), lang);
		new Popup(builder, "popupName").setOpenButton("accountButton").setText(content).set();
	}
	
	@SuppressWarnings("unused")
	private class AccountBallPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment content = new SimpleTranslatableSegment(context, "translations/account-ball/account-ball.json");

		public AccountBallPage(ServletContext servletContext)
		{
			super(servletContext, PageElement.account_ball_popup);
		}
		
	}
}
