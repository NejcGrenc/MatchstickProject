package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.resources.PageElement;
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
public class DoneServletBean extends BasePageServlet
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
		return "/done";
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Done");

		builder.appendPageElement("<p>Done</p>");
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

		public SelectTaskPage(ServletContext servletContext)
		{
			super(servletContext, PageElement.select_single_task);
		}
	}
}