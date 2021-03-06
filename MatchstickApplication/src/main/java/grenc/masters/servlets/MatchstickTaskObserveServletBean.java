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
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.resources.Video;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.servlets.delegate.popup.MatchstickTaskInfoPopup;
import grenc.masters.uservalidation.BrowserDetails;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Translate;
import grenc.masters.webpage.element.AccountBall;
import grenc.masters.webpage.element.DataPresentBall;
import grenc.masters.webpage.element.ExperimentFinishedBall;
import grenc.masters.webpage.element.LanguageBall;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
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
	
	@InsertBean
	private ExperimentFinishedBall experimentFinishedBall;
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
		return "/matchstickTaskObserve";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Matchstick task");

		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		TaskSession taskSession = matchstickTaskProcessor.taskSessionToUse(session);
		MatchstickGroup group = matchstickTaskProcessor.matchstickGroupType(session);
		
		languageBall.set(builder, session.getLang(), url());
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		accountBall.set(builder, servletContext, session.getLang());
		experimentFinishedBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session).withMatchstickGroup(builder, session, group);
		matchstickTaskInfoPopup.createPopup(builder, servletContext, session.getLang(), group, true);
		
		if (new BrowserDetails(session, request).isIEorSafariorEdge())
			builder.addStyle(Style.split_page_ie);

		builder.appendOnlyAssociatedPageElements(PageElement.matchstick_task_observe);
		builder.appendPageElement(translateProcessor.process(new MatchstickTaskObservePage(servletContext), session.getLang()));
		
		// Setup current task number
		builder.appendBodyScriptCommand("setObservingTaskNumber("+matchstickTaskProcessor.newTaskNumberForLocalPhase(taskSession)+", "+matchstickTaskProcessor.newTaskNumber(taskSession)+", "+matchstickTaskProcessor.totalNumberOfTasksForNextPhase(taskSession)+");");

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
			logger.log(session, "Change forwarding request url to: " + forwardUrl);	
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
	
	@SuppressWarnings("unused")
	private class MatchstickTaskObservePage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment headertext = new SimpleTranslatableSegment(context, "translations/matchstick-task/headertext.json");
		private SimpleTranslatableSegment observing_task = new SimpleTranslatableSegment(context, "translations/matchstick-task/observing_task.json");

		public MatchstickTaskObservePage(ServletContext context)
		{
			super(context, PageElement.matchstick_task_observe);
		}
	}
}