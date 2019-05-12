package grenc.masters.servlets.developtools;

import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.servlets.LanguageServletBean;
import grenc.masters.servlets.LoginServletBean;
import grenc.masters.servlets.SelectTaskServletBean;
import grenc.masters.servlets.UserDataServletBean;
import grenc.masters.servlets.bean.base.ServletBean;
import grenc.masters.servlets.bean.service.ServletBeanProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class SkipLogin 
{
	private static final String skipParameter = "skip";

	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	
	@InsertBean
	private SessionGenerator sessionGenerator;
	@InsertBean
	private ServletBeanProcessor servletBeanProcessor;
	
	@InsertBean
	private LanguageServletBean languageServlet;
	@InsertBean
	private LoginServletBean loginServlet;	
	@InsertBean
	private UserDataServletBean userDataServlet;
	@InsertBean
	private SelectTaskServletBean selectTaskServlet;

	
	public static boolean shouldSkip(HttpServletRequest request)
	{
		String skip = (String) request.getAttribute(skipParameter);
		String forwardUrl = (String) request.getAttribute("forwardUrl");
		return  skip != null && forwardUrl != null && !forwardUrl.isEmpty();
	}
	

	public String skip(HttpServletRequest request) {
		
		System.out.println("Skipping!");

		ServletBean forwardServlet = forwardTo(request);
		System.out.println("Skipping to " + forwardServlet.url());
		
		if (forwardServlet == languageServlet)
		{
			Session session = sessionGenerator.generateSession();
			request.setAttribute("session", session.getTag());
			System.out.println("Skipping with session " + session.getTag());
		}
		if (forwardServlet == loginServlet)
		{
			Session session = sessionGenerator.generateSession();
			String lang = "en"; // Randomize
			languageServlet.setLanguage(session.getTag(), lang);
			
			request.setAttribute("session", session.getTag());
			request.setAttribute("lang", lang);
			
			System.out.println("Skipping with session: " + session.getTag() + " and lang: " + lang);
		}
		if (forwardServlet == userDataServlet)
		{
			Session session = sessionGenerator.generateSession();
			String lang = "en"; // Randomize
			languageServlet.setLanguage(session.getTag(), lang);
			
			String subjectName = "Tony";
			Subject subject = subjectDAO.insertSubject(session.getId());
			sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());
			
			request.setAttribute("session", session.getTag());
			request.setAttribute("lang", lang);
			request.setAttribute("subjectName", subjectName);
			
			System.out.println("Skipping with session: " + session.getTag() + " and lang: " + lang + " and subjName: " + subjectName);
		}
		if (forwardServlet == selectTaskServlet)
		{
			Session session = sessionGenerator.generateSession();
			String lang = "en"; // Randomize
			languageServlet.setLanguage(session.getTag(), lang);
			
			String subjectName = "Tony";
			Subject subject = subjectDAO.insertSubject(session.getId());
			sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());

			subjectDAO.updateSubject(subject.getId(), 18, "m", lang);
			
			request.setAttribute("session", session.getTag());
			request.setAttribute("lang", lang);
			request.setAttribute("subjectName", subjectName);
			
			System.out.println("Skipping with session: " + session.getTag() + " and lang: " + lang + " and subjName: " + subjectName);
		}

		return forwardServlet.url();
	}
	
	private ServletBean forwardTo(HttpServletRequest request) {
		String forwardUrl = (String) request.getAttribute("forwardUrl");
		if (forwardUrl == null || forwardUrl.isEmpty())
			throw new RuntimeException("Parameter 'forwardUrl' cannot be null or empty when skipping.");
		
		ServletBean forwardServletBean = servletBeanProcessor.servletBeanByUrl(forwardUrl);
		if (forwardServletBean == null) 
			throw new RuntimeException("Parameter 'forwardUrl' unrecognizable: " + forwardUrl);
		
		return forwardServletBean;
	}
	
//	TODO something like this
//	private List<Servlet> executionPath() {
//		return Arrays.asList(Servlet.SessionGeneratorServlet, Servlet.LanguageServlet);
//	}

}