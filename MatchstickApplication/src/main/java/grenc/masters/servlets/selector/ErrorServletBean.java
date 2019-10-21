package grenc.masters.servlets.selector;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.element.AccountBall;
import grenc.masters.webpage.element.CreditsBall;
import grenc.masters.webpage.element.DataPresentBall;
import grenc.masters.webpage.element.LanguageBall;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class ErrorServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	
	@InsertBean
	private AccountBall accountBall;
	@InsertBean
	private DataPresentBall dataPresentBall;	
	@InsertBean
	private LanguageBall languageBall;
	@InsertBean
	private CreditsBall creditsBall;
	
	@InsertBean
	private TranslationProcessor translateProcessor;
	
	@Override
	public String url()
	{
		return "/error";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Error");
		
		builder.addStyle(Style.background);
		builder.setBodyStyle("background");
		
		builder.addStyle(Style.style);
		builder.addStyle(Style.centered);
		builder.addStyle(Style.layout);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.buttons_credits);
		builder.addScript(Script.send);
		builder.addStyle(Style.split_page);


		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		languageBall.set(builder, session.getLang(), url());
		accountBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session);
		creditsBall.set(builder, servletContext, session.getLang(), url());
		builder.appendPageElement(translateProcessor.process(new ErrorPage(servletContext), session.getLang()));

	}

	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext)
			throws IOException, ServletException
	{
		// Nothing to do here
	}
	
	
	@SuppressWarnings("unused")
	private class ErrorPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment apology = new SimpleTranslatableSegment(context, "translations/error/apology.json");
		private SimpleTranslatableSegment restart_request = new SimpleTranslatableSegment(context, "translations/error/restart_request.json");
		private SimpleTranslatableSegment restart_button = new SimpleTranslatableSegment(context, "translations/error/restart_button.json");
		
		public ErrorPage(ServletContext context)
		{
			super(context, PageElement.error);
		}
	}
}