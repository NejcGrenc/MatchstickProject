package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;


public class CreditsServlet extends BasePageServlet
{
	private static final long serialVersionUID = 2368890582618928946L;

	private SessionDAO sessionDAO;
	private SubjectDAO subjectDAO;
	
	public CreditsServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
		this.subjectDAO = SubjectDAO.getInstance();
	}
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.CreditsServlet;
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Credits");
		
		builder.setBodyStyle("background");
		builder.addStyle(Style.background);
		
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.layout);
		builder.addStyle(Style.table);

		builder.addScript(Script.send);

		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new AccountBall(builder, subject, getServletContext()).set();
		
		builder.appendPageElementFile(PageElement.credits);
		
		new Translate(builder, Script.translate_credits).translateAll();
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		// Nothing to do here
	}

}
	
