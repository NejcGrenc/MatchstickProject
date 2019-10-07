package grenc.masters.servlets.selector;

import java.util.List;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.servlets.LanguageServletBean;
import grenc.masters.servlets.LoginServletBean;
import grenc.masters.servlets.SelectTaskImagesServletBean;
import grenc.masters.servlets.SelectTaskMatchstickServletBean;
import grenc.masters.servlets.SelectTaskServletBean;
import grenc.masters.servlets.UserDataServletBean;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class Selector
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDao;
	
	@InsertBean
	private LanguageServletBean languageServlet;
	@InsertBean
	private LoginServletBean loginServlet;	
	@InsertBean
	private UserDataServletBean userDataServlet;
	@InsertBean
	private SelectTaskServletBean selectTaskServlet;
	@InsertBean
	private SelectTaskMatchstickServletBean selectTaskMatchstickServletBean;
	@InsertBean
	private SelectTaskImagesServletBean selectTaskImagesServletBean;
	
	/*
	 * Endpoint priorities:
	 * 0) Set session (if not set)
	 * 1) Set language (if not set)
	 * 2) Set user name (if not set)
	 * 3) Set user data (if not set)
	 * 4) Actually consider forwardUrl
	 * 5) The default in all cases is SelectTask
	 */
	
	public String select(String forwardUrl, String previousUrl, String sessionTag)
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
			return languageServlet.url();
		}
		
		// LOGIN
		if (session.getSubjectId() == 0)
		{
			return loginServlet.url();
		}

		// USER DATA
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		if (subject.isMissingUserData())
		{
			return userDataServlet.url();
		}
		
		List<TaskSession> taskSessions = taskSessionDao.findAllTaskForSessionId(session.getId());
		
		// MATCHSTICK TASK
		if (taskSessions.stream().anyMatch(s -> TaskType.matchstick == s.getTaskType()))
		{
			return selectTaskMatchstickServletBean.url();
		}
		
		// IMAGES TASK
		if (taskSessions.stream().anyMatch(s -> TaskType.images == s.getTaskType()))
		{
			return selectTaskImagesServletBean.url();
		}
		
		// UNKNOWN - have to handle separately
		return "UNKNOWN";
	}
	
	private boolean isNullOrEmpty(String str)
	{
		return str == null || str.isEmpty();
	}
	
}
