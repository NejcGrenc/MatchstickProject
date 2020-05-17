package grenc.masters.servlets;

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
import grenc.masters.webpage.element.ExperimentFinishedBall;
import grenc.masters.webpage.element.LanguageBall;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class SelectTaskImagesServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	
	@InsertBean
	private ExperimentFinishedBall experimentFinishedBall;
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

	private String forwardTo = "/imagesTask";

	@Override
	public String url()
	{
		return "/selectTaskImages";
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Start Familiar Figures Task");

		builder.addStyle(Style.split_page);
		builder.addScript(Script.send);
			
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		languageBall.set(builder, session.getLang(), url());
		accountBall.set(builder, servletContext, session.getLang());
		experimentFinishedBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session);
		creditsBall.set(builder, servletContext, session.getLang(), url());
		
		builder.appendHeadScriptCommand("var taskForwardUrl = '" + forwardTo + "';");
		builder.appendOnlyAssociatedPageElements(PageElement.select_single_task);
		builder.appendPageElement(translateProcessor.process(new SelectTaskPage(servletContext), session.getLang()));
	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Nothing to do here
	}
	
	
	@SuppressWarnings("unused")
	private class SelectTaskPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment start_task = new SimpleTranslatableSegment(context, "translations/select-task/images_task.json");
		private SimpleTranslatableSegment to_continue = new SimpleTranslatableSegment(context, "translations/select-task/to_continue.json");

		public SelectTaskPage(ServletContext servletContext)
		{
			super(servletContext, PageElement.select_single_task);
		}
	}
}