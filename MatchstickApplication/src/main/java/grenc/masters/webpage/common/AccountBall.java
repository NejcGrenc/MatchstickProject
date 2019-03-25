package grenc.masters.webpage.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

public class AccountBall extends CommonElement
{
	private Subject subject;
	private String basePath;
	
	public AccountBall(WebpageBuilder builder, Subject subject, ServletContext servletContext)
	{
		super(builder);
		this.subject = subject;
		this.basePath = servletContext.getRealPath("/");
	}

	public void set()
	{
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.popup);
		builder.appendPageElementFile(PageElement.account_ball);
		attachPopup();
	}
	
	private void attachPopup() {
		String content = readFile(PageElement.account_ball_popup.path());

		new Popup(builder, "popupName").setOpenButton("accountButton").setText(content).set();
		builder.appendBodyScriptCommand("setName('" + subject.getName() + "');");
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
