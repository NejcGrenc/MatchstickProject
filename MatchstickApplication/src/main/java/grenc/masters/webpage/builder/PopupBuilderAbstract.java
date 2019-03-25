package grenc.masters.webpage.builder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

public class PopupBuilderAbstract
{
	protected WebpageBuilder builder;
	private String basePath;
	
	public PopupBuilderAbstract(WebpageBuilder builder, ServletContext servletContext)
	{
		this.builder = builder;
		this.basePath = servletContext.getRealPath("/");
	}
	
	protected String readFile(String fileName)
	{
		StringBuilder content = new StringBuilder();
		try 
		{
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
