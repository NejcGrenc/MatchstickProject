package grenc.masters.servlets.developtools;

import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.SessionDAO;
import grenc.masters.entities.Session;

public class InitialCallHandler 
{
	private static final String testTasksParam = "testTasks";
	private static final String snoopParam = "snoop";

	private SessionDAO sessionDAO;
	
	private HttpServletRequest request;
	
	public InitialCallHandler(HttpServletRequest request, SessionDAO sessionDAO) 
	{
		this.request = request;
		this.sessionDAO = sessionDAO;
	}
	public InitialCallHandler(HttpServletRequest request) 
	{
		this (request, SessionDAO.getInstance());
	}
	
	
	public static boolean isInitial(HttpServletRequest request)
	{
		return (request.getAttribute("session") == null);
	}
	
	/**
	 * Handles special parameter handling, 
	 * that is mostly meant for testing and development purposes
	 */
	public void handle()
	{
		Session session = new SessionGenerator().generateSession();
		request.setAttribute("session", session.getTag());

		if (SkipLogin.shouldSkip(request)) 
		{
			String forwardUrl = new SkipLogin(request).skip();
			request.setAttribute("forwardUrl", forwardUrl);
		}
		testTasks();
		snoop();
	}
	
	private void testTasks()
	{
		String testTasks = (String) request.getAttribute(testTasksParam);
		if (testTasks != null)
		{
			System.out.println("Setting up only test tasks!");
			Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
			sessionDAO.updateSessionTestTasksOnly(session.getId(), true);
		}
	}
	
	private void snoop()
	{
		String snoop = (String) request.getAttribute(snoopParam);
		if (snoop != null)
		{
			System.out.println("Setting up snooping!");
			Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
			sessionDAO.updateSessionSnoopEnabled(session.getId(), true);
		}
	}
	
}
