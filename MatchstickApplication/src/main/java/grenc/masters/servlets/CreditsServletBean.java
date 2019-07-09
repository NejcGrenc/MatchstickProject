package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class CreditsServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	
	@InsertBean
	private AccountBall accountBall;
	
	@InsertBean
	private TranslationProcessor translateProcessor;
	
	@Override
	public String url()
	{
		return "/credits";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Credits");
		
		builder.setBodyStyle("background");
		builder.addStyle(Style.background);
		
		builder.addStyle(Style.style);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.buttons_credits);
		builder.addStyle(Style.centered);
		builder.addStyle(Style.layout);
		builder.addStyle(Style.table);

		builder.addScript(Script.send);

		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		new LanguageBall(builder, session.getLang(), url()).set();
		accountBall.set(builder, servletContext);
		
		builder.appendOnlyAssociatedPageElements(PageElement.credits);
		builder.appendPageElement(translateProcessor.process(new CreditsPage(servletContext), session.getLang()));

		
		new Translate(builder, Script.translate_credits).translateAll();
	}

	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext)
			throws IOException, ServletException
	{
		// Nothing to do here
	}
	
	
	@SuppressWarnings("unused")
	private class CreditsPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment title = new SimpleTranslatableSegment(context, "translations/credits/title.json");
		private SimpleTranslatableSegment matus = new SimpleTranslatableSegment(context, "translations/credits/matus.json");
		private SimpleTranslatableSegment grega = new SimpleTranslatableSegment(context, "translations/credits/grega.json");
		private SimpleTranslatableSegment specialthanks = new SimpleTranslatableSegment(context, "translations/credits/specialthanks.json");
		private SimpleTranslatableSegment specialthankspeople = new SimpleTranslatableSegment(context, "translations/credits/specialthankspeople.json");
		private SimpleTranslatableSegment resources = new SimpleTranslatableSegment(context, "translations/credits/resources.json");
		private SimpleTranslatableSegment resources_data = new SimpleTranslatableSegment(context, "translations/credits/resources_data.json");

		public CreditsPage(ServletContext context)
		{
			super(context, PageElement.credits);
		}
	}
}