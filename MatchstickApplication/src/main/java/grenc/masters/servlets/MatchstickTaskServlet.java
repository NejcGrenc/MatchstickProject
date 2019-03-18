package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.matchsticktask.MatchstickTaskProcessor;
import grenc.masters.matchsticktask.MatchstickTaskProcessor.MatchstickTaskProcessorReturn;
import grenc.masters.matchsticktask.ResponseProcessor;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.servlets.helper.MatchstickTaskInfoPopup;
import grenc.masters.webpage.builder.AccountBallBuilder;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;


public class MatchstickTaskServlet extends BasePageServlet 
{
	private static final long serialVersionUID = -1953103888973255388L;
	
	private SessionDAO sessionDAO;
	
	public MatchstickTaskServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
	}
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.MatchstickTaskServlet;
	}
	
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Matchstick task");
		// Also in the head:
	    // <meta name="viewport" content="width=device-width, initial-scale=1.0">

		builder.addStyle(Style.style);
		builder.addStyle(Style.buttons);

		builder.addScript(Script.send);
		
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		MatchstickTaskProcessor taskBuilder = new MatchstickTaskProcessor(session);
		MatchstickTaskProcessorReturn newTask = taskBuilder.prepareNewMatchstickTask();
		
		
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBallBuilder().fromSession(session).withBuilder(builder).build().set();
		new DataPresentBall(builder, session).set().withMatchstickGroup(taskBuilder.nextGroupType());
		new MatchstickTaskInfoPopup(builder, getServletContext()).createPopup(session.getLang());
	    
	    // Matchstick task separate libraries
		builder.addScript(Script.matchstick_main);
		builder.addScript(Script.matchstick_canvas);
		builder.addScript(Script.matchstick_matchstick);
		builder.addScript(Script.matchstick_equation);
		builder.addScript(Script.matchstick_calculator);
		builder.addScript(Script.delayed_start);
		
		builder.appendPageElementFile(PageElement.matchstick_task);

		

		
		// Setup current task number
		builder.appendBodyScriptCommand("setSolvingTaskNumber("+newTask.newTaskNumber+", "+newTask.totalNumberOfTasks+");");
		
		System.out.println(" | For session " + sessionTag);
		System.out.println(" | and task session " + session.getId());
		System.out.println(" | - Created new equation: " + newTask.newEquation);
		builder.appendBodyScriptCommand("var originalEquation = '" + newTask.newEquation + "';");

		// Start the task when all is properly set
		if (newTask.pauseAtStart)
			builder.appendBodyScriptCommand("startWithPause();");
		else
			builder.appendBodyScriptCommand("start();");
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		System.out.println("Main session: " + session);
		
		String actions = (String) request.getAttribute("actions");
		System.out.println("Value of 'actions' is " + actions);
		if (actions == null || actions.isEmpty())
		{
			System.out.println("Did not send task data! (probably clicked one of the up-right buttons)");
			return;
		}
		
		ResponseProcessor responseProcessor = new ResponseProcessor(session);
		responseProcessor.storeData(actions);
		System.out.println();
		System.out.println();
		
		responseProcessor.perhapsFinishLastTaskSession();	
		if (responseProcessor.isFinished())
		{
			processFinished(request);
		}
	}
	
	public void processFinished(HttpServletRequest request)
	{
		// Task is completed, forward to after-page
		String forwardUrl = Servlet.TaskWrapupServlet.getUrl();
		request.setAttribute("forwardUrl", forwardUrl);
		System.out.println("Change forwarding request url to: " + forwardUrl);	
	}
	
}
