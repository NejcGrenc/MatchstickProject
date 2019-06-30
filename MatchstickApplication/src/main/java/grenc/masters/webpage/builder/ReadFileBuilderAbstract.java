package grenc.masters.webpage.builder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

public class ReadFileBuilderAbstract
{
	
	protected String getBasePath(ServletContext servletContext)
	{
		return servletContext.getRealPath("/");
	}
	
	protected String readFile(ServletContext servletContext, String fileName)
	{
		StringBuilder content = new StringBuilder();
		try 
		{
			String basePath = getBasePath(servletContext);
		    BufferedReader in = new BufferedReader(new FileReader(basePath + fileName));
		    String line;
		    while ((line = in.readLine()) != null) 
		    {
		    	content.append(line);
		    }
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
	
}
