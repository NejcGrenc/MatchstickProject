package grenc.masters.servlets.selector;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.simpleton.annotation.Bean;


@Bean
public class ErrorMobileServletBean extends BasePageServlet
{
	
	@Override
	public String url()
	{
		return "/errorMobile";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Error Mobile");
		
		builder.addStyle(Style.background);
		builder.setBodyStyle("background");
		
		builder.addStyle(Style.style);
		builder.addStyle(Style.centered);
		builder.addStyle(Style.layout);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.buttons_credits);
		builder.addStyle(Style.split_page);
		builder.addStyle(Style.popup);

		builder.addScript(Script.send);
		builder.addScript(Script.translate);
		builder.addScript(Script.popup);
		builder.addScript(Script.translate_popup);
		builder.addScript(Script.send_return);

		builder.appendPageElement(new ApplicationFileSegment(servletContext, PageElement.error_mobile).getBaseText());
	}

	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext)
			throws IOException, ServletException
	{
		// Nothing to do here
	}
}