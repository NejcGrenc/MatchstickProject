package grenc.masters.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.ImageTaskDataDAO;
import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.ImageTaskData;
import grenc.masters.database.entities.MatchstickActionData;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Collapsible;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class DataPresentServletBean extends BasePageServlet
{
	private static final String ERROR_SESSION_NULL = "<h3>ERROR - session not defined</h3>";
	private static final String ERROR_SESSION_NOT_FOUND = "<h3>ERROR - session not found by tag: '%s'</h3>";

	
	private static final String breakCh = "<br />";
	private static final String tabCh = "&nbsp;&nbsp;&nbsp;&nbsp;";
	
	// How to use it
	//	builder.appendPageElement(breakCh);

	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	@InsertBean
	private MatchstickActionDataDAO matchstickActionDataDAO;
	@InsertBean
	private ImageTaskDataDAO imageTaskDataDAO;

	
	@Override
	public String url()
	{
		return "/presentTaskData";
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Data from database");
		
//		String sessionTag = ((SessionGeneratorServlet) Servlet.SessionGeneratorServlet.getServletInstance()).generateRandomString();

//		new RandomDataGenerator(sessionTag);
		
		String sessionTag = (String) request.getAttribute("session");
		if (sessionTag == null)
			sessionTag = (String) request.getParameter("session");
		
		Collapsible.enableCollapsible(builder);

		Collapsible.addCollapsible(builder, buildDataTree(sessionTag));
	}
	
	
	// TODO: Test all corner cases !!!
	/**
	 *  Depends heavily on toString() functions of objects as they are used to present their content in user friendly way. <br>
	 *  This function builds an object tree, where each node presents data form one table and it is linked to one parent
	 *  and none-to-multiple child nodes as data relationships define.
	 */
	private String buildDataTree(String sessionTag) 
	{
		if (sessionTag == null || sessionTag.isEmpty())
			return ERROR_SESSION_NULL;
		
		Session session = sessionDAO.findSessionByTag(sessionTag);
		if (session == null)
			return String.format(ERROR_SESSION_NOT_FOUND, sessionTag);
		
		
		String taskSessionStr = "";
		for (TaskSession taskSession : reverse(taskSessionDAO.findAllTaskForSessionId(session.getId())))
		{
			String taskDataStr = "";
			
			if (TaskType.matchstick.equals(taskSession.getTaskType()))
			{
				for (MatchstickTaskData taskData : reverse(matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId())))
				{
					String actionDataStr = "";
					for (MatchstickActionData actionData : reverse(matchstickActionDataDAO.findAllDataForMatchstickTaskId(taskData.getId())))
					{
						actionDataStr += present(actionData.toString());
					}
					actionDataStr += breakCh;
					taskDataStr += packageIt(taskData.getClass().getSimpleName(), present(taskData.toString()), actionDataStr);
				}
			}
			else
			{
				List<ImageTaskData> allTasks = reverse(imageTaskDataDAO.findAllTaskForSessionId(taskSession.getId()));
				for (ImageTaskData taskData : allTasks)
				{
					taskDataStr += present(taskData.toString());
				}
			}

			taskDataStr += breakCh;
			taskSessionStr += packageIt(taskSession.getClass().getSimpleName(), present(taskSession.toString()), taskDataStr);
		}
		taskSessionStr += breakCh;
		
		String sessionStr = packageIt(true, session.getClass().getSimpleName(), present(session.toString()), taskSessionStr);
		
		
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		if (subject == null)
			return sessionStr;
		
		// Package everything around subject
		String subjectStr = packageIt(true, subject.getClass().getSimpleName(), present(subject.toString()), sessionStr);
		return subjectStr;
	}
	
	
	private String present(String initialString) 
	{
		return fixToStringRepresentation(initialString);
	}
	
	/**
	 * Takes original .toString() representation of an object
	 * and splits it into a presentable multi-line form.
	 * @param initialString to fix
	 * @return initialString split into multiple lines
	 */
	private String fixToStringRepresentation(String initialString)
	{
		initialString = initialString.replace("]", "");
		String originName = initialString.split("\\[")[0];
		String[] content = initialString.split("\\[")[1].split(", ");
		String contentStr = "";
		for (String c : content)
		{
			c = c.replace("=", " = ");
			contentStr += tabCh + c + breakCh;
		}
		return originName + breakCh + contentStr + breakCh;
	}
	
	private String packageIt(String upper, String... data)
	{
		return packageIt(false, upper, data);
	}
	private String packageIt(boolean open, String upper, String... data)
	{
		return Collapsible.html(open, upper, data);
	}

	private <T> List<T> reverse(List<T> original)
	{
		Collections.reverse(original);
		return original;
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext)
			throws IOException, ServletException
	{
		// No need to do anything		
	}
}