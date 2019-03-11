package grenc.masters.servlets.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;

public class MatchstickTaskInfoPopup
{
	private final PageElement popupContentFile = PageElement.info_en;
	
	private WebpageBuilder builder;
	private String basePath;
	
	public MatchstickTaskInfoPopup(WebpageBuilder builder, ServletContext servletContext)
	{
		this.builder = builder;
		this.basePath = servletContext.getRealPath("/");
	}
	
	public void createPopup()
	{
		String content = readFile(popupContentFile.path());
		new Popup(builder, "infoPopup").setOpenButton("button-info").setWidth("35%").setText(content).addBottomCloseButton("m_closeButton", "Done").set();
	}
	
	private String readFile(String fileName)
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
