package grenc.masters.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import grenc.masters.database.ImageTaskDataDAO;
import grenc.masters.database.SessionDAO;
import grenc.masters.database.TaskSessionDAO;
import grenc.masters.entities.ImageTaskData;
import grenc.masters.entities.Session;
import grenc.masters.entities.TaskSession;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.utils.PrintUtils;
import grenc.masters.webpage.builder.AccountBallBuilder;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;


public class ImagesTaskServlet extends BasePageServlet 
{
	private static final long serialVersionUID = -1953103899973255388L;
	
	public static final String imagesTaskType = "images";

	private SessionDAO sessionDAO;
	private TaskSessionDAO taskSessionDAO;
	private ImageTaskDataDAO imageTaskDataDAO;
	
	public ImagesTaskServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
		this.taskSessionDAO = TaskSessionDAO.getInstance();
		this.imageTaskDataDAO = ImageTaskDataDAO.getInstance();
	}
	
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.ImagesTaskServlet;
	}
	
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Images task");

		builder.addStyle(Style.style);
		builder.addStyle(Style.buttons);

		builder.addScript(Script.send);
		builder.addScript(Script.familiar_figures);
		
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder);
		new AccountBallBuilder().fromSession(session).withBuilder(builder).build().set();
		new DataPresentBall(builder, session).set();
		
		builder.appendPageElementFile(PageElement.image_task);

		if (session.isTestTasksOnly())
		{
			builder.appendBodyScriptCommand("limitTasks(2);");
		}

		taskSessionDAO.insert(session.getId(), imagesTaskType, new Date().getTime(), null, false, null);	
		
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		System.out.println("Main session: " + session);
		
		List<TaskSession> taskSessions = taskSessionDAO.findAllTaskForSessionIdAndTaskTypeAndComplete(session.getId(), imagesTaskType, false);
		if (taskSessions == null || taskSessions.isEmpty())
		{
			throw new RuntimeException("No valid task session found");
		}
		PrintUtils.printList("Task Sesions", taskSessions);
		TaskSession taskSession = taskSessions.get(0);
		
		String scores = (String) request.getAttribute("scores");
		System.out.println("Value of 'scores' is " + scores);
		if (scores == null || scores.isEmpty())
		{
			System.out.println("Did not send task data! (probably clicked one of the up-right buttons)");
			return;
		}

		System.out.println("Returned data " + scores);

		storeData(taskSession, scores);
		finishTaskSession(taskSession);
		System.out.println();
		System.out.println();
	}
	
	// TODO make tests for building data properly
	void storeData(TaskSession taskSession, String stringData)
	{
		JSONTokener tokener = new JSONTokener(stringData);
		JSONObject data = new JSONObject(tokener);
		
		JSONArray scores = data.getJSONArray("scores");
		JSONArray times = data.getJSONArray("times");
		JSONArray images = data.getJSONArray("images");
		
		for (int i = 0; i < scores.length(); i++)
		{
			buildTaskData(taskSession.getId(), i, shortenImageId(images.getString(i)), times.getLong(i), scores.getBoolean(i));
			System.out.println(i + ". " + scores.getBoolean(i) + " - " + times.getLong(i) + " -> " + images.getString(i));
		}
	}
	
	
	private ImageTaskData buildTaskData(int taskSessionId, int number, String imageId, long time, boolean correct)
	{
		return imageTaskDataDAO.insert(taskSessionId, number, imageId, time, correct);
	}
	
	private String shortenImageId(String originalImageUrl)
	{
		String[] parts = originalImageUrl.split("/");
		return parts[parts.length-1];
	}
	
	private void finishTaskSession(TaskSession taskSession)
	{
		System.out.println("Marking task session " + taskSession + " as complete!");
		taskSessionDAO.updateComplete(taskSession.getId(), true);
	}
}
