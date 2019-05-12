package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.TaskSessionAssist;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.uservalidation.BrowserDetails;
import grenc.masters.webpage.CreditsBall;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class SelectTaskServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	
	@InsertBean
	private MatchstickTaskServletBean matchstickTaskServlet;
	
	@InsertBean
	private TaskSessionAssist taskSessionAssist;
	
	@Override
	public String url()
	{
		return "/selectTask";
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Select task");

		builder.addStyle(Style.split_page);
		builder.addScript(Script.send);
		builder.appendPageElementFile(PageElement.select_task);
		
		if (new BrowserDetails(request).isIEorSafari())
			builder.addStyle(Style.split_page_ie);
			
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		new LanguageBall(builder, session.getLang(), url()).set();
		new Translate(builder, Script.translate_selecttask).translateAll();
		new AccountBall(builder, session, servletContext).set();
		new DataPresentBall(builder, session).set();

		new CreditsBall(builder).set();
	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Prepare new TaskSession
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		TaskType selectedTaskType;
		if ("/imagesTask".equals((String) request.getAttribute("forwardUrl"))) 
			selectedTaskType = TaskType.images;
		else
			selectedTaskType = TaskType.matchstick;	
		
		// TaskSession automatically builds itself when you try to fetch it and it doesn't exist yet
		TaskSession taskSession = taskSessionAssist.getTaskSessionToUse(session, selectedTaskType);
		
		// Skip learning if we are part of matchstick task group 0
		if (isMatchstickGroup0(selectedTaskType, taskSession))
		{
			request.setAttribute("forwardUrl", matchstickTaskServlet.url());
		}
	}

	private boolean isMatchstickGroup0(TaskType selectedTaskType, TaskSession taskSession)
	{
		return TaskType.matchstick.equals(selectedTaskType) && 
				(taskSession.getMatchstickGroup().equals(MatchstickGroup.group_0_strategyA) || taskSession.getMatchstickGroup().equals(MatchstickGroup.group_0_strategyB));
	}
}