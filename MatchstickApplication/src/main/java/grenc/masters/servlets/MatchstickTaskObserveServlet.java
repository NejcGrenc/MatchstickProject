package grenc.masters.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.matchsticktask.MatchstickTaskProcessor;
import grenc.masters.matchsticktask.ResponseProcessor;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Video;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.servlets.helper.MatchstickTaskInfoPopup;
import grenc.masters.webpage.builder.AccountBallBuilder;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;

public class MatchstickTaskObserveServlet extends BasePageServlet
{
	private static final long serialVersionUID = 4570044122511332568L;

	private SessionDAO sessionDAO;
	
	public MatchstickTaskObserveServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
	}
	
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.MatchstickTaskObserveServlet;
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Matchstick task");

		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		MatchstickTaskProcessor taskBuilder = new MatchstickTaskProcessor(session);
		
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBallBuilder().fromSession(session).withBuilder(builder).build().set();
		new DataPresentBall(builder, session).set().withMatchstickGroup(taskBuilder.matchstickGroupType());
		new MatchstickTaskInfoPopup(builder, getServletContext()).createPopup(session.getLang());

		builder.appendPageElementFile(PageElement.matchstick_task_observe);

		prepareObserveTask(builder, taskBuilder);

		openInfoPopupBeforeFirstVideo(builder, taskBuilder);
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		if (! commonInstance().getUrl().equals((String) request.getAttribute("forwardUrl")))
		{   // Some other action - not finished watching video
			return;
		}
		
		ResponseProcessor responseProcessor = new ResponseProcessor(session);
		responseProcessor.storeEmptyTask(); // Just make an entry to increment the counter
		
		MatchstickTaskProcessor taskBuilder = new MatchstickTaskProcessor(session);
		if (taskBuilder.nextPhase() != MatchstickExperimentPhase.LearningPhase_Showing)
		{
			// Task is completed, forward to Learn-page
			String forwardUrl = Servlet.MatchstickTaskLearnServlet.getUrl();
			request.setAttribute("forwardUrl", forwardUrl);
			System.out.println("Change forwarding request url to: " + forwardUrl);	
		}
	}

	
	private void prepareObserveTask(WebpageBuilder builder, MatchstickTaskProcessor taskBuilder)
	{
		List<Video> videos = taskBuilder.prepareNewObserveMatchstickTask();
		for (Video video : videos)
			addSource(builder, video);
		builder.appendBodyScriptCommand("setup();");
	}
	
	private void addSource(WebpageBuilder builder, Video video)
	{
		String sourceCommand = "addSource('" + video.getSource() + "', '" + video.getType() + "');";
		builder.appendBodyScriptCommand(sourceCommand);
	}
	
	private void openInfoPopupBeforeFirstVideo(WebpageBuilder builder,  MatchstickTaskProcessor taskBuilder)
	{
		if (taskBuilder.newTaskNumber() == 1)
		{
			builder.appendBodyScriptCommand("document.getElementById('button-info').click();");
		}
	}
}	
