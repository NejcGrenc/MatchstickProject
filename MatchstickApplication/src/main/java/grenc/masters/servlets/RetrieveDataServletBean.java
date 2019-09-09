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
import grenc.masters.servlets.finalreports.DataFileCreator;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class RetrieveDataServletBean implements ServletBean
{
	public static final String delimiter = ",";
	
	private static final String directory = "data";
	private static final String taskDataPath = "data/taskData.csv";
		
	@InsertBean
	private DataFileCreator dataFileCreator;
	
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
		File file = createNewFile(servletContext, taskDataPath); 
		dataFileCreator.prepareFile(file);
		
		response.setContentType("text/html");
		response.setCharacterEncoding(Encoding.common);
		PrintWriter out = response.getWriter();
		out.println("<span><a href=\"" + taskDataPath + "\"><h3>Complete task data</h3></a></span>");

	}

	private File createNewFile(ServletContext servletContext, String path) throws IOException
	{
		String basePath = servletContext.getRealPath("/");
		File file = new File(basePath, path); 
		file.createNewFile();
		return file;
	}
	
	
}
