package grenc.masters.servlets.base;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;

public class Selector
{
	private SessionDAO sessionDAO;
	private SubjectDAO subjectDAO;
	
	private String forwardUrl;
	
	@SuppressWarnings("unused")
	private String previousUrl;
	private String sessionTag;
	
	public Selector()
	{
		this.sessionDAO = SessionDAO.getInstance();
		this.subjectDAO = SubjectDAO.getInstance();
	}
	
	public Selector withForwardUrl(String forwardUrl)
	{
		this.forwardUrl = forwardUrl;
		return this;
	}
	
	public Selector withPreviousUrl(String previousUrl)
	{
		this.previousUrl = previousUrl;
		return this;
	}
	public Selector withSessionTag(String sessionTag)
	{
		this.sessionTag = sessionTag;
		return this;
	}

	/*
	 * Endpoint priorities:
	 * 0) Set session (if not set)
	 * 1) Set language (if not set)
	 * 2) Set user name (if not set)
	 * 3) Set user data (if not set)
	 * 4) Actually consider forwardUrl
	 * 5) The default in all cases is SelectTask
	 */
	
	public String select()
	{
		
		if (isNullOrEmpty(sessionTag))
		{
			throw new RuntimeException("Session cannot be null in Selector");
		}
		
		
		if (! isNullOrEmpty(forwardUrl) && ! forwardUrl.equals("/"))
		{
			return forwardUrl;
		}

		
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		if (isNullOrEmpty(session.getLang()))
		{
			return Servlet.LanguageServlet.getUrl();
		}
		
		// LOGIN
		if (session.getSubjectId() == 0)
		{
			return Servlet.LoginServlet.getUrl();
		}

		// USER DATA
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		if (subject.isMissingUserData())
		{
			return Servlet.UserDataServlet.getUrl();
		}
		
		return Servlet.SelectTaskServlet.getUrl();

//		throw new SelectorException(forwardUrl);
	}
	
	private boolean isNullOrEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
	
	
	@Override
	public String toString()
	{
		return "Selector [forwardUrl=" + forwardUrl + ", previousUrl=" + previousUrl + ", sessionTag=" + sessionTag
				+ "]";
	}
	
	
//	private class SelectorException extends RuntimeException
//	{
//		private static final long serialVersionUID = -5272689905626652282L;
//
//		public SelectorException(String forwardUrl)
//		{
//			super ("No servlet found for forward url: " + forwardUrl);
//		}
//	}
}
