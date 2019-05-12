package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class LanguageServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	
	@Override
	public String url()
	{
		return "/selectLanguage";
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Select language");
		
		builder.addStyle(Style.background);
		builder.setBodyStyle("background");
		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		
		String returnUrl = (String) request.getAttribute("returnUrl");
		returnUrl = (returnUrl != null) ? returnUrl : "/";
		
		builder.addScript(Script.send);
		builder.appendHeadScriptCommand("var returnUrl = '" + returnUrl + "';");
		
		builder.addStyle(Style.language_page);
		builder.addStyle(Style.buttons);	
		builder.appendPageElementFile(PageElement.select_language);		
		
		new DataPresentBall(builder, session).set();
	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Update session with language

		String sessionTag = (String) request.getAttribute("session");
		String lang = (String) request.getAttribute("lang");
		
		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - with lang: " + lang);
		
		setLanguage(sessionTag, lang);
	}
	
	public void setLanguage(String sessionTag, String lang)
	{
		sessionDAO.updateSessionLang(sessionDAO.findSessionByTag(sessionTag).getId(), lang);
	}

}