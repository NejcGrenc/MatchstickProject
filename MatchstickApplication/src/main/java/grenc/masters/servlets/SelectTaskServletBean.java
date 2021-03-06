package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.TaskSessionAssist;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.uservalidation.BrowserDetails;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.ReturnableBean;
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
public class SelectTaskServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	
	@InsertBean
	private MatchstickTaskServletBean matchstickTaskServlet;
	
	@InsertBean
	private TaskSessionAssist taskSessionAssist;
	
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
	
	@InsertBean
	private ReturnableBean returnableBean;
	
	@Override
	public String url()
	{
		return "/selectTask";
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Select task");

		builder.addStyle(Style.split_page);
		builder.addScript(Script.send);
		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));

		if (new BrowserDetails(session, request).isIEorSafariorEdge())
			builder.addStyle(Style.split_page_ie);
			
		languageBall.set(builder, session.getLang(), url());
		accountBall.set(builder, servletContext, session.getLang());
		experimentFinishedBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session);

		creditsBall.set(builder, servletContext, session.getLang(), url());
		
		builder.appendOnlyAssociatedPageElements(PageElement.select_task);
		builder.appendPageElement(translateProcessor.process(new SelectTaskPage(servletContext), session.getLang()));

	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Prepare new TaskSession
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		TaskType selectedTaskType;
		if ("/imagesTask".equals((String) request.getAttribute("forwardUrl"))) 
			selectedTaskType = TaskType.images;
		else
			selectedTaskType = TaskType.matchstick;	
		
		// TaskSession automatically builds itself when you try to fetch it and it doesn't exist yet
		TaskSession taskSession = taskSessionAssist.getTaskSessionToUse(session, selectedTaskType);
		
		// Skip learning if we are part of matchstick task group 0
		if (isMatchstickGroup0(selectedTaskType, taskSession))
		{
			request.setAttribute("forwardUrl", matchstickTaskServlet.url());
		}
	}

	private boolean isMatchstickGroup0(TaskType selectedTaskType, TaskSession taskSession)
	{
		return TaskType.matchstick.equals(selectedTaskType) && 
				(taskSession.getMatchstickGroup().equals(MatchstickGroup.group_0_strategyA) || taskSession.getMatchstickGroup().equals(MatchstickGroup.group_0_strategyB));
	}
	
	
	@SuppressWarnings("unused")
	private class SelectTaskPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment images_task = new SimpleTranslatableSegment(context, "translations/select-task/images_task.json");
		private SimpleTranslatableSegment matchstick_task = new SimpleTranslatableSegment(context, "translations/select-task/matchstick_task.json");

		public SelectTaskPage(ServletContext servletContext)
		{
			super(servletContext, PageElement.select_task);
		}
	}
}