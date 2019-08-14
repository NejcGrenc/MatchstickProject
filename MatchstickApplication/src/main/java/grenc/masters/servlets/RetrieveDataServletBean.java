package grenc.masters.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.Encoding;
import grenc.masters.servlets.bean.base.ServletBean;
import grenc.masters.servlets.finalreports.FamiliarFiguresTaskFileCreator;
import grenc.masters.servlets.finalreports.MatchstickTaskFileCreator;
import grenc.masters.servlets.finalreports.UsersFileCreator;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class RetrieveDataServletBean implements ServletBean
{
	public static final String delimiter = ",";
	
	private static final String directory = "data";
	private static final String matchstickFilePath = "data/matchsticktask.csv";
	private static final String imageFilePath = "data/imagetask.csv";
	private static final String usersFilePath = "data/users.csv";
		
	
	@InsertBean
	private MatchstickTaskFileCreator matchstickTaskFileCreator;
	@InsertBean
	private FamiliarFiguresTaskFileCreator familiarFiguresTaskFileCreator;
	@InsertBean
	private UsersFileCreator usersFileCreator;
	
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
		new File(directory).mkdirs();
		File matchstickFile = createNewFile(servletContext, matchstickFilePath); 
		matchstickTaskFileCreator.prepareFile(matchstickFile);
		File familiarFiguresFile = createNewFile(servletContext, imageFilePath); 
		familiarFiguresTaskFileCreator.prepareFile(familiarFiguresFile);
		File usersFile = createNewFile(servletContext, usersFilePath); 
		usersFileCreator.prepareFile(usersFile);
		
		
		response.setContentType("text/html");
		response.setCharacterEncoding(Encoding.common);
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
