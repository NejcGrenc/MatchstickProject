package grenc.masters.servlets.developtools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.database.SessionDAO;
import grenc.masters.entities.Session;

public class InitialCallHandler 
{
	private static final String sessionCookieName = "sessionCookie";
	
	private static final String testTasksParam = "testTasks";
	private static final String snoopParam = "snoop";

	private SessionDAO sessionDAO;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public InitialCallHandler(HttpServletRequest request, HttpServletResponse response, SessionDAO sessionDAO) 
	{
		this.request = request;
		this.response = response;
		this.sessionDAO = sessionDAO;
	}
	public InitialCallHandler(HttpServletRequest request, HttpServletResponse response) 
	{
		this (request, response, SessionDAO.getInstance());
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
		Cookie sessionCookie = getSessionCookie();
		Session session;
		if (sessionCookie != null)
		{
			session = getSessionFromCookie(sessionCookie);
		}
		else
		{
			session = new SessionGenerator().generateSession();
			setSessionCookie(session.getTag());
		}
		request.setAttribute("session", session.getTag());

		
		if (SkipLogin.shouldSkip(request)) 
		{
			String forwardUrl = new SkipLogin(request).skip();
			request.setAttribute("forwardUrl", forwardUrl);
		}
		testTasks();
		snoop();
	}
	
	private Cookie getSessionCookie()
	{
		if (request.getCookies() == null)
			return null;
			
		for (Cookie cookie : request.getCookies())
			if (sessionCookieName.contains(cookie.getName()))
				return cookie;
		return null;
	}
	private Session getSessionFromCookie(Cookie sessionCookie)
	{
		String sessionTag = sessionCookie.getValue();
		return SessionDAO.getInstance().findSessionByTag(sessionTag);
	}
	
	private void setSessionCookie(String sessionTag)
	{
		Cookie sessionCookie = new Cookie(sessionCookieName, sessionTag);
		response.addCookie(sessionCookie);
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
