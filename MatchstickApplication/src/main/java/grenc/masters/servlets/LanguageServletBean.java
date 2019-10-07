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
import grenc.masters.webpage.common.ReturnableBean;
import grenc.masters.webpage.element.DataPresentBall;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class LanguageServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	
	@InsertBean
	private ReturnableBean returnableBean;
	@InsertBean
	private DataPresentBall dataPresentBall;
	
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
		
		returnableBean.prepareReturnTo(builder, request);
		
		builder.addScript(Script.send);		
		builder.addStyle(Style.language_page);
		builder.addStyle(Style.buttons);	
		builder.appendPageElementFile(PageElement.select_language);		
		
		dataPresentBall.set(builder, session);
	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		// Update session with language

		String sessionTag = (String) request.getAttribute("session");
		String lang = (String) request.getAttribute("lang");
		
		Session session = sessionDAO.findSessionByTag(sessionTag);
		logger.log(session, "Upsert");
		logger.log(session, " - for session: " + sessionTag);
		logger.log(session, " - with lang: " + lang);
		
		setLanguage(sessionTag, lang);
	}
	
	public void setLanguage(String sessionTag, String lang)
	{
		sessionDAO.updateSessionLang(sessionDAO.findSessionByTag(sessionTag).getId(), lang);
	}

}