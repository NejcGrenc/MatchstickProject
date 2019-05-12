package grenc.masters.servlets;

import java.io.IOException;
import java.util.List;

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
import grenc.masters.resources.Video;
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
public class MatchstickTaskObserveServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;	
	@InsertBean
	private MatchstickTaskInfoPopup matchstickTaskInfoPopup;

	@InsertBean
	private MatchstickTaskLearnServletBean matchstickTaskLearnServlet;
	
	@InsertBean
	private MatchstickTaskProcessor matchstickTaskProcessor;
	@InsertBean
	private ResponseProcessor responseProcessor;
	
	@Override
	public String url()
	{
		return "/matchstickTaskObserve";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Matchstick task");

		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		TaskSession taskSession = matchstickTaskProcessor.taskSessionToUse(session);

		new LanguageBall(builder, session.getLang(), url()).set();
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBall(builder, session, servletContext).set();
		new DataPresentBall(builder, session).set().withMatchstickGroup(matchstickTaskProcessor.matchstickGroupType(session));
		matchstickTaskInfoPopup.createPopup(builder, servletContext, session.getLang());

		builder.appendPageElementFile(PageElement.matchstick_task_observe);
		
		// Setup current task number
		builder.appendBodyScriptCommand("setObservingTaskNumber("+matchstickTaskProcessor.newTaskNumberForLocalPhase(taskSession)+", "+matchstickTaskProcessor.totalNumberOfTasksForNextPhase(taskSession)+");");

		prepareObserveTask(builder, taskSession);

		openInfoPopupBeforeStart(builder, taskSession);
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		if (! url().equals((String) request.getAttribute("forwardUrl")))
		{   // Some other action - not finished watching video
			return;
		}
		
		TaskSession taskSession = matchstickTaskProcessor.taskSessionToUse(session);
		responseProcessor.storeEmptyTask(taskSession); // Just make an entry to increment the counter
		
		if (matchstickTaskProcessor.nextPhase(taskSession) != MatchstickExperimentPhase.LearningPhase_Showing)
		{
			// Task is completed, forward to Learn-page
			String forwardUrl = matchstickTaskLearnServlet.url();
			request.setAttribute("forwardUrl", forwardUrl);
			System.out.println("Change forwarding request url to: " + forwardUrl);	
		}
	}

	
	private void prepareObserveTask(WebpageBuilder builder, TaskSession taskSession)
	{
		List<Video> videos = matchstickTaskProcessor.prepareNewObserveMatchstickTask(taskSession);
		for (Video video : videos)
			addSource(builder, video);
		builder.appendBodyScriptCommand("setup();");
	}
	
	private void addSource(WebpageBuilder builder, Video video)
	{
		String sourceCommand = "addSource('" + video.getSource() + "', '" + video.getType() + "');";
		builder.appendBodyScriptCommand(sourceCommand);
	}
	
	private void openInfoPopupBeforeStart(WebpageBuilder builder,  TaskSession taskSession)
	{
		if (matchstickTaskProcessor.newTaskNumber(taskSession) == 1)
		{
			builder.appendBodyScriptCommand("document.getElementById('button-info').click();");
		}
	}
}