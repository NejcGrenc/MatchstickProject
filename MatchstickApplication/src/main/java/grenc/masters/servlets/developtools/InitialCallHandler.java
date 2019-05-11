package grenc.masters.servlets.developtools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.simpleton.Beans;


public class InitialCallHandler 
{
	private static final String sessionCookieName = "sessionCookie";
	
	private static final String testTasksParam = "testTasks";
	private static final String snoopParam = "snoop";

	private SessionDAO sessionDAO;
	
	private SessionGenerator sessionGenerator = Beans.get(SessionGenerator.class);
	private SkipLogin skipLogin = Beans.get(SkipLogin.class);
	
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
		return (request.getAttribute("session") == null || isRestartAnew(request));
	}
	
	/**
	 * Handles special parameter handling, 
	 * that is mostly meant for testing and development purposes
	 */
	public void handle()
	{
		if (isRestartAnew(request)) 
		{
			Session session = new SessionGenerator().generateSession();
			setSessionCookie(session.getTag());
			request.setAttribute("session", session.getTag());
		}
		else 
		{
			Cookie sessionCookie = getSessionCookie();
			Session session = null;
			if (sessionCookie != null)
			{
				session = getSessionFromCookie(sessionCookie);
			}
			if (session == null) 
			{
				session = sessionGenerator.generateSession();
				setSessionCookie(session.getTag());
			}
			request.setAttribute("session", session.getTag());
		}

		
		if (SkipLogin.shouldSkip(request)) 
		{
			String forwardUrl = skipLogin.skip(request);
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
	
	private static boolean isRestartAnew(HttpServletRequest request)
	{
		return request.getAttribute("restart_anew") != null;
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
