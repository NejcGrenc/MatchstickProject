package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.MatchstickTaskProcessor;
import grenc.masters.matchsticktask.ResponseProcessor;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.servlets.helper.MatchstickTaskInfoPopup;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class MatchstickTaskLearnServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private MatchstickTaskInfoPopup matchstickTaskInfoPopup;
	
	@InsertBean
	private MatchstickTaskServletBean matchstickTaskServlet;
	
	@InsertBean
	private MatchstickTaskProcessor taskBuilder;
	@InsertBean
	private ResponseProcessor responseProcessor;

	@Override
	public String url()
	{
		return "/matchstickTaskLearn";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Matchstick task");

		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		new LanguageBall(builder, session.getLang(), url()).set();
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBall(builder, session, servletContext).set();
		new DataPresentBall(builder, session).set().withMatchstickGroup(taskBuilder.matchstickGroupType(session));
		matchstickTaskInfoPopup.createPopup(builder, servletContext, session.getLang());

		builder.appendPageElementFile(PageElement.matchstick_task_learn);
		
		// Setup current task number
		TaskSession taskSession = taskBuilder.taskSessionToUse(session);
		builder.appendBodyScriptCommand("setLearningTaskNumber("+taskBuilder.newTaskNumberForLocalPhase(taskSession)+", "+taskBuilder.totalNumberOfTasksForNextPhase(taskSession)+");");
		builder.appendBodyScriptCommand("setRestriction("+true+");");
		
		loadLearningTask(builder, session);
		
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		if (! url().equals((String) request.getAttribute("forwardUrl")))
		{   // Some other action - not finished learning
			return;
		}
		
		responseProcessor.storeEmptyTask(session); // Just make an entry to increment the counter
		
		TaskSession taskSession = taskBuilder.taskSessionToUse(session);
		if (taskBuilder.nextPhase(taskSession) != MatchstickExperimentPhase.LearningPhase_Solving)
		{
			// Task is completed, forward to actual-task-page
			String forwardUrl = matchstickTaskServlet.url();
			request.setAttribute("forwardUrl", forwardUrl);
			System.out.println("Change forwarding request url to: " + forwardUrl);	
		}
	}

	private void loadLearningTask(WebpageBuilder builder, Session session)
	{
		TaskSession taskSession = taskBuilder.taskSessionToUse(session);
		builder.appendBodyScriptCommand(taskBuilder.prepareNewLearnMatchstickTask(taskSession));
		
		if (taskBuilder.newTaskNumberForLocalPhase(taskSession) == 1)
			builder.appendBodyScriptCommand("startWithPause();");
		else
			builder.appendBodyScriptCommand("start();");	}
	
	public void processFinished(HttpServletRequest request)
	{
		// Task is completed, forward to after-page
		String forwardUrl = matchstickTaskServlet.url();
		request.setAttribute("forwardUrl", forwardUrl);
		System.out.println("Change forwarding request url to: " + forwardUrl);	
	}
	
}