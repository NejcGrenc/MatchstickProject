package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.matchsticktask.MatchstickTaskProcessor;
import grenc.masters.matchsticktask.ResponseProcessor;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.servlets.helper.MatchstickTaskInfoPopup;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;
import grenc.simpleton.Beans;
import grenc.simpleton.annotation.Bean;


@Bean
public class MatchstickTaskLearnServletBean extends BasePageServlet
{
	private SessionDAO sessionDAO = Beans.get(SessionDAO.class);

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
		MatchstickTaskProcessor taskBuilder = new MatchstickTaskProcessor(session);
		
		new LanguageBall(builder, session.getLang(), url()).set();
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBall(builder, session, servletContext).set();
		new DataPresentBall(builder, session).set().withMatchstickGroup(taskBuilder.matchstickGroupType());
		new MatchstickTaskInfoPopup(builder, servletContext).createPopup(session.getLang());

		builder.appendPageElementFile(PageElement.matchstick_task_learn);
		
		// Setup current task number
		builder.appendBodyScriptCommand("setLearningTaskNumber("+taskBuilder.newTaskNumberForLocalPhase()+", "+taskBuilder.totalNumberOfTasksForNextPhase()+");");
		builder.appendBodyScriptCommand("setRestriction("+true+");");
		
		loadLearningTask(builder, taskBuilder);
		
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
		
		ResponseProcessor responseProcessor = new ResponseProcessor(session);
		responseProcessor.storeEmptyTask(); // Just make an entry to increment the counter
		
		MatchstickTaskProcessor taskBuilder = new MatchstickTaskProcessor(session);
		if (taskBuilder.nextPhase() != MatchstickExperimentPhase.LearningPhase_Solving)
		{
			// Task is completed, forward to actual-task-page
			String forwardUrl = Servlet.MatchstickTaskServlet.getUrl();
			request.setAttribute("forwardUrl", forwardUrl);
			System.out.println("Change forwarding request url to: " + forwardUrl);	
		}
	}

	private void loadLearningTask(WebpageBuilder builder, MatchstickTaskProcessor taskBuilder)
	{
		builder.appendBodyScriptCommand(taskBuilder.prepareNewLearnMatchstickTask());
		
		if (taskBuilder.newTaskNumberForLocalPhase() == 1)
			builder.appendBodyScriptCommand("startWithPause();");
		else
			builder.appendBodyScriptCommand("start();");	}
	
	public void processFinished(HttpServletRequest request)
	{
		// Task is completed, forward to after-page
		String forwardUrl = Servlet.MatchstickTaskServlet.getUrl();
		request.setAttribute("forwardUrl", forwardUrl);
		System.out.println("Change forwarding request url to: " + forwardUrl);	
	}
	
}