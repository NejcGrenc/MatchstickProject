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
public class ExperimentFinishedBall
{
	@InsertBean
	private TranslationProcessor translateProcessor;
	
	public void set(WebpageBuilder builder, ServletContext servletContext, String lang)
	{
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.popup);
		builder.appendPageElementFile(PageElement.experiment_finished_ball);
		attachPopup(builder, servletContext, lang);
	}
	
	private void attachPopup(WebpageBuilder builder, ServletContext servletContext, String lang)
	{
		String content = translateProcessor.process(new ExperimentFinishedPage(servletContext), lang);
		new Popup(builder, "experimentFinished").setOpenButton("concludedButton")
					.addBottomCloseButton("experimentFinishedButtonClose", "Close").setText(content).set();
	}
	
	@SuppressWarnings("unused")
	private class ExperimentFinishedPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment content = new SimpleTranslatableSegment(context, 
				"translations/experiment-finished/experiment-finished-ball.json");

		public ExperimentFinishedPage(ServletContext servletContext)
		{
			super(servletContext, PageElement.experiment_finished_ball_popup);
		}	
	}
}
