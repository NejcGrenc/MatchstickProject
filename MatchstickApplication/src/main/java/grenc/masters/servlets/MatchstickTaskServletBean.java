package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.matchsticktask.MatchstickTaskProcessor;
import grenc.masters.matchsticktask.MatchstickTaskProcessor.MatchstickTaskProcessorReturn;
import grenc.masters.matchsticktask.ResponseProcessor;
import grenc.masters.matchsticktask.type.SolvableRestriction;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
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
public class MatchstickTaskServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private MatchstickTaskInfoPopup matchstickTaskInfoPopup;

	@InsertBean
	private TaskWrapupServletBean taskWrapupServlet;
	
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
		
		MatchstickTaskProcessor taskBuilder = new MatchstickTaskProcessor(session);
		MatchstickTaskProcessorReturn newTask = taskBuilder.prepareNewMatchstickTask();
		
		
		new LanguageBall(builder, session.getLang(), url()).set();
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBall(builder, session, servletContext).set();
		new DataPresentBall(builder, session).set().withMatchstickGroup(taskBuilder.matchstickGroupType());
		matchstickTaskInfoPopup.createPopup(builder, servletContext, session.getLang());
	    
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

		if (newTask.continueWithTime != 0l)
			builder.appendBodyScriptCommand("timer_presetTime(" + newTask.continueWithTime + ");");

		boolean restrict = SolvableRestriction.ONE_MOVE_ONLY.equals(newTask.restriction);
		builder.appendBodyScriptCommand("setRestriction("+restrict+");");

		
		// Start the task when all is properly set
		if (newTask.pauseAtStart)
			builder.appendBodyScriptCommand("startWithPause();");
		else
			builder.appendBodyScriptCommand("start();");
		
		openInfoPopupBeforeStart(builder, taskBuilder);

	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		System.out.println("Main session: " + session);
		
		String actions = (String) request.getAttribute("task_data");
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
		String forwardUrl = taskWrapupServlet.url();
		request.setAttribute("forwardUrl", forwardUrl);
		System.out.println("Change forwarding request url to: " + forwardUrl);	
	}
	
	private void openInfoPopupBeforeStart(WebpageBuilder builder,  MatchstickTaskProcessor taskBuilder)
	{
		if (taskBuilder.newTaskNumber() == 1)
		{
			builder.appendBodyScriptCommand("document.getElementById('button-info').click();");
		}
	}
}