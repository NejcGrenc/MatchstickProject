package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.matchsticktask.MatchstickTaskProcessor;
import grenc.masters.matchsticktask.MatchstickTaskProcessor.MatchstickTaskProcessorReturn;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.webpage.builder.AccountBallBuilder;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;

public class MatchstickTaskLearnServlet extends BasePageServlet
{
	private static final long serialVersionUID = 4570044122511332568L;

	private SessionDAO sessionDAO;
	
	public MatchstickTaskLearnServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
	}
	
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.MatchstickTaskLearnServlet;
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Matchstick task");

		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBallBuilder().fromSession(session).withBuilder(builder).build().set();
		new DataPresentBall(builder, session).set();

		builder.appendPageElementFile(PageElement.matchstick_task_learn);

		builder.appendBodyScriptCommand("var originalEquation = '" + "7+2+3=5" + "';");

				
		// Start the task when all is properly set
		if (true)
			builder.appendBodyScriptCommand("startWithPause();");
		else
			builder.appendBodyScriptCommand("start();");
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		// TODO Auto-generated method stub
		
	}
	
	
	private void loadInformationPage()
	{
		
	}
	
	private void loadSelfSolvable()
	{
		
	}
	
	private void loadLearningTask()
	{
		
	}
	
}	
