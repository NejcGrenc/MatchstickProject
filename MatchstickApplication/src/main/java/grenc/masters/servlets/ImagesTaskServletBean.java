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
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.servlets.delegate.popup.ImageTaskInfoPopup;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Translate;
import grenc.masters.webpage.element.AccountBall;
import grenc.masters.webpage.element.DataPresentBall;
import grenc.masters.webpage.element.ExperimentFinishedBall;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class ImagesTaskServletBean extends BasePageServlet
{	
	public static final String imagesTaskType = "images";

	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private ImageTaskDataDAO imageTaskDataDAO;
	
	@InsertBean
	private ImageTaskInfoPopup imageTaskInfoPopup;
	
	@InsertBean
	private ExperimentFinishedBall experimentFinishedBall;
	@InsertBean
	private AccountBall accountBall;
	@InsertBean
	private DataPresentBall dataPresentBall;	


	@InsertBean
	private TranslationProcessor translateProcessor;
	
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
		builder.addScript(Script.translate_familiarfigures);

		
		
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
		accountBall.set(builder, servletContext, session.getLang());
		experimentFinishedBall.set(builder, servletContext, session.getLang());
		dataPresentBall.set(builder, session);
		imageTaskInfoPopup.createPopup(builder, servletContext, session.getLang());

		builder.appendOnlyAssociatedPageElements(PageElement.image_task);
		builder.appendPageElement(translateProcessor.process(new ImagesTaskMainPage(servletContext), session.getLang()));
		
		openInfoPopupBeforeStart(builder);
		
		builder.appendBodyScriptCommand("start();");

		taskSessionDAO.insert(session.getId(), imagesTaskType, new Date().getTime(), null, false, null);	
		
	}

	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		String sessionTag = (String) request.getAttribute("session");
		Session session = sessionDAO.findSessionByTag(sessionTag);
		logger.log(session, "Main session: " + session);
		
		List<TaskSession> taskSessions = taskSessionDAO.findAllTaskForSessionIdAndTaskTypeAndComplete(session.getId(), imagesTaskType, false);
		if (taskSessions == null || taskSessions.isEmpty())
		{
			throw new RuntimeException("No valid task session found");
		}
		logger.printList(session, "Task Sesions", taskSessions);
		TaskSession taskSession = taskSessions.get(0);
		
		String scores = (String) request.getAttribute("scores");
		logger.log(session, "Value of 'scores' is " + scores);
		if (scores == null || scores.isEmpty())
		{
			logger.log(session, "Did not send task data! (probably clicked one of the up-right buttons)");
			return;
		}

		logger.log(session, "Returned data " + scores);

		storeData(session, taskSession, scores);
		finishTaskSession(taskSession);
		logger.log(session, "");
	}
	
	// TODO make tests for building data properly
	void storeData(Session session, TaskSession taskSession, String stringData)
	{
		JSONTokener tokener = new JSONTokener(stringData);
		JSONObject data = new JSONObject(tokener);
		
		JSONArray scores = data.getJSONArray("scores");
		JSONArray times = data.getJSONArray("times");
		JSONArray images = data.getJSONArray("images");
		
		for (int i = 0; i < scores.length(); i++)
		{
			buildTaskData(taskSession.getId(), i, shortenImageId(images.getString(i)), times.getLong(i), scores.getBoolean(i));
			logger.log(session, i + ". " + scores.getBoolean(i) + " - " + times.getLong(i) + " -> " + images.getString(i));
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

	
	@SuppressWarnings("unused")
	private class ImagesTaskMainPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment headertext = new SimpleTranslatableSegment(context, "translations/images-task/headertext.json");
		private SimpleTranslatableSegment solving_task = new SimpleTranslatableSegment(context, "translations/images-task/solving_task.json");

		public ImagesTaskMainPage(ServletContext context)
		{
			super(context, PageElement.image_task);
		}
	}

}