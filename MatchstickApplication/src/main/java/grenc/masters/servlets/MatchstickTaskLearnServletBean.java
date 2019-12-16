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
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.servlets.delegate.popup.MatchstickTaskInfoPopup;
import grenc.masters.uservalidation.BrowserDetails;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Translate;
import grenc.masters.webpage.element.AccountBall;
import grenc.masters.webpage.element.DataPresentBall;
import grenc.masters.webpage.element.LanguageBall;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
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
		return "/matchstickTaskLearn";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Matchstick task");

		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		MatchstickGroup group = taskBuilder.matchstickGroupType(session);
		
		languageBall.set(builder, session.getLang(), url());
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		accountBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session).withMatchstickGroup(builder, session, group);
		matchstickTaskInfoPopup.createPopup(builder, servletContext, session.getLang(), group, true);
		
		if (new BrowserDetails(session, request).isIEorSafariorEdge())
			builder.addStyle(Style.split_page_ie);
		
		builder.appendOnlyAssociatedPageElements(PageElement.matchstick_task_learn);
		builder.appendPageElement(translateProcessor.process(new MatchstickTaskLearnPage(servletContext), session.getLang()));
		
		// Setup current task number
		TaskSession taskSession = taskBuilder.taskSessionToUse(session);
		builder.appendBodyScriptCommand("setLearningTaskNumber("+taskBuilder.newTaskNumberForLocalPhase(taskSession)+", "+taskBuilder.newTaskNumber(taskSession)+", "+taskBuilder.totalNumberOfTasksForNextPhase(taskSession)+");");
		builder.appendBodyScriptCommand("setRestriction("+true+");");
		
		loadLearningTask(builder, session);
		
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		TaskSession taskSession = taskBuilder.taskSessionToUse(session);

		if (! url().equals((String) request.getAttribute("forwardUrl")))
		{   // Some other action - not finished learning
			return;
		}
		
		responseProcessor.storeEmptyTask(taskSession); // Just make an entry to increment the counter
		
		if (taskBuilder.nextPhase(taskSession) != MatchstickExperimentPhase.LearningPhase_Solving)
		{
			// Task is completed, forward to actual-task-page
			String forwardUrl = matchstickTaskServlet.url();
			request.setAttribute("forwardUrl", forwardUrl);
			logger.log(session, "Change forwarding request url to: " + forwardUrl);	
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
	
	
	@SuppressWarnings("unused")
	private class MatchstickTaskLearnPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment headertext = new SimpleTranslatableSegment(context, "translations/matchstick-task/headertext.json");
		private SimpleTranslatableSegment learning_task = new SimpleTranslatableSegment(context, "translations/matchstick-task/learning_task.json");

		public MatchstickTaskLearnPage(ServletContext context)
		{
			super(context, PageElement.matchstick_task_learn);
		}
	}
}