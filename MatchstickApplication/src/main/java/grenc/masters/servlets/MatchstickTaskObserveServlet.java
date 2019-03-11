package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
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
		
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
//		new Translate(builder, Script.translate_matchsticktask).translateAll();
		new AccountBallBuilder().fromSession(session).withBuilder(builder).build().set();
		new DataPresentBall(builder, session).set();
		new MatchstickTaskInfoPopup(builder, getServletContext()).createPopup();

		builder.appendPageElementFile(PageElement.matchstick_task_observe);

	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		processFinished(request);
	}
	
	public void processFinished(HttpServletRequest request)
	{
		// Task is completed, forward to after-page
		String forwardUrl = Servlet.MatchstickTaskLearnServlet.getUrl();
		request.setAttribute("forwardUrl", forwardUrl);
		System.out.println("Change forwarding request url to: " + forwardUrl);	
	}
	
}	
