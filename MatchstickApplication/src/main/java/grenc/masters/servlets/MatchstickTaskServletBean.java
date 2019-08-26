package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.MatchstickTaskProcessor;
import grenc.masters.matchsticktask.MatchstickTaskProcessor.MatchstickTaskProcessorReturn;
import grenc.masters.matchsticktask.ResponseProcessor;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.SolvableRestriction;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.servlets.delegate.popup.MatchstickTaskInfoPopup;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.element.AccountBall;
import grenc.masters.webpage.element.DataPresentBall;
import grenc.masters.webpage.element.LanguageBall;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class MatchstickTaskServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private MatchstickTaskInfoPopup matchstickTaskInfoPopup;

	@InsertBean
	private TaskWrapupServletBean taskWrapupServlet;
	
	@InsertBean
	private MatchstickTaskProcessor taskBuilder;
	@InsertBean
	private ResponseProcessor responseProcessor;
	
	@InsertBean
	private AccountBall accountBall;
	@InsertBean
	private DataPresentBall dataPresentBall;	
	@InsertBean
	private LanguageBall languageBall;
	
	@InsertBean
	private TranslationProcessor translateProcessor;
	
	@Override
	public String url()
	{
		return "/matchstickTask";
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Matchstick task");
		// Also in the head:
	    // <meta name="viewport" content="width=device-width, initial-scale=1.0">

		builder.addStyle(Style.style);
		builder.addStyle(Style.buttons);

		builder.addScript(Script.send);
		
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		TaskSession taskSession = taskBuilder.taskSessionToUse(session);

		MatchstickTaskProcessorReturn newTask = taskBuilder.prepareNewMatchstickTask(taskSession);
		MatchstickGroup group = taskBuilder.matchstickGroupType(session);
		
		languageBall.set(builder, session.getLang(), url());
		accountBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session).withMatchstickGroup(builder, session, taskBuilder.matchstickGroupType(session));
		matchstickTaskInfoPopup.createPopup(builder, servletContext, session.getLang(), group, false);

	    
	    // Matchstick task separate libraries
		builder.addScript(Script.matchstick_main);
		builder.addScript(Script.matchstick_canvas);
		builder.addScript(Script.matchstick_matchstick);
		builder.addScript(Script.matchstick_equation);
		builder.addScript(Script.matchstick_calculator);
		builder.addScript(Script.translate_matchsticktask);
		builder.addScript(Script.delayed_start);
		
		builder.appendOnlyAssociatedPageElements(PageElement.matchstick_task);
		builder.appendPageElement(translateProcessor.process(new MatchstickTaskMainPage(servletContext), session.getLang()));
		
		
		// Setup current task number
		builder.appendBodyScriptCommand("setSolvingTaskNumber("+newTask.newTaskLocalNumber+", "+newTask.newTaskNumber+", "+newTask.totalNumberOfTasks+");");
		
		System.out.println(" | For session " + sessionTag);
		System.out.println(" | and task session " + session.getId());
		System.out.println(" | - Created new equation: " + newTask.newEquation);
		builder.appendBodyScriptCommand("var originalEquation = '" + newTask.newEquation + "';");

		if (newTask.continueWithTime != 0l)
			builder.appendBodyScriptCommand("timer_presetTime(" + newTask.continueWithTime + ");");

		boolean restrict = SolvableRestriction.ONE_MOVE_ONLY.equals(newTask.restriction);
		builder.appendBodyScriptCommand("setRestriction("+restrict+");");

		
		// Start the task when all is properly set
		if (newTask.pauseAtStart)
		{
			builder.appendBodyScriptCommand("startWithPause();");
			openInfoPopupBeforeStart(builder, taskSession);
		}
		else
		{
			builder.appendBodyScriptCommand("start();");
		}

	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		System.out.println("Main session: " + session);
		
		String taskData = (String) request.getAttribute("task_data");
		System.out.println("Value of 'task_data' is " + taskData);
		if (taskData == null || taskData.isEmpty())
		{
			System.out.println("Did not send task data! (probably clicked one of the up-right buttons)");
			return;
		}
		
		TaskSession taskSession = taskBuilder.taskSessionToUse(session);
		
		Boolean isBrowserRefreshButton = null;
		if (responseProcessor.isPageRefresh(taskSession, taskData))
		{
			System.out.println("It was page refresh! Do not do anything!");
			isBrowserRefreshButton = true;
		}
		
		responseProcessor.storeData(taskSession, taskData, isBrowserRefreshButton);
		System.out.println();
		System.out.println();
		
		responseProcessor.perhapsFinishLastTaskSession(taskSession);	
		if (responseProcessor.isFinished(taskSession))
		{
			processFinished(request);
		}
	}
	
	public void processFinished(HttpServletRequest request)
	{
		// Task is completed, forward to after-page
		String forwardUrl = taskWrapupServlet.url();
		request.setAttribute("forwardUrl", forwardUrl);
		System.out.println("Change forwarding request url to: " + forwardUrl);	
	}
	
	private void openInfoPopupBeforeStart(WebpageBuilder builder, TaskSession taskSession)
	{
		builder.appendBodyScriptCommand("document.getElementById('button-info').click();");
	}
	
	@SuppressWarnings("unused")
	private class MatchstickTaskMainPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment headertext = new SimpleTranslatableSegment(context, "translations/matchstick-task/headertext.json");
		private SimpleTranslatableSegment solving_task = new SimpleTranslatableSegment(context, "translations/matchstick-task/solving_task.json");

		public MatchstickTaskMainPage(ServletContext context)
		{
			super(context, PageElement.matchstick_task);
		}
	}
}