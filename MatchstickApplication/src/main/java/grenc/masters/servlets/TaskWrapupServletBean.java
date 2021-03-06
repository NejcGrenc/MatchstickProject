package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.webpage.builder.WebpageBuilder;
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
public class TaskWrapupServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	
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
		return "/taskWrapup";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Done");
		
		builder.addStyle(Style.background);
		builder.setBodyStyle("background");
		
		builder.addStyle(Style.style);
		builder.addStyle(Style.centered);
		builder.addStyle(Style.layout);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.buttons_credits);
		builder.addScript(Script.send);

		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		languageBall.set(builder, session.getLang(), url());
		accountBall.set(builder, servletContext, session.getLang());
		experimentFinishedBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session);

		builder.appendOnlyAssociatedPageElements(PageElement.task_wrapup);
		builder.appendPageElement(translateProcessor.process(new TaskWrapupPage(servletContext), session.getLang()));

		
		boolean noFinishedMatchstickTask = taskSessionDAO.findAllTaskForSessionIdAndTaskTypeAndComplete(session.getId(), TaskType.matchstick.name(), true).isEmpty();
		boolean noFinishedImagesTask = taskSessionDAO.findAllTaskForSessionIdAndTaskTypeAndComplete(session.getId(), ImagesTaskServletBean.imagesTaskType, true).isEmpty();
		if (noFinishedMatchstickTask)
		{
			builder.appendBodyScriptCommand("presentMatchstickTaskButton();");
		}
		if (noFinishedImagesTask)
		{
			builder.appendBodyScriptCommand("presentImagesTaskButton();");
		}
		if (!noFinishedMatchstickTask && !noFinishedImagesTask)
		{
			builder.appendBodyScriptCommand("presentDoneButton();");
		}
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Save comment data
		String comment = (String) request.getAttribute("comment");
		if (comment != null && !comment.isEmpty())
		{
			Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
			TaskSession taskSession = taskSessionDAO.findAllTaskForSessionId(session.getId()).get(0);  // Possible null pointers
			
			logger.log(session, "Updating task session " + taskSession);
			logger.log(session, "    -- with notes: '" + comment + "'");
			taskSessionDAO.updateNotes(taskSession.getId(), comment);
		}
	}
	
	@SuppressWarnings("unused")
	private class TaskWrapupPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment thanks = new SimpleTranslatableSegment(context, "translations/task-wrapup/thanks.json");
		private SimpleTranslatableSegment comment = new SimpleTranslatableSegment(context, "translations/task-wrapup/comment.json");
		private SimpleTranslatableSegment encouragement = new SimpleTranslatableSegment(context, "translations/task-wrapup/encouragement.json");
		private SimpleTranslatableSegment encouragement_finish = new SimpleTranslatableSegment(context, "translations/task-wrapup/encouragement-finish.json");
		private SimpleTranslatableSegment matchsticktask = new SimpleTranslatableSegment(context, "translations/task-wrapup/matchstick-task.json");
		private SimpleTranslatableSegment familiarfigurestask = new SimpleTranslatableSegment(context, "translations/task-wrapup/familiar-figures-task.json");
		private SimpleTranslatableSegment done = new SimpleTranslatableSegment(context, "translations/task-wrapup/done.json");
		private SimpleTranslatableSegment credits = new SimpleTranslatableSegment(context, "translations/task-wrapup/credits.json");

		public TaskWrapupPage(ServletContext context)
		{
			super(context, PageElement.task_wrapup);
		}
	}

}