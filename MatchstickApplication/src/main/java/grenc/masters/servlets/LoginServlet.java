package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.SessionDAO;
import grenc.masters.database.SubjectDAO;
import grenc.masters.entities.Session;
import grenc.masters.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.uservalidation.ValidateUserSession;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;

public class LoginServlet extends BasePageServlet
{
	private static final long serialVersionUID = 2368890582418928946L;

	private SessionDAO sessionDAO;
	private SubjectDAO subjectDAO;
	
	public LoginServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
		this.subjectDAO = SubjectDAO.getInstance();
	}
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.LoginServlet;
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Login");
		
		builder.addStyle(Style.background);
		builder.setBodyStyle("background");
		
		builder.addStyle(Style.language_page);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.style);
		builder.addStyle(Style.input);	
		
		builder.addScript(Script.page_functions);
		builder.addScript(Script.send);
		

		String subjectName = (String) request.getAttribute("subjectName");
		if (isLoginWithExistingUser(subjectName))
		{
			builder.appendBodyScriptCommand("insertExistingName('" + subjectName + "');");
			builder.appendBodyScriptCommand("askForRepeated();");
		}
		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder).translate("m_welcome", "m_repeatedLogin").translateSpecial("m_nameInput", "placeholder");
		
		new DataPresentBall(builder, session).set();
		
		builder.appendPageElementFile(PageElement.login);
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		new ValidateUserSession(request).validate();
		
		String subjectName = (String) request.getAttribute("subjectName");
		if (subjectName == null || subjectName.isEmpty())
		{
			System.out.println("Did not login while on Login page! (probably clicked one of the up-right buttons)");
			return;
		}
		createLoginSubject(request);
	}
	
	private boolean isLoginWithExistingUser(String subjectName)
	{
		if (subjectName != null)
		{
			return subjectDAO.findSubjectsByNameAndComplete(subjectName, true).size() > 0;
		}
		return false;
	}


	private void createLoginSubject(HttpServletRequest request)
	{
		String subjectName = (String) request.getAttribute("subjectName");
		if (! isLoginWithExistingUser(subjectName))
		{
			createNewSubject(request);
		}
		else
		{
			boolean forceSelectedUser = Boolean.parseBoolean((String) request.getAttribute("forceSelectedUser"));
			boolean existingUser = Boolean.parseBoolean((String) request.getAttribute("existingUser"));
			
			if (forceSelectedUser)
			{
				int risk;
				if (existingUser)
				{
					loginExistingSubject(request);
					risk = 10;
				}
				else 
				{
					createNewSubject(request);
					risk = 1;
				}
				
				String session = (String) request.getAttribute("session");
				increaseSessionRisk(session, risk);
			}
		}
	}
	
	private void createNewSubject(HttpServletRequest request)
	{
		String sessionTag = (String) request.getAttribute("session");
		String subjectName = (String) request.getAttribute("subjectName");
		
		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - create new subject: " + subjectName);
		
		Session session = sessionDAO.findSessionByTag(sessionTag);
		Subject subject = subjectDAO.insertSubject(subjectName);
		
		sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());
	}
	
	// TODO find a better way
	private void loginExistingSubject(HttpServletRequest request)
	{
		String sessionTag = (String) request.getAttribute("session");
		String subjectName = (String) request.getAttribute("subjectName");
		
		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - add existing subject: " + subjectName);

		subjectDAO.insertSubject(subjectName);
		Session session = sessionDAO.findSessionByTag(sessionTag);
		Subject subject = subjectDAO.findSubjectsByNameAndComplete(subjectName, true).get(0);
		
		sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());
	}
	
	private void increaseSessionRisk(String sessionTag, int increasedRisk)
	{
		Session session = sessionDAO.findSessionByTag(sessionTag);
		int previousRisk = session.getRisk();
		int newRisk = previousRisk + increasedRisk;
		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - increasing risk to: " + newRisk);

		sessionDAO.updateSessionRisk(session.getId(), newRisk);
	}
	
}
