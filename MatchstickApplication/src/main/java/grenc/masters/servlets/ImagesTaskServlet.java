package grenc.masters.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import grenc.masters.database.dao.ImageTaskDataDAO;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.ImageTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.bean.BasePageServlet;
import grenc.masters.servlets.base.bean.BaseServlet;
import grenc.masters.servlets.helper.ImageTaskInfoPopup;
import grenc.masters.utils.PrintUtils;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.Translate;
import grenc.simpleton.Beans;
import grenc.simpleton.annotation.Bean;


public class ImagesTaskServlet extends BaseServlet
{	
	private static final long serialVersionUID = -1953103899973255388L;

	public ImagesTaskServlet()
	{
		super(Beans.get(ImagesTaskServletBean.class));
	}	
}

@Bean
class ImagesTaskServletBean extends BasePageServlet
{	
	public static final String imagesTaskType = "images";

	private SessionDAO sessionDAO = Beans.get(SessionDAO.class);
	private TaskSessionDAO taskSessionDAO = Beans.get(TaskSessionDAO.class);
	private ImageTaskDataDAO imageTaskDataDAO = Beans.get(ImageTaskDataDAO.class);
	
	
	@Override
	public String url()
	{
		return "/imagesTask";
	}

	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Images task");

		builder.addStyle(Style.style);
		builder.addStyle(Style.buttons);

		builder.addScript(Script.send);
		builder.addScript(Script.translate);
		builder.addScript(Script.familiar_figures);
		
		
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		
		// Options to change languages 
		// should be removed from the page for now
		// because it can break the experiment flow
		//
		// new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		builder.appendBodyScriptCommand("var userLang = '" + session.getLang() + "';");
		builder.appendBodyScriptCommand("var currentPage = '" + url() + "';");
		
		
		new Translate(builder, Script.translate_familiarfigures).translateAll();
		new AccountBall(builder, session, servletContext).set();
		new DataPresentBall(builder, session).set();
		new ImageTaskInfoPopup(builder, servletContext).createPopup(session.getLang());

		builder.appendPageElementFile(PageElement.image_task);

		openInfoPopupBeforeStart(builder);
		
		if (session.isTestTasksOnly())
		{
			builder.appendBodyScriptCommand("limitTasks(2);");
		}
		builder.appendBodyScriptCommand("start();");

		taskSessionDAO.insert(session.getId(), imagesTaskType, new Date().getTime(), null, false, null);	
		
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
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
	
	private void openInfoPopupBeforeStart(WebpageBuilder builder)
	{
		builder.appendBodyScriptCommand("document.getElementById('button-info').click();");
	}


}
