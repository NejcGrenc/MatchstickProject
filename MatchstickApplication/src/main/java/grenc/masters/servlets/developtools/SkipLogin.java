package grenc.masters.servlets.developtools;

import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.SessionDAO;
import grenc.masters.database.SubjectDAO;
import grenc.masters.entities.Session;
import grenc.masters.entities.Subject;
import grenc.masters.servlets.LanguageServlet;
import grenc.masters.servlets.base.Servlet;


public class SkipLogin 
{
	private static final String skipParameter = "skip";

	private SessionDAO sessionDAO;
	private SubjectDAO subjectDAO;
	
	private HttpServletRequest request;
	
	public SkipLogin(HttpServletRequest request) 
	{
		this.request = request;
		this.sessionDAO = SessionDAO.getInstance();
		this.subjectDAO = SubjectDAO.getInstance();
	}
	
	
	public static boolean shouldSkip(HttpServletRequest request)
	{
		String skip = (String) request.getAttribute(skipParameter);
		String forwardUrl = (String) request.getAttribute("forwardUrl");
		return  skip != null && forwardUrl != null && !forwardUrl.isEmpty();
	}
	

	public String skip() {
		
		System.out.println("Skipping!");

		Servlet forwardServlet = forwardTo();
		System.out.println("Skipping to " + forwardServlet.getUrl());
		
		if (forwardServlet == Servlet.LanguageServlet)
		{
			Session session = new SessionGenerator().generateSession();
			request.setAttribute("session", session.getTag());
			System.out.println("Skipping with session " + session.getTag());
		}
		if (forwardServlet == Servlet.LoginServlet)
		{
			Session session = new SessionGenerator().generateSession();
			String lang = "en"; // Randomize
			((LanguageServlet) Servlet.LanguageServlet.getServletInstance()).setLanguage(session.getTag(), lang);
			
			request.setAttribute("session", session.getTag());
			request.setAttribute("lang", lang);
			
			System.out.println("Skipping with session: " + session.getTag() + " and lang: " + lang);
		}
		if (forwardServlet == Servlet.UserDataServlet)
		{
			Session session = new SessionGenerator().generateSession();
			String lang = "en"; // Randomize
			((LanguageServlet) Servlet.LanguageServlet.getServletInstance()).setLanguage(session.getTag(), lang);
			
			String subjectName = "Tony";
			Subject subject = subjectDAO.insertSubject(subjectName);
			sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());
			
			request.setAttribute("session", session.getTag());
			request.setAttribute("lang", lang);
			request.setAttribute("subjectName", subjectName);
			
			System.out.println("Skipping with session: " + session.getTag() + " and lang: " + lang + " and subjName: " + subjectName);
		}
		if (forwardServlet == Servlet.SelectTaskServlet)
		{
			Session session = new SessionGenerator().generateSession();
			String lang = "en"; // Randomize
			((LanguageServlet) Servlet.LanguageServlet.getServletInstance()).setLanguage(session.getTag(), lang);
			
			String subjectName = "Tony";
			Subject subject = subjectDAO.insertSubject(subjectName);
			sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());

			subjectDAO.updateSubject(subject.getId(), 18, "m", lang);
			
			request.setAttribute("session", session.getTag());
			request.setAttribute("lang", lang);
			request.setAttribute("subjectName", subjectName);
			
			System.out.println("Skipping with session: " + session.getTag() + " and lang: " + lang + " and subjName: " + subjectName);
		}

		return forwardServlet.getUrl();
	}
	
	private Servlet forwardTo() {
		String forwardUrl = (String) request.getAttribute("forwardUrl");
		if (forwardUrl == null || forwardUrl.isEmpty())
			throw new RuntimeException("Parameter 'forwardUrl' cannot be null or empty when skipping.");
		
		Servlet forwardServlet = Servlet.getServletDescriptionForUrl(forwardUrl);
		if (forwardServlet == null) 
			throw new RuntimeException("Parameter 'forwardUrl' unrecognizable: " + forwardUrl);
		
		return forwardServlet;
	}
	
//	TODO something like this
//	private List<Servlet> executionPath() {
//		return Arrays.asList(Servlet.SessionGeneratorServlet, Servlet.LanguageServlet);
//	}

}