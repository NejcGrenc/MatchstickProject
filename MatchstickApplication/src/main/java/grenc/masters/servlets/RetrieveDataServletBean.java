package grenc.masters.servlets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.database.dao.ImageTaskDataDAO;
import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.TaskSessionProperty;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.servlets.bean.base.ServletBean;
import grenc.masters.servlets.finalreports.MatchstickTaskFileCreator;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class RetrieveDataServletBean implements ServletBean
{
	public static final String delimiter = ",";
	
	private static final String matchstickFilePath = "data/matchsticktask.csv";
	private static final String imageFilePath = "data/imagetask.csv";
	private static final String usersFilePath = "data/users.csv";
		
	
	@InsertBean
	private MatchstickTaskFileCreator matchstickTaskFileCreator;

	
	
	@Override
	public String url()
	{
		return "/data";
	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Do nothing
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws IOException, ServletException
	{
		File matchstickFile = createNewFile(servletContext, matchstickFilePath); 
		matchstickTaskFileCreator.prepareFile(matchstickFile);

		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<span><a href=\"" + matchstickFilePath + "\"><h3>Matchstick task data</h3></a></span>");
		out.println("<span><a href=\"" + imageFilePath + "\"><h3>Familiar figures task data</h3></a></span>");
		out.println("<span><a href=\"" + usersFilePath + "\"><h3>User data</h3></a></span>");

	}

	private File createNewFile(ServletContext servletContext, String path) throws IOException
	{
		String basePath = servletContext.getRealPath("/");
		File file = new File(basePath, path); 
		file.createNewFile();
		return file;
	}
	
	
}
