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
import grenc.masters.servlets.base.bean.BasePageServlet;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;
import grenc.simpleton.Beans;
import grenc.simpleton.annotation.Bean;


@Bean
public class TaskWrapupServletBean extends BasePageServlet
{
	private SessionDAO sessionDAO = Beans.get(SessionDAO.class);
	private TaskSessionDAO taskSessionDAO = Beans.get(TaskSessionDAO.class);

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
		
		builder.addStyle(Style.centered);
		builder.addStyle(Style.layout);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.buttons_credits);
		builder.addScript(Script.send);

		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		new LanguageBall(builder, session.getLang(), url()).set();
		new Translate(builder, Script.translate_login).translateAll();
		new AccountBall(builder, session, servletContext).set();
		new DataPresentBall(builder, session).set();

		builder.appendPageElementFile(PageElement.task_wrapup);
		
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
			
			System.out.println("Updating task session " + taskSession);
			System.out.println("    -- with notes: '" + comment + "'");
			taskSessionDAO.updateNotes(taskSession.getId(), comment);
		}
	}

}